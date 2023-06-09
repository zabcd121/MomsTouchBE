package com.momstouch.momstouchbe.setup;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class MockMultipartFileSetup {

    public MockMultipartFile getMockMultipartFile() {
        return new MockMultipartFile(
                "photo",
                "photo.png",
                "image/png",
                "helloWorld".getBytes(StandardCharsets.UTF_8)
        );
    }
}
