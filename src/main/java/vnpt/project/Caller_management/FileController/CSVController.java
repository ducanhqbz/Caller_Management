package vnpt.project.Caller_management.FileController;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.catalina.connector.ClientAbortException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vnpt.project.Caller_management.Services.CdrServices;
import vnpt.project.Caller_management.Services.ExcelServices;
import vnpt.project.Caller_management.Services.ProgressService;
import vnpt.project.Caller_management.model.Cdr;
import vnpt.project.Caller_management.repository.CdrRepository;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.*;
import java.util.*;

@Controller
@RequestMapping("/file/csv")
public class CSVController {
    @Autowired
    ExcelServices excelServices;
    @Autowired
    CdrServices cdrServices;
    @Autowired
    ProgressService progressService;
    @Autowired
    CdrRepository cdrRepository;

    @PostMapping("/upload")
    @ResponseBody
    public String upload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        int count = 0;
        if (file.getSize() == 0) {
            return "{\"status\" :\"error\".\"message\":\"file is empty\"}";

        }
        if (file.isEmpty()) {
            return "{\"status\": \"error\", \"message\": \"File không tồn tại.\"}";
        }
        if (!file.getOriginalFilename().substring(file.getOriginalFilename().length() - 3).equalsIgnoreCase("csv")) {
            return "{\"status\": \"error\", \"message\": \"Chỉ chấp nhận file có đuôi .csv.\"}";
        }

        long startTime = System.currentTimeMillis();
        try (InputStream inputStream = file.getInputStream(); // tạo ra 1 mảng các file dữ liệu dưới dạng byte
             InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8"); // từ file dữ liệu đó dịch thành các kí tự theo
             LineIterator lineIterator = IOUtils.lineIterator(inputStreamReader)) { // đọc dữ liệu theo từng dòng

            int totalLines = 0;
            while (lineIterator.hasNext()) {
                totalLines++;
                lineIterator.nextLine();
            }

            // Đặt lại inputStream và thiết lập tổng số dòng
            InputStream newInputStream = file.getInputStream();
            progressService.setTotalRecords(totalLines);

            // Đọc lại từ đầu và bỏ qua dòng tiêu đề
            LineIterator newLineIterator = IOUtils.lineIterator(new InputStreamReader(newInputStream, "UTF-8"));
            if (newLineIterator.hasNext()) {
                newLineIterator.nextLine(); // Bỏ qua tiêu đề
            }

            List<Cdr> cdrBatchList = new ArrayList<>();
            // Sử dụng newLineIterator thay vì lineIterator
            while (newLineIterator.hasNext()) {
                String line = newLineIterator.nextLine();
                String[] row = line.split(",");

                try {
                    // Extract and clean the data
                    String uuid = row[1];
                    int siteId = row[2].matches("\\d+") ? Integer.parseInt(row[2].replace("\"", "").trim()) : 0;
                    String dialNumber = row[3].replace("\"", "").trim();
                    String extension = row[4];
                    String msisdn = row[5];
                    String answerState = row[6];
                    String customerState = row[7];
                    String callDirection = row[8];
                    String uuidMap = row[9];
                    int transferred = row[10].matches("\\d+") ? Integer.parseInt(row[10].replace("\"", "").trim()) : 0;
                    String msisdnDigits = row[11].replace("\"", "").trim();
                    String agentDigits = row[12];
                    String xmlPath = row[13];
                    String recordingPath = row[14];
                    int clientId = row[15].matches("\\d+") ? Integer.parseInt(row[15].replace("\"", "").trim()) : 0;
                    String lat = row[16];
                    String lng = row[17];
                    String location = row[18];
                    String locationTolerance = row[19];
                    int autocallCampaignId = row[20].matches("\\d+") ? Integer.parseInt(row[20].replace("\"", "").trim()) : 0;
                    String address = row[21];
                    Integer tappingId = row[22].isEmpty() ? null : Integer.parseInt(row[22].replace("\"", "").trim());
                    int status = row[23].matches("\\d+") ? Integer.parseInt(row[23].replace("\"", "").trim()) : 0;
                    Long callLength = row[24].isEmpty() ? null : Long.parseLong(row[24].replace("\"", "").trim());
                    String startTimestamp = row[25];
                    String ringTimestamp = row[26];
                    String answerTimestamp = row[27];
                    String endTimestamp = row[28];
                    Long createdAt = Long.parseLong(row[29].replace("\"", "").trim());
                    Integer createdBy = row[30].isEmpty() ? null : Integer.parseInt(row[30].replace("\"", "").trim());
                    Long updatedAt = row[31].isEmpty() ? null : Long.parseLong(row[31].replace("\"", "").trim());
                    Integer updatedBy = row[32].isEmpty() ? null : Integer.parseInt(row[32].replace("\"", "").trim());
                    int isSpam = row[33].matches("\\d+") ? Integer.parseInt(row[33].replace("\"", "").trim()) : 0;
                    String disposition = row[34];
                    int earlyToTime = row[35].matches("\\d+") ? Integer.parseInt(row[35].replace("\"", "").trim()) : 0;
                    Integer talkTime = row[36].isEmpty() ? null : Integer.parseInt(row[36].replace("\"", "").trim());
                    int callWait = row[37].matches("\\d+") ? Integer.parseInt(row[37].replace("\"", "").trim()) : 0;
                    int source = row[38].matches("\\d+") ? Integer.parseInt(row[38].replace("\"", "").trim()) : 0;

                    // Create and save Cdr object
                    Cdr cdr = new Cdr();
                    cdr.setUuid(uuid);
                    cdr.setSiteId(siteId);
                    cdr.setDialNumber(dialNumber);
                    cdr.setExtension(extension);
                    cdr.setMsisdn(msisdn);
                    cdr.setAnswerState(answerState);
                    cdr.setCustomerState(customerState);
                    cdr.setCallDirection(callDirection);
                    cdr.setUuidMap(uuidMap);
                    cdr.setTransferred(transferred);
                    cdr.setMsisdnDigits(msisdnDigits);
                    cdr.setAgentDigits(agentDigits);
                    cdr.setXmlPath(xmlPath);
                    cdr.setRecordingPath(recordingPath);
                    cdr.setClientId(clientId);
                    cdr.setLat(lat);
                    cdr.setLng(lng);
                    cdr.setLocation(location);
                    cdr.setLocationTolerance(locationTolerance);
                    cdr.setAutocallCampaignId(autocallCampaignId);
                    cdr.setAddress(address);
                    cdr.setTappingId(tappingId);
                    cdr.setStatus(status);
                    cdr.setCallLength(callLength);
                    cdr.setStartTimestamp(startTimestamp);
                    cdr.setRingTimestamp(ringTimestamp);
                    cdr.setAnswerTimestamp(answerTimestamp);
                    cdr.setEndTimestamp(endTimestamp);
                    cdr.setCreatedAt(createdAt);
                    cdr.setCreatedBy(createdBy);
                    cdr.setUpdatedBy(updatedBy);
                    cdr.setIsSpam(isSpam);
                    cdr.setDisposition(disposition);
                    cdr.setEarlyToTime(earlyToTime);
                    cdr.setTalkTime(talkTime);
                    cdr.setCallWait(callWait);
                    cdr.setSource(source);
                    System.out.println(cdr);
                    // Add to batch list
                    cdrBatchList.add(cdr);
                    count++;
                    if (count == 1000) {
                        cdrServices.saveAll(cdrBatchList);
                        cdrBatchList.clear();
                        count = 0;
                    }

                    // Cập nhật tiến trình
                    progressService.updateProgress();

                } catch (Exception e) {
                    System.out.println("Lỗi khi xử lý dòng: " + String.join(",", row));
                    e.printStackTrace();
                }
            }

            // Lưu dữ liệu còn lại
            if (count > 0) {
                cdrServices.saveAll(cdrBatchList);
            }

            // Đặt lại tiến trình sau khi hoàn tất
            progressService.TotalRecords();

        } catch (Exception e) {
            e.printStackTrace();
            return "{\"status\": \"error\", \"message\": \"Đã xảy ra lỗi khi đọc tệp CSV: " + e.getMessage() + "\"}";
        }

        return "{\"status\": \"success\", \"message\": \"Upload thành công!\"}";
    }


    @GetMapping(value = "/progress")
    // Thay bằng cổng của frontend nếu khác
    @ResponseBody
    public Map<String, Integer> getProgress() {
        Map<String, Integer> response = new HashMap<>();
        response.put("progress", progressService.getProgressPercentage());
        System.out.println("day la progress:" + response);
        return response;
    }

    @GetMapping(value = "/reload")// Thay bằng cổng của frontend nếu khác
    @ResponseBody
    public Map<String, Integer> reload() {
        Map<String, Integer> response = new HashMap<>();
        progressService.reset();
        response.put("progress", 0);
        System.out.println("day la progress:" + response);
        return response;
    }

    @PostMapping("/exportExcel")
    @ResponseBody
    public Map<String, String> generateExcel(HttpServletResponse response) {
        // Generate a random file name
        String randomFileName = "excel_" + UUID.randomUUID().toString() + ".xlsx";
        String directoryPath = "D:\\HardWork\\Caller_management\\src\\main\\resources\\static\\ExcelFiles";
        String filePath = directoryPath + "\\" + randomFileName;

        // Ensure the directory exists



        try (Workbook workbook = new XSSFWorkbook()) {
            // Create a new sheet in the workbook
            Sheet sheet = workbook.createSheet("Sheet1");

            // Write the header to the sheet
            ExcelServices.writeHeader(sheet, 0);

            int offset = 0;
            int batchSize = 1000;
            int rowCount = 1; // Start writing after the header row

            while (true) {
                // Fetch a batch of data
                List<Cdr> dataBatch = fetchDataBatch(offset, batchSize);

                // Break the loop if there are no more data rows
                if (dataBatch.isEmpty()) {
                    break;
                }

                // Write each row from the batch to the sheet
                for (Cdr cdr : dataBatch) {
                    Row row = sheet.createRow(rowCount++);
                    ExcelServices.WriteCdr(cdr,row); // Changed WriteCdr to writeCdr for naming consistency
                }

                // Increment the offset for the next batch
                offset++;
            }

            // Write the workbook to the file system
            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            }

            System.out.println("Excel file written successfully: " + filePath);

        } catch (Exception e) {
            throw new RuntimeException("Error while generating Excel file", e);
        }

        // Return the file path as a response
        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("file", filePath);
        return responseMap;
    }



    @PostMapping("/exportCSV")
    @ResponseBody
    public Map<String, String> generateFile(HttpServletResponse response) {
        String randomFileName = "testout_" + UUID.randomUUID().toString() + ".txt";
        String directoryPath = "src/main/resources/static/files";
        String filePath = directoryPath + "/" + randomFileName;

        try {
            // Tạo thư mục nếu chưa tồn tại
            File directory = new File(directoryPath);
            if (!directory.exists()) {
                boolean created = directory.mkdirs();
                if (!created) {
                    throw new IOException("Failed to create directory: " + directoryPath);
                }
            }

            // Ghi dữ liệu vào file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                // Ghi header
                writer.write("id, uuid, site_id, dial_number, extension, msisdn, answer_state, customer_state, call_direction, uuid_map, transferred, msisdn_digits, agent_digits, xml_path, recording_path, client_id, lat, lng, location, location_tolerance, autocall_campaign_id, address, xtapping_id, status, call_length, start_timestamp, ring_timestamp, answer_timestamp, end_timestamp, created_at, created_by, updated_by, is_spam, disposition, early_to_time, talk_time, call_wait, source");
                writer.newLine();

                int offset = 0;
                int batchSize = 1000;

                while (true) {
                    // Lấy 1000 bản ghi dữ liệu
                    List<Cdr> dataBatch = fetchDataBatch(offset, batchSize);
                    if (dataBatch.isEmpty()) break;

                    // Ghi dữ liệu từng bản ghi vào file
                    for (Cdr c : dataBatch) {
                        writer.write(
                                c.getId() + ", " +
                                        c.getUuid() + ", " +
                                        c.getSiteId() + ", " +
                                        c.getDialNumber() + ", " +
                                        c.getExtension() + ", " +
                                        c.getMsisdn() + ", " +
                                        c.getAnswerState() + ", " +
                                        c.getCustomerState() + ", " +
                                        c.getCallDirection() + ", " +
                                        c.getUuidMap() + ", " +
                                        c.getTransferred() + ", " +
                                        c.getMsisdnDigits() + ", " +
                                        c.getAgentDigits() + ", " +
                                        c.getXmlPath() + ", " +
                                        c.getRecordingPath() + ", " +
                                        c.getClientId() + ", " +
                                        c.getLat() + ", " +
                                        c.getLng() + ", " +
                                        c.getLocation() + ", " +
                                        c.getLocationTolerance() + ", " +
                                        c.getAutocallCampaignId() + ", " +
                                        c.getAddress() + ", " +
                                        c.getTappingId() + ", " +
                                        c.getStatus() + ", " +
                                        c.getCallLength() + ", " +
                                        c.getStartTimestamp() + ", " +
                                        c.getRingTimestamp() + ", " +
                                        c.getAnswerTimestamp() + ", " +
                                        c.getEndTimestamp() + ", " +
                                        c.getCreatedAt() + ", " +
                                        c.getCreatedBy() + ", " +
                                        c.getUpdatedBy() + ", " +
                                        c.getIsSpam() + ", " +
                                        c.getDisposition() + ", " +
                                        c.getEarlyToTime() + ", " +
                                        c.getTalkTime() + ", " +
                                        c.getCallWait() + ", " +
                                        c.getSource()
                        );
                        writer.newLine();
                    }

                    offset ++; // Tăng offset để lấy batch tiếp theo
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error while generating file: " + e.getMessage(), e);
        }

        // Trả về đường dẫn file để download
        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("file", "/files/" + randomFileName);
        return responseMap;
    }


    @Transactional
    protected List<Cdr> fetchDataBatch(int pageNumber, int batchSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, batchSize);
        List<Cdr> dataBatch = cdrRepository.findAll(pageRequest).getContent();
        return dataBatch;
    }

    private static Workbook getWorkbook(InputStream inputStream, String excelFilePath) throws IOException {
        Workbook workbook = null;
        if (excelFilePath.endsWith("xlsx")) {
            workbook = new XSSFWorkbook(inputStream);
        } else if (excelFilePath.endsWith("xls")) {
            workbook = new HSSFWorkbook(inputStream);
        } else {
            throw new IllegalArgumentException("The specified file is not Excel file");
        }

        return workbook;
    }
}


