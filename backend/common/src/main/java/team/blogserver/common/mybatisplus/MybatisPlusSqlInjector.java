//package team.blogserver.common.mybatisplus;
//
//import com.baomidou.mybatisplus.core.injector.AbstractMethod;
//import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
//import com.baomidou.mybatisplus.extension.injector.methods.LogicDeleteByIdWithFill;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//
///**
// * Mybatis-Plus自定义Mapper方法
// *
// * @author Ashinch
// * @date 2021/01/28
// */
//@Component
//public class MybatisPlusSqlInjector extends DefaultSqlInjector {
//    @Override
//    public List<AbstractMethod> getMethodList(Class<?> mapperClass) {
//        List<AbstractMethod> methodList = super.getMethodList(mapperClass);
//        methodList.add(new LogicDeleteByIdWithFill());
//        return methodList;
//    }
//}
