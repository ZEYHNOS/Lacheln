import React, { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import axios from "axios";
import { COLOR_MAP } from "@/constants/colorMap"; 
import productDummy from "../../Company/Management/Product/productDummy.js"; // 더미 데이터 import

const baseUrl = import.meta.env.VITE_API_BASE_URL;

// 백엔드 category 코드 → URL path 매핑
const categoryUrlMap = {
    D: "dress",
    S: "studio",
    M: "makeup",
};

// 탭용 한글 → 백엔드 코드
const tabCategoryCodeMap = {
    "드레스": "D",
    "스튜디오": "S",
    "메이크업": "M",
};

function Product() {
    const [selected, setSelected] = useState("드레스");
    const [selectedColor, setSelectedColor] = useState(null);
    const [currentPage, setCurrentPage] = useState(1);
    const itemsPerPage = 9;

    const filteredItems = productDummy.filter((item) => {
        const categoryMatch = item.category === tabCategoryCodeMap[selected];
        const notPackage = item.status !== "PACKAGE";
        const colorMatch = selectedColor ? COLOR_MAP[item.color] === selectedColor : true;
        return categoryMatch && notPackage && colorMatch;
    });

    const totalPages = Math.ceil(filteredItems.length / itemsPerPage);
    const currentItems = filteredItems.slice((currentPage - 1) * itemsPerPage, currentPage * itemsPerPage);

    return (
        <div className="min-h-screen bg-gray-50">
            {/* 상단 메뉴 탭 */}
            <header className="bg-white shadow">
                <div className="flex justify-center space-x-4 px-6 py-4">
                    {["스튜디오", "드레스", "메이크업"].map((category) => (
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
                        <>
                        <div className="mb-4">
                            <h3 className="font-semibold text-gray-700 mb-2">지역</h3>
                            <div className="flex flex-wrap gap-2 text-sm">
                            {["국내", "해외"].map((region) => (
                                <button
                                key={region}
                                className="px-2 py-1 border rounded bg-white text-purple-600 hover:bg-purple-50"
                                >
                                {region}
                                </button>
                            ))}
                            </div>
                        </div>
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
                        </>
                    )}

                    {/* 메이크업 필터 */}
                    {selected === "메이크업" && (
                        <div className="mb-4">
                        <h3 className="font-semibold text-gray-700 mb-2">구성</h3>
                        <div className="flex flex-wrap gap-2 text-sm">
                            {["메이크업", "헤어 포함"].map((item) => (
                            <button
                                key={item}
                                className="px-2 py-1 border rounded bg-white text-purple-600 hover:bg-purple-50"
                            >
                                {item}
                            </button>
                            ))}
                        </div>
                        </div>
                    )}
                </aside>


                {/* 상품 리스트 */}
                <main className="flex-1 p-6">
                    <div className="grid grid-cols-3 gap-6">
                        {currentItems.map((item) => (
                            <Link to={`/product/${item.id}`} key={item.id}>
                                <div className="bg-white p-4 border rounded shadow-sm hover:shadow-md transition-shadow duration-200">
                                    <img
                                        src={item.image_url_list[0]}
                                        alt={item.name}
                                        className="h-48 w-full object-cover rounded mb-3"
                                    />
                                    <h4 className="font-medium text-gray-800">{item.name}</h4>
                                    <p className="text-sm text-gray-500">
                                        {COLOR_MAP[item.color] || item.color} / {item.in_available === "Y" ? "실내촬영 가능" : "실내촬영 불가"}
                                    </p>
                                    <p className="text-violet-600 font-semibold mt-2">
                                        ₩ {item.price.toLocaleString()}
                                    </p>
                                </div>
                            </Link>
                        ))}
                    </div>

                    {/* 페이지네이션 */}
                    <div className="flex justify-center mt-10">
                        {Array(totalPages)
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



// // 백엔드에서 카테고리 변경
    // useEffect(() => {
    //     const fetchData = async () => {
    //         try {
    //             const endpoint = `${baseUrl}/product/${categoryUrlMap[selected]}/list`;
    //             const response = await axios.get(endpoint);
    //             setProductList(response.data);
    //             setCurrentPage(1);
    //         } catch (error) {
    //             console.error("상품 로딩 실패:", error);
    //         }
    //     };

    //     fetchData();
    // }, [selected]);

    // // 필터링 (색상 + 패키지 제외)
    // const filteredItems = productList.filter((item) => {
    //     const notPackage = item.status !== "PACKAGE";
    //     const colorMatch = selectedColor ? COLOR_MAP[item.color] === selectedColor : true;
    //     return notPackage && colorMatch;
    // });