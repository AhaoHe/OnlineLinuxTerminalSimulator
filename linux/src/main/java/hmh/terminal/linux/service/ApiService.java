package hmh.terminal.linux.service;

import com.baomidou.mybatisplus.extension.service.IService;
import hmh.terminal.linux.dao.entity.Api;
import hmh.terminal.linux.dao.entity.Menu;
import hmh.terminal.linux.vo.ApiDataVo;
import hmh.terminal.linux.vo.ApiVo;
import hmh.terminal.linux.vo.MenuDataVo;

import java.util.List;

/**
 * @author heminghao
 * @version 0.1 beta
 * @date 2020/3/18 21:11
 */
public interface ApiService extends IService<Api> {
    /**
     * 根据角色查询API接口URL
     * @param roles
     * @return
     */
    List<Api> getApiUrlByRoles(String ...roles);

    /**
     * 根据用户名称查询API接口URL
     * @param username
     * @return
     */
    List<Api> getApiUrlByUserName(String username);

    List<Api> getApiUrlByUserNameNew(String username);

    /**
     * 查所有（编辑使用）
     */
    List<ApiVo> getAllApiList();

    /*
    * 查询api列表
    * */
    List<ApiVo> bulidAPILabel();
    int GetMaxApiID();

    /*
    * 更新APILabel
    * */
    boolean UpdateAPILabel(List<ApiVo> apiVos);

    ApiDataVo getApiDataByID(Integer id);

    boolean UpdateApiData(Api api);
}
