import { useRef } from "react";
import { FaUser, FaChevronDown, FaChevronUp } from "react-icons/fa";
import { useNavigate } from "react-router-dom";
import apiClient from "../../../lib/apiClient";

export default function ProfileButton({ isActive, onClick, isLoggedIn, onLogout }) {
    const navigate = useNavigate();

    const handleLogin = () => {
        navigate("/login");
    };

    const handleLogout = async () => {
        try {
            const response = await apiClient.post("/userlogout");
            if (response.data.result.resultCode === 201) {
                onLogout();
                navigate("/");
            }
        } catch (error) {
            console.error("로그아웃 중 오류 발생:", error);
        }
    };

    return (
        <div className="relative">
            {/* 프로필 버튼 */}
            <div
                className={`flex items-center gap-1 cursor-pointer hover:bg-gray-200 p-2 rounded-lg transition ${isActive && isLoggedIn ? "bg-gray-300" : ""}`}
                onClick={onClick}
            >
                <FaUser className="text-3xl text-[#845EC2]" />
                {isActive && isLoggedIn ? (
                    <FaChevronUp className="text-2xl text-[#845EC2]" />
                ) : (
                    <FaChevronDown className="text-2xl text-[#845EC2]" />
                )}
            </div>

            {/* 드롭다운 메뉴 */}
            {isActive && (
                <div
                    className="absolute right-0 mt-2 w-40 bg-white shadow-lg rounded-lg py-2 z-50">
                        
                    {/* 로그인/로그아웃 버튼 */}
                    {!isLoggedIn ? (
                        <button
                            className="w-full text-black text-lg font-semibold py-3 bg-gray-100 rounded-t-lg"
                            onClick={handleLogin}>
                            로그인
                        </button>
                    ) : (
                        <button
                            className="w-full text-black text-lg font-semibold py-3 bg-gray-100 rounded-t-lg"
                            onClick={handleLogout}>
                            로그아웃
                        </button>
                    )}

                    {/* 로그인 상태일 때 표시되는 메뉴 */}
                    {isLoggedIn && ( <>
                            <hr />
                            <ul className="text-black text-sm">
                                <li className="px-4 py-2 hover:bg-gray-100 cursor-pointer">😊 내 정보</li>
                                <li className="px-4 py-2 hover:bg-gray-100 cursor-pointer">📦 내 주문</li>
                                <li className="px-4 py-2 hover:bg-gray-100 cursor-pointer">💰 내 리뷰</li>
                                <li className="px-4 py-2 hover:bg-gray-100 cursor-pointer">❤️ 구독내역</li>
                                <li className="px-4 py-2 hover:bg-gray-100 cursor-pointer">🎟 내 쿠폰</li>
                            </ul>
                        </>
                    )}
                </div>
            )}
        </div>
    );
}
