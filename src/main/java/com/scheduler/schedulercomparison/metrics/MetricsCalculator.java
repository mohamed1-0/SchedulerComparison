package com.scheduler.schedulercomparison.metrics;

import com.scheduler.schedulercomparison.model.Process;

import java.util.List;

/**
 * بتحسب المقاييس (Metrics) بعد ما الخوارزمية تخلص
 * WT, TAT, RT لكل process + المتوسطات
 */
public class MetricsCalculator {

    private List<Process> processes;

    public MetricsCalculator(List<Process> processes) {
        this.processes = processes;
    }

    // ========== Average Waiting Time ==========
    public double getAverageWaitingTime() {
        double total = 0;
        for (Process p : processes) {
            total += p.getWaitingTime();
        }
        return total / processes.size();
    }

    // ========== Average Turnaround Time ==========
    public double getAverageTurnaroundTime() {
        double total = 0;
        for (Process p : processes) {
            total += p.getTurnaroundTime();
        }
        return total / processes.size();
    }

    // ========== Average Response Time ==========
    public double getAverageResponseTime() {
        double total = 0;
        for (Process p : processes) {
            total += p.getResponseTime();
        }
        return total / processes.size();
    }

    // ========== CPU Utilization ==========
    // نسبة الوقت اللي الـ CPU كانت شغالة فيه
    public double getCpuUtilization() {
        int totalBurst = 0;
        int totalTime = 0;

        for (Process p : processes) {
            totalBurst += p.getBurstTime();
            totalTime = Math.max(totalTime, p.getCompletionTime());
        }

        if (totalTime == 0) return 0;
        return ((double) totalBurst / totalTime) * 100;
    }

    // ========== Throughput ==========
    // عدد الـ processes اللي خلصت في وحدة الوقت
    public double getThroughput() {
        int totalTime = 0;
        for (Process p : processes) {
            totalTime = Math.max(totalTime, p.getCompletionTime());
        }
        if (totalTime == 0) return 0;
        return (double) processes.size() / totalTime;
    }

    // ========== Summary String ==========
    public String getSummary() {
        return String.format(
                "Avg WT: %.2f | Avg TAT: %.2f | Avg RT: %.2f | CPU: %.1f%% | Throughput: %.3f",
                getAverageWaitingTime(),
                getAverageTurnaroundTime(),
                getAverageResponseTime(),
                getCpuUtilization(),
                getThroughput()
        );
    }
}