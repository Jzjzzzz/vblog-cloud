package team.blogserver.common.mapper;

import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import team.blogserver.common.model.domain.ArticleTags;
import team.blogserver.common.model.domain.Tags;

import java.util.List;

/**
 * ArticleTagsMapper.java
 *
 * @author Ashinch
 * @date 2021/07/08
 */
@Repository
@SuppressWarnings({"AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc"})
public interface ArticleTagsMapper extends Mapper<ArticleTags> {
    @Select("SELECT b.id, b.tagName, b.date, b.sort " +
            "FROM article_tags as a, tags as b WHERE " +
            "a.aid = #{aid} AND a.tid = b.id")
    List<Tags> selectByAid(Integer aid);
}
