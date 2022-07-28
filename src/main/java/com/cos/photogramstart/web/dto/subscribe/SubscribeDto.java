package com.cos.photogramstart.web.dto.subscribe;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubscribeDto {

    private Integer id;
    private String username;
    private String profileImageUrl;
    private BigInteger subscribeState;
    private BigInteger equalUserState;
}
