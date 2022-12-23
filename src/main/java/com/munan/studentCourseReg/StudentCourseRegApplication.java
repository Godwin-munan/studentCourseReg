package com.munan.studentCourseReg;

import com.munan.studentCourseReg.model.AppUser;
import com.munan.studentCourseReg.model.Role;
import com.munan.studentCourseReg.service.AppUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

@SpringBootApplication
public class StudentCourseRegApplication {

	private static final Logger log = LoggerFactory.getLogger(StudentCourseRegApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(StudentCourseRegApplication.class, args);
	}

	@Bean
	CommandLineRunner runner(AppUserService user){
		return args ->{

			try {
				user.commandLine(
						new Role(null, "ROLE_ADMIN"),
						new AppUser(null, "admin@gmail.com", "1234",
								Collections.singleton(new Role(null, "ROLE_ADMIN"))));
			}catch(Exception e){
				log.debug("'{}'",e.getMessage().toUpperCase());
			}
		};
	}

	@Bean
	public PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}


}
