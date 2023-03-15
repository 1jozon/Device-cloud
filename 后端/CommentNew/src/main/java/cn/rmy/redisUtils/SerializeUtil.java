package cn.rmy.redisUtils;

import java.io.*;

/**
 * 序列化工具
 *
 * @author chu
 * @date 2022/03/21
 */
public class SerializeUtil {
    /**
     *
     * 序列化
     */
    public static byte[] serialize(Object obj) {

        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = null;

        try {
            // 序列化
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);

            oos.writeObject(obj);
            byte[] byteArray = baos.toByteArray();
            return byteArray;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * 反序列化
     *
     * @param bytes
     * @return
     */
    public static Object unSerialize(byte[] bytes) {

        ByteArrayInputStream bais = null;
        ObjectInputStream ois = null;

        try {
            // 反序列化为对象
            bais = new ByteArrayInputStream(bytes);
            ois = new ObjectInputStream(bais);
            return ois.readObject();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
