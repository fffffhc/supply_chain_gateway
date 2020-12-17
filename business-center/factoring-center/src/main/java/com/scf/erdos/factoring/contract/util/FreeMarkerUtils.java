package com.scf.erdos.factoring.contract.util;

import freemarker.template.*;

import java.io.*;
import java.util.Map;



/**
 * @Description : FreeMarker
 * @author：bao-clm
 * @date: 2020/8/13
 * @version：1.0
 */
public class FreeMarkerUtils {
    private static Configuration configuration;

    /**
     * 生成Freemarker配置对象并设置模板读取路径
     */
    static {

        if (null == configuration) {
            configuration = new Configuration();

            configuration.setDefaultEncoding("UTF-8");

            configuration.setClassForTemplateLoading(FreeMarkerUtils.class, "/contractTemplate");

            // 设置对象的包装器
            configuration.setObjectWrapper(new DefaultObjectWrapper());
            // 设置异常处理器//这样的话就可以${a.b.c.d}即使没有属性也不会出错
            configuration.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);

            // 下面 2019-06-04 hanson 添加
            configuration.setTagSyntax(freemarker.template.Configuration.AUTO_DETECT_TAG_SYNTAX);
        }

    }


    /**
     * <pre>getTemplate(获取Freemarker模板)
     * 修改备注：
     * @param templateName 模板名称
     * @return
     */
    private static Template getTemplate(String templateName) throws IOException{

        Template template = configuration.getTemplate(templateName);
        template.setEncoding("UTF-8");

        return template;
    }

    /**
     * <pre>write(生成模板文件)
     * @param templateName 模板的名称
     * @param rootMap 模板中数据的集合</pre>
     */
    public static File write(String templateName, Map<String, Object> rootMap){
        File file = new File("tempFileName");
        try {
            Template template = getTemplate(templateName);
            Writer out = new OutputStreamWriter(new FileOutputStream(file),"utf-8");
            template.process(rootMap, out);
            out.close();
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 解析字符串模板
     * @param template  字符串模板
     * @param model 数据
     * @return 解析后内容
     */
    public static String process(String template, Map<String, ?> model){
        return process(template, model, configuration);
    }


    /**
     * 解析字符串模板
     * @param template  字符串模板
     * @param model 数据
     * @return 解析后内容
     */
    public static String process(String template, Map<String, ?> model,Configuration configuration){
        if (template == null) {
            return null;
        }
        StringWriter out = new StringWriter();
        try {
            new Template("template", new StringReader(template), configuration != null ? configuration : configuration).process(model, out);
        } catch (TemplateException | IOException e) {
            System.out.println("异常");
            if(configuration == throwExpConfiguration) {
                throw new RuntimeException(e);
            }
        }
        return out.toString();
    }

    /**
     * 根据ftl模板解析成字符串
     * @param templateName
     * @param model
     * @return
     */
    public static String processByFtl(String templateName, Map<String, Object> model) {
        return processByFtl(templateName, model, configuration);
    }

    /**
     * 根据ftl模板解析成字符串
     * @param templateName
     * @param model
     * @param configuration
     * @return
     */
    public static String processByFtl(String templateName, Map<String, Object> model, Configuration configuration){
        StringWriter out = new StringWriter();
        try {
            Template template = configuration.getTemplate(templateName);
            template.process(model, out);
            out.close();
        } catch (IOException | TemplateException e) {
            System.out.println("异常");
            if(configuration == throwExpConfiguration) {
                throw new RuntimeException(e);
            }
        }
        return out.toString();
    }

    /** 抛异常的配置 */
    private static Configuration throwExpConfiguration;

    /**
     * 生成Freemarker配置对象并设置模板读取路径
     */
    static {

        if (null == throwExpConfiguration) {
            throwExpConfiguration = new Configuration(Configuration.VERSION_2_3_22);

            throwExpConfiguration.setDefaultEncoding("UTF-8");

            throwExpConfiguration.setClassForTemplateLoading(FreeMarkerUtils.class, "/contractTemplate");

            // 设置对象的包装器
            throwExpConfiguration.setObjectWrapper(new DefaultObjectWrapper());
            // 设置异常处理器//这样的话就可以${a.b.c.d}即使没有属性也不会出错
            //throwExpConfiguration.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER); 不处理异常

            // 下面 2019-06-04 hanson 添加
            throwExpConfiguration.setTagSyntax(freemarker.template.Configuration.AUTO_DETECT_TAG_SYNTAX);

        }

    }

    public static Configuration getThrowExpConfiguration() {
        return throwExpConfiguration;
    }
}
