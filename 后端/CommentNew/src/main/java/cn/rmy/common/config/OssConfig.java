package cn.rmy.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * oss配置设置
 *
 * @author chu
 * @date 2021/11/08
 */
@Component
@ConfigurationProperties(prefix = "oss")
public class OssConfig {

    @Value("${oss.access-id}")
    private String accessId;

    @Value("${oss.bucket}")
    private String bucket;

    @Value("${oss.endpoint}")
    private String endpoint;

    @Value("${oss.access-key}")
    private String accessKey;

    @Value("${oss.host}")
    private String host;

    public String getAccessId(){
        return accessId;
    }

    public String getBucket(){
        return bucket;
    }

    public String getAccessKey() {
        return accessKey;
    }

//    public String getCallbackUrl() {
//        return callbackUrl;
//    }

    public String getEndpoint() {
        return endpoint;
    }

    public String getHost() {
        return host;
    }

}
