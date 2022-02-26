package com.asura.skeleton.controller;

import com.asura.common.annotation.ResponseResult;
import com.asura.common.domain.Result;
import com.asura.common.exception.BizException;
import com.asura.skeleton.pojo.UserEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author gongwenzhou
 */
@ResponseResult
@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping(value="/getResult")
    public Result getResult(){
        return Result.suc("test");
    }

    @GetMapping(value="/getResult2")
    public UserEntity getResult2(){
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setName("zhangsan");
        return user;
    }

    @GetMapping("/fail")
    public Integer error() {
        // 查询结果数
        int res = 0;
        if( res == 0 ) {
            throw new BizException("没有数据");
        }
        return res;
    }


}
