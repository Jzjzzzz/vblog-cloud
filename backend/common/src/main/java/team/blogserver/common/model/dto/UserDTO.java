package team.blogserver.common.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import team.ark.core.util.BeanUtil;
import team.blogserver.common.model.domain.Domain;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO implements Serializable {
    private static final long serialVersionUID = 2779766396874674429L;
    private Integer id;
    private String username;

    public UserDTO(Domain domain) {
        BeanUtil.copyProperties(domain, this);
    }
}
