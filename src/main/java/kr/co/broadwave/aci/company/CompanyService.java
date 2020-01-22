package kr.co.broadwave.aci.company;

import kr.co.broadwave.aci.keygenerate.KeyGenerateService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
    private final CompanyRepositoryCustom companyRepositoryCustom;

    @Autowired
    public CompanyService(CompanyRepository companyRepository,
                          KeyGenerateService keyGenerateService,
                          CompanyRepositoryCustom companyRepositoryCustom,
                         ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        this.companyRepositoryCustom = companyRepositoryCustom;
        this.companyRepository = companyRepository;
        this.keyGenerateService = keyGenerateService;
    }

    public Company save(Company company) {
        if ( company.getCsNumber() == null || company.getCsNumber().isEmpty()){
            String csDivision = company.getCsDivision().getCode();
            String csNumber = keyGenerateService.keyGenerate("bs_company", csDivision, company.getModify_id());
            company.setCsNumber(csNumber);
        }
        return companyRepository.save(company);
    }

    public Optional<Company> findByCsNumber(String csNumber) {
        return companyRepository.findByCsNumber(csNumber);
    }


    public Page<CompanyListDto> findByCompanySearch(String csNumber, String csOperator,  Long csDivisionType, Long csRegionalType, Pageable pageable) {
        return companyRepositoryCustom.findByCompanySearch(csNumber,csOperator,csDivisionType,csRegionalType,pageable);
    }

    public CompanyDto findById(Long id) {
        Optional<Company> optionalCompany = companyRepository.findById(id);
        return optionalCompany.map(company -> modelMapper.map(company, CompanyDto.class)).orElse(null);
//        if (optionalCompany.isPresent()) {
//            return modelMapper.map(optionalCompany.get(), CompanyDto.class);
//        } else {
//            return null;
//        }
    }

    public void delete(Company company) {
        companyRepository.delete(company);
    }

    //운영사명으로 장비등록 아이디저장하기
    public Optional<Company> findByCsOperator(String company) {
        return companyRepository.findByCsOperator(company);
    }
}
