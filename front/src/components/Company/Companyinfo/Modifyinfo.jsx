import React, { useState } from "react";

function Modifyinfo() {
    const [tab, setTab] = useState("profile");

    return (
        <div className="text-black w-full bg-white">
            {/* 상단 탭 버튼 */}
            <div className="flex space-x-4 items-center px-2 pt-2 bg-white">
                <button
                    className={`font-bold bg-white text-base pb-1 border-b-2 transition-all duration-150 text-[#845EC2] focus:outline-none focus:ring-0 active:outline-none active:ring-0 ${tab === "profile" ? "border-[#845EC2]" : "border-transparent"}`}
                    style={{ boxShadow: "none" }}
                    onClick={() => setTab("profile")}
                    tabIndex={0}
                >
                    프로필설정
                </button>
                <button
                    className={`font-bold bg-white text-base pb-1 border-b-2 transition-all duration-150 text-[#845EC2] focus:outline-none focus:ring-0 active:outline-none active:ring-0 ${tab === "security" ? "border-[#845EC2]" : "border-transparent"}`}
                    style={{ boxShadow: "none" }}
                    onClick={() => setTab("security")}
                    tabIndex={0}
                >
                    비밀번호변경
                </button>
                <button
                    className={`font-bold bg-white text-base pb-1 border-b-2 transition-all duration-150 text-[#845EC2] focus:outline-none focus:ring-0 active:outline-none active:ring-0 ${tab === "work" ? "border-[#845EC2]" : "border-transparent"}`}
                    style={{ boxShadow: "none" }}
                    onClick={() => setTab("work")}
                    tabIndex={0}
                >
                    업무시간설정
                </button>
            </div>

            {/* 탭별 내용 */}
            <div className="mt-6">
                {tab === "profile" && (
                    <div>
                        <h1 className="mb-4">프로필설정</h1>
                        <p>프로필 정보 수정 화면입니다.</p>
                    </div>
                )}
                {tab === "security" && (
                    <div>
                        <h1 className="mb-4">비밀번호변경</h1>
                        <p>비밀번호 재설정 화면입니다.</p>
                    </div>
                )}
                {tab === "work" && (
                    <div>
                        <h1 className="mb-4">업무시간설정</h1>
                        <p>근무 관리 및 출퇴근 기록 화면입니다.</p>
                    </div>
                )}
            </div>
            <style>{`
                button:focus, button:active, button:hover {
                    outline: none !important;
                    box-shadow: none !important;
                    border-color: inherit !important;
                    color: #845EC2 !important;
                    background: white !important;
                }
            `}</style>
        </div>
    );
}

export default Modifyinfo; 