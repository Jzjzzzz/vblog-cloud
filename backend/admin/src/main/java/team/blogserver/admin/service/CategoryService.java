package team.blogserver.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import team.ark.core.util.StringUtils;
import team.blogserver.common.mapper.CategoryMapper;
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
public class CategoryService extends ServiceImpl<CategoryMapper, Category> {
    public IPage<Category> listPage(Page<Category> pageParam,String cateName) {
        if(cateName==null || cateName.equals("")){
            return baseMapper.selectPage(pageParam, null);
        }
        QueryWrapper<Category> categoryQueryWrapper = new QueryWrapper<>();

        categoryQueryWrapper
                .like(StringUtils.isNotBlank(cateName),"cateName",cateName);
        return baseMapper.selectPage(pageParam,categoryQueryWrapper);
    }

    public boolean deleteBatchCategory(List<Category> categoryList) {
        Assert.isTrue(categoryList.size()>=1, "选择分类数量异常");
        ArrayList<Integer> ids=new ArrayList<>();
        for (Category category : categoryList) {
            ids.add(category.getId());
        }
        int result = baseMapper.deleteBatchIds(ids);
        if(result>=1){
            return  true;
        }
        return false;
    }

    public boolean topBlogById(Integer id) {
        List<Category> categories = baseMapper.selectList(null);
        //获取需要置顶的标签
        Category category = categories.stream().filter(s -> s.getId().equals(id)).findFirst().get();
        //查询当前标签集合中的最大值
        Integer maxSort = categories.stream().max(Comparator.comparingInt(Category::getSort)).get().getSort();
        //设置需要置顶的标签为最大值
        category.setSort(maxSort+1);
        int sum = baseMapper.updateById(category);
        if(sum>=1){
            return true;
        }
        return false;
    }
}
