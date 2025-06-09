import React from "react";
import { Link } from "react-router-dom";
import { useNavigate } from "react-router-dom";
import Call from "../../../image/Support/call.png";

function Suggestion() {
    const navigate = useNavigate();

    const menuItems = [
        { label: "고객지원", path: "/support" },
        { label: "상담신청", path: "/consult" },
        { label: "챗봇", path: "/chatbot" },
        { label: "건의함", path: "/suggestion" },
    ];

    const data = [
        { id: 1, title: "예시 건의 1", author: "홍길동", date: "2025-05-14", confirmed: "확인" },
        { id: 2, title: "예시 건의 2", author: "김철수", date: "2025-05-13", confirmed: "미확인" },
        { id: 3, title: "예시 건의 3", author: "이서윤", date: "2025-05-10", confirmed: "확인" },
        { id: 4, title: "예시 건의 4", author: "박지후", date: "2025-05-11", confirmed: "미확인" },
        { id: 5, title: "예시 건의 5", author: "최민준", date: "2025-05-09", confirmed: "확인" },
        { id: 6, title: "예시 건의 6", author: "장하늘", date: "2025-05-08", confirmed: "미확인" },
        { id: 7, title: "예시 건의 7", author: "윤도현", date: "2025-05-07", confirmed: "확인" },
        { id: 8, title: "예시 건의 8", author: "정세린", date: "2025-05-06", confirmed: "미확인" },
        { id: 9, title: "예시 건의 9", author: "한지민", date: "2025-05-05", confirmed: "확인" },
        { id: 10, title: "예시 건의 10", author: "서지후", date: "2025-05-04", confirmed: "미확인" },
    ];

    return (
        <>
            <div className="mx-auto w-full border-[1px] font-semibold border-[#845EC2]">
                {/* 메뉴 바 */}
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
                    <div className="flex font-semibold border-b-2 pb-2 text-purple-700 text-center">
                        <div className="w-2/5">제목</div>
                        <div className="w-1/5">작성자</div>
                        <div className="w-1/5">작성일</div>
                        <div className="w-1/5">확인여부</div>
                    </div>

                    {data.map((item) => (
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
                    ))}
                </div>

                {/* 페이지네이션 */}
                <div className="flex justify-center mt-4">
                    <div className="flex gap-1">
                        {["<<", "<", "1", "2", "3", ">", ">>"].map((p, i) => (
                            <div
                                key={i}
                                className={`w-[32px] h-[32px] border border-[#000000] flex items-center justify-center cursor-pointer 
                                ${p === "1" ? "text-white bg-[#845EC2]" : "hover:bg-gray-100"}`}
                            >
                                {p}
                            </div>
                        ))}
                    </div>
                </div>

                {/* 고객센터 안내 */}
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