import React, { useState, useEffect } from "react";
import Calendar from "react-calendar";
import 'react-calendar/dist/Calendar.css';
import { FaChevronLeft, FaChevronRight } from "react-icons/fa";
import apiClient from "../../../lib/apiClient";

function Schedule() {
    const today = new Date();
    const [value, setValue] = useState(today);
    const [activeStartDate, setActiveStartDate] = useState(new Date(today.getFullYear(), today.getMonth(), 1));
    const [calendarData, setCalendarData] = useState({});
    const year = value.getFullYear();
    const month = value.getMonth() + 1; // 1~12

    // 날짜 포맷 함수
    const formatDate = (date) => {
        return date.toISOString().slice(0, 10);
    };

    // 달 이동 핸들러
    const handlePrevMonth = () => {
        setActiveStartDate(prev => {
            const d = new Date(prev);
            d.setMonth(d.getMonth() - 1);
            return d;
        });
        setValue(prev => {
            const d = new Date(prev);
            d.setMonth(d.getMonth() - 1);
            d.setDate(1);
            return d;
        });
    };
    const handleNextMonth = () => {
        setActiveStartDate(prev => {
            const d = new Date(prev);
            d.setMonth(d.getMonth() + 1);
            return d;
        });
        setValue(prev => {
            const d = new Date(prev);
            d.setMonth(d.getMonth() + 1);
            d.setDate(1);
            return d;
        });
    };

    // 달력 데이터 불러오기
    useEffect(() => {
        const fetchCalendarData = async () => {
            const y = activeStartDate.getFullYear();
            const m = (activeStartDate.getMonth() + 1).toString().padStart(2, '0');
            console.log(`요청하는 달: ${y}-${m}`);
            try {
                const { data } = await apiClient.get(`/calendar/${y}/${m}`);
                console.log(data);
                setCalendarData(data);
            } catch (err) {
                console.error('캘린더 데이터 로딩 실패:', err);
                setCalendarData({});
            }
        };
        fetchCalendarData();
    }, [activeStartDate]);

    // 날짜별 예약 가져오기
    const getReservationsForDate = (date) => {
        const day = date.getDate().toString();
        return calendarData[day] && calendarData[day][0] !== null ? calendarData[day] : [];
    };

    return (
        <div className="bg-white min-h-screen p-0 text-black">
            {/* 월, 이동 버튼 */}
            <div className="flex items-center px-8 pt-8 mb-4">
                <h1 className="text-4xl font-bold text-pp">{year}년 {month}월 예약현황</h1>
                <div className="flex flex-row gap-2 ml-[2vw]">
                    <button onClick={handlePrevMonth} className="bg-pp text-white rounded-full p-2 hover:bg-purple-700">
                        <FaChevronLeft />
                    </button>
                    <button onClick={handleNextMonth} className="bg-pp text-white rounded-full p-2 hover:bg-purple-700">
                        <FaChevronRight />
                    </button>
                </div>
            </div>
            
            {/* 월 구분선 */}
            <div className="w-fit mb-2 px-8">
                <div className="bg-pp h-2 rounded w-1/2" />
            </div>
            
            {/* 캘린더 */}
            <div className="schedule-calendar-wrapper w-full mx-auto my-8 px-0">
                <Calendar
                    value={value}
                    onChange={(date) => {
                        setValue(date);
                        console.log('선택된 날짜:', date);
                    }}
                    activeStartDate={activeStartDate}
                    onActiveStartDateChange={({ activeStartDate }) => setActiveStartDate(activeStartDate)}
                    locale="ko-KR"
                    showNavigation={false}
                    formatShortWeekday={(locale, date) => {
                        const week = ['SUNDAY', 'MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY'];
                        return week[date.getDay()];
                    }}
                    navigationLabel={null}
                    prevLabel={null}
                    nextLabel={null}
                    prev2Label={null}
                    next2Label={null}
                    tileDisabled={({ date }) => {
                        // 이번 달이 아니면 비활성화
                        return date.getMonth() !== value.getMonth() || date.getFullYear() !== value.getFullYear();
                    }}
                    tileContent={({ date, view }) => {
                        if (view === 'month') {
                            const reservations = getReservationsForDate(date);
                            // 요일별 색상 클래스
                            let colorClass = '';
                            const day = date.getDay();
                            if (date.getMonth() !== value.getMonth() || date.getFullYear() !== value.getFullYear()) {
                                colorClass = 'text-gray-300';
                            } else if (day === 0) {
                                colorClass = 'text-red-500';
                            } else if (day === 6) {
                                colorClass = 'text-blue-500';
                            } else {
                                colorClass = 'text-black';
                            }
                            return (
                                <span className={`calendar-date-only ${colorClass}`}>
                                    {date.getDate()}
                                    {reservations.map((r, idx) =>
                                        r && (
                                            <div
                                                key={r.id || idx}
                                                className="reservation-pill"
                                                style={{
                                                    background: r.color,
                                                    color: 'white',
                                                    fontSize: '0.9rem',
                                                    marginTop: 4,
                                                    borderRadius: 8,
                                                    padding: '2px 8px',
                                                    whiteSpace: 'nowrap'
                                                }}
                                            >
                                                {r.title} ({r.starttime})
                                            </div>
                                        )
                                    )}
                                </span>
                            );
                        }
                        return null;
                    }}
                    tileClassName={({ date, view }) => {
                        if (view === 'month') {
                            if (date.getMonth() !== value.getMonth() || date.getFullYear() !== value.getFullYear()) {
                                return 'text-gray-300';
                            }
                            const day = date.getDay();
                            if (day === 0) return 'text-red-500';
                            if (day === 6) return 'text-blue-500';
                            return 'text-black';
                        }
                        return '';
                    }}
                />
                <style>{`
                    .schedule-calendar-wrapper .react-calendar {
                        border: none !important;
                        box-shadow: none !important;
                    }
                    .schedule-calendar-wrapper {
                        width: 100%;
                        max-width: none;
                        min-width: 0;
                        margin: 0 100px 0 0;
                        background: white;
                        border-radius: 0;
                        box-shadow: 0 4px 24px rgba(0,0,0,0.08);
                        padding: 0;
                    }
                    .schedule-calendar-wrapper .react-calendar {
                        width: 100% !important;
                        font-size: 2rem;
                    }
                    .schedule-calendar-wrapper .react-calendar__month-view__weekdays {
                        background: var(--pp);
                        border-radius: 0;
                    }
                    .schedule-calendar-wrapper .react-calendar__month-view__weekdays__weekday {
                        color: white;
                        font-weight: bold;
                        font-size: 1.3rem;
                        padding: 20px 0;
                        letter-spacing: 2px;
                    }
                    .schedule-calendar-wrapper .react-calendar__month-view__weekdays__weekday abbr {
                        text-decoration: none;
                    }
                    .schedule-calendar-wrapper .react-calendar__month-view__weekdays__weekday:last-child {
                        color: #ff4d4f !important;
                    }
                    .schedule-calendar-wrapper .react-calendar__tile {
                        font-size: 1rem;
                        min-width: 100px;
                        min-height: 150px;
                        height: auto;
                        vertical-align: top;
                        padding: 16px 0 0 0;
                        border-radius: 0;
                    }
                    .schedule-calendar-wrapper .react-calendar__tile--active {
                        border: 1px solid var(--pp) !important;
                        color: inherit !important;
                        background: rgb(129, 144, 255) !important;
                    }
                    .reservation-pill {
                        display: inline-block;
                        background: var(--pp);
                        color: white;
                        font-size: 1.1rem;
                        border-radius: 0;
                        padding: 6px 18px;
                        margin-top: 12px;
                        white-space: nowrap;
                    }
                    /* 기본 날짜 숨김 */
                    .schedule-calendar-wrapper .react-calendar__tile abbr {
                        display: none !important;
                    }
                    .calendar-date-only {
                        font-size: 1rem;
                        font-weight: 600;
                        width: 100%;
                        display: flex;
                        justify-content: flex-end;
                        align-items: flex-start;
                        padding: 0 12px 0 0;
                        height: 100%;
                        pointer-events: none;
                    }
                    /* 요일별 세로줄 구분선 */
                    .schedule-calendar-wrapper .react-calendar__month-view__days__day:not(:nth-child(7n+1)) {
                        border-left: 2px solid var(--pp);
                    }
                    /* week별 가로줄 구분선 */
                    .schedule-calendar-wrapper .react-calendar__month-view__days__day {
                        border-top: 2px solid var(--pp);
                    }
                    .schedule-calendar-wrapper .react-calendar__month-view__days__day:nth-child(-n+7) {
                        border-top: 2px solid var(--pp);
                    }
                    /* hover, active, focus 시에도 border와 글씨 색상 유지 */
                    .schedule-calendar-wrapper .react-calendar__tile,
                    .schedule-calendar-wrapper .react-calendar__tile:active,
                    .schedule-calendar-wrapper .react-calendar__tile:focus,
                    .schedule-calendar-wrapper .react-calendar__tile:hover {
                        border: 1px solid var(--pp) !important;
                        color: inherit !important;
                        background: none !important;
                    }
                    /* 선택된 날짜(타일)도 border, 글씨색 유지 */
                    .schedule-calendar-wrapper .react-calendar__tile--active {
                        border: 1px solid var(--pp) !important;
                        color: white !important;
                        font-color: var(--pp) !important;
                    }
                `}</style>
            </div>
        </div>
    );
}

export default Schedule; 