import React, { useEffect, useState } from "react";
import apiClient from "../../../../lib/apiClient";
const baseUrl = import.meta.env.VITE_API_BASE_URL;

function Notification() {
    const [notifications, setNotifications] = useState([]);

    useEffect(() => {
        // sse 연결
        apiClient.get("/company/sse/subscribe").then((res) => {
            console.log(res);
            if (res.data.status == 200) {
                setNotifications(res.data.data);
                console.log(notifications);
            }
        });

        // 알림 목록 조회
        fetch(`${baseUrl}/company/alert/list`, { credentials: 'include' })
            .then(response => response.json())
            .then(data => {
                if (data.result.resultCode === 200) {
                    setNotifications(data.data);
                    console.log(data.data);
                }
            })
            .catch(error => console.error("알림 데이터 로드 오류", error));
    }, []);

    const getIconByType = (type) => {
        const iconMap = {
            "상품 예약 알림": "📅",
            // 추가 타입별 아이콘 매핑
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