package kr.co.broadwave.aci.mastercode;

import kr.co.broadwave.aci.bscodes.CodeType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author InSeok
 * Date : 2019-07-31
 * Remark :
 */
public interface MasterCodeRepository extends JpaRepository<MasterCode,Long> {
    Optional<MasterCode> findByAndCodeTypeAndCode(CodeType codeType,String code);
    List<MasterCode> findByAndCodeType(CodeType codeType);

}
