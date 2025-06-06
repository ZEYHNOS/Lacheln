import { useState, useEffect, useRef } from "react";
import { FaBell, FaChevronDown, FaChevronUp } from "react-icons/fa";
import ChattingRoomModal from "../../User/UserPage/chattingRoomModal";

export default function AlarmButton({ isActive, onClick, isLoggedIn }) {
    const [notifications, setNotifications] = useState([]);
    const eventSourceRef = useRef(null);

    useEffect(() => {
        if (!isLoggedIn) return;
        const eventSource = new EventSource("/user/sse/subscribe", { withCredentials: true });
        eventSourceRef.current = eventSource;

        eventSource.onmessage = (event) => {
            try {
                const data = JSON.parse(event.data);
                if (data.isTrusted) return; // 연결 확인 메시지는 무시
                setNotifications((prev) => [data, ...prev].slice(0, 10)); // 최근 10개만
            } catch (e) {}
        };
        eventSource.onerror = () => {
            eventSource.close();
        };
        return () => {
            eventSource.close();
        };
    }, [isLoggedIn]);

    const handleAlertHistoryClick = () => {
        onClick(); // 드롭다운 닫기
        // URL 이동 등 추가 동작
        window.location.href = "/user/alert/list";
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
                <div className="absolute right-0 mt-2 w-72 bg-white shadow-lg rounded-lg py-2 z-50">
                    <p
                        className="text-gray-500 text-center py-1 cursor-pointer"
                        onClick={handleAlertHistoryClick}
                    >
                        🔔 알림내역
                    </p>
                    <hr />
                    <ul className="text-black text-sm max-h-80 overflow-y-auto">
                        {notifications.length === 0 && (
                            <li className="px-4 py-2 text-gray-400 text-center">알림이 없습니다.</li>
                        )}
                        {notifications.map((item, idx) => (
                            <li key={idx} className="px-4 py-2 hover:bg-gray-100 flex items-start gap-2">
                                <span className="text-xl">{item.icon || "🔔"}</span>
                                <span className="flex-1">{item.text || JSON.stringify(item)}</span>
                            </li>
                        ))}
                    </ul>
                </div>
            )}
        </div>
    );
}
