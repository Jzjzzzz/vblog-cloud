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
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date date;
}
