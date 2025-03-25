import { useState, useEffect, useRef } from "react";
import { Link, useNavigate } from "react-router-dom";

export default function CompanyRegisterForm() {
  const navigate = useNavigate();
  const [isVerifiedBusiness, setIsVerifiedBusiness] = useState(false);
  const [isVerifiedSales, setIsVerifiedSales] = useState(false);
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

  useEffect(() => {
    validateForm();
  }, [email, password, passwordConfirm, mainContact, authCode, bnRegNo, address, postalCode, detailAddress, isVerifiedPhone, isVerifiedBusiness, isVerifiedSales]);

  const validateForm = () => {
    let newErrors = {};
    const isSalesNumberValid = firstNum.length === 4 && koreanText.length > 0 && lastNum.length >= 1;
    if (!email.trim()) newErrors.email = "이메일을 입력해주세요.";
    if (!/\S+@\S+\.\S+/.test(email)) newErrors.email = "사용할 수 없는 이메일입니다.";
    if (!password.trim()) newErrors.password = "비밀번호를 입력해주세요.";
    if (!passwordConfirm.trim()) newErrors.passwordConfirm = "비밀번호 확인을 입력해주세요.";
    if (password !== passwordConfirm) newErrors.passwordConfirm = "비밀번호가 일치하지 않습니다.";
    if (!mainContact.trim()) newErrors.mainContact = "대표자 전화번호를 입력해주세요.";
    if (!isVerifiedPhone) newErrors.authCode = "전화번호 인증을 완료해주세요.";
    if (!bnRegNo.trim()) newErrors.bnRegNo = "사업자등록번호를 입력해주세요.";
    if (!isVerifiedBusiness) newErrors.bnRegNo = "사업자등록번호 인증을 완료해주세요.";
    if (!isSalesNumberValid) newErrors.mos = "통신판매업 신고 번호를 올바르게 입력해주세요.";
    if (!isVerifiedSales) newErrors.mos = "통신판매업 신고 번호 인증을 완료해주세요.";
    if (!address.trim() || !postalCode.trim() || !detailAddress.trim()) newErrors.address = "주소를 입력해주세요.";
    setErrors(newErrors);
    setIsFormValid(Object.keys(newErrors).length === 0);
  };

  const handleRegister = async () => {
    if (!isFormValid) {
        alert("모든 필수 항목을 올바르게 입력해주세요.");
        return;
    }
    const requestData = {
        email,
        password,
        passwordConfirm,
        name,
        repName,
        mainContact,
        bnRegNo,
        mos: `${firstNum}-${koreanText}-${lastNum}`,
        address,
        postalCode,
        profile: "default-profile-image.jpg", // 기본 프로필 이미지 또는 파일 경로
        category: "S", // 기본 카테고리 값
        status: "ACTIVATE",   // 기본 상태 값
        role: "USER"        // 기본 역할 값
    };
    try {
        const response = await fetch("http://192.168.0.121:5050/company/signup", {
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


  // 인증 코드 전송 (더미 데이터)
  const handleSendAuthCode = () => {
    setSentCode(""); // 기존 코드 초기화

    if (mainContact === "01037552866") {
      setSentCode("000000");  // 더미 인증 코드 설정
      setPhoneAuthMessage("인증번호가 전송되었습니다.");
      setErrors((prevErrors) => ({ ...prevErrors, mainContact: "" }));
    } else {
      setPhoneAuthMessage("❌ 인증 실패: 올바른 전화번호를 입력해주세요.");
    }
  };

  const handleVerifyPhone = () => {
    if (!authCode.trim()) {
      setPhoneAuthMessage("❌ 인증 실패: 인증번호를 입력해주세요.");
      return;
    }

    if (authCode === sentCode) {
      setIsVerifiedPhone(true);
      setPhoneAuthMessage("✅ 전화번호 인증 완료");
      setErrors((prevErrors) => ({ ...prevErrors, authCode: "" }));
    } else {
      setPhoneAuthMessage("❌ 인증 실패: 인증번호를 확인해주세요.");
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
    setBnRegNo(formatBusinessNumber(e.target.value));
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
  const handleVerifyBusinessNumber = () => {
    const formattedBusinessNumber = bnRegNo.replace(/-/g, "");

    if (formattedBusinessNumber === "0000000000") {
      setIsVerifiedBusiness(true);
      setBusinessAuthMessage("✅ 사업자 등록번호 인증 완료");
    } else {
      setBusinessAuthMessage("❌ 인증 실패: 번호를 확인해주세요.");
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

        <div className="mt-4 space-y-4">
          {[  { label: "이메일", value: email, setValue: setEmail, type: "email", error: errors.email, placeholder: "이메일 입력" },
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

          <div>
            <label className="text-sm text-[#845EC2]">전화번호</label>
            <div className="flex space-x-2">
              <input
                type="tel"
                value={mainContact}
                onChange={(e) => setMainContact(e.target.value)}
                className="flex-1 p-2 border border-[#845EC2] bg-white text-black rounded-md mt-1"
                placeholder="전화번호 입력"
                disabled={isVerifiedPhone}
              />
              <button
                className={`px-4 py-2 rounded-md mt-1 ${mainContact ? "bg-[#845EC2] text-white" : "bg-gray-300 text-gray-500 cursor-not-allowed"}`}
                disabled={!mainContact || isVerifiedPhone}
                onClick={handleSendAuthCode}
              >
                {isVerifiedPhone ? "✅ 인증 완료" : "인증 코드 보내기"}
              </button>
            </div>
            {phoneAuthMessage && <p className="text-green-500 text-xs mt-1">{phoneAuthMessage}</p>}
          </div>

          <div>
            <label className="text-sm text-[#845EC2]">인증번호</label>
            <div className="flex space-x-2">
              <input
                type="text"
                value={authCode}
                onChange={(e) => setAuthCode(e.target.value)}
                className="flex-1 p-2 border border-[#845EC2] bg-white text-black rounded-md mt-1"
                placeholder="인증번호 입력"
                maxLength={6}
                disabled={isVerifiedPhone}
              />
              <button
                className="px-4 py-2 rounded-md mt-1 bg-[#845EC2] text-white"
                disabled={!authCode || isVerifiedPhone}
                onClick={handleVerifyPhone}
              >
                {isVerifiedPhone ? "✅ 인증 완료" : "인증 확인"}
              </button>
            </div>
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
          <div>
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
                className="px-4 py-2 rounded-md bg-[#845EC2] text-white"
                onClick={handleVerifyBusinessNumber}
                disabled={isVerifiedBusiness}
              >
                {isVerifiedBusiness ? "✅ 인증 완료" : "조회하기"}
              </button>
              </div>
          {businessAuthMessage && <p className="text-sm text-green-500">{businessAuthMessage}</p>}
        </div>

        {/* 통신판매업 신고번호 입력 및 조회 버튼 */}
        <div className="mt-4">
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
        </div>
      </div>

          {/* 회원가입 버튼 */}
          <button
            className={`w-full p-3 rounded-md text-white mt-4 ${isFormValid ? "bg-[#845EC2]" : "bg-gray-300 cursor-not-allowed"}`}
            onClick={handleRegister}
            disabled={!isFormValid}
          >
            업체 회원가입 하기
          </button>
        </div>
      </div>
  );
}