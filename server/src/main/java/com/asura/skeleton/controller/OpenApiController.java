package com.asura.skeleton.controller;

import com.asura.common.annotation.ResponseResult;
import com.asura.open.api.mode.ReqBody;
import org.springframework.web.bind.annotation.*;

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


    @PostMapping("/getBody")
    public ReqBody getBody(@RequestBody(required = false) ReqBody reqBody) {
        reqBody.setId(reqBody.getId() + 1);
        reqBody.setName(reqBody.getName() + "-server");
        System.out.println(reqBody);
        return reqBody;
    }

}
