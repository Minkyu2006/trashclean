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
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileUploadDto {
    private Long id;
    private FileUpload fileUpload;
    private String fileName;
    private String saveFileName;
    private String filePath;
    private String fileFullPath;
    private String contentType;
    private Long size;

    public Long getId() {
        return id;
    }

    public FileUpload getFileUpload() {
        return fileUpload;
    }

    public String getFileName() {
        return fileName;
    }

    public String getSaveFileName() {
        return saveFileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getFileFullPath() {
        return fileFullPath;
    }

    public String getContentType() {
        return contentType;
    }

    public Long getSize() {
        return size;
    }
}
