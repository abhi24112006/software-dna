package com.demo;

public class Teacher {

    private String subject;

    private int experience;

    public Teacher() {

    }

    public Teacher(String subject) {

        this.subject = subject;

    }

    public void teach() {

        System.out.println("Teaching...");

    }

}