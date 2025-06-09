import React, { useEffect, useState, useRef } from "react";
import apiClient from "../../../../lib/apiClient";
const baseUrl = import.meta.env.VITE_API_BASE_URL;

function Notification() {
    const [notifications, setNotifications] = useState([]);
    const eventSourceRef = useRef(null);
    const retryDelay = 5000; // 5초 후 재연결

    const connectSSE = () => {
        if (eventSourceRef.current) {
            eventSourceRef.current.close(); // 기존 연결 닫기
        }

        const eventSource = new EventSource(`${baseUrl}/sse/company/subscribe`, { withCredentials: true });
        eventSourceRef.current = eventSource;

        eventSource.onopen = (e) => {
            console.log("✅ SSE 연결됨:", e);
        };

        eventSource.addEventListener("alert", (e) => {
            console.log("📩 alert 이벤트 수신:", e.data);
            try {
                const newNotification = JSON.parse(e.data);
                setNotifications((prev) => [newNotification, ...prev]);
            } catch (err) {
                console.error("❗ JSON 파싱 실패", err);
            }
        });

        eventSource.onerror = (e) => {
            console.error("❌ SSE 에러 발생:", e);
            console.log("🛰️ readyState:", eventSource.readyState);

            eventSource.close();
            eventSourceRef.current = null;

            // 재연결 시도
            setTimeout(() => {
                console.log("🔄 SSE 재연결 시도 중...");
                connectSSE();
            }, retryDelay);
        };
    };

    useEffect(() => {
        connectSSE();
        return () => {
            if (eventSourceRef.current) {
                eventSourceRef.current.close();
            }
        };
    }, []);

    const getIconByType = (type) => {
        const iconMap = {
            "상품 예약 알림": "📅",
        };
        return iconMap[type] || "🔔";
    };

    const handleNotificationClick = (msId, accessUrl) => {
        fetch(`${baseUrl}/company/alert/read`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ msId }),
            credentials: 'include'
        }).then(() => {
            window.location.href = accessUrl;
        }).catch(error => console.error("읽음 처리 오류", error));
    };

    return (
        <div className="w-full bg-white p-6 rounded shadow text-black lg:ml-24 md:ml-12 ml-4 mt-4">
            <div className="max-w-2xl">
                <h1 className="text-2xl font-bold text-[#845EC2] mb-6">알림 내역</h1>
                <ul>
                    {notifications.length === 0 && (
                        <li className="text-gray-400 py-8 text-center">알림이 없습니다.</li>
                    )}
                    {notifications.map((item, idx) => (
                        <li key={idx} onClick={() => handleNotificationClick(item.msId, item.accessUrl)} className="flex items-start py-4 border-b last:border-b-0 border-[#D6CDEA]">
                            <span className="text-2xl mr-4 mt-1">{getIconByType(item.type)}</span>
                            <span className="flex-1 text-base leading-relaxed">{item.content}</span>
                            <span className="text-sm text-gray-500">{new Date(item.sentTime).toLocaleTimeString('ko-KR', { hour: '2-digit', minute: '2-digit' })}</span>
                        </li>
                    ))}
                </ul>
            </div>
        </div>
    );
}

export default Notification;
