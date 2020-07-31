package kr.co.broadwave.aci.devicestats.errweight;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import kr.co.broadwave.aci.devicestats.payment.Payment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2020-07-28
 * Remark :
 */
@Slf4j
@Repository
public class ErrweightRepositoryCustomImp extends QuerydslRepositorySupport implements ErrweightRepositoryCustom {

    public ErrweightRepositoryCustomImp() {
        super(Payment.class);
    }

    @Override
    public List<ErrweightDataDto> findByErrweighttDataListQuerydsl(ErrweightMapperDto errweightMapperDto,String fromVal, String toVal){

        QErrweightData errweightData = QErrweightData.errweightData;

        JPQLQuery<ErrweightDataDto> query = from(errweightData)
                .select(Projections.constructor(ErrweightDataDto.class,
                        errweightData.deviceid,
                        errweightData.err01Cnt.sum().multiply(errweightMapperDto.getErr01Weight()),
                        errweightData.err02Cnt.sum().multiply(errweightMapperDto.getErr02Weight()),
                        errweightData.err03Cnt.sum().multiply(errweightMapperDto.getErr03Weight()),
                        errweightData.err04Cnt.sum().multiply(errweightMapperDto.getErr04Weight()),
                        errweightData.err05Cnt.sum().multiply(errweightMapperDto.getErr05Weight()),
                        errweightData.err06Cnt.sum().multiply(errweightMapperDto.getErr06Weight()),
                        errweightData.err07Cnt.sum().multiply(errweightMapperDto.getErr07Weight()),
                        errweightData.err08Cnt.sum().multiply(errweightMapperDto.getErr08Weight()),
                        errweightData.err09Cnt.sum().multiply(errweightMapperDto.getErr09Weight()),
                        errweightData.err10Cnt.sum().multiply(errweightMapperDto.getErr10Weight())
                ));

        if(!fromVal.equals("") && !toVal.equals("")){
            query.where(errweightData.eventTime.between(fromVal,toVal));
        }else if(!fromVal.equals("")){
            query.where(errweightData.eventTime.goe(fromVal));
        }else if(!toVal.equals("")){
            query.where(errweightData.eventTime.loe(toVal));
        }

        query.groupBy(errweightData.deviceid);

        return query.fetch();
    }

}
