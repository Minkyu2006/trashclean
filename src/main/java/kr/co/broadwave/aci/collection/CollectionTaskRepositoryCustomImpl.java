package kr.co.broadwave.aci.collection;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.broadwave.aci.accounts.AccountRole;
import kr.co.broadwave.aci.accounts.QAccount;
import kr.co.broadwave.aci.bscodes.ProcStatsType;
import kr.co.broadwave.aci.company.Company;
import kr.co.broadwave.aci.company.QCompany;
import kr.co.broadwave.aci.equipment.QEquipment;
import kr.co.broadwave.aci.files.QFileUpload;
import kr.co.broadwave.aci.imodel.QIModel;
import kr.co.broadwave.aci.mastercode.QMasterCode;
import kr.co.broadwave.aci.vehicle.QVehicle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

/**
 * @author Minkyu
 * Date : 2020-01-28ø
 * Remark :
 */
@Slf4j
@Repository
public class CollectionTaskRepositoryCustomImpl extends QuerydslRepositorySupport implements CollectionTaskRepositoryCustom{

    public CollectionTaskRepositoryCustomImpl() {
        super(Company.class);
    }

    @Override
    public  Page<CollectionListDto> findByCollectionList(String ctCode, String dateFrom, String dateTo, Long emTypeId, String userName, String vehicleNumber, Pageable pageable){

        QCollectionTask collectionTask = QCollectionTask.collectionTask;
        QAccount account = QAccount.account;
        QVehicle vehicle = QVehicle.vehicle;
        QMasterCode masterCode = QMasterCode.masterCode;

        JPQLQuery<CollectionListDto> query = from(collectionTask)
                .innerJoin(collectionTask.devicetype,masterCode)
                .innerJoin(collectionTask.accountId,account)
                .innerJoin(collectionTask.vehicleId,vehicle)
                .select(Projections.constructor(CollectionListDto.class,
                        collectionTask.id,
                        collectionTask.ctCode,
                        collectionTask.yyyymmdd,
                        masterCode.name,
                        collectionTask.deviceid,
                        account.username,
                        vehicle.vcNumber,
                        collectionTask.completeDateTime
                ));

        // 검색조건필터
        if (ctCode != null && !ctCode.isEmpty()){
            query.where(collectionTask.ctCode.likeIgnoreCase(ctCode.concat("%")));
        }

        if(dateFrom != null && dateTo != null){
            query.where(collectionTask.yyyymmdd.between(dateFrom,dateTo));
        }else if(dateFrom != null){
            query.where(collectionTask.yyyymmdd.goe(dateFrom));
        }else if(dateTo != null){
            query.where(collectionTask.yyyymmdd.loe(dateTo));
        }

        if (emTypeId != null ){
            query.where(collectionTask.devicetype.id.eq(emTypeId));
        }

        if (userName != null && !userName.isEmpty()){
            query.where(collectionTask.accountId.username.likeIgnoreCase(userName.concat("%")));
        }

        if (vehicleNumber != null && !vehicleNumber.isEmpty()){
            query.where(collectionTask.vehicleId.vcNumber.likeIgnoreCase(vehicleNumber.concat("%")));
        }

        query.orderBy(collectionTask.id.desc());

        final List<CollectionListDto> collectionListDtos = Objects.requireNonNull(getQuerydsl()).applyPagination(pageable, query).fetch();
        return new PageImpl<>(collectionListDtos, pageable, query.fetchCount());

    }

    // 수거업무 보기버튼누를시 나오는 Querydsl
    @Override
    public CollectionInfoDto findByCollectionInfoQueryDsl(Long id) {

        JPAQueryFactory queryFactory = new JPAQueryFactory(this.getEntityManager());

        QCollectionTask collectionTask = QCollectionTask.collectionTask;

        return queryFactory.select(Projections.constructor(CollectionInfoDto.class,
                collectionTask.ctCode,collectionTask.yyyymmdd,
                collectionTask.deviceid,collectionTask.devicetype.name,
                collectionTask.accountId.username,collectionTask.accountId.userid,
                collectionTask.vehicleId.vcNumber,collectionTask.vehicleId.vcName))
                .from(collectionTask)
                .where(collectionTask.id.eq(id))
                .fetchOne();
    }

    // 수거업무리스트 Querydsl
    @Override
    public Page<CollectionTaskListDto> findByCollectionsTaskList(String currentuserid, AccountRole role, ProcStatsType procStatsType, Pageable pageable){

        //AccountRole admin = AccountRole.valueOf("ROLE_ADMIN");
        AccountRole subadmin = AccountRole.valueOf("ROLE_SUBADMIN");

        QCollectionTask collectionTask = QCollectionTask.collectionTask;
        QEquipment equipment = QEquipment.equipment;
        QCompany company = QCompany.company;
        QFileUpload fileUpload = QFileUpload.fileUpload;
        QIModel model = QIModel.iModel;

        JPQLQuery<CollectionTaskListDto> query = from(collectionTask)
                .innerJoin(collectionTask.emId,equipment)
                .innerJoin(collectionTask.emId.company,company)
                .innerJoin(collectionTask.emId.mdId,model)
                .leftJoin(model.mdFileid,fileUpload)
                .select(Projections.constructor(CollectionTaskListDto.class,
                        collectionTask.id,
                        collectionTask.ctCode,
                        collectionTask.yyyymmdd,
                        collectionTask.deviceid,
                        equipment.mdId.mdName,
                        equipment.mdId.mdType.name,
                        company.csOperator,
                        fileUpload.filePath,
                        fileUpload.saveFileName
                ));

        // 검색조건필터
        if(role != subadmin){
            if (role != null && currentuserid != null ){
                query.where(collectionTask.accountId.role.eq(role));
                query.where(collectionTask.accountId.userid.eq(currentuserid));
            }
        }

        if (procStatsType != null ){
            query.where(collectionTask.procStats.eq(procStatsType));
        }

        query.orderBy(collectionTask.yyyymmdd.desc());

        final List<CollectionTaskListDto> collections = Objects.requireNonNull(getQuerydsl()).applyPagination(pageable, query).fetch();
        return new PageImpl<>(collections, pageable, query.fetchCount());
    }

    // 수거업무리스트 수거업무버튼 누를시 나오는 Querydsl
    @Override
    public CollectionTaskListInfoDto findByCollectionListInfoQueryDsl(Long id) {

        JPAQueryFactory queryFactory = new JPAQueryFactory(this.getEntityManager());

        QCollectionTask collectionTask = QCollectionTask.collectionTask;
        QEquipment equipment = QEquipment.equipment;
        QCompany company = QCompany.company;
        QFileUpload fileUpload = QFileUpload.fileUpload;
        QIModel model = QIModel.iModel;

        return queryFactory.select(Projections.constructor(CollectionTaskListInfoDto.class,
                collectionTask.id,collectionTask.ctCode,collectionTask.ctSeq,collectionTask.yyyymmdd,
                collectionTask.deviceid,equipment.mdId.mdName,equipment.mdId.mdType.name,
                company.csOperator,fileUpload.filePath,fileUpload.saveFileName,
                equipment.emCountry.name,equipment.emLocation.name,model.mdMaximumPayload,model.mdUnit.name,
                collectionTask.procStats))
                .from(collectionTask)
                .innerJoin(collectionTask.emId,equipment)
                .innerJoin(collectionTask.emId.company,company)
                .innerJoin(collectionTask.emId.mdId,model)
                .leftJoin(model.mdFileid,fileUpload)
                .where(collectionTask.id.eq(id))
                .fetchOne();
    }

}

