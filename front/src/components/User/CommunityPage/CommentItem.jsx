import React, { useState } from "react";
import apiClient from "../../../lib/apiClient";

export default function CommentItem({
  comment,
  postId,
  user,
  getTier,
  fetchComments,
  depth = 0,
}) {
  const [isReplying, setIsReplying] = useState(false);
  const [replyContent, setReplyContent] = useState("");

  const isSemiProOrHigher = () =>
    ["semi_pro", "professional", "world_class", "challenger", "admin"].includes(
      getTier().toLowerCase()
    );

  const canDelete =
    user && (user.userId === comment.userId || getTier().toLowerCase() === "admin");

  const handleReplySubmit = async () => {
    if (!replyContent.trim()) return;
    try {
      await apiClient.post("/comment", {
        postId: Number(postId),
        parentCmtId: comment.cmtId,
        cmtContent: replyContent,
      });
      setReplyContent("");
      setIsReplying(false);
      fetchComments();
    } catch (err) {
      alert("답글 작성 실패: " + (err.response?.data?.message || ""));
    }
  };

  const handleCommentDelete = async () => {
    if (!window.confirm("정말 삭제하시겠습니까?")) return;
    try {
      await apiClient.delete(`/comment/${comment.cmtId}`);
      fetchComments();
    } catch {
      alert("삭제 실패");
    }
  };

  const formatDateTime = (input) => {
    try {
      if (Array.isArray(input)) {
        const [y, m, d, h = 0, min = 0] = input;
        return `${y}.${String(m).padStart(2, "0")}.${String(d).padStart(2, "0")} ${String(h).padStart(2, "0")}:${String(min).padStart(2, "0")}`;
      }
      const date = new Date(input);
      return `${date.getFullYear()}.${String(date.getMonth() + 1).padStart(2, "0")}.${String(
        date.getDate()
      ).padStart(2, "0")} ${String(date.getHours()).padStart(2, "0")}:${String(
        date.getMinutes()
      ).padStart(2, "0")}`;
    } catch {
      return "날짜 오류";
    }
  };

  return (
    <div className="mb-3" style={{ marginLeft: `${depth * 24}px` }}>
      <div className="border border-gray-300 rounded overflow-hidden">
        {/* 상단 정보 바 */}
        <div className="bg-[#f5f5f5] px-3 py-2 flex justify-between items-center text-sm">
          <span className="font-semibold">{comment.userNickName}</span>
          <div className="text-xs text-gray-500 flex items-center gap-2">
            <span>{formatDateTime(comment.cmtCreate)}</span>

            {isSemiProOrHigher() && comment.cmtDegree < 4 && (
              <button
                onClick={() => setIsReplying(!isReplying)}
                style={{
                  backgroundColor: "#0f86ef",
                  color: "#fbfbfb",
                  padding: "4px 12px",
                  borderRadius: "4px"
                }}
              >
                {isReplying ? "취소" : "답글"}
              </button>
            )}

            {canDelete && (
              <button
                onClick={handleCommentDelete}
                style={{
                  backgroundColor: "#FF898B",
                  color: "#fbfbfb",
                  padding: "4px 12px",
                  borderRadius: "4px"
                }}
              >
                삭제
              </button>
            )}
          </div>
        </div>

        {/* 본문 내용 */}
        <div className="bg-white px-3 py-2 text-sm text-gray-800 whitespace-pre-wrap">
          {comment.cmtContent}

          {isReplying && (
            <div className="mt-3">
              <textarea
                className="w-full h-20 border border-gray-300 rounded px-2 py-1 text-sm"
                value={replyContent}
                onChange={(e) => setReplyContent(e.target.value)}
                placeholder="답글을 입력하세요..."
              />
              <div className="text-right mt-1">
                <button
                  onClick={handleReplySubmit}
                  className="px-3 py-1 text-sm bg-[#845EC2] text-white rounded"
                >
                  등록
                </button>
              </div>
            </div>
          )}
        </div>
      </div>

      {/* 자식 댓글 렌더링 */}
      {comment.children?.length > 0 && (
        <div className="mt-3">
          {comment.children.map((child) => (
            <CommentItem
              key={child.cmtId}
              comment={child}
              postId={postId}
              user={user}
              getTier={getTier}
              fetchComments={fetchComments}
              depth={depth + 1}
            />
          ))}
        </div>
      )}
    </div>
  );
}
