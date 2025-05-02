import React, { useState,useEffect } from "react";
import { useNavigate } from "react-router-dom"; 
import axios from "axios";
// import productDummy from "./productDummy"; // 더미 데이터 import

const baseUrl = import.meta.env.VITE_API_BASE_URL;


function Product() {
    const [selected, setSelected] = useState("전체보기");
    const [currentPage, setCurrentPage] = useState(1);
    // 백엔드에서 받아올때
    const [productList, setProductList] = useState([]);
    const navigate = useNavigate();
    const itemsPerPage = 5;

    // 실제 백엔드에서 받아오는 주소
    useEffect(() => {
        axios.get(`${baseUrl}/product/list`, {
            withCredentials: true
        })
            .then(res => {
                console.log("받아온 데이터:", res.data);
                setProductList(res.data.data);
            })
            .catch(err => console.error("상품 목록 불러오기 실패", err));
    }, []);

    // ✅ 탭 필터링 로직    
    // productDummy -> productList
    const filteredItems = productList.filter((item) => {
        if (selected === "전체보기") return true;
        if (selected === "개별상품") return item.status !== "PACKAGE";
        if (selected === "패키지상품") return item.status === "PACKAGE";
        return false;
    });

    // 총 페이지 수 계산
    const totalPages = Math.ceil(filteredItems.length / itemsPerPage);
    // 현재 페이지에 보여줄 상품 슬라이스
    const currentItems = filteredItems.slice(
        (currentPage - 1) * itemsPerPage,
        currentPage * itemsPerPage
    );

    return (
        <div className="w-full h-full overflow-auto bg-white text-black flex flex-col">
            <div className="bg-white text-black p-6 rounded-lg w-full">
                {/* 상품 카테고리 필터 버튼 */}
                <div className="flex justify-between mb-4 bg-white p-2 rounded-md">
                    <div>
                        {["전체보기", "개별상품", "패키지상품"].map((category) => (
                            <button
                                key={category}
                                className={`px-4 py-2 rounded-md transition-colors duration-200 bg-transparent 
                                    ${selected === category ? "text-[#845EC2] font-bold border border-[#845EC2]" : "text-[#B39CD0]"}
                                    focus:ring-0 focus:outline-none`}
                                onClick={() => {
                                    setSelected(category);
                                    setCurrentPage(1); 
                                }}
                            >
                                {category}
                            </button>
                        ))}
                    </div>
                    <button
                        className="bg-[#845EC2] text-white px-8 py-2 rounded hover:bg-purple-500 transition-colors duration-200"
                        onClick={() => navigate("/company/product/add")}
                    >
                        추가
                    </button>
                </div>

                {/* 상품 리스트 */}
                <div className="overflow-x-auto">
                    <table className="w-full border-collapse border border-gray-300">
                        <thead>
                            <tr className="bg-gray-100 text-center">
                                <th className="border border-gray-300 p-2">이미지</th>
                                <th className="border border-gray-300 p-2">상품명</th>
                                <th className="border border-gray-300 p-2">상태</th>
                                <th className="border border-gray-300 p-2">가격</th>
                            </tr>
                        </thead>
                        <tbody>
                            {currentItems.map((product) => (
                                <tr
                                    key={product.id}
                                    className="text-center cursor-pointer hover:bg-gray-100 transition-colors duration-200"
                                    onClick={() => navigate(`/company/product/${product.id}`)}
                                >
                                    <td className="border border-gray-300 p-2">
                                    <img 
                                        src={`${baseUrl}${product.imageUrl}`} 
                                        alt="상품 이미지" 
                                        className="mx-auto w-12 h-12 object-cover rounded" 
                                    />
                                    </td>
                                    <td className="border border-gray-300 p-2">{product.name}</td>
                                    <td className="border border-gray-300 p-2">{product.status}</td>
                                    <td className="border border-gray-300 p-2">{product.price.toLocaleString()}￦</td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>

                {/* 페이지네이션 */}
                <div className="flex justify-center mt-4 py-8">
                    {Array(totalPages).fill(0).map((_, index) => (
                        <button 
                            key={index} 
                            className={`mx-1 px-3 py-1 rounded transition-colors duration-200
                                ${currentPage === index + 1 ? 'bg-[#845EC2] text-white' : 'bg-gray-200 text-black'}
                                hover:bg-[#B39CD0] focus:outline-none`}
                            onClick={() => setCurrentPage(index + 1)}
                        >
                            {index + 1}
                        </button>
                    ))}
                </div>
            </div>
        </div>
    );
}

export default Product;