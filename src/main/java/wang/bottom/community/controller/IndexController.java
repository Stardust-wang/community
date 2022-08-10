package wang.bottom.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller     // 主要用于Spring容器识别这是一个前端控制页面，用于页面跳转等
public class IndexController {
    @GetMapping("/")
    public String hello(){
        return "index";	 // 默认将跳转到resources/templates/index.html
    }
}
