import React, { useEffect, useState, useRef } from 'react';
import Addcompany from './Addcompany';

// 더미 상품 리스트 (실제 API 연동 시 교체)
const dummyProductList = [
    { id: 101, name: '내 상품1', price: 10000, imageUrl: null },
    { id: 102, name: '내 상품2', price: 15000, imageUrl: null },
    { id: 103, name: '내 상품3', price: 15000, imageUrl: null },
    { id: 104, name: '내 상품4', price: 15000, imageUrl: null },
    { id: 105, name: '내 상품5', price: 15000, imageUrl: null },
    { id: 106, name: '내 상품6', price: 15000, imageUrl: null },
    { id: 107, name: '내 상품7', price: 15000, imageUrl: null },
    { id: 108, name: '내 상품8', price: 15000, imageUrl: null },
];

// 더미 패키지 참여자 상태 (실제 API 연동 시 교체)
const dummyParticipants = [
    { companyId: 1, name: '내 회사', selected: true, productName: '내 상품2', productPrice: 15000 },
    { companyId: 2, name: '홍길동상사', selected: true, productName: '홍길동 상품A', productPrice: 12000 },
    { companyId: 3, name: '김철수유통', selected: true, productName: '철수네 상품B', productPrice: 18000 },
];

// 내 회사 id (실제 로그인 정보에서 받아와야 함)
const myCompanyId = 1;
// 방장 여부 (실제 패키지 생성자 id와 비교)
const isOwner = true;

function AddPackage() {
    // 내 상품 선택
    const [myProductList, setMyProductList] = useState([]);
    const [selectedProductId, setSelectedProductId] = useState(null);
    const [selectDone, setSelectDone] = useState(false);

    // 패키지 정보 입력
    const [packageName, setPackageName] = useState('');
    const [discount, setDiscount] = useState(0);
    const [image, setImage] = useState(null);
    const [endDate, setEndDate] = useState('');

    // 참여자 상태 (실제 API 연동 시 서버에서 받아옴)
    const [participants, setParticipants] = useState(dummyParticipants);

    // 내 회사가 항상 맨 위에 오도록 정렬
    const sortedParticipants = [
        ...participants.filter(p => p.companyId === myCompanyId),
        ...participants.filter(p => p.companyId !== myCompanyId)
    ];

    const [dropdownOpen, setDropdownOpen] = useState(false);
    const dropdownRef = useRef(null);

    // 드롭다운 외부 클릭 시 닫기
    useEffect(() => {
        function handleClickOutside(event) {
            if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
                setDropdownOpen(false);
            }
        }
        document.addEventListener('mousedown', handleClickOutside);
        return () => document.removeEventListener('mousedown', handleClickOutside);
    }, []);

    // 내 상품 리스트 불러오기 (실제 API 연동 시 교체)
    useEffect(() => {
        // 실제로는 apiClient.get('/product/list', { withCredentials: true })
        setMyProductList(dummyProductList);
    }, []);

    // 선택 완료 버튼 클릭
    const handleSelectDone = () => {
        if (!selectedProductId) return;
        setSelectDone(true);
        // 실제로는 apiClient.post(`/package/register/{packageId}?productId=${selectedProductId}`)
        // 선택 완료 후 서버에 상태 전송
        // 서버에서 참여자 상태 갱신 후 participants도 갱신 필요
    };

    // 패키지 대표 이미지 업로드
    const handleImageChange = (e) => {
        if (e.target.files && e.target.files[0]) {
            setImage(e.target.files[0]);
        }
    };

    // 모든 참여자가 선택 완료했는지 확인
    const allSelected = participants.every(p => p.selected);

    // 등록 버튼 비활성화 조건
    const isRegisterDisabled = !packageName || !image || !endDate;

    // Addcompany 모달 오픈 상태
    const [companyModalOpen, setCompanyModalOpen] = useState(true);

    return (
        <>
            {companyModalOpen && (
                <Addcompany onClose={() => setCompanyModalOpen(false)} />
            )}
            <div className="w-full min-h-screen p-16 flex flex-row gap-16 items-start bg-white">
                {/* 왼쪽: 상품 선택 */}
                <div className="flex-1">
                    <h2 className="text-3xl font-bold mb-10 text-purple-600">패키지 상품 선택</h2>
                    <div className="mb-12">
                        <h3 className="font-semibold mb-4 text-xl text-black">내 상품 선택</h3>
                        <div className="flex items-center gap-4 w-full">
                            {/* 커스텀 셀렉트박스 */}
                            <div className="relative w-[900px]" ref={dropdownRef}>
                                <button
                                    type="button"
                                    className="w-full border-2 border-violet-400 rounded-lg px-4 py-8 flex items-center gap-8 bg-white text-left focus:outline-none focus:ring-2 focus:ring-violet-400 min-h-[180px]"
                                    onClick={() => setDropdownOpen(!dropdownOpen)}
                                    disabled={selectDone}
                                >
                                    {selectedProductId ? (
                                        <>
                                            <div className="w-24 h-32 bg-gray-100 flex items-center justify-center rounded mr-8 ml-8">
                                                {myProductList.find(prod => prod.id === selectedProductId)?.imageUrl ? (
                                                    <img src={myProductList.find(prod => prod.id === selectedProductId)?.imageUrl} alt="상품" className="w-full h-full object-cover" />
                                                ) : (
                                                    <span className="text-gray-400 text-base">No image</span>
                                                )}
                                            </div>
                                            <div className="flex flex-col justify-center">
                                                <span className="text-[#845ec2] font-bold text-2xl">{myProductList.find(prod => prod.id === selectedProductId)?.name}</span>
                                                <span className="text-gray-500 text-lg mt-2">{myProductList.find(prod => prod.id === selectedProductId)?.price.toLocaleString()}원</span>
                                            </div>
                                        </>
                                    ) : (
                                        <span className="text-gray-400 text-lg">상품을 선택하세요</span>
                                    )}
                                    <svg className="ml-auto w-6 h-6 text-violet-400" fill="none" stroke="currentColor" strokeWidth="2" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" d="M19 9l-7 7-7-7" /></svg>
                                </button>
                                {dropdownOpen && !selectDone && (
                                    <ul className="absolute z-10 mt-2 w-full bg-white border border-violet-200 rounded-lg shadow-lg max-h-96 overflow-y-auto">
                                        {myProductList.map(product => (
                                            <li
                                                key={product.id}
                                                className="flex items-center gap-8 px-4 py-8 h-[180px] cursor-pointer hover:bg-violet-50"
                                                onClick={() => { setSelectedProductId(product.id); setDropdownOpen(false); }}
                                            >
                                                <div className="w-24 h-32 bg-gray-100 flex items-center justify-center rounded mr-8 ml-8">
                                                    {product.imageUrl ? (
                                                        <img src={product.imageUrl} alt="상품" className="w-full h-full object-cover" />
                                                    ) : (
                                                        <span className="text-gray-400 text-base">No image</span>
                                                    )}
                                                </div>
                                                <div className="flex flex-col justify-center">
                                                    <span className="text-[#845ec2] font-bold text-2xl">{product.name}</span>
                                                    <span className="text-gray-500 text-lg mt-2">{product.price.toLocaleString()}원</span>
                                                </div>
                                            </li>
                                        ))}
                                    </ul>
                                )}
                            </div>
                            {/* 선택 완료 버튼 */}
                            <button
                                className="h-12 px-8 bg-violet-600 text-white font-bold rounded hover:bg-violet-700 transition-colors disabled:bg-gray-300 disabled:cursor-not-allowed text-base whitespace-nowrap"
                                disabled={!selectedProductId || selectDone}
                                onClick={handleSelectDone}
                            >
                                {selectDone ? '선택 완료' : '선택 완료'}
                            </button>
                        </div>
                    </div>
                </div>
                {/* 오른쪽: 참여 회사 상태 */}
                <div className="w-96 flex-shrink-0 mt-16">
                    <h3 className="font-semibold mb-6 text-xl text-black">참여 회사 상태</h3>
                    <ul className="space-y-4">
                        {sortedParticipants.map(p => (
                            <li key={p.companyId} className="flex items-center">
                                <span className="font-bold mr-2 text-[#845ec2] text-xl">{p.name}</span>
                                {p.companyId === myCompanyId ? (
                                    <>
                                        <span className="text-violet-600 font-bold">(나)</span>
                                        {selectedProductId && (
                                            <span className="ml-2 text-black font-normal">- {myProductList.find(prod => prod.id === selectedProductId)?.name} ({myProductList.find(prod => prod.id === selectedProductId)?.price.toLocaleString()}원)</span>
                                        )}
                                    </>
                                ) : p.selected ? (
                                    <span className="ml-2 text-black font-normal">- {p.productName} ({p.productPrice?.toLocaleString()}원)</span>
                                ) : (
                                    <span className="text-red-500 font-bold">선택 대기중</span>
                                )}
                            </li>
                        ))}
                    </ul>
                    {/* 패키지 상세 설정 버튼 (방장만, 모든 회사가 선택 완료 시 활성화) */}
                    {isOwner && (
                        <button
                            className={`mt-8 w-full h-12 rounded bg-violet-600 text-white font-bold text-lg transition-colors disabled:bg-gray-300 disabled:cursor-not-allowed`}
                            disabled={!allSelected || !selectDone}
                            onClick={() => alert('패키지 상세 설정 단계로 이동!')}
                        >
                            패키지 상세 설정
                        </button>
                    )}
                </div>
            </div>
        </>
    );
}

export default AddPackage;
