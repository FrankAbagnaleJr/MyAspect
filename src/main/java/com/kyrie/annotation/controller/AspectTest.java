package com.kyrie.annotation.controller;

import com.kyrie.annotation.SystemServiceLog;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AspectTest {

    @SystemServiceLog("测试-根据id查用户")
    @GetMapping("/getById")
    public String getById() {
        return "执行了测试方法";
    }
}
