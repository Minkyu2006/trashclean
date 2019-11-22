package kr.co.broadwave.aci.equipment;

import kr.co.broadwave.aci.bscodes.*;
import kr.co.broadwave.aci.company.*;
import kr.co.broadwave.aci.keygenerate.KeyGenerateService;
import kr.co.broadwave.aci.mastercode.MasterCode;
import lombok.extern.slf4j.Slf4j;
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
 * Date : 2019-10-31
 * Time : 16:56
 * Remark : Equipment Service
 */
@Slf4j
@Service
public class EquipmentService {
    private final ModelMapper modelMapper;
    private final EquipmentRepository equipmentRepository;
    private final KeyGenerateService keyGenerateService;
    private final EquipmentRepositoryCustom equipmentRepositoryCustom;
    private final CompanyRepositoryCustom companyRepositoryCustom;

    @Autowired
    public EquipmentService(EquipmentRepository equipmentRepository,
                            KeyGenerateService keyGenerateService,
                            CompanyRepositoryCustom companyRepositoryCustom,
                            EquipmentRepositoryCustom equipmentRepositoryCustom,
                            ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        this.companyRepositoryCustom = companyRepositoryCustom;
        this.equipmentRepositoryCustom = equipmentRepositoryCustom;
        this.equipmentRepository = equipmentRepository;
        this.keyGenerateService = keyGenerateService;
    }

    public Equipment save(Equipment equipment) {
        //장비코드 가공하기
        if ( equipment.getEmNumber() == null || equipment.getEmNumber().isEmpty()){

            String emTypeCode = equipment.getEmType().getCode();
            String emCountryCode = equipment.getEmCountry().getCode();
            String emLocationCode = equipment.getEmLocation().getCode();

            Date now = new Date();
            SimpleDateFormat yyMM = new SimpleDateFormat("yyMM");
            String emNumber = keyGenerateService.keyGenerate("bs_equipment",emTypeCode+'-'+emCountryCode+'-'+emLocationCode+'-',equipment.getModify_id());

             //고유 장비번호 저장이름 바꾸기 : 장비타입-국가-지역-순번
            equipment.setEmNumber(emNumber);
        }
        return equipmentRepository.save(equipment);
    }

    public Optional<Equipment> findByEmNumber(String emNumber) {
        return equipmentRepository.findByEmNumber(emNumber);
    }

    public Page<EquipmentListDto> findByEquipmentSearch
            (String emNumber, String emDesignation, Long emTypeId,Long emCountryId, Pageable pageable) {
        return equipmentRepositoryCustom.findByEquipmentSearch(emNumber,emDesignation,emTypeId,emCountryId,pageable);
    }


    public EquipmentDto findById(Long id) {
        Optional<Equipment> optionalEquipment = equipmentRepository.findById(id);
        if (optionalEquipment.isPresent()) {
            return modelMapper.map(optionalEquipment.get(), EquipmentDto.class);
        } else {
            return null;
        }
    }

    public void delete(Equipment equipment) {
        equipmentRepository.delete(equipment);
    }

    public Page<CompanyListDto> findByAgencySearch(String csNumber, String csOperator, Pageable pageable) {
        return companyRepositoryCustom.findByAgencySearch(csNumber,csOperator,pageable);
    }


}
