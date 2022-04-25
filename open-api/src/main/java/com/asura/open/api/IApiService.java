package com.asura.open.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author asura7969
 * @create 2022-04-25-20:11
 */
@FeignClient(value = "api-service", url = "localhost:8080", path = "openApi")
public interface IApiService {

    @GetMapping("test")
    String testApi();

}
