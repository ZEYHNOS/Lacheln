import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";

const PostDetail = () => {
    const { id } = useParams();
    const navigate = useNavigate();

    const [post, setPost] = useState(null);
    const [comment, setComment] = useState("");
    const [comments, setComments] = useState([]);

    const isLoggedIn = true;

    // 더미 게시글 데이터
    const dummyPost = {
        id: id,
        title: "더미 게시글 제목입니다.",
        content: "여기에 게시글 내용이 표시됩니다. 이건 더미 데이터입니다.",
    };

    // 더미 댓글 데이터
    const dummyComments = [
        { content: "첫 번째 댓글입니다!" },
        { content: "좋은 글 감사합니다." },
        { content: "질문이 있어요~" },
    ];

    useEffect(() => {
        // 실제 API 요청 대신 더미 데이터로 설정
        setPost(dummyPost);
        setComments(dummyComments);
    }, [id]);

    const handleCommentSubmit = () => {
        if (comment.trim() === "") {
            alert("댓글을 입력해주세요!");
            return;
        }

        // 새로운 댓글 추가 (더미 처리)
        const newComment = { content: comment };
        setComments((prev) => [...prev, newComment]);
        setComment("");
        alert("댓글이 등록되었습니다!");
    };

    const handleDelete = () => {
        const confirmDelete = window.confirm("정말 삭제하시겠습니까?");
        if (confirmDelete) {
            alert("게시물이 삭제되었습니다.");
            navigate("/post");
        }
    };

    const handleSearch = (e) => {
        e.preventDefault();
        alert("검색 기능은 아직 구현되지 않았습니다.");
    };

    return (
        <div className="max-w-screen-lg mx-auto p-3">
            <h2 className="text-2xl font-bold text-center mb-4">게시글 조회</h2>
            <hr className="mb-6" />

            <nav className="mb-5">
                <ul className="flex justify-center gap-[5px] text-lg font-bold">
                    <li className="text-purple-600 hover:text-pink-400 hover:underline cursor-pointer border-l border-purple-400 px-[120px]">자유게시판</li>
                    <li className="text-purple-600 hover:text-pink-400 hover:underline cursor-pointer border-l border-r border-purple-400 px-[110px]">후기게시판</li>
                    <li className="text-purple-600 hover:text-pink-400 hover:underline cursor-pointer border-r border-purple-400 px-[120px]">질문게시판</li>
                </ul>
            </nav>

            <div className="flex justify-end space-x-2 mb-4">
            <button className="w-20 h-9 border border-purple-500 bg-white text-purple-500 rounded">수정</button>
            <button onClick={handleDelete} className="w-20 h-9 border border-purple-500 bg-white text-purple-500 rounded">삭제</button>
            </div>

            <h3 className="text-xl font-semibold text-center mb-2">{post?.title}</h3>
            <div className="text-center mb-6">
                <p className="text-gray-700">{post?.content}</p>
            </div>
            <hr className="mb-4" />

            <div className="text-center">
                <textarea
                    className="w-3/5 h-28 text-base p-2 border border-gray-400 rounded resize-none text-center"
                    placeholder={isLoggedIn ? "댓글을 입력하세요." : "로그인 후 이용해주세요."}
                    disabled={!isLoggedIn}
                    value={comment}
                    onChange={(e) => setComment(e.target.value)}
                ></textarea>

                <div className="mt-4">
                    <button
                        onClick={handleCommentSubmit}
                        className="px-4 py-2 border border-purple-500 text-purple-500 rounded hover:bg-purple-100"
                        disabled={!isLoggedIn}
                    >
                        댓글작성
                    </button>
                </div>

                <div className="mt-6 w-3/5 mx-auto text-left">
                    {comments.map((c, idx) => (
                        <div key={idx} className="p-2 border-b border-gray-300">
                            {c.content}
                        </div>
                    ))}
                </div>
            </div>

            <div className="relative mt-10">
                <form
                    onSubmit={handleSearch}
                    className="absolute right-4 bottom-0 flex items-center gap-2"
                >
                    <select className="border p-1">
                        <option value="title">제목</option>
                        <option value="content">내용</option>
                        <option value="title+content">제목+내용</option>
                        <option value="author">글쓴이</option>
                    </select>
                    <input type="text" placeholder="검색어 입력" className="border p-1" />
                    <button type="submit" className="px-3 py-1 border bg-gray-100">검색</button>
                    <button
                        type="button"
                        onClick={() => (window.location.href = "/create")}
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
