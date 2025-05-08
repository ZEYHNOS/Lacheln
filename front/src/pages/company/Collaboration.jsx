import React, { useEffect, useState } from 'react';
import apiClient from "../../../../lib/apiClient";
import axios from 'axios';

const baseUrl = import.meta.env.VITE_API_BASE_URL;

function Collaboration() {
    const [packages, setPackages] = useState([]);
    const [myCategory, setMyCategory] = useState(null);
    const [companies, setCompanies] = useState([]);
    const [currentTab, setCurrentTab] = useState("product");

    const filteredByType = (type) =>
        companies.filter((c) => c.category !== myCategory && c.category === type);

    useEffect(() => {
        axios.get(`${baseUrl}/company/category`,{
            withCredentials: true})
            .then(res => {
                setMyCategory(res.category);
            })
            .catch(err => console.error("카테고리 불러오기 실패", err));
    }, []);

    useEffect(() => {
        apiClient.get("/package/list")
        .then((res) => {
            const rawData = res.data.data || [];  // ✅ data 배열에서 실제 데이터 추출
            setPackages(rawData);            // ✅ 배열로 들어감
          
            const companies = [];
            rawData.forEach(pkg => {
              if (pkg.cp1) companies.push(pkg.cp1);
              if (pkg.cp2) companies.push(pkg.cp2);
            });
          
            setCompanies(companies);
          })
            .catch((err) => console.error("❌ 데이터 불러오기 실패:", err));
    }, []);

    return (
        <div className="p-8">
            {/* 탭 버튼 */}
            <div className="flex space-x-4 text-lg font-semibold mb-8">
                <button
                    onClick={() => setCurrentTab('product')}
                    className={`px-4 py-2 rounded-none bg-transparent border-none shadow-none outline-none 
                        ${
                        currentTab === 'product'
                            ? 'text-purple-700 font-bold'
                            : 'text-purple-300'
                        } 
                        hover:bg-transparent hover:text-purple-300 
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
                        hover:bg-transparent hover:text-purple-300 
                        focus:outline-none focus:ring-0 
                        active:outline-none active:ring-0 
                        focus-visible:outline-none focus-visible:ring-0`}
                    style={{ border: 'none', boxShadow: 'none' }}
                    >
                협업 중인 업체
                </button>
            </div>
        
        {/* 탭 내용 */}
        {currentTab === 'product' ? (
        // 상품 리스트
        <div className="grid grid-cols-4 gap-6">
            {packages.map((item, i) => (
            <div
                key={i}
                className="rounded-lg border overflow-hidden shadow-sm"
            >
                <img
                src={`${baseUrl}${item.imageUrl.replace(/\\/g, '/')}`}
                alt={item.name}
                className="w-full aspect-[3/4] object-cover"
                />
                <div className="p-3 text-sm">
                <p className="text-gray-500">{item.name}</p>
                <p className="text-gray-500">{item.period}</p>
                <p className="font-bold text-lg mt-1">
                    {item.price.toLocaleString()}원
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
                    {filteredByType('S').map((studio, i) => (
                    <div key={i} className="text-center">
                        <img
                        src={`${baseUrl}${studio.profileImageUrl.replace(/\\/g, '/')}`}
                        className="w-full aspect-[3/4] object-cover rounded-md mb-2"
                        alt={studio.name}
                        />
                        <p className="text-sm font-medium">{studio.name}</p>
                        <p className="text-xs text-gray-500">{studio.address}</p>
                    </div>
                    ))}
                </div>
                </div>
            )}
    
            {/* 드레스 (D) */}
            {myCategory !== 'D' && (
                <div>
                <h3 className="text-xl font-semibold text-purple-700 mb-4">드레스</h3>
                <div className="grid grid-cols-5 gap-4">
                    {filteredByType('D').map((dress, i) => (
                    <div key={i} className="text-center">
                        <img
                        src={`${baseUrl}${dress.profileImageUrl.replace(/\\/g, '/')}`}
                        className="w-full aspect-[3/4] object-cover rounded-md mb-2"
                        alt={dress.name}
                        />
                        <p className="text-sm font-medium">{dress.name}</p>
                        <p className="text-xs text-gray-500">{dress.address}</p>
                    </div>
                    ))}
                </div>
                </div>
            )}
    
            {/* 메이크업 (M) */}
            {myCategory !== 'M' && (
                <div>
                <h3 className="text-xl font-semibold text-purple-700 mb-4">메이크업</h3>
                <div className="grid grid-cols-5 gap-4">
                    {filteredByType('M').map((makeup, i) => (
                    <div key={i} className="text-center">
                        <img
                        src={`${baseUrl}${makeup.profileImageUrl.replace(/\\/g, '/')}`}
                        className="w-full aspect-[3/4] object-cover rounded-md mb-2"
                        alt={makeup.name}
                        />
                        <p className="text-sm font-medium">{makeup.name}</p>
                        <p className="text-xs text-gray-500">{makeup.address}</p>
                    </div>
                    ))}
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