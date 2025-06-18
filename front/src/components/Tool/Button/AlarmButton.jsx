import { useState, useEffect, useRef } from "react";
import { FaBell, FaChevronDown, FaChevronUp } from "react-icons/fa";
import apiClient from "../../../lib/apiClient";
import { useNavigate } from "react-router-dom";

const baseUrl = import.meta.env.VITE_API_BASE_URL;
const retryDelay = 5000; // 5초 후 재연결

export default function AlarmButton({ isActive, onClick, isLoggedIn }) {
    const [notifications, setNotifications] = useState([]);
    const [unreadCount, setUnreadCount] = useState(0);
    const eventSourceRef = useRef(null);
    const navigate = useNavigate();

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

    // 드롭다운(알람창) 열릴 때 읽지 않은 알림 초기화
    useEffect(() => {
        if (isActive) {
            setUnreadCount(0);
        }
    }, [isActive]);

    const handleAlertHistoryClick = () => {
        onClick(); // 드롭다운 닫기
        window.location.href = "/user/alert/list";
    };

    // 날짜 포맷 함수 추가
    function formatDate(dateString) {
        if (!dateString) return '';
        const date = new Date(dateString);
        const yyyy = date.getFullYear();
        const mm = String(date.getMonth() + 1).padStart(2, '0');
        const dd = String(date.getDate()).padStart(2, '0');
        const hh = String(date.getHours()).padStart(2, '0');
        const min = String(date.getMinutes()).padStart(2, '0');
        return `${yyyy}.${mm}.${dd} ${hh}:${min}`;
    }

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

            {unreadCount > 0 && (
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
                            <li
                                key={idx}
                                className="px-4 py-2 hover:bg-gray-100 cursor-pointer flex flex-col items-start gap-1"
                                onClick={e => {
                                    e.stopPropagation();
                                    navigate(item.accessUrl);
                                }}
                            >
                                <div className="flex items-center gap-2">
                                    <span className="text-xl">{item.icon || "🔔"}</span>
                                    <span className="font-bold text-sm text-gray-800">{item.title || JSON.stringify(item)}</span>
                                </div>
                                <div className="text-xs text-gray-600 ml-7">{item.content || ""}</div>
                                <div className="text-[10px] text-gray-400 ml-7 mt-1">{item.time ? formatDate(item.time) : ''}</div>
                            </li>
                        ))}
                    </ul>
                </div>
            )}
        </div>
    );
}
