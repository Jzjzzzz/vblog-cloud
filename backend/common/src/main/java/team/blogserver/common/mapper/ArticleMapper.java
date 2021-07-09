package team.blogserver.common.mapper;

import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import team.blogserver.common.model.domain.Article;

/**
 * ArticleMapper.java
 *
 * @author Ashinch
 * @date 2021/07/08
 */
@Repository
public interface ArticleMapper extends Mapper<Article> {
    @Results(id = "mapping", value = {
            @Result(column = "cid", property = "category",
                    one = @One(select = "team.blogserver.common.mapper.CategoryMapper.selectById")),
            @Result(column = "uid", property = "author",
                    one = @One(select = "team.blogserver.common.mapper.UserMapper.selectById"))
    })
    @Select("")
    Article resultMap();
}
