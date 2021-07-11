package team.blogserver.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import team.ark.core.security.JwtUtils;
import team.blogserver.common.mapper.ArticleMapper;
import team.blogserver.common.mapper.ArticleTagsMapper;
import team.blogserver.common.model.domain.Article;
import team.blogserver.common.model.domain.ArticleTags;
import team.blogserver.common.model.domain.Tags;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 文章服务
 *
 * @author Ashinch
 * @date 2021/07/08
 */
@Slf4j
@Service
public class ArticleService extends ServiceImpl<ArticleMapper, Article> {
    @Resource
    ArticleTagsMapper articleTagsMapper;

    public IPage<Article> listPage(Page<Article> pageParam, String title, Long state) {
        QueryWrapper<Article> articleQueryWrapper = new QueryWrapper<>();
        articleQueryWrapper.select(Article.class, i -> !i.getProperty().endsWith("Content"));
        articleQueryWrapper.like(StringUtils.isNotBlank(title), "title", title);
        if (state != null&&state!=10L) {
            articleQueryWrapper.eq("state", state);
        }
        return baseMapper.selectPage(pageParam, articleQueryWrapper);
    }

    public IPage<Article> listPageByState(Page<Article> pageParam, Integer state) {
        return baseMapper.selectPage(pageParam, new QueryWrapper<Article>()
                .select(Article.class, i -> !i.getProperty().endsWith("Content"))
                .eq(state != null, "state", state));
    }

    public IPage<Article> listPageOrderByDESC(Page<Article> pageParam, String field) {
        return baseMapper.selectPage(pageParam, new QueryWrapper<Article>()
                .select(Article.class, i -> !i.getProperty().endsWith("Content"))
                .orderByDesc(field));
    }

    public boolean addArticleTags(Article article) {
        article.setUid(JwtUtils.<Integer>getId());
        article.setPublishDate(new Date());
        article.setEditTime(new Date());
        baseMapper.insert(article);
        for (Tags tags : article.getTags()) {
            if (articleTagsMapper.insert(new ArticleTags(article.getId(), tags.getId())) < 1) {
                return false;
            }
        }
        return true;
    }

}
