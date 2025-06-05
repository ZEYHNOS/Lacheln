import React, { useState, useEffect } from "react";
import { useLocation } from "react-router-dom";
import tosspay from "../../../../image/Payment/tosspay.png";
import kakaopay from "../../../../image/Payment/kakaopay.png";
import payco from "../../../../image/Payment/payco.png";
import axios from "axios";
import apiClient from "../../../../lib/apiClient";

// 더미 쿠폰데이터 예시
const dummyCouponList = [
    { id: 1, name: "WELCOME쿠폰", discount: 5000 },
    { id: 2, name: "10%할인쿠폰", discount: 10000 },
];

// 결제 상품명 생성(패키지명 + 단품명)
function getPaymentName(packageList, singleList) {
    let names = [];
    if (packageList.length > 0) {
        names.push(...packageList.map(pkg => pkg.packName));
    }
    if (singleList.length > 0) {
        names.push(...singleList.map(item => item.pdName));
    }
    return names.join(' + ');
}

export default function ChoicePayment() {
    const location = useLocation();
    const checkedItems = location.state?.checkedItems || [];
    const [selectedPg, setSelectedPg] = useState(null);
    const [selectedCouponId, setSelectedCouponId] = useState("");
    const [couponList] = useState(dummyCouponList);
    const [usePoint, setUsePoint] = useState(0);
    const [inputPoint, setInputPoint] = useState("");
    const [userName, setUserName] = useState("");
    const [userEmail, setUserEmail] = useState("");
    const [mileage, setMileage] = useState(0);

  // 패키지/단품 분리
    const packageList = checkedItems.filter(item => item.packId);
    const singleList = checkedItems.filter(item => !item.packId);

    // 유저 정보 불러오기 
    useEffect(() => {
        async function fetchUser() {
            try {
                const userprofile = await apiClient.get('/user/profile', {withCredentials: true});
                setUserName(userprofile.data.data.name);
                setUserEmail(userprofile.data.data.email);
                setMileage(userprofile.data.data.mileage);
            } catch (error) {
                console.log(error);
            }
        }
        fetchUser();
    }, []);

    // 결제 금액 계산
    // 1. 총 상품금액: 패키지 원가 + 단품 가격
    const totalProductPrice =
        packageList.reduce((sum, pkg) => sum + (pkg.products[0]?.price || 0), 0) +
        singleList.reduce((sum, item) => sum + (item.price || 0), 0);

    // 2. 총 추가금액: 옵션 가격
    const totalExtraPrice =
        packageList.reduce(
            (sum, pkg) =>
                sum +
                pkg.products.reduce(
                    (pSum, p) =>
                        pSum +
                        (p.cartDetails
                            ? p.cartDetails.reduce((dSum, d) => dSum + (d.detailPrice || 0), 0)
                            : 0),
                    0
                ),
            0
        ) +
        singleList.reduce(
            (sum, item) =>
                sum +
                (item.cartDetails
                    ? item.cartDetails.reduce((dSum, d) => dSum + (d.detailPrice || 0), 0)
                    : 0),
            0
        );

    // 3. 총 할인금액: 패키지/단품 할인
    const totalDiscount =
        packageList.reduce((sum, pkg) => sum + (pkg.discountPrice || 0), 0) +
        singleList.reduce((sum, item) => sum + (item.discountPrice || 0), 0) +
        usePoint;

    // 4. 최종 결제금액
    const finalPrice = totalProductPrice + totalExtraPrice - totalDiscount;

    // 5. 포인트 적립 예상
    const Lachelnpoint = Math.floor(finalPrice * 0.015);

    // 결제하기 버튼처리리
    const handlePayment = async () => {
        try {
            // 1. 백엔드에 merchant_uid 요청(결제고유번호 백엔드에서 생성)
            const merchant = await apiClient.post('/payment/verify');
            const merchant_uid = merchant.data.merchant_uid;
            
            // 2. 결제금액 요청(백엔드에서 유효성 검사 및 결제금액 반환)
            const payment_verify = await apiClient.post('/payment/verify', {
                cartId,
                mileage: usePoint,
                couponId: selectedCouponId,
            }, {withCredentials: true});
            const finally_price = payment_verify.data.finally_price;

            // 3. PG사 초기화
            const { IMP } = window;
            IMP.init(process.env.REACT_APP_PORTONE_IDENTIFYCODE);

            // 4. PG사 결제 요청
            const paymentName = getPaymentName(packageList, singleList);
            console.log("결제 요청 파라미터", {
                pg: selectedPg,
                pay_method: "card",
                merchant_uid,
                name: paymentName,
                amount: finally_price,
                buyer_name: userName,
                buyer_email: userEmail,
            });
            IMP.request_pay(
                {
                    pg: selectedPg,
                    pay_method: "card",
                    merchant_uid,
                    name: paymentName,
                    amount: finally_price,
                    buyer_name: userName,
                    buyer_email: userEmail,
                },
                async function (rsp) {
                    if (rsp.success) {
                        // 4. 결제 성공: 필요하면 백엔드에 검증 요청
                        await axios.post('/api/payment/confirm', {
                            imp_uid: rsp.imp_uid,
                            merchant_uid: rsp.merchant_uid
                        });
                        alert("결제가 완료되었습니다!");
                    } else {
                        alert(`결제 실패: ${rsp.error_msg}`);
                    }
                }
            );
        } catch (err) {
            console.log(err);
            alert("결제 준비 중 오류가 발생했습니다.");
        }
    };

    return (
        <div className="max-w-4xl mx-auto p-6 bg-white min-h-screen">
            <h1 className="font-bold text-lg text-black mb-8">결제 정보</h1>
            {/* 상품확인 */}
            <section className="mb-2">
                <div className="flex border-b mb-2">
                <div className="flex-1 text-center text-black font-bold py-2 border-r">상품정보</div>
                <div className="w-1/4 text-center text-black font-bold py-2">상품금액</div>
                </div>
                {/* 패키지 상품 */}
                {packageList.map(pkg => {
                    const firstProduct = pkg.products[0];
                    return (
                        <div key={pkg.packId} className="flex items-center border-b py-4">
                            {/* 대표 이미지 */}
                            <div className="flex-1 flex items-start">
                                {firstProduct && (
                                <img src={firstProduct.pdImageUrl} alt={firstProduct.pdName} className="w-27 h-36 object-cover rounded mr-4" />
                                )}
                                <div>
                                    <div className="font-bold text-purple-700 text-lg mb-1">[패키지] {pkg.packName}</div>
                                    {pkg.products.map(p => (
                                        <div key={p.cartId} className="flex items-center text-black text-semibold">
                                        <span>[{p.cpName}] {p.pdName}</span>
                                        </div>
                                    ))}
                                    <div className="text-xs text-gray-500 mb-1">
                                        예약일시: {firstProduct && firstProduct.startTime ? `${firstProduct.startTime[0]}-${String(firstProduct.startTime[1]).padStart(2, '0')}-${String(firstProduct.startTime[2]).padStart(2, '0')} ${String(firstProduct.startTime[3]).padStart(2, '0')}:${String(firstProduct.startTime[4]).padStart(2, '0')}` : '-'}
                                        {firstProduct && firstProduct.taskTime ? ` / 소요시간: ${firstProduct.taskTime[0] > 0 ? firstProduct.taskTime[0] + '시간 ' : ''}${firstProduct.taskTime[1] > 0 ? firstProduct.taskTime[1] + '분' : ''}` : ''}
                                    </div>
                                </div>
                            </div>
                            {/* 오른쪽: 가격/원가/할인금액 */}
                            <div className="w-1/4 text-right flex flex-col items-end">
                                <div className="font-bold text-purple-700 text-xl">{(pkg.price - (pkg.discountPrice || 0)).toLocaleString()}원</div>
                                <div className="text-black font-bold mt-1 text-xs">원가 {pkg.products[0]?.price?.toLocaleString() || 0}원</div>
                                <div className="text-pink-600 font-bold mt-1 text-xs">할인: -{pkg.discountPrice.toLocaleString()}원</div>
                            </div>
                        </div>
                    );
                })}
                {/* 단품 상품 */}
                {singleList.map(item => (
                    <div key={item.cartId} className="flex items-center border-b py-4">
                        <img
                            src={item.pdImageUrl}
                            alt={item.pdName}
                            className="w-27 h-36 object-cover rounded mr-4"
                        />
                        <div className="flex-1">
                            <div className="font-semibold text-black whitespace-nowrap">
                                [{item.cpName}] {item.pdName}
                            </div>
                            <div className="text-xs text-gray-500 whitespace-nowrap">
                                예약일시: {item.startTime ? `${item.startTime[0]}-${String(item.startTime[1]).padStart(2, '0')}-${String(item.startTime[2]).padStart(2, '0')} ${String(item.startTime[3]).padStart(2, '0')}:${String(item.startTime[4]).padStart(2, '0')}` : '-'}
                                {item.taskTime ? ` / 소요시간: ${item.taskTime[0] > 0 ? item.taskTime[0] + '시간 ' : ''}${item.taskTime[1] > 0 ? item.taskTime[1] + '분' : ''}` : ''}
                            </div>
                            {item.cartDetails && item.cartDetails.map(detail => (
                                <div key={detail.cartDtId} className="text-xs text-gray-600 whitespace-nowrap">
                                    {detail.optionName}: {detail.optionDetailName} {detail.detailPrice > 0 ? `(+${detail.detailPrice.toLocaleString()}원)` : ''} {detail.detailTaskTime ? `${detail.detailTaskTime[0] > 0 ? detail.detailTaskTime[0] + '시간 ' : ''}${detail.detailTaskTime[1] > 0 ? detail.detailTaskTime[1] + '분' : ''}` : ''}
                                </div>
                            ))}
                        </div>
                        <div className="w-1/4 text-right font-bold text-lg text-black">
                            {item.price?.toLocaleString()}원
                        </div>
                    </div>
                ))}
            </section>

            {/* 쿠폰 및 마일리지 적용 */}
            <section className="mb-2">
                <h2 className="font-bold text-lg text-purple-700 mb-2 ">쿠폰 · 마일리지</h2>
                <div className="flex w-full gap-4 items-end mb-2">
                    {/* 쿠폰 선택 */}
                    <div className="flex-1">
                        <label className="block text-xs text-gray-500 mb-1">쿠폰</label>
                        <select
                            className="border border-purple-700 rounded px-4 py-2 bg-white text-black w-full h-12"
                            value={selectedCouponId}
                            onChange={e => setSelectedCouponId(e.target.value)}
                        >
                            <option value="">쿠폰 선택</option>
                            {couponList.map(coupon => (
                                <option key={coupon.id} value={coupon.id}>
                                    {coupon.name} ({coupon.discount.toLocaleString()}원)
                                </option>
                            ))}
                        </select>
                    </div>
                    {/* 포인트 입력 */}
                    <div className="flex-1">
                        <label className="block text-xs text-gray-500 mb-1">
                            마일리지 <span className="text-purple-700 font-bold">{mileage.toLocaleString()}P</span> 보유
                        </label>
                        <div className="flex">
                            <input
                                type="text"
                                inputMode="numeric"
                                pattern="[0-9]*"
                                className="border border-purple-700 rounded px-4 py-2 bg-white text-black w-full h-12"
                                placeholder="사용 포인트"
                                value={inputPoint}
                                onChange={e => {
                                    const onlyNum = e.target.value.replace(/[^0-9]/g, "");
                                    setInputPoint(onlyNum);
                                }}
                                min={0}
                                max={mileage}
                            />
                            <button
                                className="ml-2 px-4 h-12 min-w-[64px] bg-purple-700 text-white rounded flex items-center justify-center leading-none whitespace-nowrap"
                                onClick={() => {
                                    // 입력값이 mileage보다 크면 mileage로 제한
                                    setUsePoint(Math.max(0, Math.min(mileage, Number(inputPoint))));
                                }}
                            >사용</button>
                        </div>
                    </div>
                </div>
                <div className="mt-4 text-right text-xs text-gray-600">
                    Lacheln포인트 적립 예상: <span className="font-bold text-purple-700">{Lachelnpoint.toLocaleString()} P (1.5% 적립)</span>
                </div>
            </section>

            {/* 결제금액 계산 */}
            <section>
                <h2 className="font-bold text-lg text-purple-700 mb-8">결제금액</h2>
                <div className="grid grid-cols-5 gap-2 text-center font-bold border-b pb-2 mb-8">
                    <div>
                        <div className="text-gray-500 text-sm">총 상품금액</div>
                        <div className="text-black text-lg">{totalProductPrice.toLocaleString()}원</div>
                    </div>
                    <div>
                        <div className="text-gray-500 text-sm">총 추가금액</div>
                        <div className="text-black text-lg">{totalExtraPrice.toLocaleString()}원</div>
                    </div>
                    <div>
                        <div className="text-gray-500 text-sm">총 할인금액</div>
                        <div className="text-red-500 text-lg">{totalDiscount.toLocaleString()}원</div>
                    </div>
                    <div className="text-black text-lg">=</div>
                    <div>
                        <div className="text-gray-500 text-sm">최종 결제금액</div>
                        <div className="text-purple-700 text-lg">{finalPrice.toLocaleString()}원</div>
                    </div>
                </div>
            </section>

            {/* 결제수단 */}
            <section className="mb-8">
                <h2 className="font-bold text-lg text-purple-700 mb-8">결제수단 선택</h2>
                <div className="flex w-full gap-8">
                    {/* 왼쪽: 결제수단 선택 영역 */}
                    <div className="flex-1">
                        <div className="w-full">
                            {/* 윗줄: 신용·체크카드 */}
                            <div className="flex w-full justify-center mb-8">
                                <button
                                    className={`w-full w-3/4 py-5 rounded font-bold text-lg border border-purple-700 transition
                                        ${selectedPg === "nice" ? "bg-purple-700 text-white" : "bg-white text-purple-700"}
                                    `}
                                    onClick={() => setSelectedPg("nice")}
                                    type="button"
                                >
                                    신용·체크카드
                                </button>
                            </div>
                            {/* 아랫줄: 간편결제 아이콘 */}
                            <div className="flex w-full justify-center">
                                {/* 토스페이 */}
                                <div className="flex justify-center">
                                    <button
                                        className={`w-48 h-24 rounded flex items-center justify-center border transition
                                            ${selectedPg === "tosspay" ? "bg-blue-300" : "bg-white"}
                                        `}
                                        onClick={() => setSelectedPg("tosspay")}
                                        type="button"
                                    >
                                        <img src={tosspay} alt="토스페이" className="w-12 h-12" />
                                    </button>
                                </div>
                                {/* 카카오페이 */}
                                <div className="flex justify-center">
                                    <button
                                        className={`w-48 h-24 rounded flex items-center justify-center border transition
                                            ${selectedPg === "kakaopay" ? "bg-yellow-100" : "bg-white"}
                                        `}
                                        onClick={() => setSelectedPg("kakaopay")}
                                        type="button"
                                    >
                                        <img src={kakaopay} alt="카카오페이" className="w-12 h-12" />
                                    </button>
                                </div>
                                {/* 페이코 */}
                                <div className="flex justify-center">
                                    <button
                                        className={`w-48 h-24 rounded flex items-center justify-center border transition
                                            ${selectedPg === "payco" ? "bg-red-300" : "bg-white"}
                                        `}
                                        onClick={() => setSelectedPg("payco")}
                                        type="button"
                                    >
                                        <img src={payco} alt="페이코" className="w-12 h-12" />
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                    {/* 오른쪽: 결제 안내 및 결제 버튼 */}
                    <div className="w-80 flex flex-col items-center justify-between bg-white rounded p-6 border">
                        <div className="mb-4 w-full text-xs text-gray-700">
                            <div className="font-bold mb-2">개인정보 수집 및 이용 동의</div>
                            <div className="mb-2">개인정보 수집 및 이용 동의 <span className="text-purple-700 underline cursor-pointer">보기</span></div>
                            <div className="mb-2">상품, 가격, 할인 정보 등을 확인하였으며 결제에 동의합니다.</div>
                        </div>
                        <button className="w-full bg-purple-600 text-white px-8 py-3 rounded font-bold text-lg mt-4" onClick={handlePayment}>
                            결제하기
                        </button>
                    </div>
                </div>
            </section>
        </div>
    );
}
