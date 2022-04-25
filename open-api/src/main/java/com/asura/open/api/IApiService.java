package com.asura.open.api;

import com.asura.common.domain.Result;
import com.asura.open.api.mode.ReqBody;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author asura7969
 * @create 2022-04-25-20:11
 */
@FeignClient(value = "api-service", url = "localhost:8080", path = "openApi")
public interface IApiService {

    @GetMapping("test")
    String testApi();

    @PostMapping("getBody")
    Result<ReqBody> getBody(@RequestBody(required = false) ReqBody req);
}
