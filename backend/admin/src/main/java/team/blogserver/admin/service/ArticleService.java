package team.blogserver.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import team.blogserver.common.mapper.ArticleMapper;
import team.blogserver.common.mapper.ArticleTagsMapper;
import team.blogserver.common.model.domain.Article;
import team.blogserver.common.model.domain.ArticleTags;

import javax.annotation.Resource;

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
    public IPage<Article> listPage(Page<Article> pageParam, String title) {
        return baseMapper.selectPage(pageParam, new QueryWrapper<Article>()
                .select(Article.class, i -> !i.getProperty().endsWith("Content"))
                .like(StringUtils.isNotBlank(title), "title", title));
    }
}
