package kr.co.broadwave.aci.position;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import kr.co.broadwave.aci.company.Company;
import kr.co.broadwave.aci.equipment.EquipmentBaseListDto;
import kr.co.broadwave.aci.equipment.EquipmentListDto;
import kr.co.broadwave.aci.equipment.QEquipment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

/**
 * @author Minkyu
 * Date : 2020-05-27
 * Remark :
 */
@Repository
public class PositionRepositoryCustomImpl extends QuerydslRepositorySupport implements PositionRepositoryCustom {

    public PositionRepositoryCustomImpl() {
        super(Company.class);
    }


    @Override
    public Page<PositionListDto> findByPositionSearch(String psZoneCode, String psZoneName, Long psLocationId, Long psCountryId, Pageable pageable){

        QPosition position = QPosition.position;

        JPQLQuery<PositionListDto> query = from(position)
                .select(Projections.constructor(PositionListDto.class,
                        position.id,
                        position.psZoneCode,
                        position.psCountry,
                        position.psLocation,
                        position.psZoneName,
                        position.psPostnumber,
                        position.psAddress,
                        position.psDetailAddress,
                        position.psLatitude,
                        position.psLongitude
                ));

        // 검색조건필터
        if (psZoneCode != null && !psZoneCode.isEmpty()){
            query.where(position.psZoneCode.likeIgnoreCase(psZoneCode.concat("%")));
        }
        if (psZoneName != null && !psZoneName.isEmpty()){
            query.where(position.psZoneName.likeIgnoreCase(psZoneName.concat("%")));
        }
        if (psCountryId != null ){
            query.where(position.psCountry.id.eq(psCountryId));
        }
        if (psLocationId != null ){
            query.where(position.psLocation.id.eq(psLocationId));
        }

        query.orderBy(position.id.desc());

        final List<PositionListDto> positionListDtos = Objects.requireNonNull(getQuerydsl()).applyPagination(pageable, query).fetch();
        return new PageImpl<>(positionListDtos, pageable, query.fetchCount());
    }

}
