package com.demo;
import java.util.List;
@Deprecated
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
        for (int i = 0; i < 5; i++) {
            System.out.println("Iteration: " + i);
        }

    }

}