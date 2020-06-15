package com.tfjy.sda.controller;



import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.tfjy.sda.service.ChaoxingFeignService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;


/*
 * @ClassName: ChaoxingController
 * @description TODO
 * @Version:V1.0
 * @author: 张兴军
 * @date: 2020/6/1 14:43
 */
@RestController
@RequestMapping("/consume")
@DefaultProperties(defaultFallback = "fallbackMethod")
public class ChaoxingController {
   @Resource
    private ChaoxingFeignService chaoxingFeignService;

    @GetMapping("/home")
    public String homePage(){
      return   chaoxingFeignService.homePage();
    }
}
