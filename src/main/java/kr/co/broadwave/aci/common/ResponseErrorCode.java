package kr.co.broadwave.aci.common;

/**
 * @author InSeok
 * Date : 2019-04-04
 * Time : 09:39
 * Remark : Rest controller 응답코드
 */
public enum ResponseErrorCode {
    E001("E001", "이미 존재하는 데이터입니다. 입력된데이터의 코드(아이디)를 확인하시기바랍니다."),
    E002("E002", "사용중인 데이터는 삭제할수 없습니다."),
    E003("E003", "삭제할 데이터가 데이터베이스에 존재하지않습니다."),
    E004("E004", "데이터가 존재하지 않습니다."),
    E005("E005", "운영사가 존재하지않습니다."),
    E006("E006", "패스워드를 입력하세요."),
    E007("E007", "아이디를 입력하세요."),
    E008("E008", "이미 존재하는 아이디입니다. 다른 아이디로 바꾸어주세요"),
    E009("E009", "변경하고자하는 회원정보가 존재하지않습니다. 로그인 후 다시 시도하세요"),
    E010("E010", "현재 비밀번호가 일치하지 않습니다."),
    E011("E011", "신규 입력한 비밀번호가 일치하지 않습니다."),
    E012("E012", "아이디가 존재하지 않습니다."),
    E013("E013", "회원가입 승인(거절)처리 중 실패하였습니다."),
    E014("E014", "사용자(아이디) 정보가 부정확합니다. 재 로그인후 시도해주세요."),
    E015("E015", "첨부파일삭제중 에러가 발생하였습니다. 새로고침하여 다시 시도해주세요"),
    E016("E016", "장비타입or국가or지역코드가 존재하지않습니다."),
    E017("E017", "조사담당자삭제중 에러가 발생하였습니다. 새로고침하여 다시 시도해주세요"),
    E018("E018", "업체구분이 존재하지않습니다."),
    E019("E019", "운영권역이 존재하지않습니다."),
    E020("E020", "조회중 에러가 발생하였습니다."),
    ;

    private String code;
    private String desc;

    ResponseErrorCode(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
