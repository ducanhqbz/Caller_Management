package vnpt.project.Caller_management.Services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import vnpt.project.Caller_management.model.Cdr;
import vnpt.project.Caller_management.repository.CdrRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
public class CdrServices {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    CdrRepository cdrRepository;

    private final JdbcTemplate jdbcTemplate;

    public CdrServices(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public long GetUnixTimeStamp(String date) {
        if (date == null || date.trim().isEmpty()) {
            // Trả về Unix timestamp của ngày hiện tại (00:00:00)
            return LocalDate.now().atStartOfDay().toEpochSecond(ZoneOffset.UTC);
        }

        try {
            // Loại bỏ khoảng trắng thừa và sử dụng định dạng "yyyy-MM-dd"
            String cleanedDate = date.trim();

            // Sử dụng định dạng ngày "yyyy-MM-dd"
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            // Phân tích chuỗi thành LocalDate
            LocalDate localDate = LocalDate.parse(cleanedDate, formatter);

            // Chuyển LocalDate thành Unix timestamp (00:00:00 của ngày đó)
            return localDate.atStartOfDay().toEpochSecond(ZoneOffset.UTC);
        } catch (Exception e) {
            throw new RuntimeException("Invalid date format: " + date, e);
        }
    }

    public int SaveCDR(Cdr cdr) {
        Cdr cdrSave = cdrRepository.save(cdr);
        if (cdrSave != null) {
            System.out.println("success");
            return 1;
        } else {
            return 0;
        }

    }

    @Transactional
    public void batchInsert(List<Cdr> cdrList) {
        int batchSize = 1000; // Cấu hình batch size tùy ý
        for (int i = 0; i < cdrList.size(); i++) {
            entityManager.persist(cdrList.get(i));
            if (i > 0 && i % batchSize == 0) {
                // Gửi batch và xóa cache để tránh tràn bộ nhớ
                entityManager.flush();
                entityManager.clear();
            }
        }
        // Gửi các bản ghi còn lại
        entityManager.flush();
        entityManager.clear();
    }

    public void saveAll(List<Cdr> cdrList) {
        cdrRepository.saveAll(cdrList);
    }
}



