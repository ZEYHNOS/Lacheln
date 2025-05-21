import React from "react";
import { Link } from "react-router-dom";
import Call from "../../../image/Support/call.png";
function Suggestion() {


    const menuItems = [
        { label: "고객지원", path: "/support" },
        { label: "상담신청", path: "/consult" },
        { label: "챗봇", path: "/chatbot" },
        { label: "건의함", path: "/suggestion" },
    ];
    const data = [
        { title: "예시 건의 1", author: "홍길동", date: "2025-05-14", confirmed: "확인" },
        { title: "예시 건의 2", author: "김철수", date: "2025-05-13", confirmed: "미확인" },
        { title: "예시 건의 3", author: "이서윤", date: "2025-05-10", confirmed: "확인" },
        { title: "예시 건의 4", author: "박지후", date: "2025-05-11", confirmed: "미확인" },
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
        ${idx === 0 ? "border-l-0" : ""} ${idx === 3 ? "border-r-0" : ""}
    `}
                        >
                            <Link
                                to={item.path}
                                className={`
            w-full h-full flex items-center justify-center text-[20px] font-semibold
            ${item.label === "건의함" ? "bg-[#E2C5EE] text-[#000000]" : "text-[#615e5e]"}
            hover:bg-[#E2C5EE] hover:text-[#000000] hover:underline
        `}
                            >
                                {item.label}
                            </Link>
                        </li>

                    ))}
                </ul>

                <div className="mt-4 ml-8 text-[25px] text-[#845EC2]">내가 쓴 건의 사항</div>

                <div className="w-full p-4">

                    {/* 헤더 */}
                    <div className="flex font-semibold border-b-2 pb-2 text-purple-700 text-center">
                        <div className="w-2/5">제목</div>
                        <div className="w-1/5">작성자</div>
                        <div className="w-1/5">작성일</div>
                        <div className="w-1/5">확인여부</div>
                    </div>

                    {/* 데이터 행 */}
                    {data.map((item, index) => (
                        <div
                            key={index}
                            className="flex py-2 border-b hover:bg-purple-50 text-gray-800 text-center"
                        >
                            <div className="w-2/5">{item.title}</div>
                            <div className="w-1/5">{item.author}</div>
                            <div className="w-1/5">{item.date}</div>
                            <div className="w-1/5">{item.confirmed}</div>
                        </div>
                    ))}

                </div>
                {/* 페이지네이션 */}
                <div className="flex justify-center mt-4">
                    <div className="flex gap-1">
                        <div className="w-[32px] h-[32px] border border-[#000000] flex items-center justify-center cursor-pointer hover:bg-gray-100">{'<<'}</div>
                        <div className="w-[32px] h-[32px] border border-[#000000] flex items-center justify-center cursor-pointer hover:bg-gray-100">{'<'}</div>
                        <div className="w-[32px] h-[32px] text-[#FFFFFF] bg-[#845EC2] border border-[#000000] flex items-center justify-center cursor-pointer">1</div>
                        <div className="w-[32px] h-[32px] border border-[#000000] flex items-center justify-center cursor-pointer hover:bg-gray-100">2</div>
                        <div className="w-[32px] h-[32px] border border-[#000000] flex items-center justify-center cursor-pointer hover:bg-gray-100">3</div>
                        <div className="w-[32px] h-[32px] border border-[#000000] flex items-center justify-center cursor-pointer hover:bg-gray-100">{'>'}</div>
                        <div className="w-[32px] h-[32px] border border-[#000000] flex items-center justify-center cursor-pointer hover:bg-gray-100">{'>>'}</div>
                    </div>
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

export default Suggestion;