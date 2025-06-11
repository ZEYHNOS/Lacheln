import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import apiClient from "../../../../lib/apiClient";

const baseUrl = import.meta.env.VITE_API_BASE_URL;

const PaymentAndReview = () => {
  const [userInfo, setUserInfo] = useState({ userId: "", name: "", nickname: "", email: "", phone: "", tier: "", notification: "", gender: "", mileage: 0, language: "", currency: "", profileImageUrl: "" });
  const [orderList, setOrderList] = useState([]);
  const [reviewList, setReviewList] = useState([]);
  const [pagination, setPagination] = useState({ curPage: 0, curElement: 0, size: 0, totalPage: 0, totalElement: 0, order: "" });
  const [loading, setLoading] = useState(false);
  const [currentView, setCurrentView] = useState('orders');
  const navigate = useNavigate();

  const tabs = [
    { name: '내 정보', path: '/user' },
    { name: '내 쿠폰', path: '/user/coupons' },
    { name: '찜 & 구독', path: '/user/wishlist' },
    { name: '주문내역 & 리뷰', path: '/user/review' }
  ];

  useEffect(() => {
    getUserProfile();
    getOrderList();
    getReviewList();
  }, []);

  const getUserProfile = async () => {
    try {
      const res = await apiClient.get(`${baseUrl}/user/profile`);
      if (res.status === 200) setUserInfo(res.data.data);
    } catch (err) {
      console.error("User 정보 로딩 실패 :", err);
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
    second: '2-digit',
    hour12: false
  });
};


  const getCategoryText = (c) => {
    switch(c) {
      case 'D': return '드레스';
      case 'S': return '스튜디오';
      case 'M': return '메이크업';
      default: return c || '기타';
    }
  };

  const getPaymentStatusText = (s) => {
    switch(s) {
      case 'PAID': return '결제완료';
      case 'COMPLETED': return '완료';
      case 'PENDING': return '진행중';
      case 'CANCELLED': return '취소';
      case 'REFUNDED': return '환불';
      case 'FAILED': return '실패';
      default: return s || '상태 정보 없음';
    }
  };

  const getOrderStatusColor = (s) => {
    switch(s) {
      case '결제완료':
      case '완료': return 'text-green-600 bg-green-100';
      case '진행중': return 'text-blue-600 bg-blue-100';
      case '취소': return 'text-red-600 bg-red-100';
      case '환불': return 'text-orange-600 bg-orange-100';
      default: return 'text-gray-600 bg-gray-100';
    }
  };

  const getReviewStatusText = (s) => {
    switch(s) {
      case 'REGISTERED': return '등록됨';
      case 'PENDING': return '대기중';
      case 'DELETED': return '삭제됨';
      default: return s;
    }
  };

  const getReviewStatusColor = (s) => {
    switch(s) {
      case 'REGISTERED': return 'text-green-600 bg-green-100';
      case 'PENDING': return 'text-yellow-600 bg-yellow-100';
      case 'DELETED': return 'text-red-600 bg-red-100';
      default: return 'text-gray-600 bg-gray-100';
    }
  };

  const renderStars = (rating) => {
    const r = Math.round(rating);
    return [...Array(5)].map((_, i) => (
      <span key={i} className={`text-lg ${i < r ? 'text-yellow-400' : 'text-gray-300'}`}>★</span>
    ));
  };

  const getOrderList = async () => {
    setLoading(true);
    try {
      const res = await apiClient.get(`${baseUrl}/payment/user/list`);
      if (res.status === 200 && res.data.result?.resultCode === 200) {
        const transformed = res.data.data.map(p => {
          const paid = convertArrayToDate(p.paidAt);
          const sched = convertArrayToDate(p.scheduleAt);
          return {
            id: p.payDetailId,
            orderNumber: `PAY${p.payDetailId}`,
            date: formatDate(paid),
            scheduleAt: sched ? formatDate(sched) : null,
            productName: p.pdName || '상품명 정보 없음',
            instructor: p.cpId ? `업체 ID: ${p.cpId}` : '정보 없음',
            price: p.payCost ? Number(p.payCost) : 0,
            status: getPaymentStatusText(p.status),
            reviewWritten: false,
            couponName: p.couponName,
            category: getCategoryText(p.category),
            refundPrice: p.refundPrice ? Number(p.refundPrice) : 0,
            options: p.options || [],
            userName: p.userName
          };
        });
        setOrderList(transformed);
      }
    } catch (err) {
      console.error("주문내역 로딩 실패 :", err);
    } finally {
      setLoading(false);
    }
  };

  const getReviewList = async () => {
    setLoading(true);
    try {
      const res = await apiClient.get(`${baseUrl}/review/user`);
      if (res.status === 200 && res.data.result?.resultCode === 200) {
        setReviewList(res.data.data || []);
        setPagination(res.data.pagination || {});
      }
      console.log(res.data);
      console.log(reviewList.length);
    } catch (err) {
      console.error("리뷰 로딩 실패 :", err);
    } finally {
      setLoading(false);
    }
  };

  const handleTabClick = (tab) => {
    navigate(tab.path);
  };

  return (
    <div className="bg-white min-h-screen">
      <div className="w-[880px] mx-auto">
        <nav className="flex px-8 pt-6 border-b border-gray-200">
          {tabs.map(tab => (
            <button
              key={tab.name}
              onClick={() => handleTabClick(tab)}
              className={`flex-1 text-center py-4 px-4 font-medium border-b-2 ${location.pathname === tab.path ? 'border-purple-600 text-purple-600' : 'border-transparent text-gray-500 hover:text-gray-700'}`}
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
            <div className="text-center text-pp"><div>주문내역</div><div className="text-xl font-bold text-pp">{orderList.length}</div></div>
            <div className="text-center text-pp"><div>리뷰</div><div className="text-xl font-bold text-pp">{reviewList.length}</div></div>
          </div>
        </div>

        <div className="flex gap-3 px-8 mb-6">
          <button onClick={() => setCurrentView('orders')} className={`px-6 py-3 rounded-lg font-medium ${currentView === 'orders' ? 'bg-purple-600 text-white' : 'bg-gray-100 text-gray-700'}`}>결제내역 ({orderList.length})</button>
          <button onClick={() => setCurrentView('reviews')} className={`px-6 py-3 rounded-lg font-medium ${currentView === 'reviews' ? 'bg-purple-600 text-white' : 'bg-gray-100 text-gray-700'}`}>내 리뷰 ({reviewList.length})</button>
        </div>

        {currentView === 'orders' ? (
          <div className="px-8 space-y-4">
            {loading ? (
              <div className="py-12 text-center text-gray-500">결제내역을 불러오는 중...</div>
            ) : orderList.length === 0 ? (
              <div className="py-12 text-center text-gray-500">결제내역이 없습니다.</div>
            ) : orderList.map(o => (
              <div key={o.id} className="border p-6 rounded-lg hover:shadow-md transition-shadow">
                <div className="flex justify-between items-start mb-4">
                  <div>
                    <div className="text-sm text-gray-500">결제번호: {o.orderNumber}</div>
                    <div className="text-sm text-gray-500">결제일: {o.date}</div>
                    {o.scheduleAt && <div className="text-sm text-gray-500">예약일: {o.scheduleAt}</div>}
                    {o.userName && <div className="text-sm text-gray-500">주문자: {o.userName}</div>}
                  </div>
                  <span className={`px-3 py-1 rounded-full text-sm ${getOrderStatusColor(o.status)}`}>{o.status}</span>
                </div>
              
                <div className="mb-4">
                  <h4 className="font-medium text-pp">{o.productName}</h4>
                  <p className="text-gray-600">{o.instructor}</p>
                  {o.couponName && <p className="text-sm text-blue-600">쿠폰: {o.couponName}</p>}
                  <p className="text-sm text-gray-500">카테고리: {o.category}</p>
                </div>

                {o.options.length > 0 && (
                  <div className="mb-4">
                    <p className="text-sm text-gray-600 mb-2">선택 옵션:</p>
                    <div className="flex flex-wrap gap-2">
                      {o.options.map((opt, i) => (
                        <span key={i} className="px-2 py-1 text-xs bg-gray-100 rounded">{opt.name || `옵션 ${i+1}`}</span>
                      ))}
                    </div>
                  </div>
                )}

                <div className="flex justify-between items-center">
                  <div>
                    <div className="text-lg font-bold text-pp">{o.price.toLocaleString()}원</div>
                    {o.refundPrice > 0 && <div className="text-sm text-red-600">환불금액: {o.refundPrice.toLocaleString()}원</div>}
                  </div>
                  <div className="flex gap-2">
                    {(o.status === '완료' || o.status === '결제완료') && !o.reviewWritten ? (
                      <button className="px-4 py-2 bg-purple-600 text-white rounded-lg">리뷰 작성</button>
                    ) : (
                      <span className="px-4 py-2 bg-gray-100 text-gray-600 rounded-lg">리뷰 작성완료</span>
                    )}
                    <button className="px-4 py-2 border border-gray-300 rounded-lg">상세보기</button>
                  </div>
                </div>
              </div>
            ))}
          </div>
        ) : (
          <div className="px-8 space-y-4">
            {loading ? (
              <div className="py-12 text-center text-gray-500">리뷰를 불러오는 중...</div>
            ) : reviewList.length === 0 ? (
              <div className="py-12 text-center text-gray-500">작성한 리뷰가 없습니다.</div>
            ) : reviewList.map((r, idx) => (
              <div key={idx} className="border p-6 rounded-lg hover:shadow-md transition-shadow">
                <div className="flex justify-between items-start mb-4">
                  <div>
                    <div className="flex items-center gap-3 mb-2">
                      <h4 className="font-medium text-pp">{r.productName}</h4>
                      <span className={`px-2 py-1 rounded-full text-xs ${getReviewStatusColor(r.status)}`}>{getReviewStatusText(r.status)}</span>
                    </div>
                    <p className="text-gray-600 mb-2">작성자: {r.nickname}</p>
                    <div className="flex items-center gap-2">
                      {renderStars(r.score)}
                      <span className="text-sm text-gray-500">({r.score}/5)</span>
                    </div>
                  </div>
                  <div className="text-sm text-gray-500">{formatDate(r.createdAt)}</div>
                </div>
              
                <p className="text-gray-700 leading-relaxed mb-4">{r.content}</p>

                {r.imageUrlList?.length > 0 && (
                  <div className="flex gap-2 mb-4">
                    {r.imageUrlList.map((u, i) => (
                      <img key={i} src={u} alt={`리뷰 이미지 ${i+1}`} className="w-20 h-20 object-cover rounded-lg border" onError={e => e.currentTarget.style.display = 'none'} />
                    ))}
                  </div>
                )}

                <div className="flex justify-between items-center text-sm text-gray-500">
                  <span>상품 ID: {r.productId}</span>
                  <span>업체 ID: {r.companyId}</span>
                  {r.status === 'REGISTERED' && (
                    <div className="flex gap-2">
                      <button className="px-3 py-1 border rounded text-sm">수정</button>
                      <button className="px-3 py-1 border rounded text-sm">삭제</button>
                    </div>
                  )}
                </div>
              </div>
            ))}

            {pagination.totalPage > 1 && (
              <div className="flex justify-center mt-6 text-sm text-gray-500">
                페이지 {pagination.curPage + 1} / {pagination.totalPage}
              </div>
            )}
          </div>
        )}

        <div className="px-8 mt-8 mb-8">
          <button className="w-full py-3 bg-pp text-white rounded-lg font-medium" onClick={() => navigate('/')}>메인 페이지</button>
        </div>
      </div>
    </div>
  );
};

export default PaymentAndReview;
