import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import apiClient from "../../../lib/apiClient";

const baseUrl = import.meta.env.VITE_API_BASE_URL;
const sortTabs = ["인기 많은 순", "최근 등록 순", "할인 순"];

function Package() {
  const [selectedTab, setSelectedTab] = useState("인기순");
  const [visibleCount, setVisibleCount] = useState(8);
  const [packageList, setPackageList] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchPackages = async () => {
      try {
        const res = await apiClient.get("/package");
        if (res.data && res.data.data) {
          setPackageList(res.data.data);
        }
      } catch (err) {
        console.error("패키지 데이터 불러오기 실패", err);
      }
    };
    fetchPackages();
  }, []);

  // 정렬된 패키지 리스트
  const sortedPackages = (() => {
    if (selectedTab === "최근등록순") {
      return [...packageList].sort((a, b) => b.packageId - a.packageId);
    }
    if (selectedTab === "할인순") {
      return [...packageList].sort((a, b) => (b.discountrate || 0) - (a.discountrate || 0));
    }
    // 기본: 인기순 (그대로)
    return packageList;
  })();

  return (
    <div className="min-h-screen bg-white flex flex-col items-center py-8">
      {/* 정렬 탭 */}
      <div className="flex mb-8 w-full max-w-4xl">
        {["인기순", "최근등록순", "할인순"].map(tab => (
          <button
            key={tab}
            className={`flex-1 py-2 rounded-t-lg text-base font-semibold transition-colors duration-200
              ${selectedTab === tab ? "bg-[#845EC2] text-[#ffffff]" : "bg-[#F3EFFF] text-[#B39CD0] hover:bg-[#F3EFFF]"}`}
            onClick={() => setSelectedTab(tab)}
          >
            {tab}
          </button>
        ))}
      </div>

      {/* 패키지 카드 리스트 */}
      <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6 w-full max-w-6xl mb-8">
        {sortedPackages.length === 0 ? (
          <div className="col-span-4 text-center text-gray-400 py-20">패키지가 없습니다.</div>
        ) : (
          sortedPackages.slice(0, visibleCount).map(pkg => {
            // 이미지 경로 변환
            const imageUrl = pkg.imageUrl ? `${baseUrl}${pkg.imageUrl.replace(/\\/g, '/')}` : '/default/images/product.png';
            // 구성상품 리스트
            const products = pkg.packageProductResponseList || [];
            // 가격 합산
            const totalPrice = products.reduce((sum, p) => sum + (p.price || 0), 0);
            // 할인율 적용
            const discountRate = pkg.discountRate || pkg.discountrate || 0;
            const hasDiscount = discountRate > 0;
            const discountedPrice = hasDiscount ? Math.floor(totalPrice * (1 - discountRate / 100)) : totalPrice;
            return (
              <div
                key={pkg.packageId}
                className="bg-white border rounded-lg shadow p-4 flex flex-col cursor-pointer hover:shadow-lg transition"
                onClick={() => navigate(`/package/${pkg.packageId}`)}
              >
                <img
                  src={imageUrl}
                  alt="패키지 이미지"
                  className="w-full object-cover rounded mb-3"
                  style={{ aspectRatio: '2 / 3' }}
                />
                <div className="font-bold mb-1 text-lg text-black">{pkg.packageName}</div>
                <ul className="text-xs text-gray-600 mb-2">
                  {products.map((p, i) => (
                    <li key={i}>
                      <span className="font-semibold">{p.productName}</span> <span className="text-gray-400">|</span> {p.companyName}
                    </li>
                  ))}
                </ul>
                <div className="mb-1 flex flex-col items-start">
                  {hasDiscount ? (
                    <>
                      <span className="text-xs text-gray-400 line-through mb-1">{totalPrice.toLocaleString()}원</span>
                      <span className="text-2xl font-extrabold text-[#7C1FFF] leading-tight">{discountedPrice.toLocaleString()}원</span>
                      <span className="text-xs text-[#FF5959] mt-1">{discountRate}% 할인 적용</span>
                    </>
                  ) : (
                    <span className="font-bold text-lg text-[#845EC2]">총 {totalPrice.toLocaleString()}원</span>
                  )}
                </div>
                <button className="bg-[#845ec2] text-white py-2 rounded hover:bg-purple-700 hover:text-white transition mt-2">패키지 상세 보기</button>
              </div>
            );
          })
        )}
      </div>

      {/* 더보기 버튼 */}
      {sortedPackages.length > visibleCount && (
        <button
          className="px-8 py-2 bg-[#B39CD0] text-white rounded hover:bg-[#845EC2] transition mb-8"
          onClick={() => setVisibleCount(visibleCount + 8)}
        >
          더보기
        </button>
      )}
    </div>
  );
}

export default Package; 