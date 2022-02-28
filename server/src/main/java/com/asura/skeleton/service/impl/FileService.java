package com.asura.skeleton.service.impl;

import com.asura.common.domain.Result;
import com.asura.dao.datasource.first.dto.FileDTO;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author asura7969
 * @create 2022-02-28-19:31
 */
public interface FileService {
    FileDTO getFileById(String id);

    Result<Boolean> upLoadFiles(MultipartFile file);
}
