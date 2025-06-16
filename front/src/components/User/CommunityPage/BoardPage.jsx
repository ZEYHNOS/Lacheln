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
  const [isSemiPro, setIsSemiPro] = useState(false);

  const [searchType, setSearchType] = useState("title");
  const [keyword, setKeyword] = useState("");

  const fetchUserInfo = async () => {
    try {
      const res = await apiClient.get("/auth/me");
      const tier = res.data.data?.userTier || res.data.data?.tier;
      setIsAdmin(tier === "ADMIN");
      setIsSemiPro(["SEMI_PRO", "PROFESSIONAL", "WORLD_CLASS", "CHALLENGER", "ADMIN"].includes(tier));
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
      const queryParams = new URLSearchParams();
      queryParams.append("page", page);
      queryParams.append("size", 30);

      // ✅ 검색
      if (keyword) {
        queryParams.append("type", searchType);
        queryParams.append("keyword", keyword);
        if (typeof selectedBoardId === "number") {
          queryParams.append("boardId", selectedBoardId);
        }

        const res = await apiClient.get(`/post/search?${queryParams.toString()}`);
        setPosts(res.data.data.content);
        setTotalPages(res.data.data.totalPages);
        return;
      }

      // ✅ 일반 목록
      let base = "/post/all";
      if (selectedBoardId === "popular") {
        base = "/post/popular";
      } else if (typeof selectedBoardId === "number") {
        base = "/post/list";
        queryParams.append("boardId", selectedBoardId);
      }

      const res = await apiClient.get(`${base}?${queryParams.toString()}`);
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
  }, [selectedBoardId, page, searchType, keyword]);

  const handleSearch = () => {
    setPage(1);
    fetchPosts();
  };

  // 생략된 import 및 상태 선언 등은 그대로 유지

  return (
    <div className="p-6">
      <div className="w-full max-w-[1280px] mx-auto px-4">
        <div className="flex justify-between items-center gap-2 mb-4 flex-wrap sm:flex-nowrap">
          <div className="flex flex-wrap gap-2">
            <BoardTabs
              boards={boards}
              selectedBoardId={selectedBoardId}
              onSelect={(id) => {
                setSelectedBoardId(id);
                setPage(1);
              }}
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

        {/* 🔍 검색창 */}
        <div className="flex gap-2 mb-4">
          <select
            value={searchType}
            onChange={(e) => setSearchType(e.target.value)}
            className="border border-gray-300 bg-white text-black px-2 py-1 rounded text-sm"
          >
            <option value="title">제목</option>
            <option value="title_content">제목+내용</option>
          </select>
          <input
            type="text"
            value={keyword}
            onChange={(e) => setKeyword(e.target.value)}
            onKeyDown={(e) => e.key === "Enter" && handleSearch()}
            placeholder="검색어 입력"
            className="border border-gray-300 bg-white text-black px-2 py-1 rounded flex-1 text-sm"
          />
          <button
            onClick={handleSearch}
            className="bg-[#845EC2] text-white px-4 py-1 rounded text-sm hover:bg-purple-700"
          >
            검색
          </button>
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
