package api.gymmanagement.DTOs;

import java.math.BigDecimal;

public record RevenueReportResponse(
        String gymName,
        BigDecimal amount,
        String currency
) {}