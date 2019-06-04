package com.example.broasdcastdemo;

import java.io.Serializable;

public class Person implements Serializable {

    private static final long serialVersionUID = -7060210544600464481L;
    private String name;

    public Person(String name) {
        this.name = name;
    }

    public Person() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
