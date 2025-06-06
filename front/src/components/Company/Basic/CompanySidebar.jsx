import React from "react";
import { Link, useLocation } from "react-router-dom";

import Companyinfo from "../../../image/Company/SideBar/company_info.png";
import Modifyinfo from "../../../image/Company/SideBar/modify_info.png";
import Statistics from "../../../image/Company/SideBar/statistics.png";
import Management from "../../../image/Company/SideBar/management.png";
import Product from "../../../image/Company/SideBar/product_management.png";
import Order from "../../../image/Company/SideBar/order_management.png";
import Review from "../../../image/Company/SideBar/review_management.png";
import Work from "../../../image/Company/SideBar/work.png";
import Collaboration from "../../../image/Company/SideBar/collaboration.png";
import Messenger from "../../../image/Company/SideBar/messenger.png";
import Notification from "../../../image/Company/SideBar/notification.png";
import Schedule from "../../../image/Company/SideBar/schedule.png";
import Community from "../../../image/Company/SideBar/community.png";
import Support from "../../../image/Company/SideBar/support.png";
import Language from "../../../image/Company/SideBar/language.png";

const BASE_PATH = "/company";

const menuSections = [
    {
        title: "가게정보",
        icon: Companyinfo,
        items: [
            { path: "modifyinfo", icon: Modifyinfo, label: "정보수정" },
            { path: "statistics", icon: Statistics, label: "통계" },
        ],
    },
    {
        title: "관리",
        icon: Management,
        items: [
            { path: "product", icon: Product, label: "상품관리" },
            { path: "order", icon: Order, label: "주문관리" },
            { path: "review", icon: Review, label: "리뷰관리" },
        ],
    },
    {
        title: "업무",
        icon: Work,
        items: [
            { path: "collaboration", icon: Collaboration, label: "협업" },
            { path: "messenger", icon: Messenger, label: "메신저" },
            { path: "notification", icon: Notification, label: "알림" },
            { path: "schedule", icon: Schedule, label: "일정" },
        ],
    },
];

const bottomMenu = [
    { icon: Community, label: "커뮤니티" },
    { icon: Support, label: "고객지원" },
    { icon: Language, label: "한국어" },
];

function CompanySidebar() {
    const location = useLocation();

    return (
        <aside className="bg-white w-60 h-full shadow-md p-4 border-r-2 border-purple-600 flex flex-col overflow-y-auto custom-scroll">
            <div className="space-y-8 flex-grow">
                {menuSections.map((section) => (
                    <div key={section.title} className="mb-6">
                        <div className="flex items-center text-purple-600 font-bold text-lg mb-2">
                            <img src={section.icon} alt={section.title} className="w-9 h-9 mr-2" />
                            <span>{section.title}</span>
                        </div>
                        <ul className="space-y-3 text-gray-600 text-base pl-8">
                            {section.items.map((item) => {
                                const isActive = location.pathname.includes(`${BASE_PATH}/${item.path}`);
                                return (
                                    <Link
                                        key={item.path}
                                        to={`${BASE_PATH}/${item.path}`}
                                        className={`flex items-center text-purple-600 cursor-pointer rounded-md transition-all 
                                        ${isActive ? "bg-green-200 text-purple-700 font-bold" : "hover:text-blue-600"}`}
                                    >
                                        <img src={item.icon} alt={item.label} className="w-7 h-7 mr-2" />
                                        {item.label}
                                    </Link>
                                );
                            })}
                        </ul>
                    </div>
                ))}
            </div>

            <div className="mt-12 space-y-6">
                <ul className="space-y-2 text-gray-600 text-sm">
                    {bottomMenu.map((item) => (
                        <li key={item.label} className="flex items-center text-purple-600 font-bold cursor-pointer hover:text-blue-600">
                            <img src={item.icon} alt={item.label} className="w-9 h-9 mr-2" />
                            {item.label}
                        </li>
                    ))}
                </ul>
            </div>
        </aside>
    );
}


export default CompanySidebar;
