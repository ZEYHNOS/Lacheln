import React, { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import apiClient from "../../../lib/apiClient";
import { COLOR_MAP } from "../../../constants/colorMap.js";

const baseUrl = import.meta.env.VITE_API_BASE_URL;

// 카테고리 코드 매핑
const categoryCodes = {
    "드레스": "D",
    "스튜디오": "S",
    "메이크업": "M"
};

// 탭용 한글 → 백엔드 코드
const tabCategoryCodeMap = {
    "드레스": "D",
    "스튜디오": "S",
    "메이크업": "M",
};

// 상태 코드 매핑
const statusCodeMap = {
    ACTIVE: "공개",
    INACTIVE: "비공개",
    SETTING: "설정중",
    REMOVE: "삭제됨",
    PACKAGE: "패키지"
};

const categoryMap = { "드레스": "dress", "스튜디오": "studio", "메이크업": "makeup" };

function Product() {
    const [selected, setSelected] = useState("드레스");
    const [selectedColor, setSelectedColor] = useState(null);
    const [currentPage, setCurrentPage] = useState(1);
    const [productList, setProductList] = useState([]);
    const [minPriceInput, setMinPriceInput] = useState("");
    const [maxPriceInput, setMaxPriceInput] = useState("");
    const [minPrice, setMinPrice] = useState("");
    const [maxPrice, setMaxPrice] = useState("");
    const [pagination, setPagination] = useState({
        curPage: 1,
        curElement: 0,
        size: 12,
        totalPage: 1,
        totalElement: 0,
        order: "desc"
    });
    const itemsPerPage = 12;
    const [isFirstLoad, setIsFirstLoad] = useState(true);

    // 백엔드에서 상품 목록 가져오기
    useEffect(() => {
        const fetchData = async () => {
            try {
                let url;
                if (isFirstLoad) {
                    url = "/product";
                } else {
                    url = selected === "드레스"
                        ? `/product?page=${currentPage - 1}`
                        : `/product?category=${categoryCodes[selected]}&page=${currentPage - 1}`;
                    if (minPrice) url += `&minimum=${minPrice}`;
                    if (maxPrice) url += `&maximum=${maxPrice}`;
                }
                const response = await apiClient.get(url);
                if (response.data && response.data.data) {
                    setProductList(response.data.data);
                    if (response.data.pagination) {
                        setPagination(response.data.pagination);
                    }
                }
                if (isFirstLoad) setIsFirstLoad(false);
            } catch (error) {
                console.error("상품 로딩 실패:", error);
            }
        };
        fetchData();
    // eslint-disable-next-line
    }, [selected, currentPage, minPrice, maxPrice]);

    // 가격 입력값 검증 및 포맷팅
    const handlePriceChange = (value, setter) => {
        const numericValue = value.replace(/[^0-9]/g, '');
        setter(numericValue);
    };

    // 필터링 (색상 + ACTIVE 상태만 표시)
    const filteredItems = productList.filter((item) => {
        const colorMatch = selectedColor ? COLOR_MAP[item.color] === selectedColor : true;
        return colorMatch;
    });

    const totalPage = pagination && pagination.totalPage ? pagination.totalPage : 1;

    return (
        <div className="min-h-screen bg-gray-50">
            {/* 상단 메뉴 탭 */}
            <header className="bg-white shadow">
                <div className="flex justify-center space-x-4 px-6 py-4">
                    {Object.keys(categoryCodes).map((category) => (
                        <button
                            key={category}
                            className={`px-4 py-2 rounded-md transition-colors duration-200 
                                ${selected === category
                                    ? "text-[#845EC2] font-bold border border-[#845EC2] bg-white"
                                    : "text-[#B39CD0] bg-white"}
                                focus:outline-none`}
                            onClick={() => {
                                setSelected(category);
                                setCurrentPage(1);
                            }}
                        >
                            {category}
                        </button>
                    ))}
                </div>
            </header>

            <div className="flex max-w-7xl mx-auto">
                {/* 조건 검색 사이드바 */}
                <aside className="w-64 p-6 bg-white border-r">
                    <h2 className="text-xl font-bold mb-4 text-black">조건 검색</h2>

                    {/* 가격 범위 검색 (공통) */}
                    <div className="mb-6">
                        <h3 className="font-semibold text-gray-700 mb-2">가격</h3>
                        <div className="space-y-2">
                            <div className="flex items-center">
                                <input
                                    type="text"
                                    value={minPriceInput}
                                    onChange={(e) => handlePriceChange(e.target.value, setMinPriceInput)}
                                    placeholder="최소 가격"
                                    className="w-full bg-white px-3 py-2 border-2 border-gray-300 rounded text-black text-sm shadow-sm focus:outline-none focus:border-[#845EC2] focus:shadow-md transition"
                                />
                            </div>
                            <div className="flex items-center">
                                <input
                                    type="text"
                                    value={maxPriceInput}
                                    onChange={(e) => handlePriceChange(e.target.value, setMaxPriceInput)}
                                    placeholder="최대 가격"
                                    className="w-full bg-white px-3 py-2 border-2 border-gray-300 rounded text-black text-sm shadow-sm focus:outline-none focus:border-[#845EC2] focus:shadow-md transition"
                                />
                            </div>
                            <button
                                className="w-full mt-2 py-2 bg-[#845EC2] text-white rounded font-semibold hover:bg-[#5C2E91] transition"
                                onClick={() => {
                                    setMinPrice(minPriceInput);
                                    setMaxPrice(maxPriceInput);
                                    setCurrentPage(1);
                                }}
                            >
                                적용
                            </button>
                        </div>
                    </div>

                    {/* 드레스 필터 */}
                    {selected === "드레스" && (
                        <div className="mb-4">
                            <h3 className="font-semibold text-gray-700 mb-2">색상</h3>
                            <div className="flex flex-wrap gap-2 text-sm">
                                {Object.values(COLOR_MAP).map((colorKo) => (
                                    <button
                                        key={colorKo}
                                        className={`px-2 py-1 border rounded transition-colors duration-200
                                            ${selectedColor === colorKo
                                                ? "bg-[#845EC2] text-white border-[#845EC2]"
                                                : "bg-white text-purple-600"} focus:outline-none`}
                                        onClick={() =>
                                            setSelectedColor(selectedColor === colorKo ? null : colorKo)
                                        }
                                    >
                                        {colorKo}
                                    </button>
                                ))}
                            </div>
                        </div>
                    )}

                    {/* 스튜디오 필터 */}
                    {selected === "스튜디오" && (
                        <div className="mb-4">
                            <h3 className="font-semibold text-gray-700 mb-2">장소</h3>
                            <div className="flex flex-wrap gap-2 text-sm">
                                {["실내", "실외"].map((place) => (
                                    <button
                                        key={place}
                                        className="px-2 py-1 border rounded bg-white text-purple-600 hover:bg-purple-50"
                                    >
                                        {place}
                                    </button>
                                ))}
                            </div>
                        </div>
                    )}

                    {/* 메이크업 필터 */}
                    {selected === "메이크업" && (
                        <div className="mb-4">
                            <h3 className="font-semibold text-gray-700 mb-2">서비스 유형</h3>
                            <div className="flex flex-wrap gap-2 text-sm">
                                {["출장", "방문"].map((type) => (
                                    <button
                                        key={type}
                                        className="px-2 py-1 border rounded bg-white text-purple-600 hover:bg-purple-50"
                                    >
                                        {type}
                                    </button>
                                ))}
                            </div>
                        </div>
                    )}
                </aside>

                {/* 상품 리스트 */}
                <main className="flex-1 p-6">
                    <div className="grid grid-cols-4 gap-4">
                        {filteredItems.length === 0 ? (
                            <div className="col-span-4 text-center text-gray-400 py-20">상품이 없습니다.</div>
                        ) : (
                            filteredItems.map((item) => (
                                <Link to={`/product/${categoryMap[selected]}/${item.productId}`} key={item.productId}>
                                    <div className="bg-white p-4 border rounded shadow-sm hover:shadow-md transition-shadow duration-200">
                                        <img
                                            src={item.imageUrl ? `${baseUrl}${item.imageUrl.replace(/\\/g, '/')}` : '/default/images/product.png'}
                                            alt={item.productName}
                                            className="h-48 w-full object-cover rounded mb-3"
                                        />
                                        <h4 className="font-medium text-gray-800 text-sm line-clamp-2">{item.productName}</h4>
                                        <p className="text-xs text-gray-500 mt-1">{item.companyName}</p>
                                        <p className="text-violet-600 font-semibold mt-2">
                                            ₩ {item.price.toLocaleString()}
                                        </p>
                                    </div>
                                </Link>
                            ))
                        )}
                    </div>

                    {/* 페이지네이션 */}
                    <div className="flex justify-center mt-10">
                        {Array(totalPage)
                            .fill(0)
                            .map((_, index) => (
                                <button
                                    key={index}
                                    className={`mx-1 px-3 py-1 rounded transition-colors duration-200
                                        ${currentPage === index + 1
                                            ? "bg-[#845EC2] text-white"
                                            : "bg-gray-200 text-black"}
                                        hover:bg-[#B39CD0] focus:outline-none`}
                                    onClick={() => setCurrentPage(index + 1)}
                                >
                                    {index + 1}
                                </button>
                            ))}
                    </div>
                </main>
            </div>
        </div>
    );
}

export default Product;