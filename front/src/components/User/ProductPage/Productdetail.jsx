import React, { useState, useEffect, useRef } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axios from 'axios';
import { COLOR_MAP } from "../../../constants/colorMap.js";
import dummyProduct from "../../Company/Management/Product/productDummy.js";
import AddWrite from '../../Tool/WriteForm/AddWrite.jsx';

const baseUrl = import.meta.env.VITE_API_BASE_URL;

// 분을 'X시간 Y분'으로 변환하는 함수
function formatTime(minutes) {
    const hour = Math.floor(minutes / 60);
    const min = minutes % 60;
    if (hour > 0 && min > 0) return `${hour}시간 ${min}분`;
    if (hour > 0) return `${hour}시간`;
    return `${min}분`;
}

// 선택한 옵션들의 plus_cost 합산 함수
function getTotalOptionPrice(product, selectedOptions) {
    if (!product) return 0;
    let total = 0;
    // optionList와 option_list 모두 대응
    const optionArr = product.optionList || product.option_list || [];
    optionArr.forEach(opt => {
        const selected = selectedOptions[opt.name];
        if (selected) {
            const found = (opt.option_dt_list || opt.optionDtList || []).find(dt => dt.op_dt_name === selected || dt.opDtName === selected);
            if (found) total += found.plus_cost || found.plusCost || 0;
        }
    });
    // sizeList
    if (product.sizeList && selectedOptions['size']) {
        const found = product.sizeList.find(sz => sz.size === selectedOptions['size']);
        if (found) total += found.plus_cost || found.plusCost || 0;
    }
    return total;
}

const ProductDetail = () => {
    const { category, productid } = useParams();
    const [product, setProduct] = useState(null);
    const navigate = useNavigate();

    const [selectedOptions, setSelectedOptions] = useState({});
    const [mainImageIndex, setMainImageIndex] = useState(0); 
    const [selectedTab, setSelectedTab] = useState('detail'); 

    const writeRef = useRef();

    useEffect(() => {
        axios.get(`${baseUrl}/product/${category}/${productid}`)
            .then((res) => {
                console.log('백엔드 원본 응답:', res.data);
                const data = res.data.data;
                setProduct({
                    ...data,
                    name: data.name,
                    id: data.id,
                    price: data.price,
                    descriptionList: data.descriptionList,
                    optionList: data.optionList,
                    sizeList: data.sizeList,
                    color: data.color,
                    image_url_list: data.productImageUrl ? data.productImageUrl.map(img => baseUrl + img.url.replace(/\\/g, '/')) : [],
                    // 기타 필요한 필드
                });
            })
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

    // descriptionList가 바뀔 때마다 AddWrite에 세팅
    useEffect(() => {
        if (selectedTab === 'detail' && writeRef.current && product?.descriptionList) {
            writeRef.current.setContentFromJsonArray?.(product.descriptionList);
        }
    }, [selectedTab, product?.descriptionList]);

    // 옵션 중복선택 방지
    const handleOptionChange = (groupName, value) => {
        // option_list와 optionList 모두 대응
        const group = (product.option_list || product.optionList || []).find((opt) => opt.name === groupName);
        // size 옵션은 group이 없을 수 있음
        const isOverlap = group ? group.overlap === "Y" : false;

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
                        {category === 'dress' && (
                            <div className="flex items-center mb-4">
                                <label className="w-24 font-medium text-gray-700">색상</label>
                                <select
                                    value={product.color}
                                    disabled
                                    className="flex-grow border p-2 rounded bg-white text-black appearance-none"
                                >  
                                    {Object.entries(COLOR_MAP).map(([eng, kor]) => (
                                        <option key={eng} value={eng}>{kor}</option>
                                    ))}
                                </select>
                                <div
                                    className="ml-2 w-20 h-10 rounded border border-gray-400"
                                    style={{ backgroundColor: product.color }}
                                />
                            </div>
                        )}

                        {/* 작업시간 표시 */}
                        {product.taskTime && (
                            <div className="mb-4">
                                <label className="block font-medium text-gray-700 mb-1">작업시간</label>
                                <div className="px-3 py-2 border rounded bg-white text-black">{formatTime(product.taskTime)}</div>
                            </div>
                        )}

                        {/* 옵션 창창 */}
                        <div className="space-y-4">
                            {(product.option_list || []).map((opt, i) => {
                                const selected = selectedOptions[opt.name];
                                const found = (opt.option_dt_list || []).find(dt => dt.op_dt_name === selected);
                                return (
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
                                        {selected && found && (
                                            <div className="text-sm text-purple-700 mt-1">
                                                선택: {selected} {found.plus_cost ? `(+₩${found.plus_cost.toLocaleString()})` : ""}
                                            </div>
                                        )}
                                    </div>
                                );
                            })}
                        </div>

                        {/* 옵션 선택 (optionList) */}
                        {product.optionList && product.optionList.length > 0 && product.optionList.map((opt, i) => {
                            const selected = selectedOptions[opt.name];
                            const found = (opt.option_dt_list || []).find(dt => dt.op_dt_name === selected);
                            return (
                                <div key={i} className="mb-4">
                                    <label className="block font-medium text-gray-700 mb-1">{opt.name}</label>
                                    <select
                                        className="w-full border bg-white text-black px-3 py-2 rounded mt-1"
                                        onChange={e => handleOptionChange(opt.name, e.target.value)}
                                        value={selectedOptions[opt.name] || ""}
                                    >
                                        <option value="">옵션 선택</option>
                                        {(opt.option_dt_list || []).map((item, idx) => (
                                            <option key={idx} value={item.op_dt_name}>
                                                {item.op_dt_name} (₩{item.plus_cost.toLocaleString()})
                                            </option>
                                        ))}
                                    </select>
                                    {selected && found && (
                                        <div className="text-sm text-purple-700 mt-1">
                                            선택: {selected} {found.plus_cost ? `(+₩${found.plus_cost.toLocaleString()})` : ""}
                                        </div>
                                    )}
                                </div>
                            );
                        })}

                        {/* 사이즈 옵션 추가 */}
                        {product.sizeList && product.sizeList.length > 0 && (
                            <div className="mb-4">
                                <label className="block font-medium text-gray-700 mb-1">사이즈</label>
                                <select
                                    className="w-full border bg-white text-black px-3 py-2 rounded mt-1"
                                    onChange={e => handleOptionChange('size', e.target.value)}
                                    value={selectedOptions['size'] || ""}
                                >
                                    <option value="">사이즈 선택</option>
                                    {product.sizeList.map((item, idx) => (
                                        <option key={idx} value={item.size}>
                                            {item.size} (재고: {item.stock}, +₩{item.plus_cost})
                                        </option>
                                    ))}
                                </select>
                            </div>
                        )}

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
                        <div className="mb-2 text-right font-bold text-lg text-purple-700">
                            총 결제금액: ₩ {((product.price || 0) + getTotalOptionPrice(product, selectedOptions)).toLocaleString()}
                        </div>
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
                        <AddWrite ref={writeRef} readOnly />
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