package vnpt.project.Caller_management.Services;

import org.springframework.stereotype.Service;

@Service
public class ProgressService {
    private int progressPercentage = 0;
    private int totalRecords = 0;
    private int processedRecords = 0;

    public synchronized void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
        this.processedRecords = 0; // reset processed records count
        this.progressPercentage = 0;
    }

    public synchronized void updateProgress() {
        processedRecords++;
        progressPercentage = (int) ((processedRecords * 100.0) / totalRecords);
    }

    public synchronized int getProgressPercentage() {
        return progressPercentage;
    }

    public synchronized void TotalRecords() {
        totalRecords = 100;
        processedRecords = 100;
        progressPercentage = 100;
    }
    public synchronized void reset() {
        totalRecords = 0;
        processedRecords = 0;
        progressPercentage = 0;
    }
}
