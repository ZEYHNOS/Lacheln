import React, { useEffect, useState } from "react";
import axios from "axios";

const BASE_URL = import.meta.env.VITE_API_BASE_URL;

export default function AdminMemberPage() {
    const [companies, setCompanies] = useState([]);
    const [loading, setLoading] = useState(true);
    const [page, setPage] = useState(0);
    const [size, setSize] = useState(10);
    const [totalPages, setTotalPages] = useState(1);
    const [totalElements, setTotalElements] = useState(0);
    const [last, setLast] = useState(false);

    useEffect(() => {
        console.log(`🔄 페이지 로딩 시작: 페이지 ${page}, 사이즈 ${size}`);
        setLoading(true);
        axios.get(`${BASE_URL}/company/all/${page}?size=${size}`, { withCredentials: true })
            .then(res => {
                console.log("✅ API 원본 응답:", res.data);
                
                // Based on your API structure: { result: {...}, data: {...} }
                const responseData = res.data?.data;
                
                if (responseData) {
                    console.log(`📊 데이터 설정: 회사 ${responseData.companies?.length}개, 총 페이지 ${responseData.totalPages}, 마지막 페이지: ${responseData.last}`);
                    setCompanies(responseData.companies || []);
                    setTotalPages(responseData.totalPages || 1);
                    setTotalElements(responseData.totalElements || 0);
                    setLast(responseData.last || false);
                } else {
                    console.error("❌ No data found in response:", res.data);
                    setCompanies([]);
                    setTotalPages(1);
                    setTotalElements(0);
                    setLast(true);
                }
            })
            .catch(error => {
                console.error("API 호출 오류:", error);
                setCompanies([]);
                setTotalPages(1);
                setTotalElements(0);
                setLast(true);
            })
            .finally(() => setLoading(false));
    }, [page, size]);

    if (loading) return <div className="text-2xl text-[#845EC2] text-center py-20 font-semibold">로딩 중...</div>;
    if (!companies || companies.length === 0) return <div className="text-lg text-gray-400 text-center py-16">업체가 없습니다.</div>;

    return (
        <div className="max-w-7xl mx-auto p-10">
            <h2 className="text-3xl font-extrabold mb-10 text-[#845EC2] text-center tracking-wider">업체(회사) 목록</h2>

            <div className="overflow-x-auto rounded-3xl shadow-2xl bg-white">
                <table className="min-w-full table-fixed border-separate border-spacing-0">
                    <thead>
                        <tr className="bg-gradient-to-r from-[#845EC2] to-[#D65DB1]">
                            <th className="p-5 text-lg text-white font-bold border-b-2 border-[#F6F1FA]">ID</th>
                            <th className="p-5 text-lg text-white font-bold border-b-2 border-[#F6F1FA]">이메일</th>
                            <th className="p-5 text-lg text-white font-bold border-b-2 border-[#F6F1FA]">이름</th>
                            <th className="p-5 text-lg text-white font-bold border-b-2 border-[#F6F1FA]">주소</th>
                            <th className="p-5 text-lg text-white font-bold border-b-2 border-[#F6F1FA]">우편번호</th>
                            <th className="p-5 text-lg text-white font-bold border-b-2 border-[#F6F1FA]">사업자등록번호</th>
                            <th className="p-5 text-lg text-white font-bold border-b-2 border-[#F6F1FA]">통신판매업신고번호</th>
                            <th className="p-5 text-lg text-white font-bold border-b-2 border-[#F6F1FA]">연락처</th>
                            <th className="p-5 text-lg text-white font-bold border-b-2 border-[#F6F1FA]">카테고리</th>
                            <th className="p-5 text-lg text-white font-bold border-b-2 border-[#F6F1FA]">프로필</th>
                        </tr>
                    </thead>
                    <tbody>
                        {companies.map(company => (
                            <tr key={company.id} className="even:bg-[#F6F1FA] odd:bg-white text-[#5B5B5B] hover:bg-[#E0CFFD] transition">
                                <td className="p-4 text-base text-[#845EC2] font-mono truncate max-w-[160px]">{company.id}</td>
                                <td className="p-4 text-base">{company.email}</td>
                                <td className="p-4 text-base">{company.name}</td>
                                <td className="p-4 text-base">{company.address}</td>
                                <td className="p-4 text-base">{company.postalCode}</td>
                                <td className="p-4 text-base">{company.bnRegNo}</td>
                                <td className="p-4 text-base">{company.mos}</td>
                                <td className="p-4 text-base">{company.contact}</td>
                                <td className="p-4 text-base">{company.category}</td>
                                <td className="p-4 text-base">{company.profileImageUrl}</td>
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
                        onClick={() => {
                            console.log(`⬅️ 이전 버튼 클릭: ${page} → ${page - 1}`);
                            setPage(page - 1);
                        }}
                        disabled={page === 0}
                        style={{ opacity: page === 0 ? 0.5 : 1, cursor: page === 0 ? "not-allowed" : "pointer" }}
                    >이전</button>
                    <button
                        className="px-6 py-3 rounded-xl text-lg font-bold bg-[#845EC2] text-white shadow hover:bg-[#6C51B4] transition"
                        onClick={() => {
                            console.log(`➡️ 다음 버튼 클릭: ${page} → ${page + 1}`);
                            setPage(page + 1);
                        }}
                        disabled={last || page === totalPages - 1}
                        style={{ opacity: last || page === totalPages - 1 ? 0.5 : 1, cursor: last || page === totalPages - 1 ? "not-allowed" : "pointer" }}
                    >다음</button>
                </div>
                <div className="text-lg">
                    <span className="mr-3 text-[#845EC2] font-semibold">페이지 {page + 1} / {totalPages}</span>
                    <span className="text-gray-500">총 {totalElements}개</span>
                </div>
                <div>
                    <select
                        value={size}
                        onChange={e => { 
                            console.log(`📏 사이즈 변경: ${size} → ${Number(e.target.value)}`);
                            setPage(0); 
                            setSize(Number(e.target.value)); 
                        }}
                        className="text-lg px-3 py-2 rounded-lg border border-[#E0CFFD] shadow"
                    >
                        {[10, 20, 50].map(option => (
                            <option key={option} value={option}>{option}개씩 보기</option>
                        ))}
                    </select>
                </div>
            </div>
        </div>
    );
}