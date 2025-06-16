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
    { label: "신고", path: "/report" },
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
    <div className="mx-auto w-full border-[1px] font-semibold border-[#845EC2] min-h-[700px] flex flex-col">
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
                    ${item.label === "건의함" ? "bg-purple-400 text-white" : "text-black"}
                    ${item.label === "신고" ? "bg-black-100 text-black-500" : ""}
                    hover:bg-purple-400 hover:text-black`}
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
                className={`w-1/5 font-bold ${item.status === "COMPLETED" ? "text-blue-600" : "text-red-500"
                  }`}
              >
                {item.status === "COMPLETED" ? "완료" : "진행중"}
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

      {/* 하단 고객센터 안내 */}
      <div className="w-full border-t-2 border-[#845EC2] bg-purple-400 text-center py-5 mt-auto">
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

export default Suggestion;