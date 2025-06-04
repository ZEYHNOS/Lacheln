import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import apiClient from "../../../lib/apiClient";
import ScheduleSelect from '../../Tool/Schedule/ScheduleSelect.jsx';
import { toast } from 'react-toastify';

const baseUrl = import.meta.env.VITE_API_BASE_URL;

function Packagedetail() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [showSchedule, setShowSchedule] = useState(false);

  useEffect(() => {
    const fetchDetail = async () => {
      try {
        const res = await apiClient.get(`/package/info/${id}`);
        if (res.data && res.data.data) {
          setData(res.data.data);
        }
      } catch (err) {
        alert("패키지 정보를 불러오지 못했습니다.");
      } finally {
        setLoading(false);
      }
    };
    fetchDetail();
  }, [id]);

  if (loading) return <div className="text-center py-20">로딩중...</div>;
  if (!data) return <div className="text-center py-20 text-red-500">데이터가 없습니다.</div>;

  // 이미지 경로 변환
  const imageUrl = data.imageUrl ? `${baseUrl}${data.imageUrl.replace(/\\/g, '/')}` : '/default/images/product.png';
  // 상품 리스트
  const products = data.productInfoList || [];
  // 가격/할인 계산
  const totalPrice = products.reduce((sum, p) => sum + (p.price || 0), 0);
  const discountRate = data.discountRate || data.discountrate || 0;
  const hasDiscount = discountRate > 0;
  const discountedPrice = hasDiscount ? Math.floor(totalPrice * (1 - discountRate / 100)) : totalPrice;
  const benefit = totalPrice - discountedPrice;

  // 업체 ID 배열 추출
  const cpIds = (data.productInfoList || [])
    .map(p => p.companyId || p.cpId)
    .filter(Boolean);

  // 패키지 장바구니 추가 함수
  const handlePackageCartAdd = async ({ localDateTime }) => {
    try {
      const packData = {
        packId: data.packageId || data.id,
        packName: data.name,
        discountPrice: data.discountrate || data.discountRate || 0,
        packImageUrl: data.imageUrl ? baseUrl + data.imageUrl.replace(/\\/g, '/') : '',
        taskTime: data.taskTime ? {
          hour: parseInt(data.taskTime.split(':')[0]),
          minute: parseInt(data.taskTime.split(':')[1]),
          second: 0,
          nano: 0
        } : { hour: 0, minute: 0, second: 0, nano: 0 },
        startTime: localDateTime,
        cartAddProductRequest: (data.productInfoList || []).map(p => ({
          cp_id: p.companyId || p.cpId || 0,
          pd_id: p.productId || p.pdId || 0,
          cp_name: p.companyName,
          pd_name: p.productName,
          pd_image_url: p.imageUrl ? baseUrl + p.imageUrl.replace(/\\/g, '/') : '',
          pd_price: p.price,
          cart_quantity: 1,
          task_time: { hour: 0, minute: 0, second: 0, nano: 0 }, // 필요시 계산
          start_datetime: localDateTime,
          option_details: [] // 옵션이 있다면 여기에 추가
        }))
      };

      console.log('패키지 장바구니 추가 요청 데이터:', packData);
      
      const response = await apiClient.post('/user/cart/add/package', packData);
      if (response.data?.result?.resultCode === 200) {
        toast.success('패키지가 장바구니에 추가되었습니다.');
        setShowSchedule(false);
      } else {
        toast.error('패키지 장바구니 추가에 실패했습니다.');
      }
    } catch (e) {
      toast.error('패키지 장바구니 추가 중 오류가 발생했습니다.');
    }
  };

  return (
    <div className="max-w-2xl mx-auto bg-white rounded-lg shadow p-6 mt-8 mb-16">
      <div className="w-full mb-6">
        <img src={imageUrl} alt="패키지 대표 이미지" className="w-full rounded-lg object-contain bg-white" style={{ aspectRatio: '16 / 9' }} />
      </div>
      <div className="mb-4">
        <div className="text-xl font-bold mb-2 text-[#22223B]">{data.name}</div>
        <div className="flex items-end gap-2 mb-2">
          <span className="text-2xl font-extrabold text-[#7C1FFF]">{discountedPrice.toLocaleString()}원</span>
          {hasDiscount && (
            <>
              <span className="text-base text-gray-400 line-through">{totalPrice.toLocaleString()}원</span>
              <span className="text-pink-500 font-bold text-lg">{discountRate}%</span>
            </>
          )}
        </div>
        <div className="text-sm text-gray-500">최대 혜택 적용 시</div>
        {data.taskTime && (
          <div className="text-sm text-gray-500 mt-1">
            예상 작업시간: <span className="font-medium">{data.taskTime}</span>
          </div>
        )}
      </div>

      <div className="mb-6">
        <div className="text-base font-bold text-[#845EC2] mb-2">포함 상품 & 패키지 특전</div>
        <div className="space-y-3">
          {products.map((p, i) => (
            <div key={i} className="flex items-center gap-3 bg-gray-50 rounded p-2">
              {p.imageUrl && (
                <img src={`${baseUrl}${p.imageUrl.replace(/\\/g, '/')}`} alt={p.productName} className="w-14 h-14 object-cover rounded" />
              )}
              <div>
                <div className="font-bold text-[#22223B] text-sm">{p.productName}</div>
                <div className="text-xs text-gray-500">{p.companyName}</div>
                {p.desc && <div className="text-xs text-gray-400">{p.desc}</div>}
              </div>
            </div>
          ))}
        </div>
      </div>

      {/* 패키지 특전/가격 표 */}
      <div className="bg-gray-50 rounded-lg p-4 mb-6">
        <div className="font-bold text-[#845EC2] mb-2">패키지 특전</div>
        <div className="flex flex-col gap-1 text-sm">
          <div className="flex justify-between"><span className="text-gray-700 font-semibold">정상가</span><span className="text-gray-700 font-semibold">{totalPrice.toLocaleString()} 원</span></div>
          {hasDiscount && <div className="flex justify-between"><span className="text-[#FF5959] font-semibold">할인가</span><span className="text-[#FF5959] font-semibold">-{benefit.toLocaleString()} 원</span></div>}
          <div className="flex justify-between"><span className="text-[#7C1FFF] font-bold">최종 혜택가</span><span className="text-[#7C1FFF] font-bold">{discountedPrice.toLocaleString()} 원</span></div>
        </div>
        {hasDiscount && (
          <div className="mt-3 bg-pink-100 text-pink-600 text-center rounded p-2 font-semibold">
            패키지 구매 시 <span className="font-bold">총 {benefit.toLocaleString()}원 절약</span>
          </div>
        )}
      </div>

      <div className="flex gap-4 mt-8">
        <button className="flex-1 py-3 rounded bg-[#B39CD0] text-white font-bold text-lg hover:bg-[#845EC2] transition" onClick={() => setShowSchedule(true)}>장바구니</button>
        <button className="flex-1 py-3 rounded bg-[#B39CD0] text-white font-bold text-lg hover:bg-[#845EC2] transition" onClick={() => navigate(-1)}>목록으로</button>
      </div>

      {/* 일정 선택 모달 */}
      {showSchedule && (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-40">
          <div className="bg-white rounded-lg shadow-lg" style={{ padding: 0 }}>
            <div className="p-8 text-black">
              <ScheduleSelect
                cpIds={cpIds}
                productId={data.id}
                onSelect={handlePackageCartAdd}
              />
              <button className="mt-4 w-full py-2 bg-gray-300 rounded" onClick={() => setShowSchedule(false)}>닫기</button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

export default Packagedetail;
