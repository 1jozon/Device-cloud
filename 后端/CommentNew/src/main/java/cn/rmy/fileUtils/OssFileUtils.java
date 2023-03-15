package cn.rmy.fileUtils;

import cn.rmy.common.beans.Instrument;
import cn.rmy.common.config.OssConfig;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.*;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.varia.StringMatchFilter;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;


/**
 * 文件工具
 *
 * @author chu
 * @date 2021/11/09
 */
@Component
public class OssFileUtils {

    private OssConfig ossConfig;
    @Autowired
    public void getOssConfig(OssConfig ossConfig) {
        this.ossConfig = ossConfig;
    }

    /**
     * 默认图片最大大小
     */
    private long DEFAULT_MAX_SIZE = 200 * 1024 * 1024;

    /**
     * 默认的文件名长度
     */
    private int DEFAULT_FILE_NAME_LENGTH = 100;

    /**
     * 默认过期时间 3年
     */
    private Long DEFAULT_EXPIRATION_TIME = 3600L * 1000 * 24 * 365 * 3;

    /*
    * 分片上传时每个分片的大小
    * */
    private final long partSize = 1 * 1024 * 1024L;


    /**
     * 检查文件路径，不存在创建
     *
     * @param path 路径
     */
    public void checkPath(String path){
        if (!new File(path).exists()){
            new File(path).mkdir();
        }
    }

    /**
     * 得到文件类型
     *
     * @param file 文件
     * @return {@link String}
     */
    public String getFileType(MultipartFile file){
        if (file == null){
            return StringUtils.EMPTY;
        }
        return getFileType(file.getName());
    }

    /**
     * 得到文件类型
     *
     * @param fileName 文件名称
     * @return {@link String}
     */
    public String getFileType(String fileName){
        int separatorIndex = fileName.lastIndexOf(".");
        if (separatorIndex < 0){
            return "";
        }
        return fileName.substring(separatorIndex + 1).toLowerCase();
    }

    /**
     * 检查允许下载
     *
     * @param resource 资源
     * @return boolean
     */
    public boolean checkAllowDownload(String resource)
    {
        // 禁止目录上跳级别
        if (StringUtils.contains(resource, ".."))
        {
            return false;
        }

        // 检查允许下载的文件规则
        if (ArrayUtils.contains(MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION, getFileType(resource)))
        {
            return true;
        }

        // 不在允许下载的文件规则
        return false;
    }

    /**
     * 获取文件名的后缀
     *
     * @param file 文件
     * @return {@link String}
     */
    public String getExtension(MultipartFile file){
        String extension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.'));
        if (StringUtils.isEmpty(extension)){
            extension = MimeTypeUtils.getExtension(file.getContentType());
        }
        return extension;
   }

    /**
     * 允许文件大小
     *
     * @param file 文件
     * @throws Exception 异常
     */
    public boolean allowedSize(MultipartFile file){
        long size = file.getSize();
        if (DEFAULT_MAX_SIZE != -1 && size > DEFAULT_MAX_SIZE){
            return false;
        }
        return true;
    }

    /**
     * 获取文件名
     *
     * @param file 文件
     * @return {@link String}
     */
    public String getFilename(MultipartFile file){
        String filename = file.getOriginalFilename().substring(0,file.getOriginalFilename().lastIndexOf("."));
        return filename;
    }

    /**
     * 通过ossUrl获取文件名
     *
     * @param url url
     * @return {@link String}
     */
    public String getFilename(String url){
        String filename1 = StringUtils.substringAfter(url,".com/");
        String filename = StringUtils.substringBefore(filename1,"?Expires");
        return filename;
    }

    /**
     * 编辑文件名
     *
     * @param file 文件
     * @return {@link String}
     */
    public String extractFilename(MultipartFile file){
        String extension = getExtension(file);

        String filename = DateUtils.datePath() + "/" + IdUtils.fastUUID() + "." + extension;
        return filename;
    }

    /**
     * 上传文件
     *
     * @param file       文件
     * @param type       类型
     * @param uuidSign   uuid的迹象
     * @param pkgVersion pkg版本 != null 为上传升级包
     * @return {@link String}
     * @throws Exception 异常
     */
    public String uploadFile(MultipartFile file, Integer type, Integer uuidSign, String pkgVersion)throws Exception{

        //判断文件大小
        if (!allowedSize(file)){
            throw new Exception();
        }

        String accessId = ossConfig.getAccessId();
        String bucket = ossConfig.getBucket();
        String accessKey = ossConfig.getAccessKey();
        String endpoint = ossConfig.getEndpoint();

        OSS ossClient = new OSSClientBuilder().build(endpoint, accessId, accessKey);

        //用户上传文件时指定的前缀
        String dir = "";
        switch (type){
            case 0:
                //头像
                dir = "avatar/";
                break;
            case 1:
                //图标
                dir = "icon/";
                break;
            case 2:
                //轮播图
                dir = "pictures/";
                break;
            case 3:
                //升级包
                dir = "upgradeBags/";
                break;
            case 4:
                //其他
                dir = "others/";
                break;
        }

        String oldFilename = getFilename(file);
        String newFilename = null;
        String ext = getExtension(file);
        String date = DateUtils.dateTime();
        String uuid = UUID.randomUUID().toString().replace("_","");

        //是否加密文件名（不加密原名上传）
        if (pkgVersion != null && pkgVersion.length() > 0) {
            newFilename = dir + pkgVersion + "/" + oldFilename + ext;
        }else if (uuidSign == 1 && pkgVersion == null && pkgVersion.length() == 0){
            newFilename = dir + date + "/" + oldFilename + ext;
        }else {
            newFilename = dir + date + "/" + uuid + ext;
        }

        String url2 = "";
        //通过ossClient来获取上传文件后的url
        try{
            ossClient.putObject(bucket,newFilename,new ByteArrayInputStream(file.getBytes()));
            //设置url有效期
            Date dateExpiration = new Date(System.currentTimeMillis() + DEFAULT_EXPIRATION_TIME);
            URL url = ossClient.generatePresignedUrl(bucket,newFilename,dateExpiration);

            //url = ossConfig.getHost() + "/" + filename;
            url2 = url.toString();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            ossClient.shutdown();
        }
        return url2;
    }


    /**
     * 通过url删除oss仓库内文件
     *
     * @param url url
     * @return boolean
     */
    public boolean deleteFile(String url){
        if(url == null || url.length() == 0){
            return true;
        }
        boolean flag = false;
        String accessId = ossConfig.getAccessId();

        String bucket = ossConfig.getBucket();

        String accessKey = ossConfig.getAccessKey();

        String endpoint = ossConfig.getEndpoint();
        OSS ossClient = new OSSClientBuilder().build(endpoint,accessId,accessKey);

        String filename = getFilename(url);

        try{
            ossClient.deleteObject(bucket,filename);
            flag = true;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            ossClient.shutdown();
        }
        return flag;
    }

    /**
     * 删除多个文件
     *
     * @param urls url
     * @return boolean
     */
    public boolean deleteFiles(List<String> urls){
         boolean flag = false;
         for (String url : urls){
              flag = deleteFile(url);
             if (!flag){
                 break;
             }
         }
         return flag;
    }

    /**
     * 获取oss前缀列表文件
     * type 0-头像文件、1-图标文件、2-图片文件、3-升级包文件、4-其他文件
     * @param type 类型
     * @return {@link List}<{@link OSSObjectSummary}>
     */
    public List<OSSObjectSummary> listOssPrefixFile(Integer type){

        String accessId = ossConfig.getAccessId();
        String bucket = ossConfig.getBucket();
        String accessKey = ossConfig.getAccessKey();
        String endpoint = ossConfig.getEndpoint();
        OSS ossClient = new OSSClientBuilder().build(endpoint,accessId,accessKey);
        //用户上传文件时指定的前缀
        String dir = null;
        switch (type){
            case 0:
                //头像
                dir = "avatar/";
                break;
            case 1:
                //图标
                dir = "icon/";
                break;
            case 2:
                //轮播图
                dir = "pictures/";
                break;
            case 3:
                //升级包
                dir = "upgradeBags/";
                break;
            case 4:
                //其他
                dir = "others/";
                break;
        }
        // 列举文件。如果不设置keyPrefix，则列举存储空间下的所有文件。如果设置keyPrefix，则列举包含指定前缀的文件。
        ObjectListing objectListing = ossClient.listObjects(bucket,dir);
        List<OSSObjectSummary> list = objectListing.getObjectSummaries();

        return list;
    }



    /**
     * 上传文件
     *
     * @param file       文件
     * @param type       类型
     * @param uuidSign   uuid的迹象
     * @param pkgVersion pkg版本 != null 为上传升级包
     * @return {@link String}
     * @throws Exception 异常
     */
    public String uploadFileMultipart(MultipartFile file, Integer type, Integer uuidSign, String pkgVersion)throws Exception{

        //判断文件大小
        if (!allowedSize(file)){
            throw new Exception();
        }

        String accessId = ossConfig.getAccessId();
        String bucket = ossConfig.getBucket();
        String accessKey = ossConfig.getAccessKey();
        String endpoint = ossConfig.getEndpoint();

        OSS ossClient = new OSSClientBuilder().build(endpoint, accessId, accessKey);

        //用户上传文件时指定的前缀
        String dir = "";
        switch (type){
            case 0:
                //头像
                dir = "avatar/";
                break;
            case 1:
                //图标
                dir = "icon/";
                break;
            case 2:
                //轮播图
                dir = "pictures/";
                break;
            case 3:
                //升级包
                dir = "upgradeBags/";
                break;
            case 4:
                //其他
                dir = "others/";
                break;
        }

        String oldFilename = getFilename(file);
        String newFilename = null;
        String ext = getExtension(file);
        String date = DateUtils.dateTime();
        String uuid = UUID.randomUUID().toString().replace("_","");

        //是否加密文件名（不加密原名上传）
        if (pkgVersion != null && pkgVersion.length() > 0) {
            newFilename = dir + pkgVersion + "/" + oldFilename + ext;
        }else if (uuidSign == 1 && pkgVersion == null && pkgVersion.length() == 0){
            newFilename = dir + date + "/" + oldFilename + ext;
        }else {
            newFilename = dir + date + "/" + uuid + ext;
        }

        String url2 = "";
        //通过ossClient来获取上传文件后的url
        try{
            ossClient.putObject(bucket,newFilename,new ByteArrayInputStream(file.getBytes()));
            //设置url有效期
            Date dateExpiration = new Date(System.currentTimeMillis() + DEFAULT_EXPIRATION_TIME);
            URL url = ossClient.generatePresignedUrl(bucket,newFilename,dateExpiration);

            //url = ossConfig.getHost() + "/" + filename;
            url2 = url.toString();

            // 分片上传
            InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(bucket, newFilename);
            InitiateMultipartUploadResult uploadResult = ossClient.initiateMultipartUpload(request);
            String uploadId = uploadResult.getUploadId();

            List<PartETag> partETags = new ArrayList<PartETag>();
            long fileLength = file.getSize();
            int partCount = (int) (fileLength/partSize);
            if(fileLength % partSize != 0){
                partCount++;
            }
            // 遍历分片上传
            for(int i = 0; i < partCount; i++){
                long startPos = i * partSize;
                long curPartSize = (i + 1 == partCount) ? (fileLength - startPos) : partSize;

                File file1 = transFille(file);
                InputStream inStream = new FileInputStream(file1);
                inStream.skip(startPos);
                UploadPartRequest uploadPartRequest = new UploadPartRequest();
                uploadPartRequest.setBucketName(bucket);
                uploadPartRequest.setKey(newFilename);
                uploadPartRequest.setUploadId(uploadId);
                uploadPartRequest.setInputStream(inStream);
                uploadPartRequest.setPartSize(curPartSize);
                uploadPartRequest.setPartNumber(i + 1);
                UploadPartResult uploadPartResult = ossClient.uploadPart(uploadPartRequest);
                // 每次上传分片之后，oss的返回结果包括PartETag
                partETags.add(uploadPartResult.getPartETag());
            }
            // 在执行完成分片上传操作时，需要提供所有有效的partETags。OSS收到提交的partETags后，会逐一验证每个分片的有效性。当所有的数据分片验证通过后，OSS将把这些分片组合成一个完整的文件。
            CompleteMultipartUploadRequest completeMultipartUploadRequest = new CompleteMultipartUploadRequest(bucket, newFilename, uploadId, partETags);
            // 完成分片上传
            CompleteMultipartUploadResult completeMultipartUploadResult = ossClient.completeMultipartUpload(completeMultipartUploadRequest);

        }catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return url2;
    }

    /*
    * 将后台接收的 MultipartFile 转换成 File
     */
    public File transFille(MultipartFile multipartFile) throws Exception{
        int n;
        // String path = "\\home\\temFileSavePath\\";
        String path = "C:\\\\Users\\\\Administrator\\\\Desktop\\\\file\\\\";
        File file = new File(multipartFile.getOriginalFilename());
        // 文件流转换
        try (InputStream in = multipartFile.getInputStream(); OutputStream os = new FileOutputStream(file)){
            byte[] buffer = new byte[4096];
            while((n = in.read(buffer, 0, 4096)) != -1){
                os.write(buffer, 0, 4096);
            }
            // 读取文件第一行
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            bufferedReader.close();
        } catch (IOException e){
            e.printStackTrace();
        }
        return file;
    }

}
