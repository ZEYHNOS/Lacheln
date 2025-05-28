// components/User/CommunityPage/NewBoardPage.jsx
import React, { useEffect, useState } from "react";
import apiClient from "../../../lib/apiClient";
import PostTable from "./PostTable";
import BoardTabs from "./BoardTabs";
import Pagination from "./Pagination";
import BoardCreateModal from "./BoardCreateModal";

export default function NewBoardPage() {
  const [posts, setPosts] = useState([]);
  const [boards, setBoards] = useState([]);
  const [selectedBoardId, setSelectedBoardId] = useState(null); // null이면 전체 게시판
  const [page, setPage] = useState(1);
  const [totalPages, setTotalPages] = useState(1);
  const [isModalOpen, setIsModalOpen] = useState(false);

  const userId = localStorage.getItem("userId");
  const userTier = localStorage.getItem("userTier");
  const isAdmin = userTier?.toLowerCase() === "admin";

  // 게시판 목록 가져오기
  const fetchBoards = async () => {
    try {
      const res = await apiClient.get("/board");
      setBoards(res.data.data);
    } catch (err) {
      console.error("게시판 목록 조회 실패", err);
    }
  };

  // 게시글 목록 가져오기
  const fetchPosts = async () => {
    try {
      const endpoint = selectedBoardId
        ? `/post/list?boardId=${selectedBoardId}&page=${page}&size=30`
        : `/post/all?page=${page}&size=30`;
      const res = await apiClient.get(endpoint);
      setPosts(res.data.data.content);
      setTotalPages(res.data.data.totalPages);
    } catch (err) {
      console.error("게시글 목록 조회 실패", err);
    }
  };

  useEffect(() => {
    fetchBoards();
  }, []);

  useEffect(() => {
    fetchPosts();
  }, [selectedBoardId, page]);

  return (
    <div className="p-6">
      {/* 상단 탭 + 글쓰기 버튼 + 게시판 생성 (관리자만) */}
      <div className="flex justify-between items-center mb-4">
        <BoardTabs
          boards={boards}
          selectedBoardId={selectedBoardId}
          onSelect={setSelectedBoardId}
        />
        <div className="space-x-2">
          {isAdmin && (
            <button
              onClick={() => setIsModalOpen(true)}
              className="bg-purple-500 text-white px-4 py-2 rounded hover:bg-purple-600"
            >
              게시판 생성
            </button>
          )}
          <a
            href="/create"
            className="bg-[#845EC2] text-white px-4 py-2 rounded hover:bg-purple-700"
          >
            글쓰기
          </a>
        </div>
      </div>

      {/* 게시글 테이블 */}
      <PostTable posts={posts} />

      {/* 페이지네이션 */}
      <Pagination current={page} total={totalPages} onChange={setPage} />

      {/* 게시판 생성 모달 */}
      {isModalOpen && (
        <BoardCreateModal
          onClose={() => setIsModalOpen(false)}
          onCreated={() => {
            fetchBoards();
            setIsModalOpen(false);
          }}
        />
      )}
    </div>
  );
}