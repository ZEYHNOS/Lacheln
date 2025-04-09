import { useState } from "react";

const PostDetail = () => {
    const [comment, setComment] = useState("");
    const [comments, setComments] = useState([]);

    const handleCommentSubmit = () => {
        if (comment.trim() === "") {
            alert("댓글을 입력해주세요!");
            return;
        }
        setComments([...comments, comment]);
        setComment("");
        alert("댓글이 등록되었습니다!");
    };
    const handleDelete = () => {
        const confirmDelete = window.confirm("정말 삭제하시겠습니까?");
        if (confirmDelete) {

        }
        alert("게시물이 삭제되었습니다.");
        window.location.href = "/Post";
    }

    return (
        <div className="max-w-screen-lg mx-auto p-6">
            <h2 className="text-2xl font-bold text-center mb-4">게시글 조회</h2>
            <hr className="mb-6" />

            <nav className="mb-6 text-center">
                <ul className="flex justify-center space-x-16 text-lg font-bold">
                    <li className="text-purple-600 hover:text-pink-400 hover:underline cursor-pointer">자유게시판</li>
                    <li className="text-purple-600 hover:text-pink-400 hover:underline cursor-pointer">후기게시판</li>
                    <li className="text-purple-600 hover:text-pink-400 hover:underline cursor-pointer">질문게시판</li>
                </ul>
            </nav>

            <div className="flex justify-end space-x-2 mb-4">
                <button className="w-14 h-8 border border-purple-500 bg-white text-purple-500 rounded">수정</button>
                <button className="w-14 h-8 border border-purple-500 bg-white text-purple-500 rounded">삭제</button>
            </div>

            <h3 className="text-xl font-semibold text-center mb-2">첫 번째 게시글</h3>
            <hr className="mb-4" />

            <div className="text-center">
                <textarea
                    className="w-3/5 h-28 text-base p-2 border border-gray-400 rounded resize-none text-center"
                    placeholder="로그인 후 이용해주세요."
                    disabled
                ></textarea>

                <div className="mt-6">
                    <input
                        type="text"
                        value={comment}
                        onChange={(e) => setComment(e.target.value)}
                        placeholder="댓글을 입력하세요"
                        className="w-3/5 p-2 border border-gray-300 rounded"
                    />
                    <button
                        onClick={handleCommentSubmit}
                        className="ml-2 px-4 py-2 border border-purple-500 text-purple-500 rounded hover:bg-purple-100"
                    >
                        댓글작성
                    </button>

                    <div className="mt-6 w-3/5 mx-auto text-left">
                        {comments.map((c, idx) => (
                            <div key={idx} className="p-2 border-b border-gray-300">
                                {c}
                            </div>
                        ))}
                    </div>
                </div>
            </div>

            <div className="relative mt-10">
                <form className="absolute right-4 bottom-0 flex items-center gap-2">
                    <select className="border p-1">
                        <option value="title">제목</option>
                        <option value="content">내용</option>
                        <option value="title+content" selected>제목+내용</option>
                        <option value="author">글쓴이</option>
                    </select>
                    <input type="text" placeholder="검색어 입력" className="border p-1" />
                    <button type="submit" className="px-3 py-1 border bg-gray-100">검색</button>
                    <button
                        type="button"
                        onClick={() => window.location.href = "/CreatePost"}
                        className="ml-2 px-3 py-1 border border-purple-500 text-purple-500 rounded hover:bg-purple-100"
                    >
                        글쓰기
                    </button>
                </form>
            </div>
        </div>
    );
};

export default PostDetail;