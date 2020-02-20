package kr.co.broadwave.aci.collection;

import kr.co.broadwave.aci.accounts.Account;
import kr.co.broadwave.aci.bscodes.ProcStatsType;
import kr.co.broadwave.aci.equipment.Equipment;
import kr.co.broadwave.aci.mastercode.MasterCode;
import kr.co.broadwave.aci.vehicle.Vehicle;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Minkyu
 * Date : 2020-01-23
 * Time :
 * Remark : 수거업무등록 클래스
 */
@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="cl_task")
public class CollectionTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id; // 수거등록 고유ID

    @Column(name="ct_code")
    private String ctCode; // 수거관리코드

    @Column(name="ct_seq")
    private Integer ctSeq; // 수거처리순번(무조건1)

    @Column(name="yyyymmdd")
    private String yyyymmdd; // 수거 예정일

    @ManyToOne(targetEntity = Equipment.class,fetch = FetchType.LAZY)
    @JoinColumn(name="em_id")
    private Equipment emId; // 장비코드(아이디값)PK

    @Column(name="deviceid")
    private String deviceid; // 장비타입 -> 선택한 장비코드의 emNumber 가져와서 등록

    @ManyToOne(targetEntity = MasterCode.class,fetch = FetchType.LAZY)
    @JoinColumn(name="devicetype")
    private MasterCode devicetype; // 모델타입록 -> 선택한 장비코드의 emType 가져와서 등록

    @Enumerated(EnumType.STRING)
    @Column(name="proc_stats")
    private ProcStatsType procStats; // 수거처리단계(ENUM CL01: 계획 CL02: 확정,CL03:수거중 CL04:완료)

    @ManyToOne(targetEntity = Account.class,fetch = FetchType.LAZY)
    @JoinColumn(name="account_id")
    private Account accountId; // 수거원 조회하고 -> 선택한 수거원 고유아이디값 등록

    @ManyToOne(targetEntity = Vehicle.class,fetch = FetchType.LAZY)
    @JoinColumn(name="vehicle_id")
    private Vehicle vehicleId; // 차량 조회하고 -> 선택한 차량 고유아이디값 등록

    @Column(name="complete_date")
    private LocalDateTime completeDateTime;

    @Column(name="insert_date")
    private LocalDateTime insertDateTime;

    @Column(name="insert_id")
    private String insert_id;

    @Column(name="modify_date")
    private LocalDateTime modifyDateTime;

    @Column(name="modify_id")
    private String modify_id;

}
