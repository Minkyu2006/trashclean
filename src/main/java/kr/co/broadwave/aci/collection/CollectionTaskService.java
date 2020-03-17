package kr.co.broadwave.aci.collection;

import kr.co.broadwave.aci.accounts.AccountRole;
import kr.co.broadwave.aci.bscodes.ProcStatsType;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author Minkyu
 * Date : 2020-01-23
 * Time :
 * Remark : CollectionTaskService
 */
@Service
public class CollectionTaskService {
    private final ModelMapper modelMapper;
    private final CollectionTaskRepositoryCustom collectionTaskRepositoryCustom;
    private final CollectionTaskRepository collectionTaskRepository;

    @Autowired
    public CollectionTaskService(CollectionTaskRepository collectionTaskRepository,
                                 CollectionTaskRepositoryCustom collectionTaskRepositoryCustom,
                                 ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        this.collectionTaskRepositoryCustom = collectionTaskRepositoryCustom;
        this.collectionTaskRepository = collectionTaskRepository;
    }
    public CollectionTask save(CollectionTask collectionTask) {
        return collectionTaskRepository.save(collectionTask);
    }

    public Page<CollectionListDto> findByCollectionList(String ctCode, String dateFrom, String dateTo, Long emTypeId, String userName, String vehicleNumber, Pageable pageable) {
        return collectionTaskRepositoryCustom.findByCollectionList(ctCode,dateFrom,dateTo,emTypeId,userName,vehicleNumber,pageable);
    }

    public CollectionInfoDto findById(Long id) {
        Optional<CollectionTask> optionalEquipment = collectionTaskRepository.findById(id);
        return optionalEquipment.map(collectionTask -> modelMapper.map(collectionTask, CollectionInfoDto.class)).orElse(null);
    }

    public List<CollectionInfoDto> findByCollectionInfoQueryDsl(String ctCode) {
        return collectionTaskRepositoryCustom.findByCollectionInfoQueryDsl(ctCode);
    }

    public void delete(CollectionTask collectionTask) {
        collectionTaskRepository.delete(collectionTask);
    }


    public Page<CollectionTaskListDateDto> findByCollectionsTaskDateList(String currentuserid, AccountRole role, ProcStatsType procStatsType, Pageable pageable) {
        return collectionTaskRepositoryCustom.findByCollectionsTaskDateList(currentuserid,role,procStatsType,pageable);
    }

    public List<CollectionTaskListDeviceDto> findByCollectionsTaskDeviceList(String ctCode,String currentuserid, AccountRole role){
        return collectionTaskRepositoryCustom.findByCollectionsTaskDeviceList(ctCode,currentuserid,role);
    }

    public CollectionTaskListDto findByCollectionsTaskInfoList(Long id){
        return collectionTaskRepositoryCustom.findByCollectionsTaskInfoList(id);
    }

    public CollectionTaskListInfoDto findByCollectionListInfoQueryDsl(Long id) {
        return collectionTaskRepositoryCustom.findByCollectionListInfoQueryDsl(id);
    }

    public Optional<CollectionTask> findById2(Long receiveId) {
        return collectionTaskRepository.findById(receiveId);
    }

    public List<CollectionDto> findByCtCodeSeqQuerydsl(String ctCode) {
        return  collectionTaskRepositoryCustom.findByCtCodeSeqQuerydsl(ctCode);
    }

    public List<CollectionMoniteringListDto> moniteringQuerydsl(String ctCode) {
        return collectionTaskRepositoryCustom.moniteringQuerydsl(ctCode);
    }
}
