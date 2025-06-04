import React, { useEffect, useState } from "react";
import apiClient from "../../../lib/apiClient";
import { useNavigate } from "react-router-dom";

export default function CreatePostPage() {
  const navigate = useNavigate();

  const [boards, setBoards] = useState([]);
  const [selectedBoardId, setSelectedBoardId] = useState("");
  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");
  const [error, setError] = useState("");

  // ✅ 세미프로 이상인지 확인
  useEffect(() => {
    const checkTier = async () => {
      try {
        const res = await apiClient.get("/auth/me");
        const tier = res.data.data?.userTier || res.data.data?.tier;
        const allowed = ["SEMI_PRO", "PRO", "ADMIN"];
        if (!allowed.includes(tier)) {
          alert("세미프로 이상만 글을 작성할 수 있습니다.");
          navigate("/community");
        }
      } catch {
        alert("로그인 후 이용 가능합니다.");
        navigate("/login");
      }
    };
    checkTier();
  }, [navigate]);

  // 게시판 목록 불러오기
  useEffect(() => {
    const fetchBoards = async () => {
      try {
        const res = await apiClient.get("/board");
        const filtered = res.data.data.filter(
          (b) => b.boardName !== "전체게시판" && b.boardName !== "인기게시판"
        );
        setBoards(filtered);
        if (filtered.length > 0) setSelectedBoardId(filtered[0].boardId);
      } catch (err) {
        console.error("게시판 목록 조회 실패", err);
      }
    };
    fetchBoards();
  }, []);

  // 게시글 등록
  const handleSubmit = async () => {
    if (!selectedBoardId || !title.trim() || !content.trim()) {
      setError("모든 필드를 입력해주세요.");
      return;
    }

    try {
      await apiClient.post("/post", {
        boardId: selectedBoardId,
        postTitle: title,
        postContent: content,
      });
      navigate("/community");
    } catch (err) {
      console.error("게시글 작성 실패", err);
      setError("게시글 작성 중 오류가 발생했습니다.");
    }
  };

  return (
    <div className="p-6 max-w-[800px] mx-auto">
      <h2 className="text-2xl font-bold mb-4">글쓰기</h2>

      {/* 게시판 선택 */}
      <div className="mb-4">
        <label className="block mb-1 font-semibold">게시판</label>
        <select
          className="w-full border px-3 py-2 rounded"
          value={selectedBoardId}
          onChange={(e) => setSelectedBoardId(Number(e.target.value))}
        >
          {boards.map((board) => (
            <option key={board.boardId} value={board.boardId}>
              {board.boardName}
            </option>
          ))}
        </select>
      </div>

      {/* 제목 */}
      <div className="mb-4">
        <label className="block mb-1 font-semibold">제목</label>
        <input
          className="w-full border px-3 py-2 rounded"
          type="text"
          placeholder="제목을 입력하세요"
          value={title}
          onChange={(e) => setTitle(e.target.value)}
        />
      </div>

      {/* 본문 */}
      <div className="mb-4">
        <label className="block mb-1 font-semibold">내용</label>
        <textarea
          className="w-full h-60 border px-3 py-2 rounded resize-none"
          placeholder="내용을 입력하세요"
          value={content}
          onChange={(e) => setContent(e.target.value)}
        />
      </div>

      {/* 에러 메시지 */}
      {error && <p className="text-red-500 mb-4">{error}</p>}

      {/* 등록 버튼 */}
      <div className="flex justify-end">
        <button
          onClick={handleSubmit}
          className="px-5 py-2 bg-[#845EC2] text-white rounded hover:bg-purple-700"
        >
          등록
        </button>
      </div>
    </div>
  );
}
