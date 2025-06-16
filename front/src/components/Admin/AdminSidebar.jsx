import React, { useEffect, useState } from "react";
import { FaChartBar, FaUser, FaFlag, FaQuestionCircle } from "react-icons/fa";
import { Link, useLocation } from "react-router-dom";

const menu = [
    { icon: <FaChartBar />, text: "통계 대시보드", path: "/admin" },
    { icon: <FaFlag />, text: "신고 관리", path: "/admin/report" },
    { icon: <FaQuestionCircle />, text: "문의 관리", path: "/admin/inquiry" },
    { icon: <FaUser />, text: "회원 조회(업체)", path: "/admin/members/company" },
    { icon: <FaUser />, text: "회원 조회(유저)", path: "/admin/members/user" }
];

export default function AdminSidebar() {
    const location = useLocation();
    const [reportCount, setReportCount] = useState(0);

    useEffect(() => {
        // 🔁 전체 신고 목록 가져오기
        const reportsRaw = localStorage.getItem("allReports");
        const readIds = JSON.parse(localStorage.getItem("readReportIds") || "[]");

        try {
            const allReports = JSON.parse(reportsRaw || "[]");
            if (Array.isArray(allReports)) {
                const unread = allReports.filter(r => !readIds.includes(r.reportId));
                setReportCount(unread.length);
            } else {
                setReportCount(0);
            }
        } catch (e) {
            console.error("⚠️ 신고 목록 파싱 실패:", e);
            setReportCount(0);
        }
    }, []);

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
                                    <span className="text-xl mr-4 relative">
                                        {item.icon}
                                        {item.text === "신고 관리" && reportCount > 0 && (
                                            <span className="absolute -top-2 -right-2 bg-red-500 text-white rounded-full px-2 py-0.5 text-xs font-bold">
                                                {reportCount}
                                            </span>
                                        )}
                                    </span>
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
