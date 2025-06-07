import React, { useState } from "react";
import { FaChartBar, FaUser, FaFlag, FaQuestionCircle, FaCog } from "react-icons/fa";
import { Link, useLocation } from "react-router-dom";

const menu = [
    { icon: <FaChartBar />, text: "통계 대시보드", path: "/admin" },
    { icon: <FaFlag />, text: "신고 관리", path: "/admin/report" },
    { icon: <FaQuestionCircle />, text: "문의 관리", path: "/admin/inquiry" },
    { icon: <FaUser />, text: "회원 관리", path: "/admin/user" },
    { icon: <FaCog />, text: "설정", path: "/admin/setting" }
];

export default function AdminSidebar() {
    const location = useLocation();
    return (
        <aside className="fixed top-0 left-0 h-full w-60 bg-white border-r border-gray-200 shadow-lg z-30 flex flex-col">
            <div className="text-center py-7 border-b border-gray-200">
                <span className="text-3xl font-bold text-[#845EC2] tracking-wide">Admin</span>
            </div>
            <nav className="flex-1 py-8">
                <ul className="space-y-3">
                    {menu.map(item => (
                        <li key={item.path}>
                            <Link to={item.path}>
                                <div className={`flex items-center px-7 py-3 rounded-xl transition-all 
                                    ${location.pathname === item.path 
                                        ? "bg-[#845EC2] text-white font-bold" 
                                        : "hover:bg-[#E0CFFD] text-[#845EC2]"}`}>
                                    <span className="text-xl mr-4">{item.icon}</span>
                                    <span>{item.text}</span>
                                </div>
                            </Link>
                        </li>
                    ))}
                </ul>
            </nav>
        </aside>
    );
}
