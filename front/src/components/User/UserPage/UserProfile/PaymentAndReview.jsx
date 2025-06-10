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
  const navigate = useNavigate();

  const tabs = [
    { name: '내 정보', path: '/user' },
    { name: '내 쿠폰', path: '/user/coupons' },
    { name: '찜 & 구독', path: '/user/wishlist' },
    { name: '주문내역 & 리뷰', path: '/user/review' }
  ];

  // 샘플 주문 데이터
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

  // 샘플 리뷰 데이터
  const sampleReviews = [
    {
      id: 1,
      orderNumber: "ORD20241201001",
      productName: "프리미엄 골프 레슨",
      instructor: "김코치",
      rating: 5,
      comment: "정말 친절하고 자세하게 가르쳐주셨습니다. 스윙 폼이 많이 개선되었어요!",
      date: "2024-12-02",
      images: []
    },
    {
      id: 2,
      orderNumber: "ORD20241115004",
      productName: "골프 퍼팅 레슨",
      instructor: "최프로",
      rating: 4,
      comment: "퍼팅 실력이 눈에 띄게 향상되었습니다. 추천해요!",
      date: "2024-11-16",
      images: []
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
        setUserInfo(res.data.data);
      }
    } catch (err) {
      console.error("User 정보 로딩 실패 :", err);
      // 임시 데이터 설정
      setUserInfo({
        name: "신현준",
        currency: "KRW",
        language: "KOR"
      });
    }
  };

  const getOrderList = async () => {
    try {
      // API 호출 대신 샘플 데이터 사용
      setOrderList(sampleOrders);
    } catch (err) {
      console.error("주문내역 로딩 실패 :", err);
      setOrderList(sampleOrders);
    }
  };

  const getReviewList = async () => {
    try {
      // API 호출 대신 샘플 데이터 사용
      setReviewList(sampleReviews);
    } catch (err) {
      console.error("리뷰 로딩 실패 :", err);
      setReviewList(sampleReviews);
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

  // 주문 상태 표시 함수
  const getOrderStatusColor = (status) => {
    switch(status) {
      case '완료': return 'text-green-600 bg-green-100';
      case '진행중': return 'text-blue-600 bg-blue-100';
      case '취소': return 'text-red-600 bg-red-100';
      default: return 'text-gray-600 bg-gray-100';
    }
  };

  // 언어 표시 함수
  const getLanguageDisplay = (language) => {
    switch(language) {
      case 'KOR': return '한국어';
      case 'ENG': return 'English';
      default: return language;
    }
  };

  // 통화 표시 함수
  const getCurrencyDisplay = (currency) => {
    switch(currency) {
      case 'KRW': return 'KRW';
      case 'USD': return 'USD';
      default: return currency;
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
        <div className="relative p-8 rounded-b-3xl">
          <div className="flex items-center justify-between">
            <div className="flex-1">
              <h2 className="text-pp text-sm mb-1">안녕하세요</h2>
              <h1 className="text-xl font-bold text-pp">{userInfo.name || '신현준'} 고객님</h1>
            </div>
            <div className="flex items-center justify-between mb-6 mt-6">
              <div className="flex gap-6 text-sm">
                <div className="text-center">
                  <div className="text-pp">설정화폐</div>
                  <div className="text-pp font-medium">{getCurrencyDisplay(userInfo.currency)}</div>
                </div>
                <div className="text-center">
                  <div className="text-pp">설정언어</div>
                  <div className="text-pp font-medium text-purple-600">{getLanguageDisplay(userInfo.language)}</div>
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
              주문내역
            </button>
            <button 
              onClick={() => setCurrentView('reviews')}
              className={`px-6 py-3 rounded-lg font-medium transition-colors ${
                currentView === 'reviews' 
                  ? 'bg-purple-600 text-white' 
                  : 'bg-gray-100 text-gray-700 hover:bg-gray-200'
              }`}
            >
              내 리뷰
            </button>
          </div>
        </div>

        {/* Orders List */}
        {currentView === 'orders' && (
          <div className="px-8 space-y-4">
            <h3 className="text-lg font-bold text-pp mb-4">주문내역</h3>
            {orderList.length === 0 ? (
              <div className="text-center py-12 text-gray-500">
                주문내역이 없습니다.
              </div>
            ) : (
              orderList.map((order) => (
                <div key={order.id} className="border border-gray-200 rounded-lg p-6 hover:shadow-md transition-shadow">
                  <div className="flex justify-between items-start mb-4">
                    <div>
                      <div className="text-sm text-gray-500 mb-1">주문번호: {order.orderNumber}</div>
                      <div className="text-sm text-gray-500">주문일: {order.date}</div>
                    </div>
                    <span className={`px-3 py-1 rounded-full text-sm font-medium ${getOrderStatusColor(order.status)}`}>
                      {order.status}
                    </span>
                  </div>
                  
                  <div className="mb-4">
                    <h4 className="font-medium text-pp text-lg mb-1">{order.productName}</h4>
                    <p className="text-gray-600">강사: {order.instructor}</p>
                  </div>
                  
                  <div className="flex justify-between items-center">
                    <div className="text-lg font-bold text-pp">
                      {order.price.toLocaleString()}원
                    </div>
                    <div className="flex gap-2">
                      {order.status === '완료' && !order.reviewWritten && (
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
            <h3 className="text-lg font-bold text-pp mb-4">내 리뷰</h3>
            {reviewList.length === 0 ? (
              <div className="text-center py-12 text-gray-500">
                작성한 리뷰가 없습니다.
              </div>
            ) : (
              reviewList.map((review) => (
                <div key={review.id} className="border border-gray-200 rounded-lg p-6 hover:shadow-md transition-shadow">
                  <div className="flex justify-between items-start mb-4">
                    <div>
                      <h4 className="font-medium text-pp text-lg mb-1">{review.productName}</h4>
                      <p className="text-gray-600 mb-2">강사: {review.instructor}</p>
                      <div className="flex items-center gap-2">
                        {renderStars(review.rating)}
                        <span className="text-sm text-gray-500">({review.rating}/5)</span>
                      </div>
                    </div>
                    <div className="text-sm text-gray-500">
                      {review.date}
                    </div>
                  </div>
                  
                  <div className="mb-4">
                    <p className="text-gray-700 leading-relaxed">{review.comment}</p>
                  </div>
                  
                  <div className="flex justify-between items-center text-sm text-gray-500">
                    <span>주문번호: {review.orderNumber}</span>
                    <div className="flex gap-2">
                      <button className="px-3 py-1 border border-gray-300 text-gray-700 rounded text-sm hover:bg-gray-50">
                        수정
                      </button>
                      <button className="px-3 py-1 border border-gray-300 text-gray-700 rounded text-sm hover:bg-gray-50">
                        삭제
                      </button>
                    </div>
                  </div>
                </div>
              ))
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