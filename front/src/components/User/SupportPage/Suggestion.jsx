import React, { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import Call from "../../../image/Support/call.png";
import apiClient from "../../../lib/apiClient";
import dayjs from "dayjs";

function Suggestion() {
  const navigate = useNavigate();
  const [inquiries, setInquiries] = useState([]);

  const menuItems = [
    { label: "고객지원", path: "/support" },
    { label: "챗봇", path: "/chatbot" },
    { label: "건의함", path: "/suggestion" },
  ];

  useEffect(() => {
    apiClient
      .get("/inquiry")
      .then((res) => {
        console.log("✅ 문의 목록 응답:", res.data);
        setInquiries(res.data.data || []);
      })
      .catch(() => console.error("❌ 문의 목록 조회 실패"));
  }, []);

  return (
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

      <div className="mt-4 ml-8 text-[25px] text-[#845EC2] font-bold">📋 내가 쓴 문의 내역</div>

      <div className="w-full p-4">
        <div className="flex font-semibold border-b-2 pb-2 text-purple-700 text-center">
          <div className="w-2/5">제목</div>
          <div className="w-1/5">상태</div>
          <div className="w-2/5">작성일</div>
        </div>

        {inquiries.length === 0 ? (
          <div className="text-center text-gray-500 mt-8">작성된 문의가 없습니다.</div>
        ) : (
          inquiries.map((item) => (
            <div
              key={item.inquiryId}
              className="flex py-2 border-b hover:bg-purple-50 text-gray-800 text-center cursor-pointer"
              onClick={() => navigate(`/inquiry/${item.inquiryId}`)}
            >
              <div className="w-2/5">{item.title}</div>
              <div
                className={`w-1/5 font-bold ${
                  item.status === "DONE" ? "text-blue-600" : "text-red-500"
                }`}
              >
                {item.status === "DONE" ? "완료" : "접수중"}
              </div>
              <div className="w-2/5">
                {Array.isArray(item.createdAt)
                  ? dayjs(
                      new Date(
                        item.createdAt[0],
                        item.createdAt[1] - 1,
                        item.createdAt[2],
                        item.createdAt[3],
                        item.createdAt[4],
                        item.createdAt[5]
                      )
                    ).format("YYYY.MM.DD")
                  : "날짜 오류"}
              </div>
            </div>
          ))
        )}
      </div>

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
  );
}

export default Suggestion;