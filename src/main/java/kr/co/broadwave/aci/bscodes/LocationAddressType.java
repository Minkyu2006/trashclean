package kr.co.broadwave.aci.bscodes;

/**
 * @author MinKyu
 * Date : 2019-09-06
 * Remark : 출동일지 구타입
 */
public enum LocationAddressType {
    S0001("S0001","강남구",LocationCityType.L01), //서울특별시
    S0002("S0002","강동구",LocationCityType.L01),
    S0003("S0003","강북구",LocationCityType.L01),
    S0004("S0004","강서구",LocationCityType.L01),
    S0005("S0005","관악구",LocationCityType.L01),
    S0006("S0006","광진구",LocationCityType.L01),
    S0007("S0007","구로구",LocationCityType.L01),
    S0008("S0008","금천구",LocationCityType.L01),
    S0009("S0009","노원구",LocationCityType.L01),
    S0010("S0010","도봉구",LocationCityType.L01),
    S0011("S0011","동대문구",LocationCityType.L01),
    S0012("S0012","동작구",LocationCityType.L01),
    S0013("S0013","마포구",LocationCityType.L01),
    S0014("S0014","서대문구",LocationCityType.L01),
    S0015("S0015","서초구",LocationCityType.L01),
    S0016("S0016","성동구",LocationCityType.L01),
    S0017("S0017","성북구",LocationCityType.L01),
    S0018("S0018","송파구",LocationCityType.L01),
    S0019("S0019","양천구",LocationCityType.L01),
    S0020("S0020","영등포구",LocationCityType.L01),
    S0021("S0021","용산구",LocationCityType.L01),
    S0022("S0022","은평구",LocationCityType.L01),
    S0023("S0023","종로구",LocationCityType.L01),
    S0024("S0024","중구",LocationCityType.L01),
    S0025("S0025","중랑구",LocationCityType.L01),

    I0001("I0001","강화군",LocationCityType.L02), //인천광역시
    I0002("I0002","계양구",LocationCityType.L02),
    I0003("I0003","남구",LocationCityType.L02),
    I0004("I0004","남동구",LocationCityType.L02),
    I0005("I0005","동구",LocationCityType.L02),
    I0006("I0006","부평구",LocationCityType.L02),
    I0007("I0007","서구",LocationCityType.L02),
    I0008("I0008","연수구",LocationCityType.L02),
    I0009("I0009","옹진군",LocationCityType.L02),
    I0010("I0010","중구",LocationCityType.L02),


    DG0001("DG0001","중구",LocationCityType.L03), //대구광역시
    DG0002("DG0002","동구",LocationCityType.L03),
    DG0003("DG0003","서구",LocationCityType.L03),
    DG0004("DG0004","남구",LocationCityType.L03),
    DG0005("DG0005","북구,",LocationCityType.L03),
    DG0006("DG0006","수성구",LocationCityType.L03),
    DG0007("DG0007","달서구",LocationCityType.L03),
    DG0008("DG0008","달성군",LocationCityType.L03),

    B0001("B0001","중구",LocationCityType.L04), //부산광역시
    B0002("B0002","서구",LocationCityType.L04),
    B0003("B0003","동구",LocationCityType.L04),
    B0004("B0004","남구",LocationCityType.L04),
    B0005("B0005","북구",LocationCityType.L04),
    B0006("B0006","영도구",LocationCityType.L04),
    B0007("B0007","부산진구",LocationCityType.L04),
    B0008("B0008","동래구",LocationCityType.L04),
    B0009("B0009","해운대구",LocationCityType.L04),
    B0010("B0010","사하구",LocationCityType.L04),
    B0011("B0011","금정구",LocationCityType.L04),
    B0012("B0012","강서구",LocationCityType.L04),
    B0013("B0013","연제구",LocationCityType.L04),
    B0014("B0014","수영구",LocationCityType.L04),
    B0015("B0015","사상구",LocationCityType.L04),
    B0016("B0016","기장군",LocationCityType.L04),

    DJ0001("DJ0001","동구",LocationCityType.L05), //대전광역시
    DJ0002("DJ0002","중구",LocationCityType.L05),
    DJ0003("DJ0003","서구",LocationCityType.L05),
    DJ0004("DJ0004","유성구",LocationCityType.L05),
    DJ0005("DJ0005","대덕구",LocationCityType.L05),

    US0001("US0001","중구",LocationCityType.L06), //울산광역시
    US0002("US0002","남구",LocationCityType.L06),
    US0003("US0003","동구",LocationCityType.L06),
    US0004("US0004","북구",LocationCityType.L06),
    US0005("US0005","울주군",LocationCityType.L06),

    GJ0001("GJ0001","동구",LocationCityType.L07), //광주광역시
    GJ0002("GJ0002","서구",LocationCityType.L07),
    GJ0003("GJ0003","남구",LocationCityType.L07),
    GJ0004("GJ0004","북구",LocationCityType.L07),
    GJ0005("GJ0005","광산구",LocationCityType.L07),

    G0001("G0001","가평군",LocationCityType.L08), //경기도
    G0002("G0002","고양시 덕양구",LocationCityType.L08),
    G0003("G0003","고양시 일산동구",LocationCityType.L08),
    G0004("G0004","고양시 일산서구",LocationCityType.L08),
    G0005("G0005","과천시",LocationCityType.L08),
    G0006("G0006","광명시",LocationCityType.L08),
    G0007("G0007","광주시",LocationCityType.L08),
    G0008("G0008","구리시",LocationCityType.L08),
    G0009("G0009","군포시",LocationCityType.L08),
    G0010("G0010","김포시",LocationCityType.L08),
    G0011("G0011","남양주시",LocationCityType.L08),
    G0012("G0012","동두천시",LocationCityType.L08),
    G0013("G0013","부천시 소사구",LocationCityType.L08),
    G0014("G0014","부천시 오정구",LocationCityType.L08),
    G0015("G0015","부천시 원미구",LocationCityType.L08),
    G0016("G0016","성남시 분당구",LocationCityType.L08),
    G0017("G0017","성남시 수정구,",LocationCityType.L08),
    G0018("G0018","성남시 중원구",LocationCityType.L08),
    G0019("G0019","수원시 권선구",LocationCityType.L08),
    G0020("G0020","수원시 영통구",LocationCityType.L08),
    G0021("G0021","수원시 장안구",LocationCityType.L08),
    G0022("G0022","수원시 팔달구",LocationCityType.L08),
    G0023("G0023","시흥시",LocationCityType.L08),
    G0024("G0024","안산시 단원구",LocationCityType.L08),
    G0025("G0025","안산시 상록구",LocationCityType.L08),
    G0026("G0026","안성시",LocationCityType.L08),
    G0027("G0027","안양시 동안구",LocationCityType.L08),
    G0028("G0028","안양시 만안구",LocationCityType.L08),
    G0029("G0029","양주시",LocationCityType.L08),
    G0030("G0030","양평군",LocationCityType.L08),
    G0031("G0031","여주시",LocationCityType.L08),
    G0032("G0032","연천군",LocationCityType.L08),
    G0033("G0033","오산시",LocationCityType.L08),
    G0034("G0034","용인시 기흥구",LocationCityType.L08),
    G0035("G0035","용인시 수지구",LocationCityType.L08),
    G0036("G0036","용인시 처인구",LocationCityType.L08),
    G0037("G0037","의왕시",LocationCityType.L08),
    G0038("G0038","의정부시",LocationCityType.L08),
    G0039("G0039","이천시",LocationCityType.L08),
    G0040("G0040","파주시",LocationCityType.L08),
    G0041("G0041","평택시",LocationCityType.L08),
    G0042("G0042","포천시",LocationCityType.L08),
    G0043("G0043","하남시",LocationCityType.L08),
    G0044("G0044","화성시",LocationCityType.L08),


    GD0001("GD0001","춘천시",LocationCityType.L09), //강원도
    GD0002("GD0002","원주시",LocationCityType.L09),
    GD0003("GD0003","강릉시",LocationCityType.L09),
    GD0004("GD0004","동해시",LocationCityType.L09),
    GD0005("GD0005","태백시",LocationCityType.L09),
    GD0006("GD0006","속초시",LocationCityType.L09),
    GD0007("GD0007","삼척시",LocationCityType.L09),
    GD0008("GD0008","홍천군",LocationCityType.L09),
    GD0009("GD0009","횡성군",LocationCityType.L09),
    GD0010("GD0010","영월군",LocationCityType.L09),
    GD0011("GD0011","평창군",LocationCityType.L09),
    GD0012("GD0012","정선군",LocationCityType.L09),
    GD0013("GD0013","철원군",LocationCityType.L09),
    GD0014("GD0014","화천군",LocationCityType.L09),
    GD0015("GD0015","양구군",LocationCityType.L09),
    GD0016("GD0016","인제군",LocationCityType.L09),
    GD0017("GD0017","고성군",LocationCityType.L09),
    GD0018("GD0018","양양군",LocationCityType.L09),

    CB0001("CB0001","청주시 상당구",LocationCityType.L10), //충청북도
    CB0002("CB0002","청주시 서원구",LocationCityType.L10),
    CB0003("CB0003","청주시 청원구",LocationCityType.L10),
    CB0004("CB0004","청주시 흥덕구",LocationCityType.L10),
    CB0005("CB0005","충주시",LocationCityType.L10),
    CB0006("CB0006","제천시",LocationCityType.L10),
    CB0007("CB0007","영동군",LocationCityType.L10),
    CB0008("CB0008","진천군",LocationCityType.L10),
    CB0009("CB0009","괴산군",LocationCityType.L10),
    CB0010("CB0010","음성군",LocationCityType.L10),
    CB0011("CB0011","단양군",LocationCityType.L10),
    CB0012("CB0012","증평군",LocationCityType.L10),
    CB00013("CB00013","보은군",LocationCityType.L10),
    CB00014("CB00014","옥천군" ,LocationCityType.L10),

    CN0001("CN0001","천안시 동남구",LocationCityType.L11), //충청남도
    CN0002("CN0002","천안시 서북구",LocationCityType.L11),
    CN0003("CN0003","보령시",LocationCityType.L11),
    CN0004("CN0004","아산시",LocationCityType.L11),
    CN0005("CN0005","서산시",LocationCityType.L11),
    CN0006("CN0006","논산시",LocationCityType.L11),
    CN0007("CN0007","계룡시",LocationCityType.L11),
    CN0008("CN0008","당진시",LocationCityType.L11),
    CN0009("CN0009","공주시",LocationCityType.L11),
    CN0010("CN0010","금산군",LocationCityType.L11),
    CN0011("CN0011","연기군",LocationCityType.L11),
    CN0012("CN0012","부여군",LocationCityType.L11),
    CN0013("CN0013","서천군",LocationCityType.L11),
    CN0014("CN0014","청양군",LocationCityType.L11),
    CN0015("CN0015","홍성군",LocationCityType.L11),
    CN0016("CN0016","예산군",LocationCityType.L11),
    CN0017("CN0017","태안군",LocationCityType.L11),

    JB0001("JB0001","전주시 덕진구",LocationCityType.L12), //전라북도
    JB0002("JB0002","군산시 완산구",LocationCityType.L12),
    JB0003("JB0003","익산시",LocationCityType.L12),
    JB0004("JB0004","정읍시",LocationCityType.L12),
    JB0005("JB0005","남원시",LocationCityType.L12),
    JB0006("JB0006","김제시",LocationCityType.L12),
    JB0007("JB0007","군산시",LocationCityType.L12),
    JB0008("JB0008","진안군",LocationCityType.L12),
    JB0009("JB0009","무주군",LocationCityType.L12),
    JB0010("JB0010","장수군",LocationCityType.L12),
    JB0011("JB0011","임실군",LocationCityType.L12),
    JB0012("JB0012","순창군",LocationCityType.L12),
    JB0013("JB0013","고창군",LocationCityType.L12),
    JB0014("JB0014","부안군",LocationCityType.L12),
    JB0015("JB0015","완주군",LocationCityType.L12),

    JN0001("JN0001","목포시",LocationCityType.L13), //전라남도
    JN0002("JN0002","여수시",LocationCityType.L13),
    JN0003("JN0003","순천시",LocationCityType.L13),
    JN0004("JN0004","나주시",LocationCityType.L13),
    JN0005("JN0005","광양시",LocationCityType.L13),
    JN0006("JN0006","담양군",LocationCityType.L13),
    JN0007("JN0007","곡성군",LocationCityType.L13),
    JN0008("JN0008","구례군",LocationCityType.L13),
    JN0009("JN0009","고흥군",LocationCityType.L13),
    JN0010("JN0010","보성군",LocationCityType.L13),
    JN0011("JN0011","화순군",LocationCityType.L13),
    JN0012("JN0012","장흥군",LocationCityType.L13),
    JN0013("JN0013","강진군",LocationCityType.L13),
    JN0014("JN0014","해남군",LocationCityType.L13),
    JN0015("JN0015","영암군",LocationCityType.L13),
    JN0016("JN0016","무안군",LocationCityType.L13),
    JN0017("JN0017","함평군",LocationCityType.L13),
    JN0018("JN0018","영광군",LocationCityType.L13),
    JN0019("JN0019","장성군",LocationCityType.L13),
    JN0020("JN0020","완도군",LocationCityType.L13),
    JN0021("JN0021","진도군",LocationCityType.L13),
    JN0022("JN0022","신안군",LocationCityType.L13),

    GB0001("GB0001","경산시",LocationCityType.L14), //경상북도
    GB0002("GB0002","경주시",LocationCityType.L14),
    GB0003("GB0003","고령군",LocationCityType.L14),
    GB0004("GB0004","구미시",LocationCityType.L14),
    GB0005("GB0005","군위군",LocationCityType.L14),
    GB0006("GB0006","김천시",LocationCityType.L14),
    GB0007("GB0007","문경시",LocationCityType.L14),
    GB0008("GB0008","봉화군",LocationCityType.L14),
    GB0009("GB0009","상주시",LocationCityType.L14),
    GB0010("GB0010","성주군",LocationCityType.L14),
    GB0011("GB0011","안동시",LocationCityType.L14),
    GB0012("GB0012","영덕군",LocationCityType.L14),
    GB0013("GB0013","영양군",LocationCityType.L14),
    GB0014("GB0014","영주시",LocationCityType.L14),
    GB0015("GB0015","영천시",LocationCityType.L14),
    GB0016("GB0016","예천군",LocationCityType.L14),
    GB0017("GB0017","울릉군",LocationCityType.L14),
    GB0018("GB0018","울진군",LocationCityType.L14),
    GB0019("GB0019","의성군",LocationCityType.L14),
    GB0020("GB0020","청도군",LocationCityType.L14),
    GB0021("GB0021","청송군",LocationCityType.L14),
    GB0022("GB0022","칠곡군",LocationCityType.L14),
    GB0023("GB0023","포항시 남구",LocationCityType.L14),
    GB0024("GB0024","포항시 북구",LocationCityType.L14),

    GN0001("GN0001","거제시",LocationCityType.L15), //경상남도
    GN0002("GN0002","거창군",LocationCityType.L15),
    GN0003("GN0003","고성군",LocationCityType.L15),
    GN0004("GN0004","김해시",LocationCityType.L15),
    GN0005("GN0005","남해군",LocationCityType.L15),
    GN0006("GN0006","밀양시",LocationCityType.L15),
    GN0007("GN0007","사천시",LocationCityType.L15),
    GN0008("GN0008","산청군",LocationCityType.L15),
    GN0009("GN0009","양산시",LocationCityType.L15),
    GN0010("GN0010","의령군",LocationCityType.L15),
    GN0011("GN0011","진주시",LocationCityType.L15),
    GN0012("GN0012","창녕군",LocationCityType.L15),
    GN0013("GN0013","창원시 마산합포구",LocationCityType.L15),
    GN0014("GN0014","창원시 마산회원구",LocationCityType.L15),
    GN0015("GN0015","창원시 성산구",LocationCityType.L15),
    GN0016("GN0016","창원시 의창구",LocationCityType.L15),
    GN0017("GN0017","창원시 진해구",LocationCityType.L15),
    GN0018("GN0018","통영시",LocationCityType.L15),
    GN0019("GN0019","하동군",LocationCityType.L15),
    GN0020("GN0020","함안군",LocationCityType.L15),
    GN0021("GN0021","함양군",LocationCityType.L15),
    GN0022("GN0022","합천군",LocationCityType.L15),

    JJ0001("JJ0001","제주시",LocationCityType.L16),
    JJ0002("JJ0002","서귀포시",LocationCityType.L16),

    SJ0001("SJ0001","",LocationCityType.L17);

    private String code;
    private String desc;
    private LocationCityType locationCityType;

    LocationAddressType(String code, String desc,LocationCityType locationCityType) {
        this.code = code;
        this.desc = desc;
        this.locationCityType = locationCityType;
    }
    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public String getLocationCityType(){
        return locationCityType.getCode();
    }
}
