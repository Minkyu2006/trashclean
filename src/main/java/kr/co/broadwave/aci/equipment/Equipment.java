package kr.co.broadwave.aci.equipment;

import kr.co.broadwave.aci.accounts.AccountRole;
import kr.co.broadwave.aci.bscodes.ApprovalType;
import kr.co.broadwave.aci.bscodes.EmType;
import kr.co.broadwave.aci.bscodes.NowStateType;
import kr.co.broadwave.aci.mastercode.MasterCode;
import kr.co.broadwave.aci.teams.Team;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Minkyu
 * Date : 2019-10-31
 * Time : 16:45
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
@Table(name="bs_equipment")
public class Equipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="em_id")
    private Long id; // 장비관리 고유ID

    @Column(unique = true,name="em_number")
    private String emNumber; // 장비 번호

    @Column(name="em_cereal_number")
    private String emCerealNumber; // 장비 시리얼 번호

    @Column(name="em_designation")
    private String emDesignation; // 장비명칭

    @Enumerated(EnumType.STRING)
    @Column(name="em_type")
    private EmType emType; // 장비타입

    @Column(name="em_aws_number")
    private String emAwsNumber; // AWS상 Device ID

    @Column(name="em_embedded_number")
    private String emEmbeddedNumber; // 임베디드 기판 번호

    @Enumerated(EnumType.STRING)
    @Column(name="em_now_state")
    private NowStateType emNowState; // 현재상태

    @Column(name="em_agency")
    private String emAgency; // 소속운영사

    @Column(name="insert_date")
    private LocalDateTime insertDateTime;

    @Column(name="insert_id")
    private String insert_id;

    @Column(name="modify_date")
    private LocalDateTime modifyDateTime;

    @Column(name="modify_id")
    private String modify_id;
}
