package com.slembers.alarmony;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class AlarmonyApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlarmonyApplication.class, args);
	}

}
