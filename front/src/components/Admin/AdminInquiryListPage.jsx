import React, { useEffect, useState } from "react";
import axios from "axios";
import { Link } from "react-router-dom";

const BASE_URL = import.meta.env.VITE_API_BASE_URL;

// 🔧 날짜 배열 -> YYYY-MM-DD 포맷 문자열 변환 함수
const formatDate = (dateArray) => {
  if (!Array.isArray(dateArray)) return "날짜 없음";
  const [year, month, day] = dateArray;
  return `${year}-${String(month).padStart(2, "0")}-${String(day).padStart(2, "0")}`;
};

// 🔧 상태 enum → 한글 변환 맵
const statusLabelMap = {
  IN_PROGRESS: "진행중",
  COMPLETED: "완료",
};

export default function AdminInquiryListPage() {
  const [inquiries, setInquiries] = useState([]);

  useEffect(() => {
    axios.get(`${BASE_URL}/inquiry/admin/list`, { withCredentials: true })
      .then((res) => {
        console.log("✅ 관리자용 문의 목록 응답:", res.data);
        setInquiries(res.data.data || []);
      })
      .catch((err) => {
        console.error("❌ 문의 목록 불러오기 실패", err);
      });
  }, []);

  return (
    <div className="w-full">
      <h2 className="text-2xl font-bold mb-6">📨 문의 목록</h2>
      <table className="w-full border border-gray-300">
        <thead className="bg-[#F6F1FA]">
          <tr>
            <th className="p-3">ID</th>
            <th className="p-3">제목</th>
            <th className="p-3">작성자 이메일</th>
            <th className="p-3">상태</th>
            <th className="p-3">작성일</th>
          </tr>
        </thead>
        <tbody>
          {inquiries.length === 0 ? (
            <tr><td colSpan="5" className="text-center py-6 text-gray-500">문의가 없습니다.</td></tr>
          ) : (
            inquiries.map((inquiry) => (
              <tr key={inquiry.inquiryId} className="text-center border-t">
                <td className="p-3">{inquiry.inquiryId}</td>
                <td className="p-3">
                  <Link to={`/admin/inquiry/${inquiry.inquiryId}`} className="text-blue-600 underline">
                    {inquiry.title}
                  </Link>
                </td>
                <td className="p-3">{inquiry.userEmail}</td>
                <td className="p-3">{statusLabelMap[inquiry.status] || inquiry.status}</td>
                <td className="p-3">{formatDate(inquiry.createdAt)}</td>
              </tr>
            ))
          )}
        </tbody>
      </table>
    </div>
  );
}
