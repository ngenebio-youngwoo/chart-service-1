package com.ngenebio.msa.chart.test.support.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpringConfiguration {
    private FlywayConfiguration flyway;
}
