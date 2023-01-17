package com.ngenebio.msa.chart.controller;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chart/hla")
public class HlaChartController {
    @PostMapping("/base-variation-plot")
    public ResponseEntity<Resource> generateBaseVariationPlotChart() {
        // TODO: 차트 생성 서비스 호출
//        return ResponseEntity.ok()
//                .contentType(reportFileFormat.getMediaTypeValue())
//                .contentLength(0)
//                .body(resource);
        throw new NotImplementedException();
    }
}
