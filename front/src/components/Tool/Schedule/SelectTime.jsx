import React, { useState, useEffect } from "react";
import apiClient from "../../../lib/apiClient";

// 시간 문자열을 분 단위로 변환
const timeToMinutes = (time) => {
    const [h, m] = time.split(":").map(Number);
    return h * 60 + m;
};

// 30분 단위 슬롯 생성
const generateTimeSlots = (start, end, reserved) => {
    const slots = [];
    let cur = timeToMinutes(start);
    const endMin = timeToMinutes(end);

    while (cur < endMin) {
        const hour = String(Math.floor(cur / 60)).padStart(2, "0");
        const min = String(cur % 60).padStart(2, "0");
        const time = `${hour}:${min}`;
        // 예약된 시간인지 확인
        const blocked = reserved.some(
            (r) => cur >= timeToMinutes(r.start) && cur < timeToMinutes(r.end)
        );
        slots.push({ time, blocked });
        cur += 30;
    }
    return slots;
};

export default function SelectTime({ productId, cpId, date, onSelect, onBack }) {
    const [workHours, setWorkHours] = useState(null); // {start: "10:00", end: "19:00"}
    const [reserved, setReserved] = useState([]); // [{start: "12:00", end: "14:00"}]
    const [slots, setSlots] = useState([]);
    const [selectedTime, setSelectedTime] = useState(null);

    // 날짜 변경 시 업무시간/예약이력 불러오기
    useEffect(() => {
        if (!cpId || !productId || !date) return;
        Promise.all([
            apiClient.get(`/company/${cpId}/workhours`, { params: { date } }),
            apiClient.get(`/product/${productId}/reserved`, { params: { date } }),
        ])
            .then(([workRes, reservedRes]) => {
                setWorkHours(workRes.data);
                setReserved(reservedRes.data || []);
            })
            .catch(() => {
                setWorkHours(null);
                setReserved([]);
            });
    }, [cpId, productId, date]);

    // 시간 슬롯 생성
    useEffect(() => {
        if (workHours) {
            setSlots(generateTimeSlots(workHours.start, workHours.end, reserved));
        } else {
            setSlots(generateTimeSlots("09:00", "19:00", reserved));
        }
    }, [workHours, reserved]);

    // 오전/오후 분리
    const splitSlotsByAmPm = (slots) => {
        const am = [];
        const pm = [];
        slots.forEach(slot => {
            const [h, m] = slot.time.split(":").map(Number);
            if (h < 12 || (h === 12 && m <= 30)) {
                am.push(slot);
            } else {
                pm.push(slot);
            }
        });
        return { am, pm };
    };
    const { am: amSlots, pm: pmSlots } = splitSlotsByAmPm(slots);

    const handleTimeSelect = (time) => {
        setSelectedTime(time);
        if (onSelect) {
            onSelect({ date, time });
        }
    };

    return (
        <div className="flex flex-col items-center justify-center min-h-screen bg-white p-6 text-black">
            <div className="w-[600px] h-[800px] bg-white rounded-2xl shadow-lg p-12">
                <div className="flex items-center justify-between mb-8">
                    <span className="font-bold text-lg px-2 py-1">{date} 시간 선택</span>
                    <button 
                        onClick={onBack} 
                        className="back-btn text-sm text-gray-500 border border-gray-300 bg-white rounded px-4 py-2 transition-colors duration-150"
                    >&lt; 날짜 다시 선택</button>
                </div>
                <div>
                    <div className="font-semibold mb-2">오전</div>
                    <div className="grid grid-cols-4 gap-4 mb-4">
                        {amSlots.length === 0 && <div className="col-span-4 text-gray-400">선택 가능한 시간이 없습니다</div>}
                        {amSlots.map((slot) => (
                            <button
                                key={slot.time}
                                disabled={slot.blocked}
                                className={`time-slot-btn w-full h-12 rounded-xl font-bold border transition-colors duration-150 text-black
                                    ${slot.blocked
                                        ? "bg-gray-100 text-gray-400 cursor-not-allowed border-gray-100"
                                        : (selectedTime === slot.time
                                            ? "bg-[#9333ea] border-[#9333ea] text-white"
                                            : "bg-white border-gray-200 hover:bg-gray-100")
                                    }
                                `}
                                onClick={() => !slot.blocked && handleTimeSelect(slot.time)}
                            >
                                {slot.time}
                            </button>
                        ))}
                    </div>
                    <div className="font-semibold mb-2">오후</div>
                    <div className="grid grid-cols-4 gap-4">
                        {pmSlots.length === 0 && <div className="col-span-4 text-gray-400">선택 가능한 시간이 없습니다</div>}
                        {pmSlots.map((slot) => (
                            <button
                                key={slot.time}
                                disabled={slot.blocked}
                                className={`time-slot-btn w-full h-12 rounded-xl font-bold border transition-colors duration-150 text-black
                                    ${slot.blocked
                                        ? "bg-gray-100 text-gray-400 cursor-not-allowed border-gray-100"
                                        : (selectedTime === slot.time
                                            ? "bg-[#9333ea] border-[#9333ea] text-white"
                                            : "bg-white border-gray-200 hover:bg-gray-100")
                                    }
                                `}
                                onClick={() => !slot.blocked && handleTimeSelect(slot.time)}
                            >
                                {slot.time}
                            </button>
                        ))}
                    </div>
                </div>
            </div>
            <style>{`
                button {
                    background: #fff;
                }
                button:hover {
                    background: #9333ea;
                    color: #fff;
                }
                .time-slot-btn:hover:not(:disabled) {
                    background: #9333ea !important;
                    color: #fff !important;
                    border-color: #9333ea !important;
                }
                .back-btn:hover {
                    background: #9333ea !important;
                    color: #fff !important;
                    border-color: #9333ea !important;
                }
            `}</style>
        </div>
    );
}
