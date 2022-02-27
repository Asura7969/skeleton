package com.asura.dao.datasource.first.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDateTime;

/**
 * @author asura7969
 * @create 2022-02-26-22:56
 */
@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@TableName("user")
public class UserDTO {

    @TableId(value = "id", type = IdType.NONE)
    private Long id;
    @TableField("name")
    private String name;
    private Integer age;
    private String email;
    private LocalDateTime createTime;
}
