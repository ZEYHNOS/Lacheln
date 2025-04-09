import React, { useRef, useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import Addphoto from "../../../../image/Company/addimage.png";
import { Star } from "lucide-react";
import axios from "axios";
import AddWrite from "../../../Tool/WriteForm/AddWrite.jsx";

function AddProduct() {
    const navigate = useNavigate();
    
    // 상태 변수
    const [rec, setRec] = useState(false);
    const [name, setName] = useState("");
    const [color, setColor] = useState("WHITE");
    const [price, setPrice] = useState("");
    const [status, setStatus ] = useState("ACTIVE")
    const [task_time, settask_time] = useState("2시간 대여");
    const [indoor, setIndoor] = useState(true);
    const [outdoor, setOutdoor] = useState(false);
    const [images, setImages] = useState([]);
    const [isDragging, setIsDragging] = useState(false);
    const [selectedImage, setSelectedImage] = useState(images.length > 0 ? images[0] : null);
    const [options, setOptions] = useState([]);
    const [categoryCode, setCategoryCode] = useState({});
    const writeRef = useRef();


    // product/add 페이지를 열때 백엔드에서 카테고리를 불러옴
    // useEffect(() => {
    //     axios.get("/api/product/category")
    //         .then(res => {
    //             const code = res.data.cp_category;
    //             setCategoryCode(code);

    //             if (code === "d") {
    //                 axios.get("/api/product/sizes")
    //                     .then(sizeRes => {
    //                         const sizeList = sizeRes.data.size_list || [];
    //                         const sizeOption = {
    //                             title: "사이즈",
    //                             isRequired: false,
    //                             isMultiSelect: false,
    //                             details: sizeList.map(s => ({
    //                                 name: s.size,
    //                                 stock: s.stock,
    //                                 extraPrice: s.plus_cost,
    //                                 extraTime: ""
    //                             }))
    //                         };
    //                         setOptions([sizeOption]);
    //                     })
    //                     .catch(err => console.error("사이즈 불러오기 실패", err));
    //             }
    //         })
    //         .catch(err => console.error("카테고리 불러오기 실패", err));
    // }, []);

    // 백엔드 요청 대신 하드코딩된 더미 코드 사용 -> 삭제예정
    useEffect(() => {
        const code = "m"; // 드레스라고 가정
        setCategoryCode(code);
    
        if (code === "d") {
            // 사이즈 옵션용 기본 더미 데이터
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
    
        const imageURLs = files.map(file => URL.createObjectURL(file));
        const newImageList = [...images, ...imageURLs];
    
        setImages(newImageList);
    
        // 이미지가 처음 등록됐을 때, 대표 이미지 자동 설정
        if (!selectedImage && imageURLs.length > 0) {
            setSelectedImage(imageURLs[0]);
        }
    
        setIsDragging(false);
    };

    const handleChangeStatus = (event) => {
        setStatus(event.target.value);
    };
    
    // 백엔드 전송 코드 
    const handleSubmit = () => {
        // 상품 상세설명 추출해서 배열처리함
        const descriptionArray = writeRef.current?.getContentAsJsonArray();
        const payload = {
            name,
            price: parseInt(price || 0),
            status,
            rec: rec ? "Y" : "N",
            task_time: parseInt(task_time),
            description: JSON.stringify(descriptionArray),
            image_url_list: images,
            hash_tag_list: [],
            in_available: indoor ? "Y" : "N",
            out_available: outdoor ? "Y" : "N",
            color,
        };

    // ✅ 주소 설정
    let postUrl = "";
    if (categoryCode === "d") {
        postUrl = "/api/product/dress/register";
        payload.size_list = options[0].details.map(detail => ({
        size: detail.name,
        stock: parseInt(detail.stock || 0),
        plus_cost: parseInt(detail.extraPrice || 0)
        }));
        payload.option_list = options.slice(1).map(opt => ({
        name: opt.title,
        overlap: opt.isMultiSelect ? "Y" : "N",
        essential: opt.isRequired ? "Y" : "N",
        status: "ACTIVE",
        option_dt_list: opt.details.map(dt => ({
            op_dt_name: dt.name,
            plus_cost: parseInt(dt.extraPrice || 0),
            plus_time: parseInt(dt.extraTime || 0)
        }))
        }));
    } else if (categoryCode === "s") {
        postUrl = "/api/product/studio/register";
    } else if (categoryCode === "m") {
        postUrl = "/api/product/makeup/register";
    }
    
    // ✅ 스튜디오 / 메이크업은 option_list만
    if (categoryCode !== "d") {
        payload.option_list = options.map(opt => ({
        name: opt.title,
        overlap: opt.isMultiSelect ? "Y" : "N",
        essential: opt.isRequired ? "Y" : "N",
        status: "ACTIVE",
        option_dt_list: opt.details.map(dt => ({
            op_dt_name: dt.name,
            plus_cost: parseInt(dt.extraPrice || 0),
            plus_time: parseInt(dt.extraTime || 0)
        }))
        }));
    }
    
    // ✅ 전송
    axios.post(postUrl, payload)
        .then(res => {
        console.log("등록 성공", res.data);
        // 필요 시 navigate("/product/list")
        })
        .catch(err => {
        console.error("등록 실패", err);
        });
    };

    return (
        <div className="w-full max-w-6xl mx-auto p-6 bg-white text-black rounded-md">
            <h1 className="text-2xl font-bold text-[#845EC2] mb-4">상품 추가하기</h1>

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
                        {images.map((img, index) => (
                            <img 
                                key={index} 
                                src={img} 
                                alt={`업로드된 이미지 ${index}`} 
                                className="w-16 h-16 object-cover rounded-md cursor-pointer border-2 border-transparent hover:border-[#845EC2] flex-shrink-0" 
                                onClick={() => setSelectedImage(img)} 
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
                                <select value={color} 
                                    onChange={(e) => setColor(e.target.value)}
                                    className="flex-grow border p-2 rounded bg-white text-black">
                                    <option value="white">하양</option>
                                    <option value="black">검정</option>
                                    <option value="red">빨강</option>
                                    <option value="orange">주황</option>
                                    <option value="yellow">노랑</option>
                                    <option value="green">초록</option>
                                    <option value="blue">파랑</option>
                                    <option value="navy">남</option>
                                    <option value="pupple">보라</option>
                                    <option value="brown">갈색</option>
                                    <option value="pink">분홍</option>
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

                            {/* 옵션 추가하기 */}
                            {options.map((option, optIdx) => (
                            <div key={optIdx} className="border rounded p-4 mt-4 bg-white">
                                {/* 옵션 제목 */}
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
                                        checked={option.isRequired}
                                        onChange={(e) => {
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
                                    {categoryCode === "d" && option.title === "사이즈" && (
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
                                    {!(categoryCode === "d" && option.title === "사이즈") && (
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
            
            <AddWrite ref={writeRef} />

            <div className="flex justify-end mt-6 space-x-4">
                <button onClick={handleSubmit} 
                className="bg-[#845EC2] text-white px-6 py-2 rounded hover:bg-purple-500">추가</button>
                <button className="bg-gray-300 text-black px-6 py-2 rounded" onClick={() => navigate(-1)}>취소</button>
            </div>
        </div>
    );
}

export default AddProduct;