import React, { useEffect, useState } from 'react';
import apiClient from "../../../../lib/apiClient";
import axios from 'axios';
import { data } from 'react-router-dom';

const baseUrl = import.meta.env.VITE_API_BASE_URL;

function Collaboration() {
    const [packages, setPackages] = useState([]);
    const [myCategory, setMyCategory] = useState(null);
    const [companies, setCompanies] = useState([]);
    const [currentTab, setCurrentTab] = useState("product");

    // 업체 타입을 category 값으로만 구분 (대문자)
    const getCompanyType = (company) => {
        if (!company) return null;
        return (company.category || '').toUpperCase();
    };

    const filteredByType = (type) =>
        companies.filter((c) => getCompanyType(c) === type);

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

    return (
        <div className="p-8 relative">
            {/* 탭 버튼 + 밑줄 */}
            <div className="relative">
                <div className="flex space-x-4 text-lg font-semibold mb-2 z-10 relative">
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
                </div>
                {/* absolute 밑줄 */}
                <div className="absolute left-0 right-0 border-b border-purple-300" style={{top: '44px', height: 0}} />
            </div>

            {/* 탭 내용 */}
            {currentTab === 'product' ? (
                <div className="grid grid-cols-4 gap-6">
                    {packages.map((item, i) => (
                        <div
                            key={i}
                            className="rounded-lg border overflow-hidden shadow-sm"
                        >
                            <img
                                src={item.imageUrl ? `${baseUrl}${item.imageUrl.replace(/\\/g, '/')}` : '/default/images/package.png'}
                                alt={item.name || '패키지 이미지'}
                                className="w-full aspect-[3/4] object-cover"
                            />
                            <div className="p-3 text-sm">
                                <p className="text-sm font-bold text-center text-black">{item.name || '이름 없음'}</p>
                                <p className="text-xs text-center text-gray-500">
                                    {item.startDate && item.endDate
                                        ? `${item.startDate[0]}.${item.startDate[1]}.${item.startDate[2]} ~ ${item.endDate[0]}.${item.endDate[1]}.${item.endDate[2]}`
                                        : '기간 정보 없음'}
                                </p>
                                <p className="font-bold text-right text-blue-500 text-lg mt-1">
                                    {(item.totalPrice || 0).toLocaleString()}원
                                </p>
                            </div>
                        </div>
                    ))}
                </div>
            ) : (
                myCategory && (
                    <div className="space-y-12">
                        {/* 스튜디오 (S) */}
                        {myCategory !== 'S' && (
                            <div>
                                <h3 className="text-xl font-semibold text-purple-700 mb-4">스튜디오</h3>
                                <div className="grid grid-cols-5 gap-4">
                                    {filteredByType('S').length > 0 ? (
                                        filteredByType('S').map((studio, i) => (
                                            <div key={i} className="text-center border p-2 rounded">
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
                                        <div className="col-span-5 text-center text-gray-400 py-12">협업 중인 스튜디오가 없습니다.</div>
                                    )}
                                </div>
                            </div>
                        )}

                        {/* 드레스 (D) */}
                        {myCategory !== 'D' && (
                            <div>
                                <h3 className="text-xl font-semibold text-purple-700 mb-4">드레스</h3>
                                <div className="grid grid-cols-5 gap-4">
                                    {filteredByType('D').length > 0 ? (
                                        filteredByType('D').map((dress, i) => (
                                            <div key={i} className="text-center border p-2 rounded">
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
                                        <div className="col-span-5 text-center text-gray-400 py-12">협업 중인 드레스 업체가 없습니다.</div>
                                    )}
                                </div>
                            </div>
                        )}

                        {/* 메이크업 (M) */}
                        {myCategory !== 'M' && (
                            <div>
                                <h3 className="text-xl font-semibold text-purple-700 mb-4">메이크업</h3>
                                <div className="grid grid-cols-5 gap-4">
                                    {filteredByType('M').length > 0 ? (
                                        filteredByType('M').map((makeup, i) => (
                                            <div key={i} className="text-center border p-2 rounded">
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
                                        <div className="col-span-5 text-center text-gray-400 py-12">협업 중인 메이크업 업체가 없습니다.</div>
                                    )}
                                </div>
                            </div>
                        )}
                    </div>
                )
            )}
        </div>
    );
}

export default Collaboration; 