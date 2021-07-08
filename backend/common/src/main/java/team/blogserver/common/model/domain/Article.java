package team.blogserver.common.model.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.dreamyoung.mprelation.JoinColumn;
import com.github.dreamyoung.mprelation.OneToMany;
import com.github.dreamyoung.mprelation.OneToOne;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.data.annotation.Transient;

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
@ApiModel(value = "Article", description = "文章实体类", parent = Domain.class)
@TableName("article")
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
    @ApiModelProperty("栏目ID")
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
    @OneToOne
    @TableField(exist = false)
    @JoinColumn(name = "uid", referencedColumnName = "id")
    @ApiModelProperty("作者ID")
    private User author;
}
