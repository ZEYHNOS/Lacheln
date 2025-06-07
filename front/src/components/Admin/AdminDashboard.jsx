import React from "react";
import { FaFlag, FaUser, FaQuestionCircle } from "react-icons/fa";

export default function AdminDashboard() {
    // (예시 값. 실제 데이터는 fetch해서 사용 가능)
    const stats = [
        { icon: <FaFlag className="text-3xl text-[#845EC2]" />, label: "오늘 신고", value: 5 },
        { icon: <FaQuestionCircle className="text-3xl text-[#845EC2]" />, label: "오늘 문의", value: 8 },
        { icon: <FaUser className="text-3xl text-[#845EC2]" />, label: "신규 회원", value: 12 }
    ];

    return (
        <div>
            <h2 className="text-xl font-semibold mb-8">관리자 통계 대시보드</h2>
            <div className="grid grid-cols-1 md:grid-cols-3 gap-8 mb-12">
                {stats.map((item, idx) => (
                    <div key={idx} className="flex items-center bg-[#F6F1FA] rounded-xl shadow p-6">
                        <div className="mr-6">{item.icon}</div>
                        <div>
                            <div className="text-lg font-medium">{item.label}</div>
                            <div className="text-2xl font-bold text-[#845EC2]">{item.value}</div>
                        </div>
                    </div>
                ))}
            </div>
            {/* 차트 예시 */}
            <div className="bg-white border rounded-xl p-6 shadow">
                <div className="text-lg mb-3 font-semibold">월별 가입자 수 (예시 차트)</div>
                <div className="w-full flex items-end space-x-4 h-40">
                    {/* Fake chart: replace with chart library as needed */}
                    {[18, 26, 34, 23, 41, 37, 30].map((val, idx) => (
                        <div key={idx} className="flex flex-col items-center">
                            <div className="bg-[#845EC2] w-8" style={{ height: `${val * 2}px` }} />
                            <span className="text-xs mt-2">{idx + 1}월</span>
                        </div>
                    ))}
                </div>
            </div>
        </div>
    );
}
