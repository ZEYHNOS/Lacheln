package aba3.lucid.domain.inquiry;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "inquiry_image")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InquiryImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inquiryImageId;

    //문의ID
    private Long InquiryId;

    //이미지URL
    private String imageUrl;
}
