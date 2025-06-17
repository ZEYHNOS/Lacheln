package aba3.lucid.image.service;

import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.image.ImageType;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.common.validate.Validator;
import aba3.lucid.config.ImageConfig;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.company.repository.CompanyRepository;
import aba3.lucid.domain.product.entity.ProductImageEntity;
import aba3.lucid.domain.product.repository.ProductImageRepository;
import aba3.lucid.domain.user.entity.UsersEntity;
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
public class ImageService {

    private final ImageConfig imageConfig;

    private final ProductImageRepository productImageRepository;
    private final CompanyRepository companyRepository;

        public String profileImageUpload(CompanyEntity company, MultipartFile profileImg, ImageType type) throws IOException {
            // 확장자, Content-Type 체크
            if (profileImg.getContentType() == null || !profileImg.getContentType().startsWith("image/")) {
                throw new ApiException(ErrorCode.BAD_REQUEST, "image가 아닙니다.");
            }

            // 디렉토리 생성
            Path path = Paths.get(imageConfig.getDir(), String.valueOf(company.getCpId()), type.getType());
            File fileDir = path.toFile();
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }

            // 파일 저장 및 경로 저장
            String originalFileName = profileImg.getOriginalFilename();
            if (originalFileName == null) {
                throw new ApiException(ErrorCode.BAD_REQUEST, "이미지 이름이 존재하지 않습니다.");
            }

            // 이름 중복 방지
            String uuid = UUID.randomUUID().toString();
            String savedName = "\\" + uuid + "_" + originalFileName;

            // 파일 저장
            File destination = new File(fileDir, savedName);
            profileImg.transferTo(destination);

            // 경로 반환 (DB에 저장할 값)
            String filePath = "\\" + company.getCpId() + "\\" + type.getType() + savedName;
            return filePath;
        }


    //프로필 이미지 삭제
    public void deleteProfileImage(String imagePath) {
        if(imagePath == null || imagePath.isEmpty())
            return;
        //만약 imagePath가 상대경로라면, 실제 저장경로로 변환 필요
        String baseDir = imageConfig.getDir();
        String absPath = baseDir + imagePath; //경로 조합
        File file = new File(absPath.replace("/", File.separator));
        if(file.exists()) {
            file.delete();
        }
    }


    // 파일 업로드
    public List<String> imagesUpload(CompanyEntity company, List<MultipartFile> images, ImageType type) throws IOException {
        // TODO 확장자 확인하기
        for (MultipartFile image : images) {
            if (image.getContentType() == null || !image.getContentType().startsWith("image/")) {
                throw new ApiException(ErrorCode.BAD_REQUEST, "image가 아닙니다.");
            }
        }

        // 해당 업체의 파일이 존재하지 않을 때
        Path path = Paths.get(imageConfig.getDir(), String.valueOf(company.getCpId()), type.getType());
        File fileDir = path.toFile();
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
            String savedName = "\\" + uuid + "_" + originalFileName;

            // 파일 저장
            File destination = new File(fileDir, savedName);
            image.transferTo(destination);
            
            // 파일 주소 저장
            filePathList.add("\\" + company.getCpId() + "\\" + type.getType() + savedName);
        }

        return filePathList;
    }

    // 유저 파일 업로드
    public List<String> imagesUpload(UsersEntity user, List<MultipartFile> images, ImageType type) throws IOException {
        // TODO 확장자 확인하기
        for (MultipartFile image : images) {
            if (image.getContentType() == null || !image.getContentType().startsWith("image/")) {
                throw new ApiException(ErrorCode.BAD_REQUEST, "image가 아닙니다.");
            }
        }

        // 해당 업체의 파일이 존재하지 않을 때
        Path path = Paths.get(imageConfig.getDir(), user.getUserId(), type.getType());
        File fileDir = path.toFile();
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
            String savedName = "\\" + uuid + "_" + originalFileName;

            // 파일 저장
            File destination = new File(fileDir, savedName);
            image.transferTo(destination);

            // 파일 주소 저장
            filePathList.add("\\" + user.getUserId() + "\\" + type.getType() + savedName);
        }

        return filePathList;
    }


    public void deleteProductImageByImageIdList(List<Long> productImageIdList, Long companyId) {
        // 이미지 Id 리스트가 없으면 return
        if (productImageIdList == null || productImageIdList.isEmpty()) {
            return;
        }

        List<ProductImageEntity> imageList = productImageRepository.findAllById(productImageIdList);
        // 요청하는 업체가 해당 이미지의 주인이 아닌 경우
        imageList.forEach(it -> {
                    if (it.getProduct().getCompany().getCpId() != companyId) {
                        throw new ApiException(ErrorCode.BAD_REQUEST);
                    } else {
                        deleteProductImageByImageId(it);
                    }
                });
    }

    public void deleteProductImageByImageId(ProductImageEntity productImage) {
        File file = new File(imageConfig.getDir() + productImage.getPdImageUrl());
        if (file.exists()) {
            boolean deleted = file.delete();
            if (!deleted) {
                log.warn("이미지 삭제 실패 URL : {}", productImage.getPdImageUrl());
            }

            productImageRepository.delete(productImage);
        } else {
            log.warn("이미 삭제된 이미지 : {}", productImage.getPdImageUrl());
        }
    }

    public void deleteProductImage(Long productId) {
        Validator.throwIfInvalidId(productId);

        // 1. DB에서 해당 productId에 연결된 이미지 경로 조회
        List<String> imagePaths = productImageRepository.findImageUrlsByProductId(productId);

        // 2. 실제 파일 시스템에서 이미지 삭제
        for (String path : imagePaths) {
            File file = new File(imageConfig.getDir() + path);
            if (file.exists()) {
                boolean deleted = file.delete();
                if (!deleted) {
                    log.warn("이미지 삭제 실패: {}", file.getAbsolutePath());
                }
            } else {
                log.warn("이미지를 찾을 수 없음: {}", file.getAbsolutePath());
            }
        }

        // 3. DB에서 이미지 메타데이터 삭제
        productImageRepository.deleteByProduct_PdId(productId);
    }


}
