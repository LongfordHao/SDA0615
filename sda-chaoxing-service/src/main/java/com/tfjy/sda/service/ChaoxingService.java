package com.tfjy.sda.service;/*
 * @ClassName: ChaoxingService
 * @description TODO
 * @Version:V1.0
 * @author: 张兴军
 * @date: 2020/5/31 15:02
 */

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "CHAOXING-SERVICE")
public interface ChaoxingService {

}
