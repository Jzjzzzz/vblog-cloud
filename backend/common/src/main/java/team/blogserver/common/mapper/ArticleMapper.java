package team.blogserver.common.mapper;

import org.apache.ibatis.annotations.ResultMap;
import org.springframework.stereotype.Repository;
import team.blogserver.common.model.domain.Article;
import team.blogserver.common.model.domain.Roles;

/**
 * ArticleMapper.java
 *
 * @author Ashinch
 * @date 2021/07/08
 */
@Repository
public interface ArticleMapper extends Mapper<Article> {
}
