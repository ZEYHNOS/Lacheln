import React, { useEffect, useState } from "react";
import axios from "axios";
import { useParams, useNavigate } from "react-router-dom";

const BASE_URL = import.meta.env.VITE_API_BASE_URL;

export default function ReportDetailPage() {
    const { reportId } = useParams();
    const [report, setReport] = useState(null);
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();

    useEffect(() => {
        axios.get(`${BASE_URL}/report/admin/${reportId}`, { withCredentials: true })
            .then(res => {
                console.log("backend data", res.data.data)
                setReport(res.data.data);
                setLoading(false);
            })
            .catch(err => {
                console.error("신고 상세 조회 실패:", err);
                setLoading(false);
            });
    }, [reportId]);

    if (loading) return <div>로딩 중...</div>;
    if (!report) return <div>신고 정보를 불러올 수 없습니다.</div>;

    return (
        <div className="max-w-xl mx-auto p-6 bg-[#F6F1FA] rounded-xl shadow">
        <h2 className="text-xl font-bold mb-6 text-[#845EC2]">신고 상세 정보</h2>

            <div className="mb-4">
                <span className="font-semibold text-[#845EC2]">신고자 이름: </span>
                <span className="text-black">{report.reporterName}</span>
            </div>
            <div className="mb-4">
                <span className="font-semibold text-[#845EC2]">신고 대상: </span>
                <span className="text-black">{report.targetName}</span>
            </div>
            <div className="mb-4">
                <span className="font-semibold text-[#845EC2]">제목: </span>
                <span className="text-black">{report.reportTitle}</span>
            </div>
            <div className="mb-4">
                <span className="font-semibold text-[#845EC2]">내용: </span>
                <span className="text-black">{report.reportContent}</span>
            </div>
            <div className="mb-4">
                <span className="font-semibold text-[#845EC2]">신고 시간: </span>
                <span className="text-black">
                    {Array.isArray(report.createdAt)
                        ? report.createdAt.join("-")
                        : report.createdAt}
                </span>
            </div>
            <div className="mb-4">
                <span className="font-semibold text-[#845EC2]">업체ID: </span>
                <span className="text-black">{report.cpId}</span>
            </div>
            <div className="mb-4">
                <span className="font-semibold text-[#845EC2]">유저ID: </span>
                <span className="text-black">{report.userId}</span>
            </div>
            
            {/* 필요한 추가 정보가 있으면 여기에 더 표시 */}
            <button
                className="mt-6 px-4 py-2 bg-[#845EC2] text-white rounded"
                onClick={() => navigate(-1)}
            >
                목록으로 돌아가기
            </button>
        </div>
    );
}
