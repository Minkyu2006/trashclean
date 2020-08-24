package kr.co.broadwave.aci.excel;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractXlsView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
/**
 * @author InSeok
 * Date : 2019-04-26
 * Time : 09:31
 * Remark : 엑셀 전황용 View
 */
@Component("excelDownXls")
public class ExcelDownXlsView extends AbstractXlsView{
    @Override
    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
        new ExcelWriter(workbook, model, response).create();
    }
}