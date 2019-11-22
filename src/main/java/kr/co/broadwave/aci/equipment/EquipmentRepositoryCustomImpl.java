package kr.co.broadwave.aci.equipment;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import kr.co.broadwave.aci.company.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Minkyu
 * Date : 2019-11-01
 * Remark :
 */
@Repository
public class EquipmentRepositoryCustomImpl extends QuerydslRepositorySupport implements EquipmentRepositoryCustom{

    public EquipmentRepositoryCustomImpl() {
        super(Company.class);
    }

    @Override
    public Page<EquipmentListDto> findByEquipmentSearch
            (String emNumber, String emDesignation, Long emTypeId,Long emCountryId, Pageable pageable){

        QEquipment equipment = QEquipment.equipment;

        JPQLQuery<EquipmentListDto> query = from(equipment)
                .select(Projections.constructor(EquipmentListDto.class,
                        equipment.id,
                        equipment.emNumber,
                        equipment.emCerealNumber,
                        equipment.emDesignation,
                        equipment.emType,
                        equipment.emCountry,
                        equipment.emLocation,
                        equipment.emAwsNumber,
                        equipment.emEmbeddedNumber,
                        equipment.company
                ));


        // 검색조건필터
        if (emNumber != null && !emNumber.isEmpty()){
            query.where(equipment.emNumber.likeIgnoreCase(emNumber.concat("%")));
        }
        if (emDesignation != null && !emDesignation.isEmpty()){
            query.where(equipment.emDesignation.containsIgnoreCase(emDesignation));
        }
        if (emTypeId != null ){
            query.where(equipment.emType.id.eq(emTypeId));
        }
        if (emCountryId != null ){
            query.where(equipment.emCountry.id.eq(emCountryId));
        }

        query.orderBy(equipment.id.desc());

        final List<EquipmentListDto> equipments = getQuerydsl().applyPagination(pageable, query).fetch();
        return new PageImpl<>(equipments, pageable, query.fetchCount());
    }
}
