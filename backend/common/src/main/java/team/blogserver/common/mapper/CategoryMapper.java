package team.blogserver.common.mapper;

import org.springframework.stereotype.Repository;
import team.blogserver.common.model.domain.Category;
import team.blogserver.common.model.dto.ExcelCategoryDTO;

import java.util.List;

/**
 * @author Jzj
 * @date 2021/07/08
 */
@Repository
public interface CategoryMapper extends Mapper<Category> {
    void insertBatch(List<ExcelCategoryDTO> list);
}
