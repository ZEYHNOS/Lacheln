import React from 'react';

const PostCreation = () => {
    return (
        <div className="flex flex-col items-center">
            <h2 className="text-center text-2xl font-bold mb-4">게시글 작성</h2>
            <hr className="w-full mb-4" />

            <nav className="text-center mb-5">
                <h3 className="text-lg font-semibold mb-2">분류</h3>
                <ul className="flex justify-center space-x-4">
                    <li className="board-item2 px-6 py-2 font-bold cursor-pointer">자유게시판</li>
                    <li className="board-item px-6 py-2 font-bold cursor-pointer">후기게시판</li>
                    <li className="board-item px-6 py-2 font-bold cursor-pointer">질문게시판</li>
                </ul>
            </nav>

            <div className="w-[1200px] h-[400px] mx-auto text-center">
                <h4 className="text-left mb-2">제목</h4>
                <input
                    className="w-full h-10 box-border text-center border border-gray-300 rounded-md"
                    type="text"
                    placeholder="제목을 입력하세요."
                />

                <h4 className="text-left mt-4 mb-2">내용</h4>
                <textarea
                    className="w-full h-[150px] box-border p-4 border border-gray-300 rounded-md"
                    placeholder="내용을 입력하세요."
                />
            </div>

            <div className="absolute flex gap-3 right-16 bottom-16">
                <button className="px-4 py-2 bg-blue-500 text-white rounded-md">저장</button>
                <button
                    className="px-4 py-2 bg-yellow-500 text-white rounded-md"
                    type="button"
                    onClick={() => (window.location.href = 'update-post.html')}
                >
                    수정
                </button>
                <button className="px-4 py-2 bg-red-500 text-white rounded-md">취소</button>
            </div>
        </div>
    );
};

export default PostCreation;