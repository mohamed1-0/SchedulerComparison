package com.scheduler.schedulercomparison.model;

/**
 * كل object من دي بيمثل خانة واحدة في الـ Gantt Chart
 * مثال: P1 شغال من الثانية 0 لحد الثانية 3
 */
public class GanttEntry {

    private String processId;  // اسم الـ Process زي P1
    private int startTime;     // بدأت امتى؟
    private int endTime;       // خلصت امتى؟

    // Constructor
    public GanttEntry(String processId, int startTime, int endTime) {
        this.processId = processId;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // Getters
    public String getProcessId() { return processId; }
    public int getStartTime()    { return startTime; }
    public int getEndTime()      { return endTime; }

    @Override
    public String toString() {
        return processId + " [" + startTime + " → " + endTime + "]";
    }
}