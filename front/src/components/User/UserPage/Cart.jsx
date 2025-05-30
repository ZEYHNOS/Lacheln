import React, { useEffect, useState } from "react";
import axios from "axios";

const userId = "user_id"; // 실제 로그인 정보에서 받아와야 함

export default function Cart() {
    const [cartList, setCartList] = useState([]);
    const [checked, setChecked] = useState([]);
    const [allChecked, setAllChecked] = useState(false);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        setLoading(true);
        axios.get(`/user/cart/search/${userId}`)
            .then(res => {
                const list = res.data.cartList || [];
                setCartList(list);
                setChecked(list.map(item => item.cart_id));
                setAllChecked(true);
            })
            .catch(() => {
                setCartList([]);
            })
            .finally(() => setLoading(false));
    }, []);

    // 전체 선택/해제
    const handleAllCheck = () => {
        if (allChecked) {
            setChecked([]);
        } else {
            setChecked(cartList.map(item => item.cart_id));
        }
        setAllChecked(!allChecked);
    };

    // 개별 선택/해제
    const handleCheck = (cartId) => {
        setChecked(prev =>
            prev.includes(cartId) ? prev.filter(id => id !== cartId) : [...prev, cartId]
        );
    };

    // 금액 계산
    const totalProductPrice = cartList
        .filter(item => checked.includes(item.cart_id))
        .reduce((sum, item) => sum + (item.pd_price || 0), 0);

    // 옵션 추가금, 할인금액 등은 실제 cart_detail 데이터에 맞게 추가 구현 필요
    const totalExtraPrice = 0;
    const totalDiscount = 0;
    const finalPrice = totalProductPrice + totalExtraPrice - totalDiscount;

    return (
        <div className="flex flex-col min-h-screen text-black" style={{ background: 'white' }}>
            <div className="flex-1 max-w-4xl mx-auto p-6 w-full">
                {/* 상단 헤더 */}
                <div className="flex items-center border-b pb-2 mb-4">
                    <input
                        type="checkbox"
                        checked={allChecked}
                        onChange={handleAllCheck}
                        className="mr-2"
                    />
                    <span className="font-bold text-lg flex-1">일괄선택</span>
                    <span className="w-2/3 text-center font-bold">상품정보</span>
                    <span className="w-1/3 text-right font-bold">상품금액</span>
                </div>

                {/* 상품 리스트 or 비었을 때 메시지 */}
                {loading ? (
                    <div className="text-center py-20 text-gray-500">장바구니를 불러오는 중입니다...</div>
                ) : cartList.length === 0 ? (
                    <div className="text-center py-20 text-gray-500">장바구니가 비었습니다.</div>
                ) : (
                    cartList.map(item => (
                        <div key={item.cart_id} className="flex items-center border-b py-4">
                            <input
                                type="checkbox"
                                checked={checked.includes(item.cart_id)}
                                onChange={() => handleCheck(item.cart_id)}
                                className="mr-2"
                            />
                            <img
                                src={item.pd_image_url}
                                alt={item.pd_name}
                                className="w-24 h-24 object-cover rounded mr-4"
                            />
                            <div className="flex-1">
                                <div className="font-semibold text-black">[{item.category}] {item.pd_name}</div>
                                {/* 옵션/설명/상세(cart_detail) 등 추가 영역 */}
                                <div className="text-sm text-gray-500">{item.description}</div>
                                {/* 옵션 상세(cart_detail) 정보가 있으면 아래에 map으로 표시 */}
                                {/* {item.cart_detail && item.cart_detail.map(detail => (
                                    <div key={detail.cart_dt_id} className="text-xs text-gray-600 ml-2">
                                        {detail.op_name}: {detail.op_dt_name} (+{detail.op_price}원)
                                    </div>
                                ))} */}
                            </div>
                            <div className="w-1/3 text-right font-bold text-lg text-black">
                                {item.pd_price?.toLocaleString()}원
                            </div>
                        </div>
                    ))
                )}

                {/* 하단 금액 요약 */}
                <div className="mt-8 border-t pt-4 grid grid-cols-5 gap-2 text-center font-bold">
                    <div>
                        <div className="text-gray-500 text-sm">총 상품금액</div>
                        <div className="text-black">{totalProductPrice.toLocaleString()}원</div>
                    </div>
                    <div>
                        <div className="text-gray-500 text-sm">총 추가금액</div>
                        <div className="text-black">{totalExtraPrice.toLocaleString()}원</div>
                    </div>
                    <div>
                        <div className="text-gray-500 text-sm">총 할인금액</div>
                        <div className="text-black">{totalDiscount.toLocaleString()}원</div>
                    </div>
                    <div>=</div>
                    <div>
                        <div className="text-gray-500 text-sm">최종 결제금액</div>
                        <div className="text-purple-700">{finalPrice.toLocaleString()}원</div>
                    </div>
                </div>

                {/* 하단 버튼 */}
                <div className="flex gap-4 mt-6">
                    <button className="flex-1 py-3 bg-purple-600 text-white rounded font-bold">주문하기</button>
                    <button className="flex-1 py-3 bg-gray-200 text-black rounded font-bold">쇼핑계속하기</button>
                </div>
            </div>
        </div>
    );
}
