package org.xuxi.apix.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.xuxi.apix.annotations.ApiIgnore;
import org.xuxi.apix.annotations.ApiOperation;
import org.xuxi.apix.annotations.ApiParam;

import javax.validation.Valid;

@RestController
public class DemoApix {


    @GetMapping("/")
    public String index() {

        return "测试";
    }


    @ApiOperation("我是测试接口")
    @RequestMapping(method = {RequestMethod.POST}, path = "xxxx")
    public String login(@Validated(test.class) @RequestBody Demo demo , @ApiParam("我是s") String ssssss) {

        System.out.println(demo);

        return "测试";
    }


    @ApiIgnore
    @GetMapping("/ignoreTest")
    public String indexIgnoreTest(Demo demo) {

        return "忽略测试";
    }

}
