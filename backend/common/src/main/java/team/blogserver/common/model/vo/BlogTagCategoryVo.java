package team.blogserver.common.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import team.blogserver.common.model.domain.Category;
import team.blogserver.common.model.domain.Tags;

import java.util.List;

/**
 * @author lshz
 * @date 2021/07/10
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogTagCategoryVo {
   private List<Tags> tags;
    private  List<Category> categories;
}
