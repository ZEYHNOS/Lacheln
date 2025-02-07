import { useState } from "react";
import { FaUser, FaChevronDown, FaChevronUp } from "react-icons/fa";

export default function ProfileButton() {
    const [isOpen, setIsOpen] = useState(false);
    let timeoutId = null;

    const handleMouseEnter = () => {
        clearTimeout(timeoutId);
        setIsOpen(true);
    };

    const handleMouseLeave = () => {
        timeoutId = setTimeout(() => setIsOpen(false), 200); // 0.2초 딜레이 후 닫힘
    };
    
    return (
        <div className="relative">
            {/* 버튼 */}
            <div 
                className="flex items-center gap-1 cursor-pointer hover:bg-gray-200 p-2 rounded-lg transition"
                onMouseEnter={handleMouseEnter}
                onMouseLeave={handleMouseLeave}
            >
                <FaUser className="text-3xl text-[#845EC2]" />
                {isOpen ? (
                    <FaChevronUp className="text-2xl text-[#845EC2]" />
                ) : (
                    <FaChevronDown className="text-2xl text-[#845EC2]" />
                )}
            </div>

            {/* 드롭다운 메뉴 */}
            {isOpen && (
                <div 
                className="absolute right-0 mt-2 w-40 bg-white shadow-lg rounded-lg py-2 z-50"
                onMouseEnter={handleMouseEnter} // 드롭다운 내부에서도 유지
                onMouseLeave={handleMouseLeave}
                >
                    <button className="w-full text-black text-lg font-semibold py-3 bg-gray-100 rounded-t-lg">
                        로그인
                    </button>
                    <p className="text-gray-500 text-center py-1">회원가입하기</p>
                    <hr />
                    <ul className="text-black text-sm">
                        <li className="px-4 py-2 hover:bg-gray-100 cursor-pointer">📦 내 주문</li>
                        <li className="px-4 py-2 hover:bg-gray-100 cursor-pointer">💰 내 리뷰</li>
                        <li className="px-4 py-2 hover:bg-gray-100 cursor-pointer"> 🎟 내 쿠폰</li>
                        <li className="px-4 py-2 hover:bg-gray-100 cursor-pointer">💬 메시지</li>
                        <li className="px-4 py-2 hover:bg-gray-100 cursor-pointer">💳 결제</li>
                        <li className="px-4 py-2 hover:bg-gray-100 cursor-pointer">❤️ 구독</li>
                        
                    </ul>
                </div>
            )}
        </div>
    );
}
