import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { Star } from "lucide-react";
import apiClient from "../../../../lib/apiClient";
import { COLOR_MAP } from "../../../../constants/colorMap.js";

const baseUrl = import.meta.env.VITE_API_BASE_URL;
const imageBaseUrl = `${baseUrl}`;

function ViewProduct() {
    const { id } = useParams();
    const navigate = useNavigate();
    const [product, setProduct] = useState(null);
    const [selectedImage, setSelectedImage] = useState(null);
    const [categoryCode, setCategoryCode] = useState(null);
    const [isPublishing, setIsPublishing] = useState(false);
    const [isActive, setIsActive] = useState(false);

    // 카테고리를 불러옴
    useEffect(() => {
        apiClient.get("/company/category")
            .then(res => setCategoryCode(res.data))
            .catch(err => console.error("카테고리 불러오기 실패", err));
    }, []);

    // 불러온 카테고리를 변환해서 주소에 넣음
    useEffect(() => {
        if (!categoryCode || !id) return;

        const categoryMap = {
            S: "studio",
            D: "dress",
            M: "makeup"
        };

        const category = categoryMap[categoryCode];

        apiClient.get(`/product/${category}/${id}`)
            .then(res => {
                const data = res.data.data;
                setProduct(data);
                setIsActive(data.status === "ACTIVE" || data.status === "PACKAGE");
                if (data.productImageUrl?.length > 0) {
                    setSelectedImage(`${imageBaseUrl}${data.productImageUrl[0].url}`);
                }
            })
            .catch(err => console.error("상품 상세 정보 불러오기 실패", err));
    }, [categoryCode, id]);

    // 퍼블릭으로 전환하는 함수
    const handlePublish = () => {
        if (!id) return;

        setIsPublishing(true);

        apiClient.post(`/product/upload/${id}`)
            .then(res => {
                alert("상품이 공개 상태로 전환되었습니다.");
                // 상품 정보 다시 불러오기
                const categoryMap = {
                    S: "studio",
                    D: "dress",
                    M: "makeup"
                };
                const category = categoryMap[categoryCode];
                return apiClient.get(`/product/${category}/${id}`);
            })
            .then(res => {
                const updatedProduct = res.data.data;
                setProduct(updatedProduct);
                setIsActive(updatedProduct.status === "ACTIVE");
                setIsPublishing(false);
            })
            .catch(err => {
                console.error("상품 공개 전환 실패", err);
                alert("상품 공개 전환에 실패했습니다.");
                setIsPublishing(false);
            });
    };

    if (!product) return <div className="p-6">로딩 중...</div>;

    // 백엔드에서 받아오는 정보
    const {
        name,
        price,
        rec,
        taskTime,
        inAvailable,
        outAvailable,
        color,
        productImageUrl,
        optionList,
        descriptionList,
        overlap,
        essential,
        // 스튜디오 전용 필드
        maxPeople,
        bgOptions,
        // 메이크업 전용 필드
        visit,
        manager,
        business_trip
    } = product;
    const isDress = categoryCode === "D";
    const isStudio = categoryCode === "S";
    const isMakeup = categoryCode === "M";
    const showOverlap = isDress ? overlap : null;
    const showEssential = isDress ? essential : null;

    // 상세정보 추출
    const sortedDescription = descriptionList
        .sort((a, b) => a.order - b.order) 
        .map(item => item.value)

    // plusTime(plus_time) HH:mm:ss → 사람이 읽기 쉬운 시간 문자열로 변환
    function formatLocalTime(time) {
        // 문자열(HH:mm:ss) 처리
        if (typeof time === "string" && time.includes(":")) {
            const [h, m] = time.split(":");
            const hours = parseInt(h, 10);
            const minutes = parseInt(m, 10);
            let result = "";
            if (hours > 0) result += `${hours}시간`;
            if (minutes > 0) result += (result ? " " : "") + `${minutes}분`;
            if (!result) result = "0분";
            return result;
        }
        // 배열([시, 분]) 처리
        if (Array.isArray(time)) {
            const [hours, minutes] = time;
            let result = "";
            if (hours > 0) result += `${hours}시간`;
            if (minutes > 0) result += (result ? " " : "") + `${minutes}분`;
            if (!result) result = "0분";
            return result;
        }
        return "";
    }

    return (
        <div className="w-full max-w-6xl mx-auto p-6 bg-white text-black rounded-md">
            <div className="flex justify-between items-center mb-4">
                <h1 className="text-2xl font-bold text-[#845EC2]">상품 상세 보기</h1>
                <button
                    onClick={() => navigate(`/company/product/edit/${id}`)}
                    className={`px-4 py-2 rounded ${
                        product.status === "PACKAGE" 
                        ? "bg-gray-400 cursor-not-allowed" 
                        : "bg-[#845EC2] hover:bg-purple-500"
                    } text-white`}
                    disabled={product.status === "PACKAGE"}
                >
                    수정
                </button>
            </div>

            <div className="flex items-start space-x-6">
                <div className="w-1/3 p-2">
                    <div className="border rounded-lg overflow-hidden bg-gray-100 w-full h-96 aspect-[3/4]">
                        {selectedImage ? (
                            <img src={selectedImage} alt="대표 이미지" className="w-full h-full object-cover" />
                        ) : (
                            <div className="w-full h-full flex items-center justify-center text-gray-400">No image</div>
                        )}
                    </div>

                    <div className="mt-4 flex space-x-2 overflow-x-auto max-w-full p-1">
                        {productImageUrl?.map((img, index) => {
                            const fullUrl = `${imageBaseUrl}${img.url}`;
                            return (
                                <img
                                    key={index}
                                    src={fullUrl}
                                    alt={`업로드된 이미지 ${index}`}
                                    onClick={() => setSelectedImage(fullUrl)}
                                    className={`w-16 h-16 object-cover rounded-md cursor-pointer flex-shrink-0 border 
                                        ${selectedImage === fullUrl ? "border-[#845EC2] border-2" : "border-gray-300"}`}
                                />
                            );
                        })}
                    </div>

                    <div className="bg-white rounded-md mt-4">
                        {/* 공통 필드: 실내/야외 촬영 가능 여부 */}
                        <div className="flex items-center space-x-6 mb-4">
                            <label className="flex items-center space-x-2 cursor-default">
                                <input
                                    type="checkbox"
                                    checked={isMakeup ? (business_trip === "Y") : (inAvailable === "Y")}
                                    readOnly
                                    className="w-5 h-5 rounded border-2 border-[#845EC2] appearance-none cursor-default
                                    bg-white checked:bg-[#845EC2] checked:border-[#845EC2]
                                    checked:after:content-['✓'] checked:after:text-white checked:after:text-sm
                                    checked:after:font-bold checked:after:block checked:after:text-center
                                    checked:after:leading-[18px]"
                                />
                                <span className="text-black">
                                    {isMakeup ? "출장" : "실내촬영가능"}
                                </span>
                            </label>

                            <label className="flex items-center space-x-2 cursor-default">
                                <input
                                    type="checkbox"
                                    checked={outAvailable === "Y" || visit === "Y"}
                                    readOnly
                                    className="w-5 h-5 rounded border-2 border-[#845EC2] appearance-none cursor-default
                                    bg-white checked:bg-[#845EC2] checked:border-[#845EC2]
                                    checked:after:content-['✓'] checked:after:text-white checked:after:text-sm
                                    checked:after:font-bold checked:after:block checked:after:text-center
                                    checked:after:leading-[18px]"
                                />
                                <span className="text-black">
                                    {isMakeup ? "방문" : "야외촬영가능"}
                                </span>
                            </label>
                        </div>
                    </div>
                </div>

                <div className="w-2/3 p-2">
                    <div className="flex items-center justify-between mb-2">
                        <div className="flex items-center space-x-2">
                            <h2 className="text-lg font-semibold">상품정보</h2>
                            <Star
                                className={`w-6 h-6 ${rec === "Y" ? "text-purple-600 fill-current" : "text-purple-400"}`}
                                fill={rec === "Y" ? "currentColor" : "none"}
                            />
                        </div>
                        <button
                            onClick={handlePublish}
                            disabled={isPublishing || isActive}
                            className="bg-green-500 text-white px-3 py-1 rounded hover:bg-green-600 disabled:bg-gray-400 disabled:cursor-not-allowed"
                        >
                            {isPublishing ? "처리 중..." : isActive ? "판매중" : "판매 시작"}
                        </button>
                    </div>

                    <div className="space-y-2">
                        <div className="flex items-center">
                            <label className="w-24">상품명</label>
                            <input value={name} disabled className="flex-grow border p-2 rounded bg-white text-black" />
                        </div>
                        <div className="flex items-center">
                            <label className="w-24">가격</label>
                            <input value={`${price.toLocaleString()}원`} disabled className="flex-grow border p-2 rounded bg-white text-black" />
                        </div>
                        {isDress && (
                            <div className="flex items-center">
                                <label className="w-24">색상</label>
                                <select value={color} disabled className="flex-grow border p-2 rounded bg-white text-black appearance-none">
                                    {Object.entries(COLOR_MAP).map(([eng, kor]) => (
                                        <option key={eng} value={eng}>
                                            {kor}
                                        </option>
                                    ))}
                                </select>
                                <div className="ml-2 w-24 h-10 rounded" style={{ backgroundColor: color, border: '1px solid #ccc' }} />
                            </div>
                        )}
                        <div className="flex items-center">
                            <label className="w-24">대여시간</label>
                            <select value={taskTime} disabled className="border p-2 rounded flex-grow bg-white text-black appearance-none">
                                <option value="00:30:00">30분 대여</option>
                                <option value="01:00:00">1시간 대여</option>
                                <option value="01:30:00">1시간 30분 대여</option>
                                <option value="02:00:00">2시간 대여</option>
                                <option value="02:30:00">2시간 30분 대여</option>
                                <option value="03:00:00">3시간 대여</option>
                            </select>
                        </div>
                        {/* 메이크업 담당자 */}
                        {isMakeup && (
                            <div className="flex items-center mt-2">
                                <label className="w-24">담당자</label>
                                <input 
                                    type="text" 
                                    value={manager || "-"}
                                    disabled
                                    className="flex-grow border p-2 rounded bg-white text-black"
                                />
                            </div>
                        )}
                        {/* 스튜디오 전용 필드 */}
                        {isStudio && (
                            <div className="space-y-4">
                                <div className="flex items-center">
                                    <label className="w-32">최대수용인원</label>
                                    <input 
                                        type="text" 
                                        value={maxPeople}
                                        disabled
                                        className="flex-grow border p-2 rounded bg-white text-black"
                                    />
                                </div>
                                <div className="flex items-center">
                                    <label className="w-32">배경선택여부</label>
                                    <select
                                        value={bgOptions}
                                        disabled
                                        className="flex-grow border p-2 rounded bg-white text-black appearance-none"
                                    >
                                        <option value="Y">가능</option>
                                        <option value="N">불가능</option>
                                    </select>
                                </div>
                            </div>
                        )}
                    </div>

                    {/* 드레스 사이즈 리스트 출력 */}
                    {product.sizeList?.length > 0 && (
                        <div className="mt-6">
                            <h3 className="font-semibold mb-2">드레스 사이즈 리스트</h3>
                            <div className="flex items-center space-x-4">
                                <label className="flex items-center space-x-1">
                                    <input type="checkbox" checked={showOverlap === "Y"} readOnly className="w-5 h-5 rounded border-2 border-[#845EC2] appearance-none cursor-pointer
                                        bg-white checked:bg-[#845EC2] checked:border-[#845EC2]
                                        checked:after:content-['✓'] checked:after:text-white checked:after:text-sm
                                        checked:after:font-bold checked:after:block checked:after:text-center
                                        checked:after:leading-[18px]" />
                                    <span>중복 선택</span>
                                </label>
                                <label className="flex items-center space-x-1">
                                    <input type="checkbox" checked={showEssential === "Y"} readOnly className="w-5 h-5 rounded border-2 border-[#845EC2] appearance-none cursor-pointer
                                        bg-white checked:bg-[#845EC2] checked:border-[#845EC2]
                                        checked:after:content-['✓'] checked:after:text-white checked:after:text-sm
                                        checked:after:font-bold checked:after:block checked:after:text-center
                                        checked:after:leading-[18px]" />
                                    <span>필수 선택</span>
                                </label>
                            </div>
                            <div className="space-y-2">
                                {product.sizeList.map((sizeItem, idx) => (
                                    <div key={idx} className="flex space-x-4 bg-white border p-2 rounded">
                                        <input
                                            type="text"
                                            value={`사이즈: ${sizeItem.size}`}
                                            disabled
                                            className="border p-2 rounded bg-white text-black flex-1"
                                        />
                                        <input
                                            type="text"
                                            value={`재고: ${sizeItem.stock}`}
                                            disabled
                                            className="border p-2 rounded bg-white text-black flex-1"
                                        />
                                        <input
                                            type="text"
                                            value={`추가금액: ${sizeItem.plusCost}원`}
                                            disabled
                                            className="border p-2 rounded bg-white text-black flex-1"
                                        />
                                    </div>
                                ))}
                            </div>
                        </div>
                    )}
                    {/* 옵션 정보 */}
                    {optionList?.length > 0 && (
                        <div>
                            <h3 className="font-semibold mt-4">옵션 정보</h3>
                            {optionList.map((opt, i) => (
                                <div key={i} className="border rounded p-4 mt-2 bg-white">
                                    <div className="flex justify-between items-center mb-2">
                                        <input type="text" value={opt.name} disabled className="border p-2 rounded bg-white text-black" />
                                        <div className="flex items-center space-x-4">
                                            <label className="flex items-center space-x-1">
                                                <input type="checkbox" checked={opt.overlap === "Y"} readOnly className="w-5 h-5 rounded border-2 border-[#845EC2] appearance-none cursor-pointer
                                                    bg-white checked:bg-[#845EC2] checked:border-[#845EC2]
                                                    checked:after:content-['✓'] checked:after:text-white checked:after:text-sm
                                                    checked:after:font-bold checked:after:block checked:after:text-center
                                                    checked:after:leading-[18px]" />
                                                <span>중복 선택</span>
                                            </label>
                                            <label className="flex items-center space-x-1">
                                                <input type="checkbox" checked={opt.essential === "Y"} readOnly className="w-5 h-5 rounded border-2 border-[#845EC2] appearance-none cursor-pointer
                                                    bg-white checked:bg-[#845EC2] checked:border-[#845EC2]
                                                    checked:after:content-['✓'] checked:after:text-white checked:after:text-sm
                                                    checked:after:font-bold checked:after:block checked:after:text-center
                                                    checked:after:leading-[18px]" />
                                                <span>필수 선택</span>
                                            </label>
                                        </div>
                                    </div>
                                    {opt.optionDtList.map((dt, j) => (
                                        <div key={j} className="flex space-x-2 mb-2 bg-white p-2 rounded">
                                            <input type="text" value={dt.opDtName} disabled className="border p-2 rounded bg-white text-black" />
                                            <input type="text" value={formatLocalTime(dt.plusTime)} disabled className="border p-2 rounded bg-white text-black" />
                                            <input type="text" value={`${dt.plusCost}원`} disabled className="border p-2 rounded bg-white text-black" />
                                        </div>
                                    ))}
                                </div>
                            ))}
                        </div>
                    )}
                </div>
            </div>

            <div className="mt-6">
            <h2 className="text-lg font-semibold mb-2">상품 상세 설명</h2>
                <div className="prose max-w-none bg-white border rounded p-4 space-y-4">
                    {descriptionList
                        .sort((a, b) => a.order - b.order)
                        .map((item, idx) => {
                        if (item.type === "TEXT") {
                            return (
                            <p key={idx} className="whitespace-pre-wrap">
                                {item.value}
                            </p>
                            );
                        } else if (item.type === "IMAGE") {
                            const imageUrl = item.value.startsWith("http")
                            ? item.value
                            : `${imageBaseUrl.replace(/\/$/, "")}${item.value.replaceAll("##", "/")}`;
                            return (
                            <img
                                key={idx}
                                src={imageUrl}
                                alt={`이미지-${idx}`}
                                className="max-w-xs mx-auto block"
                            />
                            );
                        } else if (item.type === "YOUTUBE") {
                            return (
                            <div key={idx} className="youtube-wrapper">
                                <iframe
                                src={item.value}
                                width="560"
                                height="315"
                                title={`유튜브-${idx}`}
                                allowFullScreen
                                className="mx-auto"
                                />
                            </div>
                            );
                        } else {
                            return null;
                        }
                    })}
                </div>
            </div>
        </div>
    );
}

export default ViewProduct;