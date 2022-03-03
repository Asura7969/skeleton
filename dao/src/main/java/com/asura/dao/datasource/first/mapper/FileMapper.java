package com.asura.dao.datasource.first.mapper;

import com.asura.dao.datasource.first.dto.FileDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author asura7969
 * @create 2022-02-28-21:12
 */
@Mapper
@Repository
public interface FileMapper {

    /**
     * 将数据信息插入到数据库
     * @param fileDTO
     */
    void insertFile(FileDTO fileDTO);

    /**
     * 根据id获取文件
     * @param id 文件id
     * @return
     */
    FileDTO selectFileById(String id);
}
