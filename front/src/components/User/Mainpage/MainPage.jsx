import React, { useEffect, useRef, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import main1 from "../../../image/MainpageSlide/slide1.png";
import main2 from "../../../image/MainpageSlide/slide2.png";
import main3 from "../../../image/MainpageSlide/slide3.png";
import apiClient from "../../../lib/apiClient";
import { EllipseCarousel } from "./PopularProduct";
import officialYoutube from '../../../image/MainpageSlide/official_youtube.png';
import officialInsta from '../../../image/MainpageSlide/official_insta.png';
import productIcon from '../../../image/MainpageSlide/button/product.png';
import packageIcon from '../../../image/MainpageSlide/button/package.png';
import boardIcon from '../../../image/MainpageSlide/button/board.png';
import helpIcon from '../../../image/MainpageSlide/button/help.png';
import { motion } from "framer-motion";

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
    const [postList, setPostList] = useState([]);
    const [miniCategory, setMiniCategory] = useState("드레스");
    const [miniProducts, setMiniProducts] = useState([]);
    const baseUrl = import.meta.env.VITE_API_BASE_URL;

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

    useEffect(() => {
        // 게시글 5개 불러오기
        apiClient.get("/post/all?page=1&size=5")
            .then(res => {
                if (res.data && res.data.data && res.data.data.content) {
                    setPostList(res.data.data.content);
                }
            })
            .catch(err => {
                console.error("게시글 불러오기 실패:", err);
            });
    }, []);

    useEffect(() => {
        const code = { "드레스": "D", "스튜디오": "S", "메이크업": "M" }[miniCategory];
        apiClient.get(`/api/product/list?category=${code}&size=3`)
            .then(res => setMiniProducts(res.data.data || []));
    }, [miniCategory]);

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
            <motion.div
                initial={{ opacity: 0, y: 40 }}
                whileInView={{ opacity: 1, y: 0 }}
                viewport={{ once: false, amount: 0.3 }}
                transition={{ duration: 0.7 }}
                className="w-full flex justify-center mt-4"
            >
                <div className="relative w-full max-w-7xl h-[500px] flex items-center justify-center rounded-2xl group">
                    {/* 이미지 */}
                    {carouselImages.map((img, idx) => {
                        let position = idx - current;
                        if (position < -1) position += carouselImages.length;
                        if (position > 1) position -= carouselImages.length;

                        const isCenter = Math.abs(position) === 0;
                        let style = {
                            borderRadius: '20px',
                            transition: 'transform 0.7s, opacity 0.7s, width 0.7s, height 0.7s',
                            zIndex: isCenter ? 10 : 0,
                            opacity: Math.abs(position) > 1 ? 0 : 1,
                            width: isCenter ? '70%' : '50%',
                            height: isCenter ? '70%' : '50%',
                            left: '50%',
                            top: '50%',
                            transform: `translate(-50%, -50%) translateX(${position * 85}%)`
                        };

                        return (
                            <img
                                key={img.src}
                                src={img.src}
                                alt={img.alt}
                                className="absolute object-cover"
                                style={style}
                            />
                        );
                    })}
                    {/* 좌우 버튼 */}
                    <button onClick={goToPrev} className="absolute -left-16 top-1/2 -translate-y-1/2 text-black p-3 shadow text-5xl z-20 border-0 bg-transparent transition">&#60;</button>
                    <button onClick={goToNext} className="absolute -right-16 top-1/2 -translate-y-1/2 text-black p-3 shadow text-5xl z-20 border-0 bg-transparent transition">&#62;</button>
                    {/* 인디케이터 바 */}
                    <div className="absolute bottom-6 left-1/2 -translate-x-1/2 flex gap-2 z-20">
                        {carouselImages.map((_, idx) => (
                            <button
                                key={idx}
                                onClick={() => setCurrent(idx)}
                                className={`transition-all duration-300 rounded-full ${current === idx ? 'bg-[#845EC2]' : 'bg-gray-300'}`}
                                aria-label={`Go to slide ${idx + 1}`}
                                style={{ height: '2px', width: current === idx ? '40px' : '18px', minWidth: 'unset', padding: 0 }}
                            />
                        ))}
                    </div>
                </div>
            </motion.div>

            {/* 바로가기 버튼 */}
            <motion.div
                initial={{ opacity: 0, y: 40 }}
                whileInView={{ opacity: 1, y: 0 }}
                viewport={{ once: false, amount: 0.3 }}
                transition={{ duration: 0.7, delay: 0.1 }}
                className="flex gap-16 justify-center my-8"
            >
                <a href="/product">
                    <img src={productIcon} alt="product" className="h-40 w-40 object-contain border-2 border-[#845EC2] rounded-xl" />
                </a>
                <a href="/package">
                    <img src={packageIcon} alt="package" className="h-40 w-40 object-contain border-2 border-[#845EC2] rounded-xl" />
                </a>
                <a href="/community">
                    <img src={boardIcon} alt="board" className="h-40 w-40 object-contain border-2 border-[#845EC2] rounded-xl" />
                </a>
                <a href="/support">
                    <img src={helpIcon} alt="help" className="h-40 w-40 object-contain border-2 border-[#845EC2] rounded-xl" />
                </a>
            </motion.div>

            {/* 인기 상품 */}
            <motion.div
                initial={{ opacity: 0, y: 40 }}
                whileInView={{ opacity: 1, y: 0 }}
                viewport={{ once: false, amount: 0.3 }}
                transition={{ duration: 0.7, delay: 0.2 }}
                className="w-full max-w-6xl px-8 mt-16 mb-32"
            >
                <h2 className="text-3xl font-bold mb-16 text-[#845EC2]">인기 상품</h2>
                <EllipseCarousel items={popularProducts.slice(0, 10)} />
            </motion.div>
            
            {/* 리뷰/게시글 리스트 영역 */}
            <motion.div
                initial={{ opacity: 0, y: 40 }}
                whileInView={{ opacity: 1, y: 0 }}
                viewport={{ once: false, amount: 0.3 }}
                transition={{ duration: 0.7, delay: 0.4 }}
                className="flex gap-16 justify-between w-full my-12 pl-32 pr-32 "
            >
                {/* 리뷰 리스트 */}
                <div className="flex-1 border-t-4 border-b-4 border-[#845EC2] flex flex-col items-center mx-2">
                    <div className="flex gap-2 mb-4 mt-2">
                        {["드레스", "스튜디오", "메이크업"].map(cat => (
                            <button
                                key={cat}
                                className={`px-3 py-1 rounded ${miniCategory === cat ? "bg-[#845EC2] text-white" : "bg-white text-[#845EC2] border border-[#845EC2]"}`}
                                onClick={() => setMiniCategory(cat)}
                            >{cat}</button>
                        ))}
                    </div>
                    <div className="grid grid-cols-3 gap-4 w-full">
                        {miniProducts.length === 0 ? (
                            <div className="col-span-3 text-center text-gray-400 py-10">상품이 없습니다.</div>
                        ) : (
                            miniProducts.map(item => (
                                <div key={item.productId} className="bg-white rounded shadow p-2 flex flex-col items-center">
                                    <div className="aspect-[3/4] w-21 rounded mb-2 overflow-hidden bg-gray-100">
                                        <img
                                            src={
                                                item.imageUrl
                                                    ? item.imageUrl.startsWith("/image/")
                                                        ? `${baseUrl}${item.imageUrl}`
                                                        : `${baseUrl}${item.imageUrl.replace(/\\/g, "/")}`
                                                    : "/default/images/product.png"
                                            }
                                            alt={item.productName}
                                            className="w-full h-full object-cover"
                                        />
                                    </div>
                                    <div className="font-medium text-xs text-gray-800 text-center line-clamp-1">{item.productName}</div>
                                    <div className="text-[10px] text-gray-500 text-center line-clamp-1">{item.companyName}</div>
                                    <div className="text-xs text-violet-600 font-semibold mt-1">₩ {item.price.toLocaleString()}</div>
                                </div>
                            ))
                        )}
                    </div>
                </div>
                {/* 게시글 리스트 */}
                <div className="flex-1 border-t-4 border-b-4 border-[#845EC2] flex flex-col items-center mx-2">
                    <div className="text-[#3CB4AC] text-lg font-semibold my-2">최근 게시판</div>
                    <ul className="w-full">
                        {postList.length === 0
                            ? [1, 2, 3, 4, 5].map(idx => (
                                <li key={idx} className="py-2 px-4 border-b last:border-b-0 text-center text-gray-400">게시글 없음</li>
                            ))
                            : postList.map((post, idx) => (
                                <li key={post.postId || idx} className="py-2 px-4 border-b last:border-b-0 flex items-center text-center">
                                    <span className="flex-[2] text-xs text-gray-500 text-left">{post.category}</span>
                                    <span className="flex-[3] font-semibold text-left">{post.postTitle || post.title}</span>
                                    <span className="flex-[1] text-xs text-gray-500 text-left">{post.userNickName}</span>
                                    <span className="flex-[1] text-pink-500 text-right">♥ {post.likeCount}</span>
                                </li>
                            ))}
                    </ul>
                </div>
            </motion.div>

            {/* 공식 유튜브, 인스타그램 */}
            <motion.div
                initial={{ opacity: 0, y: 40 }}
                whileInView={{ opacity: 1, y: 0 }}
                viewport={{ once: false, amount: 0.3 }}
                transition={{ duration: 0.7, delay: 0.3 }}
                className="w-full flex flex-col items-center max-w-4xl mx-auto my-16"
            >
                <div className="text-3xl font-bold mb-2 text-[#845EC2]">OFFICIAL ZONE</div>
                <div className="w-full h-1 bg-[#845EC2] rounded mb-10" style={{ maxWidth: '600px' }}></div>
                <div className="w-full flex justify-between items-center gap-20">
                    <a href="https://www.youtube.com/@yeungjin" target="_blank" rel="noopener noreferrer">
                        <img src={officialYoutube} alt="공식 유튜브" className="h-48 object-contain rounded-xl" />
                    </a>
                    <a href="https://www.instagram.com/with_yju/" target="_blank" rel="noopener noreferrer">
                        <img src={officialInsta} alt="공식 인스타그램" className="h-48 object-contain rounded-xl" />
                    </a>
                </div>
            </motion.div>
        </div>
    );
}

export default MainPage;
