package com.tfjy.sda.service;

import org.springframework.stereotype.Component;

/*
 * @ClassName: ChaoxingFeignFallback
 * @description TODO
 * @Version:V1.0
 * @author: 张兴军
 * @date: 2020/6/13 11:48
 */

@Component
public class ChaoxingFeignFallback implements ChaoxingFeignService {
    @Override
    public String homePage() {
        return"该接口不可使用，请稍后重试！";
    }
}
