import { useState } from "react";
import { FaBell, FaChevronDown, FaChevronUp } from "react-icons/fa";

export default function AlarmButton() {
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
        <div 
            className="relative"
            onMouseEnter={handleMouseEnter}
            onMouseLeave={handleMouseLeave}
        >
            <div className="flex items-center gap-1 cursor-pointer hover:bg-gray-200 p-2 rounded-lg transition">
                <FaBell className="text-3xl text-[#845EC2]" />
                {isOpen ? (
                    <FaChevronUp className="text-2xl text-[#845EC2]" />
                ) : (
                    <FaChevronDown className="text-2xl text-[#845EC2]" />
                )}
            </div>

            {/* 드롭다운 메뉴 */}
            {isOpen && (
                <div 
                    className="absolute left-0 mt-2 w-40 bg-white shadow-lg rounded-lg py-2 z-50"
                    onMouseEnter={handleMouseEnter} // 드롭다운 내부에서도 유지
                    onMouseLeave={handleMouseLeave}
                >
                    <p className="text-gray-500 text-center py-1">알림 센터</p>
                    <hr />
                    <ul className="text-black text-sm">
                        <li className="px-4 py-2 hover:bg-gray-100 cursor-pointer">📩 메시지</li>
                        <li className="px-4 py-2 hover:bg-gray-100 cursor-pointer">🔔 주문 업데이트</li>
                        <li className="px-4 py-2 hover:bg-gray-100 cursor-pointer">⚠ 보안 알림</li>
                    </ul>
                </div>
            )}
        </div>
    );
}
