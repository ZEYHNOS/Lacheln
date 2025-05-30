import React, { useState, useEffect } from "react";
import Calendar from "react-calendar";
import "react-calendar/dist/Calendar.css";
import apiClient from "../../../lib/apiClient";
import { Lunar } from 'lunar-javascript';

// 한국 공휴일 목록
const KOREAN_HOLIDAYS = [
    "01-01", // 신정
    "03-01", // 삼일절
    "05-05", // 어린이날
    "06-06", // 현충일
    "08-15", // 광복절
    "10-03", // 개천절
    "10-09", // 한글날
    "12-25", // 성탄절
];

// 음력 공휴일 체크 함수
const isLunarHoliday = (date) => {
    const lunar = Lunar.fromDate(date);
    const lunarMonth = lunar.getMonth();
    const lunarDay = lunar.getDay();

    // 설날(음력 1월 1일), 설 다음날(음력 1월 2일)
    if (lunarMonth === 1 && (lunarDay === 1 || lunarDay === 2)) return true;

    // 설 전날: 설날(음력 1월 1일)의 전날(양력 기준)
    const prevDate = new Date(date);
    prevDate.setDate(date.getDate() + 1); // 하루 뒤가 설날이면 오늘이 설 전날
    const lunarTomorrow = Lunar.fromDate(prevDate);
    if (lunarTomorrow.getMonth() === 1 && lunarTomorrow.getDay() === 1) return true;

    // 부처님오신날 (음력 4월 8일)
    if (lunarMonth === 4 && lunarDay === 8) return true;

    // 추석 연휴 (음력 8월 14일, 15일, 16일)
    if (lunarMonth === 8 && (lunarDay === 14 || lunarDay === 15 || lunarDay === 16)) return true;

    return false;
};

// 오늘 이전 날짜 비활성화 함수
const isPast = (date) => {
    const today = new Date();
    today.setHours(0,0,0,0);
    return date < today;
};

export default function SelectDate({ onDateSelect, cpId, value, setValue, modalStyle }) {
    const [holidays, setHolidays] = useState([]);

    useEffect(() => {
        const year = value?.getFullYear();
        const month = value?.getMonth() + 1;
        if (!cpId) return;
        apiClient
            .get("/holidays", { params: { year, month, companyId: cpId } })
            .then((res) => setHolidays(res.data.holidays || []))
            .catch(() => setHolidays([]));
    }, [value, cpId]);

    const tileDisabled = ({ date, view, activeStartDate }) => {
        if (view !== "month") return false;
        const d = date.toISOString().slice(0, 10);
        const isNotThisMonth = date.getMonth() !== activeStartDate.getMonth() || date.getFullYear() !== activeStartDate.getFullYear();
        // 오늘 이전 날짜 비활성화
        if (isPast(date)) return true;
        return holidays.includes(d) || isNotThisMonth;
    };

    const tileContent = ({ date, view, activeStartDate }) => {
        if (view === "month") {
            const d = date.toISOString().slice(0, 10);
            const day = date.getDay();
            let color = "#111";
            // 오늘 이전 날짜는 회색
            if (isPast(date)) color = "#d1d5db";
            else {
                // 공휴일 체크
                const monthDay = `${String(date.getMonth() + 1).padStart(2, "0")}-${String(date.getDate()).padStart(2, "0")}`;
                const isHoliday = KOREAN_HOLIDAYS.includes(monthDay) || 
                                holidays.includes(d) || 
                                day === 0 || 
                                isLunarHoliday(date);
                if (date.getMonth() !== activeStartDate.getMonth() || date.getFullYear() !== activeStartDate.getFullYear()) {
                    color = "#d1d5db";
                } else if (isHoliday) {
                    color = "#e11d48";
                } else if (day === 6) {
                    color = "#2563eb";
                }
            }
            const today = new Date();
            const isToday = date.getFullYear() === today.getFullYear() && date.getMonth() === today.getMonth() && date.getDate() === today.getDate();
            return (
                <span className="calendar-date-text" style={{ color, display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center', height: 40 }}>
                    {date.getDate()}
                    {isToday && <span className="text-xs text-green-500 mt-1">오늘</span>}
                </span>
            );
        }
        return undefined;
    };

    const tileClassName = ({ date, view }) => {
        if (view !== "month") return "calendar-default";
        return "calendar-default";
    };

    return (
        <div className="flex flex-col items-center justify-center" style={modalStyle}>
            <h3 className="font-bold text-lg mb-2">날짜를 선택해 주세요</h3>
            <Calendar
                onChange={(date) => {
                    setValue(date);
                    const y = date.getFullYear();
                    const m = String(date.getMonth() + 1).padStart(2, "0");
                    const d = String(date.getDate()).padStart(2, "0");
                    onDateSelect(`${y}-${m}-${d}`);
                }}
                value={value}
                tileDisabled={tileDisabled}
                tileClassName={tileClassName}
                tileContent={({ date, view, activeStartDate }) => {
                    if (view === "month") {
                        const d = date.toISOString().slice(0, 10);
                        const day = date.getDay();
                        let color = "#111";
                        if (isPast(date)) color = "#d1d5db";
                        else {
                            const monthDay = `${String(date.getMonth() + 1).padStart(2, "0")}-${String(date.getDate()).padStart(2, "0")}`;
                            const isHoliday = KOREAN_HOLIDAYS.includes(monthDay) || 
                                holidays.includes(d) || 
                                day === 0 || 
                                isLunarHoliday(date);
                            if (date.getMonth() !== activeStartDate.getMonth() || date.getFullYear() !== activeStartDate.getFullYear()) {
                                color = "#d1d5db";
                            } else if (isHoliday) {
                                color = "#e11d48";
                            } else if (day === 6) {
                                color = "#2563eb";
                            }
                        }
                        const today = new Date();
                        const isToday = date.getFullYear() === today.getFullYear() && date.getMonth() === today.getMonth() && date.getDate() === today.getDate();
                        return (
                            <span className="calendar-date-text" style={{ color, display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center', height: 40 }}>
                                {date.getDate()}
                                {isToday && <span className="text-xs text-green-500 mt-1">오늘</span>}
                            </span>
                        );
                    }
                    return undefined;
                }}
                locale="ko-KR"
                className="!border-none !shadow-none calendar-tall"
                style={{ width: "100%" }}
                navigationLabel={({ date }) => `${date.getFullYear()}년 ${date.getMonth() + 1}월`}
                prev2Label={null}
                next2Label={null}
                minDate={new Date()}
                minDetail="month"
                navigationAriaLabel="달력 네비게이션"
            />
            <style>{`
                .react-calendar__month-view__days__day abbr { display: none; }
                .calendar-date-text { font-size: 1rem; font-weight: 500; height: 40px !important; justify-content: center; }
                .react-calendar__month-view__days__day:not(:disabled):hover {
                    background: #22c55e !important;
                    color: #fff !important;
                    border-radius: 12px !important;
                }
                .react-calendar__month-view__days__day:not(:disabled):hover .calendar-date-text {
                    color: #fff !important;
                }
                .react-calendar__navigation button:hover {
                    background: #9333ea !important;
                    color: #fff !important;
                }
            `}</style>
        </div>
    );
}
