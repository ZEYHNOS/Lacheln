import React, { useEffect, useRef } from "react";
import { useLocation } from "react-router-dom";
import { toast } from "react-toastify";

function MainCompany() {
    const location = useLocation();
    const hasShownToast = useRef(false);

    useEffect(() => {
        // 이미 토스트를 표시했다면 더 이상 실행하지 않음
        if (hasShownToast.current) return;

        // 로컬 로그인 시도 여부 확인
        const localLoginSuccess = sessionStorage.getItem('localLoginSuccess');
        const userType = sessionStorage.getItem('userType');

        if (localLoginSuccess === 'true' && userType === "COMPANY") {
            // 로그인 상태 확인
            fetch(`${import.meta.env.VITE_API_BASE_URL}/auth/me`, {
                credentials: 'include'
            })
            .then(res => res.json())
            .then(data => {
                if (data.data?.valid && !hasShownToast.current) {
                    toast.success("업체 로그인이 완료되었습니다!", {
                        position: "top-center",
                        autoClose: 1000,
                    });
                    hasShownToast.current = true;
                    // 상태 제거
                    sessionStorage.removeItem('localLoginSuccess');
                    sessionStorage.removeItem('userType');
                }
            })
            .catch(error => console.error("로그인 상태 확인 중 오류:", error));
        }
    }, []);

    return (
        <div className="text-black">
            <h1>회사 메인 페이지</h1>
        </div>
    );
}

export default MainCompany; 