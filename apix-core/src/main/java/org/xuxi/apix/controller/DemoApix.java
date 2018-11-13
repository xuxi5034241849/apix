package org.xuxi.apix.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.xuxi.apix.annotations.*;

import javax.validation.Valid;

@Api("演示API管理接口")
@RestController
public class DemoApix {


    @GetMapping("/")
    public String index() {

        return "测试";
    }

    @ApiOperation("我是测试接口")
    @RequestMapping(method = {RequestMethod.POST, RequestMethod.GET}, path = "xxxx")
    public String login(@RequestBody Demo demo , @ApiParam("我是s") String ssssss) {

        System.out.println(demo);

        return "测试";
    }


    @ApiIgnore
    @GetMapping("/ignoreTest")
    public String indexIgnoreTest(Demo demo) {

        return "忽略测试";
    }

}
