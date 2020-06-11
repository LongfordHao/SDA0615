package com.tfjy.sda.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.tfjy.sda.Jurisdiction;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@DefaultProperties(defaultFallback = "fallbackMethod")
public class ChaoxingController {

//    @Resource
//    private JurisdictionFeignService jurisdictionFeignService;
//
//    @GetMapping("/test")
//    @ResponseBody
//    public String test(){
//        Jurisdiction jurisdiction = new Jurisdiction();
//        jurisdiction.setPassword("13483607023");
//        jurisdiction.setUserCode("13483607023");
//        String login = jurisdictionFeignService.login(jurisdiction);
//
//        System.out.println("["+login+"]");
//        JSONObject jsonObject = JSONObject.parseObject(login);
//        String data = jsonObject.getString("data");
//        JSONArray parse = (JSONArray) JSONArray.parseArray("["+data+"]");
//
//        for (Object json : parse) {
//            JSONObject js= (JSONObject) json;
//            System.out.println("id ;"+js.get("id"));
//            System.out.println("username: "+js.get("userName"));
//            System.out.println("token: "+js.get("token"));
//        }
//        return jurisdictionFeignService.login(jurisdiction);
//    }
}
