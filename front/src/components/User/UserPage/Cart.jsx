import React, { useEffect, useState } from "react";
import apiClient from "../../../lib/apiClient";
import { toast } from "react-toastify";

const userId = "user_id"; // 실제 로그인 정보에서 받아와야 함

export default function Cart() {
    const [cartList, setCartList] = useState([]);
    const [checked, setChecked] = useState([]);
    const [allChecked, setAllChecked] = useState(false);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        setLoading(true);
        apiClient.get(`/user/cart/search`)
            .then(res => {
                const list = res.data.data || [];
                setCartList(list);
                // 패키지/단품 분리 및 그룹화
                const packageMap = {};
                const singleList = [];
                list.forEach(item => {
                    if (item.packId) {
                        if (!packageMap[item.packId]) {
                            packageMap[item.packId] = {
                                ...item,
                                products: []
                            };
                        }
                        packageMap[item.packId].products.push(item);
                    } else {
                        singleList.push(item);
                    }
                });
                const packageList = Object.values(packageMap);
                // 패키지ID + 단품ID 모두 checked에 포함
                setChecked([
                    ...packageList.map(pkg => pkg.packId),
                    ...singleList.map(item => item.cartId)
                ]);
                setAllChecked(true);
            })
            .catch(() => {
                setCartList([]);
            })
            .finally(() => setLoading(false));
    }, []);

    // 패키지/단품 분리 및 그룹화
    const packageMap = {};
    const singleList = [];
    cartList.forEach(item => {
        if (item.packId) {
            if (!packageMap[item.packId]) {
                packageMap[item.packId] = {
                    ...item,
                    products: []
                };
            }
            packageMap[item.packId].products.push(item);
        } else {
            singleList.push(item);
        }
    });
    const packageList = Object.values(packageMap);

    // 패키지/단품 체크박스 관리
    const checkedPackageIds = checked.filter(id => packageList.some(pkg => pkg.packId === id));
    const checkedSingleIds = checked.filter(id => singleList.some(item => item.cartId === id));

    // 전체 선택/해제 (패키지+단품)
    const handleAllCheck = () => {
        if (allChecked) {
            setChecked([]);
        } else {
            const allIds = [
                ...packageList.map(pkg => pkg.packId),
                ...singleList.map(item => item.cartId)
            ];
            setChecked(allIds);
        }
        setAllChecked(!allChecked);
    };

    // 패키지 체크/해제
    const handlePackageCheck = (packId) => {
        setChecked(prev =>
            prev.includes(packId) ? prev.filter(id => id !== packId) : [...prev, packId]
        );
    };
    // 단품 체크/해제
    const handleCheck = (cartId) => {
        setChecked(prev =>
            prev.includes(cartId) ? prev.filter(id => id !== cartId) : [...prev, cartId]
        );
    };

    // 금액 계산 (패키지 단위 + 단품)
    const totalProductPrice = [
        ...packageList.filter(pkg => checked.includes(pkg.packId)),
        ...singleList.filter(item => checked.includes(item.cartId))
    ].reduce((sum, item) => sum + (item.price || 0), 0);

    const totalExtraPrice = cartList
        .filter(item => checked.includes(item.cartId))
        .reduce((sum, item) => {
            if (!item.cartDetails) return sum;
            return sum + item.cartDetails.reduce((dSum, d) => dSum + (d.detailPrice || 0), 0);
        }, 0);

    // 패키지/단품 할인금액 계산 (패키지별 discountPrice는 하나만, 단품은 개별)
    const totalDiscount = [
        ...packageList.filter(pkg => checked.includes(pkg.packId)).map(pkg => pkg.discountPrice || 0),
        ...singleList.filter(item => checked.includes(item.cartId)).map(item => item.discountPrice || 0)
    ].reduce((sum, d) => sum + d, 0);

    const finalPrice = totalProductPrice + totalExtraPrice - totalDiscount;

    // 시간 포맷 변환 함수
    const formatDateTime = (arr) => {
        if (!Array.isArray(arr) || arr.length < 5) return '';
        const [y, m, d, h, min] = arr;
        return `${y}-${String(m).padStart(2, '0')}-${String(d).padStart(2, '0')} ${String(h).padStart(2, '0')}:${String(min).padStart(2, '0')}`;
    };
    const formatTaskTime = (arr) => {
        if (!Array.isArray(arr) || arr.length < 2) return '';
        const [h, m] = arr;
        return `${h > 0 ? h + '시간 ' : ''}${m > 0 ? m + '분' : ''}`.trim();
    };

    // 삭제 함수 (패키지/단품 통합)
    const handleDeleteCart = async () => {
        if (checked.length === 0) {
            toast.warning('삭제할 상품을 선택하세요.');
            return;
        }
        if (!window.confirm('정말 삭제하시겠습니까?')) return;
        // 패키지에 속한 상품들의 cartId도 모두 삭제
        const deleteCartIds = [
            ...packageList.filter(pkg => checked.includes(pkg.packId)).flatMap(pkg => pkg.products.map(p => p.cartId)),
            ...singleList.filter(item => checked.includes(item.cartId)).map(item => item.cartId)
        ];
        try {
            const response = await apiClient.delete('/user/cart/delete', { data: { cartIds: deleteCartIds } });
            if (response.data.result.resultCode === 200) {
                toast.success('상품이 삭제되었습니다.');
                setCartList(prev => prev.filter(item => !deleteCartIds.includes(item.cartId)));
                setChecked([]);
                setAllChecked(false);
            }
        } catch (e) {
            toast.error('삭제 중 오류가 발생했습니다.');
        }
    };

    return (
        <div className="flex flex-col min-h-screen text-black" style={{ background: 'white' }}>
            <div className="flex-1 max-w-4xl mx-auto p-6 w-full">
                {/* 상단 헤더 */}
                <div className="flex items-center border-b pb-2 mb-4">
                    <span className="w-2/3 text-center font-bold">상품정보</span>
                    <span className="w-1/3 text-right font-bold">상품금액</span>
                </div>

                {/* 일괄선택 & 삭제버튼  */}
                <div className="flex items-center border-b pb-2 mb-4">
                    <input
                        type="checkbox"
                        checked={allChecked}
                        onChange={handleAllCheck}
                        className="mr-2 accent-[#845EC2] w-5 h-5"
                        style={{ accentColor: '#845EC2' }}
                    />
                    <span className="font-bold flex-1">일괄선택</span>
                    <button
                        className="ml-2 px-4 py-1 border border-red-500 bg-red-50 text-red-500 rounded hover:bg-red-600 hover:text-white hover:border-red-600 shadow transition-colors"
                        style={{ minWidth: 64, minHeight: 36 }}
                        onClick={handleDeleteCart}
                    >
                        삭제
                    </button>
                </div>

                {/* 상품 리스트 or 비었을 때 메시지 */}
                {loading ? (
                    <div className="text-center py-20 text-gray-500">장바구니를 불러오는 중입니다...</div>
                ) : packageList.length === 0 && singleList.length === 0 ? (
                    <div className="text-center py-20 text-gray-500">장바구니가 비었습니다.</div>
                ) : (
                    <>
                        {/* 패키지 상품 */}
                        {packageList.map(pkg => {
                            const firstProduct = pkg.products[0];
                            return (
                                <div key={pkg.packId} className="flex items-center border-b py-4">
                                    <input
                                        type="checkbox"
                                        checked={checked.includes(pkg.packId)}
                                        onChange={() => handlePackageCheck(pkg.packId)}
                                        className="mr-2 accent-[#845EC2] w-5 h-5"
                                        style={{ accentColor: '#845EC2' }}
                                    />
                                    {/* 왼쪽: 이미지, 상품명, 상세정보 */}
                                    <div className="flex-1 flex items-start">
                                        {firstProduct && (
                                            <img src={firstProduct.pdImageUrl} alt={firstProduct.pdName} className="w-24 h-24 object-cover rounded mr-4" />
                                        )}
                                        <div>
                                            <div className="font-bold text-black text-lg mb-1">[패키지] {pkg.packName}</div>
                                            {pkg.products.map(p => (
                                                <div key={p.cartId} className="flex items-center text-semibold">
                                                    <span>[{p.cpName}] {p.pdName}</span>
                                                </div>
                                            ))}
                                            <div className="text-xs text-gray-500 mb-1">
                                                예약일시: {formatDateTime(firstProduct.startTime)} / 소요시간: {formatTaskTime(firstProduct.taskTime)}
                                            </div>
                                        </div>
                                    </div>
                                    {/* 오른쪽: 가격/원가/할인금액 */}
                                    <div className="w-1/3 text-right flex flex-col items-end">
                                        <div className="font-bold text-purple-700 text-xl">{pkg.price?.toLocaleString()}원</div>
                                        <div className="text-black font-bold mt-1 text-xs">원가 {pkg.products[0]?.price?.toLocaleString() || 0}원</div>
                                        <div className="text-pink-600 font-bold mt-1 text-xs">할인: -{pkg.discountPrice.toLocaleString()}원</div>
                                    </div>
                                </div>
                            );
                        })}
                        {/* 단품 상품 */}
                        {singleList.map(item => (
                            <div key={item.cartId} className="flex items-center border-b py-4">
                                <input
                                    type="checkbox"
                                    checked={checked.includes(item.cartId)}
                                    onChange={() => handleCheck(item.cartId)}
                                    className="mr-2 accent-[#845EC2] w-5 h-5"
                                    style={{ accentColor: '#845EC2' }}
                                />
                                <img
                                    src={item.pdImageUrl}
                                    alt={item.pdName}
                                    className="w-24 h-24 object-cover rounded mr-4"
                                />
                                <div className="flex-1">
                                    <div className="font-semibold text-black whitespace-nowrap">
                                        [{item.cpName}] {item.pdName}
                                    </div>
                                    <div className="text-xs text-gray-500 whitespace-nowrap">
                                        예약일시: {formatDateTime(item.startTime)} / 소요시간: {formatTaskTime(item.taskTime)}
                                    </div>
                                    {item.cartDetails && item.cartDetails.map(detail => (
                                        <div key={detail.cartDtId} className="text-xs text-gray-600 whitespace-nowrap">
                                            {detail.optionName}: {detail.optionDetailName} {detail.detailPrice > 0 ? `(+${detail.detailPrice.toLocaleString()}원)` : ''} {formatTaskTime(detail.detailTaskTime)}
                                        </div>
                                    ))}
                                </div>
                                <div className="w-1/3 text-right font-bold text-lg text-black">
                                    {item.price?.toLocaleString()}원
                                </div>
                            </div>
                        ))}
                    </>
                )}

                {/* 하단 금액 요약 */}
                <div className="mt-8 border-t pt-4 grid grid-cols-5 gap-2 text-center font-bold">
                    <div>
                        <div className="text-gray-500 text-sm">총 상품금액</div>
                        <div className="text-black">{totalProductPrice.toLocaleString()}원</div>
                    </div>
                    <div>
                        <div className="text-gray-500 text-sm">옵션 추가금액</div>
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
