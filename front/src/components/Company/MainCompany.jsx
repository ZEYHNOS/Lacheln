import React, { useEffect, useRef, useState } from "react";
import { useLocation } from "react-router-dom";
import { toast } from "react-toastify";
import { PieChart, Pie, Cell, BarChart, Bar, XAxis, YAxis, Tooltip, LineChart, Line, Legend, CartesianGrid } from "recharts";
import Schedule from "./Work/Schedule";

const pieData = [
    { name: "SNS", value: 24 },
    { name: "온라인광고", value: 31 },
    { name: "패키지", value: 45 }
];
const COLORS = ["#845EC2", "#4B9CD3", "#FFB6B9"];

const barData = [
    { name: "1월", 상품: 51, 패키지: 21 },
    { name: "2월", 상품: 62, 패키지: 33 },
    { name: "3월", 상품: 64, 패키지: 68 },
    { name: "4월", 상품: 71, 패키지: 71 },
    { name: "5월", 상품: 68, 패키지: 97 },
    { name: "6월", 상품: 141, 패키지: 86 }
];

const lineData = [
    { name: "1월", 상품: 51, 패키지: 21 },
    { name: "2월", 상품: 62, 패키지: 33 },
    { name: "3월", 상품: 64, 패키지: 68 },
    { name: "4월", 상품: 71, 패키지: 71 },
    { name: "5월", 상품: 68, 패키지: 97 },
    { name: "6월", 상품: 141, 패키지: 86 }
];

function MainCompany() {
    const location = useLocation();
    const hasShownToast = useRef(false);
    const graphRef = useRef(null);
    const [visible, setVisible] = useState(false);
    const calendarRef = useRef(null);
    const [calendarVisible, setCalendarVisible] = useState(false);

    useEffect(() => {
        // 이미 토스트를 표시했다면 더 이상 실행하지 않음
        if (hasShownToast.current) return;

        // 로컬 로그인 시도 여부 확인
        const localLoginSuccess = sessionStorage.getItem('localLoginSuccess');
        const userType = sessionStorage.getItem('userType');

        if (localLoginSuccess === 'true' && userType === "COMPANY") {
            // 로그인 상태 확인
            fetch(`${import.meta.env.VITE_API_BASE_URL}/auth/me`, {
                credentials: 'include'
            })
                .then(res => res.json())
                .then(data => {
                    if (data.data?.valid && !hasShownToast.current) {
                        toast.success("업체 로그인이 완료되었습니다!", {
                            position: "top-center",
                            autoClose: 1000,
                        });
                        hasShownToast.current = true;
                        // 상태 제거
                        sessionStorage.removeItem('localLoginSuccess');
                        sessionStorage.removeItem('userType');
                    }
                })
                .catch(error => console.error("로그인 상태 확인 중 오류:", error));
        }
    }, []);

    useEffect(() => {
        const observer = new window.IntersectionObserver(
            ([entry]) => {
                if (entry.isIntersecting) setVisible(true);
            },
            { threshold: 0.2 }
        );
        if (graphRef.current) observer.observe(graphRef.current);
        return () => observer.disconnect();
    }, []);

    useEffect(() => {
        const observer = new window.IntersectionObserver(
            ([entry]) => {
                if (entry.isIntersecting) setCalendarVisible(true);
            },
            { threshold: 0.2 }
        );
        if (calendarRef.current) observer.observe(calendarRef.current);
        return () => observer.disconnect();
    }, []);

    return (
        <div className="p-4 min-h-screen w-full">
            <div
                ref={graphRef}
                className={`border rounded-xl p-4 mb-8 bg-white mx-auto transition-all duration-700 ease-out
                    ${visible ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-10'}`}
            >
                <div className="flex justify-between items-center mb-4">
                    <div className="flex-1 text-center text-purple-500 text-2xl font-bold">최근 유입경로</div>
                    <div className="flex-1 text-center text-purple-500 text-2xl font-bold">월별 판매 실적</div>
                    <div className="flex-1 text-center text-purple-500 text-2xl font-bold">최근 조회수</div>
                </div>
                <div className="flex justify-between items-center">
                    {/* 파이차트 */}
                    <div className="flex-1 flex justify-center items-center gap-8">
                        <PieChart width={360} height={360}>
                            <Pie data={pieData} dataKey="value" nameKey="name" cx="50%" cy="50%" outerRadius={140} label>
                                {pieData.map((entry, index) => (
                                    <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                                ))}
                            </Pie>
                        </PieChart>
                        {/* 유입경로 라벨 */}
                        <div className="flex flex-col gap-4 ml-4">
                            {pieData.map((entry, idx) => (
                                <div key={entry.name} className="flex items-center gap-2">
                                    <span className="inline-block w-4 h-4 rounded" style={{background: COLORS[idx]}} />
                                    <span className="text-base font-medium text-gray-700">{entry.name}</span>
                                </div>
                            ))}
                        </div>
                    </div>
                    {/* 바차트 */}
                    <div className="flex-1 flex justify-center">
                        <BarChart width={440} height={360} data={barData}>
                            <CartesianGrid strokeDasharray="3 3" />
                            <XAxis dataKey="name" />
                            <YAxis />
                            <Tooltip />
                            <Legend />
                            <Bar dataKey="상품" fill="#845EC2" />
                            <Bar dataKey="패키지" fill="#4B9CD3" />
                        </BarChart>
                    </div>
                    {/* 라인차트 */}
                    <div className="flex-1 flex justify-center">
                        <LineChart width={440} height={360} data={lineData}>
                            <CartesianGrid strokeDasharray="3 3" />
                            <XAxis dataKey="name" />
                            <YAxis />
                            <Tooltip />
                            <Legend />
                            <Line type="monotone" dataKey="상품" stroke="#4B9CD3" name="상품" />
                            <Line type="monotone" dataKey="패키지" stroke="#FFB6B9" name="패키지" />
                        </LineChart>
                    </div>
                </div>
            </div>
            <div
                ref={calendarRef}
                className={`border rounded-xl pl-16 pr-16 pb-8 bg-white mx-auto transition-all duration-700 ease-out
                    ${calendarVisible ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-10'}`}
            >
                <Schedule />
            </div>
        </div>
    );
}

export default MainCompany; 