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
    const [activeButton, setActiveButton] = useState(null);

    // 사이드바 열고 닫는 함수
    const toggleSidebar = () => setIsSidebarOpen(!isSidebarOpen);

    // 버튼 상태 제어 함수
    const handleMouseEnter = (buttonName) => {
        setActiveButton(buttonName);
    };

    const handleMouseLeave = () => {
        setActiveButton(null);
    };

    return (
        <header className="bg-[#FBFBFB] shadow-md px-8 py-3 relative">
            {/* 첫 번째 줄: 좌측 메뉴 아이콘 & 우측 아이콘 */}
            <div className="flex items-center justify-between">
                {/* 좌측 메뉴 버튼 */}
                <button
                    className="p-2 text-[#845EC2] text-5xl bg-[#FBFBFB] hover:bg-gray-200 rounded-lg transition focus:outline-none"
                    onClick={toggleSidebar}>
                    <FaBars />
                </button>
                {/* 우측 버튼들 */}
                <div className="flex items-center gap-1">
                    <NationButton
                        isActive={activeButton === "nation"}
                        onMouseEnter={() => handleMouseEnter("nation")}
                        onMouseLeave={handleMouseLeave}/>
                    <ProfileButton
                        isActive={activeButton === "profile"}
                        onMouseEnter={() => handleMouseEnter("profile")}
                        onMouseLeave={handleMouseLeave}/>
                    <AlarmButton
                        isActive={activeButton === "alarm"}
                        onMouseEnter={() => handleMouseEnter("alarm")}
                        onMouseLeave={handleMouseLeave}/>
                    <CartButton
                        isActive={activeButton === "cart"}
                        onMouseEnter={() => handleMouseEnter("cart")}
                        onMouseLeave={handleMouseLeave}/>
                </div>
            </div>
            {/* 두 번째 줄: 로고 , 검색창 */}
            <div className={`flex justify-center items-center py-1 transition-all duration-300 ${isSidebarOpen ? "justify-start pl-10" : ""}`}>
                {!isSidebarOpen && (
                    <a href="/" className="mr-10 cursor-pointer">
                        <h1 className="text-5xl font-inknut font-semibold text-[#845EC2]">Lächeln</h1>
                        <p className="text-lg text-[#845EC2]">스튜디오 드레스 메이크업</p>
                    </a>
                )}
                <div
                    className={`relative transition-all duration-300 ${
                        isSidebarOpen ? "max-w-[60%] w-[60%] h-20" : "w-[44rem] h-16"
                    }`}
                >
                    <SearchBar />
                </div>
            </div>
            {/* 사이드바 추가 */}
            <Sidebar isOpen={isSidebarOpen} toggleSidebar={toggleSidebar} />
        </header>
    );
}
