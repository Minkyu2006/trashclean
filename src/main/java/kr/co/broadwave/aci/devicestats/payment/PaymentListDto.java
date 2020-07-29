package kr.co.broadwave.aci.devicestats.payment;

import lombok.*;

/**
 * @author Minkyu
 * Date : 2020-07-28
 * Time :
 * Remark :  iTainer 결제 데이타 ListDto
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
public class PaymentListDto {

    private String deviceid; // 장비코드
    private String eventTrid; // 이벤트 아이디
    private String eventTime; // 이벤트 보고 시간
    private String baseName; // 권역명
    private String disResult; // 배출 결과, rst_code는 에러코드 참조
    private String disStime; // 배출 시작 시간
    private String disEtime; // 배출 완료 시간

    private String disMethod; // 결제방법
    private String disCardno; // pay_method가 fr이 아닌 경우 결제카드번호
    private Double disWeight; // 폐기물 무게(단위 g)
    private Double disPayAmt; // 결제금액
    private Double disPreAmt; // 선결제금액
    private Double disCancelAmt; // 취소금액

    public String getDisMethod() {
        return disMethod;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public String getEventTrid() {
        return eventTrid;
    }

    public String getEventTime() {
        return eventTime;
    }

    public String getBaseName() {
        return baseName;
    }

    public String getDisResult() {
        return disResult;
    }

    public String getDisStime() {
        return disStime;
    }

    public String getDisEtime() {
        return disEtime;
    }

    public String getDisCardno() {
        return disCardno;
    }

    public Double getDisWeight() {
        return disWeight;
    }

    public Double getDisPayAmt() {
        return disPayAmt;
    }

    public Double getDisPreAmt() {
        return disPreAmt;
    }

    public Double getDisCancelAmt() {
        return disCancelAmt;
    }
}
