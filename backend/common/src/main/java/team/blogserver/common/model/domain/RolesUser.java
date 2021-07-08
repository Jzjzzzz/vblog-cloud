package team.blogserver.common.model.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * 用户角色Entity
 *
 * @author Ashinch
 * @date 2021/02/05
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "RolesUser", description = "用户角色实体类", parent = team.blogserver.common.model.domain.Domain.class)
@TableName("roles_user")
public class RolesUser extends Domain {
    private static final long serialVersionUID = -1671705180502230346L;
    @ApiModelProperty("用户ID")
    private Integer uid;
    @ApiModelProperty("角色ID")
    private Integer rid;
}
