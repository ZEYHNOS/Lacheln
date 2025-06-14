import { useState, useEffect, useRef } from 'react';
import { useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';
import apiClient from '../../../../lib/apiClient';

const baseUrl = import.meta.env.VITE_API_BASE_URL;

// 국가 및 언어 설정
const COUNTRY_OPTIONS = [
  { value: 'KOR', label: '대한민국', image: 'kor.jpg' },
  { value: 'ENG', label: 'UnitedState America', image: 'usa.jpg' },
  { value: 'JPN', label: '日本', image: 'japan.jpg' },
  { value: 'ZHO', label: '中国', image: 'china.jpg' }
];

// 화폐 설정
const CURRENCY_OPTIONS = [
  { value: 'KRW', label: 'KRW (원화)', country: 'KR' },
  { value: 'USD', label: 'USD (달러)', country: 'US' },
  { value: 'JPY', label: 'JPY (엔화)', country: 'JP' },
  { value: 'CNY', label: 'CNY (위안화)', country: 'CN' }
];

const MyPageEdit = () => {
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

  const [editForm, setEditForm] = useState({
    name: "",
    nickname: "",
    phone: "",
    image: "",
    password: "",
    confirmPassword: "",
    language: "",
    currency: "",
    adsNotification: "",
    gender: ""
  });

  const [isLoading, setIsLoading] = useState(false);
  const [errors, setErrors] = useState({});
  const [profileImageFile, setProfileImageFile] = useState(null);
  const [showEmailModal, setShowEmailModal] = useState(false);
  const [showPhoneModal, setShowPhoneModal] = useState(false);
  const [emailAuthCode, setEmailAuthCode] = useState('');
  const [phoneNumber, setPhoneNumber] = useState('');
  const [phoneAuthCode, setPhoneAuthCode] = useState('');
  const [emailTimer, setEmailTimer] = useState(0);
  const [phoneTimer, setPhoneTimer] = useState(0);
  const navigate = useNavigate();
  const emailTimerRef = useRef(null);
  const phoneTimerRef = useRef(null);
  const [showLanguageModal, setShowLanguageModal] = useState(false);
  const [showCurrencyModal, setShowCurrencyModal] = useState(false);
  const [selectedCountry, setSelectedCountry] = useState('');
  const [selectedCurrency, setSelectedCurrency] = useState('');
  const [isPhoneVerified, setIsPhoneVerified] = useState(false);

  useEffect(() => {
    getUserProfile();
  }, []);

  useEffect(() => {
    if (userInfo.userId) {
      setEditForm({
        name: userInfo.name || "",
        nickname: userInfo.nickname || "",
        phone: userInfo.phone || "",
        image: userInfo.image || "",
        password: "",
        confirmPassword: "",
        language: userInfo.language || "",
        currency: userInfo.currency || "",
        adsNotification: userInfo.notification || "",
        social: userInfo.social || "",
        gender: userInfo.gender || ""
      });
    }
  }, [userInfo]);

  useEffect(() => {
    if (userInfo) {
      setSelectedCountry(userInfo.language || 'KR');
      setSelectedCurrency(userInfo.currency || 'KRW');
    }
  }, [userInfo]);

  // 이메일 인증 타이머
  useEffect(() => {
    if (emailTimer > 0) {
      emailTimerRef.current = setTimeout(() => setEmailTimer(emailTimer - 1), 1000);
    }
    return () => clearTimeout(emailTimerRef.current);
  }, [emailTimer]);

  // 전화번호 인증 타이머
  useEffect(() => {
    if (phoneTimer > 0) {
      phoneTimerRef.current = setTimeout(() => setPhoneTimer(phoneTimer - 1), 1000);
    }
    return () => clearTimeout(phoneTimerRef.current);
  }, [phoneTimer]);

  useEffect(() => {
    console.log("editForm.image : ", editForm.image);
  }, [editForm.image]);

  useEffect(() => {
    console.log("editForm.phone : ", editForm.phone);
  }, [editForm.phone]);

  const getUserProfile = async () => {
    try {
      const res = await apiClient.get(`${baseUrl}/user/profile`, {
        headers: { "Content-Type": "application/json" },
      });
      
      if(res.status === 200) {
        console.log("User Profile Data : ", res.data.data);
        setUserInfo(res.data.data);
      }
    } catch (err) {
      console.error("User 정보 로딩 실패 :", err);
    }
  };

  const formatPhoneNumber = (value) => {
    // 숫자만 추출
    const numbers = value.replace(/[^\d]/g, '');
    
    // 정규식에 맞게 하이픈 추가
    if (numbers.length <= 3) {
      return numbers;
    } else if (numbers.length <= 7) {
      return `${numbers.slice(0, 3)}-${numbers.slice(3)}`;
    } else {
      return `${numbers.slice(0, 3)}-${numbers.slice(3, 7)}-${numbers.slice(7, 11)}`;
    }
  };

  const removeHyphens = (value) => {
    return value.replace(/-/g, '');
  };

  const handlePhoneNumberChange = (e) => {
    const formattedNumber = formatPhoneNumber(e.target.value);
    setPhoneNumber(formattedNumber);
  };

  const handleImageChange = async (e) => {
    const file = e.target.files[0];
    if (file) {
      try {
        const formData = new FormData();
        formData.append('image', file);

        const res = await apiClient.post(
          `${baseUrl}/user/image`,
          formData,
          {
            headers: {
              'Content-Type': 'multipart/form-data',
            },
          }
        );

        console.log("image data path : ", res.data.description);

        if (res.status === 200) {
          const imageUrl = res.data.description;
          console.log("imageUrl : ", imageUrl);
          setEditForm(prev => ({
            ...prev,
            image: imageUrl
          }));
          toast.success('프로필 이미지가 업로드되었습니다.');
        }
      } catch (err) {
        console.error("이미지 업로드 실패:", err);
        toast.error('이미지 업로드 중 오류가 발생했습니다.');
      }
    }
  };

  const handleInputChange = (field, value) => {
    setEditForm(prev => ({
      ...prev,
      [field]: value
    }));
    
    // 에러 메시지 초기화
    if (errors[field]) {
      setErrors(prev => ({
        ...prev,
        [field]: ""
      }));
    }
  };

  const validateForm = () => {
    const newErrors = {};

    if (!editForm.name.trim()) {
      newErrors.name = "이름을 입력해주세요.";
    }

    if (!editForm.nickname.trim()) {
      newErrors.nickname = "닉네임을 입력해주세요.";
    }

    // 비밀번호 변경 시에만 검증
    if (editForm.password || editForm.confirmPassword) {
      if (!editForm.password) {
        newErrors.password = "새 비밀번호를 입력해주세요.";
      } else if (editForm.password.length < 6) {
        newErrors.password = "비밀번호는 6자 이상이어야 합니다.";
      }

      if (!editForm.confirmPassword) {
        newErrors.confirmPassword = "비밀번호 확인을 입력해주세요.";
      } else if (editForm.password !== editForm.confirmPassword) {
        newErrors.confirmPassword = "비밀번호가 일치하지 않습니다.";
      }
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    try {
      const updateData = {
        name: editForm.name,
        password: editForm.confirmPassword ? editForm.confirmPassword : "NULL",
        nickname: editForm.nickname,
        phone: isPhoneVerified ? editForm.phone : userInfo.phone,
        language: editForm.language,
        currency: editForm.currency,
        notification: editForm.adsNotification,
        image: userInfo.image,
        gender: editForm.gender
      };

      if (editForm.image !== userInfo.image) {
        updateData.image = editForm.image;
      }

      console.log("Before Request updateData : ", updateData);

      const res = await apiClient.put('/user/update', updateData);

      console.log("After Request updateData : ", res.data.data);
      if (res.data.result.resultCode === 200) {
        toast.success(res.data.description);
        navigate('/user');
      } else {
        toast.error(res.data.description);
      }
    } catch (err) {
      console.error("프로필 수정 실패:", err);
      toast.error("프로필 수정 중 오류가 발생했습니다.");
    }
  };

  const handleCancel = () => {
    if (confirm('수정을 취소하시겠습니까? 변경사항이 저장되지 않습니다.')) {
      navigate('/user');
    }
  };

  // 등급 표시 함수
  const getTierDisplay = (tier) => {
    switch(tier) {
      case 'SEMI_PRO': return '세미프로';
      case 'PROFESSIONAL': return '프로페셔널';
      case 'AMATEUR': return '아마추어';
      case 'WORLD_CLASS': return '월드클래스';
      case 'CHALLANGER': return '챌린저';
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

  const formatTime = (seconds) => {
    const min = String(Math.floor(seconds / 60)).padStart(2, '0');
    const sec = String(seconds % 60).padStart(2, '0');
    return `${min}:${sec}`;
  };

  const handleSendPhoneCode = async () => {
    if (!phoneNumber) {
      toast.error('전화번호를 입력해주세요.');
      return;
    }

    try {
      const phoneWithoutHyphens = removeHyphens(phoneNumber);
      const res = await fetch(`${baseUrl}/sms/send`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ phoneNum: phoneWithoutHyphens }),
      });
      const data = await res.json();
      if (data.result.resultCode === 200) {
        toast.success(data.description);
        setPhoneTimer(300);
      } else {
        toast.error(data.description);
      }
    } catch (err) {
      console.error("전화번호 인증코드 전송 실패:", err);
      toast.error("전화번호 인증코드 전송 중 오류가 발생했습니다.");
    }
  };

  const handleVerifyPhoneCode = async () => {
    if (!phoneNumber) {
      toast.error('전화번호를 입력해주세요.');
      return;
    }

    try {
      const phoneWithoutHyphens = removeHyphens(phoneNumber);
      const res = await fetch(`${baseUrl}/sms/verify`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ phoneNum: phoneWithoutHyphens, code: phoneAuthCode }),
      });
      const data = await res.json();
      console.log("verify phone data : ", data);
      if (data.data.verified === true) {
        toast.success(data.description);
        setIsPhoneVerified(true);
        setUserInfo(prev => ({
          ...prev,
          phone: phoneNumber
        }));
        setEditForm(prev => ({
          ...prev,
          phone: phoneNumber
        }));
        setShowPhoneModal(false);
      } else {
        toast.error(data.description);
      }
    } catch (err) {
      console.error("전화번호 인증 실패:", err);
      toast.error("전화번호 인증 중 오류가 발생했습니다.");
    }
  };

  const handleLanguageSelect = (country) => {
    setEditForm(prev => ({
      ...prev,
      language: country
    }));
    setShowLanguageModal(false);
  };

  const handleCurrencySelect = (currency) => {
    setEditForm(prev => ({
      ...prev,
      currency: currency
    }));
    setShowCurrencyModal(false);
  };

  const getGenderDisplay = (gender) => {
    switch(gender) {
      case 'M': return '남성';
      case 'F': return '여성';
      case 'U': return '미설정';
      default: return '미설정';
    }
  };

  return (
    <div className="bg-white min-h-screen">
      <div className="w-[880px] mx-auto bg-white">
        {/* Profile Header */}
        <div className="relative p-8 rounded-b-3xl">
          <div className="flex items-center justify-between">
            <div className="flex-1">
              <h2 className="text-pp text-sm mb-1">내 정보 수정</h2>
              <h1 className="text-xl font-bold text-pp">{userInfo.name || '신현준'} 고객님</h1>
            </div>
            <div className="flex items-center justify-between mb-6 mt-6">
              <div className="flex gap-6 text-sm">
                <div className="text-center">
                  <div className="text-pp">설정화폐</div>
                  <div className="flex items-center gap-2">
                    <div className="text-pp font-medium">
                      {CURRENCY_OPTIONS.find(c => c.value === editForm.currency)?.label || getCurrencyDisplay(userInfo.currency)}
                    </div>
                    <button
                      onClick={() => setShowCurrencyModal(true)}
                      className="text-xs text-purple-600 hover:text-purple-700"
                    >
                      변경
                    </button>
                  </div>
                </div>
                <div className="text-center">
                  <div className="text-pp">설정언어</div>
                  <div className="flex items-center gap-2">
                    <div className="text-pp font-medium text-purple-600">
                      {COUNTRY_OPTIONS.find(c => c.value === editForm.language)?.label || getLanguageDisplay(userInfo.language)}
                    </div>
                    <button
                      onClick={() => setShowLanguageModal(true)}
                      className="text-xs text-purple-600 hover:text-purple-700"
                    >
                      변경
                    </button>
                  </div>
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
              <div className="w-56 h-56 bg-gray-200 rounded-lg overflow-hidden flex items-center justify-center relative">
                <img 
                  src={`${baseUrl}${editForm.image || userInfo.image || "/images/default.png"}`}
                  alt="프로필 이미지" 
                  className="w-full h-full object-cover"
                />
                <label className="absolute bottom-2 right-2 bg-purple-600 text-white p-2 rounded-full cursor-pointer hover:bg-purple-700 transition-colors">
                  <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M3 9a2 2 0 012-2h.93a2 2 0 001.664-.89l.812-1.22A2 2 0 0110.07 4h3.86a2 2 0 011.664.89l.812 1.22A2 2 0 0018.07 7H19a2 2 0 012 2v9a2 2 0 01-2 2H5a2 2 0 01-2-2V9z" />
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 13a3 3 0 11-6 0 3 3 0 016 0z" />
                  </svg>
                  <input 
                    type="file" 
                    accept="image/*" 
                    onChange={handleImageChange}
                    className="hidden"
                  />
                </label>
              </div>
            </div>
          </div>

          {/* Profile Information List */}
          <div className="flex-1 px-6 space-y-4">
            {/* 이름 (수정 가능) */}
            <div className="flex justify-between items-center py-3 border-b border-gray-100">
              <div className="flex-1 ml-4">
                <div className="flex items-center gap-4">
                  <label className="w-24 text-pp">이름</label>
                  <input
                    type="text"
                    value={editForm.name}
                    onChange={(e) => handleInputChange('name', e.target.value)}
                    className="flex-1 p-2 border rounded text-right"
                  />
                </div>
                {errors.name && <p className="text-red-500 text-sm mt-1">{errors.name}</p>}
              </div>
            </div>
            
            {/* 닉네임 (수정 가능) */}
            <div className="flex justify-between items-center py-3 border-b border-gray-100">
              <span className="text-pp">닉네임</span>
              <div className="flex-1 ml-4">
                <input
                  type="text"
                  value={editForm.nickname}
                  onChange={(e) => handleInputChange('nickname', e.target.value)}
                  className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-500 focus:border-transparent text-right"
                  placeholder="닉네임을 입력하세요"
                />
                {errors.nickname && <p className="text-red-500 text-sm mt-1">{errors.nickname}</p>}
              </div>
            </div>
            
            {/* 아이디 (수정 불가) */}
            <div className="flex justify-between items-center py-3 border-b border-gray-100">
              <span className="text-pp">아이디</span>
              <div className="flex items-center gap-2">
                <span className="text-pp text-gray-500">{userInfo.email || '-'}</span>
                {!userInfo.email && (
                  <button
                    onClick={() => setShowEmailModal(true)}
                    className="px-3 py-1 text-sm bg-purple-600 text-white rounded hover:bg-purple-700"
                  >
                    인증하기
                  </button>
                )}
              </div>
            </div>
            
            {/* 휴대전화 (수정 불가) */}
            <div className="flex justify-between items-center py-3 border-b border-gray-100">
              <span className="text-pp">휴대전화</span>
              <div className="flex items-center gap-2">
                <span className="text-pp text-gray-500">{getPhoneStatus(userInfo.phone)}</span>
                {!userInfo.phone && (
                  <button
                    onClick={() => setShowPhoneModal(true)}
                    className="px-3 py-1 text-sm bg-purple-600 text-white rounded hover:bg-purple-700"
                  >
                    인증하기
                  </button>
                )}
              </div>
            </div>
            
            {/* 알림 수신 (수정 불가) */}
            <div className="flex justify-between items-center py-3 border-b border-gray-100">
              <span className="text-pp">알림 수신</span>
              <span className="text-pp text-gray-500">{userInfo.notification === 'Y' ? '동의' : '비동의'}</span>
            </div>
            
            {/* 성별 (U일 경우에만 수정 가능) */}
            <div className="flex justify-between items-center py-3 border-b border-gray-100">
              <span className="text-pp">성별</span>
              <div className="flex-1 ml-4">
                {userInfo.gender === 'U' ? (
                  <div className="flex justify-end items-center gap-4">
                    <div className="flex gap-4">
                      <label className="flex items-center gap-2">
                        <input
                          type="radio"
                          name="gender"
                          value="M"
                          checked={editForm.gender === 'M'}
                          onChange={(e) => handleInputChange('gender', e.target.value)}
                          className="form-radio text-purple-600"
                        />
                        <span>남성</span>
                      </label>
                      <label className="flex items-center gap-2">
                        <input
                          type="radio"
                          name="gender"
                          value="F"
                          checked={editForm.gender === 'F'}
                          onChange={(e) => handleInputChange('gender', e.target.value)}
                          className="form-radio text-purple-600"
                        />
                        <span>여성</span>
                      </label>
                    </div>
                  </div>
                ) : (
                  <span className="text-pp text-gray-500 text-right block">{getGenderDisplay(userInfo.gender)}</span>
                )}
              </div>
            </div>
            
            {/* 비밀번호 변경 섹션 - 소셜 로그인이 아닌 경우에만 표시 */}
            {userInfo.social === 'L' && (
              <div className="mt-8 pt-6 border-t border-gray-200">
                <h3 className="text-pp font-bold mb-4">비밀번호 변경</h3>
                
                <div className="space-y-4">
                  <div>
                    <label className="block text-pp text-sm mb-2">새 비밀번호 (선택사항)</label>
                    <input
                      type="password"
                      value={editForm.password}
                      onChange={(e) => handleInputChange('password', e.target.value)}
                      className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-500 focus:border-transparent"
                      placeholder="새 비밀번호를 입력하세요 (6자 이상)"
                    />
                    {errors.password && <p className="text-red-500 text-sm mt-1">{errors.password}</p>}
                  </div>
                  
                  <div>
                    <label className="block text-pp text-sm mb-2">새 비밀번호 확인</label>
                    <input
                      type="password"
                      value={editForm.confirmPassword}
                      onChange={(e) => handleInputChange('confirmPassword', e.target.value)}
                      className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-500 focus:border-transparent"
                      placeholder="새 비밀번호를 다시 입력하세요"
                    />
                    {errors.confirmPassword && <p className="text-red-500 text-sm mt-1">{errors.confirmPassword}</p>}
                  </div>
                </div>
              </div>
            )}

            {/* Buttons */}
            <div className="flex justify-end gap-4 mt-8">
              <button
                onClick={() => navigate('/user')}
                className="px-6 py-2 border border-gray-300 rounded hover:bg-gray-50"
              >
                취소
              </button>
              <button
                onClick={handleSubmit}
                disabled={isLoading}
                className="px-6 py-2 bg-purple-600 text-white rounded hover:bg-purple-700 disabled:opacity-50"
              >
                {isLoading ? '수정 중...' : '수정완료'}
              </button>
            </div>
          </div>
        </div>
      </div>

      {/* Email Verification Modal */}
      {showEmailModal && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-white p-6 rounded-lg w-[400px]">
            <h3 className="text-lg font-semibold mb-4">이메일 인증</h3>
            <div className="space-y-4">
              <div className="flex gap-2">
                <input
                  type="text"
                  value={emailAuthCode}
                  onChange={(e) => setEmailAuthCode(e.target.value)}
                  className="flex-1 p-2 border rounded"
                  placeholder="인증코드 입력"
                />
                <button
                  onClick={handleSendEmailCode}
                  disabled={emailTimer > 0}
                  className="px-4 py-2 bg-purple-600 text-white rounded whitespace-nowrap"
                >
                  {emailTimer > 0 ? formatTime(emailTimer) : '인증번호 받기'}
                </button>
              </div>
              <div className="flex justify-end gap-2">
                <button
                  onClick={() => setShowEmailModal(false)}
                  className="px-4 py-2 border rounded"
                >
                  취소
                </button>
                <button
                  onClick={handleVerifyEmailCode}
                  className="px-4 py-2 bg-purple-600 text-white rounded"
                >
                  확인
                </button>
              </div>
            </div>
          </div>
        </div>
      )}

      {/* Phone Verification Modal */}
      {showPhoneModal && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-white p-6 rounded-lg w-[400px]">
            <h3 className="text-lg font-semibold mb-4">휴대전화 인증</h3>
            <div className="space-y-4">
              <div>
                <label className="block text-pp text-sm mb-2">휴대전화 번호</label>
                <div className="flex gap-2">
                  <input
                    type="tel"
                    value={phoneNumber}
                    onChange={handlePhoneNumberChange}
                    className="flex-1 p-2 border rounded"
                    placeholder="010-1234-5678"
                    maxLength={13}
                  />
                  <button
                    onClick={handleSendPhoneCode}
                    disabled={phoneTimer > 0}
                    className="px-4 py-2 bg-purple-600 text-white rounded whitespace-nowrap"
                  >
                    {phoneTimer > 0 ? formatTime(phoneTimer) : '인증번호 받기'}
                  </button>
                </div>
              </div>
              <div className="flex gap-2">
                <input
                  type="text"
                  value={phoneAuthCode}
                  onChange={(e) => setPhoneAuthCode(e.target.value)}
                  className="flex-1 p-2 border rounded"
                  placeholder="인증코드 입력"
                />
              </div>
              <div className="flex justify-end gap-2">
                <button
                  onClick={() => {
                    setShowPhoneModal(false);
                    setPhoneNumber('');
                    setPhoneAuthCode('');
                    setIsPhoneVerified(false);
                  }}
                  className="px-4 py-2 border rounded"
                >
                  취소
                </button>
                <button
                  onClick={handleVerifyPhoneCode}
                  className="px-4 py-2 bg-purple-600 text-white rounded"
                >
                  확인
                </button>
              </div>
            </div>
          </div>
        </div>
      )}

      {/* Language Settings Modal */}
      {showLanguageModal && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-white p-6 rounded-lg w-[400px]">
            <h3 className="text-lg font-semibold mb-4">언어 설정</h3>
            <div className="space-y-4">
              <div className="grid grid-cols-2 gap-4">
                {COUNTRY_OPTIONS.map((country) => (
                  <button
                    key={country.value}
                    onClick={() => handleLanguageSelect(country.value)}
                    className={`p-4 border rounded-lg flex flex-col items-center gap-2 transition-colors ${
                      editForm.language === country.value
                        ? 'border-purple-600 bg-purple-50'
                        : 'border-gray-200 hover:border-purple-300'
                    }`}
                  >
                    <img
                      src={`/images/flags/${country.image}`}
                      alt={country.label}
                      className="w-8 h-8 object-cover rounded-full"
                    />
                    <span className="text-sm">{country.label}</span>
                  </button>
                ))}
              </div>
              <div className="flex justify-end">
                <button
                  onClick={() => setShowLanguageModal(false)}
                  className="px-4 py-2 border rounded"
                >
                  취소
                </button>
              </div>
            </div>
          </div>
        </div>
      )}

      {/* Currency Settings Modal */}
      {showCurrencyModal && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-white p-6 rounded-lg w-[400px]">
            <h3 className="text-lg font-semibold mb-4">화폐 설정</h3>
            <div className="space-y-4">
              <div className="grid grid-cols-2 gap-4">
                {CURRENCY_OPTIONS.map((currency) => (
                  <button
                    key={currency.value}
                    onClick={() => handleCurrencySelect(currency.value)}
                    className={`p-4 border rounded-lg flex flex-col items-center gap-2 transition-colors ${
                      editForm.currency === currency.value
                        ? 'border-purple-600 bg-purple-50'
                        : 'border-gray-200 hover:border-purple-300'
                    }`}
                  >
                    <img
                      src={`/images/flags/${COUNTRY_OPTIONS.find(c => c.value === currency.country)?.image}`}
                      alt={currency.label}
                      className="w-8 h-8 object-cover rounded-full"
                    />
                    <span className="text-sm">{currency.label}</span>
                  </button>
                ))}
              </div>
              <div className="flex justify-end">
                <button
                  onClick={() => setShowCurrencyModal(false)}
                  className="px-4 py-2 border rounded"
                >
                  취소
                </button>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default MyPageEdit;