package com.example.tasky;

public class Task {
    private String name, date, time;
    private int priority; // 0: High, 1: Medium, 2: Low

    public Task(String name, String date, String time, int priority) {
        this.name = name;
        this.date = date;
        this.time = time;
        this.priority = priority;
    }

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
    public int getPriority() { return priority; }
    public void setPriority(int priority) { this.priority = priority; }
}