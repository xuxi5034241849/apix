package org.xuxi.apix.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.xuxi.apix.annotations.Api;
import org.xuxi.apix.annotations.ApiOperation;
import org.xuxi.apix.annotations.ApiParam;
import org.xuxi.apix.controller.module.Person;

import javax.validation.Valid;

@Api("演示API管理接口")  // 只描述Controller 信息
@RestController
public class DemoApix {


    /**
     * 演示接口1
     *
     * @param person
     * @return
     */
    @ApiOperation(value = "演示接口1") // 只描述API信息
    @PostMapping("index")
    private Person index(@Validated(Person.class) @RequestBody Person person, @ApiParam @RequestParam(required = false) String name) { // @ApiParam 会检查RequestParams ，会检查RequestParams优先级较高


        return person;
    }

}
