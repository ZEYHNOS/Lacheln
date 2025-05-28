// PostTable.jsx
import React from "react";
import { Link } from "react-router-dom";

// 말머리 정리 함수: "자유게시판" → "자유"
const getPrefix = (boardName) => boardName.replace("게시판", "");

export default function PostTable({ posts }) {
  return (
    <table className="w-full text-center border border-gray-300">
      <thead className="bg-[#845EC2] text-white">
        <tr>
          <th className="p-2 border">글 번호</th>
          <th className="p-2 border">말머리</th>
          <th className="p-2 border">제목</th>
          <th className="p-2 border">글쓴이</th>
          <th className="p-2 border">등록일</th>
          <th className="p-2 border">추천</th>
          <th className="p-2 border">조회</th>
        </tr>
      </thead>
      <tbody>
        {posts.length === 0 ? (
          <tr>
            <td colSpan="7" className="p-4 text-gray-500">
              게시글이 없습니다.
            </td>
          </tr>
        ) : (
          posts.map((post) => (
            <tr key={post.postId} className="hover:bg-gray-50">
              <td className="border p-2">{post.postId}</td>
              <td className="border p-2 font-semibold text-purple-700">
                [{getPrefix(post.category)}]
              </td>
              <td className="border p-2 text-left">
                <Link
                  to={`/post/${post.postId}`}
                  className="text-blue-600 hover:underline"
                >
                  {post.postTitle}
                </Link>
              </td>
              <td className="border p-2">{post.userNickName}</td>
              <td className="border p-2">
                {new Date(post.postCreate).toLocaleDateString("ko-KR", {
                  year: "2-digit",
                  month: "2-digit",
                  day: "2-digit",
                })}
              </td>
              <td className="border p-2">{post.likeCount}</td>
              <td className="border p-2">{post.viewCount}</td>
            </tr>
          ))
        )}
      </tbody>
    </table>
  );
}
