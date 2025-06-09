import React from "react";
import { Link } from "react-router-dom";
import Call from "../../../image/Support/call.png";
import 'react-quill/dist/quill.snow.css';
import { FaPaperPlane } from "react-icons/fa";

function InquiryForm() {
    const menuItems = [
        { label: "고객지원", path: "/support" },
        { label: "상담신청", path: "/consult" },
        { label: "챗봇", path: "/chatbot" },
        { label: "건의함", path: "/suggestion" },
    ];

    return (
        <>
            {/* 메인 폼 컨테이너 */}
            <div className="mx-auto w-full border-[1px] font-semibold border-[#845EC2]">
                {/* 네비게이션 바 */}
                <ul className="flex w-full list-none p-0 m-0 border-[1px] bg-[#FFFFFF] border-[#e1c2ff33]">
                    {menuItems.map((item, idx) => (
                        <li
                            key={item.label}
                            className={`flex items-center justify-center flex-1 border border-[#e1c2ff33] h-[65px]
                                ${idx === 0 ? "border-l-0" : ""} ${idx === menuItems.length - 1 ? "border-r-0" : ""}
                            `}
                        >
                            <Link
                                to={item.path}
                                className={`w-full h-full flex items-center justify-center text-[20px] font-semibold
                                    ${item.label === "고객지원" ? "bg-[#E2C5EE] text-[#000000]" : "text-[#615e5e]"}
                                    hover:bg-[#E2C5EE] hover:text-[#000000] hover:underline
                                `}
                            >
                                {item.label}
                            </Link>
                        </li>
                    ))}
                </ul>
                    <div className="p-8">
      {/* 제목 */}
        <div className="text-[25px] text-[#845EC2] font-bold mb-2">문의작성</div>
        <div className="text-[20px] mb-1">제목</div>
        <input
        type="text"
        className="w-full max-w-[1400px] h-[35px] border border-[#845EC2] rounded px-2 mb-4"
        placeholder="제목을 입력하세요"
        />

      {/* 본문 에디터 (여기선 예시용 div로 처리) */}
        <div className="border border-[#845EC2] rounded w-full max-w-[1400px] min-h-[250px] mb-4 p-2">
        {/* 여기에 rich text editor 라이브러리 사용 가능 */}
        <textarea
        className="border border-[#845EC2] rounded w-full max-w-[1400px] min-h-[250px] mb-4 p-2"
        placeholder="문의 내용을 입력해주세요."
    />

        {/* 입력창 + 아이콘 버튼 */}
        <div className="flex items-center border-t border-[#845EC2] mt-2 pt-2 px-2">
            <input
            type="text"
            className="flex-1 h-[35px] border border-[#845EC2] rounded-full px-4 text-sm outline-none"
            placeholder=""
            />
            <button className="ml-2 text-[#845EC2]">
            <FaPaperPlane className="w-5 h-5" />
            </button>
        </div>
        </div>

      {/* 등록 버튼 */}
        <button className="mt-2 px-6 py-1 border border-[#845EC2] rounded text-[#845EC2] hover:bg-[#845EC2] hover:text-white transition">
        등록
        </button>
    </div>

                {/* 하단 고객센터 안내 박스 */}
                <div className="w-full border-t-2 border-[#845EC2] bg-[#e1c2ff66] text-center py-5 mt-10">
                    <div className="text-[24px] font-bold">고객센터 이용안내</div>
                    <div className="flex items-center justify-center gap-2 text-[18px] font-bold">
                        <img src={Call} alt="Call Icon" className="w-10 h-10" />
                        월~금 10:00~18:00
                    </div>
                    <div className="mt-1 text-[16px] text-gray-600 font-bold">(점심 12:00~13:00)</div>
                </div>

            </div>
        </>
    );
}

export default InquiryForm;