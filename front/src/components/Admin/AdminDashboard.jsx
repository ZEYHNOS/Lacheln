import React, { useEffect, useState } from "react"; 
import { Flag, User, HelpCircle, Building } from "lucide-react";

const BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:3000';

export default function AdminDashboard() {
  const [inquiriesToday, setInquiriesToday] = useState(0);
  const [reportsToday, setReportsToday] = useState(0);
  const [dashboardData, setDashboardData] = useState({
    newCompanies: 0,
    newUsers: 0,
    totalNewMembers: 0
  });
  const [monthlyStats, setMonthlyStats] = useState([]);
  const [userMonthlyStats, setUserMonthlyStats] = useState([]);

  useEffect(() => {
    // 기존 문의 조회 - 안전한 배열 체크 추가
    fetch(`${BASE_URL}/inquiry/admin/list`, { credentials: 'include' })
      .then(res => res.json())
      .then((res) => {
        console.log("📊 문의 데이터:", res); // 디버깅용
        const today = new Date();
        
        // 안전한 배열 체크
        const inquiryData = res?.data || [];
        if (!Array.isArray(inquiryData)) {
          console.warn("⚠️ 문의 데이터가 배열이 아닙니다:", inquiryData);
          return;
        }

        const filtered = inquiryData.filter((i) => {
          if (!Array.isArray(i.createdAt)) return false;
          const [y, m, d] = i.createdAt;
          return (
            i.status === "IN_PROGRESS" &&
            y === today.getFullYear() &&
            m === today.getMonth() + 1 &&
            d === today.getDate()
          );
        });
        setInquiriesToday(filtered.length);
      })
      .catch((error) => {
        console.error("❌ 오늘 미답변 문의 수 조회 실패:", error);
        setInquiriesToday(0);
      });

    // 오늘 신고 수 조회
    fetch(`${BASE_URL}/report/today_reports`, { credentials: 'include' })
      .then(res => res.json())
      .then((res) => {
        console.log("📊 오늘 신고 데이터:", res);
        if (res?.result && res?.data) {
          setReportsToday(res.data.todayReports || 0);
        }
      })
      .catch((error) => {
        console.error("❌ 오늘 신고 수 조회 실패:", error);
        setReportsToday(0);
      });

    // 오늘 신규 업체 수 조회
    fetch(`${BASE_URL}/company/today_new_members`, { credentials: 'include' })
      .then(res => res.json())
      .then((res) => {
        console.log("📊 오늘 신규 업체 데이터:", res);
        if (res?.result && res?.data) {
          setDashboardData(prev => ({
            ...prev,
            newCompanies: res.data.newCompanies || 0
          }));
        }
      })
      .catch((error) => {
        console.error("❌ 오늘 신규 업체 수 조회 실패:", error);
      });

    // 오늘 신규 유저 수 조회
    fetch(`${BASE_URL}/user/today_new_users`, { credentials: 'include' })
      .then(res => res.json())
      .then((res) => {
        console.log("📊 오늘 신규 유저 데이터:", res);
        console.log("📊 응답 구조:", {
          result: res?.result,
          data: res?.data,
          dataType: typeof res?.data,
          isArray: Array.isArray(res?.data)
        });
        
        if (res?.result && res?.data) {
          setDashboardData(prev => {
            const newUsers = res.data.newUsers || 0;
            return {
              ...prev,
              newUsers: newUsers,
              totalNewMembers: prev.newCompanies + newUsers
            };
          });
        }
      })
      .catch((error) => {
        console.error("❌ 오늘 신규 유저 수 조회 실패:", error);
      });

    // 업체 월별 가입 통계 조회 - 안전한 배열 체크 추가
    fetch(`${BASE_URL}/company/month_members`, { credentials: 'include' })
      .then(res => res.json())
      .then((res) => {
        console.log("📈 업체 월별 통계 데이터:", res);
        if (res?.result && res?.data) {
          // 안전한 배열 체크
          const monthlyData = res.data;
          if (!Array.isArray(monthlyData)) {
            console.warn("⚠️ 업체 월별 데이터가 배열이 아닙니다:", monthlyData);
            return;
          }

          const formattedStats = monthlyData.map(stat => ({
            month: stat[0],
            count: stat[1],
            type: 'company'
          }));
          setMonthlyStats(formattedStats);
        }
      })
      .catch((error) => {
        console.error("❌ 업체 월별 통계 조회 실패:", error);
        setMonthlyStats([]);
      });

    // 유저 월별 가입 통계 조회 - 안전한 배열 체크 추가
    fetch(`${BASE_URL}/user/month_users`, { credentials: 'include' })
      .then(res => res.json())
      .then((res) => {
        console.log("📈 유저 월별 통계 데이터:", res);
        if (res?.result && res?.data) {
          // 안전한 배열 체크
          const monthlyData = res.data;
          if (!Array.isArray(monthlyData)) {
            console.warn("⚠️ 유저 월별 데이터가 배열이 아닙니다:", monthlyData);
            return;
          }

          const formattedStats = monthlyData.map(stat => ({
            month: stat[0],
            count: stat[1],
            type: 'user'
          }));
          setUserMonthlyStats(formattedStats);
        }
      })
      .catch((error) => {
        console.error("❌ 유저 월별 통계 조회 실패:", error);
        setUserMonthlyStats([]);
      });
  }, []);

  const stats = [
    { 
      icon: <Flag className="text-3xl text-red-600" size={24} />, 
      label: "오늘 신고", 
      value: reportsToday
    },
    { 
      icon: <HelpCircle className="text-3xl text-blue-600" size={24} />, 
      label: "오늘 미답변 문의", 
      value: inquiriesToday 
    },
    { 
      icon: <Building className="text-3xl text-blue-600" size={24} />, 
      label: "신규 업체 회원", 
      value: dashboardData.newCompanies 
    },
    { 
      icon: <User className="text-3xl text-green-600" size={24} />, 
      label: "신규 유저 회원", 
      value: dashboardData.newUsers 
    }
  ];

  // 월별 통계 합치기 (업체 + 유저) - 안전한 배열 체크 추가
  const combinedMonthlyStats = [];
  
  // monthlyStats와 userMonthlyStats가 배열인지 확인
  if (Array.isArray(monthlyStats) && Array.isArray(userMonthlyStats)) {
    for (let month = 1; month <= 12; month++) {
      const companyData = monthlyStats.find(stat => stat.month === month);
      const userData = userMonthlyStats.find(stat => stat.month === month);
      
      const companyCount = companyData ? companyData.count : 0;
      const userCount = userData ? userData.count : 0;
      const totalCount = companyCount + userCount;
      
      if (totalCount > 0) {
        combinedMonthlyStats.push({
          month: month,
          companyCount: companyCount,
          userCount: userCount,
          totalCount: totalCount
        });
      }
    }
  }

  return (
    <div>
      <h2 className="text-xl text-black">관리자 통계 대시보드</h2>
      
      {/* 통계 카드들 */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-8 mb-12">
        {stats.map((item, idx) => (
          <div key={idx} className="flex items-center bg-[#F6F1FA] rounded-xl shadow p-6">
            <div className="mr-6">{item.icon}</div>
            <div>
              <div className="text-lg font-medium text-gray-800">{item.label}</div>
              <div className="text-2xl font-bold text-[#845EC2]">{item.value}</div>
            </div>
          </div>
        ))}
      </div>

      {/* 세부 통계 카드 추가 */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-8 mb-12">
        <div className="bg-white border rounded-xl p-6 shadow">
          <div className="text-lg font-medium text-black mb-2">오늘 신규 가입</div>
          <div className="space-y-2">
            <div className="flex justify-between">
              <span className="text-gray-600">업체</span>
              <span className="font-bold text-blue-600">{dashboardData.newCompanies}개</span>
            </div>
            <div className="flex justify-between">
              <span className="text-gray-600">유저</span>
              <span className="font-bold text-green-600">{dashboardData.newUsers}명</span>
            </div>
            <div className="border-t pt-2 flex justify-between">
              <span className="font-medium">총합</span>
              <span className="font-bold text-[#845EC2]">{dashboardData.totalNewMembers}</span>
            </div>
          </div>
        </div>
      </div>

      {/* 월별 가입자 수 차트 - 통합 데이터로 표시 */}
      <div className="bg-white border rounded-xl p-6 shadow">
        <div className="text-lg mb-3 text-black">월별 가입자 수 (업체 + 유저)</div>
        {combinedMonthlyStats.length > 0 ? (
          <div className="w-full flex items-end space-x-4 h-40 overflow-x-auto">
            {combinedMonthlyStats.map((stat, idx) => (
              <div key={idx} className="flex flex-col items-center min-w-16">
                <div className="flex flex-col items-center">
                  {/* 유저 (위쪽, 녹색) */}
                  {stat.userCount > 0 && (
                    <div 
                      className="bg-green-500 w-8 min-h-1" 
                      style={{ height: `${Math.max(stat.userCount * 3, 5)}px` }}
                      title={`유저: ${stat.userCount}명`}
                    />
                  )}
                  {/* 업체 (아래쪽, 파란색) */}
                  {stat.companyCount > 0 && (
                    <div 
                      className="bg-blue-500 w-8 min-h-1" 
                      style={{ height: `${Math.max(stat.companyCount * 3, 5)}px` }}
                      title={`업체: ${stat.companyCount}개`}
                    />
                  )}
                </div>
                <span className="text-sm text-black mt-1">{stat.month}월</span>
                <span className="text-xs text-gray-600">총 {stat.totalCount}</span>
              </div>
            ))}
          </div>
        ) : (
          <div className="flex items-center justify-center h-40 text-gray-500">
            <div className="text-center">
              <div className="text-lg mb-2">📊</div>
              <div>데이터를 불러오는 중...</div>
            </div>
          </div>
        )}
        
        {/* 범례 */}
        <div className="flex justify-center space-x-6 mt-4">
          <div className="flex items-center">
            <div className="w-4 h-4 bg-blue-500 mr-2"></div>
            <span className="text-sm text-gray-600">업체</span>
          </div>
          <div className="flex items-center">
            <div className="w-4 h-4 bg-green-500 mr-2"></div>
            <span className="text-sm text-gray-600">유저</span>
          </div>
        </div>
      </div>

      {/* 디버깅 정보 (개발 중에만 표시) */}
      {process.env.NODE_ENV === 'development' && (
        <div className="mt-8 p-4 bg-gray-100 rounded-lg">
          {/* <h3 className="font-semibold mb-2">디버깅 정보:</h3>
          <div className="text-sm space-y-1">
            <div>오늘 신고 수: {reportsToday}</div>
            <div>신규 업체 수: {dashboardData.newCompanies}</div>
            <div>신규 유저 수: {dashboardData.newUsers}</div>
            <div>전체 신규 회원: {dashboardData.totalNewMembers}</div>
            <div>업체 월별 데이터: {JSON.stringify(monthlyStats)}</div>
            <div>유저 월별 데이터: {JSON.stringify(userMonthlyStats)}</div>
            <div>업체 월별 데이터 타입: {Array.isArray(monthlyStats) ? '배열' : typeof monthlyStats}</div>
            <div>유저 월별 데이터 타입: {Array.isArray(userMonthlyStats) ? '배열' : typeof userMonthlyStats}</div>
          </div> */}
        </div>
      )}
    </div>
  );
}