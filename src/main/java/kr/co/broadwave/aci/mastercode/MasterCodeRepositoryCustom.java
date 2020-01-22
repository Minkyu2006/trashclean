package kr.co.broadwave.aci.mastercode;

import kr.co.broadwave.aci.bscodes.CodeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author InSeok
 * Date : 2019-08-02
 * Remark :
 */
public interface MasterCodeRepositoryCustom {
    Page<MasterCodeDto> findAllBySearchStrings(CodeType codeType, String code,String name, Pageable pageable);
}
