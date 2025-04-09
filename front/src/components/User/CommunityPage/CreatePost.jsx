import { useState } from "react";
import { useNavigate } from "react-router-dom";

const CreatePost = () => {
    const [title, setTitle] = useState("");
    const [content, setContent] = useState("");
    const navigate = useNavigate();
    const [selectedBoard, setSelectedBoard] = useState("자유게시판");


    const savePost = () => {
        if (title.trim() && content.trim()) {
            const post = { title, content };
            let posts = JSON.parse(localStorage.getItem("posts")) || [];
            posts.push(post);
            localStorage.setItem("posts", JSON.stringify(posts));
            alert("게시물이 저장되었습니다.");
            navigate("/community");
        } else {
            alert("제목과 내용을 입력하세요.");
        }
    };

    return (
        <div className="max-w-7xl mx-auto p-3">
            <h2 className="text-center text-2xl font-bold">게시글 작성</h2>
            <hr className="my-6" />
            <nav className="mb-5">
                <h1 className="text-[25px] font-semibold mb-4 text-purple-500 w-full max-w-sm mx-auto text-center">분류</h1>

                <ul className="flex justify-center gap-[5px] text-lg font-bold">
                    {["자유게시판", "후기게시판", "질문게시판"].map((board, idx) => (
                        <li
                            key={idx}
                            className={`cursor-pointer px-[100px] transition-colors duration-200 ${
                                selectedBoard === board
                                    ? "text-pink-500 font-extrabold"
                                    : "text-purple-600 hover:text-pink-400"
                            }`}
                        >
                            <label className="cursor-pointer">
                                <input
                                    type="radio"
                                    name="boardType"
                                    value={board}
                                    checked={selectedBoard === board}
                                    onChange={() => setSelectedBoard(board)}
                                    className="mr-2"
                                />
                                {board}
                            </label>
                        </li>
                    ))}
                </ul>

            </nav>
            <div className="w-full max-w-6xl mx-auto">
                <label className="block text-left font-semibold mb-2">제목</label>
                <input
                    type="text"
                    className="w-full p-2 border rounded-lg mb-4"
                    placeholder="제목을 입력하세요."
                    value={title}
                    onChange={(e) => setTitle(e.target.value)}
                />
                <label className="block text-left font-semibold mb-6">내용</label>
                <textarea
                    className="w-full p-2 border rounded-lg mb-2 h-40"
                    placeholder="내용을 입력하세요."
                    value={content}
                    onChange={(e) => setContent(e.target.value)}
                ></textarea>
            </div>
            <div className="flex gap-4 mt-4 justify-end">
                <button
                    className="bg-violet-400 text-white px-4 py-2 rounded-lg hover:bg-violet-500"
                    onClick={savePost}
                >
                    저장
                </button>
                <button
                    className="bg-white text-purple-600 font-bold px-4 py-2 rounded-lg hover:bg-violet-100"
                    onClick={() => navigate("/community")}
                >
                    취소
                </button>
            </div>
        </div>
    );
};

export default CreatePost;