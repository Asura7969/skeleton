package com.asura.skeleton.controller;

import com.asura.common.domain.Result;
import com.asura.common.domain.ResultCode;
import com.asura.dao.datasource.first.dto.FileDTO;
import com.asura.skeleton.service.impl.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import static com.asura.common.utils.CommonUtils.getFileInputStream;

/**
 * @author asura7969
 * @create 2022-02-28-19:28
 */
@Slf4j
@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private FileService fileService;


    /**
     * 文件上传
     */
    @PostMapping(value = "/upload")
    public Result<Boolean> upLoadFiles(MultipartFile multipartFile){

        if (multipartFile.isEmpty()){
            return new Result<>(ResultCode.FILE_EMPTY.code(),ResultCode.FILE_EMPTY.message(),null);
        }

        return fileService.upLoadFiles(multipartFile);
    }

    /**
     * 文件下载
     * @param id 文件Id
     */
    @GetMapping(value = "/download/{id}")
    public void downloadFiles(@PathVariable("id") String id, HttpServletRequest request, HttpServletResponse response){
        OutputStream outputStream = null;

        FileDTO fileDTO = fileService.getFileById(id);
        String fileName = fileDTO.getFileName();

        InputStream inputStream = getFileInputStream(fileDTO.getFilePath());

        response.setHeader(
                "Content-Disposition",
                "attachment;filename=" + new String(fileName.getBytes(StandardCharsets.UTF_8),
                        StandardCharsets.ISO_8859_1));
        try {
            outputStream = response.getOutputStream();
        } catch (IOException e) {
            log.error("", e);
        } finally {
            try {
                if (inputStream != null){
                    inputStream.close();
                }
                if (outputStream != null){
                    outputStream.close();
                }
            } catch (IOException e) {
                log.error("", e);
            }
        }
    }

}
