// BoardTabs.jsx
import React from "react";

export default function BoardTabs({ boards, selectedBoardId, onSelect }) {
  return (
    <div className="flex space-x-4 mb-4 border-b border-gray-300 pb-2">
      {/* 전체 게시판 탭 */}
      <button
        className={`text-lg font-semibold pb-1 border-b-4 transition-all duration-150 ${
          selectedBoardId === null
            ? "border-[#845EC2] text-[#845EC2]"
            : "border-transparent text-gray-500 hover:text-[#845EC2]"
        }`}
        onClick={() => onSelect(null)}
      >
        전체게시판
      </button>

      {/* 각 게시판별 탭 */}
      {boards.map((board) => (
        <button
          key={board.id}
          className={`text-lg font-semibold pb-1 border-b-4 transition-all duration-150 ${
            selectedBoardId === board.id
              ? "border-[#845EC2] text-[#845EC2]"
              : "border-transparent text-gray-500 hover:text-[#845EC2]"
          }`}
          onClick={() => onSelect(board.id)}
        >
          {board.boardName.replace("게시판", "")}게시판
        </button>
      ))}
    </div>
  );
}