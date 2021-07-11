package team.blogserver.common.model.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class ExcelCategoryDTO {

    @ExcelProperty("id")
    private Integer id;
    @ExcelProperty("分类名称")
    private String cateName;

    @ExcelProperty("创建时间")
    private Date date;

    @ExcelProperty("排序字段")
    private Integer sort;
}
