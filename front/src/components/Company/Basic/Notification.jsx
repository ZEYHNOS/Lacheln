import React from "react";
import apiClient from "../../../lib/apiClient";
import { useNavigate } from "react-router-dom";
const baseUrl = import.meta.env.VITE_API_BASE_URL;

// 날짜 배열 → YYYY.MM.DD HH:mm 포맷 함수
function formatDate(arr) {
    if (!arr || arr.length < 5) return "";
    const [y, mo, d, h, mi] = arr;
    return `${y}.${String(mo).padStart(2, '0')}.${String(d).padStart(2, '0')} ${String(h).padStart(2, '0')}:${String(mi).padStart(2, '0')}`;
}

const getIconByTitle = (title) => {
    if (!title) return "🔔";
    if (title.includes("리뷰")) return "💬";
    if (title.includes("예약")) return "📅";
    return "🔔";
};

function Notification({ notifications, setNotifications, setUnreadCount }) {
    const navigate = useNavigate();
    const handleNotificationClick = (alertId, accessUrl) => {
        fetch(`${baseUrl}/company/alert/read`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ alertId }),
            credentials: 'include'
        }).then(() => {
            setNotifications(prev => prev.filter(notification => notification.alertId !== alertId));
            if (accessUrl) navigate(accessUrl);
        }).catch(error => console.error("읽음 처리 오류", error));
    };

    return (
        <div className="w-full bg-white p-6 rounded shadow text-black">
            <div className="max-w-2xl">
                <h1 className="text-2xl font-bold text-[#845EC2] mb-6">알림 내역</h1>
                <ul>
                    {notifications.length === 0 && (
                        <li className="text-gray-400 py-8 text-center">알림이 없습니다.</li>
                    )}
                    {notifications.map((item, idx) => (
                        <li
                            key={item.alertId || idx}
                            onClick={() => handleNotificationClick(item.alertId, item.accessUrl)}
                            className="flex items-start py-4 border-b last:border-b-0 border-[#D6CDEA] cursor-pointer"
                        >
                            <span className="text-2xl mr-4 mt-1">{getIconByTitle(item.title)}</span>
                            <div className="flex-1">
                                <div className="font-bold text-base">{item.text}</div>
                            </div>
                            <span className="text-sm text-gray-500">{formatDate(item.sentTime)}</span>
                        </li>
                    ))}
                </ul>
            </div>
        </div>
    );
}

export default Notification; 