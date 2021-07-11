package team.blogserver.admin.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import team.blogserver.common.mapper.CategoryMapper;
import team.blogserver.common.model.dto.ExcelCategoryDTO;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@NoArgsConstructor
public class ExcelCategoryDTOListener extends AnalysisEventListener<ExcelCategoryDTO> {

    private CategoryMapper categoryMapper;

    //数据列表
    private List<ExcelCategoryDTO> list=new ArrayList<>();

    //每隔5条记录批量存储一次数据
    private static final int BATCH_COUNT=5;

    public ExcelCategoryDTOListener(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    @Override
    public void invoke(ExcelCategoryDTO data, AnalysisContext analysisContext) {
        //将数据存入数据列表
        list.add(data);
        if(list.size()>=BATCH_COUNT){
            saveData();
            list.clear();
        }
        log.info("解析了一条数据：{}",data);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        //当最终剩余的数据不足BATCH_COUNT时，我们最终一次存储剩余数据
        saveData();
        log.info("所有数据解析完成！");
    }
    private void saveData(){
        log.info("{} 条数据被存储到数据库.......",list.size());
        //调用mapper层的批量save方法
        categoryMapper.insertBatch(list);
        log.info("{} 条数据存储成功.......",list.size());

    }
}
