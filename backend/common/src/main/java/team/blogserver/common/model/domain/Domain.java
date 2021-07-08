package team.blogserver.common.model.domain;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Entity基类
 *
 * @author Ashinch
 * @date 2021/02/05
 */
@Data
@ApiModel(value = "BaseEntity", description = "实体基类")
public class Domain implements Serializable {
    private static final long serialVersionUID = 452388053472956502L;
    @ApiModelProperty("主键ID")
    @TableId(type = IdType.AUTO)
    private Integer id;
//    @TableField(fill = FieldFill.INSERT)
//    private Date createAt;
//    @TableField(fill = FieldFill.INSERT_UPDATE)
//    private Date modifiedAt;
//    @Version
//    private Integer version;
//    @TableLogic
//    private Boolean isDeleted;
}
