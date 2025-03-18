import React from "react";
import { Link } from "react-router-dom";
import Notification from "../../../image/Company/Header/notification.png";
import Chatting from "../../../image/Company/Header/chatting.png";
import { ChevronDown } from "lucide-react"; 

function CompanyHeader() {
    return (
        <header className="bg-white shadow-md flex flex-col justify-between items-center relative border-b-2 border-[#845ec2] w-full py-8">
            <div className="flex justify-between items-center w-full px-6">
                <div className="w-16"></div>
                <Link to="/company" className="text-center my-1">
                    <h1 className="text-4xl font-inknut font-semi text-[#845ec2]">Lächeln</h1>
                    <p className="text-[#845ec2] text-sm">스튜디오 드레스 메이크업</p>
                </Link>
                <div className="flex items-center gap-2 text-purple-600">
                    <div className="relative cursor-pointer flex items-center">
                        <img src={Notification} alt="Notification" className="w-10 h-10" />
                        <ChevronDown size={16} className="ml-1" />
                    </div>
                    <div className="relative cursor-pointer flex items-center">
                        <img src={Chatting} alt="Chatting" className="w-10 h-10" />
                        <ChevronDown size={16} className="ml-1" />
                    </div>
                </div>
            </div>
        </header>
    );
}

export default CompanyHeader;
