package kr.co.broadwave.aci.collection.iTainerCollection;

import kr.co.broadwave.aci.bscodes.AccordiType;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author Minkyu
 * Date : 2020-06-01
 * Time :
 * Remark : CollectionTaskInstallService
 */
@Service
public class CollectionTaskInstallService {
    private final CollectionTaskInstallRepositoryCustom collectionTaskInstallRepositoryCustom;
    private final CollectionTaskInstallRepository collectionTaskInstallRepository;

    @Autowired
    public CollectionTaskInstallService(CollectionTaskInstallRepository collectionTaskInstallRepository,
                                        CollectionTaskInstallRepositoryCustom collectionTaskInstallRepositoryCustom) {
        this.collectionTaskInstallRepositoryCustom = collectionTaskInstallRepositoryCustom;
        this.collectionTaskInstallRepository = collectionTaskInstallRepository;
    }

    public CollectionTaskInstall save(CollectionTaskInstall collectionTaskInstall) {
        return collectionTaskInstallRepository.save(collectionTaskInstall);
    }

    public Page<CollectionTaskInstallListDto> findByCollectionTaskInstallSearch(AccordiType ciTypes, Long ciPriorityId, String ciCode, String psZoneCode, String deviceid, Pageable pageable) {
        return collectionTaskInstallRepositoryCustom.findByCollectionTaskInstallSearch(ciTypes,ciPriorityId,ciCode,psZoneCode,deviceid,pageable);
    }

    public List<CollectionTaskInstallDto> findByCollectionTaskInstallInfo(String ciCode) {
        return collectionTaskInstallRepositoryCustom.findByCollectionTaskInstallInfo(ciCode);
    }

    public Optional<CollectionTaskInstall> findByCiCode(String ciCode) {
        return collectionTaskInstallRepository.findByCiCode(ciCode);
    }

    public void delete(CollectionTaskInstall collectionTaskInstall) {
        collectionTaskInstallRepository.delete(collectionTaskInstall);
    }

}
