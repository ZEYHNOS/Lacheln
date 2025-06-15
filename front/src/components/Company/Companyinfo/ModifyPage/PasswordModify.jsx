import React, { useState } from "react";

function PasswordModify({ onCancel, onSubmit }) {
    const [oldPassword, setOldPassword] = useState("");
    const [newPassword, setNewPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");

    return (
        <div className="max-w-md mx-auto bg-white rounded-lg p-8">
            <h1 className="text-2xl font-bold mb-2 text-black">비밀번호 변경</h1>
            <p className="mb-1 text-sm">
                <span className="text-[#845EC2] cursor-pointer">안전한 비밀번호로 내정보를 보호</span>하세요
            </p>
            <ul className="mb-4 text-xs">
                <li className="text-red-500">○ 다른 아이디/사이트에서 사용한 적 없는 비밀번호</li>
                <li className="text-[#FFB085]">○ 이전에 사용한 적 없는 비밀번호가 안전합니다.</li>
            </ul>
            <div className="space-y-4">
                <div>
                    <input
                        type="password"
                        className="w-full border-2 border-[#845EC2] bg-white rounded-lg p-4 text-lg text-center focus:outline-none focus:ring-2 focus:ring-[#845EC2] placeholder-gray-400"
                        placeholder="이전 비밀번호"
                        value={oldPassword}
                        onChange={e => setOldPassword(e.target.value)}
                    />
                </div>
                <div>
                    <input
                        type="password"
                        className="w-full border-2 border-[#845EC2] bg-white rounded-lg p-4 text-lg text-center focus:outline-none focus:ring-2 focus:ring-[#845EC2] placeholder-gray-400"
                        placeholder="변경할 비밀번호"
                        value={newPassword}
                        onChange={e => setNewPassword(e.target.value)}
                    />
                    <div className="text-xs text-right text-gray-400 mt-1 pr-2">8~16자의 영문 대소문자, 숫자 및 특수문자 사용</div>
                </div>
                <div>
                    <input
                        type="password"
                        className="w-full border-2 border-[#845EC2] bg-white rounded-lg p-4 text-lg text-center focus:outline-none focus:ring-2 focus:ring-[#845EC2] placeholder-gray-400"
                        placeholder="비밀번호 재확인"
                        value={confirmPassword}
                        onChange={e => setConfirmPassword(e.target.value)}
                    />
                    <div className="text-xs text-right text-gray-400 mt-1 pr-2">비밀번호를 한번 더 입력하세요.</div>
                </div>
            </div>
            <div className="flex gap-4 mt-8">
                <button
                    className="flex-1 bg-[#845EC2] text-white py-3 rounded font-bold text-lg hover:bg-[#6b40b5] transition-colors"
                    onClick={() => onSubmit?.({ oldPassword, newPassword, confirmPassword })}
                >
                    수정
                </button>
            </div>
        </div>
    );
}

export default PasswordModify;
