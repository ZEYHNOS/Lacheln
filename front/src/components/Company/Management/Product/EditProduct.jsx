import React, { useEffect, useRef, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { Star } from "lucide-react";
import axios from "axios";
import apiClient from "../../../../lib/apiClient";
import AddWrite from "../../../Tool/WriteForm/AddWrite.jsx";
import Addphoto from "../../../../image/Company/addimage.png";
import { COLOR_MAP } from "../../../../constants/colorMap.js";

const baseUrl = import.meta.env.VITE_API_BASE_URL;
const imageBaseUrl = `${baseUrl}`;

const checkboxStyle = "w-5 h-5 rounded border-2 border-[#845EC2] appearance-none cursor-pointer " +
    "bg-white checked:bg-[#845EC2] checked:border-[#845EC2] " +
    "checked:after:content-['✓'] checked:after:text-white checked:after:text-sm " +
    "checked:after:font-bold checked:after:block checked:after:text-center " +
    "checked:after:leading-[18px]";


function EditProduct() {
    const { id } = useParams();
    const navigate = useNavigate();
    const writeRef = useRef();

    const [name, setName] = useState("");
    const [price, setPrice] = useState("");
    const [color, setColor] = useState("white");
    const [status, setStatus] = useState("ACTIVE");
    const [rec, setRec] = useState(false);
    const [task_time, settask_time] = useState("60");
    const [indoor, setIndoor] = useState(false);
    const [outdoor, setOutdoor] = useState(false);
    const [existingImages, setExistingImages] = useState([]);
    const [newImages, setNewImages] = useState([]);
    const [selectedImage, setSelectedImage] = useState(null);
    const [isDragging, setIsDragging] = useState(false);
    const [options, setOptions] = useState([]);
    const [categoryCode, setCategoryCode] = useState(null);
    const [sizeList, setsizeList] = useState([]);
    const [overlap, setOverlap] = useState([]);
    const [essential, setEssential] = useState([]);

    useEffect(() => {
        apiClient.get("/company/category")
            .then(res => {
                setCategoryCode(res.data);  
            })
            .catch(err => console.error("카테고리 불러오기 실패", err));
    }, []);

    // 이미지 업로드 핸들러
    const handleImageUpload = (e) => {
        e.preventDefault();
        const files = e.dataTransfer?.files || e.target.files;
        if (!files) return;

        const fileArray = Array.from(files);

        // 파일 이름과 사이즈로 중복 체크
        const existingSignatures = new Set(
            newImages.map(img => `${img.file.name}-${img.file.size}`)
        );

        const filteredFiles = fileArray.filter(
            file => !existingSignatures.has(`${file.name}-${file.size}`)
        );

        const newImageObjects = filteredFiles.map((file) => ({
            file,
            url: URL.createObjectURL(file),
            isNew: true,
        }));
        setNewImages(prev => [...prev, ...newImageObjects]);
        
        if (!selectedImage && newImageObjects.length > 0) {
            setSelectedImage(newImageObjects[0].url);
        }

        setIsDragging(false);
    };
    

    const handleChangeStatus = (e) => {
        setStatus(e.target.value);
    };

    // 백엔드에서 정보 받아옴 
    useEffect(() => {
        if (!categoryCode || !id) return;
    
        const categoryMap = {
            S: "studio",
            D: "dress",
            M: "makeup"
        };
    
        const category = categoryMap[categoryCode];
        if (!category) {
            console.error("잘못된 categoryCode:", categoryCode);
            return;
        }
    
        apiClient.get(`/product/${category}/${id}`)
            .then(res => {
                const data = res.data.data;
                setName(data.name);
                setPrice(data.price);
                setColor(data.color);
                setStatus(data.status);
                setRec(data.rec === "Y");
                settask_time(String(data.taskTime));
                setIndoor(data.inAvailable === "Y");
                setOutdoor(data.outAvailable === "Y");
                setExistingImages(data.productImageUrl.map(img => ({
                    id: img.id,
                    url: `${imageBaseUrl}${img.url}`,
                })));
                setSelectedImage(data.productImageUrl?.[0]?.url ? `${imageBaseUrl}${data.productImageUrl[0].url}` : null);                
                if(categoryCode === "D") {
                    setEssential(data.essential === "Y");
                    setOverlap(data.overlap === "Y");
                }
                setOptions(
                    (data.optionList || []).map(opt => ({
                        title: opt.name,
                        isMultiSelect: opt.overlap === "Y",
                        isRequired: opt.essential === "Y",
                        details: (opt.optionDtList || []).map(dt => ({
                            name: dt.opDtName,
                            stock: dt.stock,
                            extraTime: dt.plusTime,
                            extraPrice: dt.plusCost,
                        })),
                    }))
                );
                console.log("상품 상세 응답:", data);
                setsizeList(data.sizeList || []);
                if (writeRef.current && data.descriptionList) {
                    writeRef.current.setContentFromJsonArray?.(data.descriptionList);
                }
            })
            .catch(err => console.error("상품 상세 정보 불러오기 실패", err));
    }, [categoryCode, id]);

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

    // 상세설명 이미지 업로드
    const uploadImageToServer = async (file) => {
        const formData = new FormData();
        formData.append("images", file);
        try {
            const res = await apiClient.post("/product/image/upload", formData, {
                headers: { "Content-Type": "multipart/form-data" },
            });
            return res.data.data[0];
        } catch (err) {
            console.error("이미지 업로드 실패", err);
            return null;
        }
    };

    // 상세설명 이미지 URL변환
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
                const url = await uploadImageToServer(file); // ✅ 이제 정상 작동
                if (url) {
                    processedList.push({ ...item, value: url });
                }
            } else {
                processedList.push(item);
            }
        }
    
        return processedList.map((item, index) => ({ ...item, order: index }));
    };

    // 실제 백엔드로 보내는 주소
    const handleSubmit = async () => {
        const categoryMap = {
            S: "studio",
            D: "dress",
            M: "makeup"
        };
        
        // 카테고리 확인(서버 보내는 용도도)
        const category = categoryMap[categoryCode];
        if (!category) {
            console.error("잘못된 categoryCode:", categoryCode);
            return;
        }
    
        // 새로 업로드한 이미지들만 서버에 업로드
        const uploadedUrls = await Promise.all(
            newImages.map(async (img) => {
                const uploadedUrl = await uploadImageToServer(img.file);
                return uploadedUrl ?? null;
            })
        );
    
        // 이미지 리스트에는 새로운 이미지 주소만 포함함
        const image_url_list = uploadedUrls.filter(Boolean).map(url => url.replace(imageBaseUrl, ""));
    
        // 상세 설명 처리
        const descriptionList = writeRef.current?.getContentAsJsonArray?.() || [];
        const processedDescriptionList = await processDescriptionList(descriptionList);
    
        // 최종 payload
        const payload = {
            id,
            name,
            price: parseInt(price || 0),
            color,
            status,
            rec: rec ? "Y" : "N",
            task_time: parseInt(task_time),
            in_available: indoor ? "Y" : "N",
            out_available: outdoor ? "Y" : "N",
            image_url_list,
            category_code: categoryCode,
            descriptionList: processedDescriptionList,
            option_list: options.map(opt => ({
                name: opt.title,
                overlap: opt.isMultiSelect ? "Y" : "N",
                essential: opt.isRequired ? "Y" : "N",
                status: "ACTIVE",
                option_dt_list: opt.details.map(dt => ({
                    op_dt_name: dt.name,
                    stock: dt.stock || 0,
                    plus_time: parseInt(dt.extraTime || 0),
                    plus_cost: parseInt(dt.extraPrice || 0),
                })),
            })),
            ...(categoryCode?.toUpperCase() === "D" && { 
                sizeList, 
                essential: essential ? "Y" : "N",
                overlap: overlap ? "Y" : "N"
            }),
        };
    
        apiClient.put(`${baseUrl}/product/${category}/update/${id}`, payload)
            .then(() => {
                alert("수정 완료!");
                navigate(`/company/product/${id}`);
            })
            .catch(err => {
                console.error("수정 실패", err);
                console.log("payload:", payload);
                alert("수정 중 오류가 발생했습니다.");
            });
    };

    const combinedImages = [...existingImages, ...newImages];

    return (
        <div className="w-full max-w-6xl mx-auto p-6 bg-white text-black rounded-md">
            <h1 className="text-2xl font-bold text-[#845EC2] mb-4">상품 수정하기</h1>

            <div className="flex items-start space-x-6">
                <div className="w-1/3 p-2">
                    <div className="border rounded-lg overflow-hidden bg-gray-100 w-full h-96">
                        {selectedImage ? (
                            <img src={selectedImage} alt="대표 이미지" className="w-full h-full object-cover" />
                        ) : (
                            <div className="w-full h-full flex items-center justify-center text-gray-400">
                                No image
                            </div>
                        )}
                    </div>

                    <div className="mt-4 flex space-x-2 overflow-x-auto max-w-full p-1 custom-scrollbar">
                    {combinedImages.map((img, index) => (
                        <img
                            key={index}
                            src={img.url}
                            alt={`업로드된 이미지 ${index}`}
                            className={`w-16 h-16 object-cover rounded-md cursor-pointer border-2 ${selectedImage === img.url ? "border-[#845EC2]" : "border-transparent"}`}
                            onClick={() => setSelectedImage(img.url)}
                        />
                    ))}
                    </div>

                    <label 
                        className={`mt-2 block w-full text-center border ${
                            isDragging ? "border-dashed border-2 border-[#845EC2]" : "border border-[#845EC2]"
                        } text-[#845EC2] rounded-md py-4 cursor-pointer bg-white flex items-center justify-center space-x-2 w-full h-16`}
                        onDragOver={(e) => {
                            e.preventDefault();
                            setIsDragging(true);
                        }}
                        onDragLeave={() => setIsDragging(false)}
                        onDrop={handleImageUpload}
                    >
                        <img src={Addphoto} alt="이미지 추가" className="w-6 h-6" />
                        <span className="text-[#845EC2] font-medium">이미지 추가하기</span>
                        <input type="file" multiple accept="image/*" className="hidden" onChange={handleImageUpload} />
                    </label>
                    
                    {/* 실내 혹은 야외 촬영 여부 선택 */}
                    <div className="bg-white rounded-md flex items-center space-x-6 mt-4">
                        <label className="flex items-center space-x-2 cursor-pointer">
                            <input 
                                type="checkbox" 
                                checked={indoor} 
                                onChange={() => setIndoor(!indoor)} 
                                className="w-5 h-5 rounded border-2 border-[#845EC2] appearance-none cursor-pointer
                                                    bg-white checked:bg-[#845EC2] checked:border-[#845EC2]
                                                    checked:after:content-['✓'] checked:after:text-white checked:after:text-sm
                                                    checked:after:font-bold checked:after:block checked:after:text-center
                                                    checked:after:leading-[18px]"
                            />
                            <span className="text-black">실내촬영가능</span>
                        </label>

                        <label className="flex items-center space-x-2 cursor-pointer">
                            <input 
                                type="checkbox" 
                                checked={outdoor} 
                                onChange={() => setOutdoor(!outdoor)} 
                                className="w-5 h-5 rounded border-2 border-[#845EC2] appearance-none cursor-pointer
                                                    bg-white checked:bg-[#845EC2] checked:border-[#845EC2]
                                                    checked:after:content-['✓'] checked:after:text-white checked:after:text-sm
                                                    checked:after:font-bold checked:after:block checked:after:text-center
                                                    checked:after:leading-[18px]"
                            />
                            <span className="text-black">야외촬영가능</span>
                        </label>
                    </div>
                </div>

                <div className="w-2/3 p-2">
                    <div>
                        <div className="flex items-center justify-between">
                            <div className="flex items-center space-x-2">
                                <h2 className="text-lg font-semibold">상품정보</h2>
                                {/* 사장님 추천상품 */}
                                <Star className={`w-6 h-6 cursor-pointer transition-colors duration-300 ${
                                    rec ? "text-purple-600 fill-current" : "text-purple-400"}`}
                                    fill={rec ? "currentColor" : "none"}
                                    onClick={() => setRec(!rec)}/>
                            </div>
                            {/* 상태 설정 */}
                            <select onChange={handleChangeStatus} className="border p-1 rounded bg-white text-black">
                                <option value="ACTIVE">공개</option>
                                <option value="INACTIVE">비공개</option>
                                <option value="PACKAGE">패키지전용</option>
                            </select>
                        </div>

                        <div className="mt-3 space-y-2">
                            {/* 상품명 */}
                            <div className="flex items-center">
                                <label className="w-24">상품명</label>
                                <input type="text" value={name} onChange={(e) => setName(e.target.value)}
                                    className="flex-grow border p-2 rounded bg-white text-black"/>
                            </div>
                            {/* 가격 */}
                            <div className="flex items-center">
                                <label className="w-24">가격</label>
                                <input type="text" value={price} onChange={(e) => setPrice(e.target.value)}
                                    className="flex-grow border p-2 rounded bg-white text-black"/>
                            </div>
                            {/* 색상 */}
                            <div className="flex items-center">
                                <label className="w-24">색상</label>
                                <select
                                    value={color}
                                    onChange={(e) => setColor(e.target.value)}
                                    className="flex-grow border p-2 rounded bg-white text-black"
                                >
                                    {Object.entries(COLOR_MAP).map(([eng, kor]) => (
                                        <option key={eng} value={eng}>
                                            {kor}
                                        </option>
                                    ))}
                                </select>
                                <div className="ml-2 w-24 h-10 rounded" 
                                    style={{ backgroundColor: color, border: '1px solid #ccc'}}/>
                            </div>
                            {/* 대여시간 */}
                            <div className="flex items-center">
                                <label className="w-24">대여시간</label>
                                <select value={task_time} onChange={(e) => settask_time(e.target.value)}
                                    className="border p-2 rounded flex-grow bg-white text-black">
                                    <option value="30">30분 대여</option>
                                    <option value="60">1시간 대여</option>
                                    <option value="90">1시간 30분 대여</option>
                                    <option value="120">2시간 대여</option>
                                    <option value="150">2시간 30분 대여</option>
                                    <option value="180">3시간 대여</option>
                                </select>
                            </div>

                            {/* 드레스 사이즈 리스트 보여주기 */}
                            {categoryCode === "D" && sizeList.length > 0 && (
                                <div className="mt-4 border rounded p-4 bg-white">
                                    <h3 className="text-md font-semibold mb-2">드레스 사이즈 리스트</h3>
                                    <div className="flex items-center space-x-4">
                                        <label className="flex items-center space-x-1">
                                            <input type="checkbox" checked={overlap} className="w-5 h-5 rounded border-2 border-[#845EC2] appearance-none cursor-pointer
                                                bg-white checked:bg-[#845EC2] checked:border-[#845EC2]
                                                checked:after:content-['✓'] checked:after:text-white checked:after:text-sm
                                                checked:after:font-bold checked:after:block checked:after:text-center
                                                checked:after:leading-[18px]" />
                                            <span>중복 선택</span>
                                        </label>
                                        <label className="flex items-center space-x-1">
                                            <input type="checkbox" checked={essential} className="w-5 h-5 rounded border-2 border-[#845EC2] appearance-none cursor-pointer
                                                bg-white checked:bg-[#845EC2] checked:border-[#845EC2]
                                                checked:after:content-['✓'] checked:after:text-white checked:after:text-sm
                                                checked:after:font-bold checked:after:block checked:after:text-center
                                                checked:after:leading-[18px]" />
                                            <span>필수 선택</span>
                                        </label>
                                    </div>
                                    {sizeList.map((sizeItem, index) => (
                                        <div key={index} className="flex space-x-2 mb-2">
                                            <input
                                                type="text"
                                                className="border p-2 rounded bg-white text-black"
                                                value={`사이즈: ${sizeItem.size}`}
                                                readOnly
                                            />
                                            <input
                                                type="number"
                                                className="border p-2 rounded bg-white text-black"
                                                value={sizeItem.stock}
                                                onChange={(e) => {
                                                    const updated = [...sizeList];
                                                    updated[index].stock = parseInt(e.target.value) || 0;
                                                    setsizeList(updated);
                                                }}
                                                placeholder="재고"
                                            />
                                            <input
                                                type="number"
                                                className="border p-2 rounded bg-white text-black"
                                                value={sizeItem.plusCost}
                                                onChange={(e) => {
                                                    const updated = [...sizeList];
                                                    updated[index].plusCost = parseInt(e.target.value) || 0;
                                                    setsizeList(updated);
                                                }}
                                                placeholder="추가금액"
                                            />
                                        </div>
                                    ))}
                                </div>
                            )}

                            {/* 옵션 목록 전체 렌더링 */}
                            {options.map((option, optIdx) => {
                                const isDressSize = categoryCode?.toUpperCase() === "D" && option.title.trim().toLowerCase() === "사이즈";

                                return (
                                <div key={optIdx} className="border rounded p-4 mt-4 bg-white">
                                    {/* 옵션 제목과 설정 */}
                                    <div className="flex justify-between items-center mb-2">
                                        <input
                                            type="text"
                                            placeholder="옵션 제목"
                                            className="border p-2 rounded bg-white text-black"
                                            value={option.title}
                                            onChange={(e) => {
                                                const newOptions = [...options];
                                                newOptions[optIdx].title = e.target.value;
                                                setOptions(newOptions);
                                            }}
                                        />
                                        <div className="flex items-center space-x-4">
                                            {/* 중복 선택 */}
                                            <label className="flex items-center space-x-1">
                                                <input
                                                    type="checkbox"
                                                    checked={option.isMultiSelect}
                                                    onChange={(e) => {
                                                        const newOptions = [...options];
                                                        newOptions[optIdx].isMultiSelect = e.target.checked;
                                                        setOptions(newOptions);
                                                    }}
                                                    className={checkboxStyle}
                                                />
                                                <span>중복 선택</span>
                                            </label>
                                            {/* 필수 선택 */}
                                            <label className="flex items-center space-x-1">
                                                <input
                                                    type="checkbox"
                                                    checked={option.isRequired}
                                                    onChange={(e) => {
                                                        const newOptions = [...options];
                                                        newOptions[optIdx].isRequired = e.target.checked;
                                                        setOptions(newOptions);
                                                    }}
                                                    className={checkboxStyle}
                                                />
                                                <span>필수 선택</span>
                                            </label>
                                        </div>
                                    </div>

                                    {/* 옵션 상세 항목 반복 */}
                                    {option.details.map((detail, detailIdx) => (
                                        <div key={detailIdx} className="flex space-x-2 mb-2 bg-white p-2 rounded">
                                            <input
                                                type="text"
                                                placeholder="옵션명"
                                                value={detail.name}
                                                onChange={(e) => {
                                                    const newOptions = [...options];
                                                    newOptions[optIdx].details[detailIdx].name = e.target.value;
                                                    setOptions(newOptions);
                                                }}
                                                className="border p-2 rounded bg-white text-black"
                                            />

                                            {isDressSize ? (
                                                <input
                                                    type="number"
                                                    placeholder="재고"
                                                    value={detail.stock}
                                                    onChange={(e) => {
                                                        const newOptions = [...options];
                                                        newOptions[optIdx].details[detailIdx].stock = parseInt(e.target.value) || 0;
                                                        setOptions(newOptions);
                                                    }}
                                                    className="border p-2 rounded bg-white text-black"
                                                />
                                            ) : (
                                                <input
                                                    type="number"
                                                    placeholder="추가시간 (분단위)"
                                                    value={detail.extraTime || ""}
                                                    onChange={(e) => {
                                                        const newOptions = [...options];
                                                        newOptions[optIdx].details[detailIdx].extraTime = parseInt(e.target.value) || 0;
                                                        setOptions(newOptions);
                                                    }}
                                                    className="border p-2 rounded bg-white text-black"
                                                />
                                            )}

                                            <input
                                                type="number"
                                                placeholder="추가금 (원)"
                                                value={detail.extraPrice || ""}
                                                onChange={(e) => {
                                                    const newOptions = [...options];
                                                    newOptions[optIdx].details[detailIdx].extraPrice = parseInt(e.target.value) || 0;
                                                    setOptions(newOptions);
                                                }}
                                                className="border p-2 rounded bg-white text-black"
                                            />

                                            <button
                                                onClick={() => {
                                                    const newOptions = [...options];
                                                    newOptions[optIdx].details.splice(detailIdx, 1);
                                                    setOptions(newOptions);
                                                }}
                                                className="bg-white text-red-500 font-bold text-xl"
                                            >
                                                -
                                            </button>
                                        </div>
                                    ))}

                                    {/* 상세 옵션 추가 버튼 */}
                                    <button
                                        onClick={() => {
                                            const newOptions = [...options];
                                            newOptions[optIdx].details.push({
                                                name: "",
                                                stock: isDressSize ? 0 : undefined,
                                                extraTime: isDressSize ? undefined : 0,
                                                extraPrice: 0,
                                            });
                                            setOptions(newOptions);
                                        }}
                                        className="bg-white text-xl text-blue-600"
                                    >
                                        +
                                    </button>
                                </div>
                                );
                            })}

                            {/* 옵션 전체 추가 버튼 (옵션 map 바깥!) */}
                            <button
                                onClick={() => {
                                    const newOptions = [...options];
                                    newOptions.push({
                                        title: "",
                                        isMultiSelect: false,
                                        isRequired: false,
                                        details: [{ name: "", stock: 0, extraTime: 0, extraPrice: 0 }]
                                    });
                                    setOptions(newOptions);
                                }}
                                className="border px-72 py-4 text-[#845EC2] rounded mt-6 bg-white"
                            >
                                + 옵션 추가하기
                            </button>
                        </div>
                    </div>
                </div>
            </div>

            <AddWrite ref={writeRef} onImageUpload={uploadImageToServer} />

            {/* 하단 버튼 */}
            <div className="flex justify-end mt-6">
                <button
                    onClick={() => {
                        if (!categoryCode) {
                            alert("카테고리를 아직 불러오지 못했습니다.");
                            return;
                        }
                    
                        if (window.confirm("정말 삭제하시겠습니까?")) {
                            const categoryMap = {
                                S: "studio",
                                D: "dress",
                                M: "makeup"
                            };
                            const category = categoryMap[categoryCode];
                    
                            if (!category) {
                                alert("알 수 없는 카테고리입니다.");
                                return;
                            }
                    
                            axios.delete(`${baseUrl}/product/${category}/delete/${id}`)
                                .then(() => {
                                    alert("삭제 완료");
                                    navigate("/company/product");
                                })
                                .catch((err) => console.error("삭제 실패", err));
                        }
                    }}
                    className="bg-red-500 text-white px-6 py-2 rounded hover:bg-red-600 mr-4"
                >
                    삭제하기
                </button>
                <button
                    onClick={handleSubmit}
                    className="bg-[#845EC2] text-white px-6 py-2 rounded hover:bg-purple-500"
                >
                    수정하기
                </button>
                <button
                    className="ml-4 bg-gray-300 text-black px-6 py-2 rounded"
                    onClick={() => navigate(-1)}
                >
                    취소
                </button>
            </div>
        </div>
    );
};
export default EditProduct;
