package kr.co.broadwave.aci.devicestats;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author Minkyu
 * Date : 2020-03-13
 * Remark :
 */
public interface FirmwareRepositoryCustom {
    Page<firmwareFileListDto> findByFirmwareListQuerydsl(Pageable pageable);
}