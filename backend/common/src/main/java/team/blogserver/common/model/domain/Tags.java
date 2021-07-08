package team.blogserver.common.model.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

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
}
