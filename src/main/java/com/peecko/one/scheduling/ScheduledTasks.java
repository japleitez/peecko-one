package com.peecko.one.scheduling;

import com.peecko.one.service.ApsPlanTaskService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * cron syntax
 * <second> <minute> <hour> <day of month> <month> <day of week>
 */

@Component
public class ScheduledTasks {

    private final ApsPlanTaskService apsPlanTaskService;

    public ScheduledTasks(ApsPlanTaskService apsPlanTaskService) {
        this.apsPlanTaskService = apsPlanTaskService;
    }

    @Async
    @Scheduled(cron = "0 0/5 * * * *", zone = "Europe/Paris")
    public void closeExpiredPlans() {
        apsPlanTaskService.closeExpiredPlans();
    }

}
