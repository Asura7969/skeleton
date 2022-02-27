package com.asura.skeleton.pojo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author gongwenzhou
 */
@Data
public class UserEntity {

    private Long id;
    private String name;
    private Integer age;
    private String email;
    private LocalDateTime createTime;
}
