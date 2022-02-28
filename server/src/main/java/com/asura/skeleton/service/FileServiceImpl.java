package com.asura.skeleton.service;

import com.asura.common.domain.Result;
import com.asura.common.domain.ResultCode;
import com.asura.dao.datasource.first.dto.FileDTO;
import com.asura.skeleton.service.impl.FileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

import static com.asura.common.utils.CommonUtils.uuid;

/**
 * @author asura7969
 * @create 2022-02-28-19:31
 */
@Service
@Slf4j
public class FileServiceImpl implements FileService {

    @Value("${file.path}")
    private String savePath;
    // 2M
    private static final Integer FILE_MAX_SIZE = 1024 * 1024 * 2;
    @Override
    public FileDTO getFileById(String id) {
        return null;
    }

    @Override
    public Result<Boolean> upLoadFiles(MultipartFile file) {

        String fileName = file.getOriginalFilename();
        if (StringUtils.isEmpty(fileName)){
            return new Result<>(ResultCode.FILE_NAME_EMPTY.code(),ResultCode.FILE_NAME_EMPTY.message(),null);
        }
        //如果文件超过最大值，返回超出可上传最大值的错误
        if (file.getSize() > FILE_MAX_SIZE){
            return new Result<>(ResultCode.FILE_MAX_SIZE.code(),ResultCode.FILE_MAX_SIZE.message(),null);
        }
        String suffixName = fileName.contains(".") ? fileName.substring(fileName.lastIndexOf(".")) : null;

        String newName = uuid() + suffixName;
        File newFile = new File(savePath,newName);
        if (!newFile.getParentFile().exists()){
            newFile.getParentFile().mkdirs();
        }
        try {
            //文件写入
            file.transferTo(newFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //将这些文件的信息写入到数据库中
        FileDTO fileDTO = new FileDTO(newFile.getPath(), fileName, suffixName);
        // fileMapper.insertFile(files);
        return new Result<>(ResultCode.SUCCESS.code(),ResultCode.SUCCESS.message(),true);
    }
}
