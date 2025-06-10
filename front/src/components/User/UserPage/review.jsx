import React, { useState, useRef, useEffect } from 'react';
import Addphoto from '../../../image/Company/addimage.png';
import apiClient from '../../../lib/apiClient';
import { useNavigate } from 'react-router-dom';

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

const Review = () => {
    const [images, setImages] = useState([]);
    const [selectedImage, setSelectedImage] = useState(null);
    const [isDragging, setIsDragging] = useState(false);
    const [reviewText, setReviewText] = useState('');
    const [rating, setRating] = useState(0);
    const [hoverRating, setHoverRating] = useState(0);
    const starRefs = useRef([]);
    const reviewId = 1; // 실제로는 props 등에서 받아와야 함
    const cpId = 1; // 실제로는 props 등에서 받아와야 함
    const [companyName, setCompanyName] = useState('');
    const [companyImage, setCompanyImage] = useState('');
    const navigate = useNavigate();

    useEffect(() => {
        const fetchCompanyInfo = async () => {
            try {
                const res = await apiClient.get(`/company/info/${cpId}`);
                setCompanyName(res.data.data.name || '');
                setCompanyImage(res.data.data.profileImageUrl || '');
            } catch (err) {
                setCompanyName('회사명');
                setCompanyImage('');
            }
        };
        fetchCompanyInfo();
    }, [cpId]);

    const handleImageUpload = (e) => {
        const files = e.dataTransfer ? e.dataTransfer.files : e.target.files;
        
        if (images.length + files.length > 5) {
            alert('이미지는 최대 5개까지만 업로드할 수 있습니다.');
            return;
        }

        const newImages = Array.from(files).map(file => ({
            file,
            previewUrl: URL.createObjectURL(file)
        }));

        setImages(prev => [...prev, ...newImages]);
        if (!selectedImage && newImages.length > 0) {
            setSelectedImage(newImages[0].previewUrl);
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
    const uploadImages = async () => {
        const formData = new FormData();
        images.forEach((img) => formData.append("images", img.file)); 
        try {
            const res = await apiClient.post("/product/image/upload", formData, {
                headers: { "Content-Type": "multipart/form-data" }
            });
            console.log("🟢 업로드 응답:", res.data);
            const urls = res.data.data;
            return urls;
        } catch (err) {
            console.error("이미지 업로드 실패", err);
            return [];
        }
    };
    // 리뷰 등록 함수
    const handleSubmit = async () => {
        try {
            // 1. 이미지 업로드
            let imageUrls = [];
            if (images.length > 0) {
                imageUrls = await uploadImages();
            }
            // 2. 리뷰 등록
            await apiClient.post('/review/register', {
                reviewId, // 실제 id 변수명에 맞게!
                content: reviewText,
                rating,
                imageUrlList: imageUrls
            });
            alert('리뷰가 등록되었습니다!');
            // 필요시 페이지 이동/초기화 등
        } catch (err) {
            alert('리뷰 등록에 실패했습니다.');
            console.error(err);
        }
    };

    return (
        <div className="w-full max-w-4xl mx-auto bg-[#f7f7fa] border border-gray-200 rounded-xl p-8 flex flex-col gap-8 items-start">
            {/* 상단: 회사 프로필, 회사명, 별점 */}
            <div className="w-full flex items-center justify-between mb-2">
                <div className="flex items-center gap-2">
                    {/* 회사 프로필 이미지 */}
                    <img src={companyImage || 'https://via.placeholder.com/56x56.png?text=Logo'} alt="회사 프로필" className="w-14 h-14 rounded-full border object-cover bg-white" />
                    {/* 회사명 */}
                    <span className="text-xl font-semibold text-[#845EC2]">{companyName || '회사명'}</span>
                </div>
                <div className="flex items-center gap-2">
                    <span className="ml-2 text-lg text-[#845EC2] font-bold">평점</span>
                    {/* 별점 */}
                    <div className="flex items-center">{renderStars()}</div>
                    {/* 별점 숫자 표시 */}
                    <span className="ml-2 text-lg text-[#845EC2] font-bold">{rating.toFixed(1)}</span>
                </div>
            </div>

            {/* 하단: 이미지/리뷰 입력 */}
            <div className="w-full flex gap-8 items-start">
                {/* 이미지 업로드 섹션 */}
                <div className="w-1/3 flex flex-col items-center">
                    {/* 대표 이미지 */}
                    <div className="border rounded-lg overflow-hidden bg-gray-100 w-72 h-96 flex items-center justify-center mb-4">
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
                            <img 
                                key={index}
                                src={img.previewUrl}
                                alt={`업로드된 이미지 ${index}`} 
                                className="w-12 h-12 object-cover rounded-md cursor-pointer border-2 border-transparent hover:border-[#845EC2] flex-shrink-0" 
                                onClick={() => setSelectedImage(img.previewUrl)}
                            />
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
                            className="px-6 py-2 rounded-md bg-pp text-white font-semibold hover:bg-[#6b40b5] transition-colors"
                            type="button"
                            onClick={handleSubmit}
                        >
                            작성
                        </button>
                        <button
                            className="px-6 py-2 rounded-md bg-gray-200 text-gray-700 font-semibold hover:bg-gray-300 transition-colors"
                            type="button"
                            onClick={() => navigate(-1)}
                        >
                            취소
                        </button>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Review;
