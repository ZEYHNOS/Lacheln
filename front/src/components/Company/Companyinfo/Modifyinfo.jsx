import React, { useState, useEffect } from "react";
import apiClient from "../../../lib/apiClient";
import Addphoto from "../../../image/Company/addimage.png";
import ProfileModify from "./ModifyPage/ProfileModify";
import PasswordModify from "./ModifyPage/PasswordModify";
import OfficehoursModify from "./ModifyPage/OfficehoursModify";

const baseUrl = import.meta.env.VITE_API_BASE_URL;

function Modifyinfo() {
    const [tab, setTab] = useState("profile");
    const [company, setCompany] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [isDragging, setIsDragging] = useState(false);
    const [pendingImageFile, setPendingImageFile] = useState(null);
    const [previewImageUrl, setPreviewImageUrl] = useState("");

    useEffect(() => {
        const fetchCompany = async () => {
            try {
                setLoading(true);
                // 회사 ID 받아오기
                const companyIdRes = await apiClient.get("/company/me");
                const companyId = companyIdRes.data;
                // 회사 정보 받아오기
                const res = await apiClient.get(`/company/info/${companyId}`);
                setCompany(res.data.data);
            } catch (err) {
                setError("회사 정보를 불러오지 못했습니다.");
            } finally {
                setLoading(false);
            }
        };
        fetchCompany();
    }, []);

    if (loading) return <div className="text-center p-4">로딩중...</div>;
    if (error) return <div className="text-red-500 p-4">{error}</div>;

    // 이미지 URL 처리
    const imageUrl = company?.profileImageUrl
        ? company.profileImageUrl.startsWith("http")
            ? company.profileImageUrl
            : `${baseUrl}${company.profileImageUrl.replace(/\\/g, "/")}`
        : "/default/images/company.png";

    // 탭 버튼 스타일 클래스
    const tabBtn =
        "font-bold bg-white text-base pb-1 border-b-2 transition-all duration-150 text-[#845EC2] focus:outline-none focus:ring-0 active:outline-none active:ring-0";
    const tabBtnActive = "border-[#845EC2]";
    const tabBtnInactive = "border-transparent";

    // 이미지 업로드 핸들러
    const handleProfileImageUpload = async (file) => {
        if (!file) return;
        // 미리보기
        setCompany((prev) => ({ ...prev, profileImageUrl: URL.createObjectURL(file) }));
        // 서버 업로드
        const formData = new FormData();
        formData.append("images", file);
        try {
            const res = await apiClient.post("/company/profile/upload", formData, {
                headers: { "Content-Type": "multipart/form-data" }
            });
            if (res.data?.data?.[0]) {
                setCompany((prev) => ({ ...prev, profileImageUrl: res.data.data[0] }));
            }
        } catch {
            alert("이미지 업로드 실패");
        }
    };

    return (
        <div className="text-black w-full bg-white">
            {/* 상단 탭 버튼 */}
            <div className="flex space-x-4 items-center px-2 pt-2 bg-white">
                <button
                    className={`px-4 py-2 rounded-none bg-transparent border-none shadow-none outline-none
                        ${tab === "profile" ? "text-purple-700 font-bold" : "text-purple-300"}
                        hover:bg-transparent focus:outline-none focus:ring-0 active:outline-none active:ring-0 focus-visible:outline-none focus-visible:ring-0`}
                    style={{ border: 'none', boxShadow: 'none' }}
                    onClick={() => setTab("profile")}
                    tabIndex={0}
                >
                    프로필설정
                </button>
                <button
                    className={`px-4 py-2 rounded-none bg-transparent border-none shadow-none outline-none
                        ${tab === "security" ? "text-purple-700 font-bold" : "text-purple-300"}
                        hover:bg-transparent focus:outline-none focus:ring-0 active:outline-none active:ring-0 focus-visible:outline-none focus-visible:ring-0`}
                    style={{ border: 'none', boxShadow: 'none' }}
                    onClick={() => setTab("security")}
                    tabIndex={0}
                >
                    비밀번호변경
                </button>
                <button
                    className={`px-4 py-2 rounded-none bg-transparent border-none shadow-none outline-none
                        ${tab === "work" ? "text-purple-700 font-bold" : "text-purple-300"}
                        hover:bg-transparent focus:outline-none focus:ring-0 active:outline-none active:ring-0 focus-visible:outline-none focus-visible:ring-0`}
                    style={{ border: 'none', boxShadow: 'none' }}
                    onClick={() => setTab("work")}
                    tabIndex={0}
                >
                    업무시간설정
                </button>
            </div>

            {/* 탭별 내용 */}
            <div className="mt-6">
                {tab === "profile" && (
                    <ProfileModify
                        company={company}
                        setCompany={setCompany}
                        isDragging={isDragging}
                        setIsDragging={setIsDragging}
                        pendingImageFile={pendingImageFile}
                        setPendingImageFile={setPendingImageFile}
                        previewImageUrl={previewImageUrl}
                        setPreviewImageUrl={setPreviewImageUrl}
                        baseUrl={baseUrl}
                        Addphoto={Addphoto}
                        apiClient={apiClient}
                    />
                )}
                {tab === "security" && (
                    <PasswordModify/>
                )}
                {tab === "work" && (
                    <OfficehoursModify/>
                )}
            </div>
        </div>
    );
}

export default Modifyinfo; 