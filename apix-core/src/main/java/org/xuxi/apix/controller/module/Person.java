package org.xuxi.apix.controller.module;

import org.xuxi.apix.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

public class Person {

    @NotBlank
    String id;

    @NotBlank(groups = Person.class)
    String name;

    @ApiModelProperty
    String age;

    @ApiModelProperty(groups = Person.class)
    String sex;

    Student student;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

}
