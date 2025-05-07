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
                if (data.productImageUrl?.length > 0) {
                    setSelectedImage(`${imageBaseUrl}${data.productImageUrl[0].url}`);
                }
            })
            .catch(err => console.error("상품 상세 정보 불러오기 실패", err));
    }, [categoryCode, id]);

    if (!product) return <div className="p-6">로딩 중...</div>;

    // 백엔드에서 받아오는 정보
    const {
        name,
        price,
        status,
        rec,
        taskTime,
        inAvailable,
        outAvailable,
        color,
        productImageUrl,
        optionList,
        descriptionList,
        overlap,
        essential
    } = product;
    const isDress = categoryCode === "D";
    const showOverlap = isDress ? overlap : null;
    const showEssential = isDress ? essential : null;

    // 상세정보 추출
    const sortedDescription = descriptionList
        .sort((a, b) => a.order - b.order) 
        .map(item => item.value)


    return (
        <div className="w-full max-w-6xl mx-auto p-6 bg-white text-black rounded-md">
            <div className="flex justify-between items-center mb-4">
                <h1 className="text-2xl font-bold text-[#845EC2]">상품 상세 보기</h1>
                <button
                    onClick={() => navigate(`/company/product/edit/${id}`)}
                    className="bg-[#845EC2] text-white px-4 py-2 rounded hover:bg-purple-500"
                >
                    수정
                </button>
            </div>

            <div className="flex items-start space-x-6">
                <div className="w-full lg:w-1/2">
                    <div className="border rounded-lg overflow-hidden bg-gray-100 w-full h-96">
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

                    <div className="bg-white rounded-md flex items-center space-x-6 mt-4">
                        <label className="flex items-center space-x-2 cursor-default">
                            <input
                                type="checkbox"
                                checked={inAvailable === "Y"}
                                readOnly
                                className="w-5 h-5 rounded border-2 border-[#845EC2] appearance-none cursor-default
                                bg-white checked:bg-[#845EC2] checked:border-[#845EC2]
                                checked:after:content-['✓'] checked:after:text-white checked:after:text-sm
                                checked:after:font-bold checked:after:block checked:after:text-center
                                checked:after:leading-[18px]"
                            />
                            <span className="text-black">실내촬영가능</span>
                        </label>

                        <label className="flex items-center space-x-2 cursor-default">
                            <input
                                type="checkbox"
                                checked={outAvailable === "Y"}
                                readOnly
                                className="w-5 h-5 rounded border-2 border-[#845EC2] appearance-none cursor-default
                                bg-white checked:bg-[#845EC2] checked:border-[#845EC2]
                                checked:after:content-['✓'] checked:after:text-white checked:after:text-sm
                                checked:after:font-bold checked:after:block checked:after:text-center
                                checked:after:leading-[18px]"
                            />
                            <span className="text-black">야외촬영가능</span>
                        </label>
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
                        <select value={status} disabled className="border p-1 rounded bg-white text-black appearance-none">
                            <option value="ACTIVE">공개</option>
                            <option value="INACTIVE">비공개</option>
                            <option value="PACKAGE">패키지전용</option>
                        </select>
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
                        <div className="flex items-center">
                            <label className="w-24">대여시간</label>
                            <select value={taskTime} disabled className="border p-2 rounded flex-grow bg-white text-black appearance-none">
                                <option value="30">30분 대여</option>
                                <option value="60">1시간 대여</option>
                                <option value="90">1시간 30분 대여</option>
                                <option value="120">2시간 대여</option>
                                <option value="150">2시간 30분 대여</option>
                                <option value="180">3시간 대여</option>
                            </select>
                        </div>
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
                                            <input type="text" value={`${dt.plusTime}분`} disabled className="border p-2 rounded bg-white text-black" />
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