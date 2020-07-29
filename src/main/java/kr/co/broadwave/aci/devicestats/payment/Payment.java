package kr.co.broadwave.aci.devicestats.payment;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Minkyu
 * Date : 2020-07-28
 * Time :
 * Remark : iTainer 결제 데이타 엔티티
 */
@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="it_itainer_payment_data")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id; // iTainer 결제 데이타 테이블 고유ID

    @Column(name="deviceid")
    private String deviceid; // 장비코드

    @Column(name="event_trid")
    private String eventTrid; // 이벤트 아이디

    @Column(name="event_time")
    private String eventTime; // 이벤트 보고 시간

    @Column(name="zone")
    private String zone; // 권역코드

    @Column(name="zone_name")
    private String zoneName; // 권역명

    @Column(name="base")
    private String base; // 거점코드

    @Column(name="base_name")
    private String baseName; // 거점명

    @Column(name="dis_stime")
    private String disStime; // 배출 시작 시간

    @Column(name="dis_etime")
    private String disEtime; // 배출 완료 시간

    @Column(name="dis_result")
    private String disResult; // 배출 결과, rst_code는 에러코드 참조

    @Column(name="dis_weight")
    private Double disWeight; // 폐기물 무게(단위 g)

    @Column(name="dis_amount")
    private Double disAmount; // 실제 결제된 금액

    @Column(name="dis_pre_amt")
    private Double disPreAmt; // 선결제금액

    @Column(name="dis_pay_amt")
    private Double disPayAmt; // 결제금액

    @Column(name="dis_cancel_amt")
    private Double disCancelAmt; // 취소금액

    @Column(name="dis_method")
    private String disMethod; // 배출 결제 방법

    @Column(name="dis_terminal")
    private String disTerminal; // 결제기 터미널 ID

    @Column(name="dis_cardno")
    private String disCardno; // pay_method가 fr이 아닌 경우 결제카드번호

    @Column(name="prepay_result")
    private String prepayResult; // 선결제 정보

    @Column(name="pay_result")
    private String payResult; // 결제정보

    @Column(name="cancel_result")
    private String cancelResult; // 취소정보

    @Column(name="tmpay_result")
    private String tmpayResult; // tmpay_result

    @Column(name="insert_date")
    private LocalDateTime insertDateTime;

}
