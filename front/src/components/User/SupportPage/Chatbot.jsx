import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import Lucid from "../../../image/Support/lucid.png";
import Arrowback from "../../../image/Support/arrowback.png";
import Call from "../../../image/Support/call.png";
import Aibot from "../../../image/Support/aibot.png";
import Megaphone from "../../../image/Support/megaphone.png";
import Faq from "../../../image/Support/faq.png";

function Chatbot() {
    const menuItems = [
        { label: "고객지원", path: "/support" },
        { label: "챗봇", path: "/chatbot" },
        { label: "건의함", path: "/suggestion" },
        { label: "신고", path: "/report" },
    ];

    const [messages, setMessages] = useState([
        { from: "bot", text: "안녕하세요 챗봇입니다.\n무엇을 도와드릴까요?" }
    ]);

    const [inputText, setInputText] = useState("");
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [modalType, setModalType] = useState(""); // "chatbot", "notice", "faq"

    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [parentId, setParentId] = useState(null);

    useEffect(() => {
        const handleKeyDown = (e) => {
            if (e.key === "Escape") {
                setIsModalOpen(false);
            }
        };

        if (isModalOpen) {
            document.addEventListener("keydown", handleKeyDown);
        }

        return () => {
            document.removeEventListener("keydown", handleKeyDown);
        };
    }, [isModalOpen]);

    // 🔹 챗봇 모달이 열릴 때, API 호출하여 메시지 불러오기
    useEffect(() => {
        const fetchChatbotMessages = async () => {
            if (modalType === 'chatbot') {
                setLoading(true);
                setError(null);
                try {
                    const response = await axios.get(`/chat-bot/${parentId || 'root'}`);
                    const data = response.data;

                    // ✅ 응답 메시지 추가
                    setMessages((prev) => [...prev, { type: 'bot', text: data.message }]);

                    // ✅ 다음 요청을 위한 parentId 저장
                    if (data.parentId) {
                        setParentId(data.parentId);
                    }

                } catch (err) {
                    setError('챗봇 응답 중 오류가 발생했습니다.');
                } finally {
                    setLoading(false);
                }
            }
        };

        fetchChatbotMessages();
    }, [modalType]);

    const handleSend = () => {
        if (inputText.trim() === "") return;
        setMessages(prev => [...prev, { from: "user", text: inputText }]);
        setInputText("");
    };

    return (
        <div className="mx-auto w-full border font-semibold border-[#845EC2]">
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
                            ${item.label === "챗봇" ? "bg-purple-400 text-white" : "text-black"}
                            ${item.label === "신고" ? "bg-black-100 text-black-500" : ""}
                            hover:bg-purple-400 hover:text-black`}
                        >
                            {item.label}
                        </Link>
                    </li>
                ))}
            </ul>

            <div className="mt-12 mx-auto max-w-6xl px-4 md:px-6">
                {/* 상단 섹션 - FAQ + 사용가이드 */}
                <div className="flex flex-col md:flex-row gap-10">
                    {/* 자주 묻는 질문 */}
                    <div className="w-full md:w-1/2">
                        <h2 className="text-3xl font-semibold text-[#845EC2] mb-6">자주 묻는 질문 TOP 3</h2>
                        <ul className="space-y-5 text-gray-800">
                            {["배송은 얼마나 걸리나요?", "회원가입은 어떻게 하나요?", "결제 수단은 어떤 것이 있나요?"].map((question, index) => (
                                <li key={index} className="flex items-start gap-3">
                                    <div className="w-1.5 h-6 bg-[#AF128D] mt-1 rounded" />
                                    <span className="text-base hover:underline cursor-pointer">{question}</span>
                                </li>
                            ))}
                        </ul>
                    </div>

                    {/* 사용 가이드 */}
                    <div className="mb-4 w-full md:w-1/2 grid grid-cols-1 sm:grid-cols-2 md:grid-cols-1 gap-4">
                        {[
                            {
                                title: "챗봇 이용 방법",
                                text: "궁금한 점을 자연어로 입력해보세요. 예: \"배송 조회하고 싶어요\""
                            },
                            {
                                title: "상담 연결",
                                text: "챗봇으로 해결이 어려운 경우, 실시간 상담원 연결도 가능합니다."
                            },
                            {
                                title: "운영시간",
                                text: "평일 오전 9시부터 오후 6시까지 운영됩니다."
                            }
                        ].map((item, idx) => (
                            <div key={idx} className="bg-white hover:shadow-md transition-shadow p-5 rounded-xl border border-[#E0D4F7]">
                                <h3 className="text-lg font-semibold text-[#845EC2] mb-2">{item.title}</h3>
                                <p className="text-sm text-gray-700">{item.text}</p>
                            </div>
                        ))}
                    </div>
                </div>
            </div>

            {/* 챗봇 시작 버튼 */}
            <div className="flex justify-center gap-20">
                <div className="flex justify-center mb-6">
                    <button
                        onClick={() => {
                            setModalType("chatbot")
                            setIsModalOpen(true);
                        }}
                        className="w-[106px] h-[106px] bg-black text-white px-[12px] py-[10px] rounded-lg shadow-lg text-lg font-bold hover:bg-[#6f47a1] flex flex-col items-center justify-center"
                    >
                        <img src={Aibot} alt="Aibot Icon" className="w-10 h-10 mb-2" />
                        챗봇 상담
                    </button>
                </div>

                <div className="flex justify-center mb-6">
                    <button
                        onClick={() => {
                            setModalType("notice");
                            setIsModalOpen(true);
                        }}
                        className="w-[106px] h-[106px] bg-black text-white px-[12px] py-[10px] rounded-lg shadow-lg text-lg font-bold hover:bg-[#6f47a1] flex flex-col items-center justify-center"
                    >
                        <img src={Megaphone} alt="Megaphone Icon" className="w-10 h-10 mb-2" />
                        공지사항
                    </button>
                </div>

                <div className="flex justify-center mb-6">
                    <button
                        onClick={() => {
                            setModalType("faq");
                            setIsModalOpen(true);
                        }}
                        className="w-[106px] h-[106px] bg-black text-white px-[12px] py-[10px] rounded-lg shadow-lg text-lg font-bold hover:bg-[#6f47a1] flex flex-col items-center justify-center"
                    >
                        <img src={Faq} alt="Faq Icon" className="w-10 h-10 mb-2" />
                        FAQ
                    </button>
                </div>
            </div>
            {/* 모달 전체 통합 렌더링*/}
            {isModalOpen && (
                <div className="fixed inset-0 bg-black bg-opacity-40 flex justify-center items-center z-50">
                    <div className="bg-white rounded-xl shadow-lg w-full max-w-[900px] h-[700px] flex relative">
                        {/* 닫기 버튼 */}
                        <button
                            className="absolute top-4 right-4 w-10 h-10 bg-purple-200 text-purple-700 hover:bg-red-100 hover:text-red-500 text-xl font-bold z-10 px-2 py-1 rounded"
                            onClick={() => setIsModalOpen(false)}
                            aria-label="Close"
                        >
                            ×
                        </button>

                        {/* 챗봇 모달 */}
                        {modalType === "chatbot" && (
                            <div className="flex w-full h-full p-4 gap-4">
                                <div className="hidden md:flex flex-col w-[220px] gap-4 text-sm text-gray-700">
                                    {/* 운영시간 */}
                                    <div className="bg-gradient-to-br from-white to-gray-50 border border-purple-300 p-4 rounded-xl shadow-sm">
                                        <h2 className="text-base font-bold text-purple-800 mb-2">운영시간</h2>
                                        <ul className="space-y-1">
                                            <li>평일: 09:00~18:00</li>
                                            <li>점심: 12:00~13:00</li>
                                            <li>주말/공휴일 휴무</li>
                                        </ul>
                                    </div>

                                    {/* 빠른 메뉴 */}
                                    <div className="bg-gradient-to-br from-white to-gray-50 border border-purple-300 p-4 rounded-xl shadow-sm">
                                        <h2 className="text-base font-bold text-purple-800 mb-2">빠른 메뉴</h2>
                                        <ul className="space-y-1 list-disc list-inside text-[13px] text-[#333]">
                                            <button
                                                className="mt-4 px-4 py-2 bg-white text-[#845EC2] rounded hover:bg-purple-100"
                                                onClick={() => setModalType("faq")}
                                            >
                                                자주 묻는 질문
                                            </button>
                                            <button
                                                className="mt-4 px-4 py-2 bg-white text-[#845EC2] rounded hover:bg-purple-100"
                                                onClick={() => setModalType("notice")}
                                            >
                                                공지사항
                                            </button>
                                        </ul>
                                    </div>

                                    {/* 창 닫는 방법 */}
                                    <div className="bg-gradient-to-br from-white to-gray-50 border border-purple-300 p-4 rounded-xl shadow-sm">
                                        <h2 className="text-base font-bold text-purple-800 mb-2">창 닫는 방법</h2>
                                        <ul className="space-y-1 text-[13px]">
                                            <li>ESC키 누르기</li>
                                            <li>왼쪽 화살표 아이콘 클릭</li>
                                            <li>우측 상단 x버튼 클릭</li>
                                        </ul>
                                    </div>
                                </div>

                                {/* 모바일 운영시간 요약 뱃지 */}
                                <div className="md:hidden flex flex-col w-full">
                                    <div className="bg-[#845EC2] text-white text-sm rounded px-2 py-1 mb-2 self-start">
                                        운영시간: 평일 09:00~18:00
                                    </div>
                                </div>

                                {/* 우측 채팅창 */}
                                <div className="flex-1 relative flex flex-col bg-white border border-gray-300 rounded-lg">
                                    {/* 상단 타이틀 */}
                                    <div className="flex items-center border-b px-4 py-2">
                                        <img
                                            src={Arrowback}
                                            alt="닫기"
                                            className="w-7 h-7 mr-2 cursor-pointer"
                                            onClick={() => setIsModalOpen(false)}
                                        />
                                        <span className="text-lg font-bold text-[#845EC2]">AI 상담사 루시드</span>
                                    </div>

                                    {/* 메시지 영역 */}
                                    <div className="flex-1 overflow-y-auto px-4 py-2 pb-28 flex flex-col gap-2">
                                        {loading && (
                                            <div className="text-center text-gray-500 italic mb-2">
                                                로딩 중입니다...
                                            </div>
                                        )}

                                        {error && (
                                            <div className="text-center text-red-600 font-semibold mb-2">
                                                {error}
                                            </div>
                                        )}
                                        {messages.map((msg, idx) => (
                                            <div
                                                key={idx}
                                                className={`flex ${msg.from === "user" ? "justify-end" : "justify-start"}`}
                                            >
                                                {msg.from === "bot" && (
                                                    <img
                                                        src={Lucid}
                                                        alt="Lucid Icon"
                                                        className="w-8 h-8 mr-2 rounded-full self-start"
                                                    />
                                                )}
                                                <div
                                                    className={`max-w-[70%] p-2 rounded-lg whitespace-pre-wrap ${msg.from === "bot"
                                                        ? "bg-white border border-[#AF128D] text-black"
                                                        : "bg-white border border-[#3D055C] text-black"
                                                        }`}
                                                >
                                                    {msg.text}
                                                </div>
                                            </div>
                                        ))}
                                    </div>

                                    {/* 입력창 */}
                                    <div className="absolute bottom-0 left-0 right-0 bg-white p-2 flex items-center border-t border-gray-300">
                                        <input
                                            type="text"
                                            placeholder="메시지를 입력하세요..."
                                            className="flex-1 border border-gray-300 rounded-lg p-2 mr-2 bg-white text-black"
                                            value={inputText}
                                            onChange={(e) => setInputText(e.target.value)}
                                            onKeyDown={(e) => e.key === "Enter" && handleSend()}
                                        />
                                        <button
                                            className="bg-[#845EC2] text-white px-4 py-2 rounded-lg"
                                            onClick={handleSend}
                                        >
                                            전송
                                        </button>
                                    </div>
                                </div>
                            </div>
                        )}

                        {/* 공지사항 */}
                        {modalType === "notice" && (
                            <div className="p-4 overflow-y-auto w-full h-full">
                                {/* 상단 타이틀 */}
                                <div className="flex items-center border-b px-4 py-2">
                                    <img
                                        src={Arrowback}
                                        alt="닫기"
                                        className="w-6 h-6 mr-2 cursor-pointer"
                                        onClick={() => setIsModalOpen(false)}
                                    />
                                    <span className="text-lg font-bold text-[#845EC2]">공지사항</span>
                                </div>

                                {/* 공지 내용 */}
                                <div className="mt-5 flex flex-col gap-4 text-[15px]">

                                    {/* 공지 1 */}
                                    <div className="bg-[#F5F5F5] p-4 rounded-lg shadow-sm">
                                        <div className="text-[#845EC2] font-semibold mb-1">✅ [업데이트 안내] 챗봇 기능 개선</div>
                                        <div className="text-sm text-gray-600 mb-2">2025.05.30</div>
                                        <p>
                                            안녕하세요, 고객님!<br />
                                            보다 나은 상담 경험을 위해 AI 상담사 루시드의 응답 속도와 정확도가 향상되었습니다.<br />
                                            이제 더 빠르고 정확하게 여러분의 질문에 답변해 드릴 수 있습니다!
                                        </p>
                                    </div>

                                    {/* 공지 2 */}
                                    <div className="bg-[#FFF3CD] p-4 rounded-lg shadow-sm">
                                        <div className="text-[#D97706] font-semibold mb-1">⚠️ [서비스 점검 안내]</div>
                                        <div className="text-sm text-gray-600 mb-2">2025.06.03 00:00 ~ 04:00</div>
                                        <p>
                                            시스템 안정화를 위한 정기 점검이 예정되어 있습니다.<br />
                                            점검 시간 동안 일부 서비스 이용이 제한될 수 있으니 참고 부탁드립니다.
                                        </p>
                                    </div>

                                    {/* 공지 3 */}
                                    <div className="bg-[#E0F7FA] p-4 rounded-lg shadow-sm">
                                        <div className="text-[#00796B] font-semibold mb-1">📌 [이용 시간 변경 안내]</div>
                                        <div className="text-sm text-gray-600 mb-2">적용일: 2025.06.01</div>
                                        <p>
                                            상담 운영 시간이 아래와 같이 변경됩니다.<br /><br />
                                            <span className="font-medium">기존:</span> 평일 09:00 ~ 18:00<br />
                                            <span className="font-medium">변경:</span> 평일 10:00 ~ 17:00<br />
                                            <span className="text-sm text-gray-500">※ 점심시간(12:00 ~ 13:00)은 동일합니다.</span>
                                        </p>
                                    </div>

                                    {/* 공지 4 */}
                                    <div className="bg-[#FCE4EC] p-4 rounded-lg shadow-sm">
                                        <div className="text-[#C2185B] font-semibold mb-1">📣 [개인정보 처리방침 개정 안내]</div>
                                        <p>
                                            새로운 보안 정책에 따라 개인정보 처리방침이 변경되었습니다.<br />
                                            변경된 내용은{" "}
                                            <a
                                                href="/privacy"
                                                className="text-blue-600 underline"
                                                target="_blank"
                                                rel="noopener noreferrer"
                                            >
                                                여기서 확인하실 수 있습니다
                                            </a>
                                        </p>
                                    </div>
                                </div>
                            </div>
                        )}

                        {/* FAQ */}
                        {modalType === "faq" && (
                            <div className="p-4 overflow-y-auto w-full h-full">
                                {/* 상단 타이틀 */}
                                <div className="flex items-center border-b px-4 py-2">
                                    <img
                                        src={Arrowback}
                                        alt="닫기"
                                        className="w-6 h-6 mr-2 cursor-pointer"
                                        onClick={() => setIsModalOpen(false)}
                                    />
                                    <span className="text-lg font-bold text-purple-900">FAQ</span>
                                </div>

                                {/* FAQ 리스트 */}
                                <div className="mt-5 flex flex-col gap-4 text-[15px]">

                                    {/* Q1 */}
                                    <div className="bg-[#F5F5F5] p-4 rounded-lg shadow-sm">
                                        <div className="font-semibold text-purple-800">Q. 챗봇은 어떤 기능을 하나요?</div>
                                        <div className="mt-1 text-gray-700">
                                            AI 상담사 ‘루시드’는 간단한 문의 응답, 자주 묻는 질문 안내, 서비스 이용 방법에 대한 설명 등을 제공합니다.<br></br>
                                            복잡한 상담은 실시간 상담 연결로 이어질 수 있습니다.
                                        </div>
                                    </div>

                                    {/* Q2 */}
                                    <div className="bg-[#F5F5F5] p-4 rounded-lg shadow-sm">
                                        <div className="font-semibold text-purple-800">Q. 실시간 상담 연결은 가능한가요?</div>
                                        <div className="mt-1 text-gray-700">
                                            현재는 AI 상담을 중심으로 운영 중이며, 필요 시 상담 신청을 통해 담당자가 직접 연락드립니다.
                                        </div>
                                    </div>

                                    {/* Q3 */}
                                    <div className="bg-[#F5F5F5] p-4 rounded-lg shadow-sm">
                                        <div className="font-semibold text-purple-800">Q. 상담 가능 시간은 언제인가요?</div>
                                        <div className="mt-1 text-gray-700">
                                            평일: 09:00 ~ 18:00<br />
                                            점심시간: 12:00 ~ 13:00<br />
                                            주말 및 공휴일은 운영하지 않습니다.
                                        </div>
                                    </div>

                                    {/* Q4 */}
                                    <div className="bg-[#F5F5F5] p-4 rounded-lg shadow-sm">
                                        <div className="font-semibold text-purple-800">Q. 답변이 만족스럽지 않아요. 어떻게 하나요?</div>
                                        <div className="mt-1 text-gray-700">
                                            보다 정확한 상담을 위해 구체적인 질문을 해주시거나, 문의하기 메뉴를 통해 직접 접수해 주세요.
                                        </div>
                                    </div>

                                    {/* Q5 */}
                                    <div className="bg-[#F5F5F5] p-4 rounded-lg shadow-sm">
                                        <div className="font-semibold text-purple-800">Q. 개인정보는 안전하게 보호되나요?</div>
                                        <div className="mt-1 text-gray-700">
                                            네. 고객님의 정보는 암호화 및 보안 정책에 따라 안전하게 관리됩니다. 개인정보 처리방침을 참고해 주세요.
                                        </div>
                                    </div>

                                    {/* Q6 */}
                                    <div className="bg-[#F5F5F5] p-4 rounded-lg shadow-sm">
                                        <div className="font-semibold text-purple-800">Q. 챗봇 기록은 저장되나요?</div>
                                        <div className="mt-1 text-gray-700">
                                            사용자와의 대화 내용은 서비스 개선 및 품질 관리를 위해 일정 기간 저장될 수 있으며,<br></br>
                                            자세한 사항은 이용약관을 확인해 주세요.
                                        </div>
                                    </div>
                                </div>
                            </div>
                        )}
                    </div>
                </div>
            )}

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

export default Chatbot;