package com.momstouch.momstouchbe.global.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@Configuration
public class FileConfig implements WebMvcConfigurer {

    @Value("${fileDir.inbound}")
    private String inBoundDir;

    @Value("${fileDir.outbound}")
    private String outBoundDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("file config 진입");

        /**
         * images/** 로 들어오면 -> Ueers/doucments/qrCode/images/** 로 매핑 해주도록 하면 됨
         */
        registry
                .addResourceHandler(inBoundDir + "/**")
                .addResourceLocations("file://" + outBoundDir + "/");
    }
}
