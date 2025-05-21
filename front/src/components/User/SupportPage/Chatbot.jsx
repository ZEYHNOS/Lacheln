import React, { useState } from "react";
import { Link } from "react-router-dom";
import Lucid from "../../../image/Support/lucid.png";
import Arrowback from "../../../image/Support/arrowback.png";
import Aibot from "../../../image/Support/aibot.png";
import Megaphone from "../../../image/Support/megaphone.png";
import Faq from "../../../image/Support/faq.png";

function Chatbot() {
    const menuItems = [
        { label: "고객지원", path: "/support" },
        { label: "상담신청", path: "/consult" },
        { label: "챗봇", path: "/chatbot" },
        { label: "건의함", path: "/suggestion" },
    ];

    const [messages, setMessages] = useState([
        { from: "bot", text: "안녕하세요 챗봇입니다.\n무엇을 도와드릴까요?" }
    ]);

    const [inputText, setInputText] = useState("");

    const handleSend = () => {
        if (inputText.trim() === "") return;
        setMessages([...messages, { from: "user", text: inputText }]);
        setInputText("");
    };

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
                                className={`w-full h-full flex items-center justify-center text-[20px] font-semibold
                                    ${item.label === "챗봇" ? "bg-[#E2C5EE] text-[#000000]" : "text-[#615e5e]"}
                                    hover:bg-[#E2C5EE] hover:text-[#000000] hover:underline
                                `}
                            >
                                {item.label}
                            </Link>
                        </li>
                    ))}
                </ul>

                <div className="mx-auto w-full font-semibold flex justify-center gap-6 mt-4">

                <div className="hidden md:flex flex-col w-[200px] bg-[#fff8fc] p-4 rounded-lg shadow-sm">
                    <div className="bg-[#fff8fc] border border-[#845EC2] p-4 rounded-lg shadow">
                        <h2 className="text-lg font-semibold text-[#AF128D] mb-2">운영시간</h2>
                        <p className="text-sm text-gray-700">평일: 09:00~18:00</p>
                        <p className="text-sm text-gray-700">점심: 12:00~13:00</p>
                        <p className="text-sm text-gray-700">주말/공휴일 휴무</p>
                    </div>
                </div>

                {/* 챗봇 박스 */}
                <div className="w-[480px] h-[580px] mt-4 border border-[#AF128D] bg-[#e1c2ff33] rounded-xl shadow-lg p-4 relative flex flex-col mx-auto">

                    {/* 챗봇 박스 내부 상단바 */}
                    <div className="w-full h-12 mt-2 mb-4 bg-white border-b border-[#E0E0E0] flex items-center px-4">
                        <img src={Arrowback} alt="Arrowback Icon" className="w-6 h-6 mb-2" />
                        <span className="text-lg font-bold text-[#845EC2]">AI 상담사 루시드</span>
                    </div>
                    <div className="w-full h-[1px] mb-4 bg-[#845EC2]"></div>

                    {/* 채팅창 영역 */}
                    <div className="flex-1 overflow-y-auto flex flex-col gap-2 pb-16 px-2">
                        {messages.map((msg, idx) => (
                            <div
                                key={idx}
                                className={`flex items-start ${msg.from === "user" ? "justify-end" : "justify-start"} px-2`}
                            >
                                {msg.from === "bot" && (
                                    <img
                                        src={Lucid}
                                        alt="Lucid Icon"
                                        className="w-10 h-10 mr-2 rounded-full self-start"
                                    />
                                )}
                                <div
                                    className={`max-w-[70%] p-2 rounded-lg whitespace-pre-wrap ${msg.from === "bot"
                                        ? "bg-white border border-[#AF128D] text-black"
                                        : "bg-[#845EC2] text-white"
                                        }`}
                                >
                                    {msg.text}
                                </div>
                            </div>
                        ))}
                    </div>

                    {/* 입력창 */}
                    <div className="absolute bottom-0 left-0 right-0 bg-white p-2 flex items-center border-t">
                        <input
                            type="text"
                            placeholder="메시지를 입력하세요..."
                            className="flex-1 border border-gray-300 rounded-lg p-2 mr-2"
                            value={inputText}
                            onChange={(e) => setInputText(e.target.value)}
                            onKeyDown={(e) => { if (e.key === "Enter") handleSend(); }}
                        />
                        <button
                            className="bg-[#845EC2] text-white px-4 py-2 rounded-lg"
                            onClick={handleSend}
                        >
                            전송
                        </button>
                    </div>
                </div>

                <div className="hidden md:flex flex-col w-[200px] bg-[#f4edff] p-4 rounded-lg shadow-sm">
                    <div className="bg-[#f4edff] border border-[#845EC2] p-4 rounded-lg shadow">
                        <h2 className="text-lg font-semibold text-[#845EC2] mb-2">빠른 메뉴</h2>
                        <ul className="text-sm text-gray-700 space-y-1">
                            <li><a href="/faq" className="hover:underline">자주 묻는 질문</a></li>
                            <li><a href="/notice" className="hover:underline">공지사항</a></li>
                            <li><a href="/consult" className="hover:underline">상담 신청</a></li>
                        </ul>
                    </div>
                </div>
                </div>

                {/* 하단 챗봇 안내 박스 */}
                <div className="w-full h-[130px] border-t-2 border-[#845EC2] bg-[#000000] text-center py-5 mt-10">
                    <div className="flex justify-center gap-44">
                        <div className="flex flex-col items-center">
                            <img src={Aibot} alt="Aibot Icon" className="w-10 h-10 mb-2" />
                            <div className="text-[22px] text-white font-bold">챗봇 상담</div>
                        </div>
                        <div className="flex flex-col items-center">
                            <img src={Megaphone} alt="Megaphone Icon" className="w-10 h-10 mb-2" />
                            <div className="text-[22px] text-white font-bold">공지사항</div>
                        </div>
                        <div className="flex flex-col items-center">
                            <img src={Faq} alt="Faq Icon" className="w-10 h-10 mb-2" />
                            <div className="text-[22px] text-white font-bold">FAQ</div>
                        </div>
                    </div>
                </div>
            </div>
        </>
    );
}

export default Chatbot;