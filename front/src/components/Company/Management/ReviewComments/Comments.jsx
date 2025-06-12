import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import apiClient from "../../../../lib/apiClient";

// 별점 표시용 컴포넌트
const StarRating = ({ score }) => {
    if (!score && score !== 0) return null;
    
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

function Comments() {
    const { reviewId } = useParams();
    const [review, setReview] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [reply, setReply] = useState(null);
    const [input, setInput] = useState("");
    const [isSubmitting, setIsSubmitting] = useState(false);
    const [selectedImage, setSelectedImage] = useState(null);

    useEffect(() => {
        const fetchReviewDetail = async () => {
            try {
                setLoading(true);
                const response = await apiClient.get(`/review/info/${reviewId}`);
                console.log('리뷰 상세 데이터:', response.data);
                setReview(response.data.data);
                if (response.data.data.imageUrlList && response.data.data.imageUrlList.length > 0) {
                    setSelectedImage(response.data.data.imageUrlList[0]);
                }
            } catch (err) {
                console.error('리뷰 상세 조회 에러:', err);
                setError('리뷰 정보를 불러오는데 실패했습니다.');
            } finally {
                setLoading(false);
            }
        };

        fetchReviewDetail();
    }, [reviewId]);

    if (loading) return <div className="text-center p-4">로딩중...</div>;
    if (error) return <div className="text-red-500 p-4">{error}</div>;
    if (!review) return <div className="text-center p-4">리뷰를 찾을 수 없습니다.</div>;

    const handleSubmit = (e) => {
        e.preventDefault();
        if (!input.trim()) return;
        setIsSubmitting(true);
        setTimeout(() => {
            setReply({
                id: 1,
                author: "운영자",
                content: input,
                date: new Date().toISOString().slice(0, 10)
            });
            setInput("");
            setIsSubmitting(false);
        }, 500);
    };

    return (
        <div className="w-full px-12 py-8">
            {/* 리뷰 상세 */}
            <div className="bg-white rounded-xl shadow p-8 mb-8">
                <div className="flex flex-col md:flex-row md:items-start md:gap-8 gap-8">
                    {/* 이미지 업로드 섹션 (업로드 X, 보기만) */}
                    <div className="w-full md:w-1/3 flex flex-col items-center">
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
                            {review.imageUrlList && review.imageUrlList.map((img, index) => (
                                <div key={index} className="relative flex-shrink-0">
                                    <img
                                        src={img}
                                        alt={`업로드된 이미지 ${index}`}
                                        className={`w-12 h-12 object-cover rounded-md cursor-pointer border-2 ${selectedImage === img ? 'border-[#845EC2]' : 'border-transparent'} hover:border-[#845EC2]`}
                                        onClick={() => setSelectedImage(img)}
                                    />
                                </div>
                            ))}
                        </div>
                    </div>
                    {/* 리뷰 정보 */}
                    <div className="flex-1">
                        <div className="flex gap-4 items-center mb-2">
                            <span className="text-[#845EC2] font-bold">결제번호</span>
                            <span>{review.orderNo}</span>
                            <span className="text-[#845EC2] font-bold ml-6">작성자</span>
                            <span>{review.user}</span>
                            <span className="text-[#845EC2] font-bold ml-6">작성일</span>
                            <span>{review.date}</span>
                        </div>
                        <div className="flex gap-4 items-center mb-2">
                            <span className="text-[#845EC2] font-bold">상품명</span>
                            <span>{review.product}</span>
                            <span className="text-[#845EC2] font-bold ml-6">평점</span>
                            <StarRating score={review.score} />
                        </div>
                        <div className="mt-4 text-gray-700 text-base">
                            {review.content}
                        </div>
                    </div>
                </div>
            </div>
            {/* 답글 */}
            <div className="bg-white rounded-xl shadow p-8 mb-8">
                <h2 className="text-lg font-bold text-[#845EC2] mb-4">답글</h2>
                {reply && (
                    <div className="border-b pb-2">
                        <div className="flex gap-2 items-center mb-1">
                            <span className="font-semibold text-[#845EC2]">{reply.author}</span>
                            <span className="text-xs text-gray-400">{reply.date}</span>
                        </div>
                        <div className="text-gray-800 text-sm">{reply.content}</div>
                    </div>
                )}
            </div>
            {/* 답글 작성 (답글이 없을 때만) */}
            {!reply && (
                <form onSubmit={handleSubmit} className="bg-white rounded-xl shadow p-8 flex flex-col gap-4">
                    <textarea
                        className="w-full bg-white border border-gray-300 rounded-md p-3 resize-none focus:outline-none focus:border-[#845EC2] text-base"
                        rows={3}
                        placeholder="답글을 입력하세요..."
                        value={input}
                        onChange={e => setInput(e.target.value)}
                        disabled={isSubmitting}
                    />
                    <div className="flex justify-end">
                        <button
                            type="submit"
                            className={`px-6 py-2 rounded-md font-semibold transition-colors ${isSubmitting ? 'bg-gray-400 text-gray-600 cursor-not-allowed' : 'bg-pp text-white hover:bg-[#6b40b5]'}`}
                            disabled={isSubmitting}
                        >
                            {isSubmitting ? '등록중...' : '답글 등록'}
                        </button>
                    </div>
                </form>
            )}
        </div>
    );
}

export default Comments; 