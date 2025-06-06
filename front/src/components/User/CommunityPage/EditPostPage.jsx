import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import apiClient from "../../../lib/apiClient";

export default function EditPostPage() {
  const { postId } = useParams();
  const navigate = useNavigate();

  const [boards, setBoards] = useState([]);
  const [selectedBoardId, setSelectedBoardId] = useState("");
  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");
  const [error, setError] = useState("");

  // 게시판 목록 불러오기
  useEffect(() => {
    const fetchBoards = async () => {
      try {
        const res = await apiClient.get("/board");
        const filtered = res.data.data.filter(
          (b) => b.boardName !== "전체게시판" && b.boardName !== "인기게시판"
        );
        setBoards(filtered);
      } catch (err) {
        console.error("게시판 목록 조회 실패", err);
      }
    };
    fetchBoards();
  }, []);

  // 게시글 불러오기
  useEffect(() => {
    const fetchPost = async () => {
      try {
        const res = await apiClient.get(`/post/${postId}`);
        const data = res.data.data;
        setTitle(data.postTitle);
        setContent(data.postContent);
        setSelectedBoardId(data.boardId); // ✅ 게시판 초기값 설정
      } catch (err) {
        console.error("게시글 조회 실패", err);
        setError("게시글을 불러오는 데 실패했습니다.");
      }
    };
    fetchPost();
  }, [postId]);

  // 수정 요청
  const handleUpdate = async () => {
    if (!selectedBoardId || !title.trim() || !content.trim()) {
      setError("모든 필드를 입력해주세요.");
      return;
    }

    try {
      await apiClient.put("/post", {
        postId: Number(postId),
        postTitle: title,
        postContent: content,
        boardId: selectedBoardId,
      });
      alert("수정이 완료되었습니다.");
      navigate(`/post/${postId}`);
    } catch (err) {
      console.error("게시글 수정 실패", err);
      setError("게시글 수정 중 오류가 발생했습니다.");
    }
  };

  if (error) return <div className="p-6 text-red-500">{error}</div>;
  if (!title) return <div className="p-6">로딩 중...</div>;

  return (
    <div className="p-6 max-w-[800px] mx-auto">
      {/* 게시판 선택 */}
      <div className="mb-4">
        <label className="block mb-1 font-semibold">게시판</label>
        <select
          className="w-full border border-[#9e5fdd] px-3 py-2 rounded"
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
          className="w-full border border-[#9e5fdd] px-3 py-2 rounded"
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
          className="w-full h-60 border border-[#9e5fdd] px-3 py-2 rounded resize-none"
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
          onClick={handleUpdate}
          className="px-5 py-2 bg-[#845EC2] text-white rounded hover:bg-purple-700"
        >
          수정 완료
        </button>
      </div>
    </div>
  );
}
