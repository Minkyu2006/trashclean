package kr.co.broadwave.aci.devicestats.frimware;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author Minkyu
 * Date : 2020-03-13
 * Remark :
 */
public interface FirmwareRepositoryCustom {
    Page<FirmwareFileListDto> findByFirmwareListQuerydsl(Pageable pageable);
}