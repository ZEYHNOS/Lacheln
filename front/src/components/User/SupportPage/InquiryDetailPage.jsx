import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import apiClient from "../../../lib/apiClient";
import { toast } from "react-toastify";

export default function InquiryDetailPage() {
  const { id } = useParams();
  const [inquiry, setInquiry] = useState(null);
  const [error, setError] = useState("");

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

  const formatCreatedAt = (value) => {
    if (Array.isArray(value)) {
      const [y, m, d, h = 0, min = 0] = value;
      return `${y}.${String(m).padStart(2, "0")}.${String(d).padStart(2, "0")} ${String(h).padStart(2, "0")}:${String(min).padStart(2, "0")}`;
    }

    if (typeof value === "string") {
      const date = new Date(value);
      if (isNaN(date.getTime())) return "날짜 오류";
      return `${date.getFullYear()}.${String(date.getMonth() + 1).padStart(2, "0")}.${String(date.getDate()).padStart(2, "0")} ${String(date.getHours()).padStart(2, "0")}:${String(date.getMinutes()).padStart(2, "0")}`;
    }

    return "날짜 없음";
  };

  useEffect(() => {
    apiClient.get(`/inquiry/${id}`)
      .then((res) => {
        if (res.data?.data) {
          setInquiry(res.data.data);
        } else {
          toast.error("데이터가 비어 있습니다.");
          setError("데이터 없음");
        }
      })
      .catch((err) => {
        console.error("❌ 상세 조회 실패", err);
        toast.error("문의 내용을 불러오지 못했습니다.");
        setError("조회 실패");
      });
  }, [id]);

  if (error) {
    return (
      <div className="p-6 max-w-2xl mx-auto text-red-500">
        <h1>❌ 에러 발생</h1>
        <p>{error}</p>
      </div>
    );
  }

  if (!inquiry) {
    return (
      <div className="p-6 max-w-2xl mx-auto text-gray-500">
        <p>⏳ 로딩 중...</p>
      </div>
    );
  }

  return (
    <div className="p-6 max-w-2xl mx-auto bg-white text-black rounded shadow">
      <h1 className="text-lg font-bold mb-4">📄 문의 상세 정보</h1>

      <div className="bg-gray-50 border rounded-md p-4 shadow-sm">
        <h2 className="text-2xl font-bold text-[#845EC2] mb-2">{inquiry.title}</h2>

        <p className="text-sm text-gray-600 mb-1">
          📅 작성일: {formatCreatedAt(inquiry.createdAt)}
        </p>
        <p className="text-sm text-gray-700 mb-2">
          📂 카테고리: {categoryLabelMap[inquiry.category] || inquiry.category} /{" "}
          📌 상태: {statusLabelMap[inquiry.status] || inquiry.status}
        </p>

        <hr className="my-3" />

        <div
          className="text-sm text-gray-800 whitespace-pre-wrap bg-white p-2 rounded"
          dangerouslySetInnerHTML={{ __html: inquiry.content }}
        />

        {/* ✅ 답변 영역 */}
        <div className="mt-6">
          <h3 className="text-md font-semibold mb-2">📬 답변</h3>
          {inquiry.answer ? (
            <div className="bg-purple-100 p-3 rounded text-gray-800 whitespace-pre-line">
              {inquiry.answer}
            </div>
          ) : (
            <p className="text-gray-500">아직 답변이 등록되지 않았습니다.</p>
          )}
        </div>
      </div>
    </div>
  );
}
