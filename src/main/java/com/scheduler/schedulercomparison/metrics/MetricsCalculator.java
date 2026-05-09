package com.scheduler.schedulercomparison.metrics;

import com.scheduler.schedulercomparison.model.Process;

import java.util.List;

public class MetricsCalculator {

    private List<Process> processes;

    public MetricsCalculator(List<Process> processes) {
        this.processes = processes;
    }

    public double getAverageWaitingTime() {
        double total = 0;
        for (Process p : processes) {
            total += p.getWaitingTime();
        }
        return total / processes.size();
    }

    public double getAverageTurnaroundTime() {
        double total = 0;
        for (Process p : processes) {
            total += p.getTurnaroundTime();
        }
        return total / processes.size();
    }

    public double getAverageResponseTime() {
        double total = 0;
        for (Process p : processes) {
            total += p.getResponseTime();
        }
        return total / processes.size();
    }

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

    public double getThroughput() {
        int totalTime = 0;
        for (Process p : processes) {
            totalTime = Math.max(totalTime, p.getCompletionTime());
        }
        if (totalTime == 0) return 0;
        return (double) processes.size() / totalTime;
    }

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