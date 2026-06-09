import { useState, useEffect, useRef } from "react";
import { Link, useNavigate } from "react-router-dom";
import { toast } from "react-toastify";

const baseUrl = import.meta.env.VITE_API_BASE_URL;
export default function CompanyRegisterForm() {
  const navigate = useNavigate();
  const [isVerifiedBusiness, setIsVerifiedBusiness] = useState("false");
  const [isVerifiedSales, setIsVerifiedSales] = useState(true);
  const [businessAuthMessage, setBusinessAuthMessage] = useState("");
  const [salesAuthMessage, setSalesAuthMessage] = useState("");
  const [isVerifiedPhone, setIsVerifiedPhone] = useState(false);
  const [phoneAuthMessage, setPhoneAuthMessage] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [passwordConfirm, setConfirmPassword] = useState("");
  const [repName, setRepName] = useState("");
  const [name, setName] = useState("");
  const [mainContact, setMainContact] = useState("");
  const [authCode, setAuthCode] = useState("");
  const [bnRegNo, setBnRegNo] = useState("");
  const [address, setAddress] = useState("");
  const [detailAddress, setDetailAddress] = useState("");
  const [postalCode, setPostalCode] = useState("");
  const [errors, setErrors] = useState({});
  const [isFormValid, setIsFormValid] = useState(false);
  const [sentCode, setSentCode] = useState("");
  const [firstNum, setFirstNum] = useState("");
  const [koreanText, setKoreanText] = useState("");
  const [lastNum, setLastNum] = useState("");
  const [isEmailVerified, setIsEmailVerified] = useState(false);
  const [emailAuthCode, setEmailAuthCode] = useState("");
  const [emailAuthMessage, setEmailAuthMessage] = useState("");
  const [timer, setTimer] = useState(0); // 남은 초 (예: 300초)
  const timerRef = useRef(null); // setInterval을 위한 ref
  const [hasRequestedPhoneCode, setHasRequestedPhoneCode] = useState(false); // 전화번호 인증 재요청 확인용
  const [isEmailSent, setIsEmailSent] = useState(false);
  const [emailTimer, setEmailTimer] = useState(0);
  const emailTimerRef = useRef(null);
  const [selectedCategory, setSelectedCategory] = useState('');

  useEffect(() => {
    validateForm();
  }, [
    email, password, passwordConfirm, mainContact, authCode, bnRegNo,
    address, postalCode, detailAddress,
    isVerifiedPhone, isVerifiedBusiness, isVerifiedSales,
    isEmailVerified
  ]);

  // 이메일 인증번호 타이머
  useEffect(() => {
    if (emailTimer > 0) {
      emailTimerRef.current = setTimeout(() => setEmailTimer(emailTimer - 1), 1000);
    }

    return () => clearTimeout(emailTimerRef.current);
  }, [emailTimer]);

  // 전화번호 인증번호 타이머 설정
  useEffect(() => {
    if (timer > 0) {
      timerRef.current = setTimeout(() => setTimer(timer - 1), 1000);
    }
    return () => clearTimeout(timerRef.current);
  }, [timer]);

  // 타이머 포맷 함수
  const formatTime = (seconds) => {
    const min = String(Math.floor(seconds / 60)).padStart(2, '0');
    const sec = String(seconds % 60).padStart(2, '0');
    return `${min}:${sec}`;
  };

  // 전화번호 자동하이픈 + 숫자 11자리까지 허용
  const handlePhoneChange = (e) => {
    let value = e.target.value;
    value = value.replace(/\D/g, "");
    value = value.slice(0, 11);

    if (value.length < 4) {
      value = value;
    } else if (value.length < 8) {
      value = value.replace(/(\d{3})(\d{1,4})/, "$1-$2");
    } else {
      value = value.replace(/(\d{3})(\d{4})(\d{1,4})/, "$1-$2-$3");
    }

    setMainContact(value);
  };

  const verifyBusinessNumber =async () => {
    const result = await api.verifyBusinessNumber(bnRegNo);
    if( result.valid) {
      setIsVerifiedBusiness(bnRegNo);
    }
  };

  const validateForm = () => {
    let newErrors = {};
    if (!email.trim()) {
      newErrors.email = "이메일을 입력해주세요.";
    } else if (!/\S+@\S+\.\S+/.test(email)) {
      newErrors.email = "사용할 수 없는 이메일입니다.";
    } else if (!isEmailVerified) {
      newErrors.email = "이메일 인증을 완료해주세요.";
    }

    if (!password.trim()) newErrors.password = "비밀번호를 입력해주세요.";
    if (!passwordConfirm.trim()) newErrors.passwordConfirm = "비밀번호 확인을 입력해주세요.";
    if (password !== passwordConfirm) newErrors.passwordConfirm = "비밀번호가 일치하지 않습니다.";
    if (!mainContact.trim()) newErrors.mainContact = "대표자 전화번호를 입력해주세요.";
    if (!isVerifiedPhone) newErrors.authCode = "전화번호 인증을 완료해주세요.";
    if (!bnRegNo.trim()) newErrors.bnRegNo = "사업자등록번호를 입력해주세요.";
    // if (!isVerifiedBusiness) newErrors.bnRegNo = "사업자등록번호 인증을 완료해주세요.";
    // if (!isSalesNumberValid) newErrors.mos = "통신판매업 신고 번호를 올바르게 입력해주세요.";
    // if (!isVerifiedSales) newErrors.mos = "통신판매업 신고 번호 인증을 완료해주세요.";
    if (!address.trim() || !postalCode.trim() || !detailAddress.trim()) newErrors.address = "주소를 입력해주세요.";
    setErrors(newErrors);
    console.log("newErrors : ", Object.keys(newErrors).length);
    console.log("newErrors : ", Object.keys(newErrors));
    setIsFormValid(Object.keys(newErrors).length === 0);
    if (!bnRegNo.trim()) {
      newErrors.bnRegNo = "사업자등록번호를 입력해주세요.";
    }else if (!isVerifiedBusiness) {
      newErrors.bnRegNo = "사업자등록번호 인증을 완료해주세요.";
    }
    if (!address.trim() || !postalCode.trim() || !detailAddress.trim()) {
    newErrors.address = "주소를 입력해주세요.";
    }
    setErrors(newErrors);
    setIsFormValid(Object.keys(newErrors).length === 0);
  };

  const categoryMap = {
    스튜디오: 'S',
    드레스: 'D',
    메이크업: 'M',
  };

  const categories = Object.keys(categoryMap);

  const handleCategorySelect = (category) => {
    console.log("categoryMap[category] : ", categoryMap[category]);
    setSelectedCategory(categoryMap[category]);
  };

  const handleRegister = async () => {
    console.log("isFormValid : ", isFormValid);
    if (!isFormValid) {
      alert("모든 필수 항목을 올바르게 입력해주세요.");
      return;
    }
    console.log("PassWordConfirm : {}", passwordConfirm);
    const requestData = {
      email,
      password,
      password_confirm: passwordConfirm,
      name,
      repName,
      contact: mainContact.replace(/-/g, ""),
      bn_reg_no: bnRegNo,
      mos: "2025-서울강남-00000",
      address,
      postal_code: postalCode,
      profile: "default-profile-image.jpg", // 기본 프로필 이미지 또는 파일 경로
      category: selectedCategory,
      status: "ACTIVATE",   // 기본 상태 값
      role: "COMPANY"        // 기본 역할 값
    };

    console.log("PasswordConfirm in RequestData : ", requestData);

    try {
      const response = await fetch(`${baseUrl}/company/signup`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(requestData)
      });
      if (response.ok) {
        alert("회원가입이 성공적으로 완료되었습니다!");
        navigate("/register/success");
      } else {
        const errorData = await response.json();
        alert(`회원가입 실패: ${errorData.message}`);
      }
    } catch (error) {
      alert("서버와의 통신 중 문제가 발생했습니다.");
    }
  };

  // ✅ 이메일 인증번호 전송 요청
  const handleSendEmailCode = async () => {
    try {
      const res = await fetch(`${baseUrl}/email/send`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email }),
      });
      const data = await res.json();
      console.log("Email Send : ", data);
      setIsEmailSent(true);
      if (data.result.resultCode === 200) {
        toast.success(data.data.message, {
          position: "top-center",
          autoClose: 750,
        });
        setEmailAuthMessage(data.data.message);
        clearTimeout(emailTimerRef.current);
        setEmailTimer(300); // 5분
      } else {
        setIsEmailSent(false);
        toast.error(data.data.message, {
          position: "top-center",
          autoClose: 750,
        });
      }
    } catch (err) {
      console.error("이메일 전송 실패:", err);
      setEmailAuthMessage("이메일 전송 중 오류 발생");
    }
  };

  // ✅ 이메일 인증 확인
  const handleVerifyEmailCode = async () => {
    try {
      const res = await fetch(`${baseUrl}/email/verify`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email, code: emailAuthCode }),
      });
      const data = await res.json();
      console.log("Email Verifing : ", data);
      if (data.result.resultCode === 200) {
        toast.success(data.data.message, {
          position: "top-center",
          autoClose: 750,
        });
        setIsEmailVerified(true);
        setEmailAuthMessage(data.data.message);
        setErrors(prev => {
          const { email, ...rest } = prev;
          return rest;
        });
      } else {
        toast.error(data.data.message, {
          position: "top-center",
          autoClose: 750,
        });
        setEmailAuthMessage(data.data.message);
      }
    } catch (err) {
      console.error("이메일 인증 실패:", err);
      setEmailAuthMessage("이메일 인증 중 오류 발생");
    }
  };

  // 핸드폰 인증 코드 전송 (더미 데이터)
  const handleSendAuthCode = async () => {
    const requestNumber = mainContact.replace(/-/g, "");
    try {
      const res = await fetch(`${baseUrl}/sms/send`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ phoneNum: requestNumber }),
      });
      const data = await res.json();
      console.log("Phone Send : ", data);
      console.log(data.result.resultCode);
      console.log(data.description);
      if (data.result.resultCode === 200) {
        toast.success(data.description, {
          position: "top-center",
          autoClose: 750,
        });
      } else {
        toast.error(data.description, {
          position: "top-center",
          autoClose: 750,
        });
      }
      setPhoneAuthMessage(data.data.description);
    } catch (err) {
      console.log(err);
    }
  };

  const handleVerifyPhone = async () => {
    const requestNumber = mainContact.replace(/-/g, "");
    try {
      const res = await fetch(`${baseUrl}/sms/verify`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ phoneNum: requestNumber, code: authCode }),
      });
      const data = await res.json();
      console.log("Phone Verifing : ", data);
      if (data.data.verified === true) {
        setIsVerifiedPhone(true);
        setPhoneAuthMessage("✅ 전화번호 인증 완료");
        toast.success(data.description, {
          position: "top-center",
          autoClose: 750,
        });
        setErrors((prevErrors) => ({ ...prevErrors, authCode: "" }));
      } else {
        toast.error(data.description, {
          position: "top-center",
          autoClose: 750,
        });
      }
    } catch (err) {
      toast.error("전화번호 인증실패...", {
        position: "top-center",
        autoClose: 750,
      });
      console.error("전화번호 인증코드 전송 실패:", err);
      setPhoneAuthMessage("전화번호 인증번호 전송 중 에러발생");
    }
    if (!authCode.trim()) {
      setPhoneAuthMessage("❌ 인증 실패: 인증번호를 입력해주세요.");
      return;
    }
  };

  // 카카오 주소 검색 API 실행 함수
  const handleAddressSearch = () => {
    if (!window.daum || !window.daum.Postcode) {
      console.error("카카오 주소 검색 API가 로드되지 않았습니다.");
      return;
    }

    new window.daum.Postcode({
      oncomplete: function (data) {
        setAddress(data.roadAddress);
        setPostalCode(data.zonecode);
      },
    }).open();
  };

  // 사업자 등록번호 자동 하이픈 삽입
  const formatBusinessNumber = (value) => {
    value = value.replace(/\D/g, "").slice(0, 10); // 숫자만 입력, 10자리 제한
    return value.replace(/^([0-9]{3})([0-9]{2})([0-9]{0,5})$/, (_, p1, p2, p3) => {
      return `${p1}-${p2}-${p3}`;
    });
  };
  const handleBusinessNumberChange = (e) => {
    let value = e.target.value.replace(/\D/g, "");
    value = value.slice(0, 10);
    if (value.length < 4) {
      value = value;
    } else if (value.length < 6) {
      value = value.replace(/(\d{3})(\d{1,2})/, "$1-$2");
    } else {
      value = value.replace(/(\d{3})(\d{2})(\d{1,5})/, "$1-$2-$3");
    }
    setBnRegNo(e.target.value);
    setIsVerifiedBusiness(false);
    setBusinessAuthMessage("");
  };

  // 통신판매업신고번호 입력
  const koreanOptions = ["서울강남", "부산해운대", "대구중구", "광주서구", "인천남동"];

  const handleFirstNumChange = (e) => {
    setFirstNum(e.target.value.replace(/[^0-9]/g, "").slice(0, 4));
  };

  const handleLastNumChange = (e) => {
    setLastNum(e.target.value.replace(/[^0-9]/g, "").slice(0, 5));
  };

  const isSalesNumberValid =
    firstNum.length === 4 && koreanText.length > 0 && lastNum.length >= 1;

  // 사업자 등록번호 인증 
  const handleVerifyBusinessNumber = async() => {
   if (!/^\d{3}-\d{2}-\d{5}$/.test(bnRegNo)) {
      toast.error("사업자 등록 번호 형식이 올바르지 않습니다. (예: 123-45-67890)");
        return;
    }
    try {
      const response = await fetch(`${baseUrl}/company/status-bn`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({cpBnRegNo: bnRegNo}),
      });
      if (!response.ok) {
            const errorData = await response.json();
            setIsVerifiedBusiness(false);
            setBusinessAuthMessage(` ${errorData.message}`);
            toast.error(errorData.message);
            return;
      }
      
      const data = await response.json();
      console.log("사업자 등록번호 인증 : ", data);

      if (data.valid) {
        setIsVerifiedBusiness(true);
        setBusinessAuthMessage(`✅ ${data.message} (${data.taxType ?? ""})`);
        toast.success("사업자등록번호 인증에 성공했습니다.");
      } else {
        setIsVerifiedBusiness(false);
        setBusinessAuthMessage(`❌ ${data.message ?? "유효하지 않은 번호입니다."}`);
        toast.error(data.message ?? "인증 실패");
      } 
    } catch (error) {
      console.error("Error:", error);
      toast.error("서버 통신 중 오류가 발생했습니다.");
      setBusinessAuthMessage("❌ 서버와의 통신에 실패했습니다.");
    }
};

  // 통신판매업신고번호 인증
  const handleVerifySalesNumber = () => {
    const formattedSalesNumber = `${firstNum}-${koreanText}-${lastNum}`;

    if (formattedSalesNumber === "2025-서울강남-00000") {
      setIsVerifiedSales(true);
      setSalesAuthMessage("✅ 통신판매업 신고번호 인증 완료");
    } else {
      setSalesAuthMessage("❌ 인증 실패: 번호를 확인해주세요.");
    }
  };

  // 한글 선택 변경 
  const handleKoreanTextChange = (e) => {
    setKoreanText(e.target.value);
  };


  return (
    <div className="flex justify-center items-center min-h-screen bg-white p-4">
      <div className="w-[900px] p-10 bg-white shadow-lg rounded-2xl border border-gray-200">
        <Link to="/" className="block text-center">
          <h1 className="text-5xl font-inknut font-bold text-[#845EC2]">Lächeln</h1>
          <p className="text-[#845EC2] mb-6">스튜디오 드레스 메이크업</p>
        </Link>

        <h2 className="text-xl text-[#845EC2] font-semibold mt-6">업체 회원가입</h2>

        {/* 이메일 버튼 */}
        <div className="mt-4 space-y-1">
          <label className="text-sm text-[#845EC2]">이메일</label>
          <div className="flex items-center gap-x-2">
            <input
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              className="flex-1 p-2 border border-[#845EC2] bg-white text-black rounded-md mt-1 focus:outline-none focus:ring-2 focus:ring-[#845EC2]"
              placeholder="이메일 입력"
              disabled={isEmailVerified}
            />
            <button
              className={`shrink-0 px-4 py-2 mt-1 rounded-md min-w-[170px] text-white text-sm transition-all
                ${isEmailVerified
                  ? "bg-gray-300 text-gray-500 cursor-not-allowed"
                  : "bg-[#845EC2] hover:bg-[#6b49a8]"}`}
              disabled={isEmailVerified}
              onClick={handleSendEmailCode}
            >
              {isEmailSent ? "📨 재요청 보내기" : "인증코드 보내기"}
            </button>
          </div>

          {errors.email && <p className="text-red-500 text-xs">{errors.email}</p>}

          {/* 인증코드 입력 영역 */}
          {isEmailSent && (
            <div className="space-y-2 mt-2">
              <div className="flex items-center space-x-2">
                <div className="relative flex-1">
                  <input
                    type="text"
                    value={emailAuthCode}
                    onChange={(e) => setEmailAuthCode(e.target.value)}
                    className="w-full p-2 border border-[#845EC2] bg-white text-black rounded-md focus:outline-none focus:ring-2 focus:ring-[#845EC2] pr-16"
                    placeholder="이메일 인증코드 입력"
                    maxLength={6}
                    disabled={isEmailVerified}
                  />
                  {/* 타이머 */}
                  {emailTimer > 0 && !isEmailVerified && (
                    <span className="absolute right-3 top-2 text-xl text-blue-600 font-mono">
                      {formatTime(emailTimer)}
                    </span>
                  )}
                </div>

                <button
                  className={`px-4 py-2 rounded-md text-sm font-semibold min-w-[130px]
                    ${isEmailVerified
                      ? "bg-green-500 text-white"
                      : emailAuthCode
                        ? "bg-[#845EC2] text-white"
                        : "bg-gray-300 text-gray-500 cursor-not-allowed"}`}
                  disabled={!emailAuthCode || isEmailVerified}
                  onClick={handleVerifyEmailCode}
                >
                  {isEmailVerified ? "✅ 인증 완료" : "이메일 인증 확인"}
                </button>
              </div>

              {emailAuthMessage && (
                <p className={`text-xs mt-1 ${isEmailVerified ? "text-green-500" : "text-red-500"}`}>
                  {emailAuthMessage}
                </p>
              )}
            </div>
          )}
        </div>

        {/* 비밀번호~ 업체명 입력창 */}
        <div className="mt-4 space-y-4">
          {[
            { label: "비밀번호", value: password, setValue: setPassword, type: "password", error: errors.password, placeholder: "비밀번호 입력 (영문, 숫자, 특수문자 포함 8~20자)" },
            { label: "비밀번호 확인", value: passwordConfirm, setValue: setConfirmPassword, type: "password", error: errors.confirmPassword, placeholder: "비밀번호 재입력" },
            { label: "이름", value: repName, setValue: setRepName, type: "text", placeholder: "이름 입력" },
            { label: "업체명", value: name, setValue: setName, type: "text", placeholder: "업체명 입력" },
          ].map(({ label, value, setValue, type, error, placeholder }) => (
            <div key={label}>
              <label className="text-sm text-[#845EC2]">{label}</label>
              <input
                type={type}
                value={value}
                onChange={(e) => setValue(e.target.value)}
                className="w-full p-2 border border-[#845EC2] bg-white text-black rounded-md mt-1 focus:outline-none focus:ring-2 focus:ring-[#845EC2]"
                placeholder={placeholder}
              />
              {error && <p className="text-red-500 text-xs">{error}</p>}
            </div>
          ))}

          {/* 카테고리 선택창 */}
          <div className="mt-4 space-y-4">
            <h3 className="text-lg font-semibold">카테고리 선택</h3>
            <div className="flex space-x-4">
              {categories.map((category) => (
                <button
                  key={category}
                  className={`px-4 py-2 rounded-lg transition-colors duration-200 ${selectedCategory === categoryMap[category]
                    ? 'bg-purple-500 text-white border border-purple-500 focus:outline-none'
                    : 'bg-white text-black border border-gray-300 hover:bg-blue-500 hover:text-white focus:outline-none'
                    }`}
                  onClick={() => handleCategorySelect(category)}
                >
                  {category}
                </button>
              ))}
            </div>
          </div>

          {/* 전화번호 버튼  */}
          <div>
            <label className="text-sm text-[#845EC2]">전화번호</label>

            {/* 전화번호 입력창 + 인증 요청 버튼 한 줄 */}
            <div className="flex items-center gap-x-2">
              <input
                type="tel"
                value={mainContact}
                onChange={handlePhoneChange}
                className="flex-1 p-2 border border-[#845EC2] bg-white text-black rounded-md mt-1 focus:outline-none focus:ring-2 focus:ring-[#845EC2]"
                placeholder="전화번호 입력"
                disabled={isVerifiedPhone}
              />
              <button
                className={`shrink-0 px-4 py-2 mt-1 rounded-md min-w-[170px] text-white text-sm font-semibold transition-all
                  ${isVerifiedPhone
                    ? "bg-gray-300 text-gray-500 cursor-not-allowed"
                    : "bg-[#845EC2] hover:bg-[#6b49a8]"}`}
                disabled={isVerifiedPhone}
                onClick={handleSendAuthCode}
              >
                {sentCode ? "📨 재요청 보내기" : "인증 코드 보내기"}
              </button>
            </div>

            {/* 인증번호 입력 영역 */}
            {mainContact && (
              <>
                <div className="flex items-center space-x-2 mt-2">
                  <div className="relative flex-1">
                    <input
                      type="text"
                      value={authCode}
                      onChange={(e) => setAuthCode(e.target.value)}
                      className="w-full p-2 border border-[#845EC2] bg-white text-black rounded-md focus:outline-none focus:ring-2 focus:ring-[#845EC2] pr-16"
                      placeholder="인증번호 입력"
                      maxLength={6}
                      disabled={isVerifiedPhone}
                    />
                    {/* ⏱ 타이머: 인증번호 입력창 안 오른쪽 상단에 표시 */}
                    {sentCode && timer > 0 && !isVerifiedPhone && (
                      <span className="absolute right-3 top-2 text-xl text-blue-600 font-mono">
                        {formatTime(timer)}
                      </span>
                    )}
                  </div>

                  <button
                    className={`px-4 py-2 rounded-md text-sm font-semibold min-w-[130px]
                      ${isVerifiedPhone
                        ? "bg-green-500 text-white"
                        : authCode
                          ? "bg-[#845EC2] text-white"
                          : "bg-gray-300 text-gray-500 cursor-not-allowed"}`}
                    disabled={!authCode || isVerifiedPhone}
                    onClick={handleVerifyPhone}
                  >
                    {isVerifiedPhone ? "✅ 인증 완료" : "인증 확인"}
                  </button>
                </div>

                {/* 인증 메시지 */}
                {phoneAuthMessage && (
                  <p className={`text-xs mt-1 ${isVerifiedPhone ? "text-green-500" : "text-red-500"}`}>
                    {phoneAuthMessage}
                  </p>
                )}
              </>
            )}
          </div>

          {/* 카카오맵 주소 검색 */}
          <div>
            <label className="text-sm text-[#845EC2]">주소</label>
            <div className="flex space-x-2">
              <input
                type="text"
                value={address}
                readOnly
                className="flex-1 p-2 border border-[#845EC2] bg-white text-black rounded-md mt-1"
                placeholder="주소 검색"
              />
              <button onClick={handleAddressSearch} className="px-4 py-2 rounded-md mt-1 bg-[#845EC2] text-white">
                검색하기
              </button>
            </div>
            <input
              type="text"
              value={postalCode}
              readOnly
              className="w-full p-2 border border-[#845EC2] bg-white text-black rounded-md mt-2"
              placeholder="우편번호"
            />
            <input
              type="text"
              value={detailAddress}
              onChange={(e) => setDetailAddress(e.target.value)}
              className="w-full p-2 border border-[#845EC2] bg-white text-black rounded-md mt-2"
              placeholder="상세주소 입력"
            />
          </div>

          {/* 사업자 등록번호 입력 + 조회 버튼 */}
          { <div>
            <label className="text-sm text-[#845EC2]">사업자 등록 번호</label>
            <div className="flex space-x-2">
              <input
                type="text"
                value={bnRegNo}
                onChange={handleBusinessNumberChange}
                className="flex-1 p-2 border border-[#845EC2] bg-white text-black rounded-md mt-1"
                placeholder="사업자 등록 번호 입력 (예: 123-45-67890)"
                maxLength="12"
              />
              <button
                className="px-4 py-2 rounded-md bg-[#845EC2] text-white abled:opacity-60"
                onClick={handleVerifyBusinessNumber}
                disabled={isVerifiedBusiness}
              >
                {isVerifiedBusiness ? "인증 완료" : "조회하기"}
              </button>
            </div>
            
            {/* 변경된 피드백 메시지 영역 */}
            {businessAuthMessage && (
              <p className={`text-sm mt-1 ${isVerifiedBusiness ? "text-green-500" : "text-red-500"}`}>
                {businessAuthMessage}
              </p>
            )}
            {errors.bnRegNo && !businessAuthMessage && (
              <p className="text-sm text-red-500 mt-1">{errors.bnRegNo}</p>
            )}
          </div> }
          {/* 통신판매업 신고번호 입력 및 조회 버튼 */}
          {/* <div className="mt-4">
            <label className="text-sm text-[#845EC2]">통신 판매업 신고 번호</label>
            <div className="flex space-x-2 items-center">
              <input
                type="text"
                value={firstNum}
                onChange={(e) => setFirstNum(e.target.value)}
                className="w-30 p-2 border border-[#845EC2] bg-white text-black rounded-md mt-1"
                placeholder="2025"
                maxLength={4}
                disabled={isVerifiedSales}
              />
              <span className="text-[#845EC2]">-</span>
              <select
                value={koreanText}
                onChange={handleKoreanTextChange}
                className="w-40 p-2 border border-[#845EC2] bg-white text-black rounded-md mt-1"
                disabled={isVerifiedSales}
              >
                <option value="" disabled>지역 선택</option>
                {koreanOptions.map((option) => (
                  <option key={option} value={option}>{option}</option>
                ))}
              </select>
              <span className="text-[#845EC2]">-</span>
              <input
                type="text"
                value={lastNum}
                onChange={(e) => setLastNum(e.target.value)}
                className="w-40 p-2 border border-[#845EC2] bg-white text-black rounded-md mt-1"
                placeholder="00000"
                maxLength={5}
                disabled={isVerifiedSales}
              />
              <button
                className="px-4 py-2 rounded-md bg-[#845EC2] text-white"
                onClick={handleVerifySalesNumber}
                disabled={isVerifiedSales}
              >
                {isVerifiedSales ? "✅ 인증 완료" : "조회하기"}
              </button>
            </div>
            {salesAuthMessage && <p className="text-sm text-green-500">{salesAuthMessage}</p>}
          </div> */}
        </div>

        {/* 회원가입 버튼 */}
        <button
          className={`w-full p-3 rounded-md text-white mt-4 bg-[#845EC2]`}
          onClick={handleRegister}
        >
          업체 회원가입 하기
        </button>
      </div>
    </div>
  );
}