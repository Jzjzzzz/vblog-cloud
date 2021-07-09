package team.blogserver.common.model.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.Date;

/**
 * @author jzj
 * @date 2021/07/08
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "WebConfig", description = "网站设置")
public class WebConfig extends Domain {
    private static final long serialVersionUID = -8035714525278026715L;
    @ApiModelProperty(value = "logo")
    private String logo;
    @ApiModelProperty(value = "网站名称")
    private String name;
    @ApiModelProperty(value = "网站介绍")
    private String summary;
    @ApiModelProperty(value = "网站关键字")
    private String keyword;
    @ApiModelProperty(value = "网站作者")
    private String author;
    @ApiModelProperty(value = "网站备案号")
    private Integer recordnum;
    @ApiModelProperty(value = "创建时间")
    private Date createtime;
    @ApiModelProperty(value = "网站标题")
    private String title;
    @ApiModelProperty(value = "qq号")
    private String qqnumber;
    @ApiModelProperty(value = "qq群")
    private String qqgroup;
    @ApiModelProperty(value = "邮箱")
    private String email;
}
