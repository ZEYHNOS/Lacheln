import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import axios from "axios";
import { toast } from "react-toastify";

const BASE_URL = import.meta.env.VITE_API_BASE_URL;

export default function AdminInquiryDetailPage() {
  const { inquiryId } = useParams();
  const navigate = useNavigate();
  const [inquiry, setInquiry] = useState(null);
  const [answer, setAnswer] = useState("");

  // ✅ 한글 변환용 맵
  const categoryLabelMap = {
    ACCOUNT: "계정",
    PAYMENT: "결제",
    ADVERTISEMENT: "광고",
    EVENT: "이벤트",
  };

  const statusLabelMap = {
    IN_PROGRESS: "진행중",
    COMPLETED: "완료",
  };

  // ✅ 작성일 포맷 함수
  const formatCreatedAt = (isoString) => {
    if (!isoString) return "날짜 없음";
    const date = new Date(isoString);
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, "0");
    const day = String(date.getDate()).padStart(2, "0");
    const hour = String(date.getHours()).padStart(2, "0");
    const min = String(date.getMinutes()).padStart(2, "0");
    return `${year}.${month}.${day} ${hour}:${min}`;
  };

  useEffect(() => {
    axios.get(`${BASE_URL}/inquiry/admin/${inquiryId}`, { withCredentials: true })
      .then((res) => {
        setInquiry(res.data.data);
        setAnswer(res.data.data.answer || "");
      })
      .catch(() => toast.error("❌ 문의 상세 조회 실패"));
  }, [inquiryId]);

  const handleSubmit = () => {
    if (!answer.trim()) {
      toast.warning("답변 내용을 입력해주세요.");
      return;
    }

    axios.post(`${BASE_URL}/inquiry/admin/${inquiryId}/answer`, {
      answer
    }, { withCredentials: true })
      .then(() => {
        toast.success("✅ 답변 등록 완료");
        navigate("/admin/inquiry");
      })
      .catch(() => toast.error("❌ 답변 등록 실패"));
  };

  if (!inquiry) return <div className="p-6">⏳ 로딩 중...</div>;

  return (
    <div className="max-w-3xl mx-auto">
      <h2 className="text-2xl font-bold mb-6">📄 문의 상세</h2>
      <div className="border p-5 rounded shadow space-y-3 bg-white">
        <p><strong>제목:</strong> {inquiry.title}</p>
        <p><strong>카테고리:</strong> {categoryLabelMap[inquiry.category] || inquiry.category}</p>
        <p><strong>상태:</strong> {statusLabelMap[inquiry.status] || inquiry.status}</p>
        <p><strong>작성자:</strong> {inquiry.userEmail}</p>
        <p><strong>작성일:</strong> {formatCreatedAt(inquiry.createdAt)}</p>

        <div>
          <strong>내용:</strong>
          <div
            className="p-3 bg-gray-100 rounded mt-1 text-gray-800 whitespace-pre-wrap"
            dangerouslySetInnerHTML={{ __html: inquiry.content }}
          />
        </div>

        <div className="mt-6">
          <label className="block font-semibold mb-1">답변</label>
          <textarea
            value={answer}
            onChange={(e) => setAnswer(e.target.value)}
            className="w-full border p-3 rounded h-32"
          />
          <button
            onClick={handleSubmit}
            className="mt-3 bg-[#845EC2] text-white px-4 py-2 rounded hover:bg-[#6c49a3]"
          >
            답변 등록
          </button>
        </div>
      </div>
    </div>
  );
}
