package kr.co.broadwave.aci.company;

import kr.co.broadwave.aci.mastercode.MasterCode;
import lombok.*;

import javax.persistence.*;
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

    @ManyToOne(targetEntity = MasterCode.class,fetch = FetchType.EAGER)
    @JoinColumn(name="cs_division")
    private MasterCode csDivision; // 업체구분

    @ManyToOne(targetEntity = MasterCode.class,fetch = FetchType.EAGER)
    @JoinColumn(name="cs_regional")
    private MasterCode csRegional; // 운영권역

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

    @Column(name="cs_latitude")
    private Double csLatitude; // 배차위치위도

    @Column(name="cs_hardness")
    private Double csHardness; // 배차위치경도

    @Column(name="insert_date")
    private LocalDateTime insertDateTime;

    @Column(name="insert_id")
    private String insert_id;

    @Column(name="modify_date")
    private LocalDateTime modifyDateTime;

    @Column(name="modify_id")
    private String modify_id;

}
