import { useState, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";

export default function UserRegisterForm() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [name, setName] = useState("");
  const [phone, setPhone] = useState("");
  const [authCode, setAuthCode] = useState("");
  const [birthDate, setBirthDate] = useState("");
  const [gender, setGender] = useState(null);
  const [errors, setErrors] = useState({});
  const [isFormValid, setIsFormValid] = useState(false);
  const [isVerified, setIsVerified] = useState(false);
  const [sentCode, setSentCode] = useState(null);
  const [authMessage, setAuthMessage] = useState("");
  const navigate = useNavigate();

  useEffect(() => {
    validateForm();
  }, [email, password, confirmPassword, phone, authCode, birthDate, gender, isVerified]);

  const validateForm = () => {
    let newErrors = {};
    if (!/\S+@\S+\.\S+/.test(email)) newErrors.email = "사용할 수 없는 이메일입니다.";
    if (!/^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d!@#$%^&*]{8,20}$/.test(password)) newErrors.password = "사용할 수 없는 비밀번호입니다.";
    if (password !== confirmPassword) newErrors.confirmPassword = "비밀번호가 일치하지 않습니다.";
    if (!/^\d{10,11}$/.test(phone)) newErrors.phone = "전화번호 형식이 맞지 않습니다.";
    if (!isVerified) newErrors.authCode = "인증을 완료해주세요.";
    if (!/^\d{8}$/.test(birthDate)) newErrors.birthDate = "생년월일 형식이 맞지 않습니다.";
    if (!gender) newErrors.gender = "성별을 선택해주세요.";

    setErrors(newErrors);
    setIsFormValid(Object.keys(newErrors).length === 0);
  };

  // 더미데이터 코드 인증
  const handleSendAuthCode = () => {
    if (phone === "01037552866") {
      setSentCode("000000"); // 더미 인증 코드 저장
      setAuthMessage("인증번호가 전송되었습니다."); // 초록색 메시지 설정
      setErrors((prevErrors) => ({ ...prevErrors, phone: "" })); // 기존 오류 제거
    } else {
      setAuthMessage("");
      alert("해당 번호로 인증 코드를 보낼 수 없습니다.");
    }
  };

  // 인증 확인
  const handleVerifyAuthCode = () => {
    if (authCode === sentCode) {
      setIsVerified(true);
      setAuthMessage("인증이 완료되었습니다."); // 초록색 메시지 설정
      setErrors((prevErrors) => ({ ...prevErrors, authCode: "" })); // 인증 오류 제거
    } else {
      setAuthMessage("");
      alert("잘못된 인증번호입니다.");
    }
  };

  const handleRegister = () => {
    if (isFormValid) {
      navigate("/register/success");
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

        <div className="mt-4 space-y-4">
          {/* 이메일, 비밀번호, 이름 */}
          {[{ label: "이메일", value: email, setValue: setEmail, type: "email", error: errors.email, placeholder: "이메일 입력" },
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

          {/* 전화번호 입력 + 인증번호 전송 버튼 */}
          <div>
            <label className="text-sm text-[#845EC2]">전화번호</label>
            <div className="flex space-x-2">
              <input
                type="tel"
                value={phone}
                onChange={(e) => setPhone(e.target.value)}
                className="flex-1 p-2 border border-[#845EC2] bg-white text-black rounded-md mt-1 focus:outline-none focus:ring-2 focus:ring-[#845EC2]"
                placeholder="전화번호 입력 - 예) 01012345678"
              />
              <button
                className={`px-4 py-2 rounded-md mt-1 ${phone ? "bg-[#845EC2] text-white" : "bg-gray-300 text-gray-500 cursor-not-allowed"}`}
                disabled={!phone}
                onClick={handleSendAuthCode}
              >
                인증 코드 보내기
              </button>
            </div>
            {authMessage && <p className="text-green-500 text-xs mt-1">{authMessage}</p>}
          </div>

          {/* 인증번호 입력 + 인증 확인 버튼 */}
          <div>
            <label className="text-sm text-[#845EC2]">인증번호</label>
            <div className="flex space-x-2">
              <input
                type="text"
                value={authCode}
                onChange={(e) => setAuthCode(e.target.value)}
                className="flex-1 p-2 border border-[#845EC2] bg-white text-black rounded-md mt-1 focus:outline-none focus:ring-2 focus:ring-[#845EC2]"
                placeholder="인증번호 입력"
                disabled={isVerified}
              />
              <button
                className={`px-4 py-2 rounded-md mt-1 ${authCode ? "bg-[#845EC2] text-white" : "bg-gray-300 text-gray-500 cursor-not-allowed"}`}
                disabled={!authCode || isVerified}
                onClick={handleVerifyAuthCode}
              >
                인증 확인
              </button>
            </div>
            {isVerified && <p className="text-green-500 text-xs mt-1">인증이 완료되었습니다.</p>}
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


// const handleRegister = async () => {
//   if (!isFormValid) {
//     alert("모든 필수 항목을 올바르게 입력해주세요.");
//     return;
//   }

//   // 백엔드로 보낼 데이터 구성
//   const requestData = {
//     email,
//     password,
//     name,
//     phone,
//     birthDate,
//     gender
//   };

//   try {
//     const response = await fetch("https://your-api-endpoint.com/register", {
//       method: "POST",
//       headers: {
//         "Content-Type": "application/json"
//       },
//       body: JSON.stringify(requestData)
//     });

//     if (response.ok) {
//       alert("회원가입이 성공적으로 완료되었습니다!");
//       navigate("/register/success"); // 성공 시 이동
//     } else {
//       const errorData = await response.json();
//       alert(`회원가입 실패: ${errorData.message}`);
//     }
//   } catch (error) {
//     console.error("회원가입 중 오류 발생:", error);
//     alert("서버와의 통신 중 문제가 발생했습니다.");
//   }
// };