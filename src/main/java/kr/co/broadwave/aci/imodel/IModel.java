package kr.co.broadwave.aci.imodel;

import kr.co.broadwave.aci.files.FileUpload;
import kr.co.broadwave.aci.mastercode.MasterCode;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Minkyu
 * Date : 2019-12-19
 * Time : 10:21
 * Remark : 모델등록 클래스
 */
@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="bs_model")
public class IModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="md_id")
    private Long id; // 모델 고유ID

    @ManyToOne(targetEntity = FileUpload.class,fetch = FetchType.EAGER)
    @JoinColumn(name = "md_fileid")
    private FileUpload mdFileid;

    @Column(unique = true,name="md_number")
    private String mdNumber; // 모델 번호

    @Column(name="md_name")
    private String mdName; // 모델명

    @ManyToOne(targetEntity = MasterCode.class,fetch = FetchType.EAGER)
    @JoinColumn(name="em_type")
    private MasterCode emType; // 모델타입

    @ManyToOne(targetEntity = MasterCode.class,fetch = FetchType.EAGER)
    @JoinColumn(name="md_type")
    private MasterCode mdType; // 모델종류

    @Column(name="md_subname")
    private String mdSubname; // 모델약칭

    @Column(name="md_remark")
    private String mdRemark; // 모델특이사항

    @Column(name="insert_date")
    private LocalDateTime insertDateTime;

    @Column(name="insert_id")
    private String insert_id;

    @Column(name="modify_date")
    private LocalDateTime modifyDateTime;

    @Column(name="modify_id")
    private String modify_id;

}
