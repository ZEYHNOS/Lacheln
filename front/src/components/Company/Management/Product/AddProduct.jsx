import React, { useRef, useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import Addphoto from "../../../../image/Company/addimage.png";
import { Star } from "lucide-react";
import axios from "axios";
import apiClient from "../../../../lib/apiClient";
import { COLOR_MAP } from "../../../../constants/colorMap.js";
import AddWrite from "../../../Tool/WriteForm/AddWrite.jsx";

const baseUrl = import.meta.env.VITE_API_BASE_URL;

function AddProduct() {
    const navigate = useNavigate();
    const [rec, setRec] = useState(false);
    const [name, setName] = useState("");
    const [color, setColor] = useState("WHITE");
    const [price, setPrice] = useState("");
    const [taskTime, settaskTime] = useState("02:00:00");
    const [indoor, setIndoor] = useState(true);
    const [outdoor, setOutdoor] = useState(false);
    const [images, setImages] = useState([]);
    const [isDragging, setIsDragging] = useState(false);
    const [selectedImage, setSelectedImage] = useState(images.length > 0 ? images[0] : null);
    const [options, setOptions] = useState([]);
    const [categoryCode, setCategoryCode] = useState("");
    const writeRef = useRef();

    // 스튜디오 전용 필드
    const [maxPeople, setMaxPeople] = useState(1);
    const [backgroundOption, setBackgroundOption] = useState("Y");

    // 메이크업 전용 필드
    const [visitAvailable, setVisitAvailable] = useState(true);
    const [makeupVisit, setMakeupVisit] = useState(true);
    const [manager, setManager] = useState("");

    useEffect(() => {
        // 업체 카테고리 불러오기
        axios.get(`${baseUrl}/company/category`,{
            withCredentials: true
        })
            .then(res => {
                const category = res.data;
                setCategoryCode(category);

                // 드레스면 사이즈옵션을 자동 추가
                if (category  === "D") {
                    const sizeList = [
                        { size: "S"},
                        { size: "M"},
                        { size: "L"}
                    ];
                    const sizeOption = {
                        title: "사이즈",
                        isRequired: false,
                        isMultiSelect: false,
                        details: sizeList.map(s => ({
                            name: s.size,
                            stock: s.stock,
                            extraPrice: s.plus_cost,
                            extraTime: ""
                        }))
                    };
                    setOptions([sizeOption]);
                }
            })
            .catch(err => console.error("카테고리 불러오기 실패", err));
    }, []);

    // 옵션 추가
    const handleAddOption = () => {
        setOptions([
            ...options,
            {
                title: "",
                isRequired: false,
                isMultiSelect: false,
                details: [{ name: "", stock: "", extraTime: "", extraPrice: "" }]
            }
        ]);
    };

    // 대표 이미지 업로드 
    const handleImageUpload = (event) => {
        event.preventDefault();
        let files = [];
    
        if (event.dataTransfer) {
            files = Array.from(event.dataTransfer.files);
        } else if (event.target.files) {
            files = Array.from(event.target.files);
        }
    
        const newImageList = files.map(file => ({
            file,
            previewUrl: URL.createObjectURL(file), 
        }));
    
        setImages(prev => [...prev, ...newImageList]);
    
        if (!selectedImage && newImageList.length > 0) {
            setSelectedImage(newImageList[0].previewUrl);
        }
    
        setIsDragging(false);
    };

    //이미지 처리(formdata 파일을 백엔드로 보내야함) 
    const uploadImages = async () => {
        const formData = new FormData();
        images.forEach((img) => formData.append("images", img.file)); 
        try {
            const res = await apiClient.post("/product/image/upload", formData, {
                headers: { "Content-Type": "multipart/form-data" }
            });
            console.log("🟢 업로드 응답:", res.data);
            const urls = res.data.data;
            return urls;
        } catch (err) {
            console.error("이미지 업로드 실패", err);
            return [];
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

    // 백엔드 전송 코드 
    const handleSubmit = async () => {
        const imageUrls = await uploadImages();
        const descriptionArray = writeRef.current?.getContentAsJsonArray();
        const processedDescriptionList = await processDescriptionList(descriptionArray);

        const data = {
            name,
            price: parseInt(price || 0),
            rec: rec ? "Y" : "N",
            taskTime: taskTime,
            imageUrlList: imageUrls,
            hashTagList: [],
            descriptionList: processedDescriptionList,
        };

        // 카테고리별 추가 필드
        if (categoryCode === "D") {
            // 드레스 전용 필드
            data.inAvailable = indoor ? "Y" : "N";
            data.outAvailable = outdoor ? "Y" : "N";
            data.color = color;
            data.overlap = options[0]?.isMultiSelect ? "Y" : "N";
            data.essential = options[0]?.isRequired ? "Y" : "N";
            data.sizeList = options[0].details.map((d) => ({
                size: d.name,
                stock: parseInt(d.stock || 0),
                plusCost: parseInt(d.extraPrice || 0),
            }));
            data.optionList = options.slice(1).map((opt) => ({
                name: opt.title,
                overlap: opt.isMultiSelect ? "Y" : "N",
                essential: opt.isRequired ? "Y" : "N",
                status: "ACTIVE",
                optionDtList: opt.details.map((dt) => {
                    const minutes = parseInt(dt.extraTime || 0);
                    const hours = Math.floor(minutes / 60);
                    const remainingMinutes = minutes % 60;
                    const extraTime = `${hours.toString().padStart(2, '0')}:${remainingMinutes.toString().padStart(2, '0')}:00`;
                    
                    return {
                        opDtName: dt.name,
                        plusCost: parseInt(dt.extraPrice || 0),
                        plusTime: extraTime,
                    };
                }),
            }));
        } else if (categoryCode === "S") {
            // 스튜디오 전용 필드
            data.inAvailable = indoor ? "Y" : "N";
            data.outAvailable = outdoor ? "Y" : "N";
            data.maxPeople = parseInt(maxPeople);
            data.bgOptions = backgroundOption;
            data.optionList = options.map((opt) => ({
                name: opt.title,
                overlap: opt.isMultiSelect ? "Y" : "N",
                essential: opt.isRequired ? "Y" : "N",
                status: "ACTIVE",
                optionDtList: opt.details.map((dt) => {
                    const minutes = parseInt(dt.extraTime || 0);
                    const hours = Math.floor(minutes / 60);
                    const remainingMinutes = minutes % 60;
                    const extraTime = `${hours.toString().padStart(2, '0')}:${remainingMinutes.toString().padStart(2, '0')}:00`;
                    
                    return {
                        opDtName: dt.name,
                        plusCost: parseInt(dt.extraPrice || 0),
                        plusTime: extraTime,
                    };
                }),
            }));
        } else if (categoryCode === "M") {
            // 메이크업 전용 필드
            data.business_trip = visitAvailable ? "Y" : "N";
            data.visit = makeupVisit ? "Y" : "N";
            data.manager = manager;
            data.optionList = options.map((opt) => ({
                name: opt.title,
                overlap: opt.isMultiSelect ? "Y" : "N",
                essential: opt.isRequired ? "Y" : "N",
                status: "ACTIVE",
                optionDtList: opt.details.map((dt) => {
                    const minutes = parseInt(dt.extraTime || 0);
                    const hours = Math.floor(minutes / 60);
                    const remainingMinutes = minutes % 60;
                    const extraTime = `${hours.toString().padStart(2, '0')}:${remainingMinutes.toString().padStart(2, '0')}:00`;
                    
                    return {
                        opDtName: dt.name,
                        plusCost: parseInt(dt.extraPrice || 0),
                        plusTime: extraTime,
                    };
                }),
            }));
        }

        console.log("🟨 최종 전송 데이터:", data);
    
        let postUrl = "";
        if (categoryCode === "D") {
            postUrl = "/product/dress/register";
        } else if (categoryCode === "S") {
            postUrl = "/product/studio/register";
        } else if (categoryCode === "M") {
            postUrl = "/product/makeup/register";
        }
    
        try {
            const res = await apiClient.post(postUrl, data);
            console.log("등록 성공", res.data);
            navigate("/company/product");
        } catch (err) {
            if (err.response) {
                console.error("등록 실패", err.response.data);
            } else {
                console.error("등록 실패", err);
            }
        }
    };


    return (
        <div className="w-full max-w-6xl mx-auto p-6 bg-white text-black rounded-md">
            <h1 className="text-2xl font-bold text-[#845EC2] mb-4">상품 추가하기</h1>

            <div className="flex items-start space-x-6">
                <div className="w-1/3 p-2">
                    {/* 대표 이미지 */}
                    <div className="border rounded-lg overflow-hidden bg-gray-100 w-full h-96 aspect-[3/4]">
                        {selectedImage ? (
                            <img src={selectedImage} alt="대표 이미지" className="w-full h-full object-cover" />
                        ) : (
                            <div className="w-full h-full flex items-center justify-center text-gray-400">
                                No image
                            </div>
                        )}
                    </div>
                    {/* 이미지 미리보기 */}
                    <div className="mt-4 flex space-x-2 overflow-x-auto max-w-full p-1 custom-scrollbar">
                        {images.map((img, index) => (
                            <img 
                            key={index}
                            src={img.previewUrl}
                            alt={`업로드된 이미지 ${index}`} 
                            className="w-16 h-16 object-cover rounded-md cursor-pointer border-2 border-transparent hover:border-[#845EC2] flex-shrink-0" 
                            onClick={() => setSelectedImage(img.previewUrl)}
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
                    
                    {/* 카테고리별 추가 옵션 */}
                    {categoryCode === "D" && (
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
                    )}

                    {categoryCode === "S" && (
                        <div className="bg-white rounded-md mt-4 space-y-4">
                            <div className="flex items-center space-x-6">
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
                    )}

                    {categoryCode === "M" && (
                        <div className="bg-white rounded-md mt-4 space-y-4">
                            <div className="flex items-center space-x-6">
                                <label className="flex items-center space-x-2 cursor-pointer">
                                    <input 
                                        type="checkbox" 
                                        checked={visitAvailable} 
                                        onChange={() => setVisitAvailable(!visitAvailable)} 
                                        className="w-5 h-5 rounded border-2 border-[#845EC2] appearance-none cursor-pointer
                                                            bg-white checked:bg-[#845EC2] checked:border-[#845EC2]
                                                            checked:after:content-['✓'] checked:after:text-white checked:after:text-sm
                                                            checked:after:font-bold checked:after:block checked:after:text-center
                                                            checked:after:leading-[18px]"
                                    />
                                    <span className="text-black">출장</span>
                                </label>
                                <label className="flex items-center space-x-2 cursor-pointer">
                                    <input 
                                        type="checkbox" 
                                        checked={makeupVisit} 
                                        onChange={() => setMakeupVisit(!makeupVisit)} 
                                        className="w-5 h-5 rounded border-2 border-[#845EC2] appearance-none cursor-pointer
                                                            bg-white checked:bg-[#845EC2] checked:border-[#845EC2]
                                                            checked:after:content-['✓'] checked:after:text-white checked:after:text-sm
                                                            checked:after:font-bold checked:after:block checked:after:text-center
                                                            checked:after:leading-[18px]"
                                    />
                                    <span className="text-black">방문</span>
                                </label>
                            </div>
                        </div>
                    )}
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
                            {/* 색상 - 드레스일 때만 표시 */}
                            {categoryCode === "D" && (
                                <div className="flex items-center">
                                    <label className="w-24">색상</label>
                                    <select
                                        value={color}
                                        onChange={(e) => setColor(e.target.value)}
                                        className="flex-grow border p-2 rounded bg-white text-black"
                                    >
                                        {Object.entries(COLOR_MAP).map(([eng, kor]) => (
                                            <option key={eng} value={eng}>{kor}</option>
                                        ))}
                                    </select>
                                    <div className="ml-2 w-24 h-10 rounded" 
                                        style={{ backgroundColor: color, border: '1px solid #ccc'}}/>
                                </div>
                            )}
                            {/* 대여시간 */}
                            <div className="flex items-center">
                                <label className="w-24">대여시간</label>
                                <select value={taskTime} onChange={(e) => settaskTime(e.target.value)}
                                    className="border p-2 rounded flex-grow bg-white text-black">
                                    <option value="00:30:00">30분 대여</option>
                                    <option value="01:00:00">1시간 대여</option>
                                    <option value="01:30:00">1시간 30분 대여</option>
                                    <option value="02:00:00">2시간 대여</option>
                                    <option value="02:30:00">2시간 30분 대여</option>
                                    <option value="03:00:00">3시간 대여</option>
                                </select>
                            </div>
                            {/* 메이크업 담당자 - 대여시간 아래로 이동 */}
                            {categoryCode === "M" && (
                                <div className="flex items-center mt-2">
                                    <label className="w-24">담당자</label>
                                    <input
                                        type="text"
                                        maxLength="10"
                                        value={manager}
                                        onChange={(e) => setManager(e.target.value)}
                                        className="flex-grow border p-2 rounded bg-white text-black"
                                        placeholder="담당자 이름"
                                    />
                                </div>
                            )}
                            {categoryCode === "S" && (
                                <div className="bg-white rounded-md mt-4 space-y-4">
                                    <div className="flex items-center">
                                        <label className="w-32">최대수용인원</label>
                                        <input 
                                            type="number" 
                                            min="1"
                                            value={maxPeople} 
                                            onChange={(e) => setMaxPeople(e.target.value)}
                                            className="flex-grow border p-2 rounded bg-white text-black"
                                        />
                                    </div>
                                    <div className="flex items-center">
                                        <label className="w-32">배경선택여부</label>
                                        <select
                                            value={backgroundOption}
                                            onChange={(e) => setBackgroundOption(e.target.value)}
                                            className="flex-grow border p-2 rounded bg-white text-black"
                                        >
                                            <option value="Y">가능</option>
                                            <option value="N">불가능</option>
                                        </select>
                                    </div>
                                </div>
                            )}

                            {/* 옵션 추가하기 */}
                            {options.map((option, optIdx) => (
                            <div key={optIdx} className="border rounded p-4 mt-4 bg-white">
                                {/* 옵션 제목 및 옵션 삭제 버튼 */}
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
                                        <label className="flex items-center space-x-1">
                                            <input
                                                type="checkbox"
                                                checked={option.isMultiSelect}
                                                onChange={(e) => {
                                                    const newOptions = [...options];
                                                    newOptions[optIdx].isMultiSelect = e.target.checked;
                                                    setOptions(newOptions);
                                                }}
                                                className="w-5 h-5 rounded border-2 border-[#845EC2] appearance-none cursor-pointer
                                                            bg-white checked:bg-[#845EC2] checked:border-[#845EC2]
                                                            checked:after:content-['✓'] checked:after:text-white checked:after:text-sm
                                                            checked:after:font-bold checked:after:block checked:after:text-center
                                                            checked:after:leading-[18px]"
                                            />
                                            <span>중복 선택</span>
                                        </label>
                                        <label className="flex items-center space-x-1">
                                            <input
                                                type="checkbox"
                                                checked={categoryCode === "D" && option.title === "사이즈" ? true : option.isRequired}
                                                disabled={categoryCode === "D" && option.title === "사이즈"}
                                                onChange={(e) => {
                                                    if (categoryCode === "D" && option.title === "사이즈") return;
                                                    const newOptions = [...options];
                                                    newOptions[optIdx].isRequired = e.target.checked;
                                                    setOptions(newOptions);
                                                }}
                                                className="w-5 h-5 rounded border-2 border-[#845EC2] appearance-none cursor-pointer
                                                            bg-white checked:bg-[#845EC2] checked:border-[#845EC2]
                                                            checked:after:content-['✓'] checked:after:text-white checked:after:text-sm
                                                            checked:after:font-bold checked:after:block checked:after:text-center
                                                            checked:after:leading-[18px]"
                                            />
                                            <span>필수 선택</span>
                                        </label>
                                        {/* 옵션 전체 삭제 버튼 */}
                                        <button
                                            onClick={() => {
                                                const newOptions = [...options];
                                                newOptions.splice(optIdx, 1);
                                                setOptions(newOptions);
                                            }}
                                            className="bg-white text-red-500 font-bold text-xl px-2"
                                            title="옵션 전체 삭제"
                                        >
                                            ×
                                        </button>
                                    </div>
                                </div>
                                {/* 옵션 상세 항목 반복 */}
                                {option.details.map((detail, detailIdx) => (
                                <div key={detailIdx} className="flex space-x-2 mb-2 bg-white p-2 rounded">

                                    {/* 옵션명: 공통 */}
                                    <input
                                    type="text"
                                    placeholder="옵션명"
                                    value={detail.name}
                                    className="border p-2 rounded bg-white text-black"
                                    onChange={(e) => {
                                        const newOptions = [...options];
                                        newOptions[optIdx].details[detailIdx].name = e.target.value;
                                        setOptions(newOptions);
                                    }}
                                    />

                                    {/* 드레스 + 사이즈일 때만 재고 */}
                                    {categoryCode === "D" && option.title === "사이즈" && (
                                    <input
                                        type="text"
                                        placeholder="재고"
                                        value={detail.stock}
                                        className="border p-2 rounded bg-white text-black"
                                        onChange={(e) => {
                                        const newOptions = [...options];
                                        newOptions[optIdx].details[detailIdx].stock = e.target.value;
                                        setOptions(newOptions);
                                        }}
                                    />
                                    )}

                                    {/* 드레스 + 사이즈가 아닐 때만 추가시간 */}
                                    {!(categoryCode === "D" && option.title === "사이즈") && (
                                    <input
                                        type="text"
                                        placeholder="추가시간 (분단위)"
                                        value={detail.extraTime || ""}
                                        className="border p-2 rounded bg-white text-black"
                                        onChange={(e) => {
                                        const newOptions = [...options];
                                        newOptions[optIdx].details[detailIdx].extraTime = e.target.value;
                                        setOptions(newOptions);
                                        }}
                                    />
                                    )}

                                    {/* 추가금: 공통 */}
                                    <input
                                    type="text"
                                    placeholder="추가금 (원)"
                                    value={detail.extraPrice || ""}
                                    className="border p-2 rounded bg-white text-black"
                                    onChange={(e) => {
                                        const newOptions = [...options];
                                        newOptions[optIdx].details[detailIdx].extraPrice = e.target.value;
                                        setOptions(newOptions);
                                    }}
                                    />

                                    {/* 상세 옵션 삭제 버튼 */}
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
                                    newOptions[optIdx].details.push({ name: "", stock: "", extraTime: "", extraPrice: "" });
                                    setOptions(newOptions);
                                }}
                                className="bg-white text-xl text-blue-600"
                                >
                                +
                                </button>
                            </div>
                            ))}

                            <button onClick={handleAddOption} className="border px-72 py-4 text-[#845EC2] rounded mt-2 bg-white">
                                + 옵션 추가하기
                            </button>
                        </div>
                    </div>
                </div>
            </div>
            
            {/* 상세설명 작성칸 */}
            <AddWrite ref={writeRef} onImageUpload={uploadImageToServer} />
                            
            {/* 상품 추가/취소 버튼 */}
            <div className="flex justify-end mt-6 space-x-4">
                <button onClick={handleSubmit}
                className="bg-[#845EC2] text-white px-6 py-2 rounded hover:bg-purple-500"
                disabled={categoryCode === 'M' && manager.trim() === ''}
                >추가</button>
                <button className="bg-gray-300 text-black px-6 py-2 rounded" onClick={() => navigate(-1)}>취소</button>
            </div>
        </div>
    );
}

export default AddProduct;