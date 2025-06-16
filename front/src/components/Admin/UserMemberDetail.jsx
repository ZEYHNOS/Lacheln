import React, { useEffect, useState } from "react";
import axios from "axios";

const BASE_URL = import.meta.env.VITE_API_BASE_URL;
const formatDate = (dateArray) => {
  if (!Array.isArray(dateArray)) return "날짜 없음";
  const [year, month, day] = dateArray;
  return `${year}-${String(month).padStart(2, "0")}-${String(day).padStart(2, "0")}`;
};

export default function UserMemberDetail() {
    const [users, setUsers] = useState([]);
    const [loading, setLoading] = useState(true);
    const [page, setPage] = useState(0);
    const [size, setSize] = useState(10);
    const [totalPages, setTotalPages] = useState(1);
    const [totalElements, setTotalElements] = useState(0);
    const [last, setLast] = useState(false);

    useEffect(() => {
        setLoading(true);
        axios.get(`${BASE_URL}/user/all/${page}?size=${size}`, { withCredentials: true })
            .then(res => {
                const data = res.data.data;
                setUsers(data.users || []);
                setTotalPages(data.totalPages || 1);
                setTotalElements(data.totalElements || 0);
                setLast(data.last || false);
            })
            .catch(() => {
                setUsers([]);
                setTotalPages(1);
                setTotalElements(0);
                setLast(true);
            })
            .finally(() => setLoading(false));
    }, [page, size]);

    // 날짜 YYYY-MM-DD 포맷팅 함수
    const formatDate = (dateString) => {
        if (!dateString) return "";
        return new Date(dateString).toLocaleDateString("ko-KR");
    };

    if (loading) return <div className="text-2xl text-[#845EC2] text-center py-20 font-semibold">로딩 중...</div>;
    if (!users || users.length === 0) return <div className="text-lg text-gray-400 text-center py-16">회원이 없습니다.</div>;

    return (
        <div className="max-w-full mx-auto p-10">
            <h2 className="text-3xl font-extrabold mb-10 text-[#845EC2] text-center tracking-wider">소비자 목록</h2>

            <div className="overflow-x-auto rounded-xxl shadow-xxl bg-white">
                <table className="min-w-full w-full table-auto border-separate border-spacing-0">
                    <thead>
                        <tr className="bg-gradient-to-r from-[#845EC2] to-[#D65DB1]">
                            <th className="p-5 text-lg text-white font-bold border-b-2 border-[#F6F1FA] min-w-[120px]">ID</th>
                            <th className="p-5 text-lg text-white font-bold border-b-2 border-[#F6F1FA] min-w-[150px]">이름</th>
                            <th className="p-5 text-lg text-white font-bold border-b-2 border-[#F6F1FA] min-w-[200px]">이메일</th>
                            <th className="p-5 text-lg text-white font-bold border-b-2 border-[#F6F1FA] min-w-[150px]">가입일</th>
                            <th className="p-5 text-lg text-white font-bold border-b-2 border-[#F6F1FA] min-w-[120px]">전화번호</th>
                            <th className="p-5 text-lg text-white font-bold border-b-2 border-[#F6F1FA] min-w-[120px]">닉네임</th>
                            <th className="p-5 text-lg text-white font-bold border-b-2 border-[#F6F1FA] min-w-[120px]">상태</th>
                            <th className="p-5 text-lg text-white font-bold border-b-2 border-[#F6F1FA] min-w-[120px]">성별</th>
                            <th className="p-5 text-lg text-white font-bold border-b-2 border-[#F6F1FA] min-w-[120px]">등급</th>
                        </tr>
                    </thead>
                    <tbody>
                        {users.map(user => (
                            <tr key={user.id} className="even:bg-[#F6F1FA] odd:bg-white text-[#5B5B5B] hover:bg-[#E0CFFD] transition">
                                <td className="p-5 text-lg text-[#845EC2] font-mono truncate max-w-[220px]">{user.id}</td>
                                <td className="p-5 text-lg">{user.name}</td>
                                <td className="p-5 text-lg">{user.email}</td>
                                <td className="p-5 text-lg">{formatDate(user.joinDate)}</td>
                                <td className="p-5 text-lg">{user.phone}</td>
                                <td className="p-5 text-lg">{user.nickname}</td>
                                <td className="p-5 text-lg font-semibold">{user.status}</td>
                                <td className="p-5 text-lg">{user.gender}</td>
                                <td className="p-5 text-lg">{user.tier}</td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>

            {/* 페이지네이션 */}
            <div className="flex flex-col md:flex-row justify-between items-center mt-8 gap-4">
                <div>
                    <button
                        className="px-6 py-3 rounded-xl text-lg font-bold mr-2 bg-[#845EC2] text-white shadow hover:bg-[#6C51B4] transition"
                        onClick={() => setPage(page - 1)}
                        disabled={page === 0}
                        style={{ opacity: page === 0 ? 0.5 : 1, cursor: page === 0 ? "not-allowed" : "pointer" }}
                    >이전</button>
                    <button
                        className="px-6 py-3 rounded-xl text-lg font-bold bg-[#845EC2] text-white shadow hover:bg-[#6C51B4] transition"
                        onClick={() => setPage(page + 1)}
                        disabled={last || page === totalPages - 1}
                        style={{ opacity: last || page === totalPages - 1 ? 0.5 : 1, cursor: last || page === totalPages - 1 ? "not-allowed" : "pointer" }}
                    >다음</button>
                </div>
                <div className="text-lg">
                    <span className="mr-3 text-[#845EC2] font-semibold">페이지 {page + 1} / {totalPages}</span>
                    <span className="text-gray-500">총 {totalElements}명</span>
                </div>
            </div>
        </div>
    );
}
