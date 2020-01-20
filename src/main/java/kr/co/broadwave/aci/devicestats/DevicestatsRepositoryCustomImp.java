package kr.co.broadwave.aci.devicestats;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.broadwave.aci.equipment.EquipmentEmnumberDto;
import kr.co.broadwave.aci.equipment.QEquipment;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2020-01-14
 * Remark :
 */
@Repository
public class DevicestatsRepositoryCustomImp extends QuerydslRepositorySupport implements DevicestatsRepositoryCustom{

    public DevicestatsRepositoryCustomImp() {
        super(Devicestatus.class);
    }

    // 대시보드 종합페이지 배출량 Querydsl
    @Override
    public List<DevicestatsDto> queryDslDevicestatsAvgQuerydsl(List<String> deviceid,String yyyymmdd1,String yyyymmdd2) {

        JPAQueryFactory queryFactory = new JPAQueryFactory(this.getEntityManager());

        QDevicestatus devicestatus = QDevicestatus.devicestatus;

        return queryFactory.select(Projections.constructor(DevicestatsDto.class,
                devicestatus.yyyymmdd,devicestatus.fullLevel.avg()))
                .from(devicestatus)
                .where(devicestatus.yyyymmdd.between(yyyymmdd2,yyyymmdd1))
                .where(devicestatus.deviceid.in(deviceid))
                .groupBy(devicestatus.yyyymmdd)
                .fetch();
    }

    // 일자별장비현황 합계,배출량평균 Querydsl
    @Override
    public List<DevicestatsDailyDto> queryDslDeviceDaily(List<String> deviceid,String sMonth) {

        QDevicestatusdaily devicestatusdaily = QDevicestatusdaily.devicestatusdaily;

        JPQLQuery<DevicestatsDailyDto> query = from(devicestatusdaily)
                .select(Projections.constructor(DevicestatsDailyDto.class,
                        devicestatusdaily.yyyymmdd,
                        devicestatusdaily.actuaterCnt.sum(),
                        devicestatusdaily.inputdoorjammingCnt.sum(),
                        devicestatusdaily.frontdoorsolopenCnt.sum(),
                        devicestatusdaily.emitCnt.sum(),
                        devicestatusdaily.fullLevel.avg()))
                .groupBy(devicestatusdaily.yyyymmdd);

        if (deviceid != null){
            query.where(devicestatusdaily.deviceid.in(deviceid));
        }
        if (sMonth != null){
            query.where(devicestatusdaily.yyyymmdd.likeIgnoreCase(sMonth.concat("%")));
        }

        return query.fetch();
    }

    // 일자별장비현황 배출량평균 꺾은선그래프 그리는 Querydsl
    @Override
    public List<DevicestatsDailyHourLevelDto> queryDslDeviceDailyHourLevel(List<String> deviceid,String sendDate) {

        QDevicestatusdaily devicestatusdaily = QDevicestatusdaily.devicestatusdaily;

        JPQLQuery<DevicestatsDailyHourLevelDto> query = from(devicestatusdaily)
                .select(Projections.constructor(DevicestatsDailyHourLevelDto.class,
                        devicestatusdaily.hh,
                        devicestatusdaily.yyyymmdd,
                        devicestatusdaily.emitCnt.sum(),
                        devicestatusdaily.actuaterCnt.sum(),
                        devicestatusdaily.inputdoorjammingCnt.sum(),
                        devicestatusdaily.frontdoorsolopenCnt.sum(),
                        devicestatusdaily.fullLevel.avg()))
                .groupBy(devicestatusdaily.hh);

        if (deviceid != null){
            query.where(devicestatusdaily.deviceid.in(deviceid));
        }

        if (!sendDate.equals("")){
            query.where(devicestatusdaily.yyyymmdd.eq(sendDate));
        }

        return query.fetch();
    }

    // 일자별장비현황 횟수평균,배출량평균 Querydsl
    @Override
    public List<DevicestatsDailyMonthDto> queryDslDeviceDailyMonth(String deviceid,String deviceMonth) {

        QDevicestatusdaily devicestatusdaily = QDevicestatusdaily.devicestatusdaily;

        JPQLQuery<DevicestatsDailyMonthDto> query = from(devicestatusdaily)
                .select(Projections.constructor(DevicestatsDailyMonthDto.class,
                        devicestatusdaily.hh,
                        devicestatusdaily.actuaterCnt.sum(),
                        devicestatusdaily.inputdoorjammingCnt.sum(),
                        devicestatusdaily.frontdoorsolopenCnt.sum(),
                        devicestatusdaily.emitCnt.sum(),
                        devicestatusdaily.fullLevel.avg()))
                .groupBy(devicestatusdaily.hh);

        if (deviceid != null){
            query.where(devicestatusdaily.deviceid.eq(deviceid));
        }
        if (deviceMonth != null){
            query.where(devicestatusdaily.yyyymmdd.likeIgnoreCase(deviceMonth.concat("%")));
        }

        return query.fetch();
    }

}
