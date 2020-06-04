package kr.co.broadwave.aci.position;

import kr.co.broadwave.aci.mastercode.MasterCode;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Minkyu
 * Date : 2020-05-27
 * Time :
 * Remark : 거점등록 클래스
 */
@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="bs_position")
public class Position {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ps_id")
    private Long id; // 거점관리 고유ID

    @Column(unique = true,name="ps_base_code")
    private String psBaseCode; // 거점코드

    @ManyToOne(targetEntity = MasterCode.class,fetch = FetchType.EAGER)
    @JoinColumn(name="ps_country")
    private MasterCode psCountry; // 국가

    @ManyToOne(targetEntity = MasterCode.class,fetch = FetchType.EAGER)
    @JoinColumn(name="ps_location")
    private MasterCode psLocation; // 지역

    @Column(name="ps_base_name")
    private String psBaseName; // 거점명칭

    @Column(name="ps_postnumber")
    private String psPostnumber; // 우편번호

    @Column(name="ps_address")
    private String psAddress; // 주소

    @Column(name="ps_detail_address")
    private String psDetailAddress; // 상세주소

    @Column(name="ps_latitude")
    private Double psLatitude; // GPS위도

    @Column(name="ps_longitude")
    private Double psLongitude; // GPS경도

    @Column(name="deviceid")
    private String deviceid; // 장비코드

    @Column(name="installdate")
    private LocalDateTime installdate; // 배치(설치)일자

    @Column(name="insert_date")
    private LocalDateTime insertDateTime; // 작성일자

    @Column(name="insert_id")
    private String insert_id; // 작성자

    @Column(name="modify_date")
    private LocalDateTime modifyDateTime; // 변경일자

    @Column(name="modify_id")
    private String modify_id; // 변경자

}
