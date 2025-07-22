import React, { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import AdminSidebar from "./AdminSidebar";
import AdminHeader from "./AdminHeader";
import apiClient from "../../lib/apiClient"; // 실제 경로로!
import { toast } from "react-toastify";

export default function AdminLayout({ children }) {
  const [isLoggedIn, setIsLoggedIn] = useState(null);
  const location = useLocation();
  const navigate = useNavigate();

  useEffect(() => {
    const isFromLogin = document.referrer.includes('/login');

    apiClient
      .get("/auth/me")
      .then(res => {
        const isNowLoggedIn = res.data.data?.role === "ADMIN";
        setIsLoggedIn(isNowLoggedIn);

        if (!isNowLoggedIn) {
          navigate("/login");
        }

        if (isFromLogin && isNowLoggedIn) {
          toast.success("소셜 로그인이 완료되었습니다!", {
            position: "top-center",
            autoClose: 1000,
          });
        }
      })
      .catch(() => {
        setIsLoggedIn(false);
        navigate("/");
      });
  }, [location, navigate]);

  if (isLoggedIn === null) return null;

  return (
    <div className="min-h-screen flex bg-gray-50">
      <AdminSidebar />
      <div className="flex-1 ml-60 flex flex-col">
        <AdminHeader />
        <main className="flex-1 w-full h-auto p-10">
          {children}
        </main>
      </div>
    </div>
  );
}
