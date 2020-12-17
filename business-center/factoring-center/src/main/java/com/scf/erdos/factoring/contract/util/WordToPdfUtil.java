package com.scf.erdos.factoring.contract.util;

import com.aspose.words.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @Description : word 转 pdf 工具类
 * @author：bao-clm
 * @date: 2020/8/14
 * @version：1.0
 */
public class WordToPdfUtil {
    /**
     * 获取 aspose 的证书，不然会有水印
     *
     * @return
     */
    public static boolean getLicense() {
        boolean result = false;
        try {
           /* String licenseStr = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?> \r\n" +
                    "<License>\r\n" +
                    "  <Data>\r\n" +
                    "    <Products>\r\n" +
                    "      <Product>Aspose.Total for Java</Product>\r\n" +
                    "      <Product>Aspose.Words for Java</Product>\r\n" +
                    "    </Products>\r\n" +
                    "    <EditionType>Enterprise</EditionType>\r\n" +
                    "    <SubscriptionExpiry>20991231</SubscriptionExpiry>\r\n" +
                    "    <LicenseExpiry>20991231</LicenseExpiry>\r\n" +
                    "    <SerialNumber>8bfe198c-7f0c-4ef8-8ff0-acc3237bf0d7</SerialNumber>\r\n" +
                    "  </Data>\r\n" +
                    "  <Signature>sNLLKGMUdF0r8O1kKilWAGdgfs2BvJb/2Xp8p5iuDVfZXmhppo+d0Ran1P9TKdjV4ABwAgKXxJ3jcQTqE/2IRfqwnPf8itN8aFZlV3TJPYeD3yWE7IT55Gz6EijUpC7aKeoohTb4w2fpox58wWoF3SNp6sK6jDfiAUGEHYJ9pjU=</Signature>\r\n" +
                    "</License>";

            InputStream is = new ByteArrayInputStream(licenseStr.getBytes("UTF-8"));
           // License aposeLic = new License();
           // aposeLic.setLicense(is);
            result = true;*/

            // 凭证
            String licenseStr =
                    "<License>\n"
                            + " <Data>\n"
                            + " <Products>\n"
                            + " <Product>Aspose.Total for Java</Product>\n"
                            + " <Product>Aspose.Words for Java</Product>\n"
                            + " </Products>\n"
                            + " <EditionType>Enterprise</EditionType>\n"
                            + " <SubscriptionExpiry>20991231</SubscriptionExpiry>\n"
                            + " <LicenseExpiry>20991231</LicenseExpiry>\n"
                            + " <SerialNumber>8bfe198c-7f0c-4ef8-8ff0-acc3237bf0d7</SerialNumber>\n"
                            + " </Data>\n"
                            + " <Signature>sNLLKGMUdF0r8O1kKilWAGdgfs2BvJb/2Xp8p5iuDVfZXmhppo+d0Ran1P9TKdjV4ABwAgKXxJ3jcQTqE/2IRfqwnPf8itN8aFZlV3TJPYeD3yWE7IT55Gz6EijUpC7aKeoohTb4w2fpox58wWoF3SNp6sK6jDfiAUGEHYJ9pjU=</Signature>\n"
                            + "</License>";
            InputStream license = new ByteArrayInputStream(
                    licenseStr.getBytes("UTF-8"));
            License asposeLic = new License();
            asposeLic.setLicense(license);
            result = true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * word（doc、docx） 转 pdf，使用的是aspose-words 来转的 <br/>
     * <b style="color:red">注意：linux转换的话，如果内容有中文，需要在服务器安装中文字体，否则显示小方框</b>
     *
     * @param wordInputStream
     * @param pdfOutputStream
     * @throws Exception
     */
    public static void wordToPdf(InputStream wordInputStream, OutputStream pdfOutputStream) throws Exception {
        if (!getLicense()) { // 验证License 若不验证则转化出的pdf文档会有水印产生
            System.out.println("证书验证失败");
        }
        try {
            long old = System.currentTimeMillis();
            Document doc = new Document(wordInputStream);
            FontSettings fontSettings = new FontSettings();
            // We can choose the default font to use in the case of any missing fonts.
            //FontSettings.setDefaultFontName("simsun");

            // For testing we will set Aspose.Words to look for fonts only in a folder which doesn't exist. Since Aspose.Words won't
            // Find any fonts in the specified directory, then during rendering the fonts in the document will be subsuited with the default
            // Font specified under FontSettings.DefaultFontName. We can pick up on this subsuition using our callback.
            // FontSettings.setFontsFolder("/usr/share/fonts", true);

            // Create a new class implementing IWarningCallback which collect any warnings produced during document save.
            /// Our callback only needs to implement the "Warning" method. This method is called whenever there is a
            /// Potential issue during document procssing. The callback can be set to listen for warnings generated during document
            /// Load and/or document save.
            IWarningCallback callback = info -> {
                // We are only interested in fonts being substituted.
                if (info.getWarningType() == WarningType.FONT_SUBSTITUTION) {
                    System.out.println("Font substitution: " + info.getDescription());
                }
            };
            doc.setWarningCallback(callback);
            //必须要有这句，否则转pdf时中文会因为字体问题而显示为方块。
            // 另外服务器需要上传中文字体到/usr/share/fonts目录（复制windows C:\Windows\Fonts目录下的字体文件即可），上传到其它目录也行，安装好就行
            doc.setFontSettings(fontSettings);

            doc.save(pdfOutputStream, SaveFormat.PDF);// 全面支持DOC, DOCX, OOXML, RTF HTML, OpenDocument, PDF,
            // EPUB, XPS, SWF 相互转换
            long now = System.currentTimeMillis();
            System.out.println("pdf转换成功，共耗时：" + ((now - old) / 1000.0) + "秒"); // 转化用时
        } finally {
            if (pdfOutputStream != null) {
                try {
                    pdfOutputStream.flush();
                    pdfOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
