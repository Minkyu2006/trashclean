package kr.co.broadwave.aci.files;

import com.amazonaws.services.s3.transfer.Upload;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author InSeok
 * Date : 2019-12-05
 * Remark :
 */
public interface FileUploadRepository extends JpaRepository<FileUpload,Long> {

}
