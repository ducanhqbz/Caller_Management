package vnpt.project.Caller_management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestParam;
import vnpt.project.Caller_management.Services.CdrServices;
import vnpt.project.Caller_management.model.Cdr;
import vnpt.project.Caller_management.repository.CdrRepository;



@Controller
public class CdrController {

    @Autowired
    private CdrRepository cdrRepository;

    @Autowired
    private CdrServices cdrServices;


    @GetMapping("/GetAllCDr")
    @PreAuthorize("hasRole('SYSADMIN')")
    public String GetAllCDr(@RequestParam(value = "dialNumber", required = false) String dialNumber,
                            @RequestParam(value = "page", defaultValue = "0") int page,
                            @RequestParam(value = "size", defaultValue = "30") int size,
                                @RequestParam(value = "time", required = false) Long time,
                            Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Cdr> cdrs;

        // Sử dụng tìm kiếm chính xác hoặc bắt đầu bằng

            cdrs = cdrRepository.findAll(pageable);
 if (time != null && time > 0){

     model.addAttribute("time",time)    ;
 }

        // Add attributes cho model
        model.addAttribute("cdrs", cdrs);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", cdrs.getTotalPages());
        model.addAttribute("size", size);
        model.addAttribute("dialNumber", dialNumber);  // Giữ lại giá trị dialNumber trong form tìm kiếm

        return "CdrManagement";
    }


    @GetMapping("/FilterCdrForm")
    @PreAuthorize("hasRole('SYSADMIN')")
    public String FilterCdr(@RequestParam(value = "dialNumber", required = false) String dialNumber,
                            @RequestParam(value = "page", defaultValue = "0") int page,
                            @RequestParam(value = "size", defaultValue = "30") int size,
                            @RequestParam(value = "beginDate", required = false) String beginDate,
                            @RequestParam(value = "EndDate", required = false) String EndDate, Model model) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Cdr> cdrs = null;
        long beginTimestamp=0;
        long endTimestamp=0;



if (!beginDate.isEmpty() && !EndDate.isEmpty()) {
    beginTimestamp = cdrServices.GetUnixTimeStamp(beginDate);
     endTimestamp = cdrServices.GetUnixTimeStamp(EndDate);
    if (beginTimestamp > endTimestamp) {
        model.addAttribute("status", "BeginDate must be smaller than EndDate");
        return "FilterCdr";
    }
}


            if (dialNumber.isEmpty() && beginDate.isEmpty() && EndDate.isEmpty()) {
                cdrs = cdrRepository.findAll(pageable);
                model.addAttribute("status", "Enter dialNumber or beginDate or EndDate");
            } else if (!dialNumber.isEmpty() && beginDate.isEmpty() && EndDate.isEmpty()) {
                System.out.println("This is find by dial Number");
               cdrs = cdrRepository.findByDialNumber(dialNumber.replaceAll("\\s", ""), pageable);
            } else if (!dialNumber.isEmpty() && !beginDate.isEmpty() && !EndDate.isEmpty()) {
                cdrs = cdrRepository.findByNumberAndDate(beginTimestamp, dialNumber, endTimestamp, pageable);
            } else if (dialNumber.isEmpty() && !beginDate.isEmpty() && !EndDate.isEmpty()) {

                cdrs = cdrRepository.findByDate(beginTimestamp, endTimestamp, pageable);
            }
        System.out.println(cdrs.getTotalPages());
            model.addAttribute("cdrFilter", cdrs);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", cdrs.getTotalPages());


        model.addAttribute("size", size);
        model.addAttribute("dialNumber", dialNumber);
        model.addAttribute("beginDate", beginDate);
        model.addAttribute("EndDate", EndDate);

        return "FilterCdr";
    }

}



