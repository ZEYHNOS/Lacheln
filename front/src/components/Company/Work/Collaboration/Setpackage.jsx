import React, { useRef, useState } from "react";
import Addphoto from "../../../../image/Company/addimage.png";

function Setpackage() {
    const [packageName, setPackageName] = useState("");
    const [discount, setDiscount] = useState(0);
    const [endDate, setEndDate] = useState("");
    const [image, setImage] = useState(null);
    const [imagePreview, setImagePreview] = useState(null);
    const writeRef = useRef();

    // 대표 이미지 업로드
    const handleImageUpload = (e) => {
        const file = e.target.files[0];
        if (file) {
            setImage(file);
            setImagePreview(URL.createObjectURL(file));
        }
    };

    // 등록 버튼 클릭
    const handleSubmit = () => {
        alert("패키지 등록! (데모)");
    };

    return (
        <div className="w-full max-w-4xl mx-auto p-8 bg-white text-black rounded-md">
            <h1 className="text-2xl font-bold text-[#845EC2] mb-8">패키지 상품 추가하기</h1>
            <div className="flex items-start space-x-8">
                {/* 대표 이미지 */}
                <div className="w-1/3 p-2">
                    <div className="border rounded-lg overflow-hidden bg-gray-100 w-full h-96 flex items-center justify-center">
                        {imagePreview ? (
                            <img src={imagePreview} alt="대표 이미지" className="w-full h-full object-cover" />
                        ) : (
                            <span className="text-gray-400">No image</span>
                        )}
                    </div>
                    <label className="mt-4 block w-full text-center border border-[#845EC2] text-[#845EC2] rounded-md py-4 cursor-pointer bg-white flex items-center justify-center space-x-2">
                        <img src={Addphoto} alt="이미지 추가" className="w-6 h-6" />
                        <span className="text-[#845EC2] font-medium">이미지 추가하기</span>
                        <input type="file" accept="image/*" className="hidden" onChange={handleImageUpload} />
                    </label>
                </div>
                {/* 패키지 정보 입력 */}
                <div className="w-2/3 p-2">
                    <div className="mb-6">
                        <h2 className="text-lg font-semibold mb-4">상품정보</h2>
                        <div className="flex items-center mb-4">
                            <label className="w-24">패키지명</label>
                            <input type="text" value={packageName} onChange={e => setPackageName(e.target.value)} className="flex-grow border p-2 rounded bg-white text-black" />
                        </div>
                        <div className="flex items-center mb-4">
                            <label className="w-24">할인율(%)</label>
                            <input type="number" min="0" max="100" value={discount} onChange={e => setDiscount(e.target.value)} className="flex-grow border p-2 rounded bg-white text-black" />
                        </div>
                        <div className="flex items-center mb-4">
                            <label className="w-24">종료일</label>
                            <input type="date" value={endDate} onChange={e => setEndDate(e.target.value)} className="flex-grow border p-2 rounded bg-white text-black" />
                        </div>
                        {/* 상세설명 자리 (텍스트에디터 대체) */}
                        <div className="flex flex-col mb-4">
                            <label className="mb-2">상세설명</label>
                            <textarea ref={writeRef} rows={6} className="border p-2 rounded bg-white text-black resize-none" placeholder="패키지 상세 설명을 입력하세요." />
                        </div>
                    </div>
                    {/* 등록/취소 버튼 */}
                    <div className="flex justify-end space-x-4 mt-8">
                        <button onClick={handleSubmit} className="bg-[#845EC2] text-white px-8 py-3 rounded hover:bg-purple-500">등록</button>
                        <button className="bg-gray-300 text-black px-8 py-3 rounded">취소</button>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default Setpackage; 