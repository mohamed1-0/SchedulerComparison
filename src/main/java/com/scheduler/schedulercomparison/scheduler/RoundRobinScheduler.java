package com.scheduler.schedulercomparison.scheduler;

import com.scheduler.schedulercomparison.model.GanttEntry;
import com.scheduler.schedulercomparison.model.Process;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Round Robin Scheduler
 * كل process بتاخد quantum من الوقت، لو مخلصتش ترجع آخر الـ queue
 */
public class RoundRobinScheduler {

    private int quantum; // الـ Time Quantum

    public RoundRobinScheduler(int quantum) {
        this.quantum = quantum;
    }

    /**
     * الميثود الرئيسية اللي بتشغل الخوارزمية
     * بتاخد list من الـ processes وبترجع list من الـ GanttEntry
     */
    public List<GanttEntry> schedule(List<Process> originalProcesses) {

        // ========== خطوة 1: عمل نسخة من الـ processes عشان منغيرش الأصل ==========
        List<Process> processes = copyProcesses(originalProcesses);

        List<GanttEntry> gantt = new ArrayList<>(); // هنا هنحط الـ Gantt Chart
        Queue<Process> readyQueue = new LinkedList<>(); // الـ Queue اللي فيها الـ processes الجاهزة

        int currentTime = 0; // الساعة الحالية
        int completed = 0;   // عدد الـ processes اللي خلصت
        int n = processes.size();

        // ترتيب الـ processes حسب الـ Arrival Time
        processes.sort((a, b) -> a.getArrivalTime() - b.getArrivalTime());

        int index = 0; // بنتابع بيه مين اللي لسه مجاش

        // أضف أول process وصلت في الوقت 0
        while (index < n && processes.get(index).getArrivalTime() <= currentTime) {
            readyQueue.add(processes.get(index));
            index++;
        }

        // ========== خطوة 2: الـ Main Loop ==========
        while (completed < n) {

            // لو الـ queue فاضية، نقفز للـ process الجاية
            if (readyQueue.isEmpty()) {
                currentTime = processes.get(index).getArrivalTime();
                while (index < n && processes.get(index).getArrivalTime() <= currentTime) {
                    readyQueue.add(processes.get(index));
                    index++;
                }
            }

            // خد أول process من الـ queue
            Process current = readyQueue.poll();

            // سجل الـ Response Time لو أول مرة بتتخدم
            if (!current.isStarted()) {
                current.setResponseTime(currentTime - current.getArrivalTime());
                current.setStarted(true);
            }

            // احسب قد إيه هتشتغل دلوقتي
            int timeSlice = Math.min(quantum, current.getRemainingTime());

            // أضف خانة في الـ Gantt Chart
            gantt.add(new GanttEntry(current.getId(), currentTime, currentTime + timeSlice));

            // حدّث الوقت والـ remaining time
            currentTime += timeSlice;
            current.setRemainingTime(current.getRemainingTime() - timeSlice);

            // أضف الـ processes الجديدة اللي وصلت خلال الـ time slice دي
            while (index < n && processes.get(index).getArrivalTime() <= currentTime) {
                readyQueue.add(processes.get(index));
                index++;
            }

            // لو الـ process خلصت
            if (current.getRemainingTime() == 0) {
                completed++;
                current.setCompletionTime(currentTime);
                current.setTurnaroundTime(currentTime - current.getArrivalTime());
                current.setWaitingTime(current.getTurnaroundTime() - current.getBurstTime());
            } else {
                // لو لسه فاضل، رجّعها آخر الـ queue
                readyQueue.add(current);
            }
        }

        // ========== خطوة 3: رجّع النتايج للـ original processes ==========
        copyResultsBack(processes, originalProcesses);

        return gantt;
    }

    // ========== Helper Methods ==========

    // بتعمل نسخة جديدة من كل process عشان منغيرش الأصل
    private List<Process> copyProcesses(List<Process> original) {
        List<Process> copy = new ArrayList<>();
        for (Process p : original) {
            copy.add(new Process(p.getId(), p.getArrivalTime(), p.getBurstTime(), p.getPriority()));
        }
        return copy;
    }

    // بترجع النتايج للـ original list عشان نعرض المقاييس
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