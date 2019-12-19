package kr.co.broadwave.aci.imodel.files;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Minkyu
 * Date : 2019-12-19
 * Remark :
 */
@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="bs_model_file")
public class IModelFileUpload {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="md_fileid")
    private Long id;

    @Column(name="md_filename")
    private String fileName;

    @Column(name="md_save_filename")
    private String saveFileName;

    @Column(name="md_file_path")
    private String filePath;

    @Column(name="md_file_full_path")
    private String fileFullPath;

    @Column(name="md_content_type")
    private String contentType;

    @Column(name="md_size")
    private Long size;

    @Column(name="insert_date")
    private LocalDateTime insertDateTime;
}
