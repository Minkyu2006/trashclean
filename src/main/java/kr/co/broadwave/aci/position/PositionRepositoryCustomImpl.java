package kr.co.broadwave.aci.position;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import kr.co.broadwave.aci.company.Company;
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
    public Page<PositionListDto> findByPositionSearch(String psBaseCode, String psBaseName, Long psLocationId, Long psCountryId, Pageable pageable){

        QPosition position = QPosition.position;

        JPQLQuery<PositionListDto> query = from(position)
                .select(Projections.constructor(PositionListDto.class,
                        position.id,
                        position.psBaseCode,
                        position.psCountry,
                        position.psLocation,
                        position.psBaseName,
                        position.psPostnumber,
                        position.psAddress,
                        position.psDetailAddress,
                        position.psLatitude,
                        position.psLongitude
                ));

        // 검색조건필터
        if (psBaseCode != null && !psBaseCode.isEmpty()){
            query.where(position.psBaseCode.likeIgnoreCase(psBaseCode.concat("%")));
        }
        if (psBaseName != null && !psBaseName.isEmpty()){
            query.where(position.psBaseName.likeIgnoreCase(psBaseName.concat("%")));
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

    @Override
    public Page<PositionPopListDto> findByPositionPopSearch(Long psCountryId, Long psLocationId, String deviceid, String division, Pageable pageable){

        QPosition position = QPosition.position;

        JPQLQuery<PositionPopListDto> query = from(position)
                .select(Projections.constructor(PositionPopListDto.class,
                        position.id,
                        position.psBaseName,
                        position.deviceid,
                        position.installdate
                ));

        // 검색조건필터
        if(division.equals("")){
            if(deviceid != null && !deviceid.isEmpty()){
                query.where(position.deviceid.likeIgnoreCase(deviceid.concat("%")));
            }
        }else if(division.equals("unoperation")){
            query.where(position.deviceid.isNull());
        }else{
            if(deviceid != null && !deviceid.isEmpty()){
                query.where(position.deviceid.likeIgnoreCase(deviceid.concat("%")));
            }else{
                query.where(position.deviceid.isNotNull());
            }
        }

        if (psCountryId != null ){
            query.where(position.psCountry.id.eq(psCountryId));
        }
        if (psLocationId != null ){
            query.where(position.psLocation.id.eq(psLocationId));
        }

        query.orderBy(position.id.desc());

        final List<PositionPopListDto> positionListDtos = Objects.requireNonNull(getQuerydsl()).applyPagination(pageable, query).fetch();
        return new PageImpl<>(positionListDtos, pageable, query.fetchCount());
    }

}
