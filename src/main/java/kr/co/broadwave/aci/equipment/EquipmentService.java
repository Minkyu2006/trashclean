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
    private final EquipmentRepositoryCystom equipmentRepositoryCystom;

    @Autowired
    public EquipmentService(EquipmentRepository equipmentRepository,
                            KeyGenerateService keyGenerateService,
                            EquipmentRepositoryCystom equipmentRepositoryCystom,
                            ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        this.equipmentRepositoryCystom = equipmentRepositoryCystom;
        this.equipmentRepository = equipmentRepository;
        this.keyGenerateService = keyGenerateService;
    }

    public Equipment save(Equipment equipment) {
        if ( equipment.getEmNumber() == null || equipment.getEmNumber().isEmpty()){

            log.info("장비타입 코드값 : "+equipment.getEmType().getCode());
            String emTypeCode = equipment.getEmType().getCode();
            String emCountryCode = equipment.getEmCountry().getCode();
            String emLocationCode = equipment.getEmLocation().getCode();
            String emNumber = keyGenerateService.keyGenerate("bs_equipment",equipment.getModify_id());
            equipment.setEmNumber(emTypeCode+'-'+emCountryCode+'-'+emLocationCode+'-'+emNumber);
        }
        return equipmentRepository.save(equipment);
    }

    public Optional<Equipment> findByEmNumber(String emNumber) {
        return equipmentRepository.findByEmNumber(emNumber);
    }

    public Page<EquipmentListDto> findByEquipmentSearch(String emNumber, String emDesignation,Pageable pageable) {
        return equipmentRepositoryCystom.findByEquipmentSearch(emNumber,emDesignation,pageable);
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

}
