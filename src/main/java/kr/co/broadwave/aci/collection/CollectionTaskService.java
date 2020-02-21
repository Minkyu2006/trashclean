package kr.co.broadwave.aci.collection;

import kr.co.broadwave.aci.accounts.AccountRole;
import kr.co.broadwave.aci.bscodes.ProcStatsType;
import kr.co.broadwave.aci.keygenerate.KeyGenerateService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
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
    private final KeyGenerateService keyGenerateService;
    private final CollectionTaskRepositoryCustom collectionTaskRepositoryCustom;
    private final CollectionTaskRepository collectionTaskRepository;

    @Autowired
    public CollectionTaskService(CollectionTaskRepository collectionTaskRepository,
                                 KeyGenerateService keyGenerateService,
                                 CollectionTaskRepositoryCustom collectionTaskRepositoryCustom,
                                 ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        this.collectionTaskRepositoryCustom = collectionTaskRepositoryCustom;
        this.keyGenerateService = keyGenerateService;
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

    public CollectionInfoDto findByCollectionInfoQueryDsl(Long id) {
        return collectionTaskRepositoryCustom.findByCollectionInfoQueryDsl(id);
    }

    public void delete(CollectionTask collectionTask) {
        collectionTaskRepository.delete(collectionTask);
    }


    public Page<CollectionTaskListDto> findByCollectionsTaskList(String currentuserid, AccountRole role, ProcStatsType procStatsType, Pageable pageable) {
        return collectionTaskRepositoryCustom.findByCollectionsTaskList(currentuserid,role,procStatsType,pageable);
    }

    public CollectionTaskListInfoDto findByCollectionListInfoQueryDsl(Long id) {
        return collectionTaskRepositoryCustom.findByCollectionListInfoQueryDsl(id);
    }

    public Optional<CollectionTask> findById2(Long receiveId) {
        return collectionTaskRepository.findById(receiveId);
    }

    public CollectionDto findByCtCodeSeqQuerydsl(String ctCode, Integer collectionSeq) {
        return  collectionTaskRepositoryCustom.findByCtCodeSeqQuerydsl(ctCode,collectionSeq);
    }
}
