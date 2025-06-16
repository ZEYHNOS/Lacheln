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
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    console.log("🔄 문의 리스트 로딩 시작");
    setLoading(true);
    axios.get(`${BASE_URL}/inquiry/admin/list`, { withCredentials: true })
      .then((res) => {
        console.log("✅ 관리자용 문의 목록 응답:", res.data);
        setInquiries(res.data.data || []);
      })
      .catch((err) => {
        console.error("❌ 문의 목록 불러오기 실패", err);
        setInquiries([]);
      })
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <div className="text-2xl text-[#845EC2] text-center py-20 font-semibold">로딩 중...</div>;
  if (!inquiries || inquiries.length === 0) return <div className="text-lg text-gray-400 text-center py-16">문의 내역이 없습니다.</div>;

  return (
    <div className="max-w-full mx-auto p-4 px-8">
      <h2 className="text-3xl font-extrabold mb-10 text-[#845EC2] text-center tracking-wider">문의 관리</h2>

      <div className="overflow-x-auto rounded-xxl shadow-xxl bg-white">
        <table className="min-w-full w-full table-auto border-separate border-spacing-0">
          <thead>
            <tr className="bg-gradient-to-r from-[#845EC2] to-[#D65DB1]">
              <th className="p-6 text-lg text-white font-bold border-b-2 border-[#F6F1FA] min-w-[80px] text-center">ID</th>
              <th className="p-6 text-lg text-white font-bold border-b-2 border-[#F6F1FA] min-w-[300px] text-center">제목</th>
              <th className="p-6 text-lg text-white font-bold border-b-2 border-[#F6F1FA] min-w-[200px] text-left">작성자 이메일</th>
              <th className="p-6 text-lg text-white font-bold border-b-2 border-[#F6F1FA] min-w-[120px] text-center">상태</th>
              <th className="p-6 text-lg text-white font-bold border-b-2 border-[#F6F1FA] min-w-[120px] text-center">작성일</th>
            </tr>
          </thead>
          <tbody>
            {inquiries.map((inquiry) => (
              <tr 
                key={inquiry.inquiryId} 
                className="even:bg-[#F6F1FA] odd:bg-white text-[#5B5B5B] hover:bg-[#E0CFFD] transition"
              >
                <td className="p-5 text-base text-[#845EC2] font-mono text-center">{inquiry.inquiryId}</td>
                <td className="p-5 text-base text-center">
                  <Link 
                    to={`/admin/inquiry/${inquiry.inquiryId}`} 
                    className="text-[#845EC2] hover:text-[#6C51B4] font-semibold hover:underline transition-colors"
                  >
                    {inquiry.title}
                  </Link>
                </td>
                <td className="p-5 text-base text-left">{inquiry.userEmail}</td>
                <td className="p-5 text-base text-center">
                  <span className={`px-3 py-1 rounded-full text-sm font-semibold ${
                    inquiry.status === 'IN_PROGRESS' ? 'bg-blue-100 text-blue-800' :
                    inquiry.status === 'COMPLETED' ? 'bg-green-100 text-green-800' :
                    'bg-gray-100 text-gray-800'
                  }`}>
                    {statusLabelMap[inquiry.status] || inquiry.status}
                  </span>
                </td>
                <td className="p-5 text-base text-center">{formatDate(inquiry.createdAt)}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}