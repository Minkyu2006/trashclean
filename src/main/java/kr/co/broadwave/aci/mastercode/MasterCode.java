package kr.co.broadwave.aci.mastercode;

import kr.co.broadwave.aci.bscodes.CodeType;
import kr.co.broadwave.aci.files.FileUpload;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author InSeok
 * Date : 2019-07-31
 * Remark :
 * @Table(name="bs_code"
 * //        , uniqueConstraints = {
 * //        @UniqueConstraint(columnNames = {"bc_code_type","bc_code"})
 * //}
 * )
 */
@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="bs_code")
public class MasterCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="bc_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name="bc_code_type")
    private CodeType codeType;

    @Column(name="bc_code")
    private String code;

    @Column(name="bc_name")
    private String name;

    @Column(name="bc_remark")
    private String remark;

    @Column(name="insert_date")
    private LocalDateTime insertDateTime;

    @Column(name="insert_id")
    private String insert_id;

    @Column(name="modify_date")
    private LocalDateTime modifyDateTime;

    @Column(name="modify_id")
    private String modify_id;

    @Column(name="bc_ref_1")
    private String bcRef1;

    @Column(name="bc_ref_2")
    private String bcRef2;

    @Column(name="bc_ref_3")
    private String bcRef3;

    @Column(name="bc_ref_4")
    private String bcRef4;

}
