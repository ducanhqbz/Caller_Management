package vnpt.project.Caller_management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "vnpt.project.Caller_management.model")
public class CallerManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(CallerManagementApplication.class, args);
	}

}
