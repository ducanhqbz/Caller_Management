package vnpt.project.Caller_management.FileController;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vnpt.project.Caller_management.Services.CdrServices;
import vnpt.project.Caller_management.Services.ProgressService;
import vnpt.project.Caller_management.model.Cdr;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/file/csv")
public class CSVController {
    @Autowired
    CdrServices cdrServices;
    @Autowired
    ProgressService progressService;

    @PostMapping("/upload")
    @ResponseBody
    public String upload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        int count = 0;
        if (file.isEmpty()) {
            return "{\"status\": \"error\", \"message\": \"File không tồn tại.\"}";
        }

        long startTime = System.currentTimeMillis();
        try (InputStream inputStream = file.getInputStream();
             InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
             LineIterator lineIterator = IOUtils.lineIterator(inputStreamReader)) {

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


//    @PostMapping("/uploadOther")
//    @ResponseBody
//    public String uploadOther(@RequestParam("file") MultipartFile file) {
//        int count = 0;
//        if (file.isEmpty()) {
//            return "{\"status\": \"error\", \"message\": \"File không tồn tại.\"}";
//        }
//
//        try (InputStream inputStream = file.getInputStream()) {
//            // Đọc file để tính tổng số dòng
//            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
//            LineIterator lineIterator = IOUtils.lineIterator(inputStreamReader);
//
//            int totalLines = 0;
//            while (lineIterator.hasNext()) {
//                totalLines++;
//                lineIterator.nextLine();
//            }
//
//            // Đặt lại inputStream và thiết lập tổng số dòng
////            inputStream.close(); // Đóng lại để có thể mở lại sau
////            InputStream newInputStream = file.getInputStream();
//            progressService.setTotalRecords(totalLines);
//
//            // Đọc lại từ đầu và bỏ qua dòng tiêu đề
////            LineIterator newLineIterator = IOUtils.lineIterator(new InputStreamReader(newInputStream, "UTF-8"));
//            if (lineIterator.hasNext()) {
//                lineIterator.nextLine(); // Bỏ qua tiêu đề
//            }
//
//            List<Cdr> cdrBatchList = new ArrayList<>();
//            while (lineIterator.hasNext()) {
//                String line = lineIterator.nextLine();
//                String[] row = line.split(",");
//
//                try {
//                    String uuid = row[1];
//                    int siteId = row[2].matches("\\d+") ? Integer.parseInt(row[2].replace("\"", "").trim()) : 0;
//                    String dialNumber = row[3];
//                    String extension = row[4];
//                    String msisdn = row[5];
//                    String answerState = row[6];
//                    String customerState = row[7];
//                    String callDirection = row[8];
//                    String uuidMap = row[9];
//                    int transferred = row[10].matches("\\d+") ? Integer.parseInt(row[10].replace("\"", "").trim()) : 0;
//                    String msisdnDigits = row[11];
//                    String agentDigits = row[12];
//                    String xmlPath = row[13];
//                    String recordingPath = row[14];
//                    int clientId = row[15].matches("\\d+") ? Integer.parseInt(row[15].replace("\"", "").trim()) : 0;
//                    String lat = row[16];
//                    String lng = row[17];
//                    String location = row[18];
//                    String locationTolerance = row[19];
//                    int autocallCampaignId = row[20].matches("\\d+") ? Integer.parseInt(row[20].replace("\"", "").trim()) : 0;
//                    String address = row[21];
//                    Integer tappingId = row[22].isEmpty() ? null : Integer.parseInt(row[22].replace("\"", "").trim());
//                    int status = row[23].matches("\\d+") ? Integer.parseInt(row[23].replace("\"", "").trim()) : 0;
//                    Long callLength = row[24].isEmpty() ? null : Long.parseLong(row[24].replace("\"", "").trim());
//                    String startTimestamp = row[25];
//                    String ringTimestamp = row[26];
//                    String answerTimestamp = row[27];
//                    String endTimestamp = row[28];
//                    Long createdAt = Long.parseLong(row[29].replace("\"", "").trim());
//                    Integer createdBy = row[30].isEmpty() ? null : Integer.parseInt(row[30].replace("\"", "").trim());
//                    Long updatedAt = row[31].isEmpty() ? null : Long.parseLong(row[31].replace("\"", "").trim());
//                    Integer updatedBy = row[32].isEmpty() ? null : Integer.parseInt(row[32].replace("\"", "").trim());
//                    int isSpam = row[33].matches("\\d+") ? Integer.parseInt(row[33].replace("\"", "").trim()) : 0;
//                    String disposition = row[34];
//                    int earlyToTime = row[35].matches("\\d+") ? Integer.parseInt(row[35].replace("\"", "").trim()) : 0;
//                    Integer talkTime = row[36].isEmpty() ? null : Integer.parseInt(row[36].replace("\"", "").trim());
//                    int callWait = row[37].matches("\\d+") ? Integer.parseInt(row[37].replace("\"", "").trim()) : 0;
//                    int source = row[38].matches("\\d+") ? Integer.parseInt(row[38].replace("\"", "").trim()) : 0;
//
//                    // Create and save Cdr object
//                    Cdr cdr = new Cdr();
//                    cdr.setUuid(uuid);
//                    cdr.setSiteId(siteId);
//                    cdr.setDialNumber(dialNumber);
//                    cdr.setExtension(extension);
//                    cdr.setMsisdn(msisdn);
//                    cdr.setAnswerState(answerState);
//                    cdr.setCustomerState(customerState);
//                    cdr.setCallDirection(callDirection);
//                    cdr.setUuidMap(uuidMap);
//                    cdr.setTransferred(transferred);
//                    cdr.setMsisdnDigits(msisdnDigits);
//                    cdr.setAgentDigits(agentDigits);
//                    cdr.setXmlPath(xmlPath);
//                    cdr.setRecordingPath(recordingPath);
//                    cdr.setClientId(clientId);
//                    cdr.setLat(lat);
//                    cdr.setLng(lng);
//                    cdr.setLocation(location);
//                    cdr.setLocationTolerance(locationTolerance);
//                    cdr.setAutocallCampaignId(autocallCampaignId);
//                    cdr.setAddress(address);
//                    cdr.setTappingId(tappingId);
//                    cdr.setStatus(status);
//                    cdr.setCallLength(callLength);
//                    cdr.setStartTimestamp(startTimestamp);
//                    cdr.setRingTimestamp(ringTimestamp);
//                    cdr.setAnswerTimestamp(answerTimestamp);
//                    cdr.setEndTimestamp(endTimestamp);
//                    cdr.setCreatedAt(createdAt);
//                    cdr.setCreatedBy(createdBy);
//                    cdr.setUpdatedBy(updatedBy);
//                    cdr.setIsSpam(isSpam);
//                    cdr.setDisposition(disposition);
//                    cdr.setEarlyToTime(earlyToTime);
//                    cdr.setTalkTime(talkTime);
//                    cdr.setCallWait(callWait);
//                    cdr.setSource(source);
//
//                    // Add to batch list
//                    cdrBatchList.add(cdr);
//                    count++;
//                    if (count == 1000) {
//                        cdrServices.saveAll(cdrBatchList);
//                        cdrBatchList.clear();
//                        count = 0;
//                    }
//
//                    // Cập nhật tiến trình
//                    progressService.updateProgress();
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            // Lưu dữ liệu còn lại
//            if (count > 0) {
//                cdrServices.saveAll(cdrBatchList);
//                cdrBatchList.clear();
//            }
//
//            progressService.resetProgress();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "{\"status\": \"error\", \"message\": \"Đã xảy ra lỗi khi đọc tệp CSV: " + e.getMessage() + "\"}";
//        }
//
//        return "{\"status\": \"success\", \"message\": \"Upload thành công!\"}";
//    }

    @GetMapping( value = "/progress" )
    @CrossOrigin(origins = "http://localhost:3000") // Thay bằng cổng của frontend nếu khác
    @ResponseBody
    public Map<String, Integer> getProgress() {
        Map<String, Integer> response = new HashMap<>();
        response.put("progress", progressService.getProgressPercentage());
        System.out.println("day la progress:"+response);
        return response;
    }

    @GetMapping( value = "/reload" )
    @CrossOrigin(origins = "http://localhost:3000") // Thay bằng cổng của frontend nếu khác
    @ResponseBody
    public Map<String, Integer> reload() {
        Map<String, Integer> response = new HashMap<>();
        progressService.reset();
        response.put("progress",0);
        System.out.println("day la progress:"+response);
        return response;
    }
}
