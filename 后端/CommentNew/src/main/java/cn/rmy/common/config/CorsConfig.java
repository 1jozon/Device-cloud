package cn.rmy.common.config;

import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Bean
    public WebMvcConfigurer corsConfigurer(){
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOriginPatterns("*")    //允许域名
                       // .allowedOrigins("http://121.40.104.60")
                        .allowedMethods("*")    //允许方法
                        .allowedHeaders("*")    //允许请求头
                        .allowCredentials(true); //带上cookie信息
                   //     .exposedHeaders(HttpHeaders.SET_COOKIE).maxAge(3600L);  //3600秒内不需要在发送预检验请求，可以缓存结果
            }
        };
    }


}
