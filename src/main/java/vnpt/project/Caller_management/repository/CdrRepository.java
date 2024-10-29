package vnpt.project.Caller_management.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vnpt.project.Caller_management.model.Cdr;

import java.util.List;

public interface CdrRepository extends JpaRepository<Cdr, Integer> {
    Page<Cdr> findByDialNumberContaining(String dialNumber, Pageable pageable);
    Page<Cdr> findByDialNumber(String dialNumber, Pageable pageable);
    Page<Cdr> findByDialNumberStartingWith(String dialNumber, Pageable pageable);

    @Query("select c from Cdr c where c.recordingPath = :recordingPath")
    Page<Cdr> findByRecordingPath(String recordingPath, Pageable pageable);

    Page<Cdr> findAll(Pageable pageable);

    @Query("Select c from Cdr c where c.dialNumber=:dialnumber and c.createdAt between :beginDate and :endDate")
    Page<Cdr> findByNumberAndDate(long beginDate, String dialnumber, long endDate, Pageable pageable);
    @Query("Select c from Cdr c where c.createdAt between :beginDate and :endDate")
Page<Cdr> findByDate(long beginDate, long endDate, Pageable pageable);


}
