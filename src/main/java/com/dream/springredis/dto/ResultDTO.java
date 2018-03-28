package com.dream.springredis.dto;

import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class ResultDTO<T> implements Serializable {
    @NonNull
    private String code;
    @NonNull
    private String msg;
    private T obj;
}
