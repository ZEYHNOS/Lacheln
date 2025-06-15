import React from "react";

function ProfileModify({
    company,
    setCompany,
    isDragging,
    setIsDragging,
    pendingImageFile,
    setPendingImageFile,
    previewImageUrl,
    setPreviewImageUrl,
    baseUrl,
    Addphoto,
    apiClient
}) {
    if (!company) return null;

    const handleProfileChange = async () => {
        let imageUrl = company.profileImageUrl;

        if (pendingImageFile) {
            const formData = new FormData();
            formData.append("images", pendingImageFile);
            try {
                const res = await apiClient.post("/product/image/upload", formData, {
                    headers: { "Content-Type": "multipart/form-data" }
                });
                if (res.data?.data?.[0]) {
                    imageUrl = res.data.data[0];
                    setPreviewImageUrl("");
                }
            } catch {
                alert("이미지 업로드 실패");
                return;
            }
        }

        try {
            await apiClient.put(`/company/info/${company.id}`, {
                address: company.address,
                profileImageUrl: imageUrl
            });
            alert("변경 완료!");
            setPendingImageFile(null);
        } catch {
            alert("변경 실패");
        }
    };

    return (
        <div className="max-w-4xl mx-auto bg-white rounded-lg p-8 flex gap-8">
            {/* 왼쪽: 이미지 업로드/미리보기 */}
            <div className="w-1/3 flex flex-col items-center">
                <div className="border rounded-lg overflow-hidden bg-gray-100 w-full h-96 aspect-[3/4] flex items-center justify-center">
                    {previewImageUrl || company?.profileImageUrl ? (
                        <img
                            src={
                                previewImageUrl
                                    ? previewImageUrl
                                    : company.profileImageUrl.startsWith("http")
                                        ? company.profileImageUrl
                                        : `${baseUrl}${company.profileImageUrl.replace(/\\/g, "/")}`
                            }
                            alt="대표 이미지"
                            className="w-full h-full object-cover"
                        />
                    ) : (
                        <div className="w-full h-full flex items-center justify-center text-gray-400">
                            No image
                        </div>
                    )}
                </div>
                <label
                    className={`mt-2 block w-full text-center border ${isDragging
                            ? "border-dashed border-2 border-[#845EC2]"
                            : "border border-[#845EC2]"
                        } text-[#845EC2] rounded-md py-4 cursor-pointer bg-white flex items-center justify-center space-x-2 w-full h-16`}
                    onDragOver={(e) => {
                        e.preventDefault();
                        setIsDragging(true);
                    }}
                    onDragLeave={() => setIsDragging(false)}
                    onDrop={(e) => {
                        e.preventDefault();
                        setIsDragging(false);
                        const file = e.dataTransfer.files[0];
                        if (!file) return;
                        setPendingImageFile(file);
                        setPreviewImageUrl(URL.createObjectURL(file));
                    }}
                >
                    <img src={Addphoto} alt="이미지 추가" className="w-6 h-6" />
                    <span className="text-[#845EC2] font-medium">이미지 추가하기</span>
                    <input
                        type="file"
                        accept="image/*"
                        className="hidden"
                        onChange={(e) => {
                            const file = e.target.files[0];
                            if (!file) return;
                            setPendingImageFile(file);
                            setPreviewImageUrl(URL.createObjectURL(file));
                        }}
                    />
                </label>
            </div>

            {/* 오른쪽: 정보 입력/표시 */}
            <div className="w-2/3 flex flex-col justify-center">
                <div className="w-full space-y-2">
                    <div className="flex items-center">
                        <span className="w-32">이름</span>
                        <input
                            className="flex-1 border rounded p-2 bg-gray-100 text-gray-400"
                            value={company?.name || ""}
                            readOnly
                        />
                    </div>
                    <div className="flex items-center">
                        <span className="w-32">이메일</span>
                        <input
                            className="flex-1 border rounded p-2 bg-gray-100 text-gray-400"
                            value={company?.email || ""}
                            readOnly
                        />
                    </div>
                    <div className="flex items-center">
                        <span className="w-32">연락처</span>
                        <input
                            className="flex-1 border rounded p-2 bg-gray-100 text-gray-400"
                            value={company?.contact || ""}
                            readOnly
                        />
                    </div>
                    <div className="flex items-center">
                        <span className="w-32">주소</span>
                        <input
                            className="flex-1 border rounded p-2 bg-blue-200"
                            value={company?.address || ""}
                            onChange={(e) =>
                                setCompany((prev) => ({ ...prev, address: e.target.value }))
                            }
                        />
                    </div>
                    <div className="flex items-center">
                        <span className="w-32">우편번호</span>
                        <input
                            className="flex-1 border rounded p-2 bg-gray-100 text-gray-400"
                            value={company?.postalCode || ""}
                            readOnly
                        />
                    </div>
                    <div className="flex items-center">
                        <span className="w-32">사업자등록번호</span>
                        <input
                            className="flex-1 border rounded p-2 bg-gray-100 text-gray-400"
                            value={company?.bnRegNo || ""}
                            readOnly
                        />
                    </div>
                    <div className="flex items-center">
                        <span className="w-32">통신판매업신고번호</span>
                        <input
                            className="flex-1 border rounded p-2 bg-gray-100 text-gray-400"
                            value={company?.mos || ""}
                            readOnly
                        />
                    </div>
                    <div className="flex items-center">
                        <span className="w-32">카테고리</span>
                        <input
                            className="flex-1 border rounded p-2 bg-gray-100 text-gray-400"
                            value={company?.category || ""}
                            readOnly
                        />
                    </div>
                </div>
                <button
                    className="mt-6 w-full bg-[#845EC2] text-white py-3 rounded font-bold"
                    onClick={handleProfileChange}
                >
                    변경
                </button>
            </div>
        </div>
    );
}

export default ProfileModify;