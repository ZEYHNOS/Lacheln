import React, { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import Call from "../../../image/Support/call.png";
import apiClient from "../../../lib/apiClient"; // ✅ 백엔드 연동 준비

function Suggestion() {
  const navigate = useNavigate();
  const [suggestions, setSuggestions] = useState([]);

  const menuItems = [
    { label: "고객지원", path: "/support" },
    { label: "챗봇", path: "/chatbot" },
    { label: "건의함", path: "/suggestion" },
  ];

  useEffect(() => {
    // 🔁 추후 백엔드에서 실제 데이터 받아오도록 구현
    apiClient
      .get("/suggestion") // 🔄 여기는 실제 API 엔드포인트에 맞게 수정해야 함
      .then((res) => setSuggestions(res.data.data || []))
      .catch(() => console.error("건의사항 로드 실패"));
  }, []);

  return (
    <>
      <div className="mx-auto w-full border-[1px] font-semibold border-[#845EC2]">
        {/* 탭 메뉴 */}
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
                className={`
                  w-full h-full flex items-center justify-center text-[20px] font-semibold
                  ${item.label === "건의함" ? "bg-[#E2C5EE] text-black" : "text-[#615e5e]"}
                  hover:bg-[#E2C5EE] hover:text-black hover:underline
                `}
              >
                {item.label}
              </Link>
            </li>
          ))}
        </ul>

        <div className="mt-4 ml-8 text-[25px] text-[#845EC2] font-bold">내가 쓴 건의 사항</div>

        <div className="w-full p-4">
          <div className="flex font-semibold border-b-2 pb-2 text-purple-700 text-center">
            <div className="w-2/5">제목</div>
            <div className="w-1/5">작성자</div>
            <div className="w-1/5">작성일</div>
            <div className="w-1/5">확인여부</div>
          </div>

          {suggestions.length === 0 ? (
            <div className="text-center text-gray-500 mt-8">작성된 건의사항이 없습니다.</div>
          ) : (
            suggestions.map((item) => (
              <div
                key={item.id}
                className="flex py-2 border-b hover:bg-purple-50 text-gray-800 text-center cursor-pointer"
                onClick={() => navigate(`/suggestion/${item.id}`)}
              >
                <div className="w-2/5">{item.title}</div>
                <div className="w-1/5">{item.author}</div>
                <div className="w-1/5">{item.date}</div>
                <div className={`w-1/5 font-bold ${item.confirmed === "확인" ? "text-blue-600" : "text-red-500"}`}>
                  {item.confirmed}
                </div>
              </div>
            ))
          )}
        </div>

        {/* 페이지네이션은 추후 구현 가능 */}

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

export default Suggestion;
