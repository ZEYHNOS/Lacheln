import { useState, useEffect } from "react";
import { FaBars } from "react-icons/fa";
import { useLocation } from "react-router-dom";
import { toast } from "react-toastify";
import Sidebar from "./Sidebar";
import SearchBar from "./SearchBar";
import NationButton from "./Button/NationButton";
import ProfileButton from "./Button/ProfileButton";
import AlarmButton from "./Button/AlarmButton";
import CartButton from "./Button/CartButton";
import apiClient from "../../lib/apiClient";

export default function Header() {
    const [isSidebarOpen, setIsSidebarOpen] = useState(false);
    const [activeButton, setActiveButton] = useState(null);
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const location = useLocation();

    useEffect(() => {
        // /login에서 왔는지 확인
        const isFromLogin = document.referrer.includes('/login');

        apiClient.get("/auth/me")
            .then(res => {
                const wasLoggedIn = isLoggedIn;
                const isNowLoggedIn = !!res.data.data?.valid;
                setIsLoggedIn(isNowLoggedIn);

                // /login에서 왔고, 로그인된 상태라면 토스트 표시
                if (isFromLogin && isNowLoggedIn) {
                    toast.success("소셜 로그인이 완료되었습니다!", {
                        position: "top-center",
                        autoClose: 1000,
                    });
                }
            })
            .catch(() => setIsLoggedIn(false));
    }, [location]);

    const handleLogout = () => {
        setIsLoggedIn(false);
    };

    // 사이드바 열고 닫는 함수
    const toggleSidebar = () => setIsSidebarOpen(!isSidebarOpen);

    // 클릭 시 드롭다운 토글 함수
    const handleButtonClick = (buttonName) => {
        setActiveButton(prev => (prev === buttonName ? null : buttonName));
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
                        onClick={() => handleButtonClick("nation")}/>
                    <ProfileButton
                        isActive={activeButton === "profile"}
                        onClick={() => handleButtonClick("profile")}
                        isLoggedIn={isLoggedIn}
                        onLogout={handleLogout}/>
                    <AlarmButton
                        isActive={activeButton === "alarm"}
                        onClick={() => handleButtonClick("alarm")}
                        isLoggedIn={isLoggedIn}/>
                    <CartButton
                        isActive={activeButton === "cart"}
                        onClick={() => handleButtonClick("cart")}
                        isLoggedIn={isLoggedIn}/>
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
