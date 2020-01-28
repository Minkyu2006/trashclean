package kr.co.broadwave.aci.collection;

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
        if ( collectionTask.getCtCode() == null || collectionTask.getCtCode().isEmpty()){
            SimpleDateFormat todayFormat = new SimpleDateFormat("yyMMdd");
            Date time = new Date();
            String today = todayFormat.format(time);
            String ctCode = keyGenerateService.keyGenerate("bs_collection", "TS"+today, collectionTask.getModify_id());
            collectionTask.setCtCode(ctCode);
        }
        return collectionTaskRepository.save(collectionTask);
    }

    public CollectionDto findByCtCode(String ctCode) {
        Optional<CollectionTask> optionalEquipment = collectionTaskRepository.findByCtCode(ctCode);
        return optionalEquipment.map(collectionTask -> modelMapper.map(collectionTask, CollectionDto.class)).orElse(null);
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

    public Optional<CollectionTask> findByCtCodeDel(String ctCode) {
        return collectionTaskRepository.findByCtCode(ctCode);
    }

    public void delete(CollectionTask collectionTask) {
        collectionTaskRepository.delete(collectionTask);
    }

}
