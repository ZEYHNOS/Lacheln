import React, { useState, useRef, useEffect } from 'react';
import Addphoto from '../../../image/Company/addimage.png';
import apiClient from '../../../lib/apiClient';
import { useNavigate } from 'react-router-dom';

const baseUrl = import.meta.env.VITE_API_BASE_URL;

// 별 아이콘 SVG (꽉찬, 반, 빈)
const Star = ({ type = 'empty', ...props }) => {
    const ppColor = 'var(--pp)';
    if (type === 'full') {
        return (
            <svg {...props} viewBox="0 0 24 24" fill={ppColor} stroke={ppColor} width="32" height="32"><path d="M12 17.27L18.18 21l-1.64-7.03L22 9.24l-7.19-.61L12 2 9.19 8.63 2 9.24l5.46 4.73L5.82 21z"/></svg>
        );
    } else if (type === 'half') {
        return (
            <svg {...props} viewBox="0 0 24 24" width="32" height="32"><defs><linearGradient id="half"><stop offset="50%" stopColor={ppColor}/><stop offset="50%" stopColor="#E0E0E0"/></linearGradient></defs><path d="M12 17.27L18.18 21l-1.64-7.03L22 9.24l-7.19-.61L12 2 9.19 8.63 2 9.24l5.46 4.73L5.82 21z" fill="url(#half)" stroke={ppColor}/></svg>
        );
    } else {
        return (
            <svg {...props} viewBox="0 0 24 24" fill="#E0E0E0" stroke={ppColor} width="32" height="32"><path d="M12 17.27L18.18 21l-1.64-7.03L22 9.24l-7.19-.61L12 2 9.19 8.63 2 9.24l5.46 4.73L5.82 21z"/></svg>
        );
    }
};

const Review = (props) => {
    const [images, setImages] = useState([]);
    const [selectedImage, setSelectedImage] = useState(null);
    const [isDragging, setIsDragging] = useState(false);
    const [reviewText, setReviewText] = useState('');
    const [rating, setRating] = useState(0);
    const [hoverRating, setHoverRating] = useState(0);
    const [isSubmitting, setIsSubmitting] = useState(false);
    const starRefs = useRef([]);
    const reviewId = props.reviewId || 1;
    const cpId = props.cpId || 1;
    const [companyName, setCompanyName] = useState('');
    const [companyImage, setCompanyImage] = useState('');
    const [productName, setProductName] = useState(props.pdName || '');
    const navigate = useNavigate();

    console.log('ReviewModal props:', { reviewId: props.reviewId, cpId: props.cpId, isEdit: props.isEdit });

    useEffect(() => {
        const fetchCompanyInfo = async () => {
            try {
                const res = await apiClient.get(`${baseUrl}/company/info/${cpId}`);
                setCompanyName(res.data.data.name || '');
                setCompanyImage(res.data.data.profileImageUrl || '');
            } catch (err) {
                setCompanyName('');
                setCompanyImage('');
            }
        };
        fetchCompanyInfo();
    }, [cpId]);

    useEffect(() => {
        if (props.pdName) {
            setProductName(props.pdName);
        } else {
            const fetchProductInfo = async () => {
                try {
                    const res = await apiClient.get(`${baseUrl}/product/info/${props.reviewId || 1}`);
                    setProductName(res.data.data.name || '');
                } catch (err) {
                    setProductName('상품명');
                }
            };
            fetchProductInfo();
        }
    }, [props.pdName, props.reviewId]);

    // 수정 모드일 때 기존 리뷰 데이터 로드
    useEffect(() => {
        if (props.isEdit && props.existingReview) {
            const review = props.existingReview;
            setReviewText(review.content || '');
            setRating(review.score || 0);
            
            // 기존 이미지가 있으면 로드
            if (review.imageUrlList && review.imageUrlList.length > 0) {
                const existingImages = review.imageUrlList.map((url, index) => ({
                    file: null, // 기존 이미지는 파일이 없음
                    previewUrl: url,
                    isExisting: true // 기존 이미지임을 표시
                }));
                setImages(existingImages);
                setSelectedImage(existingImages[0].previewUrl);
            }
        }
    }, [props.isEdit, props.existingReview]);

    const handleImageUpload = (e) => {
        const files = e.dataTransfer ? e.dataTransfer.files : e.target.files;
        
        if (images.length + files.length > 5) {
            alert('이미지는 최대 5개까지만 업로드할 수 있습니다.');
            return;
        }

        const newImages = Array.from(files).map(file => ({
            file,
            previewUrl: URL.createObjectURL(file),
            isExisting: false
        }));

        setImages(prev => [...prev, ...newImages]);
        if (!selectedImage && newImages.length > 0) {
            setSelectedImage(newImages[0].previewUrl);
        }
    };

    // 이미지 삭제
    const handleImageRemove = (indexToRemove) => {
        const updatedImages = images.filter((_, index) => index !== indexToRemove);
        setImages(updatedImages);
        
        // 선택된 이미지가 삭제된 경우 첫 번째 이미지로 변경
        if (selectedImage === images[indexToRemove].previewUrl) {
            setSelectedImage(updatedImages.length > 0 ? updatedImages[0].previewUrl : null);
        }
    };

    // 별점 렌더링 (5개만)
    const renderStars = () => {
        const stars = [];
        const displayRating = hoverRating || rating;
        for (let i = 1; i <= 5; i++) {
            let type = 'empty';
            if (displayRating >= i) type = 'full';
            else if (displayRating >= i - 0.5) type = 'half';

            stars.push(
                <span
                    key={i}
                    ref={el => starRefs.current[i - 1] = el}
                    style={{ position: 'relative', display: 'inline-block', cursor: 'pointer' }}
                    onMouseMove={e => {
                        const rect = e.currentTarget.getBoundingClientRect();
                        const x = e.clientX - rect.left;
                        if (x < rect.width / 2) setHoverRating(i - 0.5);
                        else setHoverRating(i);
                    }}
                    onMouseLeave={() => setHoverRating(0)}
                    onClick={e => {
                        const rect = e.currentTarget.getBoundingClientRect();
                        const x = e.clientX - rect.left;
                        if (x < rect.width / 2) setRating(i - 0.5);
                        else setRating(i);
                    }}
                >
                    <Star type={type} />
                </span>
            );
        }
        return stars;
    };

    // 이미지 업로드 함수
    const uploadImages = async (reviewId) => {
        // 새로 업로드할 이미지만 필터링 (기존 이미지 제외)
        const newImages = images.filter(img => !img.isExisting && img.file);
        
        if (newImages.length === 0) {
            return [];
        }

        const formData = new FormData();
        newImages.forEach((img) => formData.append("imageList", img.file)); 
        
        try {
            const res = await apiClient.post(`${baseUrl}/image/${reviewId}`, formData, {
                headers: { "Content-Type": "multipart/form-data" }
            });
            console.log("🟢 이미지 업로드 응답:", res.data);
            return res.data.data || [];
        } catch (err) {
            console.error("이미지 업로드 실패", err);
            throw new Error("이미지 업로드에 실패했습니다.");
        }
    };

    // 리뷰 등록/수정 함수
    const handleSubmit = async () => {
        if (!reviewText.trim()) {
            alert('리뷰 내용을 입력해주세요.');
            return;
        }
        
        if (rating === 0) {
            alert('별점을 선택해주세요.');
            return;
        }

        setIsSubmitting(true);
        
        try {
            // 1. 리뷰 등록/수정
            const reviewData = {
                reviewId: props.reviewId,
                cpId: props.cpId,
                rvContent: reviewText,
                rvScore: rating,
                imageUrlList: images.map(img => img.previewUrl) // 현재는 임시로 previewUrl 사용
            };

            let reviewResponse;
            if (props.isEdit) {
                // 수정 API 호출 (실제 API 엔드포인트에 맞게 수정 필요)
                reviewResponse = await apiClient.put(`${baseUrl}/review/update`, reviewData);
            } else {
                // 등록 API 호출
                reviewResponse = await apiClient.post(`${baseUrl}/review/write`, reviewData);
            }

            console.log("🟢 리뷰 등록/수정 응답:", reviewResponse.data);
            
            // 2. 이미지 업로드 (새로운 이미지가 있는 경우)
            if (images.some(img => !img.isExisting && img.file)) {
                try {
                    const uploadedImageUrls = await uploadImages(props.reviewId);
                    console.log("🟢 업로드된 이미지 URLs:", uploadedImageUrls);
                } catch (imageError) {
                    console.warn("이미지 업로드 실패했지만 리뷰는 등록됨:", imageError);
                }
            }

            alert(props.isEdit ? '리뷰가 수정되었습니다!' : '리뷰가 등록되었습니다!');
            
            // 3. 모달 닫기 및 페이지 새로고침
            props.onClose();
            
            // 페이지 새로고침으로 업데이트된 데이터 반영
            setTimeout(() => {
                window.location.reload();
            }, 100);
            
        } catch (err) {
            console.error("리뷰 등록/수정 실패:", err);
            alert(props.isEdit ? '리뷰 수정에 실패했습니다.' : '리뷰 등록에 실패했습니다.');
        } finally {
            setIsSubmitting(false);
        }
    };

    return (
        <div className="w-full max-w-4xl mx-auto bg-[#f7f7fa] border border-gray-200 rounded-xl p-8 flex flex-col gap-8 items-start">
            {/* 상단: 좌측 리뷰작성/상품명, 우측 회사정보/평점 */}
            <div className="w-full flex flex-row justify-between items-start mb-8">
                {/* 왼쪽: 리뷰작성, 상품명 */}
                <div className="flex flex-col items-start">
                    <span className="text-2xl font-bold text-[#845EC2] mb-8">
                        {props.isEdit ? '리뷰수정' : '리뷰작성'}
                    </span>
                    <span className="text-xl text-gray-700">상품 : {productName || '상품명'}</span>
                </div>
                {/* 오른쪽: 회사 정보, 평점 */}
                <div className="flex flex-col items-end">
                    <div className="flex items-center gap-2 mb-2">
                        <img
                            src={companyImage || 'https://via.placeholder.com/56x56.png?text=Logo'}
                            alt="회사 프로필"
                            className="w-14 h-14 rounded-full border object-cover bg-white"
                        />
                        <span className="text-xl font-semibold text-[#845EC2]">{companyName || '회사명'}</span>
                    </div>
                    <div className="flex items-center gap-2">
                        <span className="text-lg text-[#845EC2] font-bold">평점</span>
                        <div className="flex items-center">{renderStars()}</div>
                        <span className="text-lg text-[#845EC2] font-bold">{rating.toFixed(1)}</span>
                    </div>
                </div>
            </div>

            {/* 하단: 이미지/리뷰 입력 */}
            <div className="w-full flex gap-8 items-start">
                {/* 이미지 업로드 섹션 */}
                <div className="w-1/3 flex flex-col items-center">
                    {/* 대표 이미지 */}
                    <div className="border rounded-lg overflow-hidden bg-gray-100 w-72 h-96 flex items-center justify-center mb-4 relative">
                        {selectedImage ? (
                            <img src={selectedImage} alt="대표 이미지" className="w-full h-full object-cover" />
                        ) : (
                            <div className="w-full h-full flex items-center justify-center text-gray-400 text-sm">
                                No image
                            </div>
                        )}
                    </div>
                    {/* 이미지 미리보기 */}
                    <div className="flex space-x-2 overflow-x-auto max-w-full p-1 custom-scrollbar mb-2">
                        {images.map((img, index) => (
                            <div key={index} className="relative flex-shrink-0">
                                <img 
                                    src={img.previewUrl}
                                    alt={`업로드된 이미지 ${index}`} 
                                    className="w-12 h-12 object-cover rounded-md cursor-pointer border-2 border-transparent hover:border-[#845EC2]" 
                                    onClick={() => setSelectedImage(img.previewUrl)}
                                />
                                {/* 삭제 버튼 */}
                                <button
                                    onClick={() => handleImageRemove(index)}
                                    className="absolute -top-1 -right-1 w-4 h-4 bg-red-500 text-white rounded-full text-xs flex items-center justify-center hover:bg-red-600"
                                    type="button"
                                >
                                    ×
                                </button>
                            </div>
                        ))}
                    </div>
                    <label 
                        className={`block w-full text-center border ${
                            isDragging ? "border-dashed border-2 border-[#845EC2]" : "border border-[#845EC2]"
                        } text-[#845EC2] rounded-md py-2 cursor-pointer bg-white flex items-center justify-center space-x-2 h-12`}
                        onDragOver={(e) => {
                            e.preventDefault();
                            setIsDragging(true);
                        }}
                        onDragLeave={() => setIsDragging(false)}
                        onDrop={handleImageUpload}
                    >
                        <img src={Addphoto} alt="이미지 추가" className="w-5 h-5" />
                        <span className="text-[#845EC2] font-medium text-sm">이미지 추가하기 (최대 5개)</span>
                        <input type="file" multiple accept="image/*" className="hidden" onChange={handleImageUpload} />
                    </label>
                </div>

                {/* 리뷰 텍스트 섹션 */}
                <div className="w-2/3 flex flex-col">
                    <textarea
                        value={reviewText}
                        onChange={(e) => setReviewText(e.target.value)}
                        placeholder="리뷰를 작성해주세요..."
                        className="w-full h-96 bg-white text-black p-4 border border-gray-300 rounded-lg resize-none focus:outline-none focus:border-[#845EC2] text-base"
                    />
                    <div className="flex justify-end gap-3 mt-4">
                        <button
                            className={`px-6 py-2 rounded-md font-semibold transition-colors ${
                                isSubmitting 
                                    ? 'bg-gray-400 text-gray-600 cursor-not-allowed' 
                                    : 'bg-pp text-white hover:bg-[#6b40b5]'
                            }`}
                            type="button"
                            onClick={handleSubmit}
                            disabled={isSubmitting}
                        >
                            {isSubmitting ? '처리중...' : (props.isEdit ? '수정' : '작성')}
                        </button>
                        <button
                            className="px-6 py-2 rounded-md bg-gray-200 text-gray-700 font-semibold hover:bg-gray-300 transition-colors"
                            type="button"
                            onClick={props.onClose}
                            disabled={isSubmitting}
                        >
                            취소
                        </button>
                    </div>
                </div>
            </div>
        </div>
    );
};

const ReviewModal = ({ isOpen, onClose, ...props }) => {
    if (!isOpen) return null;
    return (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-40">
            <div className="relative bg-white rounded-xl shadow-lg p-0 w-full max-w-4xl">
                <Review {...props} onClose={onClose} />
            </div>
        </div>
    );
};

export default ReviewModal;