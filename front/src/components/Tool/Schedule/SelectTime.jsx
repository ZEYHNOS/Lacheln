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

export default function SelectTime({ productId, cpId, date, dayOfWeek, onSelect, onBack, modalStyle }) {
    const [workHours, setWorkHours] = useState(null); // {start: "10:00", end: "19:00"}
    const [reserved, setReserved] = useState([]); // [{start: "12:00", end: "14:00"}]
    const [slots, setSlots] = useState([]);
    const [selectedTime, setSelectedTime] = useState(null);

    // 시간 슬롯 생성
    useEffect(() => {
        if (workHours && workHours.start && workHours.end) {
            setSlots(generateTimeSlots(workHours.start, workHours.end, reserved));
        } else {
            // 기본 업무시간 설정 (9:00 ~ 19:00)
            setSlots(generateTimeSlots("09:00", "19:00", reserved));
        }
    }, [workHours, reserved]);

    // 날짜 변경 시 업무시간/예약이력 불러오기
    useEffect(() => {
        if (!cpId || !productId || !date) return;
        
        const fetchData = async () => {
            try {
                const [workRes, reservedRes] = await Promise.all([
                    apiClient.get(`/api/company/business_hour/${cpId}`, { params: { date } }),
                    apiClient.get(`/payment/block/${productId}`, { params: { date } })
                        .catch(() => ({ data: [] }))
                ]);

                // 전달받은 요일(0: 월요일 ~ 6: 일요일)로 업무시간 찾기
                if (workRes.data?.data && Array.isArray(workRes.data.data)) {
                    const workHoursData = workRes.data.data[dayOfWeek];
                    if (workHoursData) {
                        const startHour = String(workHoursData.start[0]).padStart(2, '0');
                        const startMin = String(workHoursData.start[1]).padStart(2, '0');
                        const endHour = String(workHoursData.end[0]).padStart(2, '0');
                        const endMin = String(workHoursData.end[1]).padStart(2, '0');
                        
                        setWorkHours({
                            start: `${startHour}:${startMin}`,
                            end: `${endHour}:${endMin}`
                        });
                    } else {
                        console.warn('해당 요일의 업무시간이 없습니다:', dayOfWeek);
                        setWorkHours({ start: "11:00", end: "21:00" });
                    }
                } else {
                    console.warn('업무시간 데이터가 올바르지 않습니다:', workRes.data);
                    setWorkHours({ start: "11:00", end: "21:00" });
                }
                
                // 예약된 시간 데이터 변환
                const blockedTimes = (reservedRes.data || []).map(item => ({
                    start: item.startTime.split('T')[1].substring(0, 5),
                    end: new Date(new Date(item.startTime).getTime() + item.taskTime.toMinutes() * 60000)
                        .toTimeString().substring(0, 5)
                }));
                setReserved(blockedTimes);
            } catch (error) {
                console.error('시간 정보를 불러오는데 실패했습니다:', error);
                setWorkHours({ start: "11:00", end: "21:00" });
                setReserved([]);
            }
        };

        fetchData();
    }, [cpId, productId, date, dayOfWeek]);

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
    };

    return (
        <div className="flex flex-col items-center justify-center" style={modalStyle}>
            <div>
                <div className="flex items-center justify-between mb-4 px-6 pt-6">
                    <span className="font-bold text-lg">{date} 시간 선택</span>
                    <button 
                        onClick={onBack} 
                        className="back-btn text-sm text-gray-500 border border-gray-300 bg-white rounded px-3 py-1 transition-colors duration-150"
                    >&lt; 날짜 다시 선택</button>
                </div>
                <div className="px-6 pb-6">
                    <div className="font-semibold mb-1">오전</div>
                    <div className="grid grid-cols-4 gap-2 mb-2">
                        {amSlots.length === 0 && <div className="col-span-4 text-gray-400">선택 가능한 시간이 없습니다</div>}
                        {amSlots.map((slot) => (
                            <button
                                key={slot.time}
                                disabled={slot.blocked}
                                className={`time-slot-btn w-full h-10 rounded-xl font-bold border transition-colors duration-150 text-black
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
                    <div className="font-semibold mb-1">오후</div>
                    <div className="grid grid-cols-4 gap-2">
                        {pmSlots.length === 0 && <div className="col-span-4 text-gray-400">선택 가능한 시간이 없습니다</div>}
                        {pmSlots.map((slot) => (
                            <button
                                key={slot.time}
                                disabled={slot.blocked}
                                className={`time-slot-btn w-full h-10 rounded-xl font-bold border transition-colors duration-150 text-black
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
                {/* 일정 선택하기 버튼 */}
                <div className="flex justify-center px-6 pb-6">
                    <button
                        className={`px-4 py-2 rounded-lg font-bold text-white transition-colors duration-150 ${selectedTime ? 'bg-[#22c55e] hover:bg-green-700' : 'bg-gray-300 cursor-not-allowed'}`}
                        disabled={!selectedTime}
                        onClick={() => {
                            if (!selectedTime || !date) {
                                alert('날짜와 시간을 모두 선택해주세요.');
                                return;
                            }
                            const localDateTime = `${date}T${selectedTime}:00`;
                            onSelect && onSelect({ localDateTime });
                        }}
                    >
                        일정 선택하기
                    </button>
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
