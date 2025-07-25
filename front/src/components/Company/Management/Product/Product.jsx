import React, { useState,useEffect } from "react";
import { useNavigate } from "react-router-dom"; 
import apiClient from "../../../../lib/apiClient";

const baseUrl = import.meta.env.VITE_API_BASE_URL;


function Product() {
    const [selected, setSelected] = useState("전체보기");
    const [currentPage, setCurrentPage] = useState(1);
    // 백엔드에서 받아올때
    const [productList, setProductList] = useState([]);
    const navigate = useNavigate();
    const itemsPerPage = 5;

    // 실제 백엔드에서 받아오는 주소
    function getCookie(name) {
        const match = document.cookie.match(new RegExp('(^| )' + name + '=([^;]+)'));
        if (match) return match[2];
        return null;
    }
    
    useEffect(() => {
        const token = getCookie("AccessToken");
        apiClient.get("/product/list", {
            headers: { Authorization: `Bearer ${token}` }
        })
            .then(res => {
                console.log("받아온 데이터:", res.data);
                setProductList(res.data.data);
            })
            .catch(err => {
                console.error("상품 목록 불러오기 실패", err);
            });
    }, []);

    // ✅ 탭 필터링 로직    
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
                        <div className="flex space-x-4">
                            {['전체보기', '개별상품', '패키지상품'].map((category) => (
                                <button
                                    key={category}
                                    className={`px-4 py-2 rounded-none bg-transparent border-none shadow-none outline-none
                                        ${selected === category ? 'text-purple-700 font-bold' : 'text-purple-300'}
                                        hover:bg-transparent focus:outline-none focus:ring-0 active:outline-none active:ring-0 focus-visible:outline-none focus-visible:ring-0`}
                                    style={{ border: 'none', boxShadow: 'none' }}
                                    onClick={() => {
                                        setSelected(category);
                                        setCurrentPage(1);
                                    }}
                                >
                                    {category}
                                </button>
                            ))}
                        </div>
                    </div>
                    <button
                        className="bg-[#845EC2] text-white px-8 py-2 rounded hover:bg-purple-500 transition-colors duration-200"
                        onClick={() => navigate("/company/product/add")}
                    >
                        추가
                    </button>
                </div>

                {/* 상품 리스트 */}
                <div className="overflow-x-auto min-h-[400px]">
                    <table className="w-full border-collapse border border-gray-300 table-fixed">
                        <thead>
                            <tr className="bg-gray-100 text-center">
                                <th className="border border-gray-300 p-2" style={{width: '20%'}}>이미지</th>
                                <th className="border border-gray-300 p-2" style={{width: '40%'}}>상품명</th>
                                <th className="border border-gray-300 p-2" style={{width: '20%'}}>상태</th>
                                <th className="border border-gray-300 p-2" style={{width: '20%'}}>가격</th>
                            </tr>
                        </thead>
                        <tbody>
                            {currentItems.map((product) => (
                                <tr
                                    key={product.productId}
                                    className="text-center cursor-pointer hover:bg-gray-100 transition-colors duration-200"
                                    onClick={() => navigate(`/company/product/${product.productId}`)}
                                >
                                    <td className="border border-gray-300 p-2" style={{width: '20%'}}>
                                        <img 
                                            src={product.imageUrl ? `${baseUrl}${product.imageUrl.replace(/\\/g, '/')}` : '/image/default.jpg'} 
                                            alt="상품 이미지" 
                                            className="mx-auto w-12 h-12 object-cover rounded"
                                            onError={e => e.currentTarget.src = '/image/default.jpg'}
                                        />
                                    </td>
                                    <td className="border border-gray-300 p-2" style={{width: '40%'}}>{product.productName}</td>
                                    <td className="border border-gray-300 p-2" style={{width: '20%'}}>{product.status ? (product.status === "ACTIVE" ? "판매중" : product.status === "INACTIVE" ? "판매 대기중" : product.status === "PACKAGE" ? "패키지상품 판매중" : product.status) : "-"}</td>
                                    <td className="border border-gray-300 p-2" style={{width: '20%'}}>{product.price.toLocaleString()}￦</td>
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