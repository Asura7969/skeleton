package com.asura.skeleton.controller;

import com.asura.common.annotation.ResponseResult;
import com.asura.common.domain.Result;
import com.asura.open.api.IApiService;
import com.asura.open.api.mode.ReqBody;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

    @PostMapping("/getBody")
    public ReqBody getBody() {
        return iApiService.getBody(new ReqBody(1, "name")).getData();
    }


    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public class Bod {
        private Integer id;
        private String name;
    }

}
