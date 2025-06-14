import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import apiClient from "../../../../lib/apiClient";

const baseUrl = import.meta.env.VITE_API_BASE_URL;

const MyPage = () => {
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
    image: "",
    social: ""
  });
  const [activeTab, setActiveTab] = useState('내 정보');
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

  const getUserProfile = async () => {
    try {
      const res = await apiClient.get(`${baseUrl}/user/profile`, {
        headers: { "Content-Type": "application/json" },
      }).then((res) => {
        console.log("User Profile Data : ", res.data.data);
        if(res.status === 200)  {
            setUserInfo(res.data.data);
        }
      });
      if(res.status === 200)    {
        setUserInfo(res.data.data);
      }
    } catch (err) {
      console.error("User 정보 로딩 실패 :", err);
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
  }

  // 내 정보 수정 페이지
  const handleChangeProfile = () => {
    if (userInfo.tier === 'AMATEUR' || userInfo.social !== 'L') {
      navigate("/user/update");
    } else {
      navigate("/user/verify");
    }
  }

  // 등급 표시 함수
  const getTierDisplay = (tier) => {
    switch(tier) {
      case 'SEMI_PRO': return '세미프로';
      case 'PRO': return '프로';
      case 'AMATEUR': return '아마추어';
      default: return tier;
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

  // 휴대전화 인증 상태
  const getPhoneStatus = (phone) => {
    return phone && phone !== "" ? phone : "미인증";
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

        {/* Profile and Settings */}
        <div className='flex justify-between gap-8'>
            <div className="px-6 mt-20 relative z-10">
                {/* Profile Image */}
                <div className="mb-6">
                    <div className="text-pp font-bold text-center mb-2">프로필 이미지</div>
                    <div className="w-56 h-56 bg-gray-200 rounded-lg overflow-hidden flex items-center justify-center">
                        <img 
                            src={`${baseUrl}${userInfo.image || "/images/default.png"}`}
                            alt="프로필 이미지" 
                            className="w-full h-full object-cover justify-center"
                        />
                    </div>
                </div>
            </div>

            {/* Profile Information List */}
            <div className="flex-1 px-6 space-y-4">
                <div className="flex justify-between items-center py-3 border-b border-gray-100">
                    <span className="text-pp">이름</span>
                    <span className="text-pp">{userInfo.name || '-'}</span>
                </div>
                
                <div className="flex justify-between items-center py-3 border-b border-gray-100">
                    <span className="text-pp">닉네임</span>
                    <span className="text-pp">{userInfo.nickname || '-'}</span>
                </div>
                
                <div className="flex justify-between items-center py-3 border-b border-gray-100">
                    <span className="text-pp">아이디</span>
                    <span className="text-pp">{userInfo.email || '-'}</span>
                </div>
                
                <div className="flex justify-between items-center py-3 border-b border-gray-100">
                    <span className="text-pp">휴대전화</span>
                    <span className="text-pp">{getPhoneStatus(userInfo.phone)}</span>
                </div>
                
                <div className="flex justify-between items-center py-3 border-b border-gray-100">
                    <span className="text-pp">등급</span>
                    <span className="text-pp">{getTierDisplay(userInfo.tier)}</span>
                </div>
                
                <div className="flex justify-between items-center py-3 border-b border-gray-100">
                    <span className="text-pp">알림 수신</span>
                    <span className="text-pp">{userInfo.notification === 'Y' ? '동의' : '비동의'}</span>
                </div>
                
                <div className="flex justify-between items-center py-3 border-b border-gray-100">
                    <span className="text-pp">성별</span>
                    <span className="text-pp">{userInfo.gender === 'M' ? '남성' : userInfo.gender === 'F' ? '여성' : '-'}</span>
                </div>
                
                <div className="flex justify-between items-center py-3 border-b border-gray-100">
                    <span className="text-pp">Lucid Point</span>
                    <span className="text-pp">{userInfo.mileage?.toLocaleString() || '0'} Point</span>
                </div>
            </div>
        </div>

        {/* Action Buttons */}
        <div className="px-6 mt-8 mb-8">
          <div className="flex gap-3">
            <button className="flex-1 bg-purple-600 text-white py-3 px-6 rounded-lg font-medium" onClick={handleChangeProfile}>
              내 정보 수정
            </button>
            <button className="flex-1 bg-purple-200 text-gray-700 py-3 px-6 rounded-lg font-medium" onClick={handleMainPage}>
              메인 페이지
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default MyPage;