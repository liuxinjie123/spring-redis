package com.dream.springredis.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class CityDTO implements Serializable {
    private String provinceCode;
    private String provinceName;
    private String cityCode;
    private String cityName;
    private String countyCode;
    private String countyName;
}
