package team.blogserver.common.model.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.data.annotation.Transient;

/**
 * 用户Entity
 *
 * @author Ashinch
 * @date 2021/01/28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "User", description = "用户实体类", parent = Domain.class)
@TableName("user")
public class User extends Domain {
    private static final long serialVersionUID = -180340975360345281L;
    @ApiModelProperty("用户名")
    private String username;
    @ApiModelProperty("昵称")
    private String nickname;
    @JsonIgnore
    @ApiModelProperty("密码")
    private String password;
    @ApiModelProperty("可用状态")
    private Boolean enabled;
    @ApiModelProperty("邮箱")
    private String email;
    @ApiModelProperty("头像")
    private String userface;
    @ApiModelProperty("注册时间")
    private String regTime;
}
