package team.blogserver.common.model.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author Jzj
 * @date 2021/07/08
 */
@Data
@ApiModel(value = "Category", description = "分类实体")
public class Category extends Domain {
    @ApiModelProperty("分类名称")
    private String cateName;

    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @ApiModelProperty("创建时间")
    private Date date;

    @ApiModelProperty("排序字段")
    private Integer sort;
}
