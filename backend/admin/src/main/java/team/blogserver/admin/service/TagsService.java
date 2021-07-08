package team.blogserver.admin.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import team.blogserver.common.mapper.CategoryMapper;
import team.blogserver.common.mapper.TagsMapper;
import team.blogserver.common.model.domain.Category;
import team.blogserver.common.model.domain.Tags;

/**
 *
 * @author Jzj
 * @date 2021/07/08
 */

@Service
@Slf4j
public class TagsService extends ServiceImpl<TagsMapper, Tags> {


}
