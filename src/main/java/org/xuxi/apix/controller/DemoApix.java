package org.xuxi.apix.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.xuxi.apix.annotations.ApiIgnore;
import org.xuxi.apix.annotations.ApiOperation;

@RestController
public class DemoApix {


    @GetMapping("/")
    public String index() {

        return "测试";
    }


    @ApiOperation("我是测试接口")
    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, path = "xxxx")
    public String login(Demo demo) {

        return "测试";
    }


    @ApiIgnore
    @GetMapping("/ignoreTest")
    public String indexIgnoreTest(Demo demo) {

        return "忽略测试";
    }

}
