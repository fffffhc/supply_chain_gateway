package com.scf.erdos.factoring.contract.server.impl;

import com.alibaba.fastjson.JSONObject;
import com.scf.erdos.factoring.contract.util.OSSTool;
import com.scf.erdos.factoring.contract.server.ProduceContractService;
import com.scf.erdos.factoring.contract.util.table.TableVarRenderImpl;
import com.scf.erdos.factoring.contract.model.*;
import com.scf.erdos.factoring.contract.model.businessData.*;
import com.scf.erdos.factoring.contract.util.ContractGenerator;
import com.scf.erdos.factoring.contract.util.WordToPdfUtil;
import com.scf.erdos.factoring.dao.ContractVarDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.*;

/**
 * @Description : 合同生成服务接口实现
 * @author：bao-clm
 * @date: 2020/8/13
 * @version：1.0
 */

@Service
@Slf4j
@SuppressWarnings("all")
public class ProduceContractServiceImpl implements ProduceContractService {

    @Autowired
    private ContractVarDao contractVarDao;
    @Autowired
    private TableVarRenderImpl customizeVarRender;

    /**
     * @Description : 生成融资相关合同
     * @author：bao-clm
     * @date: 2020-12-03
     * @version：1.0
     */
    @Override
    @Transactional
    public void generateFinancingProjectContract(Integer id) {

        /**
         * 通过融资id 获取所有相关信息
         */
        Financing financing = contractVarDao.getFinancing(id);//融资信息
        List<DSPledgeBill> dsPledgeBillList = contractVarDao.getDsPledgeBillList(id);//票据质押列表
        Product product = contractVarDao.getProduct(financing.getProductId()); //产品
        Credit credit = contractVarDao.getCreditInfo(financing.getCreditId());//授信信息
        List<ContractTemplate> contractTemplates = contractVarDao.getContractTemplates(financing.getProductId());//合同模板

        Integer receivableId = financing.getReceivableId();
        Receivable receivable = contractVarDao.getReceivable(receivableId);//应收账款
        List<BaseContract> contractList = contractVarDao.getContractList(receivableId); //合同列表
        List<Fapiao> fapiaoList = contractVarDao.getFapiaoList(receivableId); //发票列表
        List<OtherBill> otherBillList = contractVarDao.getOtherBillList(receivableId); //其它单据列表

        Company customerCompany = contractVarDao.getCompany(financing.getCustomerCompanyId()); // 融资企业
        Company fundingCompany = contractVarDao.getCompany(financing.getFundingCompanyId()); // 资金企业

        /**
         * 合同需要的参数，比如基础合同名称张数等数据
         */
        ContractParams contractParams = new ContractParams();
        contractParams.setBasicContractName(contractList.get(0).getContractName()); //基础合同名称（多个合同只取第一个合同名称）
        contractParams.setBasicContractSize(contractList.size()); //基础合同件数
        contractParams.setFapiaoSize(fapiaoList.size());//发票件数
        contractParams.setOtherBillSize(otherBillList.size());//其它票据件数
        contractParams.setReceivableDetailSize(fapiaoList.size() + otherBillList.size()); //应收账款明细件数（发票件数 + 其它票据件数）
        /**
         * 合同编号
         */
        contractParams.setContractAllCount("");
        contractParams.setContractCompanyCount("");
        contractParams.setHead(product.getHead());
        contractParams.setType(product.getType());
        contractParams.setProductType(product.getProductType());

        JSONObject context = new JSONObject()
                .fluentPut("financing", financing)
                .fluentPut("dsPledgeBillList", dsPledgeBillList)
                .fluentPut("product", product)
                .fluentPut("credit", credit)
                .fluentPut("product", product)
                .fluentPut("receivable", receivable)
                .fluentPut("contractList", contractList)
                .fluentPut("fapiaoList", fapiaoList)
                .fluentPut("otherBillList", otherBillList)
                .fluentPut("customerCompany", customerCompany)
                .fluentPut("fundingCompany",fundingCompany)
                .fluentPut("contractParams",contractParams)
                ;

        JSONObject mainShareParam = new JSONObject();
        ContractGenerator mainGenerator = new ContractGenerator(contractTemplates, context);
        mainGenerator.setTemplateVarRender(customizeVarRender);
        List<ContractVo> mainContractVos = mainGenerator.execute();

        /**
         * 开始生成主业务合同
         */
        for(ContractVo mainContractVo : mainContractVos) {
            //generatePdfUrl(mainContractVo);
        }
    }

    /**
     * @Description : 保存 OSS云 并保存到数据
     * @author：bao-clm
     * @date: 2020-08-17
     * @version：1.0
     */
    private String generatePdfUrl(ContractVo contractVo) {
        String renderHtml = contractVo.getRenderHtml();
        UserContract userContract = new UserContract();
        userContract.setContractName("xx");

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            WordToPdfUtil.wordToPdf(new ByteArrayInputStream(renderHtml.getBytes("UTF-8")), out);
            ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
            // 保存 OSS云
            String ossUrl = OSSTool.uploadStreamByOSS(userContract, in);
            userContract.setContractUrl(ossUrl);
            // 保存生成的合同信息
            contractVarDao.saveContract(userContract);
            return ossUrl;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
