package hmh.terminal.linux.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import hmh.terminal.linux.dao.entity.Api;
import hmh.terminal.linux.dao.entity.Menu;
import hmh.terminal.linux.dao.entity.RoleApi;
import hmh.terminal.linux.result.JsonResult;
import hmh.terminal.linux.result.ResultFactory;
import hmh.terminal.linux.service.ApiService;
import hmh.terminal.linux.service.RoleApiService;
import hmh.terminal.linux.vo.ApiVo;
import hmh.terminal.linux.vo.MenuVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author heminghao
 * @version 0.1 beta
 * @date 2020/5/24 21:10
 */
@RestController
@RequestMapping("api")
@Slf4j
public class APIController {
    @Autowired
    ApiService apiService;
    @Autowired
    RoleApiService roleApiService;

    @GetMapping("label")
    public JsonResult label(){
        try {
            Map<String,Object> map = new HashMap<>();
            map.put("maxId", apiService.GetMaxApiID());
            map.put("data",apiService.bulidAPILabel());
            return  ResultFactory.SuccessResult(map);
        }catch (Exception e){
            return ResultFactory.FailResult(e.getMessage());
        }
    }

    @PostMapping("label")
    public JsonResult updateLabel(@RequestBody JSONObject apiVo){
        try {
            List<ApiVo> info = (List<ApiVo>) JSONArray.parseArray(apiVo.getString("apiVo"), ApiVo.class);
            return  ResultFactory.SuccessResult(apiService.UpdateAPILabel(info));
        }catch (Exception e){
            return ResultFactory.FailResult("修改失败");
        }
    }

    @DeleteMapping("label")
    public JsonResult deleteLabel(@RequestParam("ids") String ids){
        try {
            int[] info= Arrays.stream(ids.split(",")).mapToInt(Integer::parseInt).toArray();
            List<Integer> infos=Arrays.stream(info).boxed().collect(Collectors.toList());
            roleApiService.DelRoleApi(infos);
            return  ResultFactory.SuccessResult(apiService.removeByIds(infos));
        }catch (Exception e){
            e.printStackTrace();
            return ResultFactory.FailResult("删除失败！");
        }
    }

    @GetMapping("data")
    public JsonResult data(@RequestParam("id") Integer id) {
        try {
            return  ResultFactory.SuccessResult(apiService.getApiDataByID(id));
        }catch (Exception e){
            return ResultFactory.FailResult(e.getMessage());
        }
    }

    @PostMapping("data")
    public JsonResult setData(@RequestParam("api") String api) {
        try {
            JSONObject jsonObject=JSONObject.parseObject(api);
            Api info=(Api) JSONObject.toJavaObject(jsonObject, Api.class);
            return  ResultFactory.SuccessResult(apiService.UpdateApiData(info));
        }catch (Exception e){
            return ResultFactory.FailResult(e.getMessage());
        }
    }
}
