import React from "react";
import Companyinfo from "../../../image/Company/SideBar/company_info.png";
import Modifyinfo from "../../../image/Company/SideBar/modify_info.png";
import Statistics from "../../../image/Company/SideBar/statistics.png";
import Setting from "../../../image/Company/SideBar/setting.png";
import Product from "../../../image/Company/SideBar/product_management.png";
import Order from "../../../image/Company/SideBar/order_management.png";
import Review from "../../../image/Company/SideBar/review_management.png";
import Work from "../../../image/Company/SideBar/work.png";
import Collaboration from "../../../image/Company/SideBar/collaboration.png";
import Chatting from "../../../image/Company/SideBar/chatting.png";
import Notification from "../../../image/Company/SideBar/notification.png";
import Schedule from "../../../image/Company/SideBar/schedule.png";
import Community from "../../../image/Company/SideBar/community.png";
import Support from "../../../image/Company/SideBar/support.png";
import Language from "../../../image/Company/SideBar/language.png";

function CompanySidebar() {
    return (
        <div className="flex justify-center items-center min-h-screen">
            <aside className="bg-white w-60 h-screen shadow-md p-4 border-r-2 border-[#845ec2] flex flex-col mx-auto">
                <div className="space-y-6">
                    <div>
                        <div className="flex items-center text-purple-600 font-bold text-lg mb-2">
                            <img src={Companyinfo} alt="Companyinfo" className="w-9 h-9 mr-2" />
                            <span>가게정보</span>
                        </div>
                        <ul className="space-y-2 text-gray-600 text-base pl-8">
                            <li className="flex items-center text-purple-600 cursor-pointer hover:text-blue-600">
                                <img src={Modifyinfo} alt="Modifyinfo" className="w-7 h-7 mr-2" />
                                정보수정
                            </li>
                            <li className="flex items-center text-purple-600 cursor-pointer hover:text-blue-600">
                                <img src={Statistics} alt="Statistics" className="w-7 h-7 mr-2" />
                                통계
                            </li>
                        </ul>
                    </div>

                    <div>
                        <div className="flex items-center text-purple-600 font-bold text-lg mb-2">
                            <img src={Setting} alt="Setting" className="w-9 h-9 mr-2" />
                            <span>관리</span>
                        </div>
                        <ul className="space-y-2 text-gray-600 text-base pl-8">
                            <li className="flex items-center text-purple-600 cursor-pointer hover:text-blue-600">
                                <img src={Product} alt="Product" className="w-7 h-7 mr-2" />
                                상품관리
                            </li>
                            <li className="flex items-center text-purple-600 cursor-pointer hover:text-blue-600">
                                <img src={Order} alt="Order" className="w-7 h-7 mr-2" />
                                주문관리
                            </li>
                            <li className="flex items-center text-purple-600 cursor-pointer hover:text-blue-600">
                                <img src={Review} alt="Review" className="w-7 h-7 mr-2" />
                                리뷰관리
                            </li>
                        </ul>
                    </div>

                    <div>
                        <div className="flex items-center text-purple-600 font-bold text-lg mb-2">
                            <img src={Work} alt="Work" className="w-9 h-9 mr-2" />
                            <span>업무</span>
                        </div>
                        <ul className="space-y-2 text-gray-600 text-base pl-8">
                            <li className="flex items-center text-purple-600 cursor-pointer hover:text-blue-600">
                                <img src={Collaboration} alt="Collaboration" className="w-7 h-7 mr-2" />
                                협업
                            </li>
                            <li className="flex items-center text-purple-600 cursor-pointer hover:text-blue-600">
                                <img src={Chatting} alt="Chatting" className="w-7 h-7 mr-2" />
                                메신저
                            </li>
                            <li className="flex items-center text-purple-600 cursor-pointer hover:text-blue-600">
                                <img src={Notification} alt="Notification" className="w-7 h-7 mr-2" />
                                알림
                            </li>
                            <li className="flex items-center text-purple-600 cursor-pointer hover:text-blue-600">
                                <img src={Schedule} alt="Schedule" className="w-7 h-7 mr-2" />
                                일정
                            </li>
                        </ul>
                    </div>
                </div>

                <div className="mt-auto space-y-4">
                    <ul className="space-y-2 text-gray-600 text-sm">
                        <li className="flex items-center text-purple-600 font-bold cursor-pointer hover:text-blue-600">
                            <img src={Community} alt="Community" className="w-9 h-9 mr-2" />
                            커뮤니티
                        </li>
                        <li className="flex items-center text-purple-600 font-bold cursor-pointer hover:text-blue-600">
                            <img src={Support} alt="Support" className="w-9 h-9 mr-2" />
                            고객지원
                        </li>
                        <li className="flex items-center text-purple-600 font-bold cursor-pointer hover:text-blue-600">
                            <img src={Language} alt="Language" className="w-9 h-9 mr-2" />
                            한국어
                        </li>
                    </ul>
                </div>
            </aside>
        </div>
    );
}

export default CompanySidebar;
