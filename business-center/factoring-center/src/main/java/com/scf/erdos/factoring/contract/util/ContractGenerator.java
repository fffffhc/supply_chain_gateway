package com.scf.erdos.factoring.contract.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.scf.erdos.factoring.contract.model.*;
import com.scf.erdos.factoring.contract.model.businessData.ContractTemplate;
import com.scf.erdos.factoring.contract.model.varItems.MsgNode;
import com.scf.erdos.factoring.contract.model.varItems.VarItems;
import com.scf.erdos.factoring.contract.util.table.TableVarRender;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description : 合同生成器
 * <p>模板生成合同设计思想 </br>
 * 1.业务服务提供  上下文环境（context）、合同共享参数（shareParam），便于渲染数据 </br>
 * 2.业务服务自定处理类
 * </p>
 * @author：bao-clm
 * @date: 2020/8/13
 * @version：1.0
 */

@Slf4j
@SuppressWarnings("all")
public class ContractGenerator {

    private TableVarRender tableVarRender;
    private List<ContractTemplate> contractTemplates;
    private JSONObject context;

    public ContractGenerator(List<ContractTemplate> contractTemplates, JSONObject context) {
        this.contractTemplates = contractTemplates;
        this.context = context;
    }

    public void setTemplateVarRender(TableVarRender tableVarRender) {
        this.tableVarRender = tableVarRender;
    }

    /**
     * 初始化环境
     */
    private void init() {
        WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
    }

    /**
     * 执行
     */
    public List<ContractVo> execute() {
        // 设置freemarker的日志级别  使他不要输出太多的日志
        init();
        List<ContractVo> contractVos = new LinkedList<>();
        for (ContractTemplate contractTemplate : contractTemplates) {
            contractVos.add(new ContractVo(contractTemplate));
        }
        // 开始填充合同参数
        for (ContractVo contractVo : contractVos) {
            fillMode(contractVo);
        }
        // 填充完了，开始生成合同
        return contractVos;
    }

    private void fillMode(ContractVo contractVo) {
        /**
         *  context - > 包含上下文变量和系统变量
         *  shareParam - >包含系统变量和业务变量
         *  param - >只包含业务变量
         */
        contractVo.setSourceHtml(contractVo.getContractTemplate().getSourceHtml()); //合同源文件xml
        /**
         * 合同占位符
         */
        List<MsgNode.VarGroup> contractVarGroups = JSON.parseArray(contractVo.getContractTemplate().getVarGroupListStr(), MsgNode.VarGroup.class);
        /**
         * 融资合同json文件数据
         */
        List<VarItems> jsonFileVarItems = PlaceholderJson.findAllVarItems();

        if (contractVarGroups != null) {
            List<MsgNode.VarItem> allBizVarItems = new ArrayList<>();
            for (MsgNode.VarGroup vg : contractVarGroups) {
                List<MsgNode.VarItem> varItems = vg.getVarItems();
                if (CollectionUtils.isEmpty(varItems)) {
                    continue;
                }
                for (MsgNode.VarItem vi : varItems) {
                    if (StringUtils.isBlank(vi.getKey())) {
                        continue;
                    }
                    /**
                     * 提取出字符串中${}里的内容
                     * 例如 "${a.b} | ${a+b} | ${id_xxx} | ${id_xxx001} ${业务编号}"
                     * 返回 "a.b" "a+b" "id_xxx" "id_xxx001" "业务编号"
                     */
                    List<String> refContractVarSns = extractVarByRegular(vi.getValue());
                    /**
                     * 渲染引用的系统变量的值
                     * jsonFileVarItems - > 业务数据
                     * refJsonFileVarSns - > 业务编号
                     */
                    renderRefVarSn(contractVo, jsonFileVarItems, refContractVarSns);
                }
                allBizVarItems.addAll(vg.getVarItems());
            }
            // 渲染业务定义变量的值
            renderBizVar(contractVo, jsonFileVarItems, allBizVarItems);
        }

        String sourceHtml = contractVo.getSourceHtml();
        // 现在开始处理html，需要把一些特殊标记先处理掉，比如是table的标记
        if (contractVo.getHtmlSpecialTags() != null) {
            sourceHtml = handleHtml(contractVo);
        }
        System.out.println(sourceHtml);

        JSONObject allParam = new JSONObject()
            // .fluentPutAll(this.shareParam) // 共享参数， # 顺序放最下面
            .fluentPutAll(this.context) // 上下文参数， # 放第二
            .fluentPutAll(contractVo.getSysParam()) // 跟此contractVo相关的系统参数， # 个性化最高不能被覆盖，放最上面
            ;
        for (String k : allParam.keySet()) {
            Object v = allParam.get(k);
            // 空的话 设为默认值
            if (v == null || ((v instanceof String) && StringUtils.isBlank((String) v))) {
                allParam.put(k, "___"); // 默认值为三个空格
            }
        }

        String renderHtml = FreeMarkerUtils.process(sourceHtml, allParam);
        contractVo.setRenderHtml(renderHtml);
        contractVo.setIsCompleted(true);
    }


    /*******************************************************************************************************************/
    /*******************************************************************************************************************/
    /*******************************************************************************************************************/
    /*******************************************************************************************************************/
    /*******************************************************************************************************************/
    /**
     * 提取出字符串中${}里的内容
     * 例如 "${a.b} | ${a+b} | ${id_xxx} | ${id_xxx001} ${业务编号}"
     * 返回 "a.b" "a+b" "id_xxx" "id_xxx001" "业务编号"
     */
    private List<String> extractVarByRegular(String str) {
        List<String> list = new ArrayList<String>();
        if (StringUtils.isBlank(str)) {
            return list;
        }

        String regex = "\\$\\{([^}]*)\\}";//匹配${}
        Matcher matcher = Pattern.compile(regex).matcher(str);
        while (matcher.find()) {
            String group = matcher.group();
            list.add(group.substring(2, group.length() - 1));
        }
        return list;
    }
    /*******************************************************************************************************************/
    /*******************************************************************************************************************/
    /*******************************************************************************************************************/
    /*******************************************************************************************************************/
    /*******************************************************************************************************************/
    /**
     * 渲染关联系统变量的值
     */
    private void renderRefVarSn(ContractVo contractVo, List<VarItems> jsonFileVarItems, List<String> refJsonFileVarSns) {
        if (CollectionUtils.isEmpty(refJsonFileVarSns)) {
            return;
        }

        JSONObject sysParam = contractVo.getSysParam();
        for (String refJsonFileVarSn : refJsonFileVarSns) {
            f:
            if (refJsonFileVarSn != null) {
                if (sysParam.containsKey(refJsonFileVarSn)) {
                    break f; // 说明之前有处理过这个sn了
                }
                if (jsonFileVarItems == null) {
                    sysParam.put(refJsonFileVarSn, "");
                    break f;
                }
                VarItems sysVarItem = jsonFileVarItems.stream().filter((v) -> v.getSn().equals(refJsonFileVarSn)).findFirst().orElse(null);
                if (sysVarItem == null) {
                    // 说明没有，不处理
                    sysParam.put(refJsonFileVarSn, "");
                    break f;
                }

                String value = null;
                // 表单数据渲染处理
                if (tableVarRender != null) {
                    value = tableVarRender.renderMode(context, contractVo, sysVarItem);
                    // 表示已经成功，不需要后续操作
                    if ("success".equals(value)) {
                        break f;
                    }
                    // 表示失败了，留给后面处理
                    else if ("failure".equals(value)) {
                        value = null;
                    }
                    // 否则就是返回  数据
                    else {
                        sysParam.put(refJsonFileVarSn, value);
                        break f;
                    }
                }
                // 说明是表格行
                if ("tr".equals(sysVarItem.getFun())) {
                    String replaceKey = StringUtils.isNotBlank(sysVarItem.getEl()) ? sysVarItem.getEl() : sysVarItem.getSn();
                    contractVo.addHtmlSpecialTag("tr", "${" + sysVarItem.getSn() + "}", replaceKey);
                    break f;
                } else if ("replace".equals(sysVarItem.getFun())) {
                    contractVo.addHtmlSpecialTag("replace", "${" + sysVarItem.getSn() + "}", sysVarItem.getEl());
                    break f;
                }

                // 开始渲染这个变量的值
                value = FreeMarkerUtils.process(sysVarItem.getEl(), context);
                sysParam.put(refJsonFileVarSn, value);
            }
        }
    }
    /*******************************************************************************************************************/
    /*******************************************************************************************************************/
    /*******************************************************************************************************************/
    /*******************************************************************************************************************/
    /*******************************************************************************************************************/
    private String handleHtml(ContractVo contractVo) {

        StringBuffer sb = new StringBuffer(contractVo.getSourceHtml());
        for (JSONObject o : contractVo.getHtmlSpecialTags()) {
            String fun = o.getString("fun");
            String key = o.getString("key");
            String replaceKey = o.getString("replaceKey");
            // 表格行变量
            if ("tr".equals(fun)) {
                int index = sb.indexOf(key);
                while (index > 0) {
                    int keyStart = index;
                    int keyEnd = keyStart + key.length();
                    sb.delete(keyStart, keyEnd); // 删除标记信息

                    int trStart = sb.substring(0, keyStart).lastIndexOf("<w:tr "); // 查询前一个<w:tr 的位置，注意这有个空格，因为有的标签也是<w:tr前缀
                    sb.insert(trStart, "[#list " + replaceKey + " as obj]");
                    int trEnd = sb.indexOf("</w:tr>", trStart) + "</w:tr>".length(); // 查询后一个</w:tr>的位置
                    sb.insert(trEnd, "[/#list]");


                    index = sb.indexOf(key);
                }
            }
            // 替换变量
            else if ("replace".equals(fun)) {
                int index = sb.indexOf(key);
                while (index > 0) {
                    int keyStart = index;
                    int keyEnd = keyStart + key.length();
                    sb.replace(keyStart, keyEnd, replaceKey);

                    index = sb.indexOf(key);
                }
            }
        }
        return sb.toString();
    }
    /*******************************************************************************************************************/
    /*******************************************************************************************************************/
    /*******************************************************************************************************************/
    /*******************************************************************************************************************/
    /*******************************************************************************************************************/
    /**
     * 渲染业务变量（业务人员设定的）的值，值存进 shareParam里
     */
    private void renderBizVar(ContractVo contractVo, List<VarItems> sysVarItems, List<MsgNode.VarItem> bizVarItems) {
        if (CollectionUtils.isEmpty(bizVarItems)) {
            return;
        }

        JSONObject allParam = new JSONObject()
                .fluentPutAll(this.context) // 上下文参数， # 放第二
                .fluentPutAll(contractVo.getSysParam()) // 跟此contractVo相关的系统参数， # 个性化最高不能被覆盖，放最上面
                ;

        List<MsgNode.VarItem> bizVarItemsTemp = new ArrayList<>();
        bizVarItemsTemp.addAll(bizVarItems);
        Iterator<MsgNode.VarItem> iterator = bizVarItemsTemp.iterator();
        while (iterator.hasNext()) {
            MsgNode.VarItem bizVarItem = iterator.next();
            if (StringUtils.isBlank(bizVarItem.getKey())) {
                iterator.remove();
                continue;
            }

            String s = null;
            boolean ok = true;
            f:
            {
                if (StringUtils.isBlank(bizVarItem.getValue())) {
                    s = "";
                    break f;
                }
                try {
                    s = FreeMarkerUtils.process(bizVarItem.getValue(), allParam, FreeMarkerUtils.getThrowExpConfiguration());
                } catch (Exception e) {
                    s = null;
                    ok = false;
                }
            }
            if (ok) {
                allParam.put(bizVarItem.getKey(), s);
                contractVo.getParam().put(bizVarItem.getKey(), s);
                iterator.remove();
            }
        }
    }

}
