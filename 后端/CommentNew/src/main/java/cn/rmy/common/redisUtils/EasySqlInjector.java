package cn.rmy.common.redisUtils;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.extension.injector.methods.AlwaysUpdateSomeColumnById;
import com.baomidou.mybatisplus.extension.injector.methods.InsertBatchSomeColumn;

import java.util.List;

/**
 * 一句话功能描述.
 * 项目名称:
 * 包:
 * 类名称:
 * 类描述:   类功能详细描述
 * 创建人:   xsc
 * 创建时间:
 */
public class EasySqlInjector extends DefaultSqlInjector {

    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass){
        List<AbstractMethod> methodList = super.getMethodList(mapperClass);
        methodList.add(new InsertBatchSomeColumn());
        methodList.add(new AlwaysUpdateSomeColumnById());
//        methodList.add(new DeleteBatchByIds());
        return methodList;
    }
}
