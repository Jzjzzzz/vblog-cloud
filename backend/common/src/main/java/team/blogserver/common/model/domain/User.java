package team.blogserver.common.model.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.data.annotation.Transient;

import java.util.List;

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
@JsonIgnoreProperties(value = {"handler"})
@ApiModel(value = "User", description = "用户实体类", parent = Domain.class)
@TableName(value = "user", resultMap = "mapping")
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

    @ApiModelProperty("手机号")
    private String mobile;
    @TableField(exist = false)
    @ApiModelProperty(value = "角色映射实体, 非数据库表字段")
    private List<Roles> roles;
}
