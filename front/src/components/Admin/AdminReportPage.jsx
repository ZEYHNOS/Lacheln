import React, { useEffect, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";

const BASE_URL = import.meta.env.VITE_API_BASE_URL;

export default function AdminReportPage() {
    const [reports, setReports] = useState([]);
    const [selectedReportId, setSelectedReportId] = useState(null);
    const [reportDetail, setReportDetail] = useState(null);
    const [loadingDetail, setLoadingDetail] = useState(false);;
    const navigate = useNavigate();

    useEffect(() => {
        // 전체 리스트 조회
        axios.get(`${BASE_URL}/report/admin`, { withCredentials: true })
            .then(res => {
                setReports(res.data.data || []);
            })
            .catch(err => {
                console.error("신고 리스트 조회 실패:", err);
            });
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

    return (
        <div>
            <h2 className="text-xl font-bold mb-6 text-[#845EC2]">신고 관리</h2>
            <table className="w-full text-left border border-[#E0CFFD] bg-[#F6F1FA] rounded-xl shadow">
                <thead>
                    <tr className="bg-[#E0CFFD] border-b border-[#E0CFFD]">
                        <th className="p-3 text-[#845EC2]">번호</th>
                        <th className="p-3 text-[#845EC2]">신고자 이름</th>
                        <th className="p-3 text-[#845EC2]">신고 대상</th>
                        <th className="p-3 text-[#845EC2]">제목</th>
                        <th className="p-3 text-[#845EC2]">상태태</th>

                    </tr>
                </thead>
                <tbody>
                    {reports.map(r => (
                        <tr
                            key={r.reportId}
                            className="border-b border-[#E0CFFD] hover:bg-[#E0CFFD] cursor-pointer transition"
                            onClick={() => setSelectedReportId(r.reportId)}
                        >
                            <td className="p-3 text-[#845EC2] font-semibold">{r.reportId}</td>
                            <td className="p-3 text-[#845EC2] font-semibold">{r.reporterName}</td>
                            <td className="text-[#845EC2] font-semibold">{r.targetName}</td>
                            <td className="text-[#845EC2] font-semibold">{r.reportTitle}</td>
                            <td className="text-[#845EC2] font-semibold">{r.reportStatus}</td>
                            <td>
                                <button
                                    className="px-3 py-1 rounded bg-[#845EC2] text-white"
                                    onClick={() => navigate(`/admin/report/${r.reportId}`)}
                                >
                                    상세 보기
                                </button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
            {reports.length === 0 && (
                <div className="mt-6 text-center text-[#845EC2]">신고 내역이 없습니다.</div>
            )}
            {/* 상세 정보 영역 */}
            {selectedReportId && (
                <div className="mt-8 p-6 bg-[#F6F1FA] rounded-xl shadow">
                    <h3 className="text-lg font-bold mb-4 text-[#845EC2]">신고 상세 정보</h3>
                    {loadingDetail ? (
                        <div>로딩 중...</div>
                    ) : (
                        reportDetail && (
                            <>
                                <div className="mb-3">
                                    <span className="font-semibold text-[#845EC2]">신고자 이름: </span>
                                    <span className="text-black">{reportDetail.reporterName}</span>
                                </div>
                                <div className="mb-3">
                                    <span className="font-semibold text-[#845EC2]">신고 대상: </span>
                                    <span className="text-black">{reportDetail.targetName}</span>
                                </div>
                                <div className="mb-3">
                                    <span className="font-semibold text-[#845EC2]">제목: </span>
                                    <span className="text-black">{reportDetail.reportTitle}</span>
                                </div>
                                <div className="mb-3">
                                    <span className="font-semibold text-[#845EC2]">내용: </span>
                                    <span className="text-black">{reportDetail.reportContent}</span>
                                </div>   
                                {/* 추가 정보가 있다면 여기에 표시 */}
                            </>
                            
                        )
                        


                    )}
                </div>
            )}
        </div>
    );
}
