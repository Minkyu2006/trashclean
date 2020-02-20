package kr.co.broadwave.aci.equipment;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.broadwave.aci.company.Company;
import kr.co.broadwave.aci.company.QCompany;
import kr.co.broadwave.aci.imodel.QIModel;
import kr.co.broadwave.aci.mastercode.QMasterCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

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
                        equipment.company,
                        equipment.mdId
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

        query.orderBy(equipment.emNumber.asc());

        final List<EquipmentListDto> equipments = Objects.requireNonNull(getQuerydsl()).applyPagination(pageable, query).fetch();
        return new PageImpl<>(equipments, pageable, query.fetchCount());
    }

    // 일자별 장비현황 검색의 따른 장비아이디 보내기
    @Override
    public List<EquipmentEmnumberDto> queryDslDeviceEmNumber(String emNumber, Long emTypeId, Long emCountryId, Long emLocationId) {

        QEquipment equipment = QEquipment.equipment;

        JPQLQuery<EquipmentEmnumberDto> query = from(equipment)
                .select(Projections.constructor(EquipmentEmnumberDto.class,
                        equipment.emNumber))
                .groupBy(equipment.emNumber);

        if (emNumber != null && !emNumber.isEmpty()){
            query.where(equipment.emNumber.likeIgnoreCase(emNumber.concat("%")));
        }
        if (emTypeId != null ){
            query.where(equipment.emType.id.eq(emTypeId));
        }
        if (emCountryId != null ){
            query.where(equipment.emCountry.id.eq(emCountryId));
        }
        if (emLocationId != null ){
            query.where(equipment.emLocation.id.eq(emLocationId));
        }

        return query.fetch();
    }

    @Override
    public Page<EquipmentBaseListDto> findByBaseEquipmentSearch(String emNumber, Long emLocationId, Long emTypeId, Long emCountryId, Pageable pageable){

        QEquipment equipment = QEquipment.equipment;

        JPQLQuery<EquipmentBaseListDto> query = from(equipment)
                .select(Projections.constructor(EquipmentBaseListDto.class,
                        equipment.emNumber,
                        equipment.emType,
                        equipment.emCountry,
                        equipment.emLocation,
                        equipment.mdId,
                        equipment.vInterval,
                        equipment.vPresstime,
                        equipment.vInputtime,
                        equipment.vSolenoidtime,
                        equipment.vYellowstart,
                        equipment.vRedstart
                ));

        // 검색조건필터
        if (emNumber != null && !emNumber.isEmpty()){
            query.where(equipment.emNumber.likeIgnoreCase(emNumber.concat("%")));
        }
        if (emTypeId != null ){
            query.where(equipment.emType.id.eq(emTypeId));
        }
        if (emCountryId != null ){
            query.where(equipment.emCountry.id.eq(emCountryId));
        }
        if (emLocationId != null ){
            query.where(equipment.emLocation.id.eq(emLocationId));
        }

        query.orderBy(equipment.emNumber.asc());

        final List<EquipmentBaseListDto> equipments = Objects.requireNonNull(getQuerydsl()).applyPagination(pageable, query).fetch();
        return new PageImpl<>(equipments, pageable, query.fetchCount());
    }

    // 장비기본값셋팅 리스트쿼리
    @Override
    public List<EquipmentBaseDto> EquipmentBaseSettingQuerydsl(List<String> emNumbers) {

        JPAQueryFactory queryFactory = new JPAQueryFactory(this.getEntityManager());

        QEquipment equipment = QEquipment.equipment;

        return queryFactory.select(Projections.constructor(EquipmentBaseDto.class,
                equipment.id,equipment.emNumber,
                equipment.emCerealNumber,equipment.emDesignation,
                equipment.emType,equipment.emCountry,equipment.emLocation,
                equipment.emAwsNumber, equipment.emEmbeddedNumber,
                equipment.company,equipment.mdId,
                equipment.emInstallDate,equipment.emSubName,
                equipment.emLatitude,equipment.emHardness,
                equipment.insertDateTime,equipment.insert_id,
                equipment.emCertificationNumber))
                .from(equipment)
                .where(equipment.emNumber.in(emNumbers))
                .fetch();
    }

    // 수거업무등록페이지 장비리스트쿼리
    @Override
    public Page<EquipmentCollectionListDto> findByEquipmentCollectionQuerydsl(String emNumber, Long emTypeId, Long emCountryId,Long emLocationId,Pageable pageable) {

        QEquipment equipment = QEquipment.equipment;

        JPQLQuery<EquipmentCollectionListDto> query = from(equipment)
                .select(Projections.constructor(EquipmentCollectionListDto.class,
                        equipment.emNumber,
                        equipment.emType,
                        equipment.emCountry,
                        equipment.emLocation,
                        equipment.mdId,
                        equipment.company,
                        equipment.mdId.mdMaximumPayload,
                        equipment.mdId.mdUnit
                ));

        // 검색조건필터
        if (emNumber != null && !emNumber.isEmpty()){
            query.where(equipment.emNumber.likeIgnoreCase(emNumber.concat("%")));
        }
        if (emTypeId != null ){
            query.where(equipment.emType.id.eq(emTypeId));
        }
        if (emCountryId != null ){
            query.where(equipment.emCountry.id.eq(emCountryId));
        }
        if (emLocationId != null ){
            query.where(equipment.emLocation.id.eq(emLocationId));
        }

        query.orderBy(equipment.emNumber.asc());

        final List<EquipmentCollectionListDto> equipments = Objects.requireNonNull(getQuerydsl()).applyPagination(pageable, query).fetch();
        return new PageImpl<>(equipments, pageable, query.fetchCount());
    }

}
