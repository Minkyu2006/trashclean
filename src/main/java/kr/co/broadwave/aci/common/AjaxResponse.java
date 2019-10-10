package kr.co.broadwave.aci.common;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 * @author InSeok
 * Date : 2019-04-02
 * Time : 14:05
 * Remark : Ajax 호출용 응답클래스
 */
public class AjaxResponse {
    Map<String, Object> res;

    public AjaxResponse(){
        this.res = new HashMap<>();

    }

    public void addResponse(String key,Object obj){

        res.put(key,obj);
    }


    public Map<String, Object> success() {

        res.put("status",200);
        res.put("timestamp", new Timestamp(System.currentTimeMillis()));
        res.put("message", "SUCCESS");
        res.put("err_code", "");
        res.put("err_msg", "");


        return this.res;
    }

    public Map<String, Object> fail(String err_code,String err_msg) {
        res.clear();
        res.put("status",500);
        res.put("timestamp", new Timestamp(System.currentTimeMillis()));
        res.put("message", "Error");
        res.put("err_code", err_code);
        res.put("err_msg", err_msg);

        return this.res;
    }
}
