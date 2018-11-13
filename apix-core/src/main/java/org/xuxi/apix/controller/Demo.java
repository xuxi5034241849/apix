package org.xuxi.apix.controller;

import org.xuxi.apix.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

public class Demo {

    @ApiModelProperty(value = "姓名111111111", groups = test.class)
    String name;

    @ApiModelProperty("姓名")
    @NotBlank(groups = test.class)
    String age;

    @NotBlank(groups = test.class)
    String sex;

    String id;

    Demo1 demo1;



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

    public Demo1 getDemo1() {
        return demo1;
    }

    public void setDemo1(Demo1 demo1) {
        this.demo1 = demo1;
    }

}
