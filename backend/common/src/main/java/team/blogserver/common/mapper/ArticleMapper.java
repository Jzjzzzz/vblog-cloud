package team.blogserver.common.mapper;

import org.apache.ibatis.annotations.*;
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
            @Result(column = "id", property = "id"),
            @Result(column = "cid", property = "category",
                    one = @One(select = "team.blogserver.common.mapper.CategoryMapper.selectById")),
            @Result(column = "uid", property = "author",
                    one = @One(select = "team.blogserver.common.mapper.UserMapper.selectById")),
            @Result(column = "id", property = "tags",
                    many = @Many(select = "team.blogserver.common.mapper.ArticleTagsMapper.selectByAid"))
    })
    @Select("")
    Article resultMap();
}
