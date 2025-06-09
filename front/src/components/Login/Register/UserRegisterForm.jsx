import { useState, useEffect, useRef } from "react"
import { Link, useNavigate, useLocation } from "react-router-dom";
import apiClient from "../../../lib/apiClient";
import { toast } from "react-toastify";

const baseUrl = import.meta.env.VITE_API_BASE_URL;

export default function UserRegisterForm() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [name, setName] = useState("");
  const [nickname, setNickname] = useState("");
  const [phone, setPhone] = useState("");
  const [authCode, setAuthCode] = useState("");
  const [birthDate, setBirthDate] = useState("");
  const [gender, setGender] = useState(null);
  const [errors, setErrors] = useState({});
  const [isFormValid, setIsFormValid] = useState(false);
  const [isVerified, setIsVerified] = useState(false);
  const [sentCode, setSentCode] = useState(null);
  const [authMessage, setAuthMessage] = useState("");
  const [isEmailVerified, setIsEmailVerified] = useState(false);
  const [emailAuthCode, setEmailAuthCode] = useState("");
  const [emailAuthMessage, setEmailAuthMessage] = useState("이메일 인증을 진행해주세요.");
  const [timer, setTimer] = useState(0); // 남은 초 (예: 300초)
  const timerRef = useRef(null); // setInterval을 위한 ref
  const [hasRequestedPhoneCode, setHasRequestedPhoneCode] = useState(false); // 전화번호 인증 재요청 확인용
  const [isEmailSent, setIsEmailSent] = useState(false);
  const [emailTimer, setEmailTimer] = useState(0);
  const emailTimerRef = useRef(null);
  const [isVerifiedPhone, setIsVerifiedPhone] = useState(false);
  const [phoneAuthMessage, setPhoneAuthMessage] = useState("");
  const [mainContact, setMainContact] = useState("");

  const navigate = useNavigate();
  const location = useLocation();
  const adsNotification = location.state?.adsNotification || "N";

  useEffect(() => {
    validateForm();
  }, [email, password, confirmPassword, mainContact, authCode, birthDate, gender, isVerified, isEmailVerified, name]);

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

  const validateForm = () => {
    let newErrors = {};
    if (!email.trim()) {
      newErrors.email = "이메일을 입력해주세요.";
    } else if (!/\S+@\S+\.\S+/.test(email)) {
      newErrors.email = "사용할 수 없는 이메일입니다.";
    } else if (!isEmailSent) {
      newErrors.email = "이메일 인증을 진행해주세요.";
    }
    if (!/^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d!@#$%^&*]{8,20}$/.test(password)) newErrors.password = "사용할 수 없는 비밀번호입니다.";
    if (password !== confirmPassword) newErrors.confirmPassword = "비밀번호가 일치하지 않습니다.";
    if (!/^\d{10,11}$/.test(mainContact.replace(/-/g, ""))) newErrors.phone = "전화번호 형식이 맞지 않습니다.";
    if (!isVerifiedPhone) newErrors.authCode = "인증을 완료해주세요.";
    if (!/^\d{8}$/.test(birthDate)) newErrors.birthDate = "생년월일 형식이 맞지 않습니다.";
    if (!gender) newErrors.gender = "성별을 선택해주세요.";
    setErrors(newErrors);
    setIsFormValid(Object.keys(newErrors).length === 0);
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
      console.log("Email Verifing : ",data);
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

  // 핸드폰 인증 코드 전송
  const handleSendAuthCode = async () => {
    const requestNumber = mainContact.replace(/-/g, "");
    try {
      const res = await fetch(`${baseUrl}/sms/send`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ phoneNum: requestNumber }),
      });
      const data = await res.json();
      console.log("Phone Send : ",data);
      console.log(data.result.resultCode);
      console.log(data.description);
      if(data.result.resultCode === 200)  {
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

  // 전송한 핸드폰 인증번호 검증
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
      if(data.data.verified === true) {
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

  // 생년월일 yyyyMMdd → yyyy-MM-dd 변환 함수
  const formatBirthDate = (str) => {
    if (!/^\d{8}$/.test(str)) return str;
    return `${str.slice(0,4)}-${str.slice(4,6)}-${str.slice(6,8)}`;
  };

  const handleRegister = async () => {
    if (!isFormValid) {
      alert("모든 필수 항목을 올바르게 입력해주세요.");
      return;
    }

    // 백엔드로 보낼 데이터 구성
    const requestData = {
      email,
      password,
      name,
      nickName: nickname,
      phone: mainContact.replace(/-/g, ""),
      birthDate: formatBirthDate(birthDate),
      gender: gender === "남자" ? "M" : gender === "여자" ? "F" : gender,
      adsNotification,
    };

    try {
      await apiClient.post("/user/signup", requestData);
      alert("회원가입이 성공적으로 완료되었습니다!");
      navigate("/register/success");
    } catch (error) {
      if (error.response && error.response.data && error.response.data.message) {
        alert(`회원가입 실패: ${error.response.data.message}`);
      } else {
        alert("서버와의 통신 중 문제가 발생했습니다.");
      }
    }
  };

  return (
    <div className="flex justify-center items-center min-h-screen bg-white p-4">
      <div className="w-[900px] p-10 bg-white shadow-lg rounded-2xl border border-gray-200">
        <Link to="/" className="block text-center">
          <h1 className="text-5xl font-inknut font-bold text-[#845EC2]">Lächeln</h1>
          <p className="text-[#845EC2] mb-6">스튜디오 드레스 메이크업</p>
        </Link>

        <h2 className="text-xl text-[#845EC2] font-semibold mt-6">회원가입</h2>

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

        <div className="mt-4 space-y-4">
          {/*  비밀번호, 이름 입력 필드드*/}
          {[
            { label: "비밀번호", value: password, setValue: setPassword, type: "password", error: errors.password, placeholder: "비밀번호 입력 (영문, 숫자, 특수문자 포함 8~20자)" },
            { label: "비밀번호 확인", value: confirmPassword, setValue: setConfirmPassword, type: "password", error: errors.confirmPassword, placeholder: "비밀번호 재입력" },
            { label: "이름", value: name, setValue: setName, type: "text", placeholder: "이름 입력" }]
            .map(({ label, value, setValue, type, error, placeholder }) => (
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

          {/* 닉네임 입력 필드 */}
          <div>
            <label className="text-sm text-[#845EC2]">닉네임</label>
            <input
              type="text"
              value={nickname}
              onChange={e => setNickname(e.target.value)}
              className="w-full p-2 border border-[#845EC2] bg-white text-black rounded-md mt-1 focus:outline-none focus:ring-2 focus:ring-[#845EC2]"
              placeholder="닉네임 입력"
            />
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

          {/* 생년월일 입력 */}
          <div>
            <label className="text-sm text-[#845EC2]">생년월일</label>
              <input
                type="text"
                value={birthDate}
                onChange={(e) => setBirthDate(e.target.value)}
                className="w-full p-2 border border-[#845EC2] bg-white text-black rounded-md mt-1 focus:outline-none focus:ring-2 focus:ring-[#845EC2]"
                placeholder="생년월일 입력 (yyyymmdd)"
              />
              {errors.birthDate && <p className="text-red-500 text-xs">{errors.birthDate}</p>}
          </div>

          {/* 성별 선택 */}
          <div>
          <label className="text-sm text-[#845EC2]">성별</label>
          <div className="flex space-x-4 mt-2">
            <button
              className={`w-full p-2 border border-[#845EC2] rounded-md ${
                gender === "남자" ? "bg-[#845EC2] text-white" : "bg-white text-black"
              }`}
              onClick={() => setGender("남자")}
            >
              남자
            </button>
            <button
              className={`w-full p-2 border border-[#845EC2] rounded-md ${
                gender === "여자" ? "bg-[#845EC2] text-white" : "bg-white text-black"
              }`}
              onClick={() => setGender("여자")}
            >
              여자
            </button>
          </div>
          {errors.gender && <p className="text-red-500 text-xs">{errors.gender}</p>}
          </div>

          {/* 회원가입 버튼 */}
          <button className={`w-full p-3 rounded-md text-white mt-4 ${isFormValid ? "bg-[#845EC2]" : "bg-gray-300 cursor-not-allowed"}`} onClick={handleRegister} disabled={!isFormValid}>
            회원가입 하기
          </button>
        </div>
      </div>
    </div>
  );
}