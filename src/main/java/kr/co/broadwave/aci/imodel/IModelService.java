package kr.co.broadwave.aci.imodel;

import kr.co.broadwave.aci.bscodes.CodeType;
import kr.co.broadwave.aci.company.CompanyListDto;
import kr.co.broadwave.aci.company.CompanyRepositoryCustom;
import kr.co.broadwave.aci.equipment.*;
import kr.co.broadwave.aci.files.FileUploadDto;
import kr.co.broadwave.aci.keygenerate.KeyGenerateService;
import kr.co.broadwave.aci.mastercode.MasterCode;
import kr.co.broadwave.aci.mastercode.MasterCodeDto;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Minkyu
 * Date : 2019-12-19
 * Time : 11:51
 * Remark : IModel Service
 */
@Slf4j
@Service
public class IModelService {
    private final ModelMapper modelMapper;
    private final IModelRepository iModelRepository;
    private final KeyGenerateService keyGenerateService;
    private final IModelRepositoryCustom iModelRepositoryCustom;

    @Autowired
    public IModelService(IModelRepository iModelRepository,
                         KeyGenerateService keyGenerateService,
                         IModelRepositoryCustom iModelRepositoryCustom,
                         ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        this.iModelRepositoryCustom = iModelRepositoryCustom;
        this.iModelRepository = iModelRepository;
        this.keyGenerateService = keyGenerateService;
    }

    public IModel save(IModel imodel) {
        if (imodel.getMdNumber() == null || imodel.getMdNumber().isEmpty()){
            String mdTypeCode = imodel.getMdType().getCode();

            Date now = new Date();
            SimpleDateFormat yyMM = new SimpleDateFormat("yyMM");
            String mdNumber = keyGenerateService.keyGenerate("bs_model",mdTypeCode+'-',imodel.getModify_id());
            imodel.setMdNumber(mdNumber);
        }
        return iModelRepository.save(imodel);
    }

    public Optional<IModel> findByMdNumber(String mdNumber) {
        return iModelRepository.findByMdNumber(mdNumber);
    }

    public Page<IModelListDto> findByIModelSearch(String mdName,Long emTypeId, Long mdTypeId, String mdRemark,Pageable pageable) {
        return iModelRepositoryCustom.findByIModelSearch(mdName,emTypeId,mdTypeId,mdRemark,pageable);
    }

    public IModelDto findById(Long id) {
        Optional<IModel> optionalIModel = iModelRepository.findById(id);
        if (optionalIModel.isPresent()) {
            return modelMapper.map(optionalIModel.get(), IModelDto.class);
        } else {
            return null;
        }
    }

    public void delete(IModel iModel) {
        iModelRepository.delete(iModel);
    }

    public List<IModelDto> findByEmType(MasterCode id) {
        List<IModel> iModels = iModelRepository.findByEmType(id);
        return iModels.stream()
                .map(iModel -> modelMapper.map(iModel, IModelDto.class)
                ).collect(Collectors.toList());
    }


    public Optional<IModel> findByModel(Long mdId) {
        return iModelRepository.findById(mdId);
    }
}
