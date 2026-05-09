package com.scheduler.schedulercomparison.gui;

import javafx.beans.property.SimpleStringProperty;

public class ProcessRow {

    private final SimpleStringProperty id;
    private final SimpleStringProperty arrivalTime;
    private final SimpleStringProperty burstTime;
    private final SimpleStringProperty priority;

    private final SimpleStringProperty completionTime;
    private final SimpleStringProperty turnaroundTime;
    private final SimpleStringProperty waitingTime;
    private final SimpleStringProperty responseTime;

    public ProcessRow(String id, String arrivalTime, String burstTime, String priority) {
        this.id            = new SimpleStringProperty(id);
        this.arrivalTime   = new SimpleStringProperty(arrivalTime);
        this.burstTime     = new SimpleStringProperty(burstTime);
        this.priority      = new SimpleStringProperty(priority);
        this.completionTime = new SimpleStringProperty("-");
        this.turnaroundTime = new SimpleStringProperty("-");
        this.waitingTime    = new SimpleStringProperty("-");
        this.responseTime   = new SimpleStringProperty("-");
    }

    public String getId()             { return id.get(); }
    public String getArrivalTime()    { return arrivalTime.get(); }
    public String getBurstTime()      { return burstTime.get(); }
    public String getPriority()       { return priority.get(); }
    public String getCompletionTime() { return completionTime.get(); }
    public String getTurnaroundTime() { return turnaroundTime.get(); }
    public String getWaitingTime()    { return waitingTime.get(); }
    public String getResponseTime()   { return responseTime.get(); }

    public void setId(String v)             { id.set(v); }
    public void setArrivalTime(String v)    { arrivalTime.set(v); }
    public void setBurstTime(String v)      { burstTime.set(v); }
    public void setPriority(String v)       { priority.set(v); }
    public void setCompletionTime(String v) { completionTime.set(v); }
    public void setTurnaroundTime(String v) { turnaroundTime.set(v); }
    public void setWaitingTime(String v)    { waitingTime.set(v); }
    public void setResponseTime(String v)   { responseTime.set(v); }

    public SimpleStringProperty idProperty()             { return id; }
    public SimpleStringProperty arrivalTimeProperty()    { return arrivalTime; }
    public SimpleStringProperty burstTimeProperty()      { return burstTime; }
    public SimpleStringProperty priorityProperty()       { return priority; }
    public SimpleStringProperty completionTimeProperty() { return completionTime; }
    public SimpleStringProperty turnaroundTimeProperty() { return turnaroundTime; }
    public SimpleStringProperty waitingTimeProperty()    { return waitingTime; }
    public SimpleStringProperty responseTimeProperty()   { return responseTime; }
}