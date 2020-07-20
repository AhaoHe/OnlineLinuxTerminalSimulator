package hmh.terminal.linux.dao.provider;

import org.apache.ibatis.jdbc.SQL;

import java.util.Map;

/**
 * @author heminghao
 * @version 0.1 beta
 * @date 2020/3/18 1:47
 */
public class ApiProvider {
    /**
     * 根据用户名称获得API URL资源鉴权
     * @param params
     * @return
     */
    public String ApiByusername(Map params){
        return new SQL(){
            {
                SELECT_DISTINCT("a.api_url","a.api_method");
                FROM("api a",
                        "role b",
                        "role_api c",
                        "user d",
                        "user_role e");
                WHERE("a.api_id = c.api_id",
                        "a.api_url <> 'none'",
                        "b.role_id = c.role_id",
                        "d.uid = e.uid",
                        "e.role_id = c.role_id",
                        "d.username = #{username}");
            }
        }.toString();
    }

    /**
     * 创建查询语句
     * @param params
     * @return
     */
    public String ApiSelectSQL(Map params) {

        String[] roles = (String[])params.get("roles");

        StringBuilder sql = new StringBuilder ();
        sql.append ("select DISTINCT a.api_url ,a.api_method ");
        sql.append ("from api a, role b, role_api c ");
        sql.append ("where a.api_id = c.api_id ");
        sql.append ("  and a.api_url <> 'none' ");
        sql.append ("  and b.role_id = c.role_id ");
        sql.append ("  and b.role_name ");

        String inSQL = "in (";

        for (String role:roles) {
            inSQL +="'"+role+"',";
        }
        //去掉最后一个"，"
        inSQL = inSQL.substring (0,inSQL.length ()-1)+")";
        sql.append(inSQL);
        //返回
        return sql.toString();
    }
}
