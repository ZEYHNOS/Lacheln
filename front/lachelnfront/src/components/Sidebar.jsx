import { FaChevronLeft } from "react-icons/fa";

export default function Sidebar({ isOpen, toggleSidebar }) {
    return (
        <div
            className={`fixed top-0 left-0 h-full bg-white w-80 shadow-md transform ${
                isOpen ? "translate-x-0" : "-translate-x-full"
            } transition-transform duration-300 ease-in-out`}
        >
            {/* 사이드바 상단에 로고 + 닫기 버튼 */}
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
                    <li className="cursor-pointer hover:text-[#6A4BBC]">📦 패키지</li>
                    <li className="cursor-pointer hover:text-[#6A4BBC]">👜 브랜드</li>
                    <li className="cursor-pointer hover:text-[#6A4BBC]">🎁 상품</li>
                    <li className="cursor-pointer hover:text-[#6A4BBC]">🎉 이벤트</li>
                    <li className="cursor-pointer hover:text-[#6A4BBC]">📢 광고</li>
                    <li className="cursor-pointer hover:text-[#6A4BBC]">💬 커뮤니티</li>
                    <li className="cursor-pointer hover:text-[#6A4BBC]">📞 고객센터</li>
                </ul>
            </nav>
        </div>
    );
}
