/**
 * All rights Reserved, Designed By MiGu
 * Copyright:    Copyright(C) 2016-2020
 * Company       MiGu  Co., Ltd.
*/
package cn.rmy.common.redisUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 类作用：MessageUtil工具类.
 * 项目名称: color-common
 * 包: com.migu.colorcommon.util
 * 类名称: MessageUtil.java
 * 类描述: MessageUtil工具类
 * 创建人: nirui
 * 创建时间: 2017-07-25 11:46
 */
public class MessageUtil {
    private static Log logger = LogFactory.getLog(MessageUtil.class);

    private static Properties props = new Properties();

    static {
        InputStream in = MessageUtil.class.getClassLoader()
                .getResourceAsStream("message/resultMessage.properties");
        try {
            props.load(in);

        } catch (IOException e) {
            logger.error("read conf.properties failure. ", e);
            throw new RuntimeException("read conf.properties failure. ", e);
        }
    }

    /**
     * 根据返回码获取返回信息.
     *
     * @param resultCode
     *            返回码
     * @return props.getProperty(resultCode)
     */
    public static String resultCode2Message(String resultCode) {
        return props.getProperty(resultCode);
    }
    
    /**
     * 
      *将消息中的参数替换成对应的参数信息
      *〈功能详细描述〉
      * @param msg
      * @param params
      * @return
     */
    public static String getMessage(String msg,String[] params) {
    	if(params != null && params.length > 0) {
    		for(int i = 0; i < params.length;i++) {
    			msg = msg.replaceAll("\\{" + i + "\\}", params[i]);
    		}
    	}
    	
    	return msg;
    }
}
