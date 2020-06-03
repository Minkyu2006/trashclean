package kr.co.broadwave.aci.devicestats;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import kr.co.broadwave.aci.files.QFileUpload;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

/**
 * @author Minkyu
 * Date : 2020-03-13
 * Remark :
 */
@Repository
public class FirmwareRepositoryCustomImp extends QuerydslRepositorySupport implements FirmwareRepositoryCustom{

    public FirmwareRepositoryCustomImp() {
        super(Devicestatus.class);
    }

    @Override
    public Page<firmwareFileListDto> findByFirmwareListQuerydsl(Pageable pageable){

        QFirmware firmware = QFirmware.firmware;
        QFileUpload fileUpload = QFileUpload.fileUpload;

        JPQLQuery<firmwareFileListDto> query = from(firmware)
                .leftJoin(firmware.efFileid,fileUpload)
                .select(Projections.constructor(firmwareFileListDto.class,
                        firmware.id,
                        fileUpload.fileFullPath,
                        firmware.efType,
                        firmware.efVer,
                        firmware.efRemark
                ));

        query.orderBy(firmware.id.desc());

        final List<firmwareFileListDto> firmwares = Objects.requireNonNull(getQuerydsl()).applyPagination(pageable, query).fetch();
        return new PageImpl<>(firmwares, pageable, query.fetchCount());
    }

}
