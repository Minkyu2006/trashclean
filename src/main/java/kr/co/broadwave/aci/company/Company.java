package kr.co.broadwave.aci.company;

import kr.co.broadwave.aci.accounts.AccountRole;
import kr.co.broadwave.aci.bscodes.ApprovalType;
import kr.co.broadwave.aci.bscodes.DivisionType;
import kr.co.broadwave.aci.bscodes.RegionalType;
import kr.co.broadwave.aci.mastercode.MasterCode;
import kr.co.broadwave.aci.teams.Team;
import lombok.*;

import javax.persistence.*;
import javax.swing.plaf.synth.Region;
import java.time.LocalDateTime;

/**
 * @author Minkyu
 * Date : 2019-10-31
 * Time : 16:45
 * Remark : 업체등록 클래스
 */
@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="bs_company")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="cs_id")
    private Long id; // 업체관리 고유ID

    @Column(unique = true,name="cs_number")
    private String csNumber; // 관리코드

    @Column(name="cs_operator")
    private String csOperator; // 운영사명

    @Column(name="cs_operator_sub")
    private String csOperatorSub; // 운영사명(약칭)

    @Enumerated(EnumType.STRING)
    @Column(name="cs_division")
    private DivisionType csDivision; // 업체구분

    @Enumerated(EnumType.STRING)
    @Column(name="cs_regional")
    private RegionalType csRegional; // 운영권역

    @Column(name="cs_representative")
    private String csRepresentative; // 대표자

    @Column(name="cs_buisness_number")
    private String csBuisnessNumber; // 사업자번호

    @Column(name="cs_manager")
    private String csManager; // 담당자

    @Column(name="cs_telephone")
    private String csTelephone; // 전화번호

    @Column(name="cs_fax")
    private String csFax; // 팩스번호

    @Column(name="insert_date")
    private LocalDateTime insertDateTime;

    @Column(name="insert_id")
    private String insert_id;

    @Column(name="modify_date")
    private LocalDateTime modifyDateTime;

    @Column(name="modify_id")
    private String modify_id;

}
