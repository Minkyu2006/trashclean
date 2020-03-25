package kr.co.broadwave.aci.accounts;

import kr.co.broadwave.aci.bscodes.ApprovalType;
import kr.co.broadwave.aci.bscodes.CodeType;
import kr.co.broadwave.aci.common.AjaxResponse;
import kr.co.broadwave.aci.common.CommonUtils;
import kr.co.broadwave.aci.common.ResponseErrorCode;
import kr.co.broadwave.aci.company.Company;
import kr.co.broadwave.aci.company.CompanyService;
import kr.co.broadwave.aci.files.FileUpload;
import kr.co.broadwave.aci.files.FileUploadService;
import kr.co.broadwave.aci.mastercode.MasterCode;
import kr.co.broadwave.aci.mastercode.MasterCodeDto;
import kr.co.broadwave.aci.mastercode.MasterCodeService;
import kr.co.broadwave.aci.teams.Team;
import kr.co.broadwave.aci.teams.TeamDto;
import kr.co.broadwave.aci.teams.TeamService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;


/**
 * @author InSeok
 * Date : 2019-03-25
 * Time : 10:07
 * Remark : Account 용 Rest Controller
 */
@Slf4j
@RestController
@RequestMapping("/api/account")
public class AccountRestController {

    @Value("${aci.aws.s3.bucket.url}")
    private String AWSS3URL;

    private AjaxResponse res = new AjaxResponse();
    HashMap<String, Object> data = new HashMap<>();

    private final AccountService accountService;
    private final TeamService teamService;
    private final ModelMapper modelMapper;
    private final LoginlogService loginlogService;
    private final MasterCodeService masterCodeService;
    private final FileUploadService fileUploadService;
    private final PasswordEncoder passwordEncoder;
    private final CompanyService companyService;
    @Autowired
    public AccountRestController(AccountService accountService, ModelMapper modelMapper,
                                 FileUploadService fileUploadService, CompanyService companyService
            , TeamService teamService, LoginlogService loginlogService, MasterCodeService masterCodeService, PasswordEncoder passwordEncoder) {
        this.accountService = accountService;
        this.companyService = companyService;
        this.modelMapper = modelMapper;
        this.teamService = teamService;
        this.loginlogService = loginlogService;
        this.masterCodeService = masterCodeService;
        this.passwordEncoder = passwordEncoder;
        this.fileUploadService = fileUploadService;
    }

    @PostMapping("reg")
    public ResponseEntity<Map<String,Object>> accountSave(@ModelAttribute AccountMapperDto accountMapperDto, HttpServletRequest request){

        Account account = modelMapper.map(accountMapperDto, Account.class);

        Optional<Team> optionalTeam = teamService.findByTeamcode(accountMapperDto.getTeamcode());
        Optional<MasterCode> optionalPositionCode = masterCodeService.findById(accountMapperDto.getPositionid());


        //패스워드를 입력하세요.
        if (accountMapperDto.getPassword() == null || accountMapperDto.getPassword() ==""){
            log.info(ResponseErrorCode.E006.getDesc());
            return ResponseEntity.ok(res.fail(ResponseErrorCode.E006.getCode(), ResponseErrorCode.E006.getDesc()));
        }
        //아이디를 입력하세요.
        if (accountMapperDto.getUserid() == null || accountMapperDto.getUserid() ==""){
            log.info(ResponseErrorCode.E007.getDesc());
            return ResponseEntity.ok(res.fail(ResponseErrorCode.E007.getCode(), ResponseErrorCode.E007.getDesc()));
        }

        //부서코드가 존재하지않으면
        if (!optionalTeam.isPresent()) {
            log.info(" 선택한 부서 DB 존재 여부 체크.  부서코드: '" + accountMapperDto.getTeamcode() +"'");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.E005.getCode(), ResponseErrorCode.E005.getDesc()));
        }else{
            Team team = optionalTeam.get();
            account.setTeam(team);
        }
        //직급코드가 존재하지않으면
        if (!optionalPositionCode.isPresent()) {
            log.info(" 선택한 직급 DB 존재 여부 체크.  직급코드: '" + accountMapperDto.getPositionid() +"'");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.E016.getCode(), ResponseErrorCode.E016.getDesc()));
        }else{
            account.setPosition(optionalPositionCode.get());
        }


        Optional<Account> optionalAccount = accountService.findByUserid(account.getUserid());

        String currentuserid = CommonUtils.getCurrentuser(request);


        //신규일때
        if (accountMapperDto.getMode().equals("N")) {
            //userid 중복체크
            if (optionalAccount.isPresent()) {
                log.info("사용자저장실패(사용자아이디중복) 사용자아이디: '" + account.getUserid() + "'");
                return ResponseEntity.ok(res.fail(ResponseErrorCode.E001.getCode(), ResponseErrorCode.E001.getDesc()));
            }
            account.setInsert_id(currentuserid);
            account.setInsertDateTime(LocalDateTime.now());
        }else{
            //수정일때
            if(!optionalAccount.isPresent()){
                log.info("사용자정보수정실패 : 사용자아이디: '" + account.getUserid() + "'");
                return ResponseEntity.ok(res.fail(ResponseErrorCode.E004.getCode(), ResponseErrorCode.E004.getDesc()));
            }else{
                account.setId(optionalAccount.get().getId());
                account.setInsert_id(optionalAccount.get().getInsert_id());
                account.setInsertDateTime(optionalAccount.get().getInsertDateTime());
            }
            account.setModify_id(currentuserid);
            account.setModifyDateTime(LocalDateTime.now());

        }

        Account accountSave =  this.accountService.saveAccount(account);

        log.info("사용자 저장 성공 : '" + accountMapperDto.getUserid() +"'" );
        return ResponseEntity.ok(res.success());

    }

    @PostMapping("modifyReg")
    public ResponseEntity<Map<String,Object>> accountModifySave(@ModelAttribute AccountMapperDto accountMapperDto, HttpServletRequest request){

        Account account = modelMapper.map(accountMapperDto, Account.class);
        Optional<Team> optionalTeam = teamService.findByTeamcode(accountMapperDto.getTeamcode());
        Optional<MasterCode> optionalPositionCode = masterCodeService.findById(accountMapperDto.getPositionid());
        Optional<Company> optionalCompany = companyService.findByid(accountMapperDto.getCompanyid());

        //아이디를 입력하세요.
        if (accountMapperDto.getUserid() == null || accountMapperDto.getUserid() ==""){
            log.info(ResponseErrorCode.E007.getDesc());
            return ResponseEntity.ok(res.fail(ResponseErrorCode.E007.getCode(), ResponseErrorCode.E007.getDesc()));
        }

        //해당업체가 존재하지않으면
        if (!optionalCompany.isPresent()) {
            return ResponseEntity.ok(res.fail(ResponseErrorCode.E026.getCode(), ResponseErrorCode.E026.getDesc()));
        }else{
            Company company = optionalCompany.get();
            account.setCompany(company);
        }

        //부서코드가 존재하지않으면
        if (!optionalTeam.isPresent()) {
            log.info(" 선택한 부서 DB 존재 여부 체크.  부서코드: '" + accountMapperDto.getTeamcode() +"'");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.E005.getCode(), ResponseErrorCode.E005.getDesc()));
        }else{
            Team team = optionalTeam.get();
            account.setTeam(team);
        }

        //직급코드가 존재하지않으면
        if (!optionalPositionCode.isPresent()) {
            log.info(" 선택한 직급 DB 존재 여부 체크.  직급코드: '" + accountMapperDto.getPositionid() +"'");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.E016.getCode(), ResponseErrorCode.E016.getDesc()));
        }else{
            account.setPosition(optionalPositionCode.get());
        }

        Optional<Account> optionalAccount = accountService.findByUserid(account.getUserid());
        String currentuserid = CommonUtils.getCurrentuser(request);

        //수정일때
        if(!optionalAccount.isPresent()){
            log.info("사용자 일반 관리자(일반정보) : 사용자아이디: '" + account.getUserid() + "'");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.E004.getCode(), ResponseErrorCode.E004.getDesc()));
        }else{
            account.setId(optionalAccount.get().getId());
            account.setInsert_id(optionalAccount.get().getInsert_id());
            account.setInsertDateTime(optionalAccount.get().getInsertDateTime());
            account.setPassword(optionalAccount.get().getPassword());
            account.setUserRefleshCount(optionalAccount.get().getUserRefleshCount());
            account.setUserRefleshCheck(optionalAccount.get().getUserRefleshCheck());
            account.setUserLayoutNumber(optionalAccount.get().getUserLayoutNumber());
            account.setUserPhoto(optionalAccount.get().getUserPhoto());
        }
        account.setModify_id(currentuserid);
        account.setModifyDateTime(LocalDateTime.now());

        Account accountSave =  this.accountService.updateAccount(account);

        log.info("사용자 관리자(일반정보) 수정 성공 '" + accountMapperDto.getUserid() +"'" );
        return ResponseEntity.ok(res.success());

    }

    @PostMapping("modifyAdminPassword")
    public ResponseEntity<Map<String,Object>> accountmodifyAdminPassword(@ModelAttribute AccountMapperDto accountMapperDto, HttpServletRequest request){


        Account account = modelMapper.map(accountMapperDto, Account.class);
        Optional<Team> optionalTeam = teamService.findByTeamcode(accountMapperDto.getTeamcode());
        Optional<MasterCode> optionalPositionCode = masterCodeService.findById(accountMapperDto.getPositionid());

        //아이디를 입력하세요.
        if (accountMapperDto.getUserid() == null || accountMapperDto.getUserid() ==""){
            log.info(ResponseErrorCode.E007.getDesc());
            return ResponseEntity.ok(res.fail(ResponseErrorCode.E007.getCode(), ResponseErrorCode.E007.getDesc()));
        }

        Optional<Account> optionalAccount = accountService.findByUserid(account.getUserid());

        String currentuserid = CommonUtils.getCurrentuser(request);

        //수정일때
        if(!optionalAccount.isPresent()){
            log.info("사용자 관리자(패스워드) 실패 : 사용자아이디: '" + account.getUserid() + "'");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.E004.getCode(), ResponseErrorCode.E004.getDesc()));
        }else{
            account.setId(optionalAccount.get().getId());
            account.setInsert_id(optionalAccount.get().getInsert_id());
            account.setInsertDateTime(optionalAccount.get().getInsertDateTime());
            account.setUsername(optionalAccount.get().getUsername());
            account.setCellphone(optionalAccount.get().getCellphone());
            account.setEmail(optionalAccount.get().getEmail());
            account.setTeam(optionalAccount.get().getTeam());
            account.setApprovalType(optionalAccount.get().getApprovalType());
            account.setPosition(optionalAccount.get().getPosition());
            account.setCompany(optionalAccount.get().getCompany());
            account.setRole(optionalAccount.get().getRole());
            account.setUserRefleshCount(optionalAccount.get().getUserRefleshCount());
            account.setUserRefleshCheck(optionalAccount.get().getUserRefleshCheck());
            account.setUserLayoutNumber(optionalAccount.get().getUserLayoutNumber());
            account.setUserPhoto(optionalAccount.get().getUserPhoto());
        }
        account.setModify_id(currentuserid);
        account.setModifyDateTime(LocalDateTime.now());

        Account accountSave =  this.accountService.saveAccount(account);

        log.info("사용자 관리자(패스워드) 수정 성공 '" + accountMapperDto.getUserid() +"'" );
        return ResponseEntity.ok(res.success());

    }

    //회원가입처리
    @PostMapping("signup")
    public ResponseEntity<Map<String,Object>> signup(@ModelAttribute AccountMapperDto accountMapperDto, HttpServletRequest request){

        Account account = modelMapper.map(accountMapperDto, Account.class);
        Optional<Team> optionalTeam = teamService.findByTeamcode(accountMapperDto.getTeamcode());
        Optional<MasterCode> optionalPositionCode = masterCodeService.findById(accountMapperDto.getPositionid());
        Optional<Company> optionalCompany = companyService.findByid(accountMapperDto.getCompanyid());

        //패스워드를 입력하세요.
        if (accountMapperDto.getPassword() == null || accountMapperDto.getPassword().equals("")){
            log.info(ResponseErrorCode.E006.getDesc());
            return ResponseEntity.ok(res.fail(ResponseErrorCode.E006.getCode(), ResponseErrorCode.E006.getDesc()));
        }
        //아이디를 입력하세요.
        if (accountMapperDto.getUserid() == null || accountMapperDto.getUserid().equals("")){
            log.info(ResponseErrorCode.E007.getDesc());
            return ResponseEntity.ok(res.fail(ResponseErrorCode.E007.getCode(), ResponseErrorCode.E007.getDesc()));
        }



        Optional<Account> optionalAccount = accountService.findByUserid(account.getUserid());

        //userid 중복체크
        if (optionalAccount.isPresent()) {
            log.info("회원가입저장실패(아이디중복) 사용자아이디: '" + account.getUserid() + "'");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.E008.getCode(), ResponseErrorCode.E008.getDesc()));
        }
        account.setInsert_id("signUp");
        account.setInsertDateTime(LocalDateTime.now());
        //account.setApprovalType(ApprovalType.AT01); // 미승인상태로 회원가입
        account.setApprovalType(ApprovalType.AT02); // 등록시 승인처리 2019/12/16 - 김민규
        account.setUserRefleshCheck(1); // 등록시 새로고침체크 2020/01/02 - 김민규 (기본값 체크1)
        account.setUserRefleshCount(3); // 등록시 새로고침주기 2020/01/02 - 김민규 (기본값 2분)
        account.setUserLayoutNumber(1); // 등록시 레이아웃모드 상태번호 2020/02/25 - 김민규 (기본값 1)

        //해당업체가 존재하지않으면
        if (!optionalCompany.isPresent()) {
            return ResponseEntity.ok(res.fail(ResponseErrorCode.E026.getCode(), ResponseErrorCode.E026.getDesc()));
        }else{
            Company company = optionalCompany.get();
            account.setCompany(company);
        }
        //부서코드가 존재하지않으면
        if (!optionalTeam.isPresent()) {
            log.info(" 선택한 부서 DB 존재 여부 체크.  부서코드: '" + accountMapperDto.getTeamcode() +"'");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.E005.getCode(), ResponseErrorCode.E005.getDesc()));
        }else{
            Team team = optionalTeam.get();
            account.setTeam(team);
        }
        //직급코드가 존재하지않으면
        if (!optionalPositionCode.isPresent()) {
            log.info(" 선택한 직급 DB 존재 여부 체크.  직급코드: '" + accountMapperDto.getPositionid() +"'");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.E016.getCode(), ResponseErrorCode.E016.getDesc()));
        }else{
            account.setPosition(optionalPositionCode.get());
        }




        Account accountSave =  this.accountService.saveAccount(account);

        log.info("회원가입 저장 성공 : '" + accountMapperDto.getUserid() +"'" );
        return ResponseEntity.ok(res.success());

    }

//    @PostMapping("modifyemail")
//    public ResponseEntity accountSaveEmail(@ModelAttribute AccountMapperDtoModify accountMapperDto, HttpServletRequest request){
//
//
//        Account account = modelMapper.map(accountMapperDto, Account.class);
//
//
//        //아이디를 입력하세요.
//        if (accountMapperDto.getUserid() == null || accountMapperDto.getUserid().equals("")){
//            log.info(ResponseErrorCode.E009.getDesc());
//            return ResponseEntity.ok(res.fail(ResponseErrorCode.E009.getCode(), ResponseErrorCode.E009.getDesc()));
//        }
//
//
//        Optional<Account> optionalAccount = accountService.findByUserid(account.getUserid());
//
//        String currentuserid = CommonUtils.getCurrentuser(request);
//
//
//        //수정일때
//        if(!optionalAccount.isPresent()){
//            log.info("사용자정보 수정실패 : 사용자아이디: '" + account.getUserid() + "'");
//            return ResponseEntity.ok(res.fail(ResponseErrorCode.E004.getCode(), ResponseErrorCode.E004.getDesc()));
//        }else{
//            account.setId(optionalAccount.get().getId());
//            account.setInsert_id(optionalAccount.get().getInsert_id());
//            account.setInsertDateTime(optionalAccount.get().getInsertDateTime());
//            account.setPassword(optionalAccount.get().getPassword());
//            account.setTeam(optionalAccount.get().getTeam());
//            account.setRole(optionalAccount.get().getRole());
//            account.setUsername(optionalAccount.get().getUsername());
//            account.setApprovalType(optionalAccount.get().getApprovalType());
//            account.setPosition(optionalAccount.get().getPosition());
//
//        }
//        account.setModify_id(currentuserid);
//        account.setModifyDateTime(LocalDateTime.now());
//
//
//
//
//        Account accountSave =  this.accountService.modifyAccount(account);
//
//        log.info("사용자정보 수정 성공 : + " + accountMapperDto.getUserid() +"'" );
//        return ResponseEntity.ok(res.success());
//
//    }

    @PostMapping("list")
    public ResponseEntity<Map<String,Object>> accountList(@RequestParam(value="userid", defaultValue="") String userid,
                                      @RequestParam(value="username", defaultValue="") String username,
                                      @RequestParam(value="email", defaultValue="") String email,
                                      Pageable pageable){

        log.info("사용자 리스트 조회 / 조회조건 : userid / '" + userid + "' username / '" + username + "', email / '" + email + "'");


        Page<AccountDtoWithTeam> accounts = this.accountService.findAllBySearchStrings(userid, username, email, pageable);
        return CommonUtils.ResponseEntityPage(accounts);

    }

    @PostMapping("approvallist")
    public ResponseEntity<Map<String,Object>> accountApprovalList(
                                      @RequestParam(value="username", defaultValue="") String username,
                                      @RequestParam(value="startdate", defaultValue="") String startdate,
                                      @RequestParam(value="enddate", defaultValue="") String enddate,
                                      Pageable pageable){

        log.info("회원가입 승인조회 / 조회조건 : username / '" + username + "', startdate / '" + startdate + "', enddate / '" + enddate + "'");


        Page<AccountDto> accounts = this.accountService.findAllByApproval(username,startdate,enddate, pageable);
        return CommonUtils.ResponseEntityPage(accounts);

    }

    //회원가입 승인처리
    @PostMapping("approval")
    public ResponseEntity<Map<String,Object>> saveApproval(
                                    @RequestParam(value="userid", defaultValue="") String userid,
                                    @RequestParam(value="approvaltype", defaultValue="") String approvaltype,
                                    HttpServletRequest request
                                    ){
        Optional<Account> optionalAccount = accountService.findByUserid(userid);


        if(!optionalAccount.isPresent()){
            log.info("회원가입 승인처리실패 : 사용자아이디: '" + userid + "'");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.E012.getCode(), ResponseErrorCode.E012.getDesc()));
        }


        String currentuserid = CommonUtils.getCurrentuser(request);

        Long aLong = accountService.saveApproval(optionalAccount.get(), ApprovalType.valueOf(approvaltype), currentuserid);
        if (!aLong.equals(1L)){
            return ResponseEntity.ok(res.fail(ResponseErrorCode.E013.getCode(), ResponseErrorCode.E013.getDesc()));
        }

        return ResponseEntity.ok(res.success());
    }

    //사용자삭제
    @PostMapping("del")
    public ResponseEntity<Map<String,Object>> accountdel(@RequestParam (value="userid", defaultValue="") String userid
                                     ){
        log.info("사용자 삭제 / userid: " + userid );
        Optional<Account> optionalAccount = accountService.findByUserid(userid);
        //정보가있는지 체크
        if (!optionalAccount.isPresent()){
            log.info("사용자삭제실패 : 삭제할 데이터가 존재하지않음 , 삭제대상 userid : '" + userid + "'");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.E003.getCode(),ResponseErrorCode.E003.getDesc()));
        }
        Account account = optionalAccount.get();
        //사용중인지체크
        if( loginlogService.countByLoginAccount(account) > 0){
            log.info("사용자삭제실패 : LoginLog에서 사용중인데이터 , 삭제대상 userid : '" + userid + "'");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.E002.getCode(),ResponseErrorCode.E002.getDesc()));
        }

        accountService.delete(account);
        return ResponseEntity.ok(res.success());
    }

    @PostMapping("account")
    public ResponseEntity<Map<String,Object>> account(@RequestParam (value="userid", defaultValue="") String userid){

        //log.info("단일사용자조회  / userid: '" + userid + "'");
        Optional<Account> optionalAccount = accountService.findByUserid(userid);

        if(!optionalAccount.isPresent()){
            //log.info("단일사용자조회실패 : 조회할 데이터가 존재하지않음 , 조회대상 userid : '" + userid +"'");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.E004.getCode(),ResponseErrorCode.E004.getDesc()));
        }
        Account account = optionalAccount.get();
        //log.info("account : "+account);

        data.clear();
        data.put("datarow",account);
        res.addResponse("data",data);
        //log.info("단일사용자 조회 성공 : " + account.toString() );

        return ResponseEntity.ok(res.success());
    }

    // 프로필변경
    @PostMapping("profilereg")
    public ResponseEntity<Map<String,Object>> profilereg(@ModelAttribute AccountMapperDtoProfile accountMapperDtoProfile,
                                                    MultipartHttpServletRequest multi,
                                                    HttpServletRequest request) throws Exception{

        Account account = modelMapper.map(accountMapperDtoProfile, Account.class);
        Optional<Team> optionalTeam = teamService.findByTeamcode(accountMapperDtoProfile.getTeam());
        Optional<MasterCode> optionalPositionCode = masterCodeService.findById(accountMapperDtoProfile.getPosition());

        //부서코드가 존재하지않으면
        if (!optionalTeam.isPresent()) {
            log.info(" 선택한 부서 DB 존재 여부 체크.  부서코드: '" + accountMapperDtoProfile.getTeam() +"'");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.E005.getCode(), ResponseErrorCode.E005.getDesc()));
        }else{
            Team team = optionalTeam.get();
            account.setTeam(team);
        }

        //직급코드가 존재하지않으면
        if (!optionalPositionCode.isPresent()) {
            log.info(" 선택한 직급 DB 존재 여부 체크.  직급코드: '" + accountMapperDtoProfile.getPosition() +"'");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.E016.getCode(), ResponseErrorCode.E016.getDesc()));
        }else{
            account.setPosition(optionalPositionCode.get());
        }

        String currentuserid = CommonUtils.getCurrentuser(request);
        Optional<Account> optionalAccount = accountService.findByUserid(currentuserid);

        //수정시작
        if(optionalAccount.isPresent()){
            account.setId(optionalAccount.get().getId());
            account.setUserid(currentuserid);
            account.setPassword(optionalAccount.get().getPassword());
            account.setRole(optionalAccount.get().getRole());
            account.setApprovalType(optionalAccount.get().getApprovalType());
            account.setUserRefleshCheck(optionalAccount.get().getUserRefleshCheck());
            account.setUserRefleshCount(optionalAccount.get().getUserRefleshCount());
            account.setUserLayoutNumber(optionalAccount.get().getUserLayoutNumber());
            account.setCompany(optionalAccount.get().getCompany());
            account.setInsert_id(optionalAccount.get().getInsert_id());
            account.setInsertDateTime(optionalAccount.get().getInsertDateTime());
            account.setApprovalDateTime(optionalAccount.get().getApprovalDateTime());
            account.setApproval_id(optionalAccount.get().getApproval_id());
        }else{
            //log.info("사용자정보 수정실패 : 사용자아이디: '" + account.getUserid() + "'");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.E004.getCode(), ResponseErrorCode.E004.getDesc()));
        }
        account.setModify_id(currentuserid);
        account.setModifyDateTime(LocalDateTime.now());

        //프로필사진 저장
        Iterator<String> files = multi.getFileNames();
        String uploadFile = files.next();
        MultipartFile mFile = multi.getFile(uploadFile);

        // 저장할 파일이 존재할때만 실행
        assert mFile != null;
        if(!mFile.isEmpty()) {
            FileUpload fileUpload = fileUploadService.save(mFile);
            account.setUserPhoto(fileUpload);
        }else{
            //파일은 존재하지않으나, 기존파일이 존재할경우
            account.setUserPhoto(optionalAccount.get().getUserPhoto());
        }

        accountService.updateAccount(account);

        //파일수정일때 실행
        if(!mFile.isEmpty()) {
            if (optionalAccount.get().getUserPhoto() != null) {
                fileUploadService.del(optionalAccount.get().getUserPhoto().getId());
            }
        }

        return ResponseEntity.ok(res.success());

    }

    // 프로필 정보보기
    @PostMapping ("profileinfo")
    public ResponseEntity<Map<String,Object>> profileinfo(HttpServletRequest request){

        String currentuserid = CommonUtils.getCurrentuser(request);

        Optional<Account> accountProfile = accountService.findByUserid(currentuserid);
//        log.info("유저ID : "+currentuserid);
//        log.info("유저정보 : "+accountProfile);

        data.clear();
        if(accountProfile.isPresent()) {
            if (accountProfile.get().getUserPhoto() == null) {
                data.put("profilefilepath", "/defaultimage");
                data.put("profilefilename", "/profile.png");
            } else {
                data.put("profilefilepath", accountProfile.get().getUserPhoto().getFilePath());
                data.put("profilefilename", "/s_" + accountProfile.get().getUserPhoto().getSaveFileName());
            }
            data.put("accountProfile", accountProfile);
            data.put("accountteamcode", accountProfile.get().getTeam().getTeamcode());
            data.put("accountposition", accountProfile.get().getPosition().getId());

            data.put("AWSS3URL",AWSS3URL);
            res.addResponse("data",data);
        }

        return ResponseEntity.ok(res.success());
    }

    // 프로필 부서,직급 select호출
    @PostMapping("teamAndposition")
    public ResponseEntity<Map<String,Object>> teamAndposition(){
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        List<MasterCodeDto> profilepositions = masterCodeService.findCodeList(CodeType.C0001); // 직급코드가져오기
        List<TeamDto> profileteams = teamService.findTeamList();

        data.put("profilepositions",profilepositions);
        data.put("profileteams",profileteams);

        res.addResponse("data",data);

        return ResponseEntity.ok(res.success());
    }

    // 프로필 비밀번호변경
    @Transactional
    @PostMapping("modifypassword")
    public ResponseEntity<Map<String,Object>> modifypassword(@ModelAttribute AccountMapperDtoModify accountMapperDtoModify,
                                              HttpServletRequest request){

        Account account = modelMapper.map(accountMapperDtoModify, Account.class);

        String currentuserid = CommonUtils.getCurrentuser(request);
        Optional<Account> optionalAccount = accountService.findByUserid(currentuserid);

        //수정일때
        if(!optionalAccount.isPresent()){
            log.info("사용자정보(패스워드)수정 실패 : 사용자아이디: '" + currentuserid + "'");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.E004.getCode(), ResponseErrorCode.E004.getDesc()));
        }else{
            //현재암호비교
            if (!passwordEncoder.matches(accountMapperDtoModify.getOldpassword(),optionalAccount.get().getPassword())){
                return ResponseEntity.ok(res.fail(ResponseErrorCode.E010.getCode(), ResponseErrorCode.E010.getDesc()));
            }
            if( !accountMapperDtoModify.getNewpassword().equals(accountMapperDtoModify.getPasswordconfirm()) ){
                return ResponseEntity.ok(res.fail(ResponseErrorCode.E011.getCode(), ResponseErrorCode.E011.getDesc()));
            }

            account.setId(optionalAccount.get().getId());
            account.setUserid(currentuserid);
            account.setUserPhoto(optionalAccount.get().getUserPhoto());
            account.setUsername(optionalAccount.get().getUsername());
            account.setCellphone(optionalAccount.get().getCellphone());
            account.setEmail(optionalAccount.get().getEmail());
            account.setRole(optionalAccount.get().getRole());
            account.setCompany(optionalAccount.get().getCompany());
            account.setTeam(optionalAccount.get().getTeam());
            account.setPosition(optionalAccount.get().getPosition());
            account.setApprovalType(optionalAccount.get().getApprovalType());
            account.setUserRefleshCheck(optionalAccount.get().getUserRefleshCheck());
            account.setUserRefleshCount(optionalAccount.get().getUserRefleshCount());
            account.setUserLayoutNumber(optionalAccount.get().getUserLayoutNumber());
            account.setInsert_id(optionalAccount.get().getInsert_id());
            account.setInsertDateTime(optionalAccount.get().getInsertDateTime());
            account.setApprovalDateTime(optionalAccount.get().getApprovalDateTime());
            account.setApproval_id(optionalAccount.get().getApproval_id());
            account.setPassword(accountMapperDtoModify.getPasswordconfirm());
        }
        account.setModify_id(currentuserid);
        account.setModifyDateTime(LocalDateTime.now());

        Account accountSave =  this.accountService.saveAccount(account);

        log.info("사용자정보(패스워드)수정 성공 :  " + accountSave.toString() +"'" );
        return ResponseEntity.ok(res.success());

    }

    @RequestMapping("/headerMode")
    public ResponseEntity<Map<String,Object>> headerMode(HttpServletRequest request,
                                                         @RequestParam(value="headerMode", defaultValue="") Integer headerMode){
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        HttpSession session = request.getSession();
        if(headerMode==1){
//            log.info("headerMode : "+2);
            session.setAttribute("headerMode", 2);
            data.put("headerMode",2);
            res.addResponse("data",data);
        }else{
//            log.info("headerMode : "+1);
            session.setAttribute("headerMode", 1);
            data.put("headerMode",1);
            res.addResponse("data",data);
        }
        return ResponseEntity.ok(res.success());
    }

}
