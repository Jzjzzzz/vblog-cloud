package team.blogserver.common.model.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author Jzj
 * @date 2021/07/10
 */
@Data
@ApiModel(value = "Journal", description = "日志实体")
public class Journal   extends Domain {
    @ApiModelProperty("请求ip")
    private String hostip;

    @ApiModelProperty("请求的url")
    private String requestmethod;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date date;
}
