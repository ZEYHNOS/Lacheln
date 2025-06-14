import { useState, useEffect } from 'react';
import { Heart, Store, Trash2, Eye } from 'lucide-react';
import apiClient from '../../../../lib/apiClient';
import { Navigate, useNavigate } from 'react-router-dom';

const baseUrl = import.meta.env.VITE_API_BASE_URL;

const WishlistSubscribePage = () => {
  const [userInfo, setUserInfo] = useState({ 
    userId: "", 
    name: "", 
    nickname: "", 
    email: "", 
    phone: "", 
    tier: "", 
    mileage: 0,
    currency: "",
    gender: "",
    language: "",
    notification: "",
    profileImageUrl: ""
  });
  const [wishlistProducts, setWishlistProducts] = useState([]);
  const [subscribeList, setSubscribeList] = useState([]);
  const [loading, setLoading] = useState(false);
  const [activeTab, setActiveTab] = useState('찜 & 구독');
  const [currentView, setCurrentView] = useState('wishlist'); // 기본값을 'wishlist'로 변경
  const navigate = useNavigate();

  const tabs = [
    { name: '내 정보', path: '/user' },
    { name: '내 쿠폰', path: '/user/coupons' },
    { name: '찜 & 구독', path: '/user/wishsub' },
    { name: '주문내역 & 리뷰', path: '/user/review' }
  ];

  useEffect(() => {
    getUserProfile();
  }, []);

  useEffect(() => {
    if (userInfo.userId) {
      getWishlistData();
      getSubscribeData();
    }
  }, [userInfo.userId]);

  const getUserProfile = async () => {
    try {
      const res = await apiClient.get(`${baseUrl}/user/profile`);
      if (res.status === 200) {
        setUserInfo(res.data.data);
      }
    } catch (err) {
      console.error("User 정보 로딩 실패 :", err);
    }
  };

  const getWishlistData = async () => {
    setLoading(true);
    try {
      // First get wishlist IDs
      const wishlistRes = await apiClient.get(`${baseUrl}/user/wishlist/search`);
      console.log("wishListRes : ", wishlistRes.data);
      
      if (wishlistRes.status === 200 && wishlistRes.data?.data) {
        const wishlistIds = wishlistRes.data.data; // ID 배열
        console.log(wishlistIds);
        
        // 각 상품 ID로 상품 상세 정보 가져오기
        const productDetailsPromises = wishlistIds.map(async (wishlist) => {
          try {
            const productDetailRes = await apiClient.get(`${baseUrl}/product/${wishlist.pdId}`);
            // 원본 wishlist 정보와 상품 상세 정보를 조합
            return {
              ...productDetailRes.data,
              wishListId: wishlist.wishListId, // wishListId 추가
              // 혹은 data 구조에 따라 다음과 같이 할 수도 있습니다:
              // data: {
              //   ...productDetailRes.data.data,
              //   wishListId: wishlist.wishListId
              // }
            };
          } catch (err) {
            console.error(`상품 ID ${wishlist.pdId} 상세 정보 로딩 실패:`, err);
            return null;
          }
        });

        const productDetails = await Promise.all(productDetailsPromises);
        const validProducts = productDetails.filter(product => product !== null);

        console.log("final wishList : ", validProducts);
        setWishlistProducts(validProducts);
      }
    } catch (err) {
      console.error("찜 목록 로딩 실패 :", err);
    } finally {
      setLoading(false);
    }
  };

  const getSubscribeData = async () => {
    setLoading(true);
    try {
      const res = await apiClient.get(`${baseUrl}/subscribe/search`);
      
      if (res.status === 200 && res.data?.data) {
        const subscribeIds = res.data.data; // ID 배열
        console.log("SubscribeIds : ", subscribeIds);
        
        // 각 업체 ID로 업체 상세 정보 가져오기
        const companyDetailsPromises = subscribeIds.map(async (subscribe) => {
          try {
            const companyDetailRes = await apiClient.get(`${baseUrl}/company/info/${subscribe.cpIds}`);
            // 원본 subscribe 정보와 업체 상세 정보를 조합
            return {
              ...companyDetailRes.data,
              subscribeId: subscribe.subscribeId, // subscribeId 추가
              // 혹은 data 구조에 따라 다음과 같이 할 수도 있습니다:
              // data: {
              //   ...companyDetailRes.data.data,
              //   subscribeId: subscribe.subscribeId
              // }
            };
          } catch (err) {
            console.error(`업체 ID ${subscribe.cpIds} 상세 정보 로딩 실패:`, err);
            return null;
          }
        });

        const companyDetails = await Promise.all(companyDetailsPromises);
        const validCompanies = companyDetails.filter(company => company !== null);
        console.log("final subscribe Data : ", validCompanies);
        setSubscribeList(validCompanies);
      }
    } catch (err) {
      console.error("구독 목록 로딩 실패 :", err);
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

  const formatPrice = (price) => {
    if (!price) return '가격 정보 없음';
    return new Intl.NumberFormat('ko-KR').format(price) + '원';
  };

  const getProductStatusText = (status) => {
    const statusMap = {
      'ACTIVE': '판매중',
      'INACTIVE': '판매중지',
      'OUT_OF_STOCK': '품절',
      'DISCONTINUED': '단종'
    };
    return statusMap[status] || status;
  };

  const getCompanyCategoryText = (category) => {
    const categoryMap = {
      'RESTAURANT': '음식점',
      'RETAIL': '소매업',
      'SERVICE': '서비스업',
      'MANUFACTURING': '제조업',
      'TECHNOLOGY': '기술업'
    };
    return categoryMap[category] || category;
  };

  const handleTabClick = (tab) => {
    setActiveTab(tab.name);
    navigate(tab.path);
  };

  const handleRemoveWishlist = async (wishListId) => {
    console.log("Delete Request ProductId : ", wishListId);
    try {
      const res = await apiClient.delete(`${baseUrl}/user/wishlist/delete/${wishListId}`);
      if (res.status === 200) {
        setWishlistProducts(prev => prev.filter(item => item.wishListId !== wishListId));
        alert('찜 목록에서 제거되었습니다.');
      }
    } catch (err) {
      console.error("찜 목록 제거 실패:", err);
      alert('찜 목록 제거에 실패했습니다.');
    }
  };

  const handleUnsubscribe = async (subscribeId) => {
    console.log("Delete Request Subscribe : ", subscribeId);
    try {
      const res = await apiClient.delete(`${baseUrl}/subscribe/delete/${subscribeId}`);
      if (res.status === 200) {
        setSubscribeList(prev => prev.filter(item => item.subscribeId !== subscribeId));
        alert('구독이 취소되었습니다.');
      }
    } catch (err) {
      console.error("구독 취소 실패:", err);
      alert('구독 취소에 실패했습니다.');
    }
  };

  const handleProductView = (productId) => {
    alert(`상품 ID ${productId} 상세 페이지로 이동`);
  };

  const handleCompanyView = (companyId) => {
    alert(`업체 ID ${companyId} 상세 페이지로 이동`);
  };

  return (
    <div className="bg-white min-h-screen">
      <div className="w-[880px] mx-auto">
        <nav className="flex px-8 pt-6 border-b border-gray-200">
          {tabs.map(tab => (
            <button
              key={tab.name}
              onClick={() => handleTabClick(tab)}
              className={`flex-1 text-center py-4 px-4 font-medium border-b-2 
                ${activeTab === tab.name
                    ? 'border-purple-600 text-white bg-purple-600'
                    : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
                }`}
            >
              {tab.name}
            </button>
          ))}
        </nav>
        
        <div className='flex justify-between'>
          <div className="pl-8 pt-6 pb-8 rounded-b-3xl">
            <h2 className="text-sm text-purple-600">안녕하세요</h2>
            <h1 className="text-xl font-bold text-purple-600">{userInfo.name || userInfo.nickname || '고객'} 님</h1>
            <div className="mt-2 text-sm text-gray-600">
              <span className="bg-purple-100 text-purple-600 px-2 py-1 rounded-full text-xs font-medium mr-2">
                {userInfo.tier === 'SEMI_PRO' ? '세미프로' : userInfo.tier}
              </span>
              <span className="text-purple-600 font-medium">
                마일리지: {userInfo.mileage?.toLocaleString()} {userInfo.currency}
              </span>
            </div>
          </div>
          <div className="flex gap-6 mt-6 text-sm mr-10">
            <div className="text-center text-purple-600">
              <div>찜한 상품</div>
              <div className="text-xl font-bold text-purple-600">{wishlistProducts.length}</div>
            </div>
            <div className="text-center text-purple-600">
              <div>구독 업체</div>
              <div className="text-xl font-bold text-purple-600">{subscribeList.length}</div>
            </div>
          </div>
        </div>

        <div className="px-8 mb-6">
          <div className="flex gap-4 mb-4">
            <button
              onClick={() => setCurrentView('wishlist')}
              className={`px-6 py-2 rounded-lg font-medium transition-colors ${
                currentView === 'wishlist'
                  ? 'bg-purple-600 text-white'
                  : 'bg-gray-100 text-gray-600 hover:bg-gray-200'
              }`}
            >
              <Heart className="inline w-4 h-4 mr-2" />
              찜 목록
            </button>
            <button
              onClick={() => setCurrentView('subscribe')}
              className={`px-6 py-2 rounded-lg font-medium transition-colors ${
                currentView === 'subscribe'
                  ? 'bg-purple-600 text-white'
                  : 'bg-gray-100 text-gray-600 hover:bg-gray-200'
              }`}
            >
              <Store className="inline w-4 h-4 mr-2" />
              구독 목록
            </button>
          </div>
        </div>

        <div className="px-8 space-y-4">
          {loading ? (
            <div className="py-12 text-center text-gray-500">데이터를 불러오는 중...</div>
          ) : (
            <>
              {/* 찜 목록 */}
              {currentView === 'wishlist' && (
                <>
                  {wishlistProducts.length === 0 ? (
                    <div className="py-12 text-center text-gray-500">찜한 상품이 없습니다.</div>
                  ) : (
                    <>
                      <h3 className="text-lg font-semibold text-purple-600 mb-4">찜한 상품</h3>
                      {wishlistProducts.map((product, index) => (
                        <div key={index} className="border-2 border-purple-200 p-6 rounded-lg hover:shadow-lg transition-all bg-white">
                          <div className="flex gap-4">
                            <div className="w-24 h-24 rounded-lg overflow-hidden flex-shrink-0">
                              <img 
                                src={`${baseUrl}${product.data.imageUrl}`} 
                                alt={product.data.productName}
                                className="w-full h-full object-cover"
                              />
                            </div>
                            <div className="flex-1">
                              <div className="flex justify-between items-start mb-2">
                                <div>
                                  <h4 className="font-bold text-lg text-purple-600 mb-1">{product.data.productName}</h4>
                                  <p className="text-gray-600 text-sm mb-1">판매업체: {product.data.companyName}</p>
                                  <span className={`px-2 py-1 rounded-full text-xs ${
                                    product.data.status === 'ACTIVE' 
                                      ? 'bg-green-100 text-green-600' 
                                      : 'bg-red-100 text-red-600'
                                  }`}>
                                    {getProductStatusText(product.data.status)}
                                  </span>
                                </div>
                                <div className="text-right">
                                  <div className="text-xl font-bold text-purple-600 mb-2">
                                    {formatPrice(product.data.price)}
                                  </div>
                                </div>
                              </div>
                              <div className="flex gap-2 mt-4">
                                <button
                                  onClick={() => handleProductView(product.data.productId)}
                                  className="flex items-center gap-1 px-3 py-1 bg-purple-600 text-white rounded text-sm hover:bg-purple-700 transition-colors"
                                >
                                  <Eye className="w-3 h-3" />
                                  상품 보기
                                </button>
                                <button
                                  onClick={() => handleRemoveWishlist(product.wishListId)}
                                  className="flex items-center gap-1 px-3 py-1 bg-red-500 text-white rounded text-sm hover:bg-red-600 transition-colors"
                                >
                                  <Trash2 className="w-3 h-3" />
                                  찜 해제
                                </button>
                              </div>
                            </div>
                          </div>
                        </div>
                      ))}
                    </>
                  )}
                </>
              )}

              {/* 구독 목록 */}
              {currentView === 'subscribe' && (
                <>
                  {subscribeList.length === 0 ? (
                    <div className="py-12 text-center text-gray-500">구독한 업체가 없습니다.</div>
                  ) : (
                    <>
                      <h3 className="text-lg font-semibold text-purple-600 mb-4">구독한 업체</h3>
                      {subscribeList.map((company, index) => (
                        <div key={index} className="border-2 border-purple-200 p-6 rounded-lg hover:shadow-lg transition-all bg-white">
                          <div className="flex gap-4">
                            <div className="w-24 h-24 rounded-lg overflow-hidden flex-shrink-0">
                              <img 
                                src={`${baseUrl}${company.data.imageUrl}`} 
                                alt={company.data.name}
                                className="w-full h-full object-cover"
                              />
                            </div>
                            <div className="flex-1">
                              <div className="flex justify-between items-start mb-4">
                                <div className="flex-1">
                                  <div className="flex items-center gap-3 mb-2">
                                    <h4 className="font-bold text-lg text-purple-600">{company.data.name}</h4>
                                    <span className="px-3 py-1 bg-purple-100 text-purple-600 rounded-full text-sm font-medium">
                                      {getCompanyCategoryText(company.data.category)}
                                    </span>
                                  </div>
                                  <p className="text-gray-600 mb-1">주소: {company.data.address}</p>
                                  <p className="text-gray-600 mb-1">전화번호: {company.data.contact}</p>
                                  <p className="text-gray-600 mb-1">이메일: {company.data.email}</p>
                                  {company.data.mos && (
                                    <p className="text-gray-600 mb-2">업종: {company.data.mos}</p>
                                  )}
                                </div>
                              </div>
                              <div className="flex gap-2">
                                <button
                                  onClick={() => handleCompanyView(company.data.id)}
                                  className="flex items-center gap-1 px-3 py-1 bg-purple-600 text-white rounded text-sm hover:bg-purple-700 transition-colors"
                                >
                                  <Store className="w-3 h-3" />
                                  업체 보기
                                </button>
                                <button
                                  onClick={() => handleUnsubscribe(company.subscribeId)}
                                  className="flex items-center gap-1 px-3 py-1 bg-red-500 text-white rounded text-sm hover:bg-red-600 transition-colors"
                                >
                                  <Trash2 className="w-3 h-3" />
                                  구독 취소
                                </button>
                              </div>
                            </div>
                          </div>
                        </div>
                      ))}
                    </>
                  )}
                </>
              )}
            </>
          )}
        </div>

        <div className="px-8 mt-8 mb-8">
          <button 
            className="w-full py-3 bg-purple-600 text-white rounded-lg font-medium hover:bg-purple-700 transition-colors" 
            onClick={() => alert('메인 페이지로 이동')}
          >
            메인 페이지
          </button>
        </div>
      </div>
    </div>
  );
};

export default WishlistSubscribePage;