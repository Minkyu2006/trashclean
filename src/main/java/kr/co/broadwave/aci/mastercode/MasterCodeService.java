package kr.co.broadwave.aci.mastercode;

import kr.co.broadwave.aci.bscodes.CodeType;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author InSeok
 * Date : 2019-08-02
 * Remark :
 */
@Service
public class MasterCodeService {

    private final MasterCodeRepository masterCodeRepository;
    private final MasterCodeRepositoryCustom masterCodeRepositoryCustom;
    private final ModelMapper modelMapper;

    @Autowired
    public MasterCodeService(MasterCodeRepository masterCodeRepository, MasterCodeRepositoryCustom masterCodeRepositoryCustom, ModelMapper modelMapper) {
        this.masterCodeRepository = masterCodeRepository;
        this.masterCodeRepositoryCustom = masterCodeRepositoryCustom;
        this.modelMapper = modelMapper;
    }


    //마스터코드저장
    public MasterCode save(MasterCode masterCode)
    {
        return masterCodeRepository.save(masterCode);
    }

    //코드리스트조회
    public Page<MasterCodeDto> findAllBySearchStrings(CodeType codeType, String code, String name, Pageable pageable){
        return masterCodeRepositoryCustom.findAllBySearchStrings(codeType,code,name,pageable);
    }

    //코드분류류와 코드 겁색조건으로 코드값 개별조회
    public Optional<MasterCode> findByCoAndCodeTypeAndCode(CodeType codeType,String code){
        return masterCodeRepository.findByAndCodeTypeAndCode(codeType,code);
    }
    public Optional<MasterCode> findById(Long id){
        return masterCodeRepository.findById(id);
    }

    public void delete(MasterCode masterCode) {
        masterCodeRepository.delete(masterCode);
    }

    public List<MasterCodeDto> findCodeList(CodeType codeType){
        List<MasterCode> masterCodes = masterCodeRepository.findByAndCodeType(codeType);

        return masterCodes.stream()
                .map(masterCode -> modelMapper.map(masterCode, MasterCodeDto.class)
                ).collect(Collectors.toList());
    }


    public List<MasterCodeDto> findAllByCodeTypeEqualsAndBcRef1(CodeType codeType, String emCountry) {
        List<MasterCode> masterCodes = masterCodeRepository.findAllByCodeTypeEqualsAndBcRef1(codeType,emCountry);

        return masterCodes.stream()
                .map(masterCode -> modelMapper.map(masterCode, MasterCodeDto.class)
                ).collect(Collectors.toList());
    }

    public Optional<MasterCode> findByCode(String emCountry) {
        return masterCodeRepository.findByCode(emCountry);
    }

    public Optional<MasterCode> findByBcRef1(String emCountry) {
        return masterCodeRepository.findByBcRef1(emCountry);
    }

}
