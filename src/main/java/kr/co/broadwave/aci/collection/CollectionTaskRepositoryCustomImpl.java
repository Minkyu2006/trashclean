package kr.co.broadwave.aci.collection;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.broadwave.aci.accounts.QAccount;
import kr.co.broadwave.aci.company.Company;
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
                        vehicle.vcNumber
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

        if (vehicleNumber != null && !vehicleNumber.isEmpty()){
            query.where(collectionTask.vehicleId.vcNumber.likeIgnoreCase(vehicleNumber.concat("%")));
        }

        if (userName != null && !userName.isEmpty()){
            query.where(collectionTask.accountId.username.eq(userName));
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


}

