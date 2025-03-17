import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import SocialLogin from "./SocialLogin";

const logindata = [
    { email: "user@example.com", password: "user1234", type: "user" },
    { email: "company@example.com", password: "company1234", type: "company" }
];

export default function LoginPage() {
    const [userType, setUserType] = useState("user");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [errorMessage, setErrorMessage] = useState("");
    const [showPopup, setShowPopup] = useState(false);
    const [rememberMe, setRememberMe] = useState(false);
    const navigate = useNavigate();

    const handleLogin = () => {
        if (!email) {
            setErrorMessage("아이디를 입력하시오");
            setShowPopup(true);
            return;
        }
        if (!password) {
            setErrorMessage("비밀번호를 입력하시오");
            setShowPopup(true);
            return;
        }

        const user = logindata.find(u => u.email === email && u.password === password && u.type === userType);
        if (!user) {
            setErrorMessage("아이디 또는 비밀번호가 올바르지 않습니다");
            setShowPopup(true);
            return;
        }

        navigate(user.type === "user" ? "/" : "/company");
    };

    return (
        <div className="flex justify-center items-center min-h-screen bg-gray-100">
            <div className="w-[900px] p-10 bg-white shadow-lg rounded-2xl">
                <Link to="/" className="block text-center">
                    <h1 className="text-5xl font-inknut font-bold text-[#845ec2]">Lächeln</h1>
                    <p className="text-[#845ec2] mb-6">스튜디오 드레스 메이크업</p>
                </Link>

                {showPopup && (
                    <div className="fixed inset-0 flex justify-center items-center bg-black bg-opacity-50">
                        <div className="bg-white p-10 rounded-lg shadow-lg w-80 text-center">
                            <p className="text-black font-bold">{errorMessage}</p>
                            <button
                                onClick={() => setShowPopup(false)}
                                className="mt-8 px-6 py-2 bg-[#845EC2] text-white rounded-lg hover:bg-purple-800 transition"
                            >
                                확인
                            </button>
                        </div>
                    </div>
                )}

                {/* 사용자/업체 선택 */}
                <div className="flex mb-6">
                    <button
                        className={`flex-1 py-3 text-lg font-semibold transition border-2 border-transparent hover:border-[#845EC2] 
                    focus:outline-none focus:ring-0 ${
                            userType === "user"
                                ? "bg-[#845EC2] text-white"
                                : "bg-white text-[#845EC2]"
                        }`}
                        onClick={() => setUserType("user")}
                    >
                        회원
                    </button>
                    <button
                        className={`flex-1 py-3 text-lg font-semibold transition border-2 border-transparent hover:border-[#845EC2] 
                    focus:outline-none focus:ring-0 ${
                            userType === "company"
                                ? "bg-[#845EC2] text-white"
                                : "bg-white text-[#845EC2]"
                        }`}
                        onClick={() => setUserType("company")}
                    >
                        업체
                    </button>
                </div>

                {/* 입력 폼 */}
                <form className="space-y-4">
                    <div>
                        <label className="block text-gray-700 font-semibold">이메일</label>
                        <input
                            type="email"
                            placeholder="Email"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            className="w-full px-4 py-3 border-2 border-[#845EC2] rounded-lg bg-white text-black focus:ring-1 focus:ring-purple-400 outline-none"
                        />
                    </div>
                    <div>
                        <label className="block text-gray-700 font-semibold">비밀번호</label>
                        <input
                            type="password"
                            placeholder="Password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            className="w-full px-4 py-3 border-2 border-[#845EC2] rounded-lg bg-white text-black focus:ring-1 focus:ring-purple-400 outline-none"
                        />
                    </div>

                    {/* 아이디 저장 & 비밀번호 찾기 */}
                    <div className="flex justify-between items-center text-[#845EC2] text-sm">
                        <label className="flex items-center space-x-2">
                            <input type="checkbox" checked={rememberMe} onChange={() => setRememberMe(!rememberMe)} className="appearance-none w-5 h-5 border border-[#845EC2] rounded-sm bg-white checked:bg-[#845EC2] checked:border-transparent checked:after:content-['✔'] checked:after:text-white checked:after:text-xs checked:after:flex checked:after:justify-center checked:after:items-center cursor-pointer" />
                            <span>아이디/비밀번호 저장</span>
                        </label>
                        <a href="/find-password" className="text-[#845EC2] hover:underline">비밀번호를 잊으셨습니까?</a>
                    </div>

                    {/* 로그인 버튼 */}
                    <button
                        type="button"
                        onClick={handleLogin}
                        className="w-full py-3 bg-[#845ec2] text-white rounded-lg hover:bg-purple-800 transition"
                    >
                        로그인
                    </button>
                </form>

                {/* 회원가입 userType에 따라 다른 경로로 이동 */}
                <div className="mt-6 text-center">
                    <Link
                        to={userType === "user" ? "/register/user" : "/register/company"}
                        className="text-[#845EC2] font-semibold hover:underline"
                    >
                        아직 계정이 없으신가요?
                    </Link>
                </div>

                {/* 소셜 로그인 (고정 높이 유지) */}
                <div className="mt-6 text-center" style={{ minHeight: "120px" }}>
                    {userType === "user" ? <SocialLogin /> : <div className="h-16 opacity-0">소셜 로그인 자리</div>}
                </div>
            </div>
        </div>
    );
}
