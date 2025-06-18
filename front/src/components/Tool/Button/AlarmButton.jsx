import { useState, useEffect, useRef } from "react";
import { FaBell, FaChevronDown, FaChevronUp } from "react-icons/fa";
import apiClient from "../../../lib/apiClient";

const baseUrl = import.meta.env.VITE_API_BASE_URL;
const retryDelay = 5000; // 5초 후 재연결

export default function AlarmButton({ isActive, onClick, isLoggedIn, hasNewAlarm }) {
    const [notifications, setNotifications] = useState([]);
    const [unreadCount, setUnreadCount] = useState(0);
    const eventSourceRef = useRef(null);

    useEffect(() => {
        const connectSSE = () => {
            if (eventSourceRef.current) {
                eventSourceRef.current.close(); // 기존 연결 닫기
            }

            const eventSource = new EventSource(`${baseUrl}/user/sse/subscribe`, { withCredentials: true });
            eventSourceRef.current = eventSource;

            eventSource.onopen = (e) => {
                console.log("✅ SSE 연결됨:", e);
            };

            eventSource.addEventListener("connect", (e) => {
                console.log("🔌 connect 이벤트 수신:", e.data);
            });

            eventSource.addEventListener("alert", (e) => {
                console.log("📩 alert 이벤트 수신:", e.data);
                try {
                    const newNotification = JSON.parse(e.data);
                    setNotifications((prev) => [newNotification, ...prev]);
                    setUnreadCount((prev) => prev + 1); // 읽지 않은 알림 개수 증가
                } catch (err) {
                    console.error("❗ JSON 파싱 실패", err);
                }
            });

            eventSource.onerror = (e) => {
                console.error("❌ SSE 에러 발생:", e);
                console.log("🛰️ readyState:", eventSource.readyState);

                eventSource.close();
                eventSourceRef.current = null;

                setTimeout(() => {
                    console.log("🔄 SSE 재연결 시도 중...");
                    connectSSE();
                }, retryDelay);
            };
        };

        if (isLoggedIn) {
            connectSSE();
        }

        return () => {
            if (eventSourceRef.current) {
                eventSourceRef.current.close();
            }
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

            {hasNewAlarm && (
                <span className="absolute top-1 right-1 w-3 h-3 bg-red-500 rounded-full border-2 border-white"></span>
            )}

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
                                <span className="flex-1">{item.title || JSON.stringify(item)}</span>
                                <span className="flex-1">{item.content || ""}</span>
                            </li>
                        ))}
                    </ul>
                </div>
            )}
        </div>
    );
}
