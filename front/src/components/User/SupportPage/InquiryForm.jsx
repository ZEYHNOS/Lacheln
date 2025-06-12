import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import ReactQuill from "react-quill";
import { toast } from "react-toastify";
import apiClient from "../../../lib/apiClient"; // ✅ axios 설정된 client
import "react-quill/dist/quill.snow.css";
import { FaPaperPlane } from "react-icons/fa";

export default function InquiryForm() {
  const navigate = useNavigate();

  // 📌 Form 상태
  const [title, setTitle] = useState("");
  const [category, setCategory] = useState("ACCOUNT");
  const [content, setContent] = useState("");

  // 📌 문의 카테고리 목록 (백엔드 enum 기준)
  const categories = [
    { value: "ACCOUNT", label: "계정" },
    { value: "PAYMENT", label: "결제" },
    { value: "ADVERTISEMENT", label: "광고" },
    { value: "EVENT", label: "이벤트" }
  ];

  // 📌 등록 버튼 클릭
  const handleSubmit = async () => {
    if (!title || !content) {
      toast.error("제목과 내용을 모두 입력해주세요.");
      return;
    }

    try {
      await apiClient.post("/inquiry", {
        inquiryTitle: title,
        inquiryCategory: category,
        inquiryContent: content
      });
      toast.success("문의가 등록되었습니다.");
      navigate("/support");
    } catch (err) {
      console.error(err);
      toast.error("문의 등록에 실패했습니다.");
    }
  };

  return (
    <div className="mx-auto w-full max-w-4xl p-8 border border-[#845EC2] rounded">
      <h2 className="text-[25px] text-[#845EC2] font-bold mb-6">문의 작성</h2>

      {/* 제목 */}
      <label className="block mb-2 text-[18px] font-semibold">제목</label>
      <input
        type="text"
        value={title}
        onChange={(e) => setTitle(e.target.value)}
        className="w-full h-[40px] border border-[#845EC2] rounded px-3 mb-4"
        placeholder="제목을 입력하세요"
      />

      {/* 카테고리 */}
      <label className="block mb-2 text-[18px] font-semibold">카테고리</label>
      <select
        value={category}
        onChange={(e) => setCategory(e.target.value)}
        className="w-full h-[40px] border border-[#845EC2] rounded px-3 mb-4"
      >
        {categories.map((c) => (
          <option key={c.value} value={c.value}>
            {c.label}
          </option>
        ))}
      </select>

      {/* 본문 */}
      <label className="block mb-2 text-[18px] font-semibold">내용</label>
      <ReactQuill
        value={content}
        onChange={setContent}
        className="mb-6"
        placeholder="문의 내용을 입력해주세요."
      />

      {/* 제출 버튼 */}
      <button
        onClick={handleSubmit}
        className="flex items-center gap-2 bg-[#845EC2] text-white px-6 py-2 rounded hover:bg-[#6d44a5] transition"
      >
        <FaPaperPlane />
        등록하기
      </button>
    </div>
  );
}