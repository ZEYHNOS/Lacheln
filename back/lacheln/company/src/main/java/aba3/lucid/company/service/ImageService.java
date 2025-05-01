package aba3.lucid.company.service;

import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.image.ImageType;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.config.ImageConfig;
import aba3.lucid.domain.company.entity.CompanyEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageConfig imageConfig;

    // 파일 업로드
    public List<String> imagesUpload(CompanyEntity company, List<MultipartFile> images, ImageType type) throws IOException {
        // TODO 확장자 확인하기
        for (MultipartFile image : images) {
            if (image.getContentType() == null || !image.getContentType().startsWith("image/")) {
                throw new ApiException(ErrorCode.BAD_REQUEST, "image가 아닙니다.");
            }
        }

        // 해당 업체의 파일이 존재하지 않을 때
        String dir = imageConfig.getDir() + "\\" + company.getCpName() + "\\" + type.getType();
        File fileDir = new File(dir);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }

        // 파일 저장하고 경로 저장하기
        List<String> filePathList = new ArrayList<>();
        for (MultipartFile image : images) {
            String originalFileName = image.getOriginalFilename();
            if (originalFileName == null) {
                throw new ApiException(ErrorCode.BAD_REQUEST, "이미지 이름이 존재하지 않습니다.");
            }

            // 이름 중복 방지
            String uuid = UUID.randomUUID().toString();
            String savedName = uuid + "_" + originalFileName;

            // 파일 저장
            File destination = new File(fileDir, savedName);
            image.transferTo(destination);
            
            // 파일 주소 저장
            filePathList.add(destination.getAbsolutePath());
        }

        return filePathList;
    }

}
