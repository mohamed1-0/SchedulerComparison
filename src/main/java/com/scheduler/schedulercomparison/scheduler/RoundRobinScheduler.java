package com.scheduler.schedulercomparison.scheduler;

import com.scheduler.schedulercomparison.model.GanttEntry;
import com.scheduler.schedulercomparison.model.Process;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class RoundRobinScheduler {

    private int quantum;

    public RoundRobinScheduler(int quantum) {
        this.quantum = quantum;
    }

    public List<GanttEntry> schedule(List<Process> originalProcesses) {

        List<Process> processes = copyProcesses(originalProcesses);

        List<GanttEntry> gantt = new ArrayList<>();
        Queue<Process> readyQueue = new LinkedList<>();

        int currentTime = 0;
        int completed = 0;
        int n = processes.size();

        processes.sort((a, b) -> a.getArrivalTime() - b.getArrivalTime());

        int index = 0;

        while (index < n && processes.get(index).getArrivalTime() <= currentTime) {
            readyQueue.add(processes.get(index));
            index++;
        }

        while (completed < n) {

            if (readyQueue.isEmpty()) {
                currentTime = processes.get(index).getArrivalTime();
                while (index < n && processes.get(index).getArrivalTime() <= currentTime) {
                    readyQueue.add(processes.get(index));
                    index++;
                }
            }

            Process current = readyQueue.poll();

            if (!current.isStarted()) {
                current.setResponseTime(currentTime - current.getArrivalTime());
                current.setStarted(true);
            }

            int timeSlice = Math.min(quantum, current.getRemainingTime());

            gantt.add(new GanttEntry(current.getId(), currentTime, currentTime + timeSlice));

            currentTime += timeSlice;
            current.setRemainingTime(current.getRemainingTime() - timeSlice);

            while (index < n && processes.get(index).getArrivalTime() <= currentTime) {
                readyQueue.add(processes.get(index));
                index++;
            }

            if (current.getRemainingTime() == 0) {
                completed++;
                current.setCompletionTime(currentTime);
                current.setTurnaroundTime(currentTime - current.getArrivalTime());
                current.setWaitingTime(current.getTurnaroundTime() - current.getBurstTime());
            } else {
                readyQueue.add(current);
            }
        }

        copyResultsBack(processes, originalProcesses);

        return gantt;
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

    public int getQuantum() { return quantum; }
}