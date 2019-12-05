package kr.co.broadwave.aci.files;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author InSeok
 * Date : 2019-12-05
 * Remark :
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FileUploadDto {
    private Long id;
    private String fileName;
    private String saveFileName;
    private String filePath;
    private String contentType;
    private Long size;

}
