package vnpt.project.Caller_management.Services;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import org.springframework.stereotype.Service;
import vnpt.project.Caller_management.model.Cdr;
import vnpt.project.Caller_management.repository.CdrRepository;

import java.util.*;
@Service
public class ExcelServices {
    @Autowired
    CdrRepository cdrRepository;
    public static final int COLUMN_INDEX_ID = 0;
    public static final int COLUMN_INDEX_uuid = 1;
    public static final int COLUMN_INDEX_siteId = 2;
    public static final int COLUMN_INDEX_dial_number = 3;
    public static final int COLUMN_INDEX_Extension = 4;
    public static final int COLUMN_INDEX_msisdn = 5;
    public static final int COLUMN_INDEX_answerState = 6;
    public static final int COLUMN_INDEX_CustomerState = 7;
    public static final int COLUMN_INDEX_CallDirection = 8;
    public static final int COLUMN_INDEX_uuidMap = 9;
    public static final int COLUMN_INDEX_transferred = 10;
    public static final int COLUMN_INDEX_msisdn_digits = 11;
    public static final int COLUMN_INDEX_agent_digits = 12;
    public static final int COLUMN_INDEX_xml_path = 13;
    public static final int COLUMN_INDEX_recording_path = 14;
    public static final int COLUMN_INDEX_client_id = 15;
    public static final int COLUMN_INDEX_lat = 16;
    public static final int COLUMN_INDEX_lng = 17;
    public static final int COLUMN_INDEX_location = 18;
    public static final int COLUMN_INDEX_location_tolerance = 19;
    public static final int COLUMN_INDEX_autocall_campaign_id = 20;
    public static final int COLUMN_INDEX_address = 21;
    public static final int COLUMN_INDEX_tapping_id = 22;
    public static final int COLUMN_INDEX_status = 23;
    public static final int COLUMN_INDEX_call_length = 24;
    public static final int COLUMN_INDEX_start_timestamp = 25;
    public static final int COLUMN_INDEX_ring_timestamp = 26;
    public static final int COLUMN_INDEX_answer_timestamp = 27;
    public static final int COLUMN_INDEX_end_timestamp = 28;
    public static final int COLUMN_INDEX_created_at = 29;
    public static final int COLUMN_INDEX_created_by = 30;
    public static final int COLUMN_INDEX_updated_at = 31;
    public static final int COLUMN_INDEX_updated_by = 32;
    public static final int COLUMN_INDEX_is_spam = 33;
    public static final int COLUMN_INDEX_disposition = 34;
    public static final int COLUMN_INDEX_early_to_time = 35;
    public static final int COLUMN_INDEX_talk_time = 36;
    public static final int COLUMN_INDEX_call_wait = 37;
    public static final int COLUMN_INDEX_source = 38;
    public static final int COLUMN_INDEX_source_app = 39;
    private static final int COLUMN_INDEX_TOTAL = 4;

    private static CellStyle cellStyleFormatNumber = null;
    public static void writeHeader(Sheet sheet, int rowIndex) {
        // Tạo một đối tượng CellStyle với định dạng tiêu đề, giúp định dạng các ô tiêu đề (ví dụ: in đậm, căn giữa)
       CellStyle cellStyle = createStyleForHeader(sheet);

        // Tạo một dòng mới trong trang tính Excel tại vị trí rowIndex
        Row row = sheet.createRow(rowIndex);
        Cell cell = null;
        // Tạo một ô mới tại cột có chỉ số COLUMN_INDEX_ID trong dòng hiện tại (chứa tiêu đề "Id")
        cell = row.createCell(COLUMN_INDEX_ID);
        // Áp dụng định dạng cellStyle cho ô "Id"
        cell.setCellStyle(cellStyle);
        // Đặt giá trị chuỗi "Id" làm nội dung cho ô này (đây là tiêu đề của cột "Id")
        cell.setCellValue("Id");

        // Tạo một ô mới tại cột có chỉ số COLUMN_INDEX_uuid trong dòng hiện tại (chứa tiêu đề "uuid")
        cell = row.createCell(COLUMN_INDEX_uuid);
        // Áp dụng định dạng cellStyle cho ô "uuid"
        cell.setCellStyle(cellStyle);
        // Đặt giá trị chuỗi "uuid" làm nội dung cho ô này (đây là tiêu đề của cột "uuid")
        cell.setCellValue("uuid");

        // Tạo một ô mới tại cột có chỉ số COLUMN_INDEX_siteId trong dòng hiện tại (chứa tiêu đề "siteID")
        cell = row.createCell(COLUMN_INDEX_siteId);
        // Áp dụng định dạng cellStyle cho ô "siteID"
        cell.setCellStyle(cellStyle);
        // Đặt giá trị chuỗi "siteID" làm nội dung cho ô này (đây là tiêu đề của cột "siteID")
        cell.setCellValue("siteID");

        // Tạo một ô mới tại cột có chỉ số COLUMN_INDEX_dial_number trong dòng hiện tại (chứa tiêu đề "dial_number")
        cell = row.createCell(COLUMN_INDEX_dial_number);
        // Áp dụng định dạng cellStyle cho ô "dial_number"
        cell.setCellStyle(cellStyle);
        // Đặt giá trị chuỗi "dial_number" làm nội dung cho ô này (đây là tiêu đề của cột "dial_number")
        cell.setCellValue("dial_number");
        //----
        cell = row.createCell(COLUMN_INDEX_Extension);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Extension");
        //---
        cell = row.createCell(COLUMN_INDEX_msisdn);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("msisdn");
        //-----
        cell = row.createCell(COLUMN_INDEX_answerState);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("answerState");
        //-----
        cell = row.createCell(COLUMN_INDEX_CustomerState);
cell.setCellStyle(cellStyle);
cell.setCellValue("CustomerState");
//---
        cell =row.createCell(COLUMN_INDEX_CallDirection);
cell.setCellStyle(cellStyle);
cell.setCellValue("CallDirection");
        // Tạo một ô mới tại cột có chỉ số COLUMN_INDEX_uuidMap trong dòng hiện tại (chứa tiêu đề "uuidMap")
        cell = row.createCell(COLUMN_INDEX_uuidMap);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("uuidMap");

        // Tạo một ô mới tại cột có chỉ số COLUMN_INDEX_transferred trong dòng hiện tại (chứa tiêu đề "transferred")
        cell = row.createCell(COLUMN_INDEX_transferred);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("transferred");

        cell = row.createCell(COLUMN_INDEX_msisdn_digits);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("msisdn_digits");

        cell = row.createCell(COLUMN_INDEX_agent_digits);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("agent_digits");

        cell = row.createCell(COLUMN_INDEX_xml_path);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("xml_path");

        cell = row.createCell(COLUMN_INDEX_recording_path);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("recording_path");

        cell = row.createCell(COLUMN_INDEX_client_id);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("client_id");

        cell = row.createCell(COLUMN_INDEX_lat);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("lat");

        cell = row.createCell(COLUMN_INDEX_lng);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("lng");

        cell = row.createCell(COLUMN_INDEX_location);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("location");

        cell = row.createCell(COLUMN_INDEX_location_tolerance);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("location_tolerance");

        cell = row.createCell(COLUMN_INDEX_autocall_campaign_id);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("autocall_campaign_id");

        cell = row.createCell(COLUMN_INDEX_address);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("address");

        cell = row.createCell(COLUMN_INDEX_tapping_id);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("tapping_id");

        cell = row.createCell(COLUMN_INDEX_status);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("status");

        cell = row.createCell(COLUMN_INDEX_call_length);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("call_length");

        cell = row.createCell(COLUMN_INDEX_start_timestamp);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("start_timestamp");

        cell = row.createCell(COLUMN_INDEX_ring_timestamp);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("ring_timestamp");

        cell = row.createCell(COLUMN_INDEX_answer_timestamp);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("answer_timestamp");

        cell = row.createCell(COLUMN_INDEX_end_timestamp);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("end_timestamp");

        cell = row.createCell(COLUMN_INDEX_created_at);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("created_at");

        cell = row.createCell(COLUMN_INDEX_created_by);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("created_by");

        cell = row.createCell(COLUMN_INDEX_updated_at);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("updated_at");

        cell = row.createCell(COLUMN_INDEX_updated_by);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("updated_by");

        cell = row.createCell(COLUMN_INDEX_is_spam);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("is_spam");

        cell = row.createCell(COLUMN_INDEX_disposition);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("disposition");

        cell = row.createCell(COLUMN_INDEX_early_to_time);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("early_to_time");

        cell = row.createCell(COLUMN_INDEX_talk_time);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("talk_time");

        cell = row.createCell(COLUMN_INDEX_call_wait);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("call_wait");

        cell = row.createCell(COLUMN_INDEX_source);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("source");

        cell = row.createCell(COLUMN_INDEX_source_app);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("source_app");
    }

    private static CellStyle createStyleForHeader(Sheet sheet) {
        // Create font
        Font font = sheet.getWorkbook().createFont();
        font.setFontName("Times New Roman");
        font.setBold(true);
        font.setFontHeightInPoints((short) 14); // font size
        font.setColor(IndexedColors.WHITE.getIndex()); // text color

        // Create CellStyle
        CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        cellStyle.setFont(font);
        cellStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        return cellStyle;
    }
    protected List<Cdr> fetchDataBatch(int offset, int batchSize) {
        PageRequest pageRequest = PageRequest.of(offset / batchSize, batchSize);
        // Sử dụng getContent() để lấy List<Cdr> từ Page<Cdr>
        return cdrRepository.findAll(pageRequest).getContent();
    }
    public static void autosizeColumn(Sheet sheet, int lastColumn) {
        for (int columnIndex = 0; columnIndex < lastColumn; columnIndex++) {
            sheet.autoSizeColumn(columnIndex);
        }
    }
    // Write data
    public static void WriteCdr(Cdr cdr, Row row) {

        Cell cell = row.createCell(COLUMN_INDEX_ID);
        cell.setCellValue(cdr.getId());

        cell = row.createCell(COLUMN_INDEX_uuid);
        cell.setCellValue(cdr.getUuid());
        cell = row.createCell(COLUMN_INDEX_siteId);
        cell.setCellValue(cdr.getSiteId());

        cell = row.createCell(COLUMN_INDEX_dial_number);
        cell.setCellValue(cdr.getDialNumber());

        cell = row.createCell(COLUMN_INDEX_Extension);
        cell.setCellValue(cdr.getExtension());

        cell = row.createCell(COLUMN_INDEX_msisdn);
        cell.setCellValue(cdr.getMsisdn());

        cell = row.createCell(COLUMN_INDEX_answerState);
        cell.setCellValue(cdr.getAnswerState());


    }
//    public static void writeFooter(Sheet sheet, int rowIndex) {
//        // Tạo một dòng mới tại vị trí rowIndex
//        Row row = sheet.createRow(rowIndex);
//
//        // Tạo một ô với kiểu công thức tại vị trí COLUMN_INDEX_TOTAL
//        Cell cell = row.createCell(COLUMN_INDEX_TOTAL, CellType.FORMULA);
//
//        // Thiết lập công thức tính tổng cho cột COLUMN_INDEX_dial_number từ hàng 2 đến hàng rowIndex
//        cell.setCellFormula("SUM(" + getColumnLetter(COLUMN_INDEX_dial_number) + "2:" + getColumnLetter(COLUMN_INDEX_dial_number) + rowIndex + ")");
//    }

    // Phương thức để chuyển chỉ số cột thành chữ cái (A, B, C, ...)
    private static String getColumnLetter(int columnIndex) {
        return CellReference.convertNumToColString(columnIndex);
    }
}
