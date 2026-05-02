package api.gymmanagement.controllers;

import api.gymmanagement.DTOs.RevenueReportResponse;
import api.gymmanagement.services.RevenueService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/revenue")
public class RevenueController {

    private final RevenueService revenueService;

    public RevenueController(RevenueService revenueService) {
        this.revenueService = revenueService;
    }

    // GET /api/revenue
    @GetMapping
    public List<RevenueReportResponse> getRevenueReport() {
        return revenueService.getRevenueReport();
    }
}