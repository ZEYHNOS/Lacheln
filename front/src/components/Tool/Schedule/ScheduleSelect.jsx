import React, { useState } from "react";
import SelectDate from "./SelectDate";
import SelectTime from "./SelectTime";

export default function ScheduleModal({ productId, cpId, onSelect }) {
    const [step, setStep] = useState(1);
    const [selectedDate, setSelectedDate] = useState(null);
    const [calendarValue, setCalendarValue] = useState(null); // 달력에서 보여줄 날짜

    // 모달 사이즈 통일용 스타일
    const modalStyle = { minWidth: 400, maxWidth: 600, padding: 0 };

    // 1단계: 날짜 선택
    const handleDateSelect = (date) => {
        setSelectedDate(date);
        setCalendarValue(new Date(date));
        setStep(2);
    };

    return (
        <div className="flex flex-col items-center justify-center">
            <div className="bg-white rounded-2xl shadow-lg" style={modalStyle}>
                {step === 1 && (
                    <SelectDate
                        onDateSelect={handleDateSelect}
                        cpId={cpId}
                        value={calendarValue}
                        setValue={setCalendarValue}
                        modalStyle={modalStyle}
                    />
                )}
                {step === 2 && selectedDate && (
                    <SelectTime
                        productId={productId}
                        cpId={cpId}
                        date={selectedDate}
                        onSelect={onSelect}
                        onBack={() => setStep(1)}
                        modalStyle={modalStyle}
                    />
                )}
            </div>
        </div>
    );
}