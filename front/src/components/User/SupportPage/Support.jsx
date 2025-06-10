// Support.jsx (수정 후)
import React, { useState } from "react";
import { Link } from "react-router-dom";
import Help from "../../../image/Support/help.png";
import Call from "../../../image/Support/call.png";

export function Support() {
  const [showModal, setShowModal] = useState(false);

  const menuItems = [
    { label: "고객지원", path: "/support" },
    { label: "챗봇", path: "/chatbot" },
    { label: "건의함", path: "/suggestion" },
  ];

  const faqList = [
    { tag: "이용 방법", question: "로그인 비밀번호를 잊어버렸어요." },
    { tag: "결제 방법", question: "이메일 인증이 되지 않아요." },
    { tag: "정보", question: "결제 내역을 확인하고 싶어요" },
    { tag: "이용 방법", question: "휴면 계정을 복구하고 싶어요." },
    { tag: "이용 방법", question: "회원 탈퇴는 어떻게 하나요?" }
  ];

  return (
    <>
      <div className="mx-auto w-full border-[1px] font-semibold border-[#845EC2]">
        {/* ✅ 네비게이션 바 (상담신청 탭 제거됨) */}
        <ul className="flex w-full list-none p-0 m-0 border bg-white border-[#e1c2ff33]">
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
                  ${item.label === "고객지원" ? "bg-[#E2C5EE] text-black" : "text-[#615e5e]"}
                  hover:bg-[#E2C5EE] hover:text-black hover:underline
                `}
              >
                {item.label}
              </Link>
            </li>
          ))}
        </ul>

        {/* ✅ 상단 안내 문구 및 검색창 제거됨 */}

        <div className="mt-4 ml-8 text-[24px]">자주 묻는 질문</div>
        <hr />
        <div className="mt-4 ml-8">원하는 도움말 확인 후 해결되지 않은 문제는 아래의 문의하기 버튼을 통해 문의할 수 있습니다.</div>

        {/* FAQ 목록 */}
        <div className="mt-4 ml-8 w-full max-w-4xl">
          {faqList.map((faq, index) => (
            <div key={index} className="py-4 border-b border-gray-300 cursor-pointer hover:bg-gray-50 transition">
              <div className="text-[16px] font-medium text-gray-900 mb-1">{faq.tag}</div>
              <div className="text-sm text-gray-600">{faq.question}</div>
            </div>
          ))}
        </div>

        {/* 문의하기 버튼 */}
        <Link to="/inquiry">
          <button className="bg-[#845EC2] text-white px-4 py-2 rounded mt-4 ml-8">
            문의하기
          </button>
        </Link>

        {/* 하단 안내 */}
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

export default Support;
