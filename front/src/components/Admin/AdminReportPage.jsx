import React, { useEffect, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";

const BASE_URL = import.meta.env.VITE_API_BASE_URL;
const formatDate = (dateArray) => {
  if (!Array.isArray(dateArray)) return "날짜 없음";
  const [year, month, day] = dateArray;
  return `${year}-${String(month).padStart(2, "0")}-${String(day).padStart(2, "0")}`;
};

export default function AdminReportPage() {
    const [reports, setReports] = useState([]);
    const [selectedReportId, setSelectedReportId] = useState(null);
    const [reportDetail, setReportDetail] = useState(null);
    const [loadingDetail, setLoadingDetail] = useState(false);
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();
    const [readReportIds, setReadReportIds] = useState([]);

    useEffect(() => {
        const stored = JSON.parse(localStorage.getItem("readReportIds") || "[]");
        setReadReportIds(stored);
    }, []);

    useEffect(() => {
        console.log("🔄 신고 리스트 로딩 시작");
        setLoading(true);
        // 전체 리스트 조회
        axios.get(`${BASE_URL}/report/admin`, { withCredentials: true })
            .then(res => {
                console.log("✅ 신고 리스트 API 응답:", res.data);
                setReports(res.data.data || []);
            })
            .catch(err => {
                console.error("❌ 신고 리스트 조회 실패:", err);
                setReports([]);
            })
            .finally(() => setLoading(false));
    }, []);

    useEffect(() => {
        if (selectedReportId) {
            setLoadingDetail(true);
            axios.get(`${BASE_URL}/report/admin/${selectedReportId}`, { withCredentials: true })
                .then(res => {
                    setReportDetail(res.data.data);
                    setLoadingDetail(false);
                })
                .catch(err => {
                    console.error("신고 상세 조회 실패:", err);
                    setLoadingDetail(false);
                });
        }
    }, [selectedReportId]);

    const handleReportClick = (reportId) => {
        // localStorage에 읽음 처리
        const readIds = JSON.parse(localStorage.getItem("readReportIds") || "[]");
        if (!readIds.includes(reportId)) {
            const updated = [...readIds, reportId];
            localStorage.setItem("readReportIds", JSON.stringify(updated));
            setReadReportIds(updated);
        }

        navigate(`/admin/report/${reportId}`);
    };

    if (loading) return <div className="text-2xl text-[#845EC2] text-center py-20 font-semibold">로딩 중...</div>;
    if (!reports || reports.length === 0) return <div className="text-lg text-gray-400 text-center py-16">신고 내역이 없습니다.</div>;

    return (
        <div className="max-w-full mx-auto p-4 px-8">
            <h2 className="text-3xl font-extrabold mb-10 text-[#845EC2] text-center tracking-wider">신고 관리</h2>

            <div className="overflow-x-auto rounded-xxl shadow-xxl bg-white">
                <table className="min-w-full w-full table-auto border-separate border-spacing-0">
                    <thead>
                        <tr className="bg-gradient-to-r from-[#845EC2] to-[#D65DB1]">
                            <th className="p-6 text-lg text-white font-bold border-b-2 border-[#F6F1FA] min-w-[80px] text-center">번호</th>
                            <th className="p-6 text-lg text-white font-bold border-b-2 border-[#F6F1FA] min-w-[150px] text-center">신고자 이름</th>
                            <th className="p-6 text-lg text-white font-bold border-b-2 border-[#F6F1FA] min-w-[150px] text-center">신고 대상</th>
                            <th className="p-6 text-lg text-white font-bold border-b-2 border-[#F6F1FA] min-w-[250px] text-center">제목</th>
                            <th className="p-6 text-lg text-white font-bold border-b-2 border-[#F6F1FA] min-w-[120px] text-center">신고 일시</th> 
                            <th className="p-6 text-lg text-white font-bold border-b-2 border-[#F6F1FA] min-w-[120px] text-center">카테고리</th>
                       </tr>
                    </thead>
                    <tbody>
                        {reports.map(r => {
                            const isRead = readReportIds.includes(r.reportId);
                            
                            return (
                                <tr 
                                    key={r.reportId} 
                                    className={`cursor-pointer transition ${
                                        isRead 
                                            ? 'even:bg-[#F6F1FA] odd:bg-white text-[#5B5B5B] hover:bg-[#E0CFFD]' 
                                            : 'bg-[#FFF0F5] text-[#5B5B5B] hover:bg-[#FFE4E1] font-semibold'
                                    }`}
                                    onClick={() => handleReportClick(r.reportId)}
                                >
                                    <td className="p-5 text-base text-[#845EC2] font-mono text-center">{r.reportId}</td>
                                    <td className="p-5 text-base text-center">{r.reporterName}</td>
                                    <td className="p-5 text-base text-center">{r.targetName}</td>
                                    <td className="p-5 text-base text-center">{r.reportTitle}</td>
                                    <td className="p-5 text-base text-center">{formatDate(r.createdAt)}</td>
                                    <td className="p-5 text-base text-center">{r.reportCategory}</td>

                                </tr>
                            );
                        })}
                    </tbody>
                </table>
            </div>

            {/* 상세 정보 영역 */}
            {selectedReportId && (
                <div className="mt-8 p-8 bg-white rounded-xxl shadow-xxl border border-[#E0CFFD]">
                    <h3 className="text-2xl font-bold mb-6 text-[#845EC2] border-b-2 border-[#E0CFFD] pb-3">신고 상세 정보</h3>
                    {loadingDetail ? (
                        <div className="text-lg text-[#845EC2] text-center py-8">로딩 중...</div>
                    ) : (
                        reportDetail && (
                            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                                <div className="bg-[#F6F1FA] p-4 rounded-xl">
                                    <span className="block font-semibold text-[#845EC2] text-lg mb-2">신고자 이름</span>
                                    <span className="text-[#5B5B5B] text-base">{reportDetail.reporterName}</span>
                                </div>
                                <div className="bg-[#F6F1FA] p-4 rounded-xl">
                                    <span className="block font-semibold text-[#845EC2] text-lg mb-2">신고 대상</span>
                                    <span className="text-[#5B5B5B] text-base">{reportDetail.targetName}</span>
                                </div>
                                <div className="bg-[#F6F1FA] p-4 rounded-xl md:col-span-2">
                                    <span className="block font-semibold text-[#845EC2] text-lg mb-2">제목</span>
                                    <span className="text-[#5B5B5B] text-base">{reportDetail.reportTitle}</span>
                                </div>
                                <div className="bg-[#F6F1FA] p-4 rounded-xl md:col-span-2">
                                    <span className="block font-semibold text-[#845EC2] text-lg mb-2">내용</span>
                                    <div className="text-[#5B5B5B] text-base whitespace-pre-wrap leading-relaxed">
                                        {reportDetail.reportContent}
                                    </div>
                                </div>
                            </div>
                        )
                    )}
                </div>
            )}
        </div>
    );
}