package com.DigitalNotebook.NoteWiz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.DigitalNotebook.NoteWiz.Repository")
@EntityScan(basePackages = "com.DigitalNotebook.NoteWiz.Model")
@EnableTransactionManagement
public class NoteWizApplication {

	public static void main(String[] args) {
		SpringApplication.run(NoteWizApplication.class, args);
	}

}
