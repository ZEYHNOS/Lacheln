package aba3.lucid.image.controller;

import aba3.lucid.common.api.API;
import aba3.lucid.common.auth.CustomAuthenticationToken;
import aba3.lucid.image.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/company/image")
public class ImageController {

    private final ImageService imageService;

    @DeleteMapping("/delete")
    public API<String> deleteImages(
            @AuthenticationPrincipal CustomAuthenticationToken customAuthenticationToken,
            @RequestBody List<Long> imageIdList
    ) {
//        imageService.deleteProductImageByImageIdList(imageIdList, customAuthenticationToken.getCompanyId());
        imageService.deleteProductImageByImageIdList(imageIdList, 1L);

        return API.OK("삭제 성공");
    }

}
