import React, { useState } from "react";
import SelectDate from "./SelectDate";
import SelectTime from "./SelectTime";

export default function ScheduleModal({ productId, cpId, cpIds, onSelect }) {
    const [step, setStep] = useState(1);
    const [selectedDate, setSelectedDate] = useState(null);
    const [calendarValue, setCalendarValue] = useState(null); // 달력에서 보여줄 날짜
    const [dayOfWeek, setDayOfWeek] = useState(null); // 선택된 날짜의 요일 (0: 월요일 ~ 6: 일요일)

    // 모달 사이즈 통일용 스타일
    const modalStyle = { minWidth: 400, maxWidth: 600, padding: 0 };

    // 1단계: 날짜 선택
    const handleDateSelect = (date, dayOfWeek) => {
        setSelectedDate(date);
        setCalendarValue(new Date(date));
        setDayOfWeek(dayOfWeek);
        setStep(2);
    };

    // cpId(단일) 또는 cpIds(배열) 중 하나만 하위로 넘김
    const companyIds = cpIds && cpIds.length > 0 ? cpIds : (cpId ? [cpId] : []);

    return (
        <div className="flex flex-col items-center justify-center">
            <div className="bg-white rounded-2xl shadow-lg" style={modalStyle}>
                {step === 1 && (
                    <SelectDate
                        onDateSelect={handleDateSelect}
                        cpIds={companyIds}
                        value={calendarValue}
                        setValue={setCalendarValue}
                        modalStyle={modalStyle}
                    />
                )}
                {step === 2 && selectedDate && (
                    <SelectTime
                        productId={productId}
                        cpId={companyIds[0]}
                        date={selectedDate}
                        dayOfWeek={dayOfWeek}
                        onSelect={onSelect}
                        onBack={() => setStep(1)}
                        modalStyle={modalStyle}
                    />
                )}
            </div>
        </div>
    );
}