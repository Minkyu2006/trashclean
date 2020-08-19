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
            (String emNumber, String emDesignation, Long emTypeId,Long emCountryId,String emDashboard, Pageable pageable){

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
                        equipment.mdId,
                        equipment.emDashboard
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
        if (emDashboard != null && !emDashboard.isEmpty() ){
            query.where(equipment.emDashboard.eq(emDashboard));
        }

        query.orderBy(equipment.id.desc());

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
    public List<EquipmentBaseListDto> findByBaseEquipmentSearch(String emNumber, Long emLocationId, String emTypeId, Long emCountryId){

        QEquipment equipment = QEquipment.equipment;

        JPQLQuery<EquipmentBaseListDto> query = from(equipment)
                .select(Projections.constructor(EquipmentBaseListDto.class,
                        equipment.emNumber,
//                        equipment.emType,
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
            query.where(equipment.emType.name.eq(emTypeId));
        }
        if (emCountryId != null ){
            query.where(equipment.emCountry.id.eq(emCountryId));
        }
        if (emLocationId != null ){
            query.where(equipment.emLocation.id.eq(emLocationId));
        }

        query.orderBy(equipment.emNumber.asc());

        return query.fetch();
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
                equipment.emLatitude,equipment.emLongitude,
                equipment.insertDateTime,equipment.insert_id,
                equipment.emCertificationNumber))
                .from(equipment)
                .where(equipment.emNumber.in(emNumbers))
                .fetch();
    }

    // 수거업무등록페이지 장비리스트쿼리
    @Override
    public List<EquipmentCollectionListDto> findByEquipmentCollectionQuerydsl(Long emTypeId, Long emCountryId,Long emLocationId,Pageable pageable) {

        QEquipment equipment = QEquipment.equipment;
        QMasterCode masterCode = QMasterCode.masterCode;

        JPQLQuery<EquipmentCollectionListDto> query = from(equipment)
                .leftJoin(equipment.emState,masterCode)
                .select(Projections.constructor(EquipmentCollectionListDto.class,
                        equipment.id,
                        equipment.emNumber,
                        equipment.emType,
                        equipment.emCountry,
                        equipment.emLocation,
                        equipment.mdId,
                        equipment.emState
//                        equipment.company,
//                        equipment.mdId.mdMaximumPayload,
//                        equipment.mdId.mdUnit
                ));

        // 검색조건필터
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

        return query.fetch();
    }

    // 수거업무등록페이지 대기장비리스트
    @Override
    public Page<EquipmentWaitingCollectionListDto> findByWaitingEquipmentCollectionQuerydsl(String emType, Long emCountryId, Long emLocationId, String emNumber, String emState, Pageable pageable) {

        QEquipment equipment = QEquipment.equipment;
        QMasterCode masterCode = QMasterCode.masterCode;

        JPQLQuery<EquipmentWaitingCollectionListDto> query = from(equipment)
                .leftJoin(equipment.emState,masterCode)
                .select(Projections.constructor(EquipmentWaitingCollectionListDto.class,
                        equipment.id,
                        equipment.emNumber,
                        equipment.emType,
                        equipment.emCountry,
                        equipment.emLocation,
                        equipment.mdId,
                        equipment.emState
                ));

        // 검색조건필터
        if (emNumber != null && !emNumber.isEmpty()){
            query.where(equipment.emNumber.likeIgnoreCase(emNumber.concat("%")));
        }
        if (emType != null ){
            query.where(equipment.emType.code.eq(emType));
        }
        if (emState != null ){
            query.where(equipment.emState.name.eq(emState));
        }
        if (emCountryId != null ){
            query.where(equipment.emCountry.id.eq(emCountryId));
        }
        if (emLocationId != null ){
            query.where(equipment.emLocation.id.eq(emLocationId));
        }

        query.orderBy(equipment.emNumber.asc());

        final List<EquipmentWaitingCollectionListDto> equipments = Objects.requireNonNull(getQuerydsl()).applyPagination(pageable, query).fetch();
        return new PageImpl<>(equipments, pageable, query.fetchCount());
    }

    // 라우팅한 장비의 정보가져오기 위한 쿼리dsl
    @Override
    public List<EquipmentCollectionRegDto> findByRoutingEmNumberQuerydsl(List<String> streetRouting) {

        QEquipment equipment = QEquipment.equipment;
        QMasterCode masterCode = QMasterCode.masterCode;
        QIModel iModel = QIModel.iModel;
//        System.out.println("streetRouting : "+streetRouting);
        JPQLQuery<EquipmentCollectionRegDto> query = from(equipment)
                .select(Projections.constructor(EquipmentCollectionRegDto.class,
                        equipment,equipment.emNumber,masterCode,iModel.mdSubname,iModel.mdType.name))
                .innerJoin(equipment.emType,masterCode)
                .innerJoin(equipment.mdId,iModel);

        if (streetRouting != null ){
            query.where(equipment.emNumber.in(streetRouting));
        }

        return query.fetch();
    }

    // 장비의 타입 종류가져오기
    @Override
    public EquipmentCollectionTypeDto findByRoutingEmTypeQuerydsl(String streetdevice) {

        QEquipment equipment = QEquipment.equipment;
        QIModel iModel = QIModel.iModel;
        JPQLQuery<EquipmentCollectionTypeDto> query = from(equipment)
                .select(Projections.constructor(EquipmentCollectionTypeDto.class,
                        equipment.emNumber,
                        iModel.mdName,
                        iModel.mdType.name))
                .innerJoin(equipment.mdId,iModel);

        if (streetdevice != null ){
            query.where(equipment.emNumber.eq(streetdevice));
        }

        return query.fetchOne();
    }

    @Override
    public List<EquipmentEmNumberListDto> findByDeviceNumber(String code){

        QEquipment equipment = QEquipment.equipment;

        JPQLQuery<EquipmentEmNumberListDto> query = from(equipment)
                .select(Projections.constructor(EquipmentEmNumberListDto.class,
                        equipment.emNumber
                ));

        // 검색조건필터
        if (code != null && !code.isEmpty()){
            query.where(equipment.emType.code.eq(code));
        }

        query.orderBy(equipment.id.desc());

        return query.fetch();
    }

}
