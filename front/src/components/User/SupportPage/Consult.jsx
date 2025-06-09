import React, { useState } from 'react';
import { Link } from "react-router-dom";
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import { setHours, setMinutes } from 'date-fns';

import Person from "../../../image/Support/person.png";
import Smartphone from "../../../image/Support/smartphone.png";
import Email from "../../../image/Support/email.png";
import Weekday from "../../../image/Support/weekday.png";
import Call from "../../../image/Support/call.png";

function formatDate(date) {
    return date.toISOString().split('T')[0]; // yyyy-mm-dd
}

function getWeekdayName(date) {
    return ['일', '월', '화', '수', '목', '금', '토'][date.getDay()];
}

// 시간 필터링 함수
function isFutureTime(date, timeStr) {
    const [hours, minutes] = timeStr.split(':').map(Number);
    const selectedDateTime = new Date(date);
    selectedDateTime.setHours(hours, minutes, 0, 0);
    return selectedDateTime > new Date();
}

export default function Consult() {
    const [selectedDate, setSelectedDate] = useState(null);
    const [selectedTime, setSelectedTime] = useState('');
    const [emailLocal, setEmailLocal] = useState("");
    const [emailDomain, setEmailDomain] = useState("gmail.com");
    const [customDomain, setCustomDomain] = useState("");
    const [showModal, setShowModal] = useState(false);

    // 가상의 예약된 시간 데이터 (날짜 기준)
    const dummyReservedSlots = {
        "2025-06-06": ["10:30", "13:00"],
        "2025-06-10": ["11:00", "15:00"],
    };

    const times = [
        '10:00', '10:30', '11:00', '11:30',
        '13:00', '13:30', '14:00', '14:30',
        '15:00', '15:30', '16:00', '16:30',
        '17:00', '17:30'
    ];

    const menuItems = [
        { label: "고객지원", path: "/support" },
        { label: "상담신청", path: "/consult" },
        { label: "챗봇", path: "/chatbot" },
        { label: "건의함", path: "/suggestion" },
    ];

    const formattedDate = selectedDate ? formatDate(selectedDate) : null;
    const reservedTimes = formattedDate && dummyReservedSlots[formattedDate] ? dummyReservedSlots[formattedDate] : [];

    const [agreeChecked, setAgreeChecked] = useState(false);
    const getFullEmail = (localPart) => {
        return `${localPart}@${emailDomain === "직접 입력" ? customDomain : emailDomain}`;
    };

    const handleSubmit = () => {
        if (!agreeChecked) {
            const fullEmail = getFullEmail(emailLocal);
            console.log("제출 이메일", fullEmail);
            alert("개인정보 수집에 동의해주세요.");
            return;
        }
        // 실제 제출 로직
        alert("상담 신청이 완료되었습니다.");
    };

    return (
        <>
            <div className="mx-auto w-full border-[1px] font-semibold border-[#845EC2]">
                <ul className="flex w-full list-none p-0 m-0 border-[1px] bg-[#FFFFFF] border-[#e1c2ff33]">
                    {menuItems.map((item, idx) => (
                        <li key={item.label}
                            className={`flex items-center justify-center flex-1 border border-[#e1c2ff33] h-[65px]
                            ${idx === 0 ? "border-l-0" : ""} ${idx === 3 ? "border-r-0" : ""}`}>
                            <Link to={item.path}
                                className={`w-full h-full flex items-center justify-center text-[20px] font-semibold
                                    ${item.label === "상담신청" ? "bg-[#E2C5EE] text-[#000000]" : "text-[#615e5e]"}
                                    hover:bg-[#E2C5EE] hover:text-[#000000] hover:underline`}>
                                {item.label}
                            </Link>
                        </li>
                    ))}
                </ul>

                <div className="mt-2 text-[22px] font-bold text-center text-[#845EC2]">간편상담 서비스 신청</div>
                <div className="mt-2 mb-2 text-[16px] font-bold text-center">전문가와의 1:1 연결을 통해 상담을 도와드립니다.</div>

                <div className="mx-auto w-[1240px]">
                    <div className="flex flex-col gap-5 mt-7">
                        {/* 고객명 */}
                        <div className="flex w-[530px] mx-auto border-2 border-[#B39CD0] rounded-[10px] overflow-hidden">
                            <div className="w-[32%] flex items-center justify-center gap-2 border-r-[5px] border-[#B39CD0] bg-[#f3ebf9] py-1.5">
                                <img src={Person} alt="Person Icon" className="w-7 h-7" />
                                고객명
                            </div>
                            <div className="w-[68%] flex items-center pl-3 py-3">
                                <input type="text" placeholder="이름 입력" className="w-full bg-transparent outline-none" />
                            </div>
                        </div>

                        {/* 핸드폰 */}
                        <div className="flex w-[530px] mx-auto border-2 border-[#B39CD0] rounded-[10px] overflow-hidden">
                            <div className="w-[32%] flex items-center justify-center gap-2 border-r-[5px] border-[#B39CD0] bg-[#f3ebf9] py-1.5">
                                <img src={Smartphone} alt="Smartphone Icon" className="w-7 h-7" />
                                핸드폰
                            </div>
                            <div className="w-[68%] flex items-center pl-3 py-3 gap-2">
                                <input type="text" maxLength={3} placeholder="010"
                                    className="w-[25%] bg-transparent outline-none border-b border-gray-300 text-center"
                                    onInput={(e) => e.target.value = e.target.value.replace(/\D/g, '')} />
                                <span className="text-gray-600">-</span>
                                <input type="text" maxLength={4} placeholder="1234"
                                    className="w-[30%] bg-transparent outline-none border-b border-gray-300 text-center"
                                    onInput={(e) => e.target.value = e.target.value.replace(/\D/g, '')} />
                                <span className="text-gray-600">-</span>
                                <input type="text" maxLength={4} placeholder="5678"
                                    className="w-[30%] bg-transparent outline-none border-b border-gray-300 text-center"
                                    onInput={(e) => e.target.value = e.target.value.replace(/\D/g, '')} />
                            </div>
                        </div>

                        {/* 이메일 */}
                        <div className="flex w-[530px] mx-auto border-2 border-[#B39CD0] rounded-[10px] overflow-hidden">
                            <div className="w-[32%] flex items-center justify-center gap-2 border-r-[5px] border-[#B39CD0] bg-[#f3ebf9] py-1.5">
                                <img src={Email} alt="Email Icon" className="w-7 h-7" />
                                이메일
                            </div>
                            <div className="w-[68%] flex items-center pl-3 py-3 gap-2">
                                <input type="text" placeholder="example" className="w-[30%] bg-transparent outline-none border-b border-gray-300"
                                    value={emailLocal} onChange={(e) => setEmailLocal(e.target.value)} />
                                <span>@</span>
                                {emailDomain === "직접 입력" ? (
                                    <input
                                        type="text"
                                        placeholder="직접 입력"
                                        className="w-[40%] bg-transparent outline-none border-b border-gray-300"
                                        value={customDomain}
                                        onChange={(e) => setCustomDomain(e.target.value)}
                                    />
                                ) : (
                                    <select
                                        className="bg-transparent outline-none"
                                        value={emailDomain}
                                        onChange={(e) => setEmailDomain(e.target.value)}
                                    >
                                        <option value="gmail.com">gmail.com</option>
                                        <option value="naver.com">naver.com</option>
                                        <option value="daum.net">daum.net</option>
                                        <option value="nate.com">nate.com</option>
                                        <option value="직접 입력">직접 입력</option>
                                    </select>
                                )}
                            </div>
                        </div>

                        {/* 날짜 및 시간 선택 */}
                        <div className="flex w-[530px] mx-auto border-2 border-[#B39CD0] rounded-[10px] overflow-hidden">
                            <div className="w-[32%] flex items-center justify-center border-r-[5px] border-[#B39CD0] bg-[#f3ebf9] py-1.5">
                                <img src={Weekday} alt="Weekday Icon" className="w-7 h-7 mr-1" />
                                상담 일시
                            </div>
                            <div className="w-[68%] flex flex-col justify-around pl-3 py-3">
                                <DatePicker
                                    selected={selectedDate}
                                    onChange={(date) => {
                                        setSelectedDate(date);
                                        setSelectedTime('');
                                    }}
                                    minDate={new Date()}
                                    dateFormat="yyyy년 MM월 dd일"
                                    placeholderText="날짜 선택"
                                    className="bg-transparent outline-none border-b border-[#ccc] w-full mb-2"
                                    filterDate={(date) => {
                                        const day = date.getDay();
                                        return day !== 0 && day !== 6; // 일(0), 토(6) 제외
                                    }}
                                />
                                {selectedDate && (
                                    <span className="text-sm text-gray-600">
                                        선택된 요일: {getWeekdayName(selectedDate)}
                                    </span>
                                )}
                                <select
                                    className="w-[80%] mt-2 bg-transparent outline-none border-b border-[#ccc]"
                                    value={selectedTime}
                                    onChange={(e) => setSelectedTime(e.target.value)}
                                    disabled={!selectedDate}
                                >
                                    <option value="">시간 선택</option>
                                    {times.map((time) => {
                                        const isReserved = reservedTimes.includes(time);
                                        const isPastTime = selectedDate && !isFutureTime(selectedDate, time);
                                        const isDisabled = isReserved || isPastTime;
                                        return (
                                            <option key={time} value={time} disabled={isDisabled}>
                                                {time} {isReserved ? '⛔ 예약됨' : isPastTime ? '⏳ 지난시간' : ''}
                                            </option>
                                        );
                                    })}
                                </select>
                            </div>
                        </div>
                        {/* 개인정보 동의 */}
                        <div className="text-center mt-5">
                            <input type="checkbox" id="outsideAgree" className="scale-125 accent-[#845EC2]"
                                checked={agreeChecked} onChange={(e) => setAgreeChecked(e.target.checked)} />
                            <label htmlFor="outsideAgree" className="ml-2 text-xl">개인정보 수집동의</label>
                            <button onClick={() => setShowModal(true)} className="ml-2 text-purple-700 underline text-sm">[내용보기]</button>
                        </div>

                        {/* 신청 버튼 */}
                        <button onClick={handleSubmit} className="mx-auto mb-4 px-6 py-2 border-2 border-[#845EC2] rounded shadow-sm bg-[#845EC2] text-white hover:bg-[#9f7ae3]">
                            상담 신청하기
                        </button>
                    </div>
                </div>
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

            {/* 개인정보 모달 */}
            {showModal && (
                <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
                    <div className="bg-white p-6 rounded-xl shadow-lg max-w-md w-full max-h-[80vh] overflow-auto">
                        <h2 className="text-[24px] font-bold mb-4">개인정보 수집 안내</h2>

                        <p className="text-[16px] text-gray-800 mb-4">
                            <span className="text-purple-700 text-[22px] font-semibold">
                                개인정보처리방침
                            </span><br /><br />
                            스,드,메(이하 "회사")는 고객님의 개인정보를 중요하게 생각하며, 관련 법령을 준수하여
                            개인정보를 보호하고 안전하게 관리하고 있습니다. 본 개인정보 처리방침은 고객님께
                            제공하는 서비스에서 개인정보가 어떻게 수집, 이용, 보관, 보호되는지를 설명합니다.
                        </p>

                        {/* 1 */}
                        <div className="text-[22px] font-semibold mb-2">1. 수집하는 개인정보 및 수집 방법</div>
                        <ul className="list-disc list-inside text-[16px] text-gray-800 space-y-1 mb-4">
                            <li><strong>회원가입 및 서비스 이용 시</strong><br />
                                필수정보: 이름, 휴대전화번호, 이메일 주소<br />
                                선택정보: 프로필 사진, 관심 업체 정보
                            </li>
                            <li><strong>서비스 이용 과정에서 자동으로 수집되는 정보</strong><br />
                                접속 로그, IP 주소, 쿠키, 기기 정보
                            </li>
                            <li><strong>고객 문의 및 상담 시</strong><br />
                                문의 내용, 연락처
                            </li>
                        </ul>

                        {/* 2 */}
                        <div className="text-[22px] font-semibold mb-2">2. 개인정보의 이용 목적</div>
                        <ul className="list-disc list-inside text-[16px] text-gray-800 space-y-1 mb-4">
                            <li>회원 가입 및 서비스 이용 관리</li>
                            <li>업체 상담 및 예약 서비스 제공</li>
                            <li>고객 문의 응대 및 서비스 개선</li>
                            <li>마케팅 및 프로모션 정보 제공 (선택 동의 시)</li>
                            <li>서비스 이용 통계 분석 및 시스템 보안 유지</li>
                        </ul>

                        {/* 3 */}
                        <div className="text-[22px] font-semibold mb-2">3. 개인정보의 보관 및 파기</div>
                        <ul className="list-disc list-inside text-[16px] text-gray-800 space-y-1 mb-4">
                            <li>수집된 개인정보는 이용 목적이 달성된 후, 관련 법령에 따라 일정 기간 보관 후 안전하게 삭제됩니다.</li>
                            <li>법령에 의해 보관해야 하는 정보는 해당 기간 동안 보관 후 파기됩니다.</li>
                            <li>개인정보 파기 시, 전자적 파일 형태는 영구 삭제하며, 문서 형태는 파쇄 또는 소각 처리합니다.</li>
                        </ul>

                        {/* 4 */}
                        <div className="text-[22px] font-semibold mb-2">4. 개인정보 제공 및 위탁</div>
                        <ul className="list-disc list-inside text-[16px] text-gray-800 space-y-1 mb-4">
                            <li>회사는 원칙적으로 고객님의 개인정보를 제3자에게 제공하지 않습니다.</li>
                            <li>필요한 경우 고객님의 동의를 받은 후 특정 업체에 정보를 제공할 수 있습니다.</li>
                            <li>신뢰할 수 있는 외부 업체에 개인정보 처리를 위탁할 수 있으며, 이 경우 안전한 관리를 보장합니다.</li>
                        </ul>

                        {/* 5 */}
                        <div className="text-[22px] font-semibold mb-2">5. 개인정보 보호 조치</div>
                        <ul className="list-disc list-inside text-[16px] text-gray-800 space-y-1 mb-4">
                            <li>회사는 개인정보 보호를 위해 보안 시스템을 갖추고 있으며, 접근 권한을 제한하여 관리하고 있습니다.</li>
                            <li>개인정보 유출을 방지하기 위해 정기적인 보안 점검을 실시합니다.</li>
                        </ul>

                        {/* 6 */}
                        <div className="text-[22px] font-semibold mb-2">6. 이용자의 권리 및 행사 방법</div>
                        <ul className="list-disc list-inside text-[16px] text-gray-800 space-y-1 mb-4">
                            <li>고객님은 언제든지 본인의 개인정보를 조회, 수정, 삭제할 수 있으며, 회원 탈퇴를 요청할 수 있습니다.</li>
                            <li>개인정보 관련 문의는 고객센터를 통해 접수할 수 있으며, 회사는 신속하게 처리하겠습니다.</li>
                        </ul>

                        {/* 7 */}
                        <div className="text-[22px] font-semibold mb-2">7. 개인정보처리방침 변경</div>
                        <p className="text-[16px] text-gray-800 mb-4">
                            본 개인정보처리방침은 법령 및 정책 변경에 따라 수정될 수 있으며, 변경 사항은 홈페이지를 통해 사전 공지합니다.
                        </p>

                        {/* 부가 문장 */}
                        <p className="text-[15px] text-gray-800 mb-4">
                            수집된 개인정보는 상담 목적 외에는 사용되지 않으며, 보안적으로 안전하게 보호됩니다.
                        </p>
                        <div className="flex  justify-center items-center mb-4">
                            <input type="checkbox" id="modalAgree" className="mr-2  accent-purple-600 w-5 h-5"
                                checked={agreeChecked} onChange={(e) => setAgreeChecked(e.target.checked)} />
                            <label htmlFor="agree" className="text-[18px] font-semibold text-[#000000]">
                                위 약관에 동의합니다.
                            </label>
                        </div>
                        {/* 닫기 버튼 */}
                        <div className="text-right">
                            <button className="bg-[#845EC2] text-white font-bold" onClick={() => setShowModal(false)}>닫기</button>
                        </div>
                    </div>
                </div>
            )}

        </>
    );
}