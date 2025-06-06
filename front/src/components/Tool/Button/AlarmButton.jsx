import { useState } from "react";
import { FaBell, FaChevronDown, FaChevronUp } from "react-icons/fa";
import ChattingRoomModal from "../../User/UserPage/chattingRoomModal";

export default function AlarmButton({ isActive, onClick, isLoggedIn }) {

    const handleAlertHistoryClick = () => {
        onClick(); // 드롭다운 닫기
        // 여기에 알림 내역 보기 로직 추가하면 됩니다.
        console.log("🔔 알림 내역 보기");
    };

    return (
        <div className="relative">
            <div
                className={`flex items-center gap-1 cursor-pointer hover:bg-gray-200 p-2 rounded-lg transition ${isActive && isLoggedIn ? "bg-gray-300" : ""} ${!isLoggedIn ? "opacity-50 cursor-not-allowed" : ""}`}
                onClick={onClick}
            >
                <FaBell className="text-3xl text-[#845EC2]" />
                {isActive && isLoggedIn ? (
                    <FaChevronUp className="text-2xl text-[#845EC2]" />
                ) : (
                    <FaChevronDown className="text-2xl text-[#845EC2]" />
                )}
            </div>

            {/* 드롭다운 메뉴 */}
            {isActive && isLoggedIn && (
                <div className="absolute left-0 mt-2 w-40 bg-white shadow-lg rounded-lg py-2 z-50">
                    <p className="text-gray-500 text-center py-1">알림 센터</p>
                    <hr />
                    <ul className="text-black text-sm">
                        <li
                            className="px-4 py-2 hover:bg-gray-100 cursor-pointer"
                            onClick={handleAlertHistoryClick}
                        >
                            🔔 알림내역
                        </li>
                    </ul>
                </div>
            )}
        </div>
    );
}
