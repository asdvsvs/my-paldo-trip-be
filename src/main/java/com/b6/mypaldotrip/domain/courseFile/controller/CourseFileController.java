package com.b6.mypaldotrip.domain.courseFile.controller;

import com.b6.mypaldotrip.domain.courseFile.controller.dto.response.CourseFileAddRes;
import com.b6.mypaldotrip.domain.courseFile.controller.dto.response.CourseFileDeleteRes;
import com.b6.mypaldotrip.domain.courseFile.controller.dto.response.CourseFileListRes;
import com.b6.mypaldotrip.domain.courseFile.service.CourseFileService;
import com.b6.mypaldotrip.global.common.GlobalResultCode;
import com.b6.mypaldotrip.global.config.VersionConfig;
import com.b6.mypaldotrip.global.response.RestResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/${mpt.version}/courses/{courseId}/files")
public class CourseFileController {

    private final CourseFileService courseFileService;
    private final VersionConfig versionConfig;

    @PostMapping
    public ResponseEntity<RestResponse<CourseFileAddRes>> addFiles(
            @PathVariable Long courseId, @RequestPart MultipartFile multipartFile)
            throws IOException {
        CourseFileAddRes res = courseFileService.addFiles(courseId, multipartFile);

        return RestResponse.success(res, GlobalResultCode.CREATED, versionConfig.getVersion())
                .toResponseEntity();
    }

    @GetMapping
    public ResponseEntity<RestResponse<List<CourseFileListRes>>> getFileList(
            @PathVariable Long courseId) {
        List<CourseFileListRes> res = courseFileService.getFileList(courseId);

        return RestResponse.success(res, GlobalResultCode.SUCCESS, versionConfig.getVersion())
                .toResponseEntity();
    }

    @DeleteMapping("/{fileId}")
    public ResponseEntity<RestResponse<CourseFileDeleteRes>> deleteFile(
            @PathVariable Long courseId, @PathVariable Long fileId) {
        CourseFileDeleteRes res = courseFileService.deleteFile(courseId, fileId);

        return RestResponse.success(res, GlobalResultCode.SUCCESS, versionConfig.getVersion())
                .toResponseEntity();
    }
}
