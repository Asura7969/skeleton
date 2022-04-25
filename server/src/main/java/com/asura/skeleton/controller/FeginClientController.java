package com.asura.skeleton.controller;

import com.asura.common.annotation.ResponseResult;
import com.asura.open.api.IApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author asura7969
 * @create 2022-04-25-20:27
 */
@ResponseResult
@RestController
@RequestMapping("/fegin")
public class FeginClientController {

    @Autowired
    private IApiService iApiService;

    @GetMapping("/client")
    public String clientTest() {
        return iApiService.testApi();
    }

}
