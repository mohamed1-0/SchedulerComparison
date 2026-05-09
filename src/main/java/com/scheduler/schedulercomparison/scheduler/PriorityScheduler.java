package com.scheduler.schedulercomparison.scheduler;

import com.scheduler.schedulercomparison.model.GanttEntry;
import com.scheduler.schedulercomparison.model.Process;

import java.util.ArrayList;
import java.util.List;


public class PriorityScheduler {


    public List<GanttEntry> schedule(List<Process> originalProcesses) {

        List<Process> processes = copyProcesses(originalProcesses);

        List<GanttEntry> gantt = new ArrayList<>();

        int currentTime = 0;
        int completed = 0;
        int n = processes.size();

        processes.sort((a, b) -> a.getArrivalTime() - b.getArrivalTime());

        Process lastProcess = null;
        int sliceStart = 0;

        while (completed < n) {

            Process current = getHighestPriority(processes, currentTime);

            if (current == null) {
                currentTime++;
                continue;
            }

            if (lastProcess != null && !lastProcess.getId().equals(current.getId())) {
                gantt.add(new GanttEntry(lastProcess.getId(), sliceStart, currentTime));
                sliceStart = currentTime;
            } else if (lastProcess == null) {
                sliceStart = currentTime;
            }

            if (!current.isStarted()) {
                current.setResponseTime(currentTime - current.getArrivalTime());
                current.setStarted(true);
            }

            current.setRemainingTime(current.getRemainingTime() - 1);
            currentTime++;

            if (current.getRemainingTime() == 0) {
                completed++;
                current.setCompletionTime(currentTime);
                current.setTurnaroundTime(currentTime - current.getArrivalTime());
                current.setWaitingTime(current.getTurnaroundTime() - current.getBurstTime());

                gantt.add(new GanttEntry(current.getId(), sliceStart, currentTime));
                sliceStart = currentTime;
                lastProcess = null;
            } else {
                lastProcess = current;
            }
        }

        copyResultsBack(processes, originalProcesses);

        return gantt;
    }

    private Process getHighestPriority(List<Process> processes, int currentTime) {
        Process best = null;

        for (Process p : processes) {
            if (p.getArrivalTime() <= currentTime && p.getRemainingTime() > 0) {
                if (best == null) {
                    best = p;
                } else if (p.getPriority() < best.getPriority()) {
                    best = p;
                } else if (p.getPriority() == best.getPriority()) {
                    if (p.getArrivalTime() < best.getArrivalTime()) {
                        best = p;
                    }
                }
            }
        }
        return best;
    }

    private List<Process> copyProcesses(List<Process> original) {
        List<Process> copy = new ArrayList<>();
        for (Process p : original) {
            copy.add(new Process(p.getId(), p.getArrivalTime(), p.getBurstTime(), p.getPriority()));
        }
        return copy;
    }

    private void copyResultsBack(List<Process> calculated, List<Process> original) {
        for (Process orig : original) {
            for (Process calc : calculated) {
                if (orig.getId().equals(calc.getId())) {
                    orig.setCompletionTime(calc.getCompletionTime());
                    orig.setTurnaroundTime(calc.getTurnaroundTime());
                    orig.setWaitingTime(calc.getWaitingTime());
                    orig.setResponseTime(calc.getResponseTime());
                    orig.setStarted(calc.isStarted());
                    break;
                }
            }
        }
    }
}