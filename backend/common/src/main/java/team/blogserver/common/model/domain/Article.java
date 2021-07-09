package team.blogserver.common.model.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.Date;

/**
 * 文章Entity
 *
 * @author Ashinch
 * @date 2021/07/08
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(value = {"handler"})
@ApiModel(value = "Article", description = "文章实体类", parent = Domain.class)
@TableName(value = "article", resultMap = "mapping")
public class Article extends Domain {
    private static final long serialVersionUID = 1552702441476476804L;
    @ApiModelProperty("标题")
    private String title;
    @ApiModelProperty("MD内容")
    private String mdContent;
    @ApiModelProperty("HTML内容")
    private String htmlContent;
    @ApiModelProperty("摘要")
    private String summary;
    @ApiModelProperty("分类ID")
    private Integer cid;
    @ApiModelProperty("作者ID")
    private Integer uid;
    @ApiModelProperty("发布日期")
    private Date publishDate;
    @ApiModelProperty("编辑时间")
    private Date editTime;
    @ApiModelProperty(name = "状态", notes = "0表示草稿箱，1表示已发表，2表示已删除")
    private Integer state;
    @ApiModelProperty("阅读次数")
    private Integer pageView;
    @TableField(exist = false)
    @ApiModelProperty(value = "作者映射实体, 非数据库表字段")
    private User author;
    @TableField(exist = false)
    @ApiModelProperty(value = "分类映射实体, 非数据库表字段")
    private Category category;
}
