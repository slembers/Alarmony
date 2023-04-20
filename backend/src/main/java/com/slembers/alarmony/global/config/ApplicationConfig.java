package com.slembers.alarmony.global.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    public ModelMapper modelMapper(){
        ModelMapper modelMapper = new ModelMapper();
         modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT).setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
                 .setFieldMatchingEnabled(true);  //기본은 standard
        return modelMapper;
    }
}