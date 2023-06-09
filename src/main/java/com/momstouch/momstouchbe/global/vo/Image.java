package com.momstouch.momstouchbe.global.vo;

import lombok.*;

import javax.persistence.Column;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
public class Image{

    @Column(length = 50)
    private String fileLocalName;

    @Column(length = 50)
    private String fileOriName;

    @Column(length = 50)
    private String fileUrl;
}
