package kr.co.broadwave.aci.collection.iTainerCollection;

import kr.co.broadwave.aci.accounts.Account;
import kr.co.broadwave.aci.bscodes.AccordiType;
import kr.co.broadwave.aci.mastercode.MasterCode;
import kr.co.broadwave.aci.vehicle.Vehicle;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Minkyu
 * Date : 2020-05-29
 * Time :
 * Remark : iTainer업무지시 테이블
 */
@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="cl_task_install")
public class CollectionTaskInstall {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id; // 고유ID

    @Column(name="ci_code")
    private String ciCode; // 작업코드

    @Enumerated(EnumType.STRING)
    @Column(name="ci_type")
    private AccordiType ciType; // 구분(배치,수거,배치/수거) -> enum관리

    @ManyToOne(targetEntity = MasterCode.class,fetch = FetchType.LAZY)
    @JoinColumn(name="ci_priority")
    private MasterCode ciPriority; // 우선순위(보통/긴급) - 마스터코드등록관리

    @Column(name="deviceid")
    private String deviceid; // 장비코드(iTainer)

    @Column(name="ps_zone_code")
    private String psZoneCode; // 거점코드

    @Column(name="ps_zone_name")
    private String psZoneName; // 거점명

    @ManyToOne(targetEntity = Account.class,fetch = FetchType.LAZY)
    @JoinColumn(name="account_id")
    private Account accountId; // 수거원 조회하고 -> 선택한 수거원 고유아이디값 등록

    @ManyToOne(targetEntity = Vehicle.class,fetch = FetchType.LAZY)
    @JoinColumn(name="vehicle_id")
    private Vehicle vehicleId; // 차량 조회하고 -> 선택한 차량 고유아이디값 등록

    @Column(name="ci_remark")
    private String ciRemark; // 특이사항

    @Column(name="ci_status")
    private String ciStatus; // 배치상태(지시|완료) -> enum관리

    @Column(name="ci_complete_date")
    private LocalDateTime ciCompleteDate; // 배치완료일자

    @Column(name="insert_date")
    private LocalDateTime insertDateTime;

    @Column(name="insert_id")
    private String insert_id;

    @Column(name="modify_date")
    private LocalDateTime modifyDateTime;

    @Column(name="modify_id")
    private String modify_id;

}
