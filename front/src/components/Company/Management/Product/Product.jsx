import React, { useState } from "react";
import { useNavigate } from "react-router-dom"; 
import weddingDress from "../../../../image/dummydata/weddingdress1.jpg"; // 더미데이터용 사진

function Product() {
    const [selected, setSelected] = useState("전체보기");
    const [currentPage, setCurrentPage] = useState(1);
    const navigate = useNavigate();

    // 샘플 상품 더미 데이터
    const dress = [
        { id: "1", name: "드레스 22호 (흰)", status: "판매중", price: "100,000￦", image: weddingDress },
        { id: "2", name: "드레스 23호 (핑크)", status: "판매중", price: "120,000￦", image: weddingDress },
        { id: "3", name: "드레스 24호 (블루)", status: "비공개", price: "130,000￦", image: weddingDress },
        { id: "4", name: "드레스 25호 (레드)", status: "판매중", price: "140,000￦", image: weddingDress },
        { id: "5", name: "드레스 26호 (옐로우)", status: "판매중", price: "150,000￦", image: weddingDress },
        { id: "6", name: "드레스 27호 (퍼플)", status: "비공개", price: "160,000￦", image: weddingDress },
    ];

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
                                onClick={() => setSelected(category)}
                            >
                                {category}
                            </button>
                        ))}
                    </div>
                    {/* 상품추가 버튼 -> addproduct.jsx */}
                    <button
                        className="bg-[#845EC2] text-white px-8 py-2 rounded hover:bg-purple-500 transition-colors duration-200"
                        onClick={() => navigate("/company/product/add")} // 버튼 클릭 시 이동
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
                            {dress.map((dress) => (
                                <tr
                                    key={dress.id}
                                    className="text-center cursor-pointer hover:bg-gray-100 transition-colors duration-200"
                                    onClick={() => navigate(`/company/product/${dress.id}`)} // 클릭 시 해당 URL로 이동
                                >
                                    <td className="border border-gray-300 p-2">
                                        <img 
                                            src={dress.image} 
                                            alt="상품 이미지" 
                                            className="mx-auto w-12 h-12 object-cover rounded" 
                                        />
                                    </td>
                                    <td className="border border-gray-300 p-2">{dress.name}</td>
                                    <td className="border border-gray-300 p-2">{dress.status}</td>
                                    <td className="border border-gray-300 p-2">{dress.price}</td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>

                {/* 페이지네이션 */}
                <div className="flex justify-center mt-4 py-8">
                    {Array(11).fill(0).map((_, index) => (
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
