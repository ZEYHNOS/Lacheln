import React from "react";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import apiClient from "../../lib/apiClient";

function AdminHeader() {
    const navigate = useNavigate();

    const handleLogout = async () => {
        try {
            const response = await apiClient.post("/userlogout");
            if (response.data.result.resultCode === 201) {
                toast.success("로그아웃되었습니다.", {
                    position: "top-center",
                    autoClose: 1000,
                });
                navigate("/");
            }
        } catch (error) {
            toast.error("로그아웃 실패. 다시 시도해 주세요.", {
                position: "top-center",
                autoClose: 1000,
            });
        }
        
        
    };

    return (
        <header className="h-20 flex items-center px-10 bg-white shadow-md border-b justify-between">
            <h1 className="text-2xl font-bold text-[#845EC2]">관리자 페이지</h1>
            <button
                onClick={handleLogout}
                className="bg-pink-500 hover:bg-pink-600 text-white px-4 py-2 rounded"
            >
                로그아웃
            </button>
        </header>
    );
}

export default AdminHeader;
