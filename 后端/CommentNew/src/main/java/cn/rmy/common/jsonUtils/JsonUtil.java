package cn.rmy.common.jsonUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class JsonUtil {

    private Object data;

    /**
     * 将一个对象转化为一个json字符串，字符串交付MQTT传输
     * @param object
     * @return
     */
    public static String jsonToString(Object object){

        return JSON.toJSONString(new JsonUtil(object));
    }

    /**
     * 将收到的json字符串转化为json对象，方便取值
     * @param json
     * @return
     */
    public static JSONObject stringToJsonObject(String json){
        JSONObject object = JSONObject.parseObject(json);
        return object;
    }
}
