package com.tds.urlshortener.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatisticDTO {
    private Long totalAccessCount;
    private List<TopBrowserAccessDTO> topFiveBrowserAccess;
}
