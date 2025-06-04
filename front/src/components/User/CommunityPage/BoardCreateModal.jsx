import React, { useState } from "react";
import apiClient from "../../../lib/apiClient";

export default function BoardCreateModal({ onClose, onCreated }) {
  const [boardName, setBoardName] = useState("");
  const [error, setError] = useState("");
  const [isSubmitting, setIsSubmitting] = useState(false); // ✅ 중복 방지용 상태

  const handleSubmit = async () => {
    if (!boardName.trim()) {
      setError("게시판 이름을 입력해주세요.");
      return;
    }

    if (isSubmitting) return; // ✅ 이미 요청 중이면 무시

    setIsSubmitting(true); // ✅ 요청 시작

    try {
      await apiClient.post("/board", { boardName }); // ✅ camelCase 유지
      onCreated();
    } catch (err) {
      console.error("게시판 생성 실패", err);

      const status = err.response?.status;
      const message = err.response?.data?.message;

      if (status === 400 && message?.includes("필수")) {
        setError("게시판 이름을 입력해주세요.");
      } else if (status === 400 && message?.includes("이미 존재")) {
        setError("이미 존재하는 게시판 이름입니다.");
      } else {
        setError("게시판 생성 중 오류가 발생했습니다.");
      }
    } finally {
      setIsSubmitting(false); // ✅ 요청 종료
    }
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-40 flex items-center justify-center z-50">
      <div className="bg-white p-6 rounded-lg shadow-lg w-96">
        <h2 className="text-xl font-bold mb-4">게시판 생성</h2>

        <input
          type="text"
          value={boardName}
          onChange={(e) => setBoardName(e.target.value)}
          placeholder="예: 자유게시판"
          className="w-full border border-gray-300 rounded px-3 py-2 mb-2"
        />

        {error && <p className="text-red-500 text-sm mb-2">{error}</p>}

        <div className="flex justify-end space-x-2">
          <button
            onClick={onClose}
            className="px-4 py-2 bg-gray-300 rounded hover:bg-gray-400"
          >
            취소
          </button>
          <button
            onClick={handleSubmit}
            className={`px-4 py-2 bg-[#845EC2] text-white rounded hover:bg-purple-700 ${
              isSubmitting ? "opacity-50 cursor-not-allowed" : ""
            }`}
            disabled={isSubmitting} // ✅ 버튼 비활성화
          >
            저장
          </button>
        </div>
      </div>
    </div>
  );
}
