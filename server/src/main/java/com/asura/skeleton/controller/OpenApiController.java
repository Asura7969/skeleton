package com.asura.skeleton.controller;

import com.asura.common.annotation.ResponseResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author asura7969
 * @create 2022-04-25-20:25
 */
@ResponseResult
@RestController
@RequestMapping("/openApi")
public class OpenApiController {

    @GetMapping("/test")
    public String testApi() {
        return "OpenApiController - testApi";
    }
}
