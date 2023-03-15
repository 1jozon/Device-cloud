package cn.rmy.common.redisUtils;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * MybatisPlus 处理器
 * 处理：自动填充
 */
@Component
@Slf4j
public class MpHandler implements MetaObjectHandler {

    @Override
   // @Async("threadPoolTaskExecutor")
    public void insertFill(MetaObject metaObject) {
        log.info("start insert fill ....");
            this.setFieldValByName("createTime",new Date(),metaObject);
            this.setFieldValByName("updateTime",new Date(),metaObject);
        this.setFieldValByName("version",1,metaObject);
        this.setFieldValByName("deleted",0,metaObject);

    }

    @Override
   // @Async("threadPoolTaskExecutor")
    public void updateFill(MetaObject metaObject) {
        log.info("start update fill ....");
        this.setFieldValByName("updateTime",new Date(),metaObject);
    }

}
