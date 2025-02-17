package aba3.lucid.social;

import aba3.lucid.social.enums.SNS;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "social")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SocialEntity {

    @Id
    private long cpId;

    // SNS 이름 및 이미지 주소
    private SNS snsName;

    // 업체 다이렉트 사이트 주소
    private String urlAddress;

}
