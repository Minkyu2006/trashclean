package kr.co.broadwave.aci.position;

import kr.co.broadwave.aci.keygenerate.KeyGenerateService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Minkyu
 * Date : 2020-05-27
 * Time :
 * Remark : Position Service
 */
@Slf4j
@Service
public class PositionService {
    private final ModelMapper modelMapper;
    private final PositionRepository positionRepository;
    private final KeyGenerateService keyGenerateService;
    private final PositionRepositoryCustom positionRepositoryCustom;

    @Autowired
    public PositionService(PositionRepository positionRepository,
                           KeyGenerateService keyGenerateService,
                           PositionRepositoryCustom positionRepositoryCustom,
                           ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        this.positionRepository = positionRepository;
        this.positionRepositoryCustom = positionRepositoryCustom;
        this.keyGenerateService = keyGenerateService;
    }


    public Position save(Position position) {
        //장비코드 가공하기
        if (position.getPsBaseCode() == null || position.getPsBaseCode().isEmpty()){

            String psCountryCode = position.getPsCountry().getCode();
            String psLocationCode = position.getPsLocation().getCode();

            String baseCode = keyGenerateService.keyGenerate("bs_position",'Z'+psCountryCode+psLocationCode+'-',position.getModify_id());

            //고유 장비번호 저장이름 바꾸기 : 장비타입-국가-지역-순번
            position.setPsBaseCode(baseCode);
        }

        return positionRepository.save(position);
    }

    public Optional<Position> findByPsBaseCode(String psBaseCode) {
        return positionRepository.findByPsBaseCode(psBaseCode);
    }

    public Page<PositionListDto> findByPositionSearch(String psBaseCode, String psBaseName, Long psLocationId, Long psCountryId, Pageable pageable) {
        return positionRepositoryCustom.findByPositionSearch(psBaseCode,psBaseName,psLocationId,psCountryId,pageable);
    }

    public PositionDto findByPositionInfo(Long id) {
        Optional<Position> optionalPosition = positionRepository.findByPositionInfo(id);
        return optionalPosition.map(position -> modelMapper.map(position, PositionDto.class)).orElse(null);
    }

    public PositionPopDto findByPositionPopInfo(Long id) {
        Optional<Position> optionalPosition = positionRepository.findByPositionInfo(id);
        return optionalPosition.map(position -> modelMapper.map(position, PositionPopDto.class)).orElse(null);
    }

    public void delete(Position position) {
        positionRepository.delete(position);
    }

    public Page<PositionPopListDto> findByPositionPopSearch(Long psCountryId, Long psLocationId, String deviceid, Pageable pageable) {
        return positionRepositoryCustom.findByPositionPopSearch(psCountryId,psLocationId,deviceid,pageable);
    }
}
