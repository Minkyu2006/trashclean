package kr.co.broadwave.aci.devicestats;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2020-01-14
 * Remark :
 */
public interface DevicestatsRepositoryCustom {
    List<DevicestatsDto> getDevicestatsAvgQuerydsl(List<String> deviceid,String yyyymmdd1,String yyyymmdd2);
}