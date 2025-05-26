import React, { useEffect, useState, useRef } from 'react';
import Addcompany from './Addcompany';
import apiClient from '../../../../../lib/apiClient';
import { useParams, useNavigate } from 'react-router-dom';

const baseUrl = import.meta.env.VITE_API_BASE_URL;

// myCompanyId를 상태로 관리
function AddPackage() {
    const { id } = useParams();
    const navigate = useNavigate();
    
    // 로그인 사용자 정보
    const [myCompanyId, setMyCompanyId] = useState(null);
    const isOwner = true;
    
    // 내 상품 선택
    const [myProductList, setMyProductList] = useState([]);
    const [selectedProductId, setSelectedProductId] = useState(null);
    const [selectDone, setSelectDone] = useState(false);

    // 패키지 정보 입력
    const [packageName, setPackageName] = useState('');
    const [discount, setDiscount] = useState(0);
    const [image, setImage] = useState(null);
    const [endDate, setEndDate] = useState('');
    
    // 패키지 ID와 정보
    const [packageId, setPackageId] = useState(id || null);
    const [packageInfo, setPackageInfo] = useState(null);

    // 참여자 상태 (Addcompany에서 초대한 업체로 대체)
    const [participants, setParticipants] = useState([]);

    // Addcompany 모달 오픈 상태 - URL에 ID가 있으면 모달 표시하지 않음
    const [companyModalOpen, setCompanyModalOpen] = useState(!id);

    // 드롭다운 상태
    const [isDropdownOpen, setIsDropdownOpen] = useState(false);

    // 현재 로그인한 사용자 정보 가져오기
    useEffect(() => {
        const fetchUserInfo = async () => {
            try {
                const res = await apiClient.get('/auth/me', { withCredentials: true });
                if (res.data && res.data.data) {
                    const userId = res.data.data.id;
                    setMyCompanyId(userId);
                }
            } catch (err) {
                console.error('사용자 정보 조회 실패:', err);
                setMyCompanyId(1);
            }
        };
        
        fetchUserInfo();
    }, []);

    // 모든 업체가 상품을 선택했는지 확인
    const checkAllProductsSelected = (packageData) => {
        // 패키지에 등록된 상품 수 확인
        let selectedCount = 0;
        
        // admin, cp1, cp2 각각이 productId를 가지고 있는지 확인
        if (packageData.admin && packageData.admin.productId) selectedCount++;
        if (packageData.cp1 && packageData.cp1.productId) selectedCount++;
        if (packageData.cp2 && packageData.cp2.productId) selectedCount++;
        
        // 3개 업체 모두 상품을 등록했으면 allSelected를 true로 설정
        setAllSelected(selectedCount === 3);
    };

    // 패키지 정보 조회 함수
    const fetchPackageInfo = async (packageId) => {
        try {
            const res = await apiClient.get(`/package/${packageId}`, { withCredentials: true });
            setPackageInfo(res.data.data);
            
            // 패키지 정보에서 기본 데이터 설정
            if (res.data.data) {
                const data = res.data.data;
                setPackageName(data.name || '');
                
                // 문자열로 변환하여 비교
                const myCompanyIdStr = String(myCompanyId);
                
                // 각 업체별 상품 선택 상태 확인 (새로운 데이터 구조 처리)
                let selectedCount = 0;
                
                // admin, cp1, cp2 각각이 productId를 가지고 있는지 확인
                if (data.admin && data.admin.productId) selectedCount++;
                if (data.cp1 && data.cp1.productId) selectedCount++;
                if (data.cp2 && data.cp2.productId) selectedCount++;
                
                // 3개 업체 모두 상품을 등록했으면 allSelected를 true로 설정
                setAllSelected(selectedCount === 3);
                
                // 내 업체가 이미 상품을 등록했는지 확인
                let myCompany = null;
                let myProductRegistered = false;
                let myProduct = null;

                // 문자열로 변환하여 비교 (확실한 타입 일치)
                if (data.admin && String(data.admin.id) === myCompanyIdStr) {
                    myCompany = 'admin';
                    myProductRegistered = !!data.admin.productId;
                    myProduct = data.admin;
                } else if (data.cp1 && String(data.cp1.id) === myCompanyIdStr) {
                    myCompany = 'cp1';
                    myProductRegistered = !!data.cp1.productId;
                    myProduct = data.cp1;
                } else if (data.cp2 && String(data.cp2.id) === myCompanyIdStr) {
                    myCompany = 'cp2';
                    myProductRegistered = !!data.cp2.productId;
                    myProduct = data.cp2;
                } else {
                    console.log('내 회사와 일치하는 업체를 찾을 수 없습니다');
                    // 모든 업체 ID 명시적 변환 후 비교 로깅
                    if (data.admin) console.log('admin.id == myCompanyId:', String(data.admin.id) === myCompanyIdStr);
                    if (data.cp1) console.log('cp1.id == myCompanyId:', String(data.cp1.id) === myCompanyIdStr);
                    if (data.cp2) console.log('cp2.id == myCompanyId:', String(data.cp2.id) === myCompanyIdStr);
                }
                
                // 이미 상품이 등록되었으면 선택 완료 상태로 설정
                if (myProductRegistered) {
                    setSelectDone(true);
                    console.log('이미 등록된 상품이 있습니다:', myProduct.productName);
                } else {
                    // 상품이 등록되지 않았으면 선택 가능한 상태로 설정
                    setSelectDone(false);
                    console.log('등록된 상품이 없습니다. 상품 선택이 필요합니다.');
                }
            }
        } catch (err) {
            console.error('패키지 정보 조회 실패:', err);
        }
    };

    // URL에서 패키지 ID가 있거나 상태로 패키지 ID가 있으면 정보 조회
    useEffect(() => {
        // 우선 myCompanyId가 설정되어 있어야 함
        if (myCompanyId && packageId) {
            fetchPackageInfo(packageId);
        }
    }, [packageId, myCompanyId]);

    // 이미 상품을 등록한 경우 협업 페이지로 리다이렉트
    useEffect(() => {
        if (packageInfo && myCompanyId) {
            let myProductRegistered = false;
            let myCompanyType = '';
            
            // 문자열로 변환
            const myCompanyIdStr = String(myCompanyId);
            
            // 내가 이미 상품을 등록했는지 확인 (모든 ID를 문자열로 변환하여 비교)
            if (packageInfo.admin && String(packageInfo.admin.id) === myCompanyIdStr) {
                myCompanyType = 'admin';
                myProductRegistered = !!packageInfo.admin.productId;
            } else if (packageInfo.cp1 && String(packageInfo.cp1.id) === myCompanyIdStr) {
                myCompanyType = 'cp1';
                myProductRegistered = !!packageInfo.cp1.productId;
            } else if (packageInfo.cp2 && String(packageInfo.cp2.id) === myCompanyIdStr) {
                myCompanyType = 'cp2';
                myProductRegistered = !!packageInfo.cp2.productId;
            } else {
                console.log('내 회사 ID와 일치하는 회사를 찾을 수 없습니다:', myCompanyId);
                console.log('패키지 참여 회사들:', {
                    admin: packageInfo.admin ? packageInfo.admin.id : '없음',
                    cp1: packageInfo.cp1 ? packageInfo.cp1.id : '없음',
                    cp2: packageInfo.cp2 ? packageInfo.cp2.id : '없음'
                });
            }
            
            // ID 비교 로그 추가 (문자열 변환)
            if (packageInfo.admin) {
                console.log(`admin ID (${packageInfo.admin.id}) == myCompanyId (${myCompanyId}): ${String(packageInfo.admin.id) === myCompanyIdStr}`);
            }
            if (packageInfo.cp1) {
                console.log(`cp1 ID (${packageInfo.cp1.id}) == myCompanyId (${myCompanyId}): ${String(packageInfo.cp1.id) === myCompanyIdStr}`);
            }
            if (packageInfo.cp2) {
                console.log(`cp2 ID (${packageInfo.cp2.id}) == myCompanyId (${myCompanyId}): ${String(packageInfo.cp2.id) === myCompanyIdStr}`);
            }
        }
    }, [packageInfo, myCompanyId]);

    // Addcompany에서 초대 완료 시 호출될 함수
    const handleInviteComplete = (invitedUsers, newPackageId) => {
        setParticipants(invitedUsers.map(user => ({
            companyId: user.id,
            name: user.name,
            email: user.email,
            selected: false, 
        })));
        
        if (newPackageId) {
            setPackageId(newPackageId);
            // 패키지 ID가 생성되면 URL 업데이트
            navigate(`/company/collaboration/setproduct/${newPackageId}`, { replace: true });
        }
        
        setCompanyModalOpen(false);
    };

    // 내 상품 리스트 불러오기
    useEffect(() => {
        const fetchMyProducts = async () => {
            try {
                const res = await apiClient.get('/product/list?status=INACTIVE', { withCredentials: true });
                if (res.data && res.data.data) {
                    setMyProductList(res.data.data);
                }
            } catch (err) {
                console.error('상품 목록 불러오기 실패:', err);
            }
        };
        
        fetchMyProducts();
    }, []);

    // 선택 완료 버튼 클릭
    const handleSelectDone = () => {
        if (!selectedProductId || !packageId) return;
        
        // 패키지 ID와 상품 ID를 이용하여 상품 등록
        apiClient.post(`/product/package/register/${packageId}?productId=${selectedProductId}`, {}, { withCredentials: true })
            .then(res => {
                console.log('상품 등록 성공:', res.data);
                
                // 상품 등록 완료 상태로 즉시 설정
                setSelectDone(true);
                
                // 패키지 정보 다시 조회하여 모든 업체가 상품을 등록했는지 확인
                fetchPackageInfo(packageId);
                
                // 성공 메시지 표시
                alert("상품이 성공적으로 등록되었습니다.");
            })
            .catch(err => {
                console.error('상품 등록 실패:', err.response?.data);
                console.log('오류 세부 정보:', err.response?.data?.result);
                
                // 이미 상품이 등록되어 있는 경우 처리
                if (err.response?.data?.result?.resultCode === 500 && 
                    err.response?.data?.result?.description === "이미 패키지에 상품이 등록되어있습니다.") {
                    alert("이미 이 패키지에 상품이 등록되어 있습니다.");
                    // 패키지 정보 다시 로드하여 최신 상태 확인
                    fetchPackageInfo(packageId);
                    // 상품 등록 완료 상태로 설정
                    setSelectDone(true);
                }
            });
    };

    // 패키지 대표 이미지 업로드
    const handleImageChange = (e) => {
        if (e.target.files && e.target.files[0]) {
            setImage(e.target.files[0]);
        }
    };

    // 모든 참여자가 선택 완료했는지 확인
    const [allSelected, setAllSelected] = useState(false);

    // 등록 버튼 비활성화 조건
    const isRegisterDisabled = !packageName || !image || !endDate;

    // 내 회사가 항상 맨 위에 오도록 정렬
    const sortedParticipants = [
        ...participants.filter(p => p.companyId === myCompanyId),
        ...participants.filter(p => p.companyId !== myCompanyId)
    ];

    // 패키지 설정 페이지로 이동
    const handleNavigateToPackageSetting = () => {
        // 현재 로그인한 사용자가 admin인지 확인
        if (packageInfo && packageInfo.admin && String(packageInfo.admin.id) === String(myCompanyId)) {
            navigate(`/company/collaboration/setpackage/${packageId}`);
        } else {
            alert("패키지 관리자만 상세 설정이 가능합니다.");
        }
    };
    
    // 현재 사용자가 admin인지 확인하는 함수
    const isAdmin = () => {
        return packageInfo && packageInfo.admin && String(packageInfo.admin.id) === String(myCompanyId);
    };

    return (
        <>
            {companyModalOpen && (
                <Addcompany onClose={() => navigate('/company/collaboration')} onComplete={handleInviteComplete} />
            )}
            <div className="w-full min-h-screen p-16 flex flex-row gap-16 items-start bg-white">
                {/* 왼쪽: 상품 선택 */}
                <div className="flex-1">
                    <h2 className="text-3xl font-bold mb-10 text-purple-600">패키지 상품 선택</h2>
                    {packageInfo && (
                        <div className="mb-4 p-4 bg-purple-50 rounded-lg">
                            <h3 className="font-bold text-lg text-purple-700">패키지명: {packageInfo.name}</h3>
                            <div className="mt-2 text-sm">
                                <p className='text-black'>총 {packageInfo.admin && packageInfo.cp1 && packageInfo.cp2 ? '3' : '0'}개 업체 중 <span className="font-bold text-purple-700">{
                                    (packageInfo.admin && packageInfo.admin.productId ? 1 : 0) + 
                                    (packageInfo.cp1 && packageInfo.cp1.productId ? 1 : 0) + 
                                    (packageInfo.cp2 && packageInfo.cp2.productId ? 1 : 0)
                                }개</span> 업체가 상품을 등록했습니다.</p>
                            </div>
                        </div>
                    )}
                    <div className="mb-12">
                        <h3 className="font-semibold mb-4 text-xl text-black">내 상품 선택</h3>
                        
                        {selectDone ? (
                            <div className="p-4 bg-purple-50 rounded-lg">
                                <div className="bg-purple-100 text-purple-800 px-3 py-2 rounded-md inline-block mb-2">
                                    상품이 이미 등록되었습니다
                                </div>
                                
                                {packageInfo && (
                                    <>
                                        {packageInfo.admin && String(packageInfo.admin.id) === String(myCompanyId) && packageInfo.admin.productId && (
                                            <div className="flex items-center mt-3">
                                                <span className="font-medium mr-2 text-black">등록된 상품:</span>
                                                <span className="text-green-600 font-semibold">{packageInfo.admin.productName}</span>
                                                <span className="ml-2 text-gray-600">{packageInfo.admin.productPrice?.toLocaleString()}원</span>
                                            </div>
                                        )}
                                        {packageInfo.cp1 && String(packageInfo.cp1.id) === String(myCompanyId) && packageInfo.cp1.productId && (
                                            <div className="flex items-center mt-3">
                                                <span className="font-medium mr-2 text-black">등록된 상품:</span>
                                                <span className="text-green-600 font-semibold">{packageInfo.cp1.productName}</span>
                                                <span className="ml-2 text-gray-600">{packageInfo.cp1.productPrice?.toLocaleString()}원</span>
                                            </div>
                                        )}
                                        {packageInfo.cp2 && String(packageInfo.cp2.id) === String(myCompanyId) && packageInfo.cp2.productId && (
                                            <div className="flex items-center mt-3">
                                                <span className="font-medium mr-2 text-black">등록된 상품:</span>
                                                <span className="text-green-600 font-semibold">{packageInfo.cp2.productName}</span>
                                                <span className="ml-2 text-gray-600">{packageInfo.cp2.productPrice?.toLocaleString()}원</span>
                                            </div>
                                        )}
                                        
                                        <p className="mt-4 text-sm text-gray-600">패키지에 등록된 상품은 변경할 수 없습니다.</p>
                                    </>
                                )}
                            </div>
                        ) : (
                            <>
                                <div className="relative">
                                    {/* 커스텀 드롭다운 (이미지 포함) */}
                                    <div className="relative">
                                        <div 
                                            className="w-full p-3 border-2 border-purple-400 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-400 bg-white text-gray-900 cursor-pointer flex justify-between items-center"
                                            onClick={() => setIsDropdownOpen(!isDropdownOpen)}
                                        >
                                            {selectedProductId ? (
                                                <div className="flex items-center">
                                                    {(() => {
                                                        const selectedProduct = myProductList.find(p => p.id === selectedProductId);
                                                        if (!selectedProduct) return <span>상품을 선택하세요</span>;
                                                        
                                                        return (
                                                            <>
                                                                <img 
                                                                    src={selectedProduct.imageUrl ? `${baseUrl}${selectedProduct.imageUrl.replace(/\\/g, '/')}` : '/default/images/product.png'} 
                                                                    alt={selectedProduct.name}
                                                                    className="w-10 h-10 object-cover rounded mr-3"
                                                                    onError={(e) => { e.target.onerror = null; e.target.src = '/default/images/product.png'; }}
                                                                />
                                                                <span className="text-black font-bold">{selectedProduct.name} - {selectedProduct.price?.toLocaleString()}원</span>
                                                            </>
                                                        );
                                                    })()}
                                                </div>
                                            ) : (
                                                <span>상품을 선택하세요</span>
                                            )}
                                            <svg className="h-5 w-5 text-purple-500" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor">
                                                <path fillRule="evenodd" d="M10 3a1 1 0 01.707.293l3 3a1 1 0 01-1.414 1.414L10 5.414 7.707 7.707a1 1 0 01-1.414-1.414l3-3A1 1 0 0110 3zm-3.707 9.293a1 1 0 011.414 0L10 14.586l2.293-2.293a1 1 0 011.414 1.414l-3 3a1 1 0 01-1.414 0l-3-3a1 1 0 010-1.414z" clipRule="evenodd" />
                                            </svg>
                                        </div>
                                        
                                        {/* 드롭다운 목록 */}
                                        {isDropdownOpen && (
                                            <div className="absolute z-10 w-full mt-1 bg-white border border-gray-300 rounded-md shadow-lg max-h-60 overflow-auto">
                                                {myProductList.length > 0 ? (
                                                    myProductList.map(product => (
                                                        <div
                                                            key={product.id || product.productId || product._id}
                                                            className={`p-3 flex items-center hover:bg-purple-50 cursor-pointer ${selectedProductId === (product.id || product.productId || product._id) ? 'bg-purple-100' : ''}`}
                                                            onClick={() => {
                                                                console.log('클릭됨! product:', product);
                                                                console.log('product.id:', product.id);
                                                                console.log('product.productId:', product.productId);
                                                                console.log('product._id:', product._id);
                                                                setSelectedProductId(product.id || product.productId || product._id);
                                                                setIsDropdownOpen(false);
                                                            }}
                                                        >
                                                            <img 
                                                                src={product.imageUrl ? `${baseUrl}${product.imageUrl.replace(/\\/g, '/')}` : '/default/images/product.png'} 
                                                                alt={product.name}
                                                                className="w-12 h-12 object-cover rounded mr-3"
                                                                onError={(e) => { e.target.onerror = null; e.target.src = '/default/images/product.png'; }}
                                                            />
                                                            <div>
                                                                <p className="font-semibold text-black">{product.name}</p>
                                                                <p className="text-blue-600">{product.price?.toLocaleString()}원</p>
                                                            </div>
                                                        </div>
                                                    ))
                                                ) : (
                                                    <div className="p-3 text-center text-gray-500">
                                                        상품이 없습니다
                                                    </div>
                                                )}
                                            </div>
                                        )}
                                    </div>
                                </div>
                                
                                <button
                                    className="mt-6 px-6 py-2 bg-violet-600 text-white rounded font-semibold hover:bg-violet-700 disabled:bg-gray-300 disabled:cursor-not-allowed"
                                    disabled={!selectedProductId}
                                    onClick={handleSelectDone}
                                >
                                    상품 선택 완료
                                </button>
                            </>
                        )}
                    </div>
                </div>
                {/* 오른쪽: 참여 회사 상태 */}
                <div className="w-100 flex-shrink-0 mt-16">
                    <h3 className="font-semibold mb-6 text-xl text-black">참여 회사 상태</h3>
                    <ul className="space-y-4">
                        {packageInfo ? (
                            // 패키지 정보에서 참여 회사 표시 (새로운 데이터 구조 처리)
                            <>
                                {packageInfo.admin && (
                                    <li className="flex flex-col">
                                        <div className="flex items-center">
                                            <span className="font-bold mr-2 text-[#845ec2] text-xl">{packageInfo.admin.name}</span>
                                            <span className="ml-2 text-black font-normal">({packageInfo.admin.email})</span>
                                            <span className="ml-2 bg-blue-100 text-blue-800 px-2 py-1 rounded-full text-xs">패키지 관리자</span>
                                        </div>
                                        <div className="mt-1 ml-4">
                                            {packageInfo.admin.productId ? (
                                                <div className="flex items-center">
                                                    <span className="bg-purple-100 text-purple-800 px-2 py-1 rounded-full text-xs">상품 등록 완료</span>
                                                    <span className="ml-2 text-green-600 font-semibold">{packageInfo.admin.productName}</span>
                                                    <span className="ml-2 text-gray-600">{packageInfo.admin.productPrice?.toLocaleString()}원</span>
                                                </div>
                                            ) : (
                                                <span className="bg-gray-100 text-gray-600 px-2 py-1 rounded-full text-xs">상품 미등록</span>
                                            )}
                                        </div>
                                    </li>
                                )}
                                {packageInfo.cp1 && (
                                    <li className="flex flex-col">
                                        <div className="flex items-center">
                                            <span className="font-bold mr-2 text-[#845ec2] text-xl">{packageInfo.cp1.name}</span>
                                            <span className="ml-2 text-black font-normal">({packageInfo.cp1.email})</span>
                                        </div>
                                        <div className="mt-1 ml-4">
                                            {packageInfo.cp1.productId ? (
                                                <div className="flex items-center">
                                                    <span className="bg-purple-100 text-purple-800 px-2 py-1 rounded-full text-xs">상품 등록 완료</span>
                                                    <span className="ml-2 text-green-600 font-semibold">{packageInfo.cp1.productName}</span>
                                                    <span className="ml-2 text-gray-600">{packageInfo.cp1.productPrice?.toLocaleString()}원</span>
                                                </div>
                                            ) : (
                                                <span className="bg-gray-100 text-gray-600 px-2 py-1 rounded-full text-xs">상품 미등록</span>
                                            )}
                                        </div>
                                    </li>
                                )}
                                {packageInfo.cp2 && (
                                    <li className="flex flex-col">
                                        <div className="flex items-center">
                                            <span className="font-bold mr-2 text-[#845ec2] text-xl">{packageInfo.cp2.name}</span>
                                            <span className="ml-2 text-black font-normal">({packageInfo.cp2.email})</span>
                                        </div>
                                        <div className="mt-1 ml-4">
                                            {packageInfo.cp2.productId ? (
                                                <div className="flex items-center">
                                                    <span className="bg-purple-100 text-purple-800 px-2 py-1 rounded-full text-xs">상품 등록 완료</span>
                                                    <span className="ml-2 text-green-600 font-semibold">{packageInfo.cp2.productName}</span>
                                                    <span className="ml-2 text-gray-600">{packageInfo.cp2.productPrice?.toLocaleString()}원</span>
                                                </div>
                                            ) : (
                                                <span className="bg-gray-100 text-gray-600 px-2 py-1 rounded-full text-xs">상품 미등록</span>
                                            )}
                                        </div>
                                    </li>
                                )}
                            </>
                        ) : (
                            sortedParticipants.map(p => (
                                <li key={p.companyId} className="flex items-center">
                                    <span className="font-bold mr-2 text-[#845ec2] text-xl">{p.name}</span>
                                    <span className="ml-2 text-black font-normal">({p.email})</span>
                                </li>
                            ))
                        )}
                    </ul>
                    {/* 패키지 상세 설정 버튼 (admin만, 모든 회사가 선택 완료 시 활성화) */}
                    {isAdmin() && (
                        <button
                            className={`mt-8 w-full h-12 rounded bg-violet-600 text-white font-bold text-lg transition-colors disabled:bg-gray-300 disabled:cursor-not-allowed`}
                            disabled={!allSelected}
                            onClick={handleNavigateToPackageSetting}
                        >
                            패키지 상세 설정
                        </button>
                    )}
                    
                    {packageInfo && !allSelected && isAdmin() && (
                        <div className="mt-4 text-center text-sm text-red-500">
                            모든 업체가 상품을 등록해야 패키지 상세 설정이 가능합니다.
                        </div>
                    )}
                    
                    {packageInfo && !isAdmin() && allSelected && (
                        <div className="mt-8 text-center text-sm text-gray-700 p-3 bg-gray-100 rounded">
                            패키지 관리자만 상세 설정이 가능합니다.
                        </div>
                    )}
                </div>
            </div>
        </>
    );
}

export default AddPackage;