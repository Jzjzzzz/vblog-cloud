package team.blogserver.admin.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import team.blogserver.common.mapper.ArticleMapper;
import team.blogserver.common.model.domain.Article;

import javax.annotation.Resource;
import java.util.List;

/**
 * 文章服务
 *
 * @author Ashinch
 * @date 2021/07/08
 */
@Slf4j
@Service
public class ArticleService {
    @Resource
    private ArticleMapper articleMapper;

    public List<Article> getAll() {
        return articleMapper.selectList(null);
    }
}
