package team.blogserver.common.mapper;

import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import team.blogserver.common.model.domain.User;

/**
 * UserMapper.java
 *
 * @author Ashinch
 * @date 2021/07/09
 */
@Repository
public interface UserMapper extends Mapper<User> {
    @Results(id = "mapping", value = {
            @Result(column = "id", property = "id"),
            @Result(column = "id", property = "roles",
                    many = @Many(select = "team.blogserver.common.mapper.RolesUserMapper.selectRoleByUid"))
    })
    @Select("")
    User resultMap();
}
