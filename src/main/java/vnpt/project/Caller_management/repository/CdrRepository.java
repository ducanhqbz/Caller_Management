package vnpt.project.Caller_management.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import vnpt.project.Caller_management.model.Cdr;

import java.util.List;

public interface CdrRepository extends JpaRepository<Cdr, Integer> {
    Page<Cdr> findByDialNumberContaining(String dialNumber, Pageable pageable);

    Page<Cdr> findByDialNumber(String dialNumber, Pageable pageable);

    Page<Cdr> findByDialNumberStartingWith(String dialNumber, Pageable pageable);

    @Query("select c from Cdr c where c.recordingPath = :recordingPath")
    Page<Cdr> findByRecordingPath(String recordingPath, Pageable pageable);

    Page<Cdr> findAll(Pageable pageable);

    @Query(value = "SELECT * FROM cdr c LIMIT 10", nativeQuery = true)
    List<Cdr> findTop10();
    List<Cdr> findAll();
    @Query("Select c from Cdr c where c.dialNumber=:dialnumber and c.createdAt between :beginDate and :endDate")
    Page<Cdr> findByNumberAndDate(long beginDate, String dialnumber, long endDate, Pageable pageable);

    @Query("Select c from Cdr c where c.createdAt between :beginDate and :endDate")
    Page<Cdr> findByDate(long beginDate, long endDate, Pageable pageable);

    @Transactional
    public default List<Cdr> fetchDataBatch(int offset, int batchSize) {
        PageRequest pageRequest = PageRequest.of(offset / batchSize, batchSize);
        // Sử dụng getContent() để lấy List<Cdr> từ Page<Cdr>
        return findAll(pageRequest).getContent();
    }

}
