import React, { useState, useRef, useEffect } from 'react';
import luxuaryImg from '../../../image/userprofile/luxuary.jpg';

export function EllipseCarousel({ items }) {
    const [active, setActive] = useState(0);
    const total = items.length;
    const dragStartX = useRef(null);
    const dragEndX = useRef(null);
    const dragging = useRef(false);
    const autoSlideRef = useRef();
    const intervalRef = useRef();

    // placeholder 카드
    const placeholders = Array(10).fill(0).map((_, i) => ({
        productId: `placeholder-${i}`,
        productImageUrl: luxuaryImg,
        productName: '상품 준비중',
        price: null,
        companyName: '-',
        category: '-',
        rank: i + 1
    }));
    const displayItems = items.length > 0 ? items : placeholders;

    // 자동 슬라이드
    useEffect(() => {
        startAutoSlide();
        return () => stopAutoSlide();
    }, [active, displayItems.length]);

    const startAutoSlide = () => {
        stopAutoSlide();
        intervalRef.current = setInterval(() => {
            setActive((prev) => (prev + 1) % displayItems.length);
        }, 1000);
    };
    const stopAutoSlide = () => {
        if (intervalRef.current) clearInterval(intervalRef.current);
    };
    // 드래그/스와이프 핸들러 (드래그 끝날 때만 이동)
    const handleDragStart = (e) => {
        dragging.current = true;
        dragStartX.current = e.type === 'touchstart' ? e.touches[0].clientX : e.clientX;
        dragEndX.current = null;
        stopAutoSlide();
    };
    const handleDragMove = (e) => {
        if (!dragging.current) return;
        dragEndX.current = e.type === 'touchmove' ? e.touches[0].clientX : e.clientX;
    };
    const handleDragEnd = () => {
        if (!dragging.current || dragStartX.current === null || dragEndX.current === null) {
            dragging.current = false;
            startAutoSlide();
            return;
        }
        const diff = dragEndX.current - dragStartX.current;
        if (Math.abs(diff) > 40) { // 40px 이상 움직이면 넘김
            if (diff > 0) {
                setActive((prev) => (prev - 1 + displayItems.length) % displayItems.length);
            } else {
                setActive((prev) => (prev + 1) % displayItems.length);
            }
        }
        dragging.current = false;
        dragStartX.current = null;
        dragEndX.current = null;
        startAutoSlide();
    };
    // 버튼 클릭 시 자동 슬라이드 리셋
    const handlePrev = (e) => {
        e.stopPropagation();
        setActive((active - 1 + displayItems.length) % displayItems.length);
        startAutoSlide();
    };
    const handleNext = (e) => {
        e.stopPropagation();
        setActive((active + 1) % displayItems.length);
        startAutoSlide();
    };

    return (
        <div
            className="relative w-full h-[500px] mx-auto flex items-center justify-center"
            style={{ perspective: '1400px' }}
            onMouseDown={handleDragStart}
            onMouseMove={handleDragMove}
            onMouseUp={handleDragEnd}
            onMouseLeave={handleDragEnd}
            onTouchStart={handleDragStart}
            onTouchMove={handleDragMove}
            onTouchEnd={handleDragEnd}
        >
            {displayItems.map((item, i) => {
                let diff = i - active;
                if (diff > displayItems.length / 2) diff -= displayItems.length;
                if (diff < -displayItems.length / 2) diff += displayItems.length;
                // -2, -1, 0, 1, 2만 보이게
                let scale = 1.0;
                let opacity = 0;
                let pointerEvents = 'none';
                let zIndex = -Math.abs(diff);
                if (diff === 0) {
                    scale = 0.9;
                    opacity = 1;
                    pointerEvents = 'auto';
                    zIndex = 10;
                } else if (Math.abs(diff) === 1) {
                    scale = 0.8;
                    opacity = 1;
                    pointerEvents = 'auto';
                    zIndex = 5;
                } else if (Math.abs(diff) === 2) {
                    scale = 0.7;
                    opacity = 0.4;
                    pointerEvents = 'auto';
                    zIndex = 1;
                } else if (Math.abs(diff) <= 4) {
                    scale = 0.65;
                    opacity = 0.2;
                    pointerEvents = 'auto';
                    zIndex = 0;
                }

                // 원의 중심에서 바라보는 3D 원형 캐러셀 효과 
                const angleStep = 30; // 각 카드 간 각도
                const angle = diff * angleStep;
                const radius = 550; // 원의 반지름(px)
                return (
                    <div
                        key={item.productId}
                        className="absolute left-1/2 top-1/2"
                        style={{
                            transform: `translate(-50%, -50%) rotateY(${angle}deg) translateZ(${radius}px) scale(${scale})`,
                            opacity,
                            zIndex,
                            transition: 'all 0.7s cubic-bezier(.4,2,.6,1)',
                            pointerEvents
                        }}
                    >
                        <div className="w-64 h-96 bg-white rounded-xl shadow-lg flex flex-col items-center justify-center border-2 border-[#845EC2] overflow-hidden">
                            <div className="w-full h-64 flex items-center justify-center bg-gray-100 mb-2">
                                <img
                                    src={item.productImageUrl}
                                    alt={item.productName}
                                    className="object-cover w-full h-full"
                                    onError={e => e.currentTarget.src = luxuaryImg}
                                />
                            </div>
                            <div className="text-xs text-gray-400 mb-1">{item.category} | {item.companyName}</div>
                            <div className="text-base font-bold text-[#845EC2] mb-1 truncate w-full text-center">{item.productName}</div>
                            <div className="text-sm text-gray-700 font-semibold mb-2">{item.price ? `₩ ${item.price?.toLocaleString()}` : <span className="text-gray-300">가격 정보 없음</span>}</div>
                            <span className={`inline-block px-2 py-1 rounded-full text-xs font-bold mt-auto mb-2 ${item.rank === 1 ? 'bg-[#845EC2] text-white' : 'bg-gray-200 text-gray-700'}`}>#{item.rank}</span>
                        </div>
                    </div>
                );
            })}
            {/* 좌우 버튼 */}
            <button
                onClick={handlePrev}
                className="absolute -left-10 top-1/2 -translate-y-1/2 bg-white/80 hover:bg-white rounded-full p-2 shadow text-2xl z-20"
            >&#60;</button>
            <button
                onClick={handleNext}
                className="absolute -right-10 top-1/2 -translate-y-1/2 bg-white/80 hover:bg-white rounded-full p-2 shadow text-2xl z-20"
            >&#62;</button>
        </div>
    );
}
