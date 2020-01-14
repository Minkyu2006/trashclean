package kr.co.broadwave.aci.devicestats;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
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
    public List<DevicestatsDto> getDevicestatsAvgQuerydsl(List<String> deviceid,String yyyymmdd1,String yyyymmdd2) {

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

}
