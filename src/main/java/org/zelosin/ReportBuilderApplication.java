package org.zelosin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.zelosin.Base.ProcessingSample;
import org.zelosin.Configurations.Form.FilterType;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootApplication
public class ReportBuilderApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReportBuilderApplication.class, args);
	}
}
