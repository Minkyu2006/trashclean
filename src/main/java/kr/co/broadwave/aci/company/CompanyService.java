package kr.co.broadwave.aci.company;

import kr.co.broadwave.aci.bscodes.DivisionType;
import kr.co.broadwave.aci.bscodes.RegionalType;
import kr.co.broadwave.aci.keygenerate.KeyGenerateService;
import kr.co.broadwave.aci.teams.Team;
import kr.co.broadwave.aci.teams.TeamDto;
import kr.co.broadwave.aci.teams.TeamRepository;
import kr.co.broadwave.aci.teams.TeamRepositoryCustom;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author Minkyu
 * Date : 2019-10-31
 * Time : 16:56
 * Remark : Company Service
 */
@Service
public class CompanyService {
    private final ModelMapper modelMapper;
    private final CompanyRepository companyRepository;
    private final KeyGenerateService keyGenerateService;
    private final CompanyRepositoryCystom companyRepositoryCystom;

    @Autowired
    public CompanyService(CompanyRepository companyRepository,
                          KeyGenerateService keyGenerateService,
                          CompanyRepositoryCystom companyRepositoryCystom,
                         ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        this.companyRepositoryCystom = companyRepositoryCystom;
        this.companyRepository = companyRepository;
        this.keyGenerateService = keyGenerateService;
    }

    public Company save(Company company) {
        if ( company.getCsNumber() == null || company.getCsNumber().isEmpty()){
            Date now = new Date();
            SimpleDateFormat yyMM = new SimpleDateFormat("yyMM");
            String csNumber = keyGenerateService.keyGenerate("bs_company", yyMM.format(now), company.getModify_id());
            company.setCsNumber(csNumber);
        }
        return companyRepository.save(company);
    }

    public Optional<Company> findByCsNumber(String csNumber) {
        return companyRepository.findByCsNumber(csNumber);
    }


    public Page<CompanyListDto> findByCompanySearch(String csNumber, String csOperator, DivisionType csDivisionType, RegionalType csRegionalType, Pageable pageable) {
        return companyRepositoryCystom.findByCompanySearch(csNumber,csOperator,csDivisionType,csRegionalType,pageable);
    }

    public CompanyDto findById(Long id) {
        Optional<Company> optionalCompany = companyRepository.findById(id);
        if (optionalCompany.isPresent()) {
            return modelMapper.map(optionalCompany.get(), CompanyDto.class);
        } else {
            return null;
        }
    }

    public void delete(Company company) {
        companyRepository.delete(company);
    }
}
