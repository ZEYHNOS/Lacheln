import React from 'react';

export default function EditPost() {
    return (
        <div className="container mx-auto p-4">
            <h2 className="text-2xl font-bold text-center mb-4">게시글 수정</h2>
            <hr className="mb-4" />

            <nav className="text-center mb-6">
                <h3 className="text-lg font-semibold mb-2">분류</h3>
                <ul className="flex justify-center gap-4">
                    <li className="board-item2 px-4 py-2 font-bold cursor-pointer">자유게시판</li>
                    <li className="board-item px-4 py-2 font-bold cursor-pointer">후기게시판</li>
                    <li className="board-item px-4 py-2 font-bold cursor-pointer">질문게시판</li>
                </ul>
            </nav>

            <div className="mb-4">
                <h3 className="text-lg font-semibold">제목</h3>
                <input 
                    className="w-full h-10 border border-gray-300 rounded-md px-4" 
                    type="text" 
                    placeholder="수정할 제목을 입력하세요." 
                />
            </div>

            <div className="mb-4">
                <h3 className="text-lg font-semibold">내용</h3>
                <textarea 
                    className="w-full h-40 border border-gray-300 rounded-md px-4 py-2 resize-none" 
                    placeholder="수정할 내용을 입력하세요." 
                />
            </div>
        </div>
    );
}