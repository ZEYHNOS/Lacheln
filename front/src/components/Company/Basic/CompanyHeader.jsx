import React from "react";
import { Link, useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import Notification from "../../../image/Company/Header/notification.png";
import Chatting from "../../../image/Company/Header/chatting.png";
import { ChevronDown, LogOut } from "lucide-react"; 
import apiClient from "../../../lib/apiClient";

function CompanyHeader() {
    const navigate = useNavigate();

    const handleLogout = async () => {
        try {
            const response = await apiClient.post("/userlogout");
            if (response.data.result.resultCode === 201) {
                toast.success("안전하게 로그아웃 되었습니다.", {
                    position: "top-center",
                    autoClose: 1000,
                });
                navigate("/login");
            }
        } catch (error) {
            console.error("로그아웃 중 오류 발생:", error);
            toast.error("로그아웃 중 오류가 발생했습니다. 다시 시도해주세요.", {
                position: "top-center",
                autoClose: 1000,
            });
        }
    };

    return (
        <header className="bg-white flex flex-col justify-between items-center relative border-b-2 border-[#845ec2] w-full py-8">
            <div className="flex justify-between items-center w-full px-6">
                <div className="w-16"></div>
                <Link to="/company" className="text-center my-1">
                    <h1 className="text-4xl font-inknut font-semi text-[#845ec2]">Lächeln</h1>
                    <p className="text-[#845ec2] text-sm">스튜디오 드레스 메이크업</p>
                </Link>
                <div className="flex items-center gap-4 text-purple-600">
                    <button 
                        onClick={handleLogout}
                        className="flex items-center gap-1 bg-white hover:bg-gray-100 p-2 rounded-lg transition"
                    >
                        <LogOut size={20} />
                        <span className="text-sm">로그아웃</span>
                    </button>
                    <div className="relative cursor-pointer flex items-center">
                        <img src={Notification} alt="Notification" className="w-10 h-10" />
                        <ChevronDown size={16} className="ml-1" />
                    </div>
                </div>
            </div>
        </header>
    );
}

export default CompanyHeader;
