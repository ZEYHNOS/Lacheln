import React, { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import AdminSidebar from "./AdminSidebar";
import Footer from "../Tool/Footer";
import AdminHeader from "./AdminHeader";
import apiClient from "../../lib/apiClient"; // 실제 경로로!
import { toast } from "react-toastify";

export default function AdminLayout({ children }) {
    const [isLoggedIn, setIsLoggedIn] = useState(null);
    const location = useLocation();
    const navigate = useNavigate();

    useEffect(() => {
        // /login에서 왔는지 확인
        const isFromLogin = document.referrer.includes('/login');

        apiClient.get("/auth/me")
            .then(res => {
                const isNowLoggedIn = !!res.data.data?.valid;
                setIsLoggedIn(isNowLoggedIn);

                if (!isNowLoggedIn) {
                    // 로그인 안 돼 있으면 관리자 로그인 페이지로 리다이렉트
                    navigate("/admin/login");
                }

                // /login에서 왔고, 로그인된 상태라면 토스트 표시
                if (isFromLogin && isNowLoggedIn) {
                    toast.success("소셜 로그인이 완료되었습니다!", {
                        position: "top-center",
                        autoClose: 1000,
                    });
                }
            })
            .catch(() => {
                setIsLoggedIn(false);
                navigate("/admin/login");
            });
    }, [location, navigate]);

    // 인증 체크 중일 때 아무것도 렌더링하지 않음 (선택)
    if (isLoggedIn === null) return null;

    return (
        <div className="min-h-screen flex bg-gray-50">
            <AdminSidebar />
            <div className="flex-1 ml-60 flex flex-col">
                <AdminHeader />
                <main className="flex-1 p-10">
                    <div className="bg-white rounded-2xl shadow-lg p-10 min-h-[60vh]">
                        {children}
                    </div>
                </main>
               
            </div>
        </div>
    );
}
