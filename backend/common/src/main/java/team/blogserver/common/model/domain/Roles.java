package team.blogserver.common.model.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * 角色Entity
 *
 * @author Ashinch
 * @date 2021/02/05
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "Roles", description = "角色实体类", parent = Domain.class)
@TableName("roles")
public class Roles extends Domain {
    private static final long serialVersionUID = -8233812592683226379L;
    @ApiModelProperty("角色名")
    private String name;
}
