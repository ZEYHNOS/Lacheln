import React, { useState, useEffect, useRef } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import apiClient from '../../../lib/apiClient';
import { COLOR_MAP } from "../../../constants/colorMap.js";
import AddWrite from '../../Tool/WriteForm/AddWrite.jsx';
import ScheduleSelect from '../../Tool/Schedule/ScheduleSelect.jsx';
import ChattingModal from '../../User/UserPage/ChattingModal.jsx';
import { toast } from 'react-toastify';

const baseUrl = import.meta.env.VITE_API_BASE_URL;

// LocalTime을 'X시간 Y분'으로 변환하는 함수
function formatLocalTime(taskTime) {
    if (Array.isArray(taskTime) && taskTime.length === 2) {
        const [hour, min] = taskTime;
        if (min === 0) return `${hour}시간`;
        if (hour === 0) return `${min}분`;
        return `${hour}시간 ${min}분`;
    }
    if (typeof taskTime === 'string' && taskTime.includes(':')) {
        const [hour, min] = taskTime.split(':').map(Number);
        if (min === 0) return `${hour}시간`;
        if (hour === 0) return `${min}분`;
        return `${hour}시간 ${min}분`;
    }
    return taskTime;
}

// 배열을 HH:mm 형식으로 변환하는 함수
function arrayToLocalTime(timeArray) {
    if (!Array.isArray(timeArray) || timeArray.length !== 2) return null;
    const [hours, minutes] = timeArray;
    return `${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}`;
}

// 분(int)을 HH:mm 문자열로 변환하는 함수
function minutesToLocalTime(minutes) {
    const h = Math.floor(minutes / 60);
    const m = minutes % 60;
    return `${h.toString().padStart(2, '0')}:${m.toString().padStart(2, '0')}`;
}

// plusTime을 안전하게 HH:mm 문자열로 변환하는 함수 (2자리씩)
function safeMinutesToLocalTime(plusTime) {
    if (!plusTime || !Array.isArray(plusTime) || plusTime.length !== 2) return '00:00';
    const [h, m] = plusTime.map(Number);
    return `${(isNaN(h) ? 0 : h).toString().padStart(2, '0')}:${(isNaN(m) ? 0 : m).toString().padStart(2, '0')}`;
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
            const found = (opt.optionDtList || opt.option_dt_list || []).find(dt => (dt.opDtName || dt.op_dt_name) === selected);
            if (found) total += found.plusCost || found.plus_cost || 0;
        }
    });
    // sizeList
    if (product.sizeList && selectedOptions['size']) {
        const found = product.sizeList.find(sz => sz.size === selectedOptions['size']);
        if (found) total += found.plus_cost || found.plusCost || 0;
    }
    return total;
}

function formatOptionTime(plusTime) {
    if (!Array.isArray(plusTime) || plusTime.length !== 2) return '';
    const [hour, min] = plusTime;
    if (hour === 0 && min === 0) return '';
    if (hour > 0 && min > 0) return `${hour}시간 ${min}분`;
    if (hour > 0) return `${hour}시간`;
    if (min > 0) return `${min}분`;
    return '';
}

const ProductDetail = () => {
    const { category, productid } = useParams();
    const [product, setProduct] = useState(null);
    const [reviews, setReviews] = useState([]);
    const [loadingReviews, setLoadingReviews] = useState(false);
    const navigate = useNavigate();
    const [showChat, setShowChat] = useState(false);

    const [selectedOptions, setSelectedOptions] = useState({});
    const [mainImageIndex, setMainImageIndex] = useState(0);
    const [selectedTab, setSelectedTab] = useState('detail');
    const [showSchedule, setShowSchedule] = useState(false);

    const writeRef = useRef();

    useEffect(() => {
        apiClient.get(`/product/${category}/${productid}`)
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
                    thiscate: data.category,
                    color: data.color,
                    image_url_list: data.productImageUrl ? data.productImageUrl.map(img => baseUrl + img.url.replace(/\\/g, '/')) : [],
                });
            })
            .catch((err) => {
                console.error("상품 불러오기 실패", err);
                alert("상품 정보를 불러올 수 없습니다.");
                navigate(-1);
            });
    }, [category, productid]);

    // 리뷰 데이터 불러오기
    useEffect(() => {
        if (product?.id) {
            setLoadingReviews(true);
            apiClient.get(`/review/product/${product.id}`)
                .then((res) => {
                    console.log('리뷰 응답:', res.data);
                    if (res.data?.data) {
                        setReviews(res.data.data);
                    } else {
                        setReviews([]);
                    }
                })
                .catch((err) => {
                    console.error("리뷰 불러오기 실패", err);
                    setReviews([]);
                })
                .finally(() => {
                    setLoadingReviews(false);
                });
        }
    }, [product?.id]);

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

    // 필수 옵션이 모두 선택됐는지 확인하는 함수
    const isAllEssentialSelected = () => {
        // 옵션 그룹 필수 체크
        const optionEssentials = (product.optionList || product.option_list || []).filter(opt => opt.essential === 'Y');
        const optionOk = optionEssentials.every(opt => selectedOptions[opt.name]);
        // 사이즈 필수 체크
        let sizeOk = true;
        if (product.sizeList && product.sizeList.length > 0) {
            // sizeList에 essential: 'Y'가 하나라도 있으면 필수
            const sizeEssential = product.sizeList.some(sz => sz.essential === 'Y');
            if (sizeEssential) {
                sizeOk = !!selectedOptions['size'];
            }
        }
        return optionOk && sizeOk;
    };

    // 평점을 별점으로 표시하는 함수
    const renderStars = (score) => {
        const fullStars = Math.floor(score);
        const hasHalfStar = score % 1 >= 0.5;
        const emptyStars = 5 - fullStars - (hasHalfStar ? 1 : 0);
        
        return (
            <div className="flex items-center">
                {[...Array(fullStars)].map((_, i) => (
                    <span key={`full-${i}`} className="text-yellow-400 text-lg">★</span>
                ))}
                {hasHalfStar && <span className="text-yellow-400 text-lg">☆</span>}
                {[...Array(emptyStars)].map((_, i) => (
                    <span key={`empty-${i}`} className="text-gray-300 text-lg">☆</span>
                ))}
                <span className="ml-2 text-sm text-gray-600">({score})</span>
            </div>
        );
    };

    // 날짜 포맷팅 함수
    const formatDate = (dateString) => {
        const date = new Date(dateString);
        return date.toLocaleDateString('ko-KR', {
            year: 'numeric',
            month: 'long',
            day: 'numeric'
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
                                <div className="px-3 py-2 border rounded bg-white text-black">
                                    {formatLocalTime(product.taskTime)} 대여
                                </div>
                            </div>
                        )}

                        {/* 옵션 창 */}
                        <div className="space-y-4">
                            {(product.optionList || product.option_list || []).map((opt, i) => {
                                const optionDetails = opt.optionDtList || opt.option_dt_list || [];
                                const selected = selectedOptions[opt.name];
                                const found = optionDetails.find(dt => (dt.opDtName || dt.op_dt_name) === selected);
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
                                            {optionDetails.map((item, idx) => (
                                                <option key={idx} value={item.opDtName || item.op_dt_name}>
                                                    {(item.opDtName || item.op_dt_name)}
                                                    {` (₩${(item.plusCost || item.plus_cost || 0).toLocaleString()}${formatOptionTime(item.plusTime) ? ', ' + formatOptionTime(item.plusTime) : ''})`}
                                                </option>
                                            ))}
                                        </select>
                                        {selected && found && (
                                            <div className="text-sm text-purple-700 mt-1">
                                                선택: {selected} {(found.plusCost || found.plus_cost) ? `(+₩${(found.plusCost || found.plus_cost).toLocaleString()})` : ""}
                                            </div>
                                        )}
                                    </div>
                                );
                            })}
                        </div>

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
                            <button
                                className="w-full flex justify-center items-center gap-2 bg-purple-500 text-white font-semibold py-3 rounded shadow disabled:bg-gray-300 disabled:text-gray-400"
                                onClick={() => setShowSchedule(true)}
                                disabled={!isAllEssentialSelected()}
                            >
                                <span>🛒</span> 장바구니 담기
                            </button>
                            <button className="w-12 h-12 border bg-white rounded flex items-center justify-center text-purple-500 text-xl">
                                ❤️
                            </button>
                        </div>
                        {!isAllEssentialSelected() && (
                            <div className="text-red-500 text-sm mt-2">필수 옵션을 모두 선택해주세요.</div>
                        )}
                        <button 
                            className="bg-pink-400 text-white font-semibold py-3 rounded"
                            onClick={() => setShowChat(true)}
                        >
                            1:1 채팅하기
                        </button>
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
                            {loadingReviews ? (
                                <div className="text-center py-8">
                                    <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-purple-600 mx-auto"></div>
                                    <p className="mt-2 text-gray-500">리뷰를 불러오는 중...</p>
                                </div>
                            ) : reviews && reviews.length > 0 ? (
                                <div className="space-y-6">
                                    {reviews.map((review, idx) => (
                                        <div key={idx} className="bg-white p-4 rounded-lg border shadow-sm">
                                            {/* 리뷰 헤더 */}
                                            <div className="flex justify-between items-start mb-3">
                                                <div>
                                                    <div className="font-semibold text-gray-800">{review.nickname}</div>
                                                    <div className="text-sm text-gray-500">{formatDate(review.createdAt)}</div>
                                                </div>
                                                <div className="text-right">
                                                    {renderStars(review.score)}
                                                </div>
                                            </div>
                                            
                                            {/* 리뷰 내용 */}
                                            <div className="mb-4">
                                                <p className="text-gray-700 leading-relaxed whitespace-pre-wrap">
                                                    {review.content}
                                                </p>
                                            </div>
                                            
                                            {/* 리뷰 이미지 */}
                                            {review.imageUrlList && review.imageUrlList.length > 0 && (
                                                <div className="flex gap-2 overflow-x-auto">
                                                    {review.imageUrlList.map((imageUrl, imgIdx) => (
                                                        <img
                                                            key={imgIdx}
                                                            src={imageUrl.startsWith('http') ? imageUrl : baseUrl + imageUrl}
                                                            alt={`리뷰 이미지 ${imgIdx + 1}`}
                                                            className="w-20 h-20 object-cover rounded border"
                                                            onError={(e) => {
                                                                e.target.style.display = 'none';
                                                            }}
                                                        />
                                                    ))}
                                                </div>
                                            )}
                                        </div>
                                    ))}
                                </div>
                            ) : (
                                <div className="text-center py-8">
                                    <p className="text-gray-400 text-lg">아직 리뷰가 없습니다.</p>
                                    <p className="text-gray-300 text-sm mt-1">첫 번째 리뷰를 작성해보세요!</p>
                                </div>
                            )}
                        </div>
                    )}
                </div>
            </div>

            {/* ScheduleSelect 모달 */}
            {showSchedule && (
                <div className="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-40">
                    <div className="bg-white rounded-lg shadow-lg p-8">
                        <ScheduleSelect
                            productId={product.id}
                            cpId={product.cpId}
                            onSelect={async ({ localDateTime }) => {
                                try {
                                    // cart_detail 테이블용 데이터
                                    const cartDetailData = [];
                                    // 옵션 (optionList/option_list)
                                    const optionArr = product.optionList || product.option_list || [];
                                    optionArr.forEach(opt => {
                                        const selected = selectedOptions[opt.name];
                                        if (selected) {
                                            const found = (opt.optionDtList || opt.option_dt_list || []).find(dt => (dt.opDtName || dt.op_dt_name) === selected);
                                            if (found) {
                                                cartDetailData.push({
                                                    pd_id: product.id ?? null,
                                                    op_id: opt.opId ?? null,
                                                    op_dt_id: found.opDtId ?? null,
                                                    cart_dt_quantity: 1,
                                                    op_name: opt.name,
                                                    op_dt_name: found.opDtName ?? '',
                                                    op_price: found.plusCost ?? 0,
                                                    op_tasktime: safeMinutesToLocalTime(found.plusTime)
                                                });
                                            }
                                        }
                                    });
                                    // 사이즈 옵션
                                    if (product.sizeList && selectedOptions['size']) {
                                        const found = product.sizeList.find(sz => sz.size === selectedOptions['size']);
                                        if (found) {
                                            cartDetailData.push({
                                                pd_id: product.id ? product.id : null,
                                                op_id: 0,
                                                op_dt_id: found.sizeId ? found.sizeId : null,
                                                cart_dt_quantity: 1,
                                                op_name: '사이즈',
                                                op_dt_name: found.size,
                                                op_price: found.plus_cost || found.plusCost || 0,
                                                op_tasktime: safeMinutesToLocalTime(found.plus_time || found.plusTime)
                                            });
                                        }
                                    }

                                    // cart 테이블용 데이터
                                    const cartData = {
                                        pd_id: product.id,
                                        cp_id: product.cpId,
                                        cp_name: product.companyName || '회사명',
                                        pd_name: product.name,
                                        pd_price: product.price,
                                        pd_image_url: product.image_url_list?.[0] || '',
                                        start_datetime: localDateTime,
                                        cart_quantity: 1,
                                        task_time: arrayToLocalTime(product.taskTime) || null,
                                        category: product.thiscate,
                                        option_details: cartDetailData
                                    };

                                    console.log('장바구니 cartData:', cartData);

                                    // cartData만 전송
                                    const response = await apiClient.post('/user/cart/add/product', cartData);
                                    console.log("Add Cart data : ", response);
                                    if (response.data?.result?.resultCode === 200) {
                                        toast.success('장바구니에 추가되었습니다.');
                                        setShowSchedule(false);
                                    } else {
                                        toast.error('장바구니 추가에 실패했습니다.');
                                    }
                                } catch (error) {
                                    console.error('장바구니 추가 중 오류 발생:', error);
                                    toast.error('장바구니 추가 중 오류가 발생했습니다.');
                                }
                            }}
                        />
                        <button className="mt-4 w-full py-2 bg-gray-300 rounded" onClick={() => setShowSchedule(false)}>닫기</button>
                    </div>
                </div>
            )}

            {/* ChattingModal */}
            {showChat && (
                <ChattingModal 
                    companyId={product.cpId} 
                    onClose={() => setShowChat(false)} 
                />
            )}
        </div>
    );
};

export default ProductDetail;