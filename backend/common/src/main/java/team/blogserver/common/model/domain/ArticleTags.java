package team.blogserver.common.model.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.Date;

/**
 * 文章标签Entity
 *
 * @author Ashinch
 * @date 2021/07/08
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "ArticleTags", description = "文章标签实体类", parent = Domain.class)
@TableName("article_tags")
public class ArticleTags extends Domain {
    private static final long serialVersionUID = 8807230790177860647L;
    @ApiModelProperty("文章ID")
    private Integer aid;
    @ApiModelProperty("标签ID")
    private Integer tid;
}
