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
    EVENT: "이벤트"
  };

  const statusLabelMap = {
    IN_PROGRESS: "진행중",
    COMPLETED: "완료"
  };

  const formatCreatedAt = (arr) => {
    if (!Array.isArray(arr)) return "날짜 없음";
    const [y, m, d, h = 0, min = 0] = arr;
    return `${y}.${String(m).padStart(2, '0')}.${String(d).padStart(2, '0')} ${String(h).padStart(2, '0')}:${String(min).padStart(2, '0')}`;
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
    <div className="p-6 max-w-2xl mx-auto">
      <h1 className="text-lg font-bold mb-4">📄 문의 상세 정보</h1>

      <div className="bg-gray-50 border rounded-md p-4 shadow-sm">
        <h2 className="text-2xl font-bold text-purple-800 mb-2">{inquiry.title}</h2>

        <p className="text-sm text-gray-500 mb-1">
          📅 작성일: {formatCreatedAt(inquiry.createdAt)}
        </p>
        <p className="text-sm text-gray-600 mb-2">
          📂 카테고리: {categoryLabelMap[inquiry.category] || inquiry.category} /
          📌 상태: {statusLabelMap[inquiry.status] || inquiry.status}
        </p>

        <hr className="my-3" />

        <div
          className="text-gray-800 whitespace-pre-wrap"
          dangerouslySetInnerHTML={{ __html: inquiry.content }}
        />
      </div>

      {/* 디버깅용 JSON */}
      <div className="mt-6">
        <h3 className="text-sm font-semibold text-gray-500">[디버깅용 JSON]</h3>
        <pre className="bg-gray-100 text-xs p-3 border rounded text-gray-700 overflow-auto">
          {JSON.stringify(inquiry, null, 2)}
        </pre>
      </div>
    </div>
  );
}
