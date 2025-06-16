import React from "react";
import { Link } from "react-router-dom";

const getPrefix = (boardName) => boardName.replace("게시판", "");

const formatDate = (dateArr) => {
  if (!Array.isArray(dateArr)) return "작성일 없음";
  try {
    const [y, m, d, h = 0, min = 0, s = 0] = dateArr;
    const date = new Date(y, m - 1, d, h, min, s);
    const now = new Date();
    const isToday =
      date.getFullYear() === now.getFullYear() &&
      date.getMonth() === now.getMonth() &&
      date.getDate() === now.getDate();

    return isToday
      ? date.toLocaleTimeString("ko-KR", { hour: "2-digit", minute: "2-digit", hour12: false })
      : `${String(y).slice(-2)}.${String(m).padStart(2, "0")}.${String(d).padStart(2, "0")}`;
  } catch {
    return "오류";
  }
};

export default function PostTable({ posts }) {
  return (
    <div className="w-full max-w-[1280px] mx-auto px-4">
      <table className="w-full text-center border border-gray-300 text-sm bg-white text-black">
        <thead className="bg-[#845EC2] text-white">
          <tr>
            <th className="p-2 border w-12 text-center">번호</th>
            <th className="p-2 border w-20 text-center">말머리</th>
            <th className="p-2 border text-center">제목</th>
            <th className="p-2 border w-32 text-center">글쓴이</th>
            <th className="p-2 border w-24 text-center">작성일</th>
            <th className="p-2 border w-12 text-center">추천</th>
            <th className="p-2 border w-12 text-center">조회</th>
          </tr>
        </thead>
        <tbody>
          {posts.length === 0 ? (
            <tr>
              <td colSpan="7" className="p-4 text-gray-500 text-center bg-white">
                게시글이 없습니다.
              </td>
            </tr>
          ) : (
            posts.map((post) => (
              <tr key={post.postId} className="hover:bg-purple-50 bg-white text-black h-8">
                <td className="border px-2 py-1 text-center">{post.postId}</td>
                <td className="border px-2 py-1 text-purple-700 font-semibold text-center">
                  [{getPrefix(post.category)}]
                </td>
                <td className="border px-2 py-1 text-left">
                  <Link to={`/post/${post.postId}`} className="text-black">
                    {post.postTitle}
                    {typeof post.commentCount === "number" && post.commentCount > 0 && (
                      <span className="ml-1 text-gray-600">[{post.commentCount}]</span>
                    )}
                  </Link>
                </td>
                <td className="border px-2 py-1 text-left">{post.userNickName}</td>
                <td className="border px-2 py-1 text-center">{formatDate(post.postCreate)}</td>
                <td className="border px-2 py-1 text-center">{post.likeCount}</td>
                <td className="border px-2 py-1 text-center">{post.viewCount}</td>
              </tr>
            ))
          )}
        </tbody>
      </table>
    </div>
  );
}
