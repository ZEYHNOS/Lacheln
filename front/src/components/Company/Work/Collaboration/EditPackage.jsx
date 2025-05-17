import React, { useState, useEffect, useRef } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import apiClient from '../../../../lib/apiClient';
import Addphoto from '../../../../image/Company/addimage.png';
import AddWrite from '../../../Tool/WriteForm/AddWrite';

const baseUrl = import.meta.env.VITE_API_BASE_URL;

function EditPackage() {
    const { packageId } = useParams();
    const navigate = useNavigate();
    const fileInputRef = useRef(null);
    const editorRef = useRef(null);
    const [packageData, setPackageData] = useState({
        name: '',
        endDate: '',
        discountrate: 0
    });
    const [image, setImage] = useState(null);
    const [imagePreview, setImagePreview] = useState(null);
    const [loading, setLoading] = useState(true);
    const [submitting, setSubmitting] = useState(false);
    const [error, setError] = useState(null);
    const [packageCompanies, setPackageCompanies] = useState({ admin: null, cp1: null, cp2: null });

    useEffect(() => {
        const fetchPackageData = async () => {
            try {
                setLoading(true);
                setError(null);
                const response = await apiClient.get(`/package/${packageId}`);
                const data = response.data.data || response.data;
                if (!data) throw new Error("패키지 데이터가 없습니다.");

                // 이미지 경로 처리
                let imgUrl = data.imageUrl;
                if (imgUrl) {
                    imgUrl = `${baseUrl}${imgUrl.replace(/\\/g, '/')}`;
                    setImagePreview(imgUrl);
                }

                // 종료일 처리
                let endDateStr = '';
                if (data.endDate && Array.isArray(data.endDate)) {
                    const [year, month, day, hour = 0, minute = 0] = data.endDate;
                    const endDateObj = new Date(year, month-1, day, hour, minute);
                    endDateStr = endDateObj.toISOString().slice(0, 16);
                }

                setPackageData({
                    name: data.name || '',
                    endDate: endDateStr,
                    discountrate: data.discountrate || 0
                });

                // 업체별 정보 저장
                setPackageCompanies({
                    admin: data.admin || null,
                    cp1: data.cp1 || null,
                    cp2: data.cp2 || null
                });

                // descriptionResponseList를 에디터에 세팅
                if (editorRef.current && Array.isArray(data.descriptionResponseList)) {
                    editorRef.current.setContentFromJsonArray(data.descriptionResponseList);
                }
                setLoading(false);
            } catch (error) {
                console.error('패키지 데이터 로딩 실패:', error);
                setError("패키지 정보를 불러오는데 실패했습니다.");
                setLoading(false);
            }
        };
        fetchPackageData();
    }, [packageId]);

    // 대표 이미지 업로드
    const handleImageUpload = (e) => {
        const file = e.target.files[0];
        if (file) {
            if (file.size > 5 * 1024 * 1024) {
                alert("이미지 크기는 5MB 이하여야 합니다.");
                return;
            }
            if (!file.type.match('image.*')) {
                alert("이미지 파일만 업로드 가능합니다.");
                return;
            }
            setImage(file);
            setImagePreview(URL.createObjectURL(file));
        }
    };

    // 이미지 서버 업로드 함수
    const uploadImageToServer = async (file) => {
        const formData = new FormData();
        formData.append("images", file);
        try {
            const res = await apiClient.post("/product/image/upload", formData, {
                headers: { "Content-Type": "multipart/form-data" },
            });
            if (res.data && res.data.data) {
                if (Array.isArray(res.data.data)) {
                    return res.data.data[0];
                } else if (typeof res.data.data === 'object') {
                    return res.data.data.url || res.data.data.imageUrl || res.data.data.path;
                } else if (typeof res.data.data === 'string') {
                    return res.data.data;
                }
            }
            return null;
        } catch (err) {
            console.error("이미지 업로드 실패:", err);
            return null;
        }
    };

    // BASE64 파일 변환 함수
    function base64toBlob(base64Data) {
        const arr = base64Data.split(',');
        const mime = arr[0].match(/:(.*?);/)[1];
        const bstr = atob(arr[1]);
        let n = bstr.length;
        const u8arr = new Uint8Array(n);
        while (n--) {
            u8arr[n] = bstr.charCodeAt(n);
        }
        return new Blob([u8arr], { type: mime });
    }

    // 상세설명 이미지 URL 변환
    const processDescriptionList = async (descriptionList) => {
        const processedList = [];
        for (const item of descriptionList) {
            if (
                item.type === "IMAGE" &&
                typeof item.value === "string" &&
                item.value.startsWith("data:")
            ) {
                const blob = base64toBlob(item.value);
                const extension = blob.type.split('/')[1];
                const file = new File([blob], `image_${Date.now()}.${extension}`, {
                    type: blob.type,
                });
                const url = await uploadImageToServer(file);
                if (url) {
                    processedList.push({ ...item, value: url });
                }
            } else {
                processedList.push(item);
            }
        }
        return processedList.map((item, index) => ({ ...item, order: index }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            setSubmitting(true);
            setError(null);
            let imageUrl = imagePreview;
            if (image) {
                const uploadedImageUrl = await uploadImageToServer(image);
                if (!uploadedImageUrl) {
                    alert("대표 이미지 업로드에 실패했습니다.");
                    setSubmitting(false);
                    return;
                }
                imageUrl = uploadedImageUrl;
            }
            if (imageUrl && imageUrl.startsWith(baseUrl)) {
                imageUrl = imageUrl.replace(baseUrl, '');
            }
            // 에디터에서 descriptionResponseList 추출 및 이미지 처리
            let descriptionRequestList = [];
            if (editorRef.current) {
                const rawList = editorRef.current.getContentAsJsonArray();
                descriptionRequestList = await processDescriptionList(rawList);
            }
            const updatedData = {
                ...packageData,
                imageUrl: imageUrl,
                descriptionRequestList
            };
            await apiClient.put(`/package/update/${packageId}`, updatedData);
            alert('패키지 정보가 성공적으로 수정되었습니다.');
            navigate('/company/collaboration');
        } catch (error) {
            console.error('패키지 수정 실패:', error);
            setError("패키지 수정에 실패했습니다.");
        } finally {
            setSubmitting(false);
        }
    };

    // 원가 및 최종 판매가 계산
    const getOriginalPrice = () => {
        return (packageCompanies.admin?.productPrice || 0) + (packageCompanies.cp1?.productPrice || 0) + (packageCompanies.cp2?.productPrice || 0);
    };
    const getDiscountedPrice = () => {
        const original = getOriginalPrice();
        const discount = Number(packageData.discountrate) || 0;
        return Math.round(original * (1 - discount / 100));
    };

    if (loading) {
        return <div className="w-full p-8 text-center">로딩 중...</div>;
    }

    return (
        <div className="w-full max-w-6xl mx-auto p-8 bg-white text-black rounded-md">
            <h1 className="text-2xl font-bold text-[#845EC2] mb-8">패키지 정보 수정</h1>
            {error && (
                <div className="mb-6 p-4 bg-red-50 border border-red-200 rounded text-red-600">
                    {error}
                </div>
            )}
            <form onSubmit={handleSubmit}>
                <div className="flex items-start space-x-8 mb-8">
                    {/* 대표 이미지 */}
                    <div className="w-1/3 p-2">
                        <div className="border rounded-lg overflow-hidden bg-gray-100 w-full h-96 flex items-center justify-center">
                            {imagePreview ? (
                                <img src={imagePreview} alt="대표 이미지" className="w-full h-full object-cover" />
                            ) : (
                                <span className="text-gray-400">이미지 없음</span>
                            )}
                        </div>
                        <label className="mt-4 block w-full text-center border border-[#845EC2] text-[#845EC2] rounded-md py-4 cursor-pointer bg-white flex items-center justify-center space-x-2">
                            <img src={Addphoto} alt="이미지 추가" className="w-6 h-6" />
                            <span className="text-[#845EC2] font-medium">이미지 추가하기</span>
                            <input 
                                ref={fileInputRef}
                                type="file" 
                                accept="image/*" 
                                className="hidden" 
                                onChange={handleImageUpload} 
                            />
                        </label>
                    </div>
                    {/* 패키지 정보 입력 */}
                    <div className="w-2/3 p-2">
                        <div className="space-y-6">
                            {/* 패키지명 입력 */}
                            <div>
                                <label htmlFor="name" className="block text-sm font-medium text-gray-700 mb-1">
                                    패키지명
                                </label>
                                <input
                                    type="text"
                                    id="name"
                                    value={packageData.name}
                                    onChange={(e) => setPackageData({...packageData, name: e.target.value})}
                                    className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-500 bg-white"
                                    required
                                />
                            </div>
                            {/* 종료일 입력 */}
                            <div>
                                <label htmlFor="endDate" className="block text-sm font-medium text-gray-700 mb-1">
                                    종료일
                                </label>
                                <input
                                    type="datetime-local"
                                    id="endDate"
                                    value={packageData.endDate}
                                    onChange={(e) => setPackageData({...packageData, endDate: e.target.value})}
                                    className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-500 bg-white"
                                    required
                                />
                            </div>
                            {/* 할인율 입력 */}
                            <div>
                                <label htmlFor="discountrate" className="block text-sm font-medium text-gray-700 mb-1">
                                    할인율 (%)
                                </label>
                                <input
                                    type="number"
                                    id="discountrate"
                                    value={packageData.discountrate}
                                    onChange={(e) => setPackageData({...packageData, discountrate: Number(e.target.value)})}
                                    min="0"
                                    max="100"
                                    className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-500 bg-white"
                                />
                            </div>
                            {/* 원가/최종 판매가 표시 */}
                            <div className="flex flex-col gap-1 mt-2">
                                <div className="flex justify-between text-sm">
                                    <span className="text-gray-500">원가(3개 업체 합산)</span>
                                    <span className="font-semibold text-black">{getOriginalPrice().toLocaleString()}원</span>
                                </div>
                                <div className="flex justify-between text-sm">
                                    <span className="text-gray-500">할인율 적용 최종 판매가</span>
                                    <span className="font-bold text-purple-700">{getDiscountedPrice().toLocaleString()}원</span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                {/* 패키지 설명 입력 (에디터) - 이미지/정보 입력 아래 전체 너비로 */}
                <div className="mb-8 w-full">
                    <label className="text-xl font-bold text-[#845EC2] mb-8">패키지 설명</label>
                    <AddWrite ref={editorRef} />
                </div>
                {/* 버튼 그룹 */}
                <div className="flex justify-end space-x-4 mt-8">
                    <button
                        type="button"
                        onClick={() => navigate('/company/collaboration')}
                        className="bg-gray-300 text-black px-8 py-3 rounded hover:bg-gray-400"
                        disabled={submitting}
                    >
                        취소
                    </button>
                    <button
                        type="submit"
                        disabled={submitting}
                        className={`${submitting ? 'bg-purple-300' : 'bg-[#845EC2] hover:bg-purple-500'} text-white px-8 py-3 rounded`}
                    >
                        {submitting ? '처리 중...' : '저장'}
                    </button>
                </div>
            </form>
        </div>
    );
}

export default EditPackage;
