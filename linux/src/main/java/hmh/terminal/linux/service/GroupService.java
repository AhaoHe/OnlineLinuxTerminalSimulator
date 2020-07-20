package hmh.terminal.linux.service;

import com.baomidou.mybatisplus.extension.service.IService;
import hmh.terminal.linux.dao.entity.Group;
import hmh.terminal.linux.vo.GroupVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author heminghao
 * @version 0.1 beta
 * @date 2020/6/4 20:04
 */
public interface GroupService extends IService<Group> {

    List<Group> getGroupList(int level,int groupId);



    boolean SaveOrUpdateGroup(Group group);
    boolean delGroup(int groupId);

    //超级管理员的其他组成员
    List<Map<String,Object>> getUserUnchecked(GroupVo groupVo);
    //组管理的申请人员
    List<Map<String,Object>> getApplyUnChecked(Integer groupId,String search);
    //本组成员
    List<Map<String,Object>> getUserChecked(GroupVo groupVo);


    boolean addUserToGroup(List<Map<String,Object>> users,Integer groupId);

    boolean removeUserToGroup(List<Map<String,Object>> users, Integer groupId);



    /*
    * 修改角色时，判断是否是无组
    * */
    boolean RoleGroup(Integer groupId, Integer level,String susername);

    /*
    * 可申请组列表
    * */
    List<Map<String,Object>> applyGroupList(String username,Integer groupId);
    /*
    * 申请组的方法
    * */
    boolean ApplyGroup(Integer groupId, String username);
    /*
    * 取消或拒绝申请
    * */
    boolean delApply(Integer groupId, String usernames);
}
