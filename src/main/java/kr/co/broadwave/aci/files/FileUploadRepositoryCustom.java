package kr.co.broadwave.aci.files;

import java.util.List;

/**
 * @author InSeok
 * Date : 2019-12-05
 * Remark :
 */
public interface FileUploadRepositoryCustom {
    List<FileUploadDto> findTop10ByALL();

}
