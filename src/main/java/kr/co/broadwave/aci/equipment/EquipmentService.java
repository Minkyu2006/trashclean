package kr.co.broadwave.aci.equipment;

import kr.co.broadwave.aci.bscodes.DivisionType;
import kr.co.broadwave.aci.bscodes.EmType;
import kr.co.broadwave.aci.bscodes.NowStateType;
import kr.co.broadwave.aci.bscodes.RegionalType;
import kr.co.broadwave.aci.company.*;
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
 * Date : 2019-10-31
 * Time : 16:56
 * Remark : Equipment Service
 */
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
            Date now = new Date();
            SimpleDateFormat yyMM = new SimpleDateFormat("yyMM");
            String emNumber = keyGenerateService.keyGenerate("bs_equipment", yyMM.format(now), equipment.getModify_id());
            equipment.setEmNumber(emNumber);
        }
        return equipmentRepository.save(equipment);
    }

    public Optional<Equipment> findByEmNumber(String emNumber) {
        return equipmentRepository.findByEmNumber(emNumber);
    }

    public Page<EquipmentListDto> findByEquipmentSearch(String emNumber, String emDesignation, EmType emTypes, NowStateType nowStateType, Pageable pageable) {
        return equipmentRepositoryCystom.findByEquipmentSearch(emNumber,emDesignation,emTypes,nowStateType,pageable);
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
