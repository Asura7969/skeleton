package com.asura.dao.datasource.second.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author asura7969
 * @create 2022-02-27-12:40
 */
@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@TableName("event_info")
public class EventDTO {

    @TableId(value = "id", type = IdType.NONE)
    private Long id;
    @TableField("event_name")
    private String name;
    private Integer eventType;
    private LocalDateTime createTime;
}
