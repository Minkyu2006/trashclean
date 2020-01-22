package kr.co.broadwave.aci.devicestats;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2020-01-10
 * Time :
 * Remark : Devicestatus Service
 */
@Slf4j
@Service
public class DevicestatusService {
    private final DevicestatsRepositoryCustom devicestatsRepositoryCustom;

    @Autowired
    public DevicestatusService(DevicestatsRepositoryCustom devicestatsRepositoryCustom) {
        this.devicestatsRepositoryCustom = devicestatsRepositoryCustom;
    }

    public List<DevicestatsDto> queryDslDevicestatsAvgQuerydsl(List<String> deviceid,String yyyymmdd1,String yyyymmdd2) {
        return devicestatsRepositoryCustom.queryDslDevicestatsAvgQuerydsl(deviceid,yyyymmdd1,yyyymmdd2);
    }

    public List<DevicestatsDailyDto> queryDslDeviceDaily(List<String> deviceid,String sMonth) {
        return devicestatsRepositoryCustom.queryDslDeviceDaily(deviceid,sMonth);
    }

    public List<DevicestatsDailyHourLevelDto> queryDslDeviceDailyHourLevel(List<String> deviceid, String sendDate) {
        return devicestatsRepositoryCustom.queryDslDeviceDailyHourLevel(deviceid,sendDate);
    }

    public List<DevicestatsDailyMonthDto> queryDslDeviceDailyMonth(String deviceid,String deviceMonth){
        return devicestatsRepositoryCustom.queryDslDeviceDailyMonth(deviceid,deviceMonth);
    }
}
