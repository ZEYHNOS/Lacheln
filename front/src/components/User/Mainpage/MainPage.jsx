import React, { useEffect, useRef, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import main1 from "../../../image/MainpageSlide/mainpageslide.jpeg";
import main2 from "../../../image/MainpageSlide/mainpageslide2.jpg";
import main3 from "../../../image/MainpageSlide/mainpageslide3.jpg";
import luxuaryImg from '../../../image/userprofile/luxuary.jpg';
import apiClient from "../../../lib/apiClient";
import { EllipseCarousel } from "./PopularProduct";

const carouselImages = [
    { src: main1, alt: "메인페이지 슬라이드1" },
    { src: main2, alt: "메인페이지 슬라이드2" },
    { src: main3, alt: "메인페이지 슬라이드3" }
];

function MainPage() {
    const location = useLocation();
    const navigate = useNavigate();
    const hasShownToast = useRef(false);
    const [current, setCurrent] = useState(0);
    const timeoutRef = useRef(null);
    const [popularProducts, setPopularProducts] = useState([]);

    useEffect(() => {
        // 이미 토스트를 표시했다면 더 이상 실행하지 않음
        if (hasShownToast.current) return;

        // 소셜 로그인 또는 로컬 로그인 시도 여부 확인
        const socialLoginAttempt = sessionStorage.getItem('socialLoginAttempt');
        const localLoginSuccess = sessionStorage.getItem('localLoginSuccess');
        const userType = sessionStorage.getItem('userType');

        if (socialLoginAttempt === 'true' || localLoginSuccess === 'true') {
            // 로그인 상태 확인
            fetch(`${import.meta.env.VITE_API_BASE_URL}/auth/me`, {
                credentials: 'include'
            })
                .then(res => res.json())
                .then(data => {
                    if (data.data?.valid && !hasShownToast.current) {
                        // 유저 타입에 따라 다른 메시지 표시
                        const message = userType === "COMPANY" ? "업체 로그인이 완료되었습니다!" : "로그인이 완료되었습니다!";
                        toast.success(message, {
                            position: "top-center",
                            autoClose: 1000,
                            onClose: () => {
                                // 토스트가 닫힌 후 업체인 경우 회사 페이지로 이동
                                if (userType === "COMPANY") {
                                    navigate('/company');
                                }
                            }
                        });
                        hasShownToast.current = true;
                        // 상태 제거
                        sessionStorage.removeItem('socialLoginAttempt');
                        sessionStorage.removeItem('localLoginSuccess');
                        sessionStorage.removeItem('userType');
                    }
                })
                .catch(error => console.error("로그인 상태 확인 중 오류:", error));
        }
    }, [navigate]);

    useEffect(() => {
        // 인기 상품 불러오기
        apiClient.get("/product/popular")
            .then(res => {
                if (res.data && res.data.data) {
                    setPopularProducts(res.data.data);
                }
            })
            .catch(err => {
                console.error("인기 상품 불러오기 실패:", err);
            });
    }, []);

    // 자동 슬라이드
    useEffect(() => {
        if (timeoutRef.current) clearTimeout(timeoutRef.current);
        timeoutRef.current = setTimeout(() => {
            setCurrent((prev) => (prev + 1) % carouselImages.length);
        }, 5000);
        return () => clearTimeout(timeoutRef.current);
    }, [current]);

    const goToPrev = () => setCurrent((prev) => (prev - 1 + carouselImages.length) % carouselImages.length);
    const goToNext = () => setCurrent((prev) => (prev + 1) % carouselImages.length);

    return (
        <div className="min-h-screen bg-white text-black flex flex-col items-center">
            {/* 캐러셀 */}
            <div className="relative w-full h-[500px] flex items-center justify-center overflow-hidden bg-gray-100 mt-6 rounded-2xl shadow-lg group">
                {/* 이미지 */}
                {carouselImages.map((img, idx) => (
                    <img
                        key={img.src}
                        src={img.src}
                        alt={img.alt}
                        className={`absolute top-0 left-0 w-full h-full object-cover transition-opacity duration-700 ${idx === current ? 'opacity-100 z-0' : 'opacity-0 z-0'} pointer-events-none`}
                    />
                ))}
                {/* 좌우 버튼 */}
                <button onClick={goToPrev} className="absolute left-4 top-1/2 -translate-y-1/2 bg-white/60 hover:bg-white/90 rounded-full p-2 shadow text-3xl opacity-0 group-hover:opacity-100 transition z-10">&#60;</button>
                <button onClick={goToNext} className="absolute right-4 top-1/2 -translate-y-1/2 bg-white/60 hover:bg-white/90 rounded-full p-2 shadow text-3xl opacity-0 group-hover:opacity-100 transition z-10">&#62;</button>
                {/* 인디케이터 */}
                <div className="absolute bottom-4 left-1/2 -translate-x-1/2 flex gap-2 z-10">
                    {carouselImages.map((_, idx) => (
                        <div key={idx} className={`w-3 h-3 rounded-full ${idx === current ? 'bg-[#845EC2]' : 'bg-white border border-[#845EC2]'} transition-all`}></div>
                    ))}
                </div>
            </div>

            {/* 인기 상품 */}
            <div className="w-full max-w-6xl px-8 mt-16 mb-32">
                <h2 className="text-2xl font-bold mb-6 text-[#845EC2]">인기 상품</h2>
                <EllipseCarousel items={popularProducts.slice(0, 10)} />
            </div>
        </div>
    );
}

export default MainPage;
