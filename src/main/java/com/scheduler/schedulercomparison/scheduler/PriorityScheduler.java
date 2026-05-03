package com.scheduler.schedulercomparison.scheduler;

import com.scheduler.schedulercomparison.model.GanttEntry;
import com.scheduler.schedulercomparison.model.Process;

import java.util.ArrayList;
import java.util.List;

/**
 * Preemptive Priority Scheduler
 * في كل لحظة، الـ process اللي أولويتها أعلى (رقم أصغر) هي اللي بتشتغل
 * لو جه process أولويتها أعلى من اللي شغال → يوقفه ويشتغل هو (Preemption)
 */
public class PriorityScheduler {

    /**
     * الميثود الرئيسية اللي بتشغل الخوارزمية
     */
    public List<GanttEntry> schedule(List<Process> originalProcesses) {

        // ========== خطوة 1: عمل نسخة من الـ processes ==========
        List<Process> processes = copyProcesses(originalProcesses);

        List<GanttEntry> gantt = new ArrayList<>();

        int currentTime = 0;
        int completed = 0;
        int n = processes.size();

        // ترتيب حسب الـ Arrival Time
        processes.sort((a, b) -> a.getArrivalTime() - b.getArrivalTime());

        Process lastProcess = null; // آخر process اشتغلت
        int sliceStart = 0;        // بداية الـ slice الحالية

        // ========== خطوة 2: الـ Main Loop ==========
        while (completed < n) {

            // اختار الـ process ذات أعلى أولوية من اللي وصلت لحد دلوقتي
            Process current = getHighestPriority(processes, currentTime);

            // لو مفيش process وصلت لسه
            if (current == null) {
                currentTime++;
                continue;
            }

            // لو الـ process اتغيرت → سجل الـ Gantt Entry للـ process السابقة
            if (lastProcess != null && !lastProcess.getId().equals(current.getId())) {
                gantt.add(new GanttEntry(lastProcess.getId(), sliceStart, currentTime));
                sliceStart = currentTime;
            } else if (lastProcess == null) {
                sliceStart = currentTime;
            }

            // سجل الـ Response Time لو أول مرة بتتخدم
            if (!current.isStarted()) {
                current.setResponseTime(currentTime - current.getArrivalTime());
                current.setStarted(true);
            }

            // شغّل الـ process لمدة وحدة زمن واحدة
            current.setRemainingTime(current.getRemainingTime() - 1);
            currentTime++;

            // لو الـ process خلصت
            if (current.getRemainingTime() == 0) {
                completed++;
                current.setCompletionTime(currentTime);
                current.setTurnaroundTime(currentTime - current.getArrivalTime());
                current.setWaitingTime(current.getTurnaroundTime() - current.getBurstTime());

                // سجل آخر Gantt Entry ليها
                gantt.add(new GanttEntry(current.getId(), sliceStart, currentTime));
                sliceStart = currentTime;
                lastProcess = null;
            } else {
                lastProcess = current;
            }
        }

        // ========== خطوة 3: رجّع النتايج ==========
        copyResultsBack(processes, originalProcesses);

        return gantt;
    }

    /**
     * بتجيب الـ process ذات أعلى أولوية من اللي وصلت
     * رقم أصغر = أولوية أعلى
     * لو تساوت الأولوية → اللي وصلت أول هي الأحق (Arrival Time)
     */
    private Process getHighestPriority(List<Process> processes, int currentTime) {
        Process best = null;

        for (Process p : processes) {
            // الـ process لازم تكون وصلت ولسه مخلصتش
            if (p.getArrivalTime() <= currentTime && p.getRemainingTime() > 0) {
                if (best == null) {
                    best = p;
                } else if (p.getPriority() < best.getPriority()) {
                    // أولوية أعلى (رقم أصغر)
                    best = p;
                } else if (p.getPriority() == best.getPriority()) {
                    // تساوي في الأولوية → اللي وصلت أول
                    if (p.getArrivalTime() < best.getArrivalTime()) {
                        best = p;
                    }
                }
            }
        }
        return best;
    }

    // ========== Helper Methods ==========
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