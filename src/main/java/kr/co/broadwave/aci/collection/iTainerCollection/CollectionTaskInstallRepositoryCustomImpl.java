package kr.co.broadwave.aci.collection.iTainerCollection;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.broadwave.aci.accounts.QAccount;
import kr.co.broadwave.aci.bscodes.AccordiType;
import kr.co.broadwave.aci.mastercode.QMasterCode;
import kr.co.broadwave.aci.position.QPosition;
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
 * Date : 2020-06-02
 * Remark :
 */
@Slf4j
@Repository
public class CollectionTaskInstallRepositoryCustomImpl extends QuerydslRepositorySupport implements CollectionTaskInstallRepositoryCustom {

    public CollectionTaskInstallRepositoryCustomImpl() {
        super(CollectionTaskInstall.class);
    }

    @Override
    public Page<CollectionTaskInstallListDto> findByCollectionTaskInstallSearch
            (AccordiType ciTypes, Long ciPriorityId, String ciCode, String psBaseCode, String deviceid, Pageable pageable){

        QCollectionTaskInstall collectionTaskInstall = QCollectionTaskInstall.collectionTaskInstall;
        QAccount account = QAccount.account;
        QVehicle vehicle = QVehicle.vehicle;
        QMasterCode masterCode = QMasterCode.masterCode;

        JPQLQuery<CollectionTaskInstallListDto> query = from(collectionTaskInstall)
                .innerJoin(collectionTaskInstall.ciPriority,masterCode)
                .innerJoin(collectionTaskInstall.accountId,account)
                .innerJoin(collectionTaskInstall.vehicleId,vehicle)
                .select(Projections.constructor(CollectionTaskInstallListDto.class,
                        collectionTaskInstall.ciCode,
                        collectionTaskInstall.ciType,
                        masterCode.name,
                        collectionTaskInstall.psBaseCode,
                        collectionTaskInstall.deviceid,
                        account.username,
                        vehicle.vcNumber,
                        collectionTaskInstall.ciStatus
                ));

        // 검색조건필터
        if (ciTypes != null ){
            query.where(collectionTaskInstall.ciType.eq(ciTypes));
        }
        if (ciPriorityId != null ){
            query.where(collectionTaskInstall.ciPriority.id.eq(ciPriorityId));
        }
        if (ciCode != null && !ciCode.isEmpty()){
            query.where(collectionTaskInstall.ciCode.likeIgnoreCase(ciCode.concat("%")));
        }
        if (psBaseCode != null && !psBaseCode.isEmpty()){
            query.where(collectionTaskInstall.psBaseCode.likeIgnoreCase(psBaseCode.concat("%")));
        }
        if (deviceid != null && !deviceid.isEmpty()){
            query.where(collectionTaskInstall.deviceid.likeIgnoreCase(deviceid.concat("%")));
        }

        query.orderBy(collectionTaskInstall.id.desc());

        final List<CollectionTaskInstallListDto> collectionTaskInstallListDtos = Objects.requireNonNull(getQuerydsl()).applyPagination(pageable, query).fetch();
        return new PageImpl<>(collectionTaskInstallListDtos, pageable, query.fetchCount());
    }

    @Override
    public List<CollectionTaskInstallDto> findByCollectionTaskInstallInfo(String ciCode) {

        JPAQueryFactory queryFactory = new JPAQueryFactory(this.getEntityManager());

        QCollectionTaskInstall collectionTaskInstall = QCollectionTaskInstall.collectionTaskInstall;
        QAccount account = QAccount.account;
        QVehicle vehicle = QVehicle.vehicle;
        QMasterCode masterCode = QMasterCode.masterCode;
        QPosition position = QPosition.position;

        return queryFactory.select(Projections.constructor(CollectionTaskInstallDto.class,
                collectionTaskInstall.ciCode,collectionTaskInstall.ciType,masterCode.code,
                collectionTaskInstall.psBaseCode,position.psBaseName,collectionTaskInstall.deviceid,
                account.userid,account.username,vehicle.vcNumber,vehicle.vcName,
                collectionTaskInstall.ciRemark,collectionTaskInstall.ciStatus))
                .from(collectionTaskInstall)
                .where(collectionTaskInstall.ciCode.eq(ciCode))
                .innerJoin(collectionTaskInstall.psId,position)
                .innerJoin(collectionTaskInstall.accountId,account)
                .innerJoin(collectionTaskInstall.vehicleId,vehicle)
                .innerJoin(collectionTaskInstall.ciPriority,masterCode)
                .fetch();
    }

    @Override
    public List<CollectionTaskInstallCheckDto> findByPsBaseCodeCheck(String psBaseCode) {

        JPAQueryFactory queryFactory = new JPAQueryFactory(this.getEntityManager());

        QCollectionTaskInstall collectionTaskInstall = QCollectionTaskInstall.collectionTaskInstall;

        return queryFactory.select(Projections.constructor(CollectionTaskInstallCheckDto.class,
                collectionTaskInstall.ciCode,
                collectionTaskInstall.psBaseCode,
                collectionTaskInstall.ciStatus))
                .from(collectionTaskInstall)
                .where(collectionTaskInstall.psBaseCode.eq(psBaseCode))
                .fetch();
    }

}

