package com.ngenebio.msa.chart.model;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDto<T> {
    private int appCode;
    private String message;
    private T data;
}