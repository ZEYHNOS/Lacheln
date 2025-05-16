import React, { useEffect, useState, useRef } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import apiClient from '../../../../lib/apiClient';
import AddWrite from '../../../Tool/WriteForm/AddWrite';

const baseUrl = import.meta.env.VITE_API_BASE_URL;

function ViewPackage() {
    const { id } = useParams();
    const navigate = useNavigate();
    const [packageData, setPackageData] = useState(null);
    const [loading, setLoading] = useState(true);
    const [description, setDescription] = useState([]);
    const editorRef = useRef(null);

    useEffect(() => {
        if (!id) return;

        setLoading(true);
        apiClient.get(`/package/${id}`)
            .then(res => {
                console.log("패키지 상세 정보:", res.data.data);
                setPackageData(res.data.data);
                // 패키지 설명이 있으면 설정 (descriptionResponseList 필드 확인)
                if (res.data.data.descriptionResponseList) {
                    setDescription(res.data.data.descriptionResponseList);
                    // 에디터가 준비되면 설명 내용 설정
                    if (editorRef.current) {
                        editorRef.current.setContentFromJsonArray(res.data.data.descriptionResponseList);
                    }
                }
            })
            .catch(err => {
                console.error("패키지 정보 불러오기 실패", err);
                alert("패키지 정보를 불러오는데 실패했습니다.");
            })
            .finally(() => {
                setLoading(false);
            });
    }, [id]);

    // 에디터가 마운트된 후 내용 설정
    useEffect(() => {
        if (editorRef.current && description.length > 0) {
            editorRef.current.setContentFromJsonArray(description);
        }
    }, [editorRef.current, description]);

    if (loading) return <div className="p-8 text-center">로딩 중...</div>;
    if (!packageData) return <div className="p-8 text-center">패키지 정보를 찾을 수 없습니다.</div>;

    const { 
        name, admin, cp1, cp2, createAt, endDate, status, 
        discountrate, imageUrl, totalPrice 
    } = packageData;

    // 날짜 포맷팅 함수
    const formatDate = (dateArray) => {
        if (!dateArray || dateArray.length < 3) return "날짜 정보 없음";
        return `${dateArray[0]}.${String(dateArray[1]).padStart(2, '0')}.${String(dateArray[2]).padStart(2, '0')}`;
    };

    // 할인 전 가격 계산
    const originalPrice = (admin?.productPrice || 0) + (cp1?.productPrice || 0) + (cp2?.productPrice || 0);
    
    // 할인율 적용된 가격 계산
    const discountedPrice = totalPrice || Math.round(originalPrice * (1 - (discountrate || 0) / 100));

    // 상태 텍스트 및 색상 설정
    const getStatusInfo = (status) => {
        switch (status) {
            case 'PUBLIC':
                return { text: '판매중', bgColor: 'bg-green-100', textColor: 'text-green-700' };
            case 'PRIVATE':
                return { text: '판매 시작전', bgColor: 'bg-yellow-100', textColor: 'text-yellow-700' };
            default:
                return { text: status, bgColor: 'bg-gray-100', textColor: 'text-gray-700' };
        }
    };

    const statusInfo = getStatusInfo(status);

    return (
        <div className="p-8 max-w-6xl mx-auto">
            <div className="flex justify-between items-center mb-6">
                <div className="flex items-center gap-3">
                    <h1 className="text-2xl font-bold text-purple-700">{name}</h1>
                    <span className={`inline-block px-3 py-1 ${statusInfo.bgColor} ${statusInfo.textColor} font-semibold rounded-full text-sm`}>
                        {statusInfo.text}
                    </span>
                    {status === 'PRIVATE' && (
                        <button
                            onClick={() => {
                                if (window.confirm('패키지를 판매 시작하시겠습니까?')) {
                                    apiClient.post(`/package/upload/${id}`)
                                        .then(() => {
                                            alert('패키지 판매가 시작되었습니다.');
                                            window.location.reload(); // 페이지 새로고침
                                        })
                                        .catch(err => {
                                            console.error('판매 시작 실패', err);
                                            alert('판매 시작에 실패했습니다.');
                                        });
                                }
                            }}
                            className="px-3 py-1 bg-green-600 text-white rounded hover:bg-green-700 transition-colors text-sm ml-2"
                        >
                            판매 시작
                        </button>
                    )}
                </div>
                <div className="flex gap-3">
                    <button
                        onClick={() => navigate(`/company/collaboration/edit/${id}`)}
                        className="px-4 py-2 bg-purple-600 text-white rounded hover:bg-purple-700 transition-colors"
                    >
                        패키지 수정
                    </button>
                    <button
                        onClick={() => {
                            if (window.confirm('이 패키지를 삭제하시겠습니까?')) {
                                apiClient.delete(`/package/${id}`)
                                    .then(() => {
                                        alert('패키지가 삭제되었습니다.');
                                        navigate('/company/collaboration');
                                    })
                                    .catch(err => {
                                        console.error('패키지 삭제 실패', err);
                                        alert('패키지 삭제에 실패했습니다.');
                                    });
                            }
                        }}
                        className="px-4 py-2 bg-red-500 text-white rounded hover:bg-red-600 transition-colors"
                    >
                        패키지 삭제
                    </button>
                </div>
            </div>

            <div className="flex gap-6">
                {/* 왼쪽: 패키지 이미지와 정보 */}
                <div className="w-1/3">
                    <div className="border rounded-lg overflow-hidden bg-white shadow-sm mb-4">
                        <img
                            src={imageUrl ? `${baseUrl}${imageUrl.replace(/\\/g, '/')}` : '/default/images/package.png'}
                            alt={name}
                            className="w-full aspect-[3/4] object-cover"
                            onError={e => e.currentTarget.src = '/default/images/package.png'}
                        />
                    </div>
                    <div className="bg-white p-4 rounded-lg shadow-sm">
                        <div className="mb-3">
                            <h2 className="text-xl font-bold text-gray-800">{name}</h2>
                        </div>
                        <div className="space-y-2 text-sm">
                            <p className="flex justify-between">
                                <span className="text-gray-600 te">생성일</span>
                                <span className="font-medium text-black">{formatDate(createAt)}</span>
                            </p>
                            <p className="flex justify-between">
                                <span className="text-gray-600">종료일</span>
                                <span className="font-medium text-black">{formatDate(endDate)}</span>
                            </p>
                            <p className="flex justify-between">
                                <span className="text-gray-600">할인율</span>
                                <span className="font-medium text-red-500">{discountrate}%</span>
                            </p>
                        </div>
                        <div className="mt-4 pt-4 border-t">
                            <div className="flex justify-between items-center mb-1">
                                <span className="text-gray-600 text-sm">개별 상품 합계</span>
                                <span className="text-gray-500 line-through">{originalPrice.toLocaleString()}원</span>
                            </div>
                            <div className="flex justify-between items-center">
                                <span className="text-gray-800 font-bold">패키지 가격</span>
                                <span className="text-xl font-bold text-purple-700">{discountedPrice.toLocaleString()}원</span>
                            </div>
                            <div className="text-xs text-right text-red-500 mt-1">
                                {(discountrate || 0) > 0 ? `${discountrate}% 할인 적용` : ''}
                            </div>
                        </div>
                    </div>
                </div>

                {/* 오른쪽: 포함된 상품 정보 */}
                <div className="w-2/3 space-y-4">
                    <h2 className="text-xl font-bold text-gray-800 mb-2">포함된 상품</h2>

                    {/* 스튜디오 */}
                    {cp1?.category === 'S' && (
                        <div className="bg-white p-4 rounded-lg shadow-md hover:shadow-lg transition-shadow border-l-4 border-purple-500">
                            <div className="flex items-center mb-2">
                                <div className="w-20 h-14 rounded-full bg-purple-100 flex items-center justify-center mr-3 shadow-inner">
                                    <span className="text-purple-700 font-bold">스튜디오</span>
                                </div>
                                <div className="flex-grow">
                                    <h3 className="font-bold text-lg text-purple-700">{cp1.productName || '스튜디오상품1'}</h3>
                                </div>
                                <div className="ml-auto">
                                    <span className="font-bold text-gray-800 text-lg">{cp1.productPrice.toLocaleString()}원</span>
                                </div>
                            </div>
                        </div>
                    )}

                    {/* 드레스 */}
                    {(admin?.category === 'D' || cp1?.category === 'D' || cp2?.category === 'D') && (
                        <div className="bg-white p-4 rounded-lg shadow-md hover:shadow-lg transition-shadow border-l-4 border-pink-500 mt-3">
                            <div className="flex items-center mb-2">
                                <div className="w-20 h-14 rounded-full bg-pink-100 flex items-center justify-center mr-3 shadow-inner">
                                    <span className="text-pink-700 font-bold">드레스</span>
                                </div>
                                <div className="flex-grow">
                                    <h3 className="font-bold text-lg text-pink-700">
                                        {admin?.category === 'D' ? admin.productName : 
                                            cp1?.category === 'D' ? cp1.productName : 
                                            cp2?.category === 'D' ? cp2.productName : '드레스상품1'}
                                    </h3>
                                </div>
                                <div className="ml-auto">
                                    <span className="font-bold text-gray-800 text-lg">
                                        {admin?.category === 'D' ? admin.productPrice.toLocaleString() : 
                                            cp1?.category === 'D' ? cp1.productPrice.toLocaleString() : 
                                            cp2?.category === 'D' ? cp2.productPrice.toLocaleString() : '0'}원
                                    </span>
                                </div>
                            </div>
                        </div>
                    )}

                    {/* 메이크업 */}
                    {cp2?.category === 'M' && (
                        <div className="bg-white p-4 rounded-lg shadow-md hover:shadow-lg transition-shadow border-l-4 border-blue-500 mt-3">
                            <div className="flex items-center mb-2">
                                <div className="w-20 h-14 rounded-full bg-blue-100 flex items-center justify-center mr-3 shadow-inner">
                                    <span className="text-blue-700 font-bold">메이크업</span>
                                </div>
                                <div className="flex-grow">
                                    <h3 className="font-bold text-lg text-blue-700">{cp2.productName || '메이크업상품1'}</h3>
                                </div>
                                <div className="ml-auto">
                                    <span className="font-bold text-gray-800 text-lg">{cp2.productPrice.toLocaleString()}원</span>
                                </div>
                            </div>
                        </div>
                    )}

                    {/* 협업 업체 정보 */}
                    <h2 className="text-xl font-bold text-gray-800 mt-8 mb-2">협업 업체</h2>
                    <div className="grid grid-cols-3 gap-4">
                        {[admin, cp1, cp2].filter(Boolean).map((company, idx) => (
                            <div key={idx} 
                                className={`bg-white p-4 rounded-lg shadow-md hover:shadow-lg transition-shadow border-t-4 text-center
                                ${company.category === 'S' ? 'border-purple-500' : 
                                    company.category === 'D' ? 'border-pink-500' : 
                                    company.category === 'M' ? 'border-blue-500' : 'border-gray-500'}`}
                            >
                                <img
                                    src={company.profileImageUrl ? `${baseUrl}${company.profileImageUrl.replace(/\\/g, '/')}` : '/default/images/company.png'}
                                    alt={company.name}
                                    className="w-20 h-20 rounded-full object-cover mx-auto mb-3 shadow-md border-2 
                                    ${company.category === 'S' ? 'border-purple-200' : 
                                        company.category === 'D' ? 'border-pink-200' : 
                                        company.category === 'M' ? 'border-blue-200' : 'border-gray-200'}"
                                    onError={e => e.currentTarget.src = '/default/images/company.png'}
                                />
                                <h3 className="font-bold mb-1 text-black text-lg text-center">{company.name}</h3>
                                <p className={`text-sm font-semibold mb-1 inline-block px-3 py-1 rounded-full mx-auto
                                    ${company.category === 'S' ? 'bg-purple-100 text-purple-700' : 
                                        company.category === 'D' ? 'bg-pink-100 text-pink-700' : 
                                        company.category === 'M' ? 'bg-blue-100 text-blue-700' : 'bg-gray-100 text-gray-700'}`}
                                >
                                    {company.category === 'S' ? '스튜디오' : 
                                        company.category === 'D' ? '드레스' : 
                                        company.category === 'M' ? '메이크업' : '기타'}
                                </p>
                                <p className="text-sm text-gray-600 mt-1 text-center">{company.address}</p>
                            </div>
                        ))}
                    </div>
                </div>
            </div>

            {/* 패키지 설명 */}
            <div className="max-w-6xl mx-auto mt-8">
                <h2 className="text-xl font-bold text-gray-800 mb-2">패키지 설명</h2>
                <div className="bg-white p-4 rounded-lg shadow-md">
                    {/* 읽기 전용 모드로 설정 */}
                    <AddWrite ref={editorRef} readOnly={true} />
                </div>
            </div>
        </div>
    );
}

export default ViewPackage;