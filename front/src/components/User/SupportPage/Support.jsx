import React, { useState } from "react";
import { Link } from "react-router-dom";
import Help from "../../../image/Support/help.png";
import Menu from "../../../image/Support/menu.png";
import Arrowdown from "../../../image/Support/arrowdown.png";
import Search from "../../../image/Support/search.png";
import Call from "../../../image/Support/call.png";

export function Support() {
    const [showModal, setShowModal] = useState(false);
    const [showTypeToggle, setShowTypeToggle] = useState(false);

    const menuItems = [
        { label: "고객지원", path: "/support" },
        { label: "상담신청", path: "/consult" },
        { label: "챗봇", path: "/chatbot" },
        { label: "건의함", path: "/suggestion" },
    ];

    const menuItems2 = [
        { label: "계정", path: "/support" },
        { label: "결제", path: "/support" },
        { label: "광고", path: "/support" },
        { label: "이벤트", path: "/support" },
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
                                    ${item.label === "고객지원" ? "bg-[#E2C5EE] text-[#000000]" : "text-[#615e5e]"}
                                    hover:bg-[#E2C5EE] hover:text-[#000000] hover:underline
                                `}
                            >
                                {item.label}
                            </Link>
                        </li>
                    ))}
                </ul>

                {/* 본문 */}
                <div className="mt-6 mx-auto text-center">
                    <div className="text-[24px] mb-2">문의유형 선택</div>
                    <div>문의유형을 선택하면 문의유형에 따라 [자주 찾는 도움말]을 확인할 수 있습니다.</div>
                    <div>찾는 도움말이 보이지 않으면 [도움말 검색]을 이용해 원하는 도움말을 찾아 주세요.</div>
                </div>

                <div className="flex justify-end gap-4 mr-8 mt-2 text-[#845EC2] font-semibold">
                    <img src={Help} alt="Help Icon" className="w-7 h-7" />
                    <div className="cursor-pointer hover:underline">
                        문의방법 자세히보기
                    </div>
                    <img src={Arrowdown} alt="Arrowdown Icon" className="w-7 h-7 cursor-pointer"
                        onClick={() => setShowModal(true)}/>
                    <img src={Menu} alt="Menu Icon" className="w-7 h-7" />
                    <div
                        className="cursor-pointer hover:underline"
                    >
                        문의유형 전체보기
                    </div>
                    <img src={Arrowdown} alt="Arrowdown Icon" className="w-7 h-7 cursor-pointer"
                        onClick={() => setShowTypeToggle(prev => !prev)}/>
                </div>

                {showTypeToggle && (
                    <div className="mt-4 flex justify-center">
                    <ul className="flex justify-center w-[50%] list-none p-0 m-0 border-[1px] bg-purple-200">
                        {menuItems2.map((item, idx) => (
                            <li
                                key={item.label}
                                className={`flex items-center justify-center flex-1 h-[65px]
                                    ${idx === 0 ? "border-l-0" : ""} ${idx === 3 ? "border-r-0" : ""}
                                `}
                            >
                                <Link
                                    to={item.path}
                                    className={`
                                        w-full h-full flex items-center justify-center text-[20px] font-semibold
                                        ${item.label === "" ? "bg-purple-600 text-[#FFFFFF]" : "text-gray-700"}
                                        hover:bg-purple-600 hover:text-[#FFFFFF] hover:underline
                                    `}
                                >
                                    {item.label}
                                </Link>
                            </li>
                        ))}
                    </ul>
                </div>
                )}

                <div className="mt-4 ml-8 text-[24px] text-center">도움말 검색</div>
                <div className="mt-2 ml-8 text-center">
                    검색으로 빠르게 도움말을 찾아보실 수 있습니다.
                    <div className="flex justify-end gap-4 mr-[280px] text-[#845EC2] font-semibold">
                        <div className="relative w-[450px]">
                            <input
                                type="text"
                                name="text"
                                className="w-[450px] h-[40px] border border-gray-700"
                                placeholder="문장이 아닌 '단어'로 검색하세요(특수문자 불가)"
                            />
                            <img src={Search} alt="Search Icon" className="absolute right-3 top-1/2 -translate-y-1/2 w-7 h-7 pointer-events-none" />
                        </div>
                    </div>
                </div>

                <div className="mt-4 ml-8 text-[24px]">자주 묻는 질문</div>
                <hr />
                <div className="mt-4 ml-8">원하는 도움말 확인 후 해결되지 않은 문제는 도움말 하단의 문의하기 버튼을 눌러 문의할 수 있습니다.</div>

                <div className="mt-4 ml-8 w-full max-w-4xl">
                    {faqList.map((faq, index) => (
                        <div key={index} className="py-4 border-b border-gray-300 cursor-pointer hover:bg-gray-50 transition">
                            <div className="text-[16px] font-medium text-gray-900 mb-1">{faq.tag}</div>
                            <div className="text-sm text-gray-600">{faq.question}</div>
                        </div>
                    ))}
                </div>

                <Link to="/inquiry">
                    <button className="bg-[#845EC2] text-white px-4 py-2 rounded mt-4 ml-8">
                        문의하기
                    </button>
                </Link>

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

            {/* 모달 */}
            {showModal && (
                <div
                    className="fixed inset-0 bg-black bg-opacity-40 flex items-center justify-center z-50"
                    onClick={() => setShowModal(false)}
                >
                    <div
                        className="bg-white p-6 rounded shadow-lg w-[400px]"
                        onClick={(e) => e.stopPropagation()}
                    >
                        <div className="text-[20px] font-bold mb-4">문의방법 자세히보기</div>
                        <p>고객센터를 통해 문의할 수 있는 다양한 방법을 소개합니다.</p>
                        <ul className="list-disc pl-5 mt-2 text-sm text-gray-700">
                            <li>자주 묻는 질문 확인</li>
                            <li>도움말 검색 활용</li>
                            <li>1:1 문의하기 버튼 클릭</li>
                            <li>챗봇 이용하기</li>
                            <li>건의함 내용 확인</li>
                        </ul>
                        <button
                            onClick={() => setShowModal(false)}
                            className="mt-4 px-4 py-2 bg-purple-600 text-white rounded hover:bg-purple-700"
                        >
                            닫기
                        </button>
                    </div>
                </div>
            )}
        </>
    );
}

export default Support;