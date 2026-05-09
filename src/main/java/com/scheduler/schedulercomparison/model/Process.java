package com.scheduler.schedulercomparison.model;

public class Process {

    // ========== (Inputs بتاعت ال process) ==========
    private String id;
    private int arrivalTime;
    private int burstTime;
    private final int priority;

    // ========== (Calculated حسابات العمليات) ==========
    private int remainingTime;
    private int completionTime;
    private int waitingTime;
    private int turnaroundTime;
    private int responseTime;
    private boolean started;

    public Process(String id, int arrivalTime, int burstTime, int priority) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
        this.remainingTime = burstTime;
        this.started = false;
    }

    public String getId()           { return id; }
    public int getArrivalTime()     { return arrivalTime; }
    public int getBurstTime()       { return burstTime; }
    public int getPriority()        { return priority; }
    public int getRemainingTime()   { return remainingTime; }
    public int getCompletionTime()  { return completionTime; }
    public int getWaitingTime()     { return waitingTime; }
    public int getTurnaroundTime()  { return turnaroundTime; }
    public int getResponseTime()    { return responseTime; }
    public boolean isStarted()      { return started; }

    public void setRemainingTime(int remainingTime)   { this.remainingTime = remainingTime; }
    public void setCompletionTime(int completionTime) { this.completionTime = completionTime; }
    public void setWaitingTime(int waitingTime)       { this.waitingTime = waitingTime; }
    public void setTurnaroundTime(int turnaroundTime) { this.turnaroundTime = turnaroundTime; }
    public void setResponseTime(int responseTime)     { this.responseTime = responseTime; }
    public void setStarted(boolean started)           { this.started = started; }

    @Override
    public String toString() {
        return id;
    }
}