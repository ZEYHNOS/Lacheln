import React, { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import Help from "../../../image/Support/help.png";
import Call from "../../../image/Support/call.png";
import apiClient from "../../../lib/apiClient";

export default function Support() {
  const navigate = useNavigate();
  const [tierValue, setTierValue] = useState(""); // ✅ 상태 이름 변경

  useEffect(() => {
    apiClient
      .get("/auth/me")
      .then((res) => {
        const rawTier = res.data?.data?.tier;
        console.log("✅ tier:", rawTier);
        setTierValue(rawTier?.toUpperCase() || "");
      })
      .catch(() => setTierValue(""));
  }, []);

  const canInquire = [
    "AMATEUR",
    "SEMI_PRO",
    "PROFESSIONAL",
    "WORLD_CLASS",
    "CHALLENGER",
    "ADMIN"
  ].includes(tierValue);

  const faqList = [
    { tag: "계정", question: "로그인 비밀번호를 잊어버렸어요." },
    { tag: "결제", question: "카드 인증이 되지 않아요." },
    { tag: "결제", question: "결제 내역을 확인하고 싶어요." },
    { tag: "계정", question: "휴면 계정을 복구하고 싶어요." },
    { tag: "광고", question: "광고 신청은 어떻게 하나요?" },
    { tag: "이벤트", question: "이벤트 당첨 내역은 어떻게 확인하나요?" }
  ];

  const menuItems = [
    { label: "고객지원", path: "/support" },
    { label: "챗봇", path: "/chatbot" },
    { label: "건의함", path: "/suggestion" },
    { label: "신고", path: "/report" },
  ];

  return (
    <div className="mx-auto w-full font-semibold border border-[#845EC2] bg-white">
      {/* 탭 메뉴 */}
      <ul className="flex list-none m-0 p-0 border-b border-[#e1c2ff33]">
        {menuItems.map((item) => (
          <li
            key={item.label}
            className="flex-1 text-center h-[60px] border-r last:border-r-0 border-[#e1c2ff33]"
          >
            <Link
              to={item.path}
              className={`flex items-center justify-center w-full h-full text-[18px] font-semibold
              ${item.label === "고객지원" ? "bg-purple-400 text-white" : "text-black"}
              ${item.label === "신고" ? "bg-black-100 text-black-500" : ""}
              hover:bg-purple-400 hover:text-black`}
            >
              {item.label}
            </Link>
          </li>
        ))}
      </ul>

      {/* FAQ 영역 */}
      <div className="p-8">
        <h2 className="text-[22px] font-bold text-[#845EC2] mb-2">자주 묻는 질문</h2>
        <p className="text-sm text-gray-600 mb-4">
          원하는 도움말 확인 후 해결되지 않은 문제는 아래의 <strong>문의하기</strong> 버튼을 통해 문의할 수 있습니다.
        </p>

        <div className="divide-y border border-gray-300 rounded overflow-hidden">
          {faqList.map((faq, idx) => (
            <div key={idx} className="p-4 hover:bg-gray-50 transition">
              <div className="text-[#845EC2] text-[15px] font-bold">{faq.tag}</div>
              <div className="text-gray-700 text-sm mt-1">{faq.question}</div>
            </div>
          ))}
        </div>

        {/* 문의하기 버튼 */}
        <div className="mt-6">
          {canInquire ? (
            <Link to="/inquiry">
              <button className="bg-[#845EC2] text-white px-5 py-2 rounded hover:bg-[#6d44a5] transition">
                문의하기
              </button>
            </Link>
          ) : (
            <button
              disabled
              onClick={() => navigate("/login")}
              className="bg-gray-300 text-white px-5 py-2 rounded cursor-not-allowed opacity-50"
            >
              로그인 후 이용 가능
            </button>
          )}
        </div>
      </div>

      {/* 하단 고객센터 안내 */}
      <div className="w-full border-t-2 border-[#845EC2] bg-purple-400 text-center py-5">
        <div className="text-[20px] font-bold">고객센터 이용안내</div>
        <div className="flex items-center justify-center gap-2 text-[16px] font-bold mt-1">
          <img src={Call} alt="Call Icon" className="w-6 h-6" />
          월~금 10:00~18:00
        </div>
        <div className="mt-1 text-[14px] text-gray-600 font-bold">(점심 12:00~13:00)</div>
      </div>
    </div>
  );
}