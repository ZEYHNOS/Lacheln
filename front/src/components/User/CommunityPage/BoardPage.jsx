import { useState } from "react";

const BoardPage = () => {
    const dummyPosts = [
        { tab: "후기", id: 8, title: "여덟 번째 게시글", content: "정말 유익한 경험이었습니다. 추천드려요!", author: "박서준", date: "2025-04-02", likes: 12, views: 42 },
        { tab: "자유", id: 7, title: "일곱 번째 게시글", content: "오늘 날씨 진짜 좋네요~ 기분도 좋아요.", author: "이지은", date: "2025-03-29", likes: 9, views: 56 },
        { tab: "질문", id: 6, title: "여섯 번째 게시글", content: "React에서 props와 state 차이가 뭔가요?", author: "차은우", date: "2025-03-27", likes: 15, views: 51 },
        { tab: "후기", id: 5, title: "다섯 번째 게시글", content: "이 프로젝트 덕분에 많이 배웠어요. 감사합니다.", author: "손흥민", date: "2025-03-25", likes: 10, views: 48 },
        { tab: "자유", id: 4, title: "네 번째 게시글", content: "요즘에 책 읽는 게 재밌네요. 추천할게요!", author: "김연아", date: "2025-03-20", likes: 17, views: 45 },
        { tab: "후기", id: 3, title: "세 번째 게시글", content: "강의가 너무 좋았어요. 다음에도 참여하고 싶어요.", author: "이영희", date: "2025-03-15", likes: 24, views: 50 },
        { tab: "자유", id: 2, title: "두 번째 게시글", content: "주말에 뭐 할지 고민되네요ㅎㅎ 추천 좀요.", author: "김철수", date: "2025-03-09", likes: 7, views: 22 },
        { tab: "질문", id: 1, title: "첫 번째 게시글", content: "자바스크립트에서 배열 정렬하는 방법 궁금합니다.", author: "홍길동", date: "2025-03-04", likes: 14, views: 38 },
    ];

    const [posts, setPosts] = useState(dummyPosts);
    const [searchTerm, setSearchTerm] = useState("");
    const [searchType, setSearchType] = useState("title+content");
    const [currentPage, setCurrentPage] = useState(1);
    const postsPerPage = 5;

    const handleSearch = (e) => {
        e.preventDefault();
        const filteredPosts = dummyPosts.filter((post) => {
            const term = searchTerm.toLowerCase();
            if (searchType === "title" && post.title.toLowerCase().includes(term)) return true;
            if (searchType === "content" && post.content?.toLowerCase().includes(term)) return true;
            if (searchType === "title+content" && (post.title.toLowerCase().includes(term) || post.content?.toLowerCase().includes(term))) return true;
            if (searchType === "author" && post.author.toLowerCase().includes(term)) return true;
            return false;
        });
        setPosts(filteredPosts);
        setCurrentPage(1);
    };

    // 페이지네이션 처리
    const indexOfLastPost = currentPage * postsPerPage;
    const indexOfFirstPost = indexOfLastPost - postsPerPage;
    const currentPosts = posts.slice(indexOfFirstPost, indexOfLastPost);
    const totalPages = Math.ceil(posts.length / postsPerPage);

    return (
        <div className="max-w-7xl mx-auto p-3">
            <h2 className="text-center text-2xl font-bold">전체 게시판</h2>
            <hr className="my-6" />
            <nav className="mb-5">
                <ul className="flex justify-center gap-[5px] text-lg font-bold">
                    <li className="text-purple-600 hover:text-pink-400 hover:underline cursor-pointer border-l border-purple-400 px-[120px]">자유게시판</li>
                    <li className="text-purple-600 hover:text-pink-400 hover:underline cursor-pointer border-l border-r border-purple-400 px-[110px]">후기게시판</li>
                    <li className="text-purple-600 hover:text-pink-400 hover:underline cursor-pointer border-r border-purple-400 px-[120px]">질문게시판</li>
                </ul>
            </nav>
            <table className="w-full border-collapse">
                <thead>
                    <tr className="bg-gray-100">
                        <th className="p-3">탭</th>
                        <th className="p-3">번호</th>
                        <th className="p-3">제목</th>
                        <th className="p-3">글쓴이</th>
                        <th className="p-3">등록일</th>
                        <th className="p-3">추천</th>
                        <th className="p-3">조회</th>
                    </tr>
                </thead>

                <tbody>
                    {currentPosts.map((post, index) => (
                        <tr key={post.id} className="hover:bg-gray-100 cursor-pointer" onClick={() => window.location.href = `/post/${post.id}`}>
                            <td className="text-purple-600 font-bold p-3">{post.tab}</td>
                            <td className="p-3">{index + 1 + (currentPage - 1) * postsPerPage}</td>
                            <td className="p-3 hover: underline hover:text-blue-500">{post.title}</td>
                            <td className="p-3 hover: underline hover:text-blue-500">{post.author}</td>
                            <td className="p-3">{post.date}</td>
                            <td className={`p-3 font-bold ${post.likes >= 15 ? 'text-red-500' : 'text-blue-500'}`}>{post.likes}</td>
                            <td className="p-3">{post.views}</td>
                        </tr>
                    ))}
                </tbody>
            </table>
            
            {/* 페이지네이션 */}
            <div className="flex justify-center gap-2 mt-5">
                <button 
                    className={`px-2 py-1 border ${currentPage === 1 ? 'text-gray-400' : 'hover:border-pink-400 hover:text-red-500'}`}
                    onClick={() => setCurrentPage(currentPage - 1)}
                    disabled={currentPage === 1}
                >
                    {"<"}
                </button>
                {Array.from({ length: totalPages }, (_, i) => i + 1).map((num) => (
                    <button
                        key={num}
                        className={`px-2 py-1 border ${currentPage === num ? 'border-pink-400 text-red-500' : 'border-gray-300'} hover:border-pink-400 hover:text-red-500`}
                        onClick={() => setCurrentPage(num)}
                    >
                        {num}
                    </button>
                ))}
                <button 
                    className={`px-2 py-1 border ${currentPage === totalPages ? 'text-gray-400' : 'hover:border-pink-400 hover:text-red-500'}`}
                    onClick={() => setCurrentPage(currentPage + 1)}
                    disabled={currentPage === totalPages}
                >
                    {">"}
                </button>
            </div>

            {/* 검색 기능 */}
            <form className="flex justify-end items-center gap-2 mt-5" onSubmit={handleSearch}>
                <select className="border p-2" value={searchType} onChange={(e) => setSearchType(e.target.value)}>
                    <option value="title">제목</option>
                    <option value="content">내용</option>
                    <option value="title+content">제목+내용</option>
                    <option value="author">글쓴이</option>
                </select>
                <input type="text" placeholder="검색어 입력" className="border p-2" value={searchTerm} onChange={(e) => setSearchTerm(e.target.value)} />
                <button type="submit" className="bg-gray-200 px-3 py-2 hover:bg-violet-100">검색</button>
                <button type="button" className="bg-white text-purple-600 font-bold border rounded-lg px-4 py-2 hover:bg-violet-100" onClick={() => window.location.href = '/create'}>글쓰기</button>
            </form>
        </div>
    );
};

export default BoardPage;