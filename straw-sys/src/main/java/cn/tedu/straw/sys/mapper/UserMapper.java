package cn.tedu.straw.sys.mapper;

import cn.tedu.straw.commons.model.Permission;
import cn.tedu.straw.commons.model.Role;
import cn.tedu.straw.commons.model.User;
import cn.tedu.straw.sys.vo.UserVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author tedu.cn
 * @since 2020-12-09
 */
@Repository
public interface UserMapper extends BaseMapper<User> {

    //根据用户输入的用户名查询用户信息的方法
    @Select("select * from user where username=#{username}")
    User findUserByUsername(String username);

    @Select("select id,username,nickname from user " +
            " where username=#{username}")
    UserVo findUserVoByUsername(String username);


    //查询指定id的用户的所有权限
    @Select("SELECT p.id,p.name" +
            " FROM user u" +
            " LEFT JOIN user_role ur ON u.id=ur.user_id" +
            " LEFT JOIN role r ON r.id=ur.role_id" +
            " LEFT JOIN role_permission rp ON r.id=rp.role_id" +
            " LEFT JOIN permission p ON p.id=rp.permission_id" +
            " WHERE u.id=#{id}")
    List<Permission> findUserPermissionsById(Integer id);

    //按用户id查询用户的所有角色
    @Select("select r.id,r.name " +
            "from user u " +
            "left join user_role ur on u.id=ur.user_id " +
            "left join role r on r.id=ur.role_id " +
            "where u.id=#{userId}")
    List<Role> findUserRolesById(Integer id);





}
