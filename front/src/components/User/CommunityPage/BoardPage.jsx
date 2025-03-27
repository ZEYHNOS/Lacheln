import React, { useEffect, useState } from 'react';

export default function BoardPage() {
    const [posts, setPosts] = useState([]);

    useEffect(() => {
        // 더미 데이터 (API 연동 시 교체)
        const dummyPosts = [
            { tap: "질문", id: 1, title: "첫 번째 게시글", author: "홍길동", date: "2025-03-21", likes: 10, views: 50 },
            { tap: "자유", id: 2, title: "두 번째 게시글", author: "김철수", date: "2025-03-20", likes: 5, views: 30 },
            { tap: "후기", id: 3, title: "세 번째 게시글", author: "이영희", date: "2025-03-19", likes: 7, views: 40 }
        ];
        setPosts(dummyPosts);
    }, []);

    return (
        <div className="p-4">
            <h2 className="text-center text-2xl font-bold mb-4">전체 게시판</h2>
            <hr className="mb-4" />

            <nav className="flex justify-center mb-4">
                {['자유게시판', '후기게시판', '질문게시판'].map((item) => (
                    <li key={item} className="mx-4 list-none text-purple-600 hover:underline cursor-pointer">{item}</li>
                ))}
            </nav>

            <div className="w-4/5 mx-auto text-center">
                <table className="table-auto w-full border-collapse">
                    <thead>
                        <tr>
                            <th className="border-b py-2">탭</th>
                            <th className="border-b py-2">번호</th>
                            <th className="border-b py-2">제목</th>
                            <th className="border-b py-2">글쓴이</th>
                            <th className="border-b py-2">등록일</th>
                            <th className="border-b py-2">추천</th>
                            <th className="border-b py-2">조회</th>
                        </tr>
                    </thead>
                    <tbody>
                        {posts.map(post => (
                            <tr key={post.id} className="hover:bg-gray-100">
                                <td className="py-2 text-purple-600 font-bold">{post.tap}</td>
                                <td className="py-2">{post.id}</td>
                                <td className="py-2">{post.title}</td>
                                <td className="py-2">{post.author}</td>
                                <td className="py-2">{post.date}</td>
                                <td className="py-2 text-blue-500 font-bold">{post.likes}</td>
                                <td className="py-2">{post.views}</td>
                            </tr>
                        ))}
                    </tbody>
                </table>

                <div className="flex justify-center mt-4 gap-2">
                    {Array.from({ length: 10 }, (_, i) => i + 1).map(num => (
                        <div key={num} className={`w-8 h-8 border flex justify-center items-center cursor-pointer ${num === 1 ? 'border-red-400 text-red-400' : 'border-gray-300'} hover:border-red-400 hover:text-red-400`}>{num}</div>
                    ))}
                </div>

                <form className="flex justify-end items-center gap-2 mt-4">
                    <select className="border p-2">
                        <option value="title">제목</option>
                        <option value="content">내용</option>
                        <option value="title+content" selected>제목+내용</option>
                        <option value="author">글쓴이</option>
                    </select>
                    <input type="text" placeholder="검색어 입력" className="border p-2" />
                    <button type="submit" className="bg-blue-500 text-white px-4 py-2 rounded">검색</button>
                    <button type="button" className="bg-white text-purple-600 border border-purple-600 px-4 py-2 rounded" onClick={() => alert('글쓰기 페이지로 이동')}>글쓰기</button>
                </form>
            </div>
        </div>
    );
}