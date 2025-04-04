import { useState } from "react";
import { useNavigate } from "react-router-dom";

const CreatePost = () => {
const [title, setTitle] = useState("");
const [content, setContent] = useState("");
const navigate = useNavigate();

const savePost = () => {
    if (title.trim() && content.trim()) {
const post = { title, content };
let posts = JSON.parse(localStorage.getItem("posts")) || [];
posts.push(post);
    localStorage.setItem("posts", JSON.stringify(posts));
    alert("게시물이 저장되었습니다.");
    navigate("/board");
} else {
    alert("제목과 내용을 입력하세요.");
}
};

return (
    <div className="flex flex-col items-center p-6">
    <h2 className="text-2xl font-bold mb-4">게시글 작성</h2>
    <hr className="w-full mb-4" />
    <nav className="mb-6">
        <h3 className="text-lg font-semibold mb-2">분류</h3>
        <ul className="flex justify-center space-x-6">
        <li className="cursor-pointer text-pink-500 hover:underline">자유게시판</li>
        <li className="cursor-pointer text-purple-500 hover:text-pink-500 hover:underline">후기게시판</li>
        <li className="cursor-pointer text-purple-500 hover:text-pink-500 hover:underline">질문게시판</li>
        </ul>
    </nav>
    <div className="w-full max-w-2xl">
        <label className="block text-left font-semibold mb-2">제목</label>
        <input
        type="text"
        className="w-full p-2 border rounded-lg mb-4"
        placeholder="제목을 입력하세요."
        value={title}
        onChange={(e) => setTitle(e.target.value)}
        />
        <label className="block text-left font-semibold mb-2">내용</label>
        <textarea
        className="w-full p-2 border rounded-lg mb-4 h-40"
        placeholder="내용을 입력하세요."
        value={content}
        onChange={(e) => setContent(e.target.value)}
        ></textarea>
    </div>
    <div className="flex gap-4 mt-4">
        <button>
        className="bg-blue-500 text-white px-4 py-2 rounded-lg hover:bg-blue-600"
        onClick={savePost}
        등록
        </button>
        <button>
        className="bg-gray-400 text-white px-4 py-2 rounded-lg hover:bg-gray-500"
        onClick={() => navigate("/community")}
        취소
        </button>
    </div>
    </div>
);
};

export default CreatePost;