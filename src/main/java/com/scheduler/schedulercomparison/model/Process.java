package com.scheduler.schedulercomparison.model;

/**
 * الـ Process class بتمثل أي process بتدخل الـ scheduler
 * زي ما كل موظف عنده بيانات، كل process عندها بيانات
 */
public class Process {

    // ========== البيانات الأصلية (Input) ==========
    private String id;          // اسم الـ process زي P1, P2
    private int arrivalTime;    // وصلت امتى؟
    private int burstTime;      // محتاجة قد إيه وقت CPU؟
    private int priority;       // أولويتها (رقم أصغر = أولوية أعلى)

    // ========== بيانات الحساب (Calculated) ==========
    private int remainingTime;  // الوقت اللي فاضل ليها (بيتغير أثناء التشغيل)
    private int completionTime; // خلصت امتى؟
    private int waitingTime;    // استنت قد إيه؟
    private int turnaroundTime; // من ما وصلت لحد ما خلصت
    private int responseTime;   // أول مرة اتخدمت امتى؟
    private boolean started;    // اتخدمت قبل كده؟

    // ========== Constructor ==========
    public Process(String id, int arrivalTime, int burstTime, int priority) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
        this.remainingTime = burstTime; // في الأول الوقت الفاضل = الـ burstTime كله
        this.started = false;
    }

    // ========== Getters ==========
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

    // ========== Setters ==========
    public void setRemainingTime(int remainingTime)   { this.remainingTime = remainingTime; }
    public void setCompletionTime(int completionTime) { this.completionTime = completionTime; }
    public void setWaitingTime(int waitingTime)       { this.waitingTime = waitingTime; }
    public void setTurnaroundTime(int turnaroundTime) { this.turnaroundTime = turnaroundTime; }
    public void setResponseTime(int responseTime)     { this.responseTime = responseTime; }
    public void setStarted(boolean started)           { this.started = started; }

    /**
     * عشان لما نطبع الـ Process يظهر اسمها بدل عنوان الـ memory
     */
    @Override
    public String toString() {
        return id;
    }
}