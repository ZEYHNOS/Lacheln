import React, { useEffect, useRef } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { toast } from "react-toastify";

function MainPage() {
    const location = useLocation();
    const navigate = useNavigate();
    const hasShownToast = useRef(false);

    useEffect(() => {
        // 이미 토스트를 표시했다면 더 이상 실행하지 않음
        if (hasShownToast.current) return;

        // 소셜 로그인 또는 로컬 로그인 시도 여부 확인
        const socialLoginAttempt = sessionStorage.getItem('socialLoginAttempt');
        const localLoginSuccess = sessionStorage.getItem('localLoginSuccess');
        const userType = sessionStorage.getItem('userType');

        if (socialLoginAttempt === 'true' || localLoginSuccess === 'true') {
            // 로그인 상태 확인
            fetch(`${import.meta.env.VITE_API_BASE_URL}/auth/me`, {
                credentials: 'include'
            })
            .then(res => res.json())
            .then(data => {
                if (data.data?.valid && !hasShownToast.current) {
                    // 유저 타입에 따라 다른 메시지 표시
                    const message = userType === "COMPANY" ? "업체 로그인이 완료되었습니다!" : "로그인이 완료되었습니다!";
                    toast.success(message, {
                        position: "top-center",
                        autoClose: 1000,
                        onClose: () => {
                            // 토스트가 닫힌 후 업체인 경우 회사 페이지로 이동
                            if (userType === "COMPANY") {
                                navigate('/company');
                            }
                        }
                    });
                    hasShownToast.current = true;
                    // 상태 제거
                    sessionStorage.removeItem('socialLoginAttempt');
                    sessionStorage.removeItem('localLoginSuccess');
                    sessionStorage.removeItem('userType');
                }
            })
            .catch(error => console.error("로그인 상태 확인 중 오류:", error));
        }
    }, [navigate]);

    return (
        <div className="min-h-screen bg-white text-black flex flex-col items-center justify-center">
            <h1 className="mb-4 block">메인 페이지</h1>
            <p className="block">현재 개발중에 있습니다.</p>
        </div>
    );
}

export default MainPage;
