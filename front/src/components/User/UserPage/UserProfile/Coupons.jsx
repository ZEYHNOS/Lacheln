import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import apiClient from "../../../../lib/apiClient";

const baseUrl = import.meta.env.VITE_API_BASE_URL;

const CouponPage = () => {
  const [userInfo, setUserInfo] = useState({ userId: "", name: "", nickname: "", email: "", phone: "", tier: "", notification: "", gender: "", mileage: 0, language: "", currency: "", profileImageUrl: "" });
  const [couponList, setCouponList] = useState([]);
  const [loading, setLoading] = useState(false);
  const [activeTab, setActiveTab] = useState('내 정보');
  const navigate = useNavigate();

  const tabs = [
    { name: '내 정보', path: '/user' },
    { name: '내 쿠폰', path: '/user/coupons' },
    { name: '찜 & 구독', path: '/user/wishlist' },
    { name: '주문내역 & 리뷰', path: '/user/review' }
  ];

  useEffect(() => {
    getUserProfile();
    getCouponList();
  }, []);

  const getUserProfile = async () => {
    try {
      const res = await apiClient.get(`${baseUrl}/user/profile`);
      if (res.status === 200) setUserInfo(res.data.data);
    } catch (err) {
      console.error("User 정보 로딩 실패 :", err);
    }
  };

  const getCouponList = async () => {
    setLoading(true);
    try {
      const res = await apiClient.get(`${baseUrl}/couponbox`); // API 엔드포인트는 실제에 맞게 수정해주세요
      if (res.status === 200 && res.data.result?.resultCode === 200) {
        setCouponList(res.data.data || []);
        console.log("쿠폰 데이터 로드 완료:", res.data.data);
      }
    } catch (err) {
      console.error("쿠폰 로딩 실패 :", err);
    } finally {
      setLoading(false);
    }
  };

  const convertArrayToDate = (arr) => {
    if (!arr || !Array.isArray(arr)) return null;
    const [y, m, d, h = 0, min = 0, s = 0] = arr;
    return new Date(y, m - 1, d, h, min, s);
  };

  const formatDate = (date) => {
    if (!date) return '날짜 정보 없음';
    const dt = date instanceof Date ? date : (Array.isArray(date) ? convertArrayToDate(date) : new Date(date));
    if (!dt || isNaN(dt.getTime())) return '날짜 형식 오류';
    return dt.toLocaleString('ko-KR', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit',
      hour12: false
    });
  };

  const handleTabClick = (tab) => {
    setActiveTab(tab.name);
    navigate(tab.path);
  };

  // 쿠폰 만료 여부 확인
  const isCouponExpired = (expirationDate) => {
    const expDate = convertArrayToDate(expirationDate);
    const now = new Date();
    return expDate && expDate < now;
  };

  // 쿠폰 상태별 스타일
  const getCouponStatusStyle = (expirationDate) => {
    if (isCouponExpired(expirationDate)) {
      return 'bg-gray-100 border-gray-300 opacity-60';
    }
    return 'bg-white border-purple-200 hover:shadow-lg';
  };

  // 유효한 쿠폰과 만료된 쿠폰 분리
  const validCoupons = couponList.filter(coupon => !isCouponExpired(coupon.couponExpirationDate));
  const expiredCoupons = couponList.filter(coupon => isCouponExpired(coupon.couponExpirationDate));

  return (
    <div className="bg-white min-h-screen">
      <div className="w-[880px] mx-auto">
        <nav className="flex px-8 pt-6 border-b border-gray-200">
          {tabs.map(tab => (
            <button
              key={tab.name}
              onClick={() => handleTabClick(tab)}
              className={`flex-1 text-center py-4 px-4 font-medium border-b-2 
                ${location.pathname === tab.path 
                    ? 'border-purple-600 text-purple-600 bg-pp text-white text-md'
                    : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
                }`}
            >
              {tab.name}
            </button>
          ))}
        </nav>
        
        <div className='flex justify-between'>
          <div className="pl-8 pt-6 pb-8 rounded-b-3xl">
            <h2 className="text-sm text-pp">안녕하세요</h2>
            <h1 className="text-xl font-bold text-pp">{userInfo.name || userInfo.nickname || '고객'} 님</h1>
          </div>
          <div className="flex gap-6 mt-6 text-sm mr-10">
            <div className="text-center text-pp">
              <div>사용 가능한 쿠폰</div>
              <div className="text-xl font-bold text-pp">{validCoupons.length}</div>
            </div>
            <div className="text-center text-pp">
              <div>만료된 쿠폰</div>
              <div className="text-xl font-bold text-gray-400">{expiredCoupons.length}</div>
            </div>
          </div>
        </div>

        <div className="px-8 mb-6">
          <h3 className="text-lg font-semibold text-pp mb-4">내 쿠폰함</h3>
        </div>

        <div className="px-8 space-y-4">
          {loading ? (
            <div className="py-12 text-center text-gray-500">쿠폰을 불러오는 중...</div>
          ) : couponList.length === 0 ? (
            <div className="py-12 text-center text-gray-500">보유한 쿠폰이 없습니다.</div>
          ) : (
            <>
              {/* 사용 가능한 쿠폰 */}
              {validCoupons.length > 0 && (
                <>
                  <h4 className="text-md font-medium text-gray-800 mb-3">사용 가능한 쿠폰</h4>
                  {validCoupons.map(coupon => (
                    <div key={coupon.boxId} className={`border-2 p-6 rounded-lg transition-all ${getCouponStatusStyle(coupon.couponExpirationDate)}`}>
                      <div className="flex justify-between items-start mb-4">
                        <div className="flex-1">
                          <div className="flex items-center gap-3 mb-2">
                            <h4 className="font-bold text-lg text-pp">{coupon.couponName}</h4>
                            <span className="px-3 py-1 bg-purple-100 text-purple-600 rounded-full text-sm font-medium">
                              {coupon.couponDiscountRate}% 할인
                            </span>
                          </div>
                          <p className="text-gray-600 mb-2">{coupon.couponContent}</p>
                          <p className="text-sm text-gray-500 mb-1">제공업체: {coupon.companyName}</p>
                          <div className="text-sm text-gray-500">
                            <p>최소 주문금액: {coupon.couponMinimumCost?.toLocaleString()}원</p>
                            <p>최대 할인금액: {coupon.couponMaximumCost?.toLocaleString()}원</p>
                          </div>
                        </div>
                        <div className="text-right">
                          <div className="text-xs text-gray-500 mb-1">발급일</div>
                          <div className="text-sm text-gray-700 mb-2">{formatDate(coupon.couponCreateDate)}</div>
                          <div className="text-xs text-red-500 mb-3">만료일: {formatDate(coupon.couponExpirationDate)}</div>
                          <div className="text-md text-green-500 mt-10 font-bold">사용 가능</div>
                        </div>
                      </div>
                      
                      <div className="border-t pt-3 mt-3">
                        <div className="flex justify-between items-center text-xs text-gray-400">
                          <span>쿠폰 ID: {coupon.couponId}</span>
                          <span>Box ID: {coupon.boxId}</span>
                        </div>
                      </div>
                    </div>
                  ))}
                </>
              )}

              {/* 만료된 쿠폰 */}
              {expiredCoupons.length > 0 && (
                <>
                  <h4 className="text-md font-medium text-gray-500 mb-3 mt-8">만료된 쿠폰</h4>
                  {expiredCoupons.map(coupon => (
                    <div key={coupon.boxId} className={`border-2 p-6 rounded-lg transition-all ${getCouponStatusStyle(coupon.couponExpirationDate)}`}>
                      <div className="flex justify-between items-start mb-4">
                        <div className="flex-1">
                          <div className="flex items-center gap-3 mb-2">
                            <h4 className="font-bold text-lg text-gray-500">{coupon.couponName}</h4>
                            <span className="px-3 py-1 bg-gray-100 text-gray-500 rounded-full text-sm font-medium">
                              {coupon.couponDiscountRate}% 할인
                            </span>
                            <span className="px-2 py-1 bg-red-100 text-red-600 rounded-full text-xs">
                              만료됨
                            </span>
                          </div>
                          <p className="text-gray-400 mb-2">{coupon.couponContent}</p>
                          <p className="text-sm text-gray-400 mb-1">제공업체: {coupon.companyName}</p>
                          <div className="text-sm text-gray-400">
                            <p>최소 주문금액: {coupon.couponMinimumCost?.toLocaleString()}원</p>
                            <p>최대 할인금액: {coupon.couponMaximumCost?.toLocaleString()}원</p>
                          </div>
                        </div>
                        <div className="text-right">
                          <div className="text-xs text-gray-400 mb-1">발급일</div>
                          <div className="text-sm text-gray-400 mb-2">{formatDate(coupon.couponCreateDate)}</div>
                          <div className="text-xs text-red-400 mb-3">만료일: {formatDate(coupon.couponExpirationDate)}</div>
                          <div className="text-md text-red-500 mt-10 font-bold">사용 불가</div>
                        </div>
                      </div>
                      
                      <div className="border-t pt-3 mt-3">
                        <div className="flex justify-between items-center text-xs text-gray-300">
                          <span>쿠폰 ID: {coupon.couponId}</span>
                          <span>Box ID: {coupon.boxId}</span>
                        </div>
                      </div>
                    </div>
                  ))}
                </>
              )}
            </>
          )}
        </div>

        <div className="px-8 mt-8 mb-8">
          <button className="w-full py-3 bg-pp text-white rounded-lg font-medium" onClick={() => navigate('/')}>
            메인 페이지
          </button>
        </div>
      </div>
    </div>
  );
};

export default CouponPage;