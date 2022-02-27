package com.asura.skeleton.dao;

import com.asura.dao.datasource.first.dto.UserDTO;
import com.asura.dao.datasource.first.mapper.UserMapper;
import com.asura.dao.datasource.second.dto.EventDTO;
import com.asura.dao.datasource.second.mapper.EventMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author asura7969
 * @create 2022-02-26-23:11
 *
 * {@link https://juejin.cn/post/6961721367846715428}
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MapperTest {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private EventMapper eventMapper;


    @Test
    public void testSelect() {
        List<UserDTO> list = userMapper.selectList(null);
        assertEquals(6, list.size());
        list.forEach(System.out::println);

        final List<EventDTO> eventDTOS = eventMapper.selectList(null);
        assertEquals(5, eventDTOS.size());
        eventDTOS.forEach(System.out::println);
    }

    @Test
    public void insert() {
        UserDTO user = userMapper.selectById(6L);
        if (null != user) {
            userMapper.deleteById(6L);
        }
        Date date = new Date(1645934448000L);
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(date.toInstant(), zoneId);
        UserDTO userDTO = new UserDTO();
        userDTO.setId(6L);
        userDTO.setName("gg");
        userDTO.setAge(27);
        userDTO.setEmail("110@qq.com");
        userDTO.setCreateTime(localDateTime);
        userMapper.insert(userDTO);

        user = userMapper.selectById(6L);
        assertEquals(userDTO, user);
    }
}
