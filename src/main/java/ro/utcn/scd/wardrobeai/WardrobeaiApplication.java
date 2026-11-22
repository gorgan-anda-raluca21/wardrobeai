package ro.utcn.scd.wardrobeai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class WardrobeaiApplication {

	public static void main(String[] args) {
		SpringApplication.run(WardrobeaiApplication.class, args);
	}
}