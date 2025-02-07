package aba3.lucid.hashtag.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HashTagDto {
    // hashTag와 tag를 복합키로 사용
    // 복합키로 추출된 정보를 dto 클래스 안에 담아놓을것
    private String hashTag;
    private Long tag;
}
