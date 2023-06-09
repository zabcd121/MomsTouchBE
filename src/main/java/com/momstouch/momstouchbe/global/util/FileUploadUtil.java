package com.momstouch.momstouchbe.global.util;

import com.momstouch.momstouchbe.global.vo.Image;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystemException;
import java.time.LocalDate;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class FileUploadUtil {

    @Value("${fileDir.inbound}")
    private String inbound;

    @Value("${fileDir.outbound}")
    private String outbound;

    @Value("${fileDir.menus}")
    private String menus;

    public String uploadMenuImage(MultipartFile multipartFile) throws IOException {
        Image image = uploadFile(multipartFile, menus);
        return image.getFileUrl() + image.getFileLocalName();
    }

    private Image uploadFile(MultipartFile multipartFile, String path) throws IOException {
        if(multipartFile == null) {
            return null;
        }

        String fileOriName = multipartFile.getOriginalFilename();
        String fileExtension = FilenameUtils.getExtension(fileOriName);

        String fileUrl = path + "/" + LocalDate.now().getYear() + "/" + LocalDate.now().getMonthValue() + "/" + LocalDate.now().getDayOfMonth() + "/";

        String destinationFileName = UUID.randomUUID() + "." + fileExtension;
        File destinationFile = new File(outbound + fileUrl + destinationFileName);
        destinationFile.getParentFile().mkdirs();
        multipartFile.transferTo(destinationFile);

        return Image.builder()
                .fileLocalName(destinationFileName)
                .fileOriName(fileOriName)
                .fileUrl(inbound + fileUrl)
                .build();

    }


}
