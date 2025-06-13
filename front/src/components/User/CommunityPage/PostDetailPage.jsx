import React, { useEffect, useRef, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import apiClient from "../../../lib/apiClient";
import CommentItem from "./CommentItem";

export default function PostDetailPage() {
  const { postId } = useParams();
  const navigate = useNavigate();
  const commentRef = useRef(null);

  const [post, setPost] = useState(null);
  const [user, setUser] = useState(null);
  const [comments, setComments] = useState([]);
  const [commentContent, setCommentContent] = useState("");
  const [hasLiked, setHasLiked] = useState(false);
  const [error, setError] = useState("");

  const countAllComments = (comments) => {
    let count = 0;
    for (const comment of comments) {
      count += 1;
      if (comment.children && comment.children.length > 0) {
        count += countAllComments(comment.children);
      }
    }
    return count;
  };

  const fetchPost = async () => {
    try {
      const res = await apiClient.get(`/post/${postId}`);
      setPost(res.data.data);
      setHasLiked(res.data.data.hasLiked);
    } catch (err) {
      console.error("게시글 조회 실패", err);
      setError("게시글을 불러오는 데 실패했습니다.");
    }
  };

  const fetchUser = async () => {
    try {
      const res = await apiClient.get("/auth/me");
      setUser(res.data.data);
    } catch {
      setUser(null);
    }
  };

  const fetchComments = async () => {
    try {
      const res = await apiClient.get(`/board/comment/list?postId=${postId}`);
      setComments(res.data.data);
    } catch (err) {
      console.error("댓글 조회 실패", err);
    }
  };

  const handleCommentSubmit = async () => {
    if (!commentContent.trim()) return;
    try {
      await apiClient.post("/board/comment", {
        postId: Number(postId),
        cmtContent: commentContent,
      });
      setCommentContent("");
      fetchComments();
    } catch (err) {
      console.error("댓글 작성 실패", err);
      alert("댓글 등록 중 오류 발생");
    }
  };

  const handleDelete = async () => {
    if (window.confirm("정말 삭제하시겠습니까?")) {
      try {
        await apiClient.delete(`/post/${postId}`);
        alert("삭제되었습니다.");
        navigate("/community");
      } catch {
        alert("삭제에 실패했습니다.");
      }
    }
  };

  const handleLike = async () => {
    try {
      await apiClient.post(`/post/${postId}/like`);
      setHasLiked(true);
      fetchPost();
    } catch (err) {
      alert(err.response?.data?.message || "추천 실패");
    }
  };

  const getTier = () => user?.userTier || user?.tier || "";

  const isWriterOrAdmin = () =>
    user && post && (user.userId === post.userId || getTier().toLowerCase() === "admin");

  const isSemiProOrHigher = () =>
    ["semi_pro", "professional", "world_class", "challenger", "admin"].includes(
      getTier().toLowerCase()
    );

  const formatDateTime = (dateArr) => {
    if (!Array.isArray(dateArr)) return "작성일 오류";
    try {
      const [y, m, d, h = 0, min = 0] = dateArr;
      const date = new Date(y, m - 1, d, h, min);
      return `${date.getFullYear()}.${String(m).padStart(2, "0")}.${String(d).padStart(2, "0")} ${String(h).padStart(2, "0")}:${String(min).padStart(2, "0")}`;
    } catch {
      return "날짜 오류";
    }
  };

  useEffect(() => {
    fetchUser().finally(() => {
      fetchPost();
      fetchComments();
    });
  }, [postId]);

  if (error) return <div className="p-6 text-red-500">{error}</div>;
  if (!post) return <div className="p-6">로딩 중...</div>;

  return (
    <div className="p-4 max-w-[900px] mx-auto mt-6 mb-20 bg-[#F9F9F9]">
      <div className="bg-white border border-gray-300 rounded px-5 py-4 mb-6 shadow-sm">
        <h1 className="text-xl font-bold border-b border-gray-300 pb-2 mb-3">{post.postTitle}</h1>
        <div className="flex justify-between text-sm text-gray-500 mb-4">
          <span className="font-bold">{post.userNickName}</span>
          <span className="font-bold">{formatDateTime(post.postCreate)}</span>
        </div>
        <div
          className="mb-6 text-[15px] leading-relaxed whitespace-pre-wrap rounded px-4 py-3 bg-white"
          style={{ minHeight: "150px" }}
          dangerouslySetInnerHTML={{ __html: post.postContent }}
        />
        <div className="flex justify-between items-center">
          <div className="flex items-center gap-4">
            <button
              onClick={handleLike}
              disabled={!isSemiProOrHigher() || hasLiked}
              className={`px-4 py-2 text-sm rounded font-semibold transition 
                ${!isSemiProOrHigher() || hasLiked 
                  ? "bg-gray-300 text-white cursor-not-allowed" 
                  : "bg-[#845EC2] text-white hover:bg-[#6d48af]"}`}
            >
              ★ 추천
            </button>
            <div className="text-sm text-gray-700 font-medium">
              추천 수: <span className="font-bold text-[#845EC2]">{post.likeCount}개</span>
            </div>
          </div>
          {isWriterOrAdmin() && (
            <div className="space-x-2">
              <button
                onClick={() => navigate(`/post/edit/${postId}`)}
                className="px-3 py-1 bg-[#35AF9A] text-white rounded"
              >
                수정
              </button>
              <button
                onClick={handleDelete}
                className="px-3 py-1 bg-[#FF898B] text-white rounded"
              >
                삭제
              </button>
            </div>
          )}
        </div>
      </div>

      {/* 댓글 박스 */}
      <div ref={commentRef} className="bg-white border border-gray-300 rounded px-5 py-4">
        <h3 className="text-lg font-semibold border-b border-gray-200 pb-2 mb-4">
          댓글 [{countAllComments(comments)}]
        </h3>
        <div className="space-y-4 mb-6">
          {comments.length === 0 ? (
            <p className="text-sm text-gray-500">댓글이 없습니다.</p>
          ) : (
            comments.map((comment) => (
              <CommentItem
                key={comment.cmtId}
                comment={comment}
                postId={postId}
                user={user}
                getTier={getTier}
                fetchComments={fetchComments}
              />
            ))
          )}
        </div>
        <textarea
          disabled={!isSemiProOrHigher()}
          placeholder={
            isSemiProOrHigher()
              ? "댓글을 입력하세요..."
              : "세미프로 이상만 댓글 작성 가능"
          }
          value={commentContent}
          onChange={(e) => setCommentContent(e.target.value)}
          className="w-full h-24 border border-gray-300 px-3 py-2 rounded resize-none bg-white"
        />
        <div className="text-right mt-2">
          <button
            onClick={handleCommentSubmit}
            disabled={!isSemiProOrHigher()}
            className="px-4 py-2 text-sm rounded font-semibold bg-[#845EC2] text-white disabled:bg-gray-300 disabled:cursor-not-allowed"
          >
            댓글 등록
          </button>
        </div>
      </div>
    </div>
  );
}
