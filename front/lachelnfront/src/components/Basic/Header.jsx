import { useState } from "react";
import { FaBars } from "react-icons/fa";
import Sidebar from "./Sidebar"; 
import SearchBar from "./SearchBar";
import NationButton from "./Button/NationButton";
import ProfileButton from "./Button/ProfileButton";
import AlarmButton from "./Button/AlarmButton";
import CartButton from "./Button/CartButton";

export default function Header() {
    const [isSidebarOpen, setIsSidebarOpen] = useState(false);

    // 사이드바 열고 닫는 함수
    const toggleSidebar = () => setIsSidebarOpen(!isSidebarOpen);

    return (
        <header className="bg-[#FBFBFB] shadow-md px-8 py-3 relative">
            {/* 첫 번째 줄: 좌측 메뉴 아이콘 & 우측 아이콘 */}
            <div className="flex items-center justify-between">
                {/* 좌측 메뉴 버튼 */}
                <button
                    className="p-2 text-[#845EC2] text-5xl bg-[#FBFBFB] hover:bg-gray-200 rounded-lg transition focus:outline-none"
                    onClick={toggleSidebar}
                >
                    <FaBars />
                </button>

                {/* 우측 버튼들 */}
                <div className="flex items-center gap-1">
                    <NationButton />
                    <ProfileButton />
                    <AlarmButton />
                    <CartButton />
                </div>
            </div>

            {/* 두 번째 줄: 로고 , 검색창 */}
            <div className={`flex justify-center items-center py-1 transition-all duration-300 ${isSidebarOpen ? "justify-start pl-10" : ""}`}>
                {/* 로고 (사이드바 열리면 숨김) */}
                {!isSidebarOpen && (
                    <a href="/" className="mr-10 cursor-pointer">
                        <h1 className="text-5xl font-inknut font-semibold text-[#845EC2]">Lächeln</h1>
                        <p className="text-lg text-[#845EC2]">스튜디오 드레스 메이크업</p>
                    </a>
                )}

                {/* 검색창 (사이드바 열리면 확장) */}
                <div
                    className={`relative transition-all duration-300 ${
                        isSidebarOpen ? "max-w-[60%] w-[60%] h-20" : "w-[44rem] h-16"
                    }`}
                >
                    <SearchBar /> {/* 자동완성 검색창 적용 */}
                </div>
            </div>

            {/* 사이드바 추가 */}
            <Sidebar isOpen={isSidebarOpen} toggleSidebar={toggleSidebar} />
        </header>
    );
}
