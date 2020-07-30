package kr.co.broadwave.aci.devicestats.errweight;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Minkyu
 * Date : 2020-07-30
 * Time :
 * Remark :
 */
@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="it_itainer_err_data")
public class ErrweightData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="deviceid")
    private String deviceid; // 장비코드

    @Column(name="event_time")
    private String eventTime; // 이벤트발생시간

    @Column(name="err01_cnt")
    private Integer err01Cnt; // TD001S - 터치 스크린 디바이스 오류휫수

    @Column(name="err02_cnt")
    private Integer err02Cnt; // KD001S - 키패드 디바이스 상태

    @Column(name="err03_cnt")
    private Integer err03Cnt; // ED001S - 임베디드 보드 디바이스 상태

    @Column(name="err04_cnt")
    private Integer err04Cnt; // ED005S - 결제 도어 오류

    @Column(name="err05_cnt")
    private Integer err05Cnt; // ED008S - 폐기물 도어 오류

    @Column(name="err06_cnt")
    private Integer err06Cnt; // ED011S - 저울 디바이스 에러

    @Column(name="err07_cnt")
    private Integer err07Cnt; // PI001S - 결제 디바이스 오류

    @Column(name="err08_cnt")
    private Integer err08Cnt; // PI004C - 티머니 결제 오류

    @Column(name="err09_cnt")
    private Integer err09Cnt; // PI005C - 신용카드 결제 오류

    @Column(name="err10_cnt")
    private Integer err10Cnt; // LD001C - LTE 디바이스 오류

    @Column(name="insert_date")
    private LocalDateTime insert_date;

}
