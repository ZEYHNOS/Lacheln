// components/User/CommunityPage/BoardTabs.jsx
import React from "react";

export default function BoardTabs({ boards, selectedBoardId, onSelect }) {
  const filteredBoards = boards.filter(
    (board) => board.boardName !== "전체게시판" && board.boardName !== "인기게시판"
  );

  const tabStyle = (active) =>
    `text-sm font-medium py-1 px-3 rounded-full border transition-all ${
      active
        ? "bg-[#845EC2] text-white border-[#845EC2]"
        : "text-gray-600 border-gray-300 hover:border-[#845EC2] hover:text-[#845EC2]"
    }`;

  return (
    <div className="flex flex-wrap gap-2">
      <button
        className={tabStyle(selectedBoardId === null)}
        onClick={() => onSelect(null)}
      >
        전체
      </button>
      <button
        className={tabStyle(selectedBoardId === "popular")}
        onClick={() => onSelect("popular")}
      >
        인기
      </button>
      {filteredBoards.map((board) => (
        <button
          key={board.boardId}
          className={tabStyle(selectedBoardId === board.boardId)}
          onClick={() => onSelect(board.boardId)}
        >
          {board.boardName.replace("게시판", "")}
        </button>
      ))}
    </div>
  );
}
