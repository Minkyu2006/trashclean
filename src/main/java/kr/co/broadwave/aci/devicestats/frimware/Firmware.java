package kr.co.broadwave.aci.devicestats.frimware;

import kr.co.broadwave.aci.files.FileUpload;
import kr.co.broadwave.aci.mastercode.MasterCode;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Minkyu
 * Date : 2020-03-12
 * Time :
 * Remark : 펌웨어업데이트 엔티티
 */
@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="bs_equipment_firmware")
public class Firmware {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ef_id")
    private Long id; // 버전관리 테이블 고유ID

    @ManyToOne(targetEntity = MasterCode.class,fetch = FetchType.EAGER)
    @JoinColumn(name="ef_type")
    private MasterCode efType; // 모델타입

    @Column(name="ef_ver")
    private String efVer; // 버전

    @ManyToOne(targetEntity = FileUpload.class,fetch = FetchType.EAGER)
    @JoinColumn(name = "ef_file")
    private FileUpload efFileid; // 업데이트파일

    @Column(name="ef_remark")
    private String efRemark; // 비고

    @Column(name="insert_date")
    private LocalDateTime insertDateTime;

    @Column(name="insert_id")
    private String insert_id;

    @Column(name="modify_date")
    private LocalDateTime modifyDateTime;

    @Column(name="modify_id")
    private String modify_id;

}
