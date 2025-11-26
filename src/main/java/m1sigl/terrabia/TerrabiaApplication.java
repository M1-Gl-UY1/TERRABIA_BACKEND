package m1sigl.terrabia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude={SecurityAutoConfiguration.class})
public class TerrabiaApplication {

	public static void main(String[] args) {
		SpringApplication.run(TerrabiaApplication.class, args);
	}

}
