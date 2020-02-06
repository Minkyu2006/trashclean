package kr.co.broadwave.aci.accounts;

/**
 * @author InSeok
 * Date : 2019-03-25
 * Time : 09:33
 * Remark : 사용자 권한 구분
 */
public enum AccountRole {
    ROLE_USER("ROLE_USER", "운영사"),
    ROLE_ADMIN("ROLE_ADMIN", "관리자"),
    ROLE_SUBADMIN("ROLE_SUBADMIN", "서브관리자"),
    ROLE_COLLECTOR("ROLE_COLLECTOR", "수거원")
    ;

    private String code;
    private String desc;


    AccountRole(String code,String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }}


