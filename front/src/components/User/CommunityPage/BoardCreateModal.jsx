// BoardCreateModal.jsx
import React, { useState } from "react";
import apiClient from "../../../lib/apiClient";

export default function BoardCreateModal({ onClose, onCreated }) {
  const [boardName, setBoardName] = useState("");
  const [error, setError] = useState("");

  const handleSubmit = async () => {
    if (!boardName.trim()) {
      setError("게시판 이름을 입력해주세요.");
      return;
    }

    try {
      await apiClient.post("/board", { boardName });
      onCreated();
    } catch (err) {
      console.error("게시판 생성 실패", err);
      setError("이미 존재하는 게시판 이름입니다.");
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
            className="px-4 py-2 bg-[#845EC2] text-white rounded hover:bg-purple-700"
          >
            저장
          </button>
        </div>
      </div>
    </div>
  );
}