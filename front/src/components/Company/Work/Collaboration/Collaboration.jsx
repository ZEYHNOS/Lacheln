import React, { useEffect, useState } from 'react';
import apiClient from "../../../../lib/apiClient";
import axios from 'axios';
import { data, useNavigate } from 'react-router-dom';

const baseUrl = import.meta.env.VITE_API_BASE_URL;

function Collaboration() {
    const [packages, setPackages] = useState([]);
    const [myCategory, setMyCategory] = useState(null);
    const [companies, setCompanies] = useState([]);
    const [currentTab, setCurrentTab] = useState("product");
    const navigate = useNavigate();
    
    useEffect(() => {
        axios.get(`${baseUrl}/company/category`,{
            withCredentials: true})
            .then(res => {
                console.log("회사 카테고리가 " + res.data + "입니다.");
                setMyCategory(res.data);
            })
            .catch(err => console.error("카테고리 불러오기 실패", err));
    }, []);

    useEffect(() => {
        apiClient.get("/package/list", { withCredentials: true })
            .then((res) => {
                const rawData = res.data.data || [];
                console.log("패키지 목록 데이터:", rawData);
                setPackages(rawData);

                // admin, cp1, cp2 모두 업체로 바인딩, id 기준 중복 제거
                const uniqueCompanies = {};
                rawData.forEach(pkg => {
                    if (pkg.admin && pkg.admin.id) uniqueCompanies[pkg.admin.id] = pkg.admin;
                    if (pkg.cp1 && pkg.cp1.id) uniqueCompanies[pkg.cp1.id] = pkg.cp1;
                    if (pkg.cp2 && pkg.cp2.id) uniqueCompanies[pkg.cp2.id] = pkg.cp2;
                });
                setCompanies(Object.values(uniqueCompanies));
            })
            .catch((err) => console.error("❌ 데이터 불러오기 실패:", err));
    }, []);

    // 할인율을 적용한 가격 계산 함수
    const calculateDiscountedPrice = (pkg) => {
        const originalPrice = (pkg.admin?.productPrice || 0) + (pkg.cp1?.productPrice || 0) + (pkg.cp2?.productPrice || 0);
        // 할인율 계산 수정 - 1에서 할인율을 빼는 것이 아니라 할인율만큼 금액을 차감
        const discountAmount = Math.round(originalPrice * ((pkg.discountrate || 0) / 100));
        // 서버 가격이 있어도 할인된 가격을 적용
        const discountedPrice = originalPrice - discountAmount;
                
        return { originalPrice, discountedPrice };
    };

    return (
        <div className="p-8 relative w-full">
            {/* 탭 버튼 + 밑줄 */}
            <div className="relative w-full">
                <div className="flex justify-between items-center text-lg font-semibold mb-2 z-10 relative">
                    <div className="flex space-x-4">
                        <button
                            onClick={() => setCurrentTab('product')}
                            className={`px-4 py-2 rounded-none bg-transparent border-none shadow-none outline-none 
                                ${
                                currentTab === 'product'
                                    ? 'text-purple-700 font-bold'
                                    : 'text-purple-300'
                                } 
                                hover:bg-transparent
                                focus:outline-none focus:ring-0 
                                active:outline-none active:ring-0 
                                focus-visible:outline-none focus-visible:ring-0`}
                            style={{ border: 'none', boxShadow: 'none' }}
                        >
                        협업 중인 상품
                        </button>
                        <button
                            onClick={() => setCurrentTab('company')}
                            className={`px-4 py-2 rounded-none bg-transparent border-none shadow-none outline-none 
                                ${
                                currentTab === 'company'
                                    ? 'text-purple-700 font-bold'
                                    : 'text-purple-300'
                                } 
                                hover:bg-transparent
                                focus:outline-none focus:ring-0 
                                active:outline-none active:ring-0 
                                focus-visible:outline-none focus-visible:ring-0`}
                            style={{ border: 'none', boxShadow: 'none' }}
                        >
                        협업 중인 업체
                        </button>
                        <button
                            onClick={() => setCurrentTab('ongoing')}
                            className={`px-4 py-2 rounded-none bg-transparent border-none shadow-none outline-none 
                                ${
                                currentTab === 'ongoing'
                                    ? 'text-purple-700 font-bold'
                                    : 'text-purple-300'
                                } 
                                hover:bg-transparent
                                focus:outline-none focus:ring-0 
                                active:outline-none active:ring-0 
                                focus-visible:outline-none focus-visible:ring-0`}
                            style={{ border: 'none', boxShadow: 'none' }}
                        >
                        진행중인 협업
                        </button>
                    </div>
                    <button
                        onClick={() => navigate('/company/collaboration/setproduct')}
                        className="min-w-[90px] px-3 py-1.5 text-sm bg-pp text-white rounded hover:bg-purple-700 transition-colors"
                    >
                        패키지 추가
                    </button>
                </div>
            </div>

            {/* 탭 내용 */}
            {currentTab === 'product' ? (
                <div className="grid grid-cols-7 gap-2">
                    {packages.filter(item => item.status === 'ACTIVE' || item.status === 'INACTIVE').map((item, i) => {
                        const { originalPrice, discountedPrice } = calculateDiscountedPrice(item);
                        return (
                            <div
                                key={i}
                                className="text-center border max-w-[250px] p-2 rounded bg-white shadow-sm hover:shadow-md transition-shadow cursor-pointer"
                                onClick={() => navigate(`/company/collaboration/package/${item.packageId || item.id}`)}
                            >
                                <img
                                    src={item.imageUrl ? `${baseUrl}${item.imageUrl.replace(/\\/g, '/')}` : '/default/images/package.png'}
                                    alt={item.name || '패키지 이미지'}
                                    className="w-full aspect-[3/4] object-cover rounded-md mb-2"
                                />
                                <p className="text-sm font-bold text-black">{item.name || '이름 없음'}</p>
                                <p className="text-xs text-gray-500">
                                    {item.endDate
                                        ? `~ ${item.endDate[0]}.${String(item.endDate[1]).padStart(2, '0')}.${String(item.endDate[2]).padStart(2, '0')}`
                                        : '기간 정보 없음'}
                                </p>
                                <div className="mt-2">
                                    <p className="text-right text-gray-500 line-through text-sm">
                                        {originalPrice.toLocaleString()}원
                                    </p>
                                    <p className="font-bold text-right text-purple-700 text-lg">
                                        {discountedPrice.toLocaleString()}원
                                    </p>
                                    {item.discountrate > 0 && (
                                        <p className="text-xs text-right text-red-500">
                                            {item.discountrate}% 할인 적용
                                        </p>
                                    )}
                                </div>
                            </div>
                        );
                    })}
                </div>
            ) : currentTab === 'company' ? (
                myCategory && (() => {
                    // ACTIVE/INACTIVE 패키지에 포함된 모든 업체 추출
                    const activeOrInactiveCompanies = packages
                        .filter(pkg => pkg.status === 'ACTIVE' || pkg.status === 'INACTIVE')
                        .flatMap(pkg => [pkg.admin, pkg.cp1, pkg.cp2])
                        .filter(Boolean);

                    // 카테고리별 중복 제거 함수
                    const getUniqueCompaniesByCategory = (category) => {
                        const filtered = activeOrInactiveCompanies.filter(company => company.category === category);
                        return filtered.filter((company, idx, arr) => arr.findIndex(c => c.id === company.id) === idx);
                    };

                    return (
                        <div className="space-y-12">
                            {/* 스튜디오 (S) */}
                            {myCategory !== 'S' && (
                                <div>
                                    <h3 className="text-xl font-semibold text-purple-700 mb-4">스튜디오</h3>
                                    <div className="grid grid-cols-7 gap-2">
                                        {getUniqueCompaniesByCategory('S').length > 0 ? (
                                            getUniqueCompaniesByCategory('S').map((studio, i) => (
                                                <div key={studio.id} className="text-center border max-w-[250px] p-2 rounded">
                                                    <img
                                                        src={studio.profileImageUrl ? `${baseUrl}${studio.profileImageUrl.replace(/\\/g, '/')}` : '/default/images/company.png'}
                                                        className="w-full aspect-[3/4] object-cover rounded-md mb-2"
                                                        alt={studio.name || '스튜디오 이미지'}
                                                        onError={e => e.currentTarget.src = '/default/images/company.png'}
                                                    />
                                                    <p className="text-sm font-bold text-black">{studio.name}</p>
                                                    <p className="text-xs text-gray-500">{studio.address}</p>
                                                </div>
                                            ))
                                        ) : (
                                            <div className="col-span-7 text-center text-gray-400 py-12">협업 중인 스튜디오가 없습니다.</div>
                                        )}
                                    </div>
                                </div>
                            )}

                            {/* 드레스 (D) */}
                            {myCategory !== 'D' && (
                                <div>
                                    <h3 className="text-xl font-semibold text-purple-700 mb-4">드레스</h3>
                                    <div className="grid grid-cols-7 gap-2">
                                        {getUniqueCompaniesByCategory('D').length > 0 ? (
                                            getUniqueCompaniesByCategory('D').map((dress, i) => (
                                                <div key={dress.id} className="text-center border max-w-[250px] p-2 rounded">
                                                    <img
                                                        src={dress.profileImageUrl ? `${baseUrl}${dress.profileImageUrl.replace(/\\/g, '/')}` : '/default/images/company.png'}
                                                        className="w-full aspect-[3/4] object-cover rounded-md mb-2"
                                                        alt={dress.name || '드레스샵 이미지'}
                                                        onError={e => e.currentTarget.src = '/default/images/company.png'}
                                                    />
                                                    <p className="text-sm font-bold text-center text-black">{dress.name}</p>
                                                    <p className="text-xs text-gray-500">{dress.address}</p>
                                                </div>
                                            ))
                                        ) : (
                                            <div className="col-span-7 text-center text-gray-400 py-12">협업 중인 드레스 업체가 없습니다.</div>
                                        )}
                                    </div>
                                </div>
                            )}

                            {/* 메이크업 (M) */}
                            {myCategory !== 'M' && (
                                <div>
                                    <h3 className="text-xl font-semibold text-purple-700 mb-4">메이크업</h3>
                                    <div className="grid grid-cols-7 gap-2">
                                        {getUniqueCompaniesByCategory('M').length > 0 ? (
                                            getUniqueCompaniesByCategory('M').map((makeup, i) => (
                                                <div key={makeup.id} className="text-center border max-w-[250px] p-2 rounded">
                                                    <img
                                                        src={makeup.profileImageUrl ? `${baseUrl}${makeup.profileImageUrl.replace(/\\/g, '/')}` : '/default/images/company.png'}
                                                        className="w-full aspect-[3/4] object-cover rounded-md mb-2"
                                                        alt={makeup.name || '메이크업샵 이미지'}
                                                        onError={e => e.currentTarget.src = '/default/images/company.png'}
                                                    />
                                                    <p className="text-sm font-bold text-center text-black">{makeup.name}</p>
                                                    <p className="text-xs text-gray-500">{makeup.address}</p>
                                                </div>
                                            ))
                                        ) : (
                                            <div className="col-span-7 text-center text-gray-400 py-12">협업 중인 메이크업 업체가 없습니다.</div>
                                        )}
                                    </div>
                                </div>
                            )}
                        </div>
                    );
                })()
            ) : (
                // 진행중인 협업 탭 내용
                <div className="space-y-6 mt-4">
                    <h3 className="text-xl font-semibold text-purple-700 mb-4">진행중인 협업</h3>
                    {packages.filter(pkg => pkg.status === 'SETTING').length > 0 ? (
                        <div className="border rounded-lg overflow-hidden">
                            <table className="min-w-full divide-y divide-gray-200">
                                <thead className="bg-gray-50">
                                    <tr>
                                        <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">패키지명</th>
                                        <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">협업 업체</th>
                                        <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">상태</th>
                                        <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">작업</th>
                                    </tr>
                                </thead>
                                <tbody className="bg-white divide-y divide-gray-200">
                                    {packages.filter(pkg => pkg.status === 'SETTING').map((pkg, index) => (
                                        <tr key={index} className="hover:bg-gray-50">
                                            <td className="px-6 py-4 whitespace-nowrap">
                                                <div 
                                                    className="text-sm font-medium text-gray-900 cursor-pointer hover:text-purple-600"
                                                    onClick={() => navigate(`/company/collaboration/viewpackage/${pkg.packageId || pkg.id}`)}
                                                >
                                                    {pkg.name || '이름 없음'}
                                                </div>
                                            </td>
                                            <td className="px-6 py-4 whitespace-nowrap">
                                                <div className="text-sm text-gray-900">
                                                    {[pkg.admin?.name, pkg.cp1?.name, pkg.cp2?.name].filter(Boolean).join(', ')}
                                                </div>
                                            </td>
                                            <td className="px-6 py-4 whitespace-nowrap">
                                                <span className={`px-2 inline-flex text-xs leading-5 font-semibold rounded-full bg-blue-100 text-blue-800`}>
                                                    패키지설정중
                                                </span>
                                                <div className="mt-1 text-xs text-gray-500">
                                                    패키지 설정이 진행중입니다
                                                </div>
                                            </td>
                                            <td className="px-6 py-4 whitespace-nowrap text-sm">
                                                <button 
                                                    className="text-purple-600 hover:text-purple-900 mr-3 bg-white border border-purple-600 rounded-md px-2 py-1"
                                                    onClick={() => navigate(`/company/collaboration/setproduct/${pkg.packageId}`)}
                                                >
                                                    상세보기
                                                </button>
                                            </td>
                                        </tr>
                                    ))}
                                </tbody>
                            </table>
                        </div>
                    ) : (
                        <div className="text-center py-12 text-gray-500">진행중인 협업이 없습니다</div>
                    )}
                </div>
            )}
        </div>
    );
}

export default Collaboration; 