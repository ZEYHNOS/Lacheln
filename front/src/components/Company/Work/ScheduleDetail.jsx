import React from "react";
import { COLOR_CODE_MAP } from "../../../constants/colorcodeMap";

// 시간 배열([yyyy, mm, dd, hh, min])을 'yyyy.mm.dd hh:mm' 포맷 문자열로 변환
function formatDateTime(arr) {
    if (!Array.isArray(arr) || arr.length < 5) return '';
    const yyyy = arr[0];
    const mm = String(arr[1]).padStart(2, '0');
    const dd = String(arr[2]).padStart(2, '0');
    const hh = String(arr[3]).padStart(2, '0');
    const min = String(arr[4]).padStart(2, '0');
    return `${yyyy}.${mm}.${dd} ${hh}:${min}`;
}

export default function ScheduleDetail({ event, onClose }) {
    if (!event) return null;
    return (
        <div className="fixed inset-0 bg-black bg-opacity-20 z-50 flex items-center justify-center" onClick={onClose}>
            <div
                className="bg-white rounded-2xl px-12 py-10 min-w-[800px] min-h-[300px] shadow-xl text-[#845EC2] text-[1.2rem] relative flex flex-col gap-5"
                onClick={e => e.stopPropagation()}
            >
                <h2 className="text-5xl font-bold mb-4 text-center">상세일정</h2>
                <div className="flex flex-col gap-3">
                    <div className="flex items-center border border-[#845EC2] rounded-xl px-4 py-2">
                        <span className="min-w-[120px] font-semibold text-[#845EC2]">제목</span>
                        <span className="flex-1 text-lg">{event.title}</span>
                    </div>
                    <div className="flex items-center border border-[#845EC2] rounded-xl px-4 py-2">
                        <span className="min-w-[120px] font-semibold text-[#845EC2]">내용</span>
                        <span className="flex-1 text-lg">{event.content}</span>
                    </div>
                    <div className="flex items-center border border-[#845EC2] rounded-xl px-4 py-2">
                        <span className="min-w-[120px] font-semibold text-[#845EC2]">시작시간</span>
                        <span className="flex-1 text-lg">{formatDateTime(event.startTime)}</span>
                    </div>
                    <div className="flex items-center border border-[#845EC2] rounded-xl px-4 py-2">
                        <span className="min-w-[120px] font-semibold text-[#845EC2]">종료시간</span>
                        <span className="flex-1 text-lg">{formatDateTime(event.endTime)}</span>
                    </div>
                    <div className="flex items-center border border-[#845EC2] rounded-xl px-4 py-2">
                        <span className="min-w-[120px] font-semibold text-[#845EC2]">메모</span>
                        <span className="flex-1 text-lg">{event.memo}</span>
                    </div>
                    <div className="flex items-center border border-[#845EC2] rounded-xl px-4 py-2">
                        <span className="min-w-[120px] font-semibold text-[#845EC2]">담당자</span>
                        <span className="flex-1 text-lg">{event.manager}</span>
                    </div>
                    <div className="flex items-center border border-[#845EC2] rounded-xl px-4 py-2">
                        <span className="min-w-[120px] font-semibold text-[#845EC2]">캘린더 색상</span>
                        <span
                            className="flex-1 text-lg font-bold"
                            style={{ color: COLOR_CODE_MAP[event.color?.toUpperCase()] || "#845EC2" }}
                        >
                            {event.color}
                        </span>
                    </div>
                </div>
                <div className="flex justify-end gap-4 mt-2">
                    <button className="bg-[#845EC2] text-white rounded-xl px-8 py-2 text-lg font-bold" onClick={onClose}>확인</button>
                    <button className="border border-[#845EC2] text-[#845EC2] rounded-xl px-8 py-2 text-lg font-bold bg-white" onClick={onClose}>수정 </button>
                </div>
            </div>
        </div>
    );
}
