package dev.social.media.security;

import dev.social.media.security.config.PasswordEncoderConfig;
import dev.social.media.security.model.AppUser;
import dev.social.media.security.model.UserRole;
import dev.social.media.security.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.format.annotation.DateTimeFormat;

import java.text.DateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;


@SpringBootApplication
@EnableMongoAuditing
public class MoviesSpringApplication {

	public static void main(String[] args) {
		SpringApplication.run(MoviesSpringApplication.class, args);
	}
	private PasswordEncoderConfig passwordEncoderConfig;
/*
	@Bean
	CommandLineRunner run(UserService userService) {
			return  args -> {
				userService.saveRole(new UserRole(null,"USER"));
				userService.saveRole(new UserRole(null,"MANAGER"));
				userService.saveRole(new UserRole(null,"ADMIN"));
				//userService.saveRole(new UserRole(null,"SUPER_ADMIN"));
				LocalDateTime dateTime = LocalDateTime.now();

				userService.createUser(new AppUser(null,"Travolta","john@gmail.com","password",dateTime,null,"street 23","John","Travolta", LocalDate.of(1969,12,4),false,null,new ArrayList<>()));
				userService.createUser(new AppUser(null,"Smith","will@gmail.com","password",dateTime,null,"pd 23","Will","Smith", LocalDate.of(183,8,21),false,null,new ArrayList<>()));
				userService.createUser(new AppUser(null,"Carry","jim@gmail.com","password",dateTime,null,"street 23","Jim","Carry", LocalDate.of(1978,5,2),false,null,new ArrayList<>()));
				userService.createUser(new AppUser(null,"Schwarzenegger","arnold@gmail.com","password",dateTime,null,"cal 32","Arnold","Schwarzenegger", LocalDate.of(1966,3,20),false,null,new ArrayList<>()));




				userService.addRoleToUser("john@gmail.com","USER");
				userService.addRoleToUser("john@gmail.com","MANAGER");
				userService.addRoleToUser("will@gmail.com","MANAGER");
				userService.addRoleToUser("jim@gmail.com","ADMIN");
				userService.addRoleToUser("arnold@gmail.com","SUPER_MANAGER");
				userService.addRoleToUser("arnold@gmail.com","ADMIN");
				userService.addRoleToUser("arnold@gmail.com","USER");


			};
		}*/
	}


