import React, { useEffect, useState } from "react";
import axios from "axios";

const BASE_URL = import.meta.env.VITE_API_BASE_URL;

export default function AdminMemberPage() {
    const [companies, setCompanies] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        axios.get(`${BASE_URL}/company/allMembers`, { withCredentials: true })
            .then(res => {
                console.log("회원 API 응답:", res.data)
                // res.data가 배열이라면 바로 setMembers!
                setCompanies(res.data);
                setLoading(false);
            })
            .catch(err => {
                console.error("회원 리스트 조회 실패:", err);
                setLoading(false);
            });
    }, []);

    if (loading) return <div>로딩 중...</div>;
    if (!companies || companies.length === 0) return <div>회원이 없습니다.</div>;

    return (
        <div>
            <h2 className="text-xl font-bold mb-6 text-[#845EC2]">회원 관리</h2>
            <table className="w-full text-left border border-[#E0CFFD] bg-[#F6F1FA] rounded-xl shadow">
                <thead>
                    <tr className="bg-[#E0CFFD] border-b border-[#E0CFFD]">
                        <th className="p-3 text-[#845EC2]">회원ID</th>
                        <th className="p-3 text-[#845EC2]">이름</th>
                        <th className="p-3 text-[#845EC2]">이메일</th>
                        <th className="p-3 text-[#845EC2]">업체 주소</th>
                        <th className="p-3 text-[#845EC2]">사업자등록번호</th>
                        <th className="p-3 text-[#845EC2]">통신판매업신고번호</th>
                        <th className="p-3 text-[#845EC2]">우편번호</th>
                        <th className="p-3 text-[#845EC2]">연락처</th>
                        
                        
                        {/* 필요한 추가 정보가 있으면 여기에 더 컬럼 추가 */}
                    </tr>
                </thead>
                <tbody>
                    {companies.map(company => (
                        <tr key={company.id}>
                            <td className="p-3 text-[#845EC2]">{company.id}</td>
                            <td className="p-3 text-[#845EC2]">{company.name}</td>
                            <td className="p-3 text-[#845EC2]">{company.email}</td>
                            <td className="p-3 text-[#845EC2]">{company.address}</td>
                            <td className="p-3 text-[#845EC2]">{company.bnRegNo}</td>
                            <td className="p-3 text-[#845EC2]">{company.mos}</td>
                            <td className="p-3 text-[#845EC2]">{company.postalCode}</td>
                            <td className="p-3 text-[#845EC2]">{company.contact}</td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
}
