package team.blogserver.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * 自定义Mapper基类
 *
 * @author Ashinch
 * @date 2021/02/05
 */
@Repository
public interface Mapper<T> extends BaseMapper<T> {
    /**
     * 根据ID逻辑删除数据并且附带字段填充功能
     *
     * @param entity 注意入参是实体类, 如果字段没有自动填充,就只是单纯的逻辑删除
     *
     * @return 返回影响行数
     */
//    Integer deleteByIdWithFill(T entity);
}
