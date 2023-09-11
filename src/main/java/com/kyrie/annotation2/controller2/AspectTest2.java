package com.kyrie.annotation2.controller2;

import com.kyrie.annotation2.SystemControllerLog;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AspectTest2 {

    @SystemControllerLog("测试-根据id查用户")
    @GetMapping("/getById2")
    public String getById2() {
        return "执行了测试方法";
    }
}
