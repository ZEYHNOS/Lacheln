import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import apiClient from "../../../../lib/apiClient";

const baseUrl = import.meta.env.VITE_API_BASE_URL;

const PaymentAndReview = () => {
  const [userInfo, setUserInfo] = useState({
    userId: "",
    name: "",
    nickname: "",
    email: "",
    phone: "",
    tier: "",
    notification: "",
    gender: "",
    mileage: 0,
    language: "",
    currency: "",
    profileImageUrl: ""
  });
  const [activeTab, setActiveTab] = useState('주문내역 & 리뷰');
  const [orderList, setOrderList] = useState([]);
  const [reviewList, setReviewList] = useState([]);
  const [currentView, setCurrentView] = useState('orders'); // 'orders' or 'reviews'
  const [pagination, setPagination] = useState({
    curPage: 0,
    curElement: 0,
    size: 0,
    totalPage: 0,
    totalElement: 0,
    order: ""
  });
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const tabs = [
    { name: '내 정보', path: '/user' },
    { name: '내 쿠폰', path: '/user/coupons' },
    { name: '찜 & 구독', path: '/user/wishlist' },
    { name: '주문내역 & 리뷰', path: '/user/review' }
  ];

  // 샘플 주문 데이터 (API가 없는 경우 사용)
  const sampleOrders = [
    {
      id: 1,
      orderNumber: "ORD20241201001",
      date: "2024-12-01",
      productName: "프리미엄 골프 레슨",
      instructor: "김코치",
      price: 150000,
      status: "완료",
      reviewWritten: true
    },
    {
      id: 2,
      orderNumber: "ORD20241125002",
      date: "2024-11-25",
      productName: "골프 기초 클래스",
      instructor: "박프로",
      price: 80000,
      status: "완료",
      reviewWritten: false
    },
    {
      id: 3,
      orderNumber: "ORD20241120003",
      date: "2024-11-20",
      productName: "골프 스윙 교정",
      instructor: "이코치",
      price: 120000,
      status: "진행중",
      reviewWritten: false
    }
  ];

  useEffect(() => {
    getUserProfile();
    getOrderList();
    getReviewList();
  }, []);

  const getUserProfile = async () => {
    try {
      const res = await apiClient.get(`${baseUrl}/user/profile`, {
        headers: { "Content-Type": "application/json" },
      });
      if(res.status === 200) {
        console.log(res.data.data);
        setUserInfo(res.data.data);
      }
    } catch (err) {
      console.error("User 정보 로딩 실패 :", err);
    }
  };

  // 배열 형태의 날짜를 Date 객체로 변환하는 함수
  const convertArrayToDate = (dateArray) => {
    if (!dateArray || !Array.isArray(dateArray)) return null;
    
    // [2025, 6, 10, 20, 29, 32] -> new Date(2025, 5, 10, 20, 29, 32)
    // 월은 0부터 시작하므로 -1 해줌
    const [year, month, day, hour = 0, minute = 0, second = 0] = dateArray;
    return new Date(year, month - 1, day, hour, minute, second);
  };

  // 카테고리 텍스트 변환
  const getCategoryText = (category) => {
    switch(category) {
      case 'D': return '드레스';
      case 'S': return '스튜디오';
      case 'M': return '메이크업';
      default: return category || '기타';
    }
  };

  const getOrderList = async () => {
    setLoading(true);
    try {
      const res = await apiClient.get(`${baseUrl}/payment/user/list`, {
        headers: { "Content-Type": "application/json" },
      });
      
      console.log("Payment Data : ", res.data);
      console.log("Payment resultCode : ", res.data.result?.resultCode);
      
      if(res.status === 200 && res.data.result?.resultCode === 200) {
        console.log("결제 내역 데이터 : ", res.data.data);
        
        // API 데이터를 화면 표시용으로 변환
        const transformedOrders = res.data.data.map(payment => {
          const paidDate = convertArrayToDate(payment.paidAt);
          const scheduleDate = convertArrayToDate(payment.scheduleAt);
          
          return {
            id: payment.payDetailId,
            orderNumber: `PAY${payment.payDetailId}`,
            date: paidDate ? formatDate(paidDate) : '날짜 정보 없음',
            productName: payment.pdName || '상품명 정보 없음',
            instructor: payment.cpId ? `업체 ID: ${payment.cpId}` : '정보 없음',
            price: payment.payCost ? Number(payment.payCost) : 0,
            status: getPaymentStatusText(payment.status),
            reviewWritten: false, // 리뷰 작성 여부는 별도 로직으로 확인 필요
            scheduleAt: scheduleDate ? formatDate(scheduleDate) : null,
            couponName: payment.couponName,
            category: getCategoryText(payment.category),
            refundPrice: payment.refundPrice ? Number(payment.refundPrice) : 0,
            options: payment.options || [],
            userName: payment.userName
          };
        });
        
        setOrderList(transformedOrders);
      } else {
        console.log("API 응답이 예상과 다름, 샘플 데이터 사용");
        // API 실패시 샘플 데이터 사용
        setOrderList(sampleOrders);
      }
    } catch (err) {
      console.error("주문내역 로딩 실패 :", err);
      setOrderList(sampleOrders);
    } finally {
      setLoading(false);
    }
  };

  const getReviewList = async () => {
    setLoading(true);
    try {
      const res = await apiClient.get(`${baseUrl}/review/user`, {
        headers: { "Content-Type": "application/json" },
      });
      
      if(res.status === 200 && res.data.result.resultCode === 0) {
        console.log("리뷰 데이터:", res.data.data);
        setReviewList(res.data.data || []);
        setPagination(res.data.pagination || {});
      }
    } catch (err) {
      console.error("리뷰 로딩 실패 :", err);
      setReviewList([]);
    } finally {
      setLoading(false);
    }
  };

  // 탭 클릭 핸들러
  const handleTabClick = (tab) => {
    setActiveTab(tab.name);
    navigate(tab.path);
  };

  // 메인 페이지
  const handleMainPage = () => {
    navigate("/");
  };

  // 결제 상태 텍스트 변환
  const getPaymentStatusText = (status) => {
    switch(status) {
      case 'PAID': return '결제완료';
      case 'COMPLETED': return '완료';
      case 'PENDING': return '진행중';
      case 'CANCELLED': return '취소';
      case 'REFUNDED': return '환불';
      case 'FAILED': return '실패';
      default: return status || '상태 정보 없음';
    }
  };

  // 결제 상태 표시 함수
  const getPaymentStatusColor = (status) => {
    switch(status) {
      case 'PAID':
      case 'COMPLETED': return 'text-green-600 bg-green-100';
      case 'PENDING': return 'text-blue-600 bg-blue-100';
      case 'CANCELLED': return 'text-red-600 bg-red-100';
      case 'REFUNDED': return 'text-orange-600 bg-orange-100';
      case 'FAILED': return 'text-red-600 bg-red-100';
      default: return 'text-gray-600 bg-gray-100';
    }
  };

  // 주문 상태 표시 함수
  const getOrderStatusColor = (status) => {
    switch(status) {
      case '결제완료':
      case '완료': return 'text-green-600 bg-green-100';
      case '진행중': return 'text-blue-600 bg-blue-100';
      case '취소': return 'text-red-600 bg-red-100';
      case '환불': return 'text-orange-600 bg-orange-100';
      case '실패': return 'text-red-600 bg-red-100';
      default: return 'text-gray-600 bg-gray-100';
    }
  };

  // 리뷰 상태 표시 함수
  const getReviewStatusColor = (status) => {
    switch(status) {
      case 'REGISTERED': return 'text-green-600 bg-green-100';
      case 'PENDING': return 'text-yellow-600 bg-yellow-100';
      case 'DELETED': return 'text-red-600 bg-red-100';
      default: return 'text-gray-600 bg-gray-100';
    }
  };

  // 리뷰 상태 텍스트 변환
  const getReviewStatusText = (status) => {
    switch(status) {
      case 'REGISTERED': return '등록됨';
      case 'PENDING': return '대기중';
      case 'DELETED': return '삭제됨';
      default: return status;
    }
  };

  // 별점 표시
  const renderStars = (rating) => {
    return [...Array(5)].map((_, index) => (
      <span key={index} className={`text-lg ${index < rating ? 'text-yellow-400' : 'text-gray-300'}`}>
        ★
      </span>
    ));
  };

  // 날짜 포맷팅
  const formatDate = (date) => {
    if (!date) return '날짜 정보 없음';
    
    // Date 객체인 경우
    if (date instanceof Date) {
      return date.toLocaleDateString('ko-KR', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit'
      });
    }
    
    // 문자열인 경우
    const dateObj = new Date(date);
    if (isNaN(dateObj.getTime())) {
      return '날짜 형식 오류';
    }
    
    return dateObj.toLocaleDateString('ko-KR', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit'
    });
  };

  return (
    <div className="bg-white min-h-screen">
      {/* Main Profile Section */}
      <div className="w-[880px] mx-auto bg-white">
        {/* Tab Navigation */}
        <div className="border-b border-gray-200">
          <nav className="flex px-8 pt-6">
            {tabs.map((tab) => (
              <button
                key={tab.name}
                onClick={() => handleTabClick(tab)}
                className={`flex-1 grid place-items-center pb-4 px-4 border-b-2 font-medium text-md transition-colors duration-200 text-center ${
                  activeTab === tab.name
                    ? 'border-purple-600 text-purple-600 bg-pp text-white text-md'
                    : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
                }`}
              >
                {tab.name}
              </button>
            ))}
          </nav>
        </div>

        {/* Profile Header */}
        <div className="relative pl-8 rounded-b-3xl">
          <div className="flex items-center justify-between">
            <div className="flex-1">
              <h2 className="text-pp text-sm mb-1">안녕하세요</h2>
              <h1 className="text-xl font-bold text-pp">{userInfo.name || userInfo.nickname || '고객'} 님</h1>
            </div>
            <div className="flex items-center justify-between mt-10 mr-10">
              <div className="flex gap-6 text-sm">
                <div className="text-center">
                  <div className="text-pp text-xl font-bold mb-2">주문내역</div>
                  <div className="text-pp text-2xl">{orderList.length}건</div>
                </div>
                <div className="text-center">
                  <div className="text-pp text-xl font-bold mb-2">리뷰</div>
                  <div className="text-pp text-2xl">{reviewList.length}건</div>
                </div>
              </div>
            </div>
          </div>
        </div>

        {/* Content Toggle Buttons */}
        <div className="px-8 mb-6">
          <div className="flex gap-3">
            <button 
              onClick={() => setCurrentView('orders')}
              className={`px-6 py-3 rounded-lg font-medium transition-colors ${
                currentView === 'orders' 
                  ? 'bg-purple-600 text-white' 
                  : 'bg-gray-100 text-gray-700 hover:bg-gray-200'
              }`}
            >
              결제내역 ({orderList.length})
            </button>
            <button 
              onClick={() => setCurrentView('reviews')}
              className={`px-6 py-3 rounded-lg font-medium transition-colors ${
                currentView === 'reviews' 
                  ? 'bg-purple-600 text-white' 
                  : 'bg-gray-100 text-gray-700 hover:bg-gray-200'
              }`}
            >
              내 리뷰 ({reviewList.length})
            </button>
          </div>
        </div>

        {/* Orders List */}
        {currentView === 'orders' && (
          <div className="px-8 space-y-4">
            <h3 className="text-lg font-bold text-pp mb-4">결제내역</h3>
            {loading ? (
              <div className="text-center py-12 text-gray-500">
                결제내역을 불러오는 중...
              </div>
            ) : orderList.length === 0 ? (
              <div className="text-center py-12 text-gray-500">
                결제내역이 없습니다.
              </div>
            ) : (
              orderList.map((order) => (
                <div key={order.id} className="border border-gray-200 rounded-lg p-6 hover:shadow-md transition-shadow">
                  <div className="flex justify-between items-start mb-4">
                    <div>
                      <div className="text-sm text-gray-500 mb-1">결제번호: {order.orderNumber}</div>
                      <div className="text-sm text-gray-500">결제일: {order.date}</div>
                      {order.scheduleAt && (
                        <div className="text-sm text-gray-500">예약일: {order.scheduleAt}</div>
                      )}
                      {order.userName && (
                        <div className="text-sm text-gray-500">주문자: {order.userName}</div>
                      )}
                    </div>
                    <span className={`px-3 py-1 rounded-full text-sm font-medium ${getOrderStatusColor(order.status)}`}>
                      {order.status}
                    </span>
                  </div>
                  
                  <div className="mb-4">
                    <h4 className="font-medium text-pp text-lg mb-1">{order.productName}</h4>
                    <p className="text-gray-600">{order.instructor}</p>
                    {order.couponName && (
                      <p className="text-sm text-blue-600">쿠폰: {order.couponName}</p>
                    )}
                    {order.category && (
                      <p className="text-sm text-gray-500">카테고리: {order.category}</p>
                    )}
                  </div>
                  
                  {/* 옵션 표시 */}
                  {order.options && order.options.length > 0 && (
                    <div className="mb-4">
                      <div className="text-sm text-gray-600 mb-2">선택 옵션:</div>
                      <div className="flex flex-wrap gap-2">
                        {order.options.map((option, idx) => (
                          <span key={idx} className="px-2 py-1 bg-gray-100 text-gray-600 text-xs rounded">
                            {option.name || `옵션 ${idx + 1}`}
                          </span>
                        ))}
                      </div>
                    </div>
                  )}
                  
                  <div className="flex justify-between items-center">
                    <div>
                      {order.price > 0 ? (
                        <div className="text-lg font-bold text-pp">
                          {order.price.toLocaleString()}원
                        </div>
                      ) : (
                        <div className="text-lg font-bold text-gray-500">
                          금액 정보 없음
                        </div>
                      )}
                      {order.refundPrice > 0 && (
                        <div className="text-sm text-red-600">
                          환불금액: {order.refundPrice.toLocaleString()}원
                        </div>
                      )}
                    </div>
                    <div className="flex gap-2">
                      {(order.status === '완료' || order.status === '결제완료') && !order.reviewWritten && (
                        <button className="px-4 py-2 bg-purple-600 text-white rounded-lg text-sm font-medium hover:bg-purple-700">
                          리뷰 작성
                        </button>
                      )}
                      {order.reviewWritten && (
                        <span className="px-4 py-2 bg-gray-100 text-gray-600 rounded-lg text-sm">
                          리뷰 작성완료
                        </span>
                      )}
                      <button className="px-4 py-2 border border-gray-300 text-gray-700 rounded-lg text-sm font-medium hover:bg-gray-50">
                        상세보기
                      </button>
                    </div>
                  </div>
                </div>
              ))
            )}
          </div>
        )}

        {/* Reviews List */}
        {currentView === 'reviews' && (
          <div className="px-8 space-y-4">
            <div className="flex justify-between items-center mb-4">
              <h3 className="text-lg font-bold text-pp">내 리뷰</h3>
              {pagination.totalElement > 0 && (
                <div className="text-sm text-gray-500">
                  총 {pagination.totalElement}개의 리뷰
                </div>
              )}
            </div>
            
            {loading ? (
              <div className="text-center py-12 text-gray-500">
                리뷰를 불러오는 중...
              </div>
            ) : reviewList.length === 0 ? (
              <div className="text-center py-12 text-gray-500">
                작성한 리뷰가 없습니다.
              </div>
            ) : (
              reviewList.map((review, index) => (
                <div key={index} className="border border-gray-200 rounded-lg p-6 hover:shadow-md transition-shadow">
                  <div className="flex justify-between items-start mb-4">
                    <div className="flex-1">
                      <div className="flex items-center gap-3 mb-2">
                        <h4 className="font-medium text-pp text-lg">{review.productName}</h4>
                        <span className={`px-2 py-1 rounded-full text-xs font-medium ${getReviewStatusColor(review.status)}`}>
                          {getReviewStatusText(review.status)}
                        </span>
                      </div>
                      <p className="text-gray-600 mb-2">작성자: {review.nickname}</p>
                      <div className="flex items-center gap-2">
                        {renderStars(review.score)}
                        <span className="text-sm text-gray-500">({review.score}/5)</span>
                      </div>
                    </div>
                    <div className="text-sm text-gray-500">
                      {formatDate(review.createdAt)}
                    </div>
                  </div>
                  
                  <div className="mb-4">
                    <p className="text-gray-700 leading-relaxed">{review.content}</p>
                  </div>
                  
                  {/* 리뷰 이미지 표시 */}
                  {review.imageUrlList && review.imageUrlList.length > 0 && (
                    <div className="mb-4">
                      <div className="flex gap-2 flex-wrap">
                        {review.imageUrlList.map((imageUrl, imgIndex) => (
                          <img 
                            key={imgIndex}
                            src={imageUrl} 
                            alt={`리뷰 이미지 ${imgIndex + 1}`}
                            className="w-20 h-20 object-cover rounded-lg border"
                            onError={(e) => {
                              e.target.style.display = 'none';
                            }}
                          />
                        ))}
                      </div>
                    </div>
                  )}
                  
                  <div className="flex justify-between items-center text-sm text-gray-500">
                    <div className="flex gap-4">
                      <span>상품 ID: {review.productId}</span>
                      <span>업체 ID: {review.companyId}</span>
                    </div>
                    {review.status === 'REGISTERED' && (
                      <div className="flex gap-2">
                        <button className="px-3 py-1 border border-gray-300 text-gray-700 rounded text-sm hover:bg-gray-50">
                          수정
                        </button>
                        <button className="px-3 py-1 border border-gray-300 text-gray-700 rounded text-sm hover:bg-gray-50">
                          삭제
                        </button>
                      </div>
                    )}
                  </div>
                </div>
              ))
            )}
            
            {/* 페이지네이션 정보 */}
            {pagination.totalPage > 1 && (
              <div className="flex justify-center mt-6">
                <div className="text-sm text-gray-500">
                  페이지 {pagination.curPage + 1} / {pagination.totalPage}
                </div>
              </div>
            )}
          </div>
        )}

        {/* Action Buttons */}
        <div className="px-8 mt-8 mb-8">
          <div className="flex gap-3">
            <button className="flex-1 bg-purple-200 text-gray-700 py-3 px-6 rounded-lg font-medium" onClick={handleMainPage}>
              메인 페이지
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default PaymentAndReview;