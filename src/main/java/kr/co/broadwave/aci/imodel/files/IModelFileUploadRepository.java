package kr.co.broadwave.aci.imodel.files;

import kr.co.broadwave.aci.files.FileUpload;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Minkyu
 * Date : 2019-12-19
 * Remark :
 */
public interface IModelFileUploadRepository extends JpaRepository<IModelFileUpload,Long> {

}
