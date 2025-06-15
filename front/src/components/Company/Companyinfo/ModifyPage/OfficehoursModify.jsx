import React, { useState } from "react";

const days = [
    "월요일", "화요일", "수요일", "목요일", "금요일", "토요일", "일요일"
];
const defaultHours = [
    { open: "9:00", close: "18:00", off: false },
    { open: "9:00", close: "18:00", off: false },
    { open: "9:00", close: "18:00", off: false },
    { open: "9:00", close: "18:00", off: false },
    { open: "9:00", close: "18:00", off: false },
    { open: "10:00", close: "20:00", off: false },
    { open: "", close: "", off: true }
];
const timeOptions = [
    "9:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00"
];
const closeOptions = [
    "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00"
];

function OfficehoursModify({ onCancel, onSubmit }) {
    const [hours, setHours] = useState(defaultHours);

    const handleTimeChange = (idx, type, value) => {
        setHours(prev =>
            prev.map((h, i) =>
                i === idx ? { ...h, [type]: value, off: false } : h
            )
        );
    };

    const handleOff = idx => {
        setHours(prev =>
            prev.map((h, i) =>
                i === idx ? { open: "", close: "", off: !h.off } : h
            )
        );
    };

    return (
        <div className="max-w-2xl mx-auto bg-white rounded-lg p-8">
            <h1 className="text-2xl font-bold mb-1 text-black">업무시간 설정</h1>
            <p className="mb-6 text-sm text-gray-500">업무시간 설정 및 휴무일을 관리할 수 있습니다.</p>
            <div className="space-y-3">
                {days.map((day, idx) => (
                    <div key={day} className="flex items-center border border-[#845EC2] rounded-lg px-4 py-2 text-[#845EC2]">
                        {/* Calendar SVG */}
                        <svg className="w-5 h-5 mr-2" fill="none" stroke="currentColor" strokeWidth="2" viewBox="0 0 24 24"><rect x="3" y="4" width="18" height="18" rx="2" /><path d="M16 2v4M8 2v4M3 10h18" /></svg>
                        <span className="w-20">{day}</span>
                        {hours[idx].off ? (
                            <select
                                className="ml-4 border-none bg-white text-[#845EC2] font-bold"
                                value="휴무"
                                onChange={() => handleOff(idx)}
                            >
                                <option value="휴무">휴무</option>
                            </select>
                        ) : (
                            <>
                                <select
                                    className="ml-4 border-none bg-white text-[#845EC2] font-bold"
                                    value={hours[idx].open}
                                    onChange={e => handleTimeChange(idx, "open", e.target.value)}
                                >
                                    {timeOptions.map(t => (
                                        <option key={t} value={t}>{t}</option>
                                    ))}
                                </select>
                                <span className="mx-2">~</span>
                                <select
                                    className="border-none bg-white text-[#845EC2] font-bold"
                                    value={hours[idx].close}
                                    onChange={e => handleTimeChange(idx, "close", e.target.value)}
                                >
                                    {closeOptions.map(t => (
                                        <option key={t} value={t}>{t}</option>
                                    ))}
                                </select>
                            </>
                        )}
                        <button
                            className="ml-auto px-2 text-2xl font-bold bg-white"
                            onClick={() => handleOff(idx)}
                            title={hours[idx].off ? "근무일로 변경" : "휴무로 변경"}
                        >
                            {hours[idx].off ? "+" : "-"}
                        </button>
                    </div>
                ))}
            </div>
            <div className="flex gap-4 mt-8">
                <button
                    className="flex-1 bg-[#845EC2] text-white py-3 rounded font-bold text-lg hover:bg-[#6b40b5] transition-colors"
                    onClick={() => onSubmit?.(hours)}
                >
                    수정
                </button>
                <button
                    className="flex-1 border-2 border-[#845EC2] text-[#845EC2] py-3 rounded font-bold text-lg bg-white hover:bg-[#f5f0fa] transition-colors"
                    onClick={onCancel}
                >
                    취소
                </button>
            </div>
        </div>
    );
}

export default OfficehoursModify;
