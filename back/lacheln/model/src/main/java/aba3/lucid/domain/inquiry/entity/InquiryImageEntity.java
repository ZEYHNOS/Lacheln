package aba3.lucid.domain.inquiry.entity;

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
    private long inquiryImageId;

    @ManyToOne(fetch = FetchType.LAZY)
    private InquiryEntity inquiry;

    @Column(name = "image_url", length = 255, nullable = false)
    private String imageUrl; //이미지URL
}
