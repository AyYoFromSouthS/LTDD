package com.example.test.model;

public class StepModel {
    private static int id = 0;

    public static int getId() {
        return id;
    }

    public static void setId(int id) {
        StepModel.id = id;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    private String step;
    {
        id++;
    }
    public StepModel(String step) {
        this.step = step;
    }
}
