import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import apiClient from "../../../../lib/apiClient";
import ReviewModal from '../../UserPage/review';

const baseUrl = import.meta.env.VITE_API_BASE_URL;
const baseImageUrl = 'http://localhost:5050';

const PaymentAndReview = () => {
  const [userInfo, setUserInfo] = useState({ userId: "", name: "", nickname: "", email: "", phone: "", tier: "", notification: "", gender: "", mileage: 0, language: "", currency: "", profileImageUrl: "" });
  const [orderList, setOrderList] = useState([]);
  const [reviewList, setReviewList] = useState([]);
  const [pagination, setPagination] = useState({ curPage: 0, curElement: 0, size: 0, totalPage: 0, totalElement: 0, order: "" });
  const [loading, setLoading] = useState(false);
  const [currentView, setCurrentView] = useState('orders');
  const [isReviewOpen, setIsReviewOpen] = useState(false);
  const [activeTab, setActiveTab] = useState('주문내역 & 리뷰');
  const [reviewTarget, setReviewTarget] = useState({ reviewId: null, cpId: null, pdName: '' });
  const navigate = useNavigate();

  const tabs = [
    { name: '내 정보', path: '/user' },
    { name: '내 쿠폰', path: '/user/coupons' },
    { name: '찜 & 구독', path: '/user/wishlist' },
    { name: '주문내역 & 리뷰', path: '/user/review' }
  ];

  useEffect(() => {
    getUserProfile();
    // 리뷰를 먼저 받아온 후 결제내역을 받아오도록 순서 변경
    loadReviewsAndOrders();
  }, []);

  useEffect(() => {

  }, [reviewList]);

  // 리뷰와 주문내역을 순차적으로 로드하는 함수 (수정됨)
  const loadReviewsAndOrders = async () => {
    setLoading(true);
    try {
      // 1. 리뷰 데이터를 먼저 받아옴
      const reviewData = await getReviewList();
      // 2. 리뷰 데이터를 getOrderList에 전달
      await getOrderList(reviewData);
    } catch (err) {
      console.error("데이터 로딩 실패:", err);
    } finally {
      setLoading(false);
    }
  };

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
      millisecond: '2-digit',
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
      case 'REPLY_NEEDED': return '답변 필요';
      default: return s;
    }
  };

  const getReviewStatusColor = (s) => {
    switch(s) {
      case 'REGISTERED': return 'text-green-600 bg-green-100';
      case 'PENDING': return 'text-yellow-600 bg-yellow-100';
      case 'DELETED': return 'text-red-600 bg-red-100';
      case 'REPLY_NEEDED': return 'text-blue-600 bg-blue-100';
      default: return 'text-gray-600 bg-gray-100';
    }
  };

  const renderStars = (rating) => {
    const r = Math.round(rating);
    return [...Array(5)].map((_, i) => (
      <span key={i} className={`text-lg ${i < r ? 'text-yellow-400' : 'text-gray-300'}`}>★</span>
    ));
  };

  // 리뷰 상태 확인 함수 (수정됨 - 매개변수로 reviewData 받음)
  const getReviewStatusForOrder = (payDetailId, reviewData = reviewList) => {
    const review = reviewData.find(r => r.payDtId === payDetailId);
    if (!review) return { hasReview: false, status: null };
    return { hasReview: true, status: review.status };
  };

  // 표시할 리뷰 필터링 (REGISTERED 상태만)
  const getDisplayReviews = () => {
    return reviewList.filter(r => r.status === 'REGISTERED');
  };

  // 옵션 텍스트 포맷팅 함수 추가
  const formatOptionText = (option) => {
    const optionName = option.payOpName || '옵션';
    const optionValue = option.payOpDtName || '';
    const quantity = option.payDtQuantity || 1;
    const plusCost = option.payOpPlusCost || 0;
    
    let text = `${optionName}: ${optionValue} -> ${quantity}EA`;
    if (quantity > 1) {
      text += ` (수량: ${quantity})`;
    }
    if (plusCost > 0) {
      text += ` (+${plusCost.toLocaleString()}원)`;
    }
    
    return text;
  };

  // getOrderList 함수 수정 - reviewData를 매개변수로 받음
  const getOrderList = async (reviewData = []) => {
    try {
      const res = await apiClient.get(`${baseUrl}/payment/user/list`);
      if (res.status === 200 && res.data.result?.resultCode === 200) {
        const transformed = res.data.data.map(p => {
          // 전달받은 reviewData에서 매칭되는 리뷰 찾기
          const matchingReview = reviewData.find(review => review.payDtId === p.payDetailId);
          
          return {
            payDetailId: p.payDetailId,
            cpId: p.cpId,
            pdName: p.pdName,
            price: p.payCost ? Number(p.payCost) : 0,
            refundPrice: p.refundPrice ? Number(p.refundPrice) : 0,
            status: getPaymentStatusText(p.status),
            couponName: p.couponName,
            category: getCategoryText(p.category),
            options: p.options || [],
            userName: p.userName,
            date: formatDate(convertArrayToDate(p.paidAt)),
            scheduleAt: p.scheduleAt ? formatDate(convertArrayToDate(p.scheduleAt)) : null,
            orderNumber: `PAY${p.payDetailId}`,
            instructor: p.cpId ? `업체 ID: ${p.cpId}` : '정보 없음',
            // 매칭되는 리뷰가 있으면 reviewId 추가
            reviewId: matchingReview ? matchingReview.reviewId : null,
          };
        });
        setOrderList(transformed);
        console.log("결제내역에 reviewId 추가 완료:", transformed);
      }
    } catch (err) {
      console.error("주문내역 로딩 실패 :", err);
    }
  };

  // getReviewList 함수 수정 - 데이터를 반환하도록 변경
  const getReviewList = async () => {
    try {
      const res = await apiClient.get(`${baseUrl}/review/user`);
      if (res.status === 200 && res.data.result?.resultCode === 200) {
        const reviewData = res.data.data || [];
        setReviewList(reviewData);
        setPagination(res.data.pagination || {});
        console.log("리뷰 데이터 로드 완료:", reviewData);
        return reviewData; // 데이터를 반환
      }
      return []; // 실패시 빈 배열 반환
    } catch (err) {
      console.error("리뷰 로딩 실패 :", err);
      return []; // 에러시 빈 배열 반환
    }
  };

  const handleTabClick = (tab) => {
    setActiveTab(tab.name);
    navigate(tab.path);
  };

  // 리뷰 수정 모달 열기
  const handleEditReview = (review) => {
    setReviewTarget({ 
      reviewId: review.reviewId, 
      cpId: review.companyId, 
      pdName: review.productName,
      isEdit: true,
      existingReview: review
    });
    setIsReviewOpen(true);
  };

  // 리뷰 삭제
  const handleDeleteReview = async (reviewId) => {

    const confirmed = window.confirm("리뷰를 정말 삭제하시겠습니까?");

    if(confirmed) {
      try {
        const res = await apiClient.delete(`${baseUrl}/review/${reviewId}`);
        if(res.status === 200) {
          console.log("리뷰 데이터 삭제 완료");
          
          // ✅ 로컬 상태에서 해당 리뷰 제거
          setReviewList(prevList => 
            prevList.filter(review => review.reviewId !== reviewId)
          );
          
          // 선택사항: 주문내역도 업데이트 (reviewId 제거)
          setOrderList(prevOrders => 
            prevOrders.map(order => 
              order.reviewId === reviewId 
                ? { ...order, reviewId: null }
                : order
            )
          );
        }
      } catch (err) {
        console.error("리뷰 삭제 실패 :", err);
      }
    }
  }

  // 표시할 리뷰 목록
  const displayReviews = getDisplayReviews();

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
            <div className="text-center text-pp"><div>주문내역</div><div className="text-xl font-bold text-pp">{orderList.length}</div></div>
            <div className="text-center text-pp"><div>리뷰</div><div className="text-xl font-bold text-pp">{displayReviews.length}</div></div>
          </div>
        </div>

        <div className="flex gap-3 px-8 mb-6">
          <button onClick={() => setCurrentView('orders')} className={`px-6 py-3 rounded-lg font-medium ${currentView === 'orders' ? 'bg-purple-600 text-white' : 'bg-gray-100 text-gray-700'}`}>결제내역 ({orderList.length})</button>
          <button onClick={() => setCurrentView('reviews')} className={`px-6 py-3 rounded-lg font-medium ${currentView === 'reviews' ? 'bg-purple-600 text-white' : 'bg-gray-100 text-gray-700'}`}>내 리뷰 ({displayReviews.length})</button>
        </div>

        {currentView === 'orders' ? (
          <div className="px-8 space-y-4">
            {loading ? (
              <div className="py-12 text-center text-gray-500">결제내역을 불러오는 중...</div>
            ) : orderList.length === 0 ? (
              <div className="py-12 text-center text-gray-500">결제내역이 없습니다.</div>
            ) : orderList.map(o => {
              const reviewStatus = getReviewStatusForOrder(o.payDetailId);
              return (
                <div key={o.payDetailId} className="border p-6 rounded-lg hover:shadow-md transition-shadow">
                  <div className="flex justify-between items-start mb-4">
                    <div>
                      <div className="text-sm text-gray-500">결제번호: {o.orderNumber}</div>
                      <div className="text-sm text-gray-500">결제일: {o.date}</div>
                      {o.scheduleAt && <div className="text-sm text-gray-500">예약일: {o.scheduleAt}</div>}
                      {o.userName && <div className="text-sm text-gray-500">주문자: {o.userName}</div>}
                      {/* reviewId가 있으면 표시 (디버깅용) */}
                      {o.reviewId && <div className="text-sm text-blue-500">리뷰 ID: {o.reviewId}</div>}
                    </div>
                    <span className={`px-3 py-1 rounded-full text-sm ${getOrderStatusColor(o.status)}`}>{o.status}</span>
                  </div>
                
                  <div className="mb-4">
                    <h4 className="font-medium text-pp">{o.pdName}</h4>
                    <p className="text-gray-600">{o.instructor}</p>
                    {o.couponName && <p className="text-sm text-blue-600">쿠폰: {o.couponName}</p>}
                    <p className="text-sm text-gray-500">카테고리: {o.category}</p>
                  </div>

                  {/* 옵션 표시 부분 수정 */}
                  {o.options && o.options.length > 0 && (
                    <div className="mb-4">
                      <p className="text-sm text-gray-600 mb-2">선택 옵션:</p>
                      <div className="space-y-1">
                        {o.options.map((opt, i) => (
                          <div key={i} className="flex items-center justify-between px-3 py-2 bg-gray-50 rounded-lg">
                            <span className="text-sm text-gray-700">
                              {formatOptionText(opt)}
                            </span>
                            {opt.payOpPlusCost > 0 && (
                              <span className="text-sm font-medium text-purple-600">
                                +{opt.payOpPlusCost.toLocaleString()}원
                              </span>
                            )}
                          </div>
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
                      {/* 리뷰 작성/상태 버튼 로직 */}
                      {(o.status === '완료' || o.status === '결제완료') && (
                        <>
                          {reviewStatus.hasReview && reviewStatus.status === 'REGISTERED' && (
                            <span className="px-4 py-2 bg-green-100 text-green-600 rounded-lg text-sm">
                              리뷰 작성완료
                            </span>
                          )}
                          {reviewStatus.hasReview && reviewStatus.status === 'REPLY_NEEDED' && (
                            <button 
                              className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700" 
                              onClick={() => {
                                setReviewTarget({ 
                                  reviewId: o.reviewId, 
                                  cpId: o.cpId, 
                                  pdName: o.pdName,
                                  isEdit: false
                                });
                                setIsReviewOpen(true);
                              }}
                            >
                              리뷰 작성
                            </button>
                          )}
                        </>
                      )}
                      <button className="px-4 py-2 border border-gray-300 rounded-lg hover:bg-gray-50">상세보기</button>
                    </div>
                  </div>
                </div>
              );
            })}
          </div>
        ) : (
          <div className="px-8 space-y-4">
            {loading ? (
              <div className="py-12 text-center text-gray-500">리뷰를 불러오는 중...</div>
            ) : displayReviews.length === 0 ? (
              <div className="py-12 text-center text-gray-500">작성한 리뷰가 없습니다.</div>
            ) : displayReviews.map((r, idx) => (
              <div key={idx} className="border p-6 rounded-lg hover:shadow-md transition-shadow">
                <div className="flex justify-between items-start mb-4">
                  <div>
                    <div className="flex items-center gap-3 mb-2">
                      <h4 className="font-medium text-pp">{r.productName}</h4>
                      <span className={`px-2 py-1 rounded-full text-xs ${getReviewStatusColor(r.status)}`}>
                        {getReviewStatusText(r.status)}
                      </span>
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
                  <div className="flex gap-4 mb-4">
                    {r.imageUrlList.map((u, i) => (
                      <img 
                        key={`${r.reviewId}-${i}-${u}`} // ✅ 고유한 key 생성
                        src={baseImageUrl + u} 
                        alt={`리뷰 이미지 ${i + 1}`} // ✅ 간단한 alt 텍스트
                        className="w-20 h-20 object-cover rounded-lg border" 
                        onError={(e) => {
                          console.log(`이미지 로딩 실패: ${baseImageUrl + u}`);
                          e.currentTarget.src = '/placeholder-image.png'; // ✅ 플레이스홀더 이미지로 대체
                          // 또는 완전히 제거하려면:
                          // e.currentTarget.style.display = 'none';
                        }}
                        onLoad={() => {
                          console.log(`이미지 로딩 성공: ${baseImageUrl + u}`);
                        }}
                      />
                    ))}
                  </div>
                )}

                <div className="flex justify-between items-center text-sm text-gray-500">
                  <span>상품 ID: {r.productId}</span>
                  <span>업체 ID: {r.companyId}</span>
                  {r.status === 'REGISTERED' && (
                    <div className="flex gap-2">
                      <button 
                        className="px-3 py-1 border rounded text-sm bg-red-400 hover:bg-red-600 text-white"
                        onClick={() => handleDeleteReview(r.reviewId)}>삭제</button>
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
        <ReviewModal 
          isOpen={isReviewOpen} 
          onClose={() => setIsReviewOpen(false)} 
          reviewId={reviewTarget.reviewId} 
          cpId={reviewTarget.cpId} 
          pdName={reviewTarget.pdName}
          isEdit={reviewTarget.isEdit}
          existingReview={reviewTarget.existingReview}
        />
      </div>
    </div>
  );
};

export default PaymentAndReview;