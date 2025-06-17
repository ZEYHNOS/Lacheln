package aba3.lucid.image;

import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.image.ImageType;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.config.ImageConfig;
import aba3.lucid.domain.review.entity.ReviewEntity;
import aba3.lucid.domain.user.entity.UsersEntity;
import aba3.lucid.review.service.ReviewService;
import aba3.lucid.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserImageService {

    private final ImageConfig imageConfig;
    private final UserService userService;
    private final ReviewService reviewService;

    public List<String> reviewImageUpload(List<MultipartFile> imageList, Long reviewId, String userId, ImageType type) throws IOException {
        UsersEntity user = userService.findByIdWithThrow(userId);
        ReviewEntity review = reviewService.findByIdWithThrow(reviewId);

        for (MultipartFile image : imageList) {
            if (image.getContentType() == null || !image.getContentType().startsWith("image/")) {
                throw new ApiException(ErrorCode.BAD_REQUEST, "image 아닙니다.");
            }
        }

        log.info("imageConfig.getDir() : {}", imageConfig.getDir());
        log.info("user.getUserId() : {}", user.getUserId());
        log.info("type.getType() : {}", type.getType());

        Path path = Paths.get(imageConfig.getDir(), String.valueOf(user.getUserId()), type.getType());
        File fileDir = path.toFile();
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }

        List<String> filePathList = new ArrayList<>();
        for (MultipartFile image : imageList) {
            String originalName = image.getOriginalFilename();
            if (originalName == null) {
                throw new ApiException(ErrorCode.BAD_REQUEST, "이미지 이름 존재 X");
            }
            String uuid = UUID.randomUUID().toString();
            String savedName = "/" + uuid + "_" + originalName;

            File destination = new File(fileDir, savedName);
            image.transferTo(destination);

            filePathList.add("/" + user.getUserId() + "/" + type.getType() + savedName);
        }

        return filePathList;
    }

    public String profileImageUpload(MultipartFile image, String userId, ImageType type) throws IOException {
        UsersEntity user = userService.findByIdWithThrow(userId);

        if (image.getContentType() == null || !image.getContentType().startsWith("image/")) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "image 아닙니다.");
        }

        Path path = Paths.get(imageConfig.getDir(), String.valueOf(user.getUserId()), type.getType());
        File fileDir = path.toFile();
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }

        String originalName = image.getOriginalFilename();
        if (originalName == null) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "이미지 이름 존재 X");
        }
        String uuid = UUID.randomUUID().toString();
        String savedName = "/" + uuid + "_" + originalName;

        File destination = new File(fileDir, savedName);
        image.transferTo(destination);

        return "/" + user.getUserId() + "/" + type.getType() + savedName;
    }
}
