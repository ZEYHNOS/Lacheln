import React, { useEffect, useState } from "react";
import apiClient from "../../../lib/apiClient";
import PostTable from "./PostTable";
import BoardTabs from "./BoardTabs";
import Pagination from "./Pagination";
import BoardCreateModal from "./BoardCreateModal";

export default function BoardPage() {
  const [posts, setPosts] = useState([]);
  const [boards, setBoards] = useState([]);
  const [selectedBoardId, setSelectedBoardId] = useState(null);
  const [page, setPage] = useState(1);
  const [totalPages, setTotalPages] = useState(1);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [isAdmin, setIsAdmin] = useState(false);
  const [isSemiPro, setIsSemiPro] = useState(false); // ✅ 세미프로 이상 여부

  const fetchUserInfo = async () => {
    try {
      const res = await apiClient.get("/auth/me");
      const tier = res.data.data?.userTier || res.data.data?.tier;
      setIsAdmin(tier === "ADMIN");
      setIsSemiPro(["SEMI_PRO", "PROFESSIONAL", "WORLD_CLASS", "CHALLENGER", "ADMIN"].includes(tier)); // ✅ 조건 추가
    } catch {
      setIsAdmin(false);
      setIsSemiPro(false);
    }
  };

  const fetchBoards = async () => {
    try {
      const res = await apiClient.get("/board");
      setBoards(res.data.data);
    } catch (err) {
      console.error("게시판 목록 조회 실패", err);
    }
  };

  const fetchPosts = async () => {
    try {
      let endpoint =
        selectedBoardId === "popular"
          ? `/post/popular?page=${page}&size=30`
          : typeof selectedBoardId === "number"
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
    fetchUserInfo();
    fetchBoards();
  }, []);

  useEffect(() => {
    fetchPosts();
  }, [selectedBoardId, page]);

  return (
    <div className="p-6">
      <div className="w-full max-w-[1280px] mx-auto px-4">
        <div className="flex justify-between items-center gap-2 mb-4 flex-wrap sm:flex-nowrap">
          <div className="flex flex-wrap gap-2">
            <BoardTabs
              boards={boards}
              selectedBoardId={selectedBoardId}
              onSelect={setSelectedBoardId}
            />
          </div>
          <div className="flex gap-2 shrink-0">
            {isAdmin && (
              <button
                onClick={() => setIsModalOpen(true)}
                className="h-8 px-3 text-sm bg-purple-500 text-white rounded hover:bg-purple-600 whitespace-nowrap"
              >
                게시판 생성
              </button>
            )}
            {isSemiPro && (
              <a
                href="/create"
                className="h-8 px-3 text-sm bg-[#845EC2] text-white rounded hover:bg-purple-700 flex items-center whitespace-nowrap"
              >
                글쓰기
              </a>
            )}
          </div>
        </div>

        <PostTable posts={posts} />
        <Pagination current={page} total={totalPages} onChange={setPage} />
      </div>

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
