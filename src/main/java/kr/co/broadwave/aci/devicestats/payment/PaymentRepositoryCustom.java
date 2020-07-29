package kr.co.broadwave.aci.devicestats.payment;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2020-03-13
 * Remark :
 */
public interface PaymentRepositoryCustom {
    List<PaymentListDto> findByPaymentListQuerydsl(String fromVal, String toVal, String deviceid, String basename);
}