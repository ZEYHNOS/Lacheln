import React, { useState } from 'react';

const BoardPage = () => {
const dummyPosts = [
    { tap: "후기", id: 3, title: "세 번째 게시글", author: "이영희", date: "2025-03-21", likes: 15, views: 50 },
    { tap: "자유", id: 2, title: "두 번째 게시글", author: "김철수", date: "2025-03-20", likes: 21, views: 30 },
    { tap: "질문", id: 1, title: "첫 번째 게시글", author: "홍길동", date: "2025-03-19", likes: 12, views: 40 }
];

const [searchTerm, setSearchTerm] = useState("");
const [searchType, setSearchType] = useState("title+content");
const [filteredPosts, setFilteredPosts] = useState(dummyPosts); // 필터된 게시글 상태 추가

  // 게시글 렌더링
const displayPosts = (posts) => {
    return posts.map((post, index) => {
    const titleStyle = post.likes >= 15 ? "text-red-500" : "";
    return (
        <tr key={post.id}>
        <td className="text-purple-500 font-bold">{post.tap}</td>
        <td>{index + 1}</td>
        <td>{post.title}</td>
        <td>{post.author}</td>
        <td>{post.date}</td>
        <td className={`font-bold ${titleStyle}`}>{post.likes}</td>
        <td>{post.views}</td>
        </tr>
    );
    });
};

  // 검색 핸들러
const handleSearch = (e) => {
    e.preventDefault();
    const term = searchTerm.toLowerCase();
    const filtered = dummyPosts.filter((post) => {
    if (searchType === "title" && post.title.toLowerCase().includes(term)) return true;
    if (searchType === "content" && post.content && post.content.toLowerCase().includes(term)) return true;
    if (searchType === "title+content" && (post.title.toLowerCase().includes(term) || (post.content && post.content.toLowerCase().includes(term)))) return true;
    if (searchType === "author" && post.author.toLowerCase().includes(term)) return true;
    return false;
    });

    // 필터된 게시글을 상태에 저장
    setFilteredPosts(filtered);
};

return (
    <div>
    <h2 className="text-center text-2xl mb-4">전체 게시판</h2>
    <hr />
    <nav className="text-center mb-4">
        <ul className="flex justify-center space-x-4">
        <li className="text-purple-500 hover:underline">자유게시판</li>
        <li className="text-purple-500 hover:underline">후기게시판</li>
        <li className="text-purple-500 hover:underline">질문게시판</li>
        </ul>
    </nav>

    <table className="table-auto w-full mt-4">
        <thead>
        <tr>
            <th className="bg-gray-200 px-4 py-2">탭</th>
            <th className="bg-gray-200 px-4 py-2">번호</th>
            <th className="bg-gray-200 px-4 py-2">제목</th>
            <th className="bg-gray-200 px-4 py-2">글쓴이</th>
            <th className="bg-gray-200 px-4 py-2">등록일</th>
            <th className="bg-gray-200 px-4 py-2">추천</th>
            <th className="bg-gray-200 px-4 py-2">조회</th>
        </tr>
        </thead>
        <tbody>
          {displayPosts(filteredPosts)} {/* 필터된 게시글 목록 렌더링 */}
        </tbody>
    </table>

    <div className="flex justify-center gap-2 mt-4">
        <button className="border px-4 py-2 hover:text-red-500">{"<<"}</button>
        <button className="border px-4 py-2 hover:text-red-500">{"<"}</button>
        <button className="border px-4 py-2 text-red-500">1</button>
        <button className="border px-4 py-2 hover:text-red-500">2</button>
        <button className="border px-4 py-2 hover:text-red-500">3</button>
        <button className="border px-4 py-2 hover:text-red-500">4</button>
        <button className="border px-4 py-2 hover:text-red-500">5</button>
        <button className="border px-4 py-2 hover:text-red-500">6</button>
        <button className="border px-4 py-2 hover:text-red-500">7</button>
        <button className="border px-4 py-2 hover:text-red-500">8</button>
        <button className="border px-4 py-2 hover:text-red-500">9</button>
        <button className="border px-4 py-2 hover:text-red-500">10</button>
        <button className="border px-4 py-2 hover:text-red-500">{">"}</button>
        <button className="border px-4 py-2 hover:text-red-500">{">>"}</button>
    </div>

    <form onSubmit={handleSearch} className="mt-4">
        <div className="flex justify-center gap-4 items-center">
        <select
            className="p-2 border"
            name="search-type"
            value={searchType}
            onChange={(e) => setSearchType(e.target.value)}
        >
            <option value="title">제목</option>
            <option value="content">내용</option>
            <option value="title+content">제목+내용</option>
            <option value="author">글쓴이</option>
        </select>
        <input
            type="text"
            placeholder="검색어 입력"
            className="p-2 border"
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
        />
        <button type="submit" className="bg-purple-500 text-white px-4 py-2 rounded">검색</button>
        </div>
    </form>

    <button
        className="absolute bg-white border rounded-lg text-purple-500 font-bold px-4 py-2 right-10 bottom-10"
        onClick={() => window.location.href = '/CreatePost.jsx'}
    >
        글쓰기
    </button>
    </div>
);
};

export default BoardPage;