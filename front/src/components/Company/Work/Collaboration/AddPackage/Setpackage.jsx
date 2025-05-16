import React, { useRef, useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import Addphoto from "../../../../../image/Company/addimage.png";
import apiClient from "../../../../../lib/apiClient";
import AddWrite from "../../../../Tool/WriteForm/AddWrite";

function Setpackage() {
    const { id } = useParams(); // 라우트 경로의 :id와 일치하는 매개변수명 사용
    const packageId = id; // id 값을 packageId로 사용
    
    const navigate = useNavigate();
    // 패키지 정보 상태
    const [packageInfo, setPackageInfo] = useState({
        name: "",
        products: [],
        totalPrice: 0
    });
    const [discount, setDiscount] = useState(0);
    const [endDate, setEndDate] = useState("");
    const [description, setDescription] = useState("");
    const [image, setImage] = useState(null);
    const [imagePreview, setImagePreview] = useState(null);
    const [loading, setLoading] = useState(true); // 항상 로딩 상태로 시작 (패키지 정보를 가져와야 함)
    const [submitting, setSubmitting] = useState(false);
    const [error, setError] = useState(null);
    const fileInputRef = useRef(null);
    const editorRef = useRef(null);
    
    // 상세 설명 저장 (에디터에서 가져올 JSON 배열)
    const [descriptionList, setDescriptionList] = useState([]);

    // API에서 패키지 정보 가져오기
    useEffect(() => {
        const fetchPackageInfo = async () => {
            try {
                setLoading(true);
                setError(null);
                
                // API 호출로 패키지 정보 가져오기
                const res = await apiClient.get(`/package/${packageId}`, { withCredentials: true });
                
                // 데이터 구조 확인 및 처리
                const packageData = res.data.data || res.data;
                
                if (!packageData) {
                    throw new Error("패키지 데이터가 없습니다.");
                }
                
                // 패키지명 추출
                const name = packageData.name || "";
                
                // 상품 정보 추출
                let products = [];
                let totalPrice = 0;
                
                try {
                    // 각 업체별 상품 정보 추출 (setproduct.jsx 형식 처리)
                    const companies = ['admin', 'cp1', 'cp2'];
                    companies.forEach(companyKey => {
                        const company = packageData[companyKey];
                        if (company && company.productId) {
                            products.push({
                                id: company.productId,
                                name: company.productName || `${companyKey} 상품`,
                                price: company.productPrice || 0,
                                company: company.name
                            });
                        }
                    });
                    
                    // 총 가격 계산 - API에서 제공하는 경우 해당 값 사용
                    if (packageData.totalPrice) {
                        totalPrice = packageData.totalPrice;
                    } else {
                        totalPrice = products.reduce((sum, product) => sum + (product.price || 0), 0);
                    }
                } catch (processError) {
                    console.error("상품 정보 처리 중 오류:", processError);
                    // 오류가 발생해도 계속 진행
                }
                
                // 안전하게 패키지 정보 설정
                setPackageInfo(prevState => ({
                    ...prevState,
                    name,
                    products: products || [],
                    totalPrice: totalPrice || 0
                }));
                
                // 기존 패키지 정보가 있으면 해당 값으로 초기화
                // discountrate로 오는 경우 처리 (API 응답 구조에 맞춤)
                if (packageData.discount !== undefined) {
                    setDiscount(Number(packageData.discount));
                }
                if (packageData.discountrate !== undefined) {
                    setDiscount(Number(packageData.discountrate));
                }
                
                // endDate 처리 - 배열 형식으로 오는 경우와 문자열로 오는 경우 모두 처리
                try {
                    if (packageData.endDate) {
                        if (Array.isArray(packageData.endDate)) {
                            // [2099, 12, 31, 0, 0] 형식 처리
                            const [year, month, day, hour, minute] = packageData.endDate;
                            const endDateObj = new Date(year, month-1, day, hour, minute);
                            setEndDate(endDateObj.toISOString().slice(0, 16));
                        } else {
                            // 일반 날짜 문자열 처리
                            const endDateObj = new Date(packageData.endDate);
                            setEndDate(endDateObj.toISOString().slice(0, 16));
                        }
                    }
                } catch (dateError) {
                    console.error("날짜 처리 중 오류:", dateError);
                    // 기본 날짜 설정
                    const defaultDate = new Date();
                    defaultDate.setMonth(defaultDate.getMonth() + 1); // 한 달 후
                    setEndDate(defaultDate.toISOString().slice(0, 16));
                }
                
                if (packageData.description) {
                    setDescription(packageData.description);
                }
                
                if (packageData.imageUrl) {
                    setImagePreview(packageData.imageUrl);
                }
                
                // 상세 설명 가져오기
                if (packageData.descriptionResponseList) {
                    try {
                        const descData = Array.isArray(packageData.descriptionResponseList) ? 
                            packageData.descriptionResponseList : [];
                            
                        // 에디터에 내용 설정
                        if (editorRef.current && descData.length > 0) {
                            editorRef.current.setContentFromJsonArray(descData);
                        }
                        
                        setDescriptionList(descData);
                    } catch (descError) {
                        console.error("상세 설명 처리 중 오류:", descError);
                    }
                }
                
                setLoading(false);
            } catch (err) {
                console.error("패키지 정보 로딩 실패:", err);
                setError("패키지 정보를 불러오는데 실패했습니다. " + (err.response?.data?.message || err.message));
                setLoading(false);
            }
        };
        
        fetchPackageInfo();
    }, [packageId]);

    // 대표 이미지 업로드
    const handleImageUpload = (e) => {
        const file = e.target.files[0];
        if (file) {
            // 파일 크기 체크 (5MB 제한)
            if (file.size > 5 * 1024 * 1024) {
                alert("이미지 크기는 5MB 이하여야 합니다.");
                return;
            }
            
            // 파일 형식 체크
            if (!file.type.match('image.*')) {
                alert("이미지 파일만 업로드 가능합니다.");
                return;
            }
            
            setImage(file);
            setImagePreview(URL.createObjectURL(file));
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

    // 이미지 서버 업로드 함수
    const uploadImageToServer = async (file) => {
        const formData = new FormData();
        formData.append("images", file);
        try {
            const res = await apiClient.post("/product/image/upload", formData, {
                headers: { "Content-Type": "multipart/form-data" },
            });
            console.log("이미지 업로드 응답:", res.data);
            
            // 응답 데이터 확인 및 처리
            if (res.data && res.data.data) {
                // 배열 형태로 반환된 경우
                if (Array.isArray(res.data.data)) {
                    if (res.data.data.length > 0) {
                        // 배열의 첫 번째 항목이 문자열인지 객체인지 확인
                        const firstItem = res.data.data[0];
                        if (typeof firstItem === 'string') {
                            return firstItem; // 문자열 URL 반환
                        } else if (typeof firstItem === 'object' && firstItem !== null) {
                            // 객체 형태인 경우 URL 필드 추출
                            return firstItem.url || firstItem.imageUrl || firstItem.path || firstItem;
                        }
                    }
                    return null; // 빈 배열
                } 
                // 객체 형태로 반환된 경우
                else if (typeof res.data.data === 'object' && res.data.data !== null) {
                    return res.data.data.url || res.data.data.imageUrl || res.data.data.path || res.data.data;
                }
                // 문자열로 반환된 경우
                else if (typeof res.data.data === 'string') {
                    return res.data.data;
                }
            }
            return null;
        } catch (err) {
            console.error("이미지 업로드 실패:", err);
            return null;
        }
    };
    
    // 상세설명 이미지 URL 변환
    const processDescriptionList = async (descriptionList) => {
        const processedList = [];
    
        for (const item of descriptionList) {
            if (
                item.type === "IMAGE" &&
                typeof item.value === "string" &&
                item.value.startsWith("data:")
            ) {
                try {
                    // base64 이미지를 파일로 변환
                    const blob = base64toBlob(item.value);
                    const extension = blob.type.split('/')[1];
                    const file = new File([blob], `image_${Date.now()}.${extension}`, {
                        type: blob.type,
                    });
                    
                    // 서버에 업로드하고 URL 받아오기
                    const url = await uploadImageToServer(file);                    
                    if (url) {
                        processedList.push({ ...item, value: url });
                    } else {
                        console.error("이미지 URL을 받아오지 못했습니다");
                        // 실패한 경우에도 원본 이미지 유지 (백엔드에서 처리할 수 있다면)
                        processedList.push(item);
                    }
                } catch (error) {
                    console.error("이미지 처리 중 오류:", error);
                    // 오류 발생 시 원본 이미지 유지
                    processedList.push(item);
                }
            } else {
                processedList.push(item);
            }
        }
    
        // order 속성 추가
        return processedList.map((item, index) => ({ ...item, order: index }));
    };

    // 등록 버튼 클릭
    const handleSubmit = async () => {
        // 유효성 검사
        if (!discount && discount !== 0) {
            alert("할인율을 입력해주세요.");
            return;
        }
        
        if (!endDate) {
            alert("종료일을 입력해주세요.");
            return;
        }
        
        try {
            setSubmitting(true);
            setError(null);
            
            // 에디터에서 상세 설명 가져오기
            let finalDescriptionList = [];
            if (editorRef.current) {
                const descList = editorRef.current.getContentAsJsonArray();
                finalDescriptionList = await processDescriptionList(descList);
            }
            
            // 대표 이미지 업로드 (있을 경우)
            let imageUrl = imagePreview; // 기존 이미지가 있으면 유지
            if (image) {
                const uploadedImageUrl = await uploadImageToServer(image);
                if (!uploadedImageUrl) {
                    alert("대표 이미지 업로드에 실패했습니다.");
                    setSubmitting(false);
                    return;
                }
                imageUrl = uploadedImageUrl;
            }
            
            // 요청 데이터 준비
            const packageData = {
                name: packageInfo.name,
                discountrate: parseInt(discount),
                endDate: new Date(endDate).toISOString(),
                descriptionRequestList: finalDescriptionList,
                imageUrl: imageUrl
            };
            
            // API 호출 - /package/upload/{packageId} 엔드포인트로 요청
            const response = await apiClient.put(`/package/update/${packageId}`, packageData, {
                headers: { 'Content-Type': 'application/json' }
            });
            
            alert("패키지가 성공적으로 등록되었습니다.");
            
            // 성공 시 패키지 목록 페이지로 이동
            navigate("/company/collaboration", { replace: true });
            
            // 리다이렉트가 즉시 일어나지 않는 경우를 대비해 강제로 페이지 이동
            setTimeout(() => {
                window.location.href = "/company/collaboration";
            }, 100);
        } catch (err) {
            console.error("패키지 등록 실패:", err);
            setError("패키지 등록에 실패했습니다. " + (err.response?.data?.message || err.message));
        } finally {
            setSubmitting(false);
        }
    };

    // 취소 버튼 클릭
    const handleCancel = () => {
        if (confirm("변경 사항이 저장되지 않을 수 있습니다. 정말 취소하시겠습니까?")) {
            navigate("/company/collaboration", { replace: true });
        }
    };

    if (loading) {
        return <div className="w-full p-8 text-center">로딩 중...</div>;
    }
    
    return (
        <div className="w-full max-w-6xl mx-auto p-8 bg-white text-black rounded-md">
            <h1 className="text-2xl font-bold text-[#845EC2] mb-8">패키지 상품 등록하기</h1>
            
            {error && (
                <div className="mb-6 p-4 bg-red-50 border border-red-200 rounded text-red-600">
                    {error}
                </div>
            )}
            
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
                    <div className="mb-6">
                        <h2 className="text-lg font-semibold mb-4">상품정보</h2>
                        {/* 패키지명 (읽기 전용) */}
                        <div className="flex items-center mb-4">
                            <label className="w-24">패키지명</label>
                            <input 
                                type="text" 
                                value={packageInfo.name} 
                                readOnly 
                                className="flex-grow border p-2 rounded bg-gray-100 text-black" 
                            />
                        </div>
                        
                        {/* 포함된 상품 목록 */}
                        <div className="mb-4">
                            <label className="w-full block mb-2">포함된 상품</label>
                            <div className="border rounded p-3 bg-gray-50">
                                {packageInfo && packageInfo.products && packageInfo.products.length > 0 ? (
                                    packageInfo.products.map((product, index) => (
                                        <div key={index} className="flex justify-between py-2 border-b last:border-b-0">
                                            <div>
                                                <span>{product.name || '상품명 없음'}</span>
                                                {product.company && <span className="ml-2 text-gray-500 text-sm">({product.company})</span>}
                                            </div>
                                            <span className="font-medium">{(product.price || 0).toLocaleString()}원</span>
                                        </div>
                                    ))
                                ) : (
                                    <div className="py-2 text-gray-500 text-center">상품이 없습니다.</div>
                                )}
                                <div className="flex justify-between pt-3 font-semibold">
                                    <span>합계</span>
                                    <span>{(packageInfo.totalPrice || 0).toLocaleString()}원</span>
                                </div>
                            </div>
                        </div>
                        
                        {/* 할인율 (수정 가능) */}
                        <div className="flex items-center mb-4">
                            <label className="w-24">할인율(%)</label>
                            <input 
                                type="number" 
                                min="0" 
                                max="100" 
                                value={discount} 
                                onChange={e => setDiscount(e.target.value)} 
                                className="flex-grow border p-2 rounded bg-white text-black" 
                            />
                        </div>
                        {/* 최종 판매가 표시 */}
                        <div className="flex items-center mb-4">
                            <label className="w-24">최종 판매가</label>
                            <div className="flex-grow border p-2 rounded bg-gray-100 text-black">
                                {(packageInfo.totalPrice * (1 - discount / 100)).toLocaleString()}원
                                {discount > 0 && <span className="text-red-500 ml-2">({discount}% 할인)</span>}
                            </div>
                        </div>
                        {/* 종료일 (수정 가능) */}
                        <div className="flex items-center mb-4">
                            <label className="w-24">종료일</label>
                            <input 
                                type="datetime-local" 
                                value={endDate} 
                                onChange={e => setEndDate(e.target.value)} 
                                className="flex-grow border p-2 rounded bg-white text-black" 
                            />
                        </div>
                    </div>
                </div>
            </div>
            
            {/* 상세설명 에디터 */}
            <div className="mb-6">
                <h2 className="text-lg font-semibold mb-4">상세설명</h2>
                <div className="border rounded p-4 bg-white">
                    <AddWrite ref={editorRef} />
                </div>
            </div>
            
            {/* 등록/취소 버튼 */}
            <div className="flex justify-end space-x-4 mt-8">
                <button 
                    onClick={handleSubmit} 
                    disabled={submitting}
                    className={`${submitting ? 'bg-purple-300' : 'bg-[#845EC2] hover:bg-purple-500'} text-white px-8 py-3 rounded`}
                >
                    {submitting ? '처리 중...' : '등록'}
                </button>
                <button 
                    className="bg-gray-300 text-black px-8 py-3 rounded hover:bg-gray-400" 
                    onClick={handleCancel}
                    disabled={submitting}
                >
                    취소
                </button>
            </div>
        </div>
    );
}

export default Setpackage; 