package com.demo;

import java.util.List;

public class Student {

    private String name;

    private int age;

    public Student() {

    }

    public Student(String name, int age) {

        this.name = name;
        this.age = age;

    }

    public void study() {

        System.out.println("Studying...");

    }

}