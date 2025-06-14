package aba3.lucid.domain.company.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DashboardResponse {
    private long newCompanies;
    private long newUsers;
    private long totalNewMembers;
    private long todayReports;
}