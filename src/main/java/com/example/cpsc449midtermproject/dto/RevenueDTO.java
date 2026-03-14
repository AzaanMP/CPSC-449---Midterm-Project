package com.example.cpsc449midtermproject.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class RevenueDTO {
    private String eventTitle;
    private BigDecimal totalRevenue;
}