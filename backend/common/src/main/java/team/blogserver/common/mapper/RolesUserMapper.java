package team.blogserver.common.mapper;

import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import team.blogserver.common.model.domain.Roles;
import team.blogserver.common.model.domain.RolesUser;

import java.util.List;

/**
 * relate_user_role表Mapper
 *
 * @author Ashinch
 * @date 2021/02/05
 */
@Repository
@SuppressWarnings("AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc")
public interface RolesUserMapper extends Mapper<RolesUser> {
    @Select("SELECT b.id, b.name FROM roles_user AS a " +
            "LEFT JOIN roles AS b ON a.rid = b.id " +
            "WHERE a.uid = #{uid}")
    List<Roles> selectRoleByUid(Integer uid);
}
