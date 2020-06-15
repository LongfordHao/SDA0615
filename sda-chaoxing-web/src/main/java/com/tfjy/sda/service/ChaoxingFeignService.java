package com.tfjy.sda.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

/*
 * @ClassName: chaoxingFeignService
 * @description TODO
 * @Version:V1.0
 * @author: 张兴军
 * @date: 2020/6/13 11:14
 */
@Component
@FeignClient(value = "CHAOXING-SERVICE" ,fallback = ChaoxingFeignFallback.class)
public interface ChaoxingFeignService {
    @GetMapping("/chaoxing/home")
    public String homePage();
}
