import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axios from 'axios';
import { COLOR_MAP } from "../../../constants/colorMap.js";
import dummyProduct from "../../Company/Management/Product/productDummy.js";

const baseUrl = import.meta.env.VITE_API_BASE_URL;

const ProductDetail = () => {
    const { category, productid } = useParams();

    // 더미파일의 itemid를 인식 
    const [product, setProduct] = useState(() =>
        dummyProduct.find((item) => item.id === productid)
    );
    // 백엔드서버에서 받아오면 
    // const [product, setProduct] = useState(null);
    const navigate = useNavigate();

    const [selectedOptions, setSelectedOptions] = useState({});
    const [mainImageIndex, setMainImageIndex] = useState(0); //대표이미지가 1번
    const [selectedTab, setSelectedTab] = useState('detail'); //상품상세정보가 기본값


    useEffect(() => {
        axios.get(`${baseUrl}/product/${category}/${productid}`)
            .then((res) => setProduct(res.data))
            .catch((err) => {
                console.error("상품 불러오기 실패", err);
                alert("상품 정보를 불러올 수 없습니다.");
                navigate(-1);
            });
    }, [category, productid]);
    
    // 오토 이미지 슬라이드
    useEffect(() => {
        if (!product?.image_url_list) return;
        const timer = setInterval(() => {
            setMainImageIndex((prev) => (prev + 1) % product.image_url_list.length);
        }, 3000);
        return () => clearInterval(timer);
    }, [product]);

    // 옵션 중복선택 방지
    const handleOptionChange = (groupName, value) => {
        const group = product.option_list.find((opt) => opt.name === groupName);
        const isOverlap = group.overlap === "Y";

        if (!isOverlap && selectedOptions[groupName]) {
            alert("이 옵션은 중복 선택이 불가능합니다.");
            return;
        }

        setSelectedOptions((prev) => ({
            ...prev,
            [groupName]: value,
        }));
    };
    // 선택한 옵션 삭제하는 영역
    const handleRemoveOption = (groupName) => {
        setSelectedOptions((prev) => {
            const newOptions = { ...prev };
            delete newOptions[groupName];
            return newOptions;
        });
    };

    if (!product) {
        return <div className="text-center py-20 text-gray-500">상품 정보를 불러오는 중입니다...</div>;
    }

    return (
        <div className="max-w-5xl mx-auto p-6 text-black">
            {/* 상단 정보 */}
            <div className="flex flex-col lg:flex-row gap-6">
                {/* 이미지 영역 */}
                <div className="w-full lg:w-1/2">
                    <div className="border rounded overflow-hidden w-full aspect-square bg-gray-100">
                        <img
                            src={product.image_url_list?.[mainImageIndex] || '/placeholder.png'}
                            alt="product"
                            className="w-full h-full object-cover cursor-pointer"
                        />
                    </div>
                    <div className="flex gap-2 mt-3 overflow-x-auto">
                        {(product.image_url_list || []).map((url, idx) => (
                            <img
                                key={idx}
                                src={url}
                                alt={`thumb-${idx}`}
                                onClick={() => setMainImageIndex(idx)}
                                className={`w-20 h-20 object-cover rounded cursor-pointer border ${
                                    mainImageIndex === idx ? 'border-[#845EC2] border-2' : 'border-gray-300'
                                }`}
                            />
                        ))}
                    </div>
                </div>

                {/* 상품 정보 */}
                <div className="w-full lg:w-1/2 flex flex-col justify-between">
                    <div>
                        <h2 className="text-2xl font-semibold text-gray-800 mb-2">{product.name}</h2>
                        <p className="text-lg text-violet-600 font-bold mb-4">
                            {typeof product.price === 'number' ? `₩${product.price.toLocaleString()}` : '가격 정보 없음'}
                        </p>

                        {/* 색상 */}
                        <div className="flex items-center mb-4">
                            <label className="w-24 font-medium text-gray-700">색상</label>
                            <select
                                value={product.color}
                                disabled
                                className="flex-grow border p-2 rounded bg-white text-black appearance-none"
                            >  
                                {/* 색상 옵션 번역 */}
                                {Object.entries(COLOR_MAP).map(([eng, kor]) => (
                                    <option key={eng} value={eng}>{kor}</option>
                                ))}
                            </select>
                            <div
                                className="ml-2 w-20 h-10 rounded border border-gray-400"
                                style={{ backgroundColor: product.color }}
                            />
                        </div>

                        {/* 옵션 창창 */}
                        <div className="space-y-4">
                            {(product.option_list || []).map((opt, i) => (
                                <div key={i}>
                                    <div className="flex items-center justify-between">
                                        <label className="block font-medium text-gray-700 mb-1">{opt.name}</label>
                                        {opt.essential === 'Y' && !selectedOptions[opt.name] && (
                                            <span className="text-red-500 text-sm ml-2">필수 선택</span>
                                        )}
                                    </div>
                                    <select
                                        className="w-full border bg-white text-black px-3 py-2 rounded mt-1"
                                        onChange={(e) => handleOptionChange(opt.name, e.target.value)}
                                        value={selectedOptions[opt.name] || ""}
                                    >
                                        <option value="">옵션 선택</option>
                                        {(opt.option_dt_list || []).map((item, idx) => (
                                            <option key={idx} value={item.op_dt_name}>
                                                {item.op_dt_name} (₩{item.plus_cost.toLocaleString()})
                                            </option>
                                        ))}
                                    </select>
                                </div>
                            ))}
                        </div>

                        {/* 선택된 옵션 확인하기 */}
                        {Object.keys(selectedOptions).length > 0 && (
                            <div className="mt-6 border-t pt-4">
                                <h3 className="text-md font-semibold mb-2 text-gray-800">선택한 옵션</h3>
                                <ul className="space-y-1 text-sm text-gray-700">
                                    {Object.entries(selectedOptions).map(([key, value], i) => (
                                        <li key={i} className="flex justify-between items-center">
                                            <span>• {key} : {value}</span>
                                            <button
                                                onClick={() => handleRemoveOption(key)}
                                                className="bg-white ml-2 text-red-500 hover:underline text-sm"
                                            >
                                                X
                                            </button>
                                        </li>
                                    ))}
                                </ul>
                            </div>
                        )}
                    </div>

                    {/* 장바구니, 찜, 채팅문의 버튼 */}
                    <div className="mt-6 flex flex-col gap-3">
                        <div className="flex gap-2">
                            <button className="w-full flex justify-center items-center gap-2 bg-purple-500 text-white font-semibold py-3 rounded shadow">
                                <span>🛒</span> 장바구니 담기
                            </button>
                            <button className="w-12 h-12 border bg-white rounded flex items-center justify-center text-purple-500 text-xl">
                                ❤️
                            </button>
                        </div>
                        <button className="bg-pink-400 text-white font-semibold py-3 rounded">1:1 채팅하기</button>
                    </div>
                </div>
            </div>

            {/* 상세 / 리뷰 탭 */}
            <div className="mt-10 border-t pt-6">
                <div className="flex gap-4 mb-4">
                    <button
                        onClick={() => setSelectedTab('detail')}
                        className={`text-lg font-semibold pb-1 bg-white focus:outline-none ${
                        selectedTab === 'detail' ? 'text-purple-600 border-b-2 border-purple-600' : 'text-gray-500 hover:text-purple-600'
                        }`}> 상품상세정보
                    </button>
                    <button
                        onClick={() => setSelectedTab('review')}
                        className={`text-lg font-semibold pb-1 bg-white focus:outline-none ${
                        selectedTab === 'review' ? 'text-purple-600 border-b-2 border-purple-600' : 'text-gray-500 hover:text-purple-600'
                        }`}> 리뷰
                    </button>
                </div>

                {/* 내용 영역 */}
                <div className="bg-gray-50 p-4 rounded shadow">
                    {selectedTab === 'detail' ? (
                        <div
                        className="text-sm text-gray-700 leading-relaxed"
                        dangerouslySetInnerHTML={{ __html: product.description }}
                        ></div>
                    ) : (
                        <div className="text-sm text-gray-700 leading-relaxed">
                        {(product.reviews && product.reviews.length > 0) ? (
                            <ul className="space-y-2">
                            {product.reviews.map((review, idx) => (
                                <li key={idx} className="p-2 border rounded">{review}</li>
                            ))}
                            </ul>
                        ) : (
                            <p className="text-center text-gray-400">리뷰가 없습니다.</p>
                        )}
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
};

export default ProductDetail;