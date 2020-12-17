package com.scf.erdos.factoring.contract.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import com.scf.erdos.factoring.contract.model.businessData.UserContract;
import com.scf.erdos.factoring.util.TimeUtil1;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;

/**
 * @Description : 阿里云OSS 文件上传
 * @author：bao-clm
 * @date: 2020/8/12
 * @version：1.0
 */

@SuppressWarnings("all")
public class OSSTool {

    private static String ENDPOINT = "http://oss-cn-zhangjiakou.aliyuncs.com/";
    private static String ACCESS_KEY_ID = "LTAI4FveXBzWUM4TytNGGsQj";
    private static String ACCESS_KEY_SECRET = "iFjn0A5VLaNxMaVINAn4SsV6lBAEIH";
    private static String BUCKET_NAME = "erdos-scf-platform-test";

    /*
     * description: 上传文件公用方法 </br>
     * auther:      zcq </br>
     * date:        2018-12-07 12:55
     *
     * @param userFile
     * @param targetFile
     * @return java.lang.String
     */
    public static String uploadSingleFileByOSS(UserContract userFile, File targetFile) {
       /* if (targetFile != null) {
            String fileName = userFile.getDescription();
            if (fileName != null && !fileName.trim().equals("")) {
                String url = PropertiesUtil.getProperty("uploadPath");
                String format = TimeUtil1.dateFormatSlash(TimeUtil1.getNowTimestampMillis());
                String path = url + format + "/" + userFile.getFullNumber();
                String OSS_url = "";
                String hz = fileName.substring(fileName.lastIndexOf("."));
                String newName = UUID.randomUUID() + hz;
                try {
                    OSS_url = OSSTool.uploadFileByOSS(path + "/" + newName, targetFile);
                    // 上传文件失败判断
                    if (OSS_url.equals("error")) {
                        throw new Exception("oss文件上传失败");
                    }
                } catch (Exception e) {
                    throw new RuntimeException();
                }
                return OSS_url;
            }
        }*/
        return null;
    }

    /**
     * putObject()
     *
     * @param key  保存在oss上的文件名(包括路径)
     * @param file
     * @return
     * @throws FileNotFoundException
     * @author zhangyiying
     * @date 2016年11月14日 上午9:47:19
     */
    public static String uploadFileByOSS(String key, File file) throws FileNotFoundException {
        // key指的是 保存在oss上后的路径+文件名
        // filePath 指的是上传的文件路径
        OSSClient client = null;
        // 参数设置
        // 关于这个endPoint，可以参考：http://bbs.aliyun.com/read/149100.html?spm=5176.7189909.0.0.YiwiFw
        String endpoint = ENDPOINT;// 青岛的接口
        String accessKeyId = ACCESS_KEY_ID;
        String accessKeySecret = ACCESS_KEY_SECRET;
        String bucketName = BUCKET_NAME;
        // String key="peju/url1.jpg";//保存在oss上的文件名
        // String filePath="d://架构.png";//本地或者服务器上文件的路径
        String OOS_url = "";
        try {
            client = new OSSClient(endpoint, accessKeyId, accessKeySecret);
            // 获取指定文件的输入流
            // File file = new File(filePath);
            InputStream content = new FileInputStream(file);
            // 创建上传Object的Metadata
            ObjectMetadata meta = new ObjectMetadata();
            // 必须设置ContentLength
            meta.setContentLength(file.length());
            // 上传Object.
            PutObjectResult result = client.putObject(bucketName, key, content, meta);
            if (file.isFile() && file.exists()) {
                // file.delete();
                String[] endpointArray = endpoint.split("//");
                System.out.println(endpointArray[0] + "//" + bucketName + "." + endpointArray[1] + key);
                OOS_url = endpointArray[0] + "//" + bucketName + "." + endpointArray[1] + key;
            }
            return OOS_url;
        } catch (OSSException oe) {
            System.out.println("Error getHeader: " + oe.getHeader());
            System.out.println("Error getLocalizedMessage: " + oe.getLocalizedMessage());
            System.out.println("Error Message: " + oe.getMessage());
            System.out.println("Error Code:       " + oe.getErrorCode());
            System.out.println("Request ID:      " + oe.getRequestId());
            System.out.println("Host ID:           " + oe.getHostId());
            OOS_url = "error";
            return OOS_url;
        }
        // 打印ETag
        // System.out.println(result.getETag());
    }


    /**
     * 流的形式上传
     *
     * @param userFile
     * @param inputStream
     * @return
     * @author hanson
     * @date 2020年02月22日
     */
    public static String uploadStreamByOSS(UserContract userContract, InputStream inputStream) {
        if (inputStream != null) {
            String fileName = userContract.getContractName();
            if (fileName != null && !fileName.trim().equals("")) {
                String format = TimeUtil1.dateFormatSlash(TimeUtil1.getNowTimestampMillis());
                String path = format + "/" + fileName;
                String OSS_url = "";
                String newName = UUID.randomUUID() + ".pdf";
                try {
                    OSS_url = OSSTool.uploadStreamByOSS(path + "/" + newName, inputStream);
                    // 上传文件失败判断
                    if (OSS_url.equals("error")) {
                        throw new Exception("oss文件上传失败");
                    }
                } catch (Exception e) {
                    throw new RuntimeException();
                }
                return OSS_url;
            }
        }
        return null;
    }

    /**
     * 流的形式上传
     *
     * @param key         保存在oss上的文件名(包括路径)
     * @param inputStream
     * @return
     * @author hanson
     * @date 2020年02月22日
     */
    public static String uploadStreamByOSS(String key, InputStream inputStream) {
        // key指的是 保存在oss上后的路径+文件名
        // filePath 指的是上传的文件路径
        OSSClient client = null;
        // 参数设置
        // 关于这个endPoint，可以参考：http://bbs.aliyun.com/read/149100.html?spm=5176.7189909.0.0.YiwiFw
        String endpoint = ENDPOINT;// 青岛的接口
        String accessKeyId = ACCESS_KEY_ID;
        String accessKeySecret = ACCESS_KEY_SECRET;
        String bucketName = BUCKET_NAME;
        // String key="peju/url1.jpg";//保存在oss上的文件名
        // String filePath="d://架构.png";//本地或者服务器上文件的路径
        String OOS_url = "";
        try {
            client = new OSSClient(endpoint, accessKeyId, accessKeySecret);
            // 获取指定文件的输入流
            // File file = new File(filePath);
            InputStream content = inputStream;
            // 创建上传Object的Metadata
            ObjectMetadata meta = new ObjectMetadata();
            // 必须设置ContentLength
            meta.setContentLength(inputStream.available());
            // 上传Object.
            PutObjectResult result = client.putObject(bucketName, key, content, meta);
            {
                // file.delete();
                String[] endpointArray = endpoint.split("//");
                System.out.println(endpointArray[0] + "//" + bucketName + "." + endpointArray[1] + key);
                OOS_url = endpointArray[0] + "//" + bucketName + "." + endpointArray[1] + key;
            }
            return OOS_url;
        } catch (OSSException oe) {
            System.out.println("Error getHeader: " + oe.getHeader());
            System.out.println("Error getLocalizedMessage: " + oe.getLocalizedMessage());
            System.out.println("Error Message: " + oe.getMessage());
            System.out.println("Error Code:       " + oe.getErrorCode());
            System.out.println("Request ID:      " + oe.getRequestId());
            System.out.println("Host ID:           " + oe.getHostId());
            OOS_url = "error";
            return OOS_url;
        } catch (IOException e) {
            e.printStackTrace();
            return OOS_url;
        }
        // 打印ETag
        // System.out.println(result.getETag());
    }


    /**
     * downloadFile() 下载文件到本地磁盘上
     *
     * @param key          文件名(带文件夹)
     *                     例如文件在oss的路径为:http://39.104.20.192/font
     *                     /ARIALUNI.TTF 则key = font/ARIALUNI.TTF
     * @param downloadPath 文件要下载到磁盘中的位置 "G:/test/photo1.jpg"(test文件夹需存在)
     * @author zhangyiying
     * @date 2017年3月29日 下午8:53:13
     */
    public static void downloadFile(String key, String downloadPath) {
        try {
            OSSClient client = null;
            String endpoint = ENDPOINT;// 青岛的接口
            String accessKeyId = ACCESS_KEY_ID;
            String accessKeySecret = ACCESS_KEY_SECRET;
            String bucketName = BUCKET_NAME;
            client = new OSSClient(endpoint, accessKeyId, accessKeySecret);
            client.getObject(new GetObjectRequest(bucketName, key), new File(downloadPath));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据key获取OSS服务器上的文件
     *
     * @param diskName 文件路径
     * @param key      Bucket下的文件的路径名+文件名
     */
    public static final byte[] getOSS2Byte(String diskName, String key) {
        try {
            OSSClient client = null;
            String endpoint = ENDPOINT;// 青岛的接口
            String accessKeyId = ACCESS_KEY_ID;
            String accessKeySecret = ACCESS_KEY_SECRET;
            String bucketName = BUCKET_NAME;
            client = new OSSClient(endpoint, accessKeyId, accessKeySecret);
            OSSObject ossObj = client.getObject(bucketName, diskName + key);
            ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
            byte[] buff = new byte[100];
            int rc = 0;
            while ((rc = ossObj.getObjectContent().read(buff, 0, 100)) > 0) {
                swapStream.write(buff, 0, rc);
            }
            byte[] bt = swapStream.toByteArray();
            return bt;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public static String uploadFileByOSSTest() throws FileNotFoundException {
        // key指的是 保存在oss上后的路径+文件名
        // filePath 指的是上传的文件路径
        OSSClient client;
        // 参数设置
        // 关于这个endPoint，可以参考：http://bbs.aliyun.com/read/149100.html?spm=5176.7189909.0.0.YiwiFw
        String endpoint = ENDPOINT;// 青岛的接口
        String accessKeyId = ACCESS_KEY_ID;
        String accessKeySecret = ACCESS_KEY_SECRET;
        String bucketName = BUCKET_NAME;
        String key = "zyy_2.jpg";// 保存在oss上的文件名
        String filePath = "d://2.jpg";// 本地或者服务器上文件的路径
        String OOS_url = "";
        try {
            client = new OSSClient(endpoint, accessKeyId, accessKeySecret);
            // 获取指定文件的输入流
            File file = new File(filePath);
            InputStream content = new FileInputStream(file);
            // 创建上传Object的Metadata
            ObjectMetadata meta = new ObjectMetadata();
            // 必须设置ContentLength
            meta.setContentLength(file.length());
            // 上传Object.
            PutObjectResult result = client.putObject(bucketName, key, content, meta);
            if (file.isFile() && file.exists()) {
                // file.delete();
                String[] endpointArray = endpoint.split("//");
                System.out.println(endpointArray[0] + "//" + bucketName + "." + endpointArray[1] + "/" + key);
                OOS_url = endpointArray[0] + "//" + bucketName + "." + endpointArray[1] + key;
            }
        } catch (OSSException oe) {
            System.out.println("Error Message: " + oe.getErrorCode());
            System.out.println("Error Code:       " + oe.getErrorCode());
            System.out.println("Request ID:      " + oe.getRequestId());
            System.out.println("Host ID:           " + oe.getHostId());
            OOS_url = "error";
        } finally {
            return OOS_url;
        }
    }

    public static void main(String[] args) {
        String fileName = "afas.pdf";
        String hz = fileName.substring(fileName.lastIndexOf("."));
        System.out.println(hz);
    }

    /**
     * MultipartFile2File() MultipartFile转File文件
     *
     * @param file
     * @return
     * @author zhangyiying
     * @date 2016年11月14日 上午11:21:45
     */
    public static File MultipartFile2File(MultipartFile file) {
        File f = null;
        if (file != null) {
            CommonsMultipartFile cf = (CommonsMultipartFile) file;
            DiskFileItem fi = (DiskFileItem) cf.getFileItem();
            f = fi.getStoreLocation();
        }
        return f;
    }

}
