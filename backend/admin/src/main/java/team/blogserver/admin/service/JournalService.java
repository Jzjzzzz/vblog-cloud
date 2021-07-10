package team.blogserver.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import team.ark.core.util.StringUtils;
import team.blogserver.common.mapper.JournalMapper;
import team.blogserver.common.mapper.TagsMapper;
import team.blogserver.common.model.domain.Category;
import team.blogserver.common.model.domain.Journal;
import team.blogserver.common.model.domain.Tags;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author Jzj
 * @date 2021/07/10
 */
@Service
@Slf4j
public class JournalService extends ServiceImpl<JournalMapper, Journal> {

    public IPage<Journal> listPage(Page<Journal> pageParam) {
        return baseMapper.selectPage(pageParam, null);
    }
}
