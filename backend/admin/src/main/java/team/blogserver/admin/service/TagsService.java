package team.blogserver.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import team.ark.core.util.StringUtils;
import team.blogserver.common.mapper.TagsMapper;
import team.blogserver.common.model.domain.Category;
import team.blogserver.common.model.domain.Tags;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author Jzj
 * @date 2021/07/08
 */
@Service
@Slf4j
public class TagsService extends ServiceImpl<TagsMapper, Tags> {
    public IPage<Tags> listPage(Page<Tags> pageParam,String tagName) {
        if(tagName==null || tagName.equals("")){
            return baseMapper.selectPage(pageParam, null);
        }
        QueryWrapper<Tags> tagsQueryWrapper = new QueryWrapper<>();

        tagsQueryWrapper
                .like(StringUtils.isNotBlank(tagName),"tagName",tagName);
        return baseMapper.selectPage(pageParam,tagsQueryWrapper);

    }


    public boolean deleteBatchTag(List<Tags> tagList) {
        Assert.isTrue(tagList.size()>=1, "选中标签数量异常");
        ArrayList<Integer> ids=new ArrayList<>();
        for (Tags tag : tagList) {
            ids.add(tag.getId());
        }
        int result = baseMapper.deleteBatchIds(ids);
        if(result>=1){
            return  true;
        }
        return false;
    }
    public boolean topBlogById(Integer id) {
        List<Tags> tags = baseMapper.selectList(null);
        //获取需要置顶的标签
        Tags tag = tags.stream().filter(s -> s.getId().equals(id)).findFirst().get();
        //查询当前标签集合中的最大值
        Integer maxSort = tags.stream().max(Comparator.comparingInt(Tags::getSort)).get().getSort();
        //设置需要置顶的标签为最大值
        tag.setSort(maxSort+1);
        int sum = baseMapper.updateById(tag);
        if(sum>=1){
            return true;
        }
        return false;
    }
}
