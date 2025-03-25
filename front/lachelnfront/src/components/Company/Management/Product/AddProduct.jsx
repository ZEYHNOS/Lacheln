import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import Addphoto from "../../../../image/Company/addimage.png";

function AddProduct() {
    const navigate = useNavigate();
    
    // 상태 변수
    const [productName, setProductName] = useState("");
    const [color, setColor] = useState("#FFFFFF");
    const [price, setPrice] = useState("");
    const [category, setCategory] = useState("드레스");
    const [stock, setStock] = useState(0);
    const [rentalTime, setRentalTime] = useState("2시간 대여");
    const [description, setDescription] = useState("");
    const [indoor, setIndoor] = useState(true);
    const [outdoor, setOutdoor] = useState(false);
    const [images, setImages] = useState([]);
    const [isDragging, setIsDragging] = useState(false);
    const [selectedImage, setSelectedImage] = useState(images.length > 0 ? images[0] : null);

    // 대표 이미지 업로드 핸들러
    const handleImageUpload = (event) => {
        event.preventDefault();
        let files = [];
        
        // 드래그로 이미지 추가
        if (event.dataTransfer) {
            files = Array.from(event.dataTransfer.files);
        }
        // 클릭으로 이미지 추가
        else if (event.target.files) {
            files = Array.from(event.target.files);
        }

        const imageURLs = files.map(file => URL.createObjectURL(file));
        setImages([...images, ...imageURLs]);
        setIsDragging(false);
    };

    return (
        <div className="w-full max-w-6xl mx-auto p-6 bg-white text-black rounded-md">
            <h1 className="text-2xl font-bold text-[#845EC2] mb-4">상품 추가하기</h1>

            {/* 이미지 설정 및 야외/실내촬영 여부 */}
            <div className="flex items-start space-x-6">
                {/* 대표 이미지 */}
                <div className="w-1/3 p-2">
                    <div className="border rounded-lg overflow-hidden bg-gray-100 w-full h-96">
                        {selectedImage ? (
                            <img src={selectedImage} alt="대표 이미지" className="w-full h-full object-cover" />
                        ) : (
                            <div className="w-full h-full flex items-center justify-center text-gray-400">
                                이미지 없음
                            </div>
                        )}
                    </div>

                    {/* 이미지 미리보기 */}
                    <div className="mt-4 flex space-x-2 overflow-x-auto whitespace-nowrap max-w-full p-1 custom-scrollbar">
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

                    {/* 이미지 업로드 버튼 (가로 정렬 + 대표 이미지 칸과 동일한 넓이) */}
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

                    {/* 체크박스 */}
                    <div className="bg-white rounded-md flex items-center space-x-6 mt-4">
                        <label className="flex items-center space-x-2 cursor-pointer">
                            <input 
                                type="checkbox" 
                                checked={indoor} 
                                onChange={() => setIndoor(!indoor)} 
                                className="w-5 h-5 border-2 border-[#845EC2] rounded-md appearance-none cursor-pointer 
                                        checked:bg-[#845EC2] checked:border-[#845EC2] flex items-center justify-center 
                                        relative"
                            />
                            <span className="text-black">실내촬영가능</span>
                            {indoor && (
                                <svg className="absolute w-3 h-3 text-white left-1/2 top-1/2 transform -translate-x-1/2 -translate-y-1/2" 
                                    xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor">
                                    <path fillRule="evenodd" 
                                        d="M20.285 6.228a1 1 0 0 1 0 1.415l-9 9a1 1 0 0 1-1.415 0l-5-5a1 1 0 1 1 1.415-1.415l4.293 4.293 8.293-8.293a1 1 0 0 1 1.414 0z"
                                        clipRule="evenodd" 
                                    />
                                </svg>
                            )}
                        </label>

                        <label className="flex items-center space-x-2 cursor-pointer">
                            <input 
                                type="checkbox" 
                                checked={outdoor} 
                                onChange={() => setOutdoor(!outdoor)} 
                                className="w-5 h-5 border-2 border-[#845EC2] rounded-md appearance-none cursor-pointer 
                                        checked:bg-[#845EC2] checked:border-[#845EC2] flex items-center justify-center 
                                        relative"
                            />
                            <span className="text-black">야외촬영가능</span>
                            {outdoor && (
                                <svg className="absolute w-3 h-3 text-white left-1/2 top-1/2 transform -translate-x-1/2 -translate-y-1/2" 
                                    xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor">
                                    <path fillRule="evenodd" 
                                        d="M20.285 6.228a1 1 0 0 1 0 1.415l-9 9a1 1 0 0 1-1.415 0l-5-5a1 1 0 1 1 1.415-1.415l4.293 4.293 8.293-8.293a1 1 0 0 1 1.414 0z"
                                        clipRule="evenodd" 
                                    />
                                </svg>
                            )}
                        </label>
                    </div>
                </div>

                {/* 상품 정보 설정 */}
                <div className="w-2/3 p-2">
                    {/* 상품 공개/비공개 */}
                    <div>
                        <div className="flex items-center justify-between">
                            <h2 className="text-lg font-semibold">상품정보</h2>
                            <select className="border p-1 rounded bg-white text-black">
                                <option>공개</option>
                                <option>비공개</option>
                            </select>
                        </div>

                        {/* 상품 설정 */}
                        <div className="mt-3 space-y-2">
                            <div className="flex items-center">
                                <label className="w-24">상품명</label>
                                <input type="text" value={productName} onChange={(e) => setProductName(e.target.value)}
                                    className="flex-grow border p-2 rounded bg-white text-black"/>
                            </div>

                            <div className="flex items-center">
                                <label className="w-24">색상</label>
                                <input type="text" value={color} onChange={(e) => setColor(e.target.value)}
                                    className="flex-grow border p-2 rounded bg-white text-black"/>
                                <input type="color" value={color} onChange={(e) => setColor(e.target.value)}
                                    className="ml-2 border p-1 rounded"/>
                            </div>

                            <div className="flex items-center">
                                <label className="w-24">가격</label>
                                <input type="text" value={price} onChange={(e) => setPrice(e.target.value)}
                                    className="flex-grow border p-2 rounded bg-white text-black"/>
                            </div>

                            <div className="flex items-center">
                                <label className="w-24">분류</label>
                                <select value={category} onChange={(e) => setCategory(e.target.value)}
                                    className="border p-2 rounded flex-grow bg-white text-black">
                                    <option>드레스</option>
                                    <option>한복</option>
                                    <option>정장</option>
                                </select>
                            </div>

                            <div className="flex items-center">
                                <label className="w-24">대여시간</label>
                                <select value={rentalTime} onChange={(e) => setRentalTime(e.target.value)}
                                    className="border p-2 rounded flex-grow bg-white text-black">
                                    <option>2시간 대여</option>
                                    <option>4시간 대여</option>
                                    <option>하루 대여</option>
                                </select>
                            </div>

                            {/* 옵션 추가 */}
                            <button className="border px-4 py-2 text-[#845EC2] rounded mt-2 bg-white">+ 옵션 추가하기</button>
                        </div>
                    </div>
                </div>
            </div>
            {/* 추가, 취소 버튼 */}
            <div className="flex justify-end mt-6 space-x-4">
                <button className="bg-[#845EC2] text-white px-6 py-2 rounded hover:bg-purple-500">추가</button>
                <button className="bg-gray-300 text-black px-6 py-2 rounded" onClick={() => navigate(-1)}>취소</button>
            </div>
        </div>
    );
}

export default AddProduct;