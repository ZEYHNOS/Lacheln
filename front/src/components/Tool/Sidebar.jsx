import { useState } from "react";
import { FaChevronLeft, FaChevronDown, FaChevronUp } from "react-icons/fa";
import CompanyNav from "./CompanyNavgation/CompanyNav.jsx";


export default function Sidebar({ isOpen, toggleSidebar }) {
    const [openMenu, setOpenMenu] = useState(null);

    const toggleMenu = (menu) => {
        setOpenMenu(openMenu === menu ? null : menu);
    };

    const handleMenuClick = (menu) => {
        const items = subMenus[menu];
        if (items.length === 1) {
            window.location.href = items[0].url;
        } else {
            toggleMenu(menu);
        }
    };

    const subMenus = {
        // " 브랜드": [
        //     { name: "브랜드 페이지", url: "/brand" },
        // ],
        " 상품": [
            { name: "상품 페이지", url: "/product" },
        ],
        " 패키지": [
            { name: "패키지 페이지", url: "/package" },
        ],
        // " 이벤트": [
        //     { name: "전체 이벤트", url: "/event" },
        // ],
        " 커뮤니티": [
            { name: "전체 게시판", url: "/community" },
        ],
        " 고객센터": [
            { name: "고객지원", url: "/support" },
        ],
    };

    return (
        <div
            className={`fixed top-0 left-0 h-full bg-white w-80 shadow-md transform ${isOpen ? "translate-x-0" : "-translate-x-full"
                } transition-transform duration-300 ease-in-out flex flex-col z-50`}
        >
            {/* 사이드바 상단 */}
            <div className="flex justify-between items-center p-4 border-b border-gray-200">
                <div className="text-center w-full">
                    <h1 className="text-4xl font-inknut font-semibold text-[#845EC2]">Lächeln</h1>
                    <p className="text-xs text-[#845EC2]">스튜디오 드레스 메이크업</p>
                </div>

                {/* 닫기 버튼 */}
                <button
                    className="text-xl text-[#845EC2] p-2 bg-white transition focus:outline-none focus:ring-0"
                    onClick={toggleSidebar}
                >
                    <FaChevronLeft />
                </button>
            </div>

            {/* 메뉴 목록 */}
            <nav className="mt-4 flex-1 overflow-y-auto">
                <ul className="space-y-4 px-6 text-lg text-[#845EC2]">
                    {Object.keys(subMenus).map((menu, index) => (
                        <li key={index} className="cursor-pointer">
                            <div
                                className="flex items-center space-x-2 hover:text-[#845EC2]"
                                onClick={() => handleMenuClick(menu)}
                            >
                                <div className="flex items-center">
                                    <span className="text-2xl mr-2">·</span>
                                    <span>{menu}</span>
                                </div>
                            </div>

                            {/* 페이지 바로가기 목록*/}
                            {openMenu === menu && (
                                <ul className="mt-2 space-y-2 pl-6 text-base">
                                    {subMenus[menu].map((item, subIndex) => (
                                        <li key={subIndex} className="cursor-pointer hover:text-[#00C9A7]">
                                            <a href={item.url} className="text-[#845EC2]">{item.name}</a>
                                        </li>
                                    ))}
                                </ul>
                            )}
                        </li>
                    ))}
                </ul>
            </nav>

            {/* 회사 네비게이션 */}
            <div className="p-6 text-lg text-[#845EC2] space-y-2 flex flex-col sidebar-nav mt-auto">
                <nav>
                    <ul className="space-y-4 px-2 text-lg text-[#845EC2]">
                        <li><a href="/about" className="flex items-center hover:underline">회사소개</a></li>
                        <li><a href="/terms" className="flex items-center hover:underline">이용약관</a></li>
                        <li><a href="/privacy" className="flex items-center hover:underline">개인정보처리방침</a></li>
                        <li><a href="/location" className="flex items-center hover:underline">오시는 길</a></li>
                    </ul>
                </nav>
            </div>

            <style>
                {`
                    .sidebar-nav a {
                        display: block;
                        margin-bottom: 0.5rem;
                        color: #845EC2 !important;
                    }
                `}
            </style>
        </div>
    );
}
