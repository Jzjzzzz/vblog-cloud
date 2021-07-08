//package team.blogserver.common.mybatisplus;
//
//import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.ibatis.reflection.MetaObject;
//import org.springframework.stereotype.Component;
//import team.ark.core.util.DateTimeUtils;
//
///**
// * Mybatis-Plus元对象处理器
// *
// * @author Ashinch
// * @date 2021/01/28
// */
//@Slf4j
//@Component
//public class MybatisPlusMetaObjectHandler implements MetaObjectHandler {
//    /**
//     * 插入时填充策略
//     */
//    @Override
//    public void insertFill(MetaObject metaObject) {
//        this.setFieldValByName("createAt", DateTimeUtils.now(), metaObject);
//        this.setFieldValByName("modifiedAt", DateTimeUtils.now(), metaObject);
//    }
//
//    /**
//     * 更新时填充策略
//     */
//    @Override
//    public void updateFill(MetaObject metaObject) {
//        this.setFieldValByName("modifiedAt", DateTimeUtils.now(), metaObject);
//    }
//}
//
