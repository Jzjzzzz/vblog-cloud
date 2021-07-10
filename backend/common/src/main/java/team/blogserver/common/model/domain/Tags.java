package team.blogserver.common.model.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.Date;

/**
 * 标签Entity
 *
 * @author Ashinch
 * @date 2021/07/08
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "Tags", description = "标签实体类", parent = Domain.class)
@TableName("tags")
public class Tags extends Domain {
    private static final long serialVersionUID = -1352705180502110346L;
    @ApiModelProperty("标签名")
    private String tagName;

    @ApiModelProperty("排序字段")
    private Integer sort;

    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @ApiModelProperty("创建时间")
    private Date date;
}
