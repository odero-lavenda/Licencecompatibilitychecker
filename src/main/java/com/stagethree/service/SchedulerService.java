package com.stagethree.service;

import com.stagethree.model.LicenseInfo;
import com.stagethree.utility.ResponseFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@Service
public class SchedulerService {
    private final LicenseService licenseService;
    private Timer timer;

    public SchedulerService(LicenseService licenseService) {
        this.licenseService = licenseService;
    }

    public void startDailySchedule() {
        timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                postDailyLicense();
            }
        }, 0, 24 * 60 * 60 * 1000);

        System.out.println("⏰ Daily scheduler started - License of the Day will post every 24h");
    }

    public void stopSchedule() {
        if (timer != null) {
            timer.cancel();
            System.out.println("⏰ Daily scheduler stopped");
        }
    }

    private void postDailyLicense() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("⏰ SCHEDULED POST: License of the Day");
        System.out.println("=".repeat(60));

        LicenseInfo info = licenseService.getDailyLicense();
        String message = ResponseFormatter.formatDailyLicense(info, licenseService);

        System.out.println(message);
        System.out.println("=".repeat(60) + "\n");

        // TODO: post message to Telex channel via API
    }
}
