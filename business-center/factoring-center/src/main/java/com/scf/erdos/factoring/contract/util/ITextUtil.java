package com.scf.erdos.factoring.contract.util;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.resolver.font.DefaultFontProvider;
import com.itextpdf.layout.font.FontProvider;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Description : 工具类（pdf转换）
 * @author：bao-clm
 * @date: 2020/8/13
 * @version：1.0
 */

@SuppressWarnings("all")
public class ITextUtil {

    /**
     * html转pdf文件
     *
     * @param html
     * @param pdfOutStream
     */
    public static void htmlToPdf(String html, OutputStream pdfOutStream) {
        // pdfHTML specific code
        ConverterProperties converterProperties = new ConverterProperties();
        try {
            setFont(converterProperties);
            HtmlConverter.convertToPdf(html, pdfOutStream, converterProperties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * html转pdf文件
     *
     * @param htmlInputStream
     * @param pdfOutStream
     */
    public static void htmlToPdf(InputStream htmlInputStream, OutputStream pdfOutStream) {
        // pdfHTML specific code
        ConverterProperties converterProperties = new ConverterProperties();
        try {
            setFont(converterProperties);
            HtmlConverter.convertToPdf(htmlInputStream, pdfOutStream, converterProperties);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 设置字体
     */
    private static void setFont(ConverterProperties converterProperties) {
        // 设置字体
        FontProvider fontProvider = new DefaultFontProvider();
        try {
            // TODO 字体待切换成懒加载，加载一次就够了
            ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            //Resource[] resources = resolver.getResources("classpath*:itext/fonts/*.*");
            Resource[] resources1 = resolver.getResources("classpath*:itext/fonts/STSONG.TTF"); // 华文宋体
            Resource[] resources2 = resolver.getResources("classpath*:itext/fonts/SIMFANG.TTF"); // 仿宋
            //Resource[] resources3 = resolver.getResources("classpath*:itext/fonts/STFANGSO.TTF"); // 华文仿宋
            //Resource[] resources4 = resolver.getResources("classpath*:itext/fonts/SIMSUN.TTC"); // 宋体
            List<Resource> rs = new ArrayList<>();
            Collections.addAll(rs, resources1);
            Collections.addAll(rs, resources2);
            //Collections.addAll(rs, resources3);
            //Collections.addAll(rs, resources4);
            for (Resource r : rs) {
                byte[] bytes = IOUtils.toByteArray(r.getInputStream());
                fontProvider.addFont(bytes);
            }
            //fontProvider.addDirectory(dir)
            //fontProvider.addSystemFonts();
            converterProperties.setFontProvider(fontProvider);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
