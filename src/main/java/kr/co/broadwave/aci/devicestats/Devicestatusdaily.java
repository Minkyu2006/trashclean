package kr.co.broadwave.aci.devicestats;

import kr.co.broadwave.aci.equipment.Equipment;
import kr.co.broadwave.aci.mastercode.MasterCode;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Minkyu
 * Date : 2020-01-10
 * Time : 장비(iSolarbin,iTainer 등  통계데이터)
 * Remark : 장비등록 클래스
 */
@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="ei_equipment_stats_daily")
public class Devicestatusdaily {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private String id; // 발생년월일시간+장비코드

    @Column(name="yyyymmdd")
    private String yyyymmdd; // 발생 년월일

    @Column(name="yyyymmddhh")
    private String yyyymmddhh; // 발생 년월일 시간

    @Column(name="hh")
    private String hh; // 시간

    @ManyToOne(targetEntity = Equipment.class,fetch = FetchType.LAZY)
    @JoinColumn(name="em_id")
    private Equipment emId; // 장비코드(PK)

    @Column(name="deviceid")
    private String deviceid; // 장비코드

    @ManyToOne(targetEntity = MasterCode.class,fetch = FetchType.LAZY)
    @JoinColumn(name="devicetype")
    private MasterCode devicetype; // 장비타입

    @Column(name="emit_cnt")
    private Double emitCnt; // 일일장비 배출횟수(쓰레기배출)

    @Column(name="actuater_cnt")
    private Double actuaterCnt; // 엑추에이터 작동횟수

    @Column(name="inputdooropen_cnt")
    private Double inputdooropenCnt; // 투입구 문열림 횟수

    @Column(name="inputdoorjamming_cnt")
    private Double inputdoorjammingCnt; // 투입구 걸림 횟수

    @Column(name="frontdoormagopen_cnt")
    private Double frontdoormagopenCnt; // 마그네틱센서열림횟수

    @Column(name="frontdoorsolopen_cnt")
    private Double frontdoorsolopenCnt; // 솔레노이드센서 열림 횟수

    @Column(name="gesture_cnt")
    private Double gestureCnt; // 제스쳐센서 인식 횟수

    @Column(name="full_level")
    private Double fullLevel; // 해당일자의 쓰레기양(%)

    @Column(name="insert_date")
    private LocalDateTime insert_date; // 데이터 업데이트 일시

    @Column(name="insert_id")
    private String insert_id; // 데이터 생성자

}
