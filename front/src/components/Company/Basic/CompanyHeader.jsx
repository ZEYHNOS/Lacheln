import React, { useState, useEffect, useRef } from "react";
import { Link, useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import { ChevronDown, ChevronUp, LogOut } from "lucide-react";
import apiClient from "../../../lib/apiClient";
import NotificationIcon from "../../../image/Company/Header/notification.png";
import Notification from "./Notification";

const baseUrl = import.meta.env.VITE_API_BASE_URL;

function CompanyHeader() {
    const navigate = useNavigate();
    const [showNotification, setShowNotification] = useState(false);
    const [notifications, setNotifications] = useState([]);
    const [unreadCount, setUnreadCount] = useState(0);
    const eventSourceRef = useRef(null);
    const retryDelay = 5000; // 5초 후 재연결

    useEffect(() => {
        const connectSSE = () => {
            if (eventSourceRef.current) {
                eventSourceRef.current.close(); // 기존 연결 닫기
            }

            const eventSource = new EventSource(`${baseUrl}/sse/company/subscribe`, { withCredentials: true });
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

        connectSSE();

        return () => {
            if (eventSourceRef.current) {
                eventSourceRef.current.close();
            }
        };
    }, []);

    const handleLogout = async () => {
        try {
            const response = await apiClient.post("/userlogout");
            if (response.data.result.resultCode === 201) {
                toast.success("안전하게 로그아웃 되었습니다.", {
                    position: "top-center",
                    autoClose: 1000,
                });
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

    const handleNotificationClick = () => {
        setShowNotification(!showNotification);
        if (!showNotification) {
            setUnreadCount(0); // 알림 목록을 열 때 읽지 않은 알림 개수 초기화
        }
    };

    return (
        <header className="bg-white flex flex-col justify-between items-center relative border-b-2 border-pp w-full py-8">
            <div className="flex justify-between items-center w-full px-6">
                <div className="w-16"></div>
                <Link to="/company" className="text-center my-1 ml-16">
                    <h1 className="text-4xl font-inknut font-semi text-pp">Lächeln</h1>
                    <p className="text-pp text-sm">스튜디오 드레스 메이크업</p>
                </Link>
                <div className="flex items-center gap-4 text-pp">
                    <button
                        onClick={handleLogout}
                        className="flex items-center gap-1 bg-white hover:bg-gray-100 p-2 rounded-lg transition"
                    >
                        <LogOut size={30} />
                        <span className="text-md">로그아웃</span>
                    </button>
                    <div className="relative">
                        <div 
                            className="cursor-pointer flex items-center" 
                            onClick={handleNotificationClick}
                        >
                            <img src={NotificationIcon} alt="Notification" className="w-10 h-10" />
                            {showNotification ? (
                                <ChevronUp size={16} className="ml-1" />
                            ) : (
                                <ChevronDown size={16} className="ml-1" />
                            )}
                            {unreadCount > 0 && (
                                <span className="absolute -top-1 -right-1 bg-red-500 text-white rounded-full w-5 h-5 flex items-center justify-center text-xs">
                                    {unreadCount}
                                </span>
                            )}
                        </div>
                        {showNotification && (
                            <div className="absolute top-16 right-0 w-96 bg-white shadow-lg rounded-lg z-50">
                                <Notification 
                                    notifications={notifications} 
                                    setNotifications={setNotifications}
                                    setUnreadCount={setUnreadCount}
                                />
                            </div>
                        )}
                    </div>
                </div>
            </div>
        </header>
    );
}

export default CompanyHeader;
