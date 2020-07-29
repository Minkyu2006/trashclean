package kr.co.broadwave.aci.devicestats.payment;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
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
public class PaymentRepositoryCustomImp extends QuerydslRepositorySupport implements PaymentRepositoryCustom {

    public PaymentRepositoryCustomImp() {
        super(Payment.class);
    }

    @Override
    public List<PaymentListDto> findByPaymentListQuerydsl(String fromVal, String toVal, String deviceid, String basename){

        QPayment payment = QPayment.payment;

        JPQLQuery<PaymentListDto> query = from(payment)
                .select(Projections.constructor(PaymentListDto.class,
                        payment.deviceid,
                        payment.eventTrid,
                        payment.eventTime,
                        payment.baseName,
                        payment.disResult,
                        payment.disStime,
                        payment.disEtime,
                        payment.disMethod,
                        payment.disCardno,
                        payment.disWeight,
                        payment.disPayAmt,
                        payment.disPreAmt,
                        payment.disCancelAmt
                ));

        // 검색조건필터
        if (deviceid != null && !deviceid.isEmpty()){
            query.where(payment.deviceid.likeIgnoreCase(deviceid.concat("%")));
        }

        if (basename != null && !basename.isEmpty()){
            query.where(payment.baseName.likeIgnoreCase(basename.concat("%")));
        }

        if(!fromVal.equals("") && !toVal.equals("")){
            query.where(payment.eventTrid.substring(0,8).between(fromVal,toVal));
        }else if(!fromVal.equals("")){
            query.where(payment.eventTrid.substring(0,8).goe(fromVal));
        }else if(!toVal.equals("")){
            query.where(payment.eventTrid.substring(0,8).loe(toVal));
        }

        query.orderBy(payment.id.desc());

        return query.fetch();
    }

}
