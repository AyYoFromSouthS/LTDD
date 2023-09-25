package com.example.test.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TaskModel {
    private String task;
    private int done;
    private int impo;

    public TaskModel(String task, int done, int impo, Date time, int id) {
        this.task = task;
        this.done = done;
        this.impo = impo;
        this.time = time;
        this.id = id;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    private Date time;

    public int getId() {
        return id;
    }
    static int count = 0;

    public void setId(int id) {
        this.id = id;
    }

    private int id;
    {
        count++;
    }
    public int getDone() {
        return done;
    }

    public void setDone(int done) {
        this.done = done;
    }

    public int getImpo() {
        return impo;
    }

    public void setImpo(int impo) {
        this.impo = impo;
    }
    public TaskModel(String task, Date time)
    {
        setId(count);
        setTask(task);
        setImpo(0);
        setDone(0);
        setTime(time);
    }

    public TaskModel(){}
    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("task", task);
        result.put("done", done);
        result.put("impo", impo);
        result.put("time", time);
        return result;
    }
}