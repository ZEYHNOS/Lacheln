import React, { useEffect, useState, useRef } from 'react';
import Addcompany from './Addcompany';
import apiClient from '../../../../lib/apiClient';
import { useParams, useNavigate } from 'react-router-dom';

const baseUrl = import.meta.env.VITE_API_BASE_URL;

// 내 회사 id (실제 로그인 정보에서 받아와야 함)
const myCompanyId = 1;
// 방장 여부 (실제 패키지 생성자 id와 비교)
const isOwner = true;

function AddPackage() {
    const { id } = useParams(); // URL에서 패키지 ID 가져오기
    const navigate = useNavigate();
    
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

    // 모든 업체가 상품을 선택했는지 확인하는 함수
    const checkAllProductsSelected = (packageData) => {
        // 패키지에 등록된 상품 수 확인
        let selectedCount = 0;
        
        // admin, cp1, cp2 각각이 상품을 등록했는지 확인
        if (packageData.adminProduct) selectedCount++;
        if (packageData.cp1Product) selectedCount++;
        if (packageData.cp2Product) selectedCount++;
        
        // 3개 업체 모두 상품을 등록했으면 allSelected를 true로 설정
        setAllSelected(selectedCount === 3);
        console.log(`등록된 상품 수: ${selectedCount}/3`);
    };

    // 패키지 정보 조회 함수
    const fetchPackageInfo = async (packageId) => {
        try {
            const res = await apiClient.get(`/package/${packageId}`, { withCredentials: true });
            console.log('패키지 정보 조회 성공:', res.data);
            setPackageInfo(res.data.data);
            
            // 패키지 정보에서 기본 데이터 설정
            if (res.data.data) {
                setPackageName(res.data.data.name || '');
                // 각 업체별 상품 선택 상태 확인
                checkAllProductsSelected(res.data.data);
            }
        } catch (err) {
            console.error('패키지 정보 조회 실패:', err);
        }
    };

    // URL에서 패키지 ID가 있거나 상태로 패키지 ID가 있으면 정보 조회
    useEffect(() => {
        if (packageId) {
            fetchPackageInfo(packageId);
        }
    }, [packageId]);

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
        setSelectDone(true);
        
        // 패키지 ID와 상품 ID를 이용하여 상품 등록
        apiClient.post(`/product/package/register/${packageId}?productId=${selectedProductId}`, {}, { withCredentials: true })
            .then(res => {
                console.log('상품 등록 성공:', res.data);
                // 패키지 정보 다시 조회하여 모든 업체가 상품을 등록했는지 확인
                fetchPackageInfo(packageId);
            })
            .catch(err => console.error('상품 등록 실패:', err));
    };

    // 패키지 대표 이미지 업로드
    const handleImageChange = (e) => {
        if (e.target.files && e.target.files[0]) {
            setImage(e.target.files[0]);
        }
    };

    // 모든 참여자가 선택 완료했는지 확인
    const allSelected = participants.length > 0 && participants.every(p => p.selected);

    // 등록 버튼 비활성화 조건
    const isRegisterDisabled = !packageName || !image || !endDate;

    // 내 회사가 항상 맨 위에 오도록 정렬
    const sortedParticipants = [
        ...participants.filter(p => p.companyId === myCompanyId),
        ...participants.filter(p => p.companyId !== myCompanyId)
    ];

    // 패키지 설정 페이지로 이동
    const handleNavigateToPackageSetting = () => {
        navigate(`/company/collaboration/setpackage/${packageId}`);
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
                                <p>총 {packageInfo.admin && packageInfo.cp1 && packageInfo.cp2 ? '3' : '0'}개 업체 중 <span className="font-bold text-purple-700">{
                                    (packageInfo.adminProduct ? 1 : 0) + 
                                    (packageInfo.cp1Product ? 1 : 0) + 
                                    (packageInfo.cp2Product ? 1 : 0)
                                }개</span> 업체가 상품을 등록했습니다.</p>
                            </div>
                        </div>
                    )}
                    <div className="mb-12">
                        <h3 className="font-semibold mb-4 text-xl text-black">내 상품 선택</h3>
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
                                                        <span>{selectedProduct.name} - {selectedProduct.price?.toLocaleString()}원</span>
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
                                                    key={product.id}
                                                    className={`p-3 flex items-center hover:bg-purple-50 cursor-pointer ${selectedProductId === product.id ? 'bg-purple-100' : ''}`}
                                                    onClick={() => {
                                                        setSelectedProductId(product.id);
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
                                                        <p className="font-semibold">{product.name}</p>
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
                    </div>
                </div>
                {/* 오른쪽: 참여 회사 상태 */}
                <div className="w-96 flex-shrink-0 mt-16">
                    <h3 className="font-semibold mb-6 text-xl text-black">참여 회사 상태</h3>
                    <ul className="space-y-4">
                        {packageInfo ? (
                            // 패키지 정보에서 참여 회사 표시
                            <>
                                {packageInfo.admin && (
                                    <li className="flex items-center">
                                        <span className="font-bold mr-2 text-[#845ec2] text-xl">{packageInfo.admin.name}</span>
                                        <span className="ml-2 text-black font-normal">({packageInfo.admin.email})</span>
                                    </li>
                                )}
                                {packageInfo.cp1 && (
                                    <li className="flex items-center">
                                        <span className="font-bold mr-2 text-[#845ec2] text-xl">{packageInfo.cp1.name}</span>
                                        <span className="ml-2 text-black font-normal">({packageInfo.cp1.email})</span>
                                    </li>
                                )}
                                {packageInfo.cp2 && (
                                    <li className="flex items-center">
                                        <span className="font-bold mr-2 text-[#845ec2] text-xl">{packageInfo.cp2.name}</span>
                                        <span className="ml-2 text-black font-normal">({packageInfo.cp2.email})</span>
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
                    {/* 패키지 상세 설정 버튼 (방장만, 모든 회사가 선택 완료 시 활성화) */}
                    {isOwner && (
                        <button
                            className={`mt-8 w-full h-12 rounded bg-violet-600 text-white font-bold text-lg transition-colors disabled:bg-gray-300 disabled:cursor-not-allowed`}
                            disabled={!allSelected}
                            onClick={handleNavigateToPackageSetting}
                        >
                            패키지 상세 설정
                        </button>
                    )}
                    
                    {packageInfo && !allSelected && (
                        <div className="mt-4 text-center text-sm text-red-500">
                            모든 업체가 상품을 등록해야 패키지 상세 설정이 가능합니다.
                        </div>
                    )}
                </div>
            </div>
        </>
    );
}

export default AddPackage;