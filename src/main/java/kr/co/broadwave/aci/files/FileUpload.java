package kr.co.broadwave.aci.files;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author InSeok
 * Date : 2019-12-05
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
@Table(name="bs_file")
public class FileUpload {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="bf_id")
    private Long id;

    @Column(name="bf_filename")
    private String fileName;

    @Column(name="bf_save_filename")
    private String saveFileName;

    @Column(name="bf_file_path")
    private String filePath;

    @Column(name="bf_file_full_path")
    private String fileFullPath;

    @Column(name="bf_content_type")
    private String contentType;

    @Column(name="bf_size")
    private Long size;

    @Column(name="insert_date")
    private LocalDateTime insertDateTime;
}
