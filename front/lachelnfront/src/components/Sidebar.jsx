import { useState } from "react";
import { FaChevronLeft, FaChevronDown, FaChevronUp } from "react-icons/fa";
import CompanyNav from "./CompanyNavgation/CompanyNav.jsx";


export default function Sidebar({ isOpen, toggleSidebar }) {
    const [openMenu, setOpenMenu] = useState(null);

    const toggleMenu = (menu) => {
        setOpenMenu(openMenu === menu ? null : menu);
    };

    const subMenus = {
        "📦 패키지": ["HOT 패키지", "이달의 패키지", "패키지상품"],
        "👜 브랜드": ["구찌 Gucci", "루이비통 Louis Vuitton", "샤넬 Chanel"],
        "🎁 상품": ["스튜디오", "드레스", "메이크업"],
        "🎉 이벤트": ["이달의 이벤트", "당첨자 발표"],
        "📢 광고": ["광고 안내", "광고 문의"],
        "💬 커뮤니티": ["게시판", "리뷰", "질문"],
        "📞 고객센터": ["FAQ", "문의하기"],
    };

    return (
        <div
            className={`fixed top-0 left-0 h-full bg-white w-80 shadow-md transform ${
                isOpen ? "translate-x-0" : "-translate-x-full"
            } transition-transform duration-300 ease-in-out`}
        >
            {/* 사이드바 상단 */}
            <div className="flex justify-between items-center p-4 border-b border-gray-200">
                <div className="text-center w-full">
                    <h1 className="text-4xl font-inknut font-semibold text-[#845EC2]">Lächeln</h1> {/* 로고 키움 */}
                    <p className="text-xs text-[#845EC2]">스튜디오 드레스 메이크업</p> {/* 스드메 글씨 줄임 */}
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
            <nav className="mt-4">
                <ul className="space-y-4 px-6 text-lg text-[#845EC2]">
                    {Object.keys(subMenus).map((menu, index) => (
                        <li key={index} className="cursor-pointer">
                            <div
                                className="flex items-center space-x-2 hover:text-[#6A4BBC]"
                                onClick={() => toggleMenu(menu)}
                            >
                                {openMenu === menu ? <FaChevronUp /> : <FaChevronDown />}
                                <span>{menu}</span>
                            </div>

                            {/* 세부 요소 */}
                            {openMenu === menu && (
                                <ul className="mt-2 space-y-2 pl-6 text-base hover:text-[#B39CD0]">
                                    {subMenus[menu].map((item, subIndex) => (
                                        <li key={subIndex} className="cursor-pointer hover:text-[#00C9A7]">
                                            {item}
                                        </li>
                                    ))}
                                </ul>
                            )}
                        </li>
                    ))}
                </ul>
                {/* 회사 네비게이션 */}
                <div className="p-4 text-lg text-[#845EC2] space-y-2 flex flex-col sidebar-nav">
                    <CompanyNav />
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
            </nav>
        </div>
    );
}
