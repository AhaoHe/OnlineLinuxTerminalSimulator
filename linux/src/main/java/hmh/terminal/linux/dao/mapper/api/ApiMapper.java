package hmh.terminal.linux.dao.mapper.api;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import hmh.terminal.linux.dao.entity.Api;
import hmh.terminal.linux.dao.provider.ApiProvider;
import hmh.terminal.linux.vo.ApiDataVo;
import hmh.terminal.linux.vo.ApiVo;
import hmh.terminal.linux.vo.MenuDataVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

/**
 * @author heminghao
 * @version 0.1 beta
 * @date 2020/3/18 1:32
 */
public interface ApiMapper extends BaseMapper<Api> {
    /**
     * 注意：该方法前端暂时没用到！
     *
     * 通过角色获得后台AIP访问地址
     * 一个账号有多个角色。
     * @param roles
     * @return
     */
    @SelectProvider(type = ApiProvider.class, method ="ApiSelectSQL")
    List<Api> getApiUrlByRoles(@Param("roles") String ...roles);


    /**
     * 根据用户名称获得API URL资源鉴权
     * @param username
     * @return
     */
    @SelectProvider(type = ApiProvider.class, method ="ApiByusername")
    List<Api> getApiUrlByUserName(@Param("username") String username);


    List<Api> getApiUrlByUserNameNew(@Param("username") String username);

    /**
     * 管理员对API进行管理
     */
    /**
     * 查所有（编辑使用）
     */
    List<ApiVo> getAllApiList();

    /*
    * 查询列表
    * */
    List<ApiVo> getApiLabelList();

    /*
    * 查询最大值
    * */
    @Select("SELECT max(api_id)+1 FROM api")
    int getMaxApiID();

    /*
     * 查询Api id的数据
     * */
    ApiDataVo getApiData(@Param("id")Integer id);
}
