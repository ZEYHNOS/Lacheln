import React, { useState } from "react";
import SelectDate from "./SelectDate";
import SelectTime from "./SelectTime";

export default function ScheduleModal({ productId, cpId, onSelect }) {
    const [step, setStep] = useState(1);
    const [selectedDate, setSelectedDate] = useState(null);
    const [calendarValue, setCalendarValue] = useState(null); // 달력에서 보여줄 날짜

    // 1단계: 날짜 선택
    const handleDateSelect = (date) => {
        setSelectedDate(date);
        setCalendarValue(new Date(date));
        setStep(2);
    };

    // 2단계: 시간 선택
    const handleTimeSelect = ({ date, time }) => {
        onSelect({ date, time });
        // 모달 닫기 등 추가 동작 가능
    };

    return (
        <div>
            {step === 1 && (
                <SelectDate
                    onDateSelect={handleDateSelect}
                    cpId={cpId}
                    value={calendarValue}
                    setValue={setCalendarValue}
                />
            )}
            {step === 2 && selectedDate && (
                <SelectTime
                    productId={productId}
                    cpId={cpId}
                    date={selectedDate}
                    onSelect={handleTimeSelect}
                    onBack={() => setStep(1)}
                />
            )}
        </div>
    );
}