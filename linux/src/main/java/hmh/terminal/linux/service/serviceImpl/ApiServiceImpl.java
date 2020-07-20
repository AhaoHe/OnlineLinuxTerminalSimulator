package hmh.terminal.linux.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import hmh.terminal.linux.dao.entity.Api;
import hmh.terminal.linux.dao.entity.Menu;
import hmh.terminal.linux.dao.mapper.api.ApiMapper;
import hmh.terminal.linux.service.ApiService;
import hmh.terminal.linux.vo.ApiDataVo;
import hmh.terminal.linux.vo.ApiVo;
import hmh.terminal.linux.vo.MenuVo;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

/**
 * @author heminghao
 * @version 0.1 beta
 * @date 2020/3/18 21:12
 */
@Service("ApiService")
public class ApiServiceImpl extends ServiceImpl<ApiMapper, Api> implements ApiService {
    /**
     * 根据角色查询API接口URL
     *
     * @param roles
     * @return
     */
    @Override
    public List<Api> getApiUrlByRoles(String... roles) {
        return this.baseMapper.getApiUrlByRoles(roles);
    }

    /**
     * 根据用户名称查询API接口URL
     *
     * @param username
     * @return
     */
    @Override
    public List<Api> getApiUrlByUserName(String username) {
        System.out.println("getApiUrlByUserName.................");
        return this.baseMapper.getApiUrlByUserName(username);
    }

    @Override
    public List<Api> getApiUrlByUserNameNew(String username) {
        System.out.println("Loading API=>");
        return this.baseMapper.getApiUrlByUserNameNew(username);
    }

    /**
     * 查所有（编辑使用）
     */
    @Override
    public List<ApiVo> getAllApiList() {
        return this.baseMapper.getAllApiList();

    }

    @Override
    public List<ApiVo> bulidAPILabel() {
        return buildLabel(0,this.baseMapper.getApiLabelList(),new LinkedList<>());
    }

    @Override
    public int GetMaxApiID() {
        return this.baseMapper.getMaxApiID();
    }

    private List<ApiVo> buildLabel(int pid, List<ApiVo> apiVos, List<ApiVo> result){
        apiVos.forEach(apiVo -> {
            if(apiVo.getPid() == pid){
                List<ApiVo> temp = new LinkedList<>();
                List<ApiVo> children=buildLabel(apiVo.getId(),apiVos,temp);
                if( children.size() > 0)
                    apiVo.setChildren(children);
                result.add(apiVo);
            }
        });
        return result;
    }

    @Override
    public boolean UpdateAPILabel(List<ApiVo> apiVos) {
        //拆解树结构
        List<ApiVo> chilren= TreeToListChilren(apiVos,new LinkedList<ApiVo>(),0);
        for(int i=0 ;i<apiVos.size();i++){
            apiVos.get(i).setSort(i);
        }
        apiVos.addAll(chilren);
        //更新menuLabel
        for (ApiVo apiVo:apiVos){
            if(apiVo.isNewData()){
                Api api = new Api();
                api.setId(apiVo.getId());
                api.setPid(apiVo.getPid());
                if (apiVo.getChildren()!=null&&apiVo.getChildren().size()>0)
                    api.setApiMethod("");
                api.setApiName(apiVo.getLabel());
                api.setApiUrl("");
                api.setApiSort(apiVo.getSort());
                this.save(api);
            }else{
                LambdaUpdateWrapper<Api> wrapper = new LambdaUpdateWrapper<>();
                wrapper
                        .set(Api::getPid,apiVo.getPid())
                        .set(Api::getApiName,apiVo.getLabel())
                        .set(Api::getApiSort,apiVo.getSort())
                        .eq(Api::getId,apiVo.getId());
                this.update(wrapper);
            }
        }
        return true;
    }

    @Override
    public ApiDataVo getApiDataByID(Integer id) {
        return this.baseMapper.getApiData(id);
    }

    @Override
    public boolean UpdateApiData(Api api) {
        return this.updateById(api);
    }

    private List<ApiVo> TreeToListChilren(List<ApiVo> apiVos,LinkedList<ApiVo> result,int pid){
        for(int i=0 ;i<apiVos.size();i++){
            ApiVo apiVo = apiVos.get(i);
            if(apiVo.getChildren()!=null&&apiVo.getChildren().size()!=0){
                TreeToListChilren(apiVo.getChildren(),result,apiVo.getId());
            }else {
                apiVo.setSort(i);
                apiVo.setPid(pid);
                result.add(apiVo);
            }
        }
        return result;
    }

}
