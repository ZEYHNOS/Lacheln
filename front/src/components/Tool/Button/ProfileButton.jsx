import { useRef, useState } from "react";
import { FaUser, FaChevronDown, FaChevronUp } from "react-icons/fa";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import apiClient from "../../../lib/apiClient";
import ChattingRoomModal from "../../User/UserPage/chattingRoomModal";
import ChattingModal from "../../User/UserPage/ChattingModal";

export default function ProfileButton({ isActive, onClick, isLoggedIn, onLogout }) {
    const [showMessageModal, setShowMessageModal] = useState(false);

    const openMessageModal = () => {
        setShowMessageModal(true); // 메시지 모달 열기
    };

    const closeMessageModal = () => {
        onClick(); // 드롭다운 닫기
        setShowMessageModal(false);
    }

    const navigate = useNavigate();

    const handleMyPage = () => {
        onClick();
        navigate("/user");
    };

    const handleLogin = () => {
        navigate("/login");
    };

    const handleReview = () => {
        onClick();
        navigate("/user/review");
    }

    const handleSubscribe = () => {
        onClick();
        navigate("/user/wishsub");
    }

    const handleCoupons = () => {
        onClick();
        navigate("/user/coupons");
    }

    const handleLogout = async () => {
        try {
            const response = await apiClient.post("/userlogout");
            if (response.data.result.resultCode === 201) {
                toast.success("안전하게 로그아웃 되었습니다.", {
                    position: "top-center",
                    autoClose: 1000,
                });
                onLogout();
                onClick();
                navigate("/");
            }
        } catch (error) {
            console.error("로그아웃 중 오류 발생:", error);
            toast.error("로그아웃 중 오류가 발생했습니다. 다시 시도해주세요.", {
                position: "top-center",
                autoClose: 1000,
            });
        }
    };

    return (
        <div className="relative">
            {/* 프로필 버튼 */}
            <div
                className={`flex items-center gap-1 cursor-pointer hover:bg-gray-200 p-2 rounded-lg transition ${isActive ? "bg-gray-300" : ""}`}
                onClick={onClick}
            >
                <FaUser className="text-3xl text-[#845EC2]" />
                {isActive ? (
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
                                <li className="px-4 py-2 hover:bg-gray-100 cursor-pointer" onClick={handleMyPage}>😊 내 정보</li>
                                <li className="px-4 py-2 hover:bg-gray-100 cursor-pointer" onClick={handleReview}>📦 주문 & 리뷰</li>
                                <li className="px-4 py-2 hover:bg-gray-100 cursor-pointer" onClick={handleSubscribe}>❤️ 구독내역</li>
                                <li className="px-4 py-2 hover:bg-gray-100 cursor-pointer" onClick={handleCoupons}>🎟 내 쿠폰</li>
                                <li className="px-4 py-2 hover:bg-gray-100 cursor-pointer" onClick={openMessageModal}>
                                    📩 내 메시지
                                </li>
                            </ul>
                        </>
                    )}
                    {/* 메시지 모달 */}
                    {showMessageModal && (
                        <ChattingRoomModal showModal={showMessageModal} onClose={closeMessageModal} />
                    )}

                    {/* 채팅창만 있는 모달 인자값으로 CompanyId와 onClose가져가기 */}
                    {/* {showMessageModal && (
                        <ChattingModal companyId={1} onClose={closeMessageModal} />
                    )} */}
                </div>
            )}
        </div>
    );
}
