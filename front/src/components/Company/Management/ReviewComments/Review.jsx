import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import apiClient from "../../../../lib/apiClient";

const baseUrl = import.meta.env.VITE_API_BASE_URL;
// 별점 표시용 컴포넌트
const StarRating = ({ score }) => {
    const fullStars = Math.floor(score);
    const halfStar = score % 1 >= 0.5;
    const emptyStars = 5 - fullStars - (halfStar ? 1 : 0);

    return (
        <div className="flex items-center">
            {Array(fullStars).fill(0).map((_, i) => (
                <svg key={`full-${i}`} width="20" height="20" viewBox="0 0 24 24" fill="#845EC2">
                    <path d="M12 17.27L18.18 21l-1.64-7.03L22 9.24l-7.19-.61L12 2 9.19 8.63 2 9.24l5.46 4.73L5.82 21z"/>
                </svg>
            ))}
            {halfStar && (
                <svg key="half" width="20" height="20" viewBox="0 0 24 24">
                    <defs>
                        <linearGradient id="half">
                            <stop offset="50%" stopColor="#845EC2"/>
                            <stop offset="50%" stopColor="#E0E0E0"/>
                        </linearGradient>
                    </defs>
                    <path d="M12 17.27L18.18 21l-1.64-7.03L22 9.24l-7.19-.61L12 2 9.19 8.63 2 9.24l5.46 4.73L5.82 21z" fill="url(#half)"/>
                </svg>
            )}
            {Array(emptyStars).fill(0).map((_, i) => (
                <svg key={`empty-${i}`} width="20" height="20" viewBox="0 0 24 24" fill="#E0E0E0">
                    <path d="M12 17.27L18.18 21l-1.64-7.03L22 9.24l-7.19-.61L12 2 9.19 8.63 2 9.24l5.46 4.73L5.82 21z"/>
                </svg>
            ))}
            <span className="ml-1 text-gray-600">{score.toFixed(1)}</span>
        </div>
    );
};

function Review() {
    const [reviews, setReviews] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [companyId, setCompanyId] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        fetchCompanyAndReviews();
    }, []);

    const fetchCompanyAndReviews = async () => {
        try {
            setLoading(true);
            // 먼저 회사 정보를 가져옵니다
            const companyResponse = await apiClient.get('/company/me');
            console.log('회사 정보 응답:', companyResponse.data);
            const companyId = companyResponse.data;
            console.log('회사 ID:', companyId);
            setCompanyId(companyId);

            // 회사 ID로 리뷰를 조회합니다
            const reviewResponse = await apiClient.get(`/review/company/${companyId}`);
            console.log('리뷰 데이터:', reviewResponse.data);
            setReviews(reviewResponse.data.data);
            setError(null);
        } catch (err) {
            console.error('상세 에러 정보:', err.response || err);
            setError('데이터를 불러오는데 실패했습니다.');
            console.error('데이터 조회 에러:', err);
        } finally {
            setLoading(false);
        }
    };

    if (loading) return <div className="text-center p-4">로딩중...</div>;
    if (error) return <div className="text-red-500 p-4">{error}</div>;

    return (
        <div className="container mx-auto p-4">
            <h1 className="text-3xl font-extrabold text-[#845EC2] mb-6 border-b pb-3">리뷰 관리</h1>
            
            {reviews.length === 0 ? (
                <p className="text-gray-500">등록된 리뷰가 없습니다.</p>
            ) : (
                <div className="grid gap-4">
                    {reviews.map((review) => (
                        <div 
                            key={review.reviewId} 
                            className="border rounded-lg p-4 shadow-sm cursor-pointer hover:bg-gray-50"
                            onClick={() => navigate(`/company/review/comments/${review.reviewId}`)}
                        >
                            <div className="flex justify-between items-start mb-2">
                                <div>
                                    <h3 className="font-semibold text-black">{review.nickname} 님의 리뷰!</h3>
                                    <p className="text-sm text-gray-500">
                                        {new Date(
                                            review.createdAt[0],
                                            review.createdAt[1] - 1,
                                            review.createdAt[2],
                                            review.createdAt[3] || 0,
                                            review.createdAt[4] || 0,
                                            review.createdAt[5] || 0,
                                            review.createdAt[6] || 0
                                        ).toLocaleDateString()}
                                    </p>
                                    <p className="text-sm text-gray-600 mt-1">
                                        상품: {review.productName}
                                    </p>
                                </div>
                                <StarRating score={review.score} />
                            </div>
                            <p className="text-gray-700 mt-2">{review.content}</p>
                            {review.imageUrlList && review.imageUrlList.length > 0 && (
                                <div className="mt-3 flex gap-2">
                                    {review.imageUrlList.map((imageUrl, index) => (
                                        <img 
                                            key={index}
                                            src={imageUrl ? `${baseUrl}${imageUrl.replace(/\\/g, '/')}` : '/default/images/product.png'} 
                                            alt={`리뷰 이미지 ${index + 1}`}
                                            className="w-20 h-20 object-cover rounded"
                                            onClick={() => handleImageClick(imageUrl)}
                                        />
                                    ))}
                                </div>
                            )}
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
}

export default Review; 