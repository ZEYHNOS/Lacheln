import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { Star } from "lucide-react";
import axios from "axios";
import productDummy from "./productDummy";

function ViewProduct() {
    const { id } = useParams();
    const navigate = useNavigate();
    const [product, setProduct] = useState(null);
    const [selectedImage, setSelectedImage] = useState(null); // 대표 이미지 상태 추가

    // 실제 백엔드에서 받아오는 주소
    // useEffect(() => {
    //     axios.get(`http://localhost:5050/product/detail/${id}`)
    //         .then(res => {
    //             setProduct(res.data);
    //             setSelectedImage(res.data.image_url_list?.[0]);
    //         })
    //         .catch(err => console.error("상품 상세 정보 불러오기 실패", err));
    // }, [id]);
    useEffect(() => {
        const found = productDummy.find(p => p.id === id);
        if (found) {
            setProduct(found);
            setSelectedImage(found.image_url_list?.[0]);
        }
    }, [id]);

    if (!product) return <div className="p-6">로딩 중...</div>;

    const {
        name,
        price,
        status,
        rec,
        task_time,
        in_available,
        out_available,
        color,
        image_url_list,
        option_list,
        description
    } = product;

    return (
        <div className="w-full max-w-6xl mx-auto p-6 bg-white text-black rounded-md">
            <div className="flex justify-between items-center mb-4">
                <h1 className="text-2xl font-bold text-[#845EC2]">상품 상세 보기</h1>
                <button
                    onClick={() => navigate(`/company/product/edit/${id}`)}
                    className="bg-[#845EC2] text-white px-4 py-2 rounded hover:bg-purple-500"
                >
                    수정
                </button>
            </div>

            <div className="flex items-start space-x-6">
                {/* 이미지 영역 */}
                <div className="w-1/3 p-2">
                    <div className="border rounded-lg overflow-hidden bg-gray-100 w-full h-96">
                        {selectedImage ? (
                            <img src={selectedImage} alt="대표 이미지" className="w-full h-full object-cover" />
                        ) : (
                            <div className="w-full h-full flex items-center justify-center text-gray-400">No image</div>
                        )}
                    </div>

                    <div className="mt-4 flex space-x-2 overflow-x-auto max-w-full p-1">
                        {image_url_list?.map((img, index) => (
                            <img
                                key={index}
                                src={img}
                                alt={`업로드된 이미지 ${index}`}
                                onClick={() => setSelectedImage(img)}
                                className={`w-16 h-16 object-cover rounded-md cursor-pointer flex-shrink-0 border 
                                            ${selectedImage === img ? "border-[#845EC2] border-2" : "border-gray-300"}`}
                            />
                        ))}
                    </div>

                    {/* 실내/야외 체크 */}
                    <div className="bg-white rounded-md flex items-center space-x-6 mt-4">
                        <label className="flex items-center space-x-2 cursor-default">
                            <input
                                type="checkbox"
                                checked={in_available === "Y"}
                                readOnly
                                className="w-5 h-5 rounded border-2 border-[#845EC2] appearance-none cursor-default
                                            bg-white checked:bg-[#845EC2] checked:border-[#845EC2]
                                            checked:after:content-['✓'] checked:after:text-white checked:after:text-sm
                                            checked:after:font-bold checked:after:block checked:after:text-center
                                            checked:after:leading-[18px]"
                            />
                            <span className="text-black">실내촬영가능</span>
                        </label>

                        <label className="flex items-center space-x-2 cursor-default">
                            <input
                                type="checkbox"
                                checked={out_available === "Y"}
                                readOnly
                                className="w-5 h-5 rounded border-2 border-[#845EC2] appearance-none cursor-default
                                            bg-white checked:bg-[#845EC2] checked:border-[#845EC2]
                                            checked:after:content-['✓'] checked:after:text-white checked:after:text-sm
                                            checked:after:font-bold checked:after:block checked:after:text-center
                                            checked:after:leading-[18px]"
                            />
                            <span className="text-black">야외촬영가능</span>
                        </label>
                    </div>
                </div>

                {/* 정보 영역 */}
                <div className="w-2/3 p-2">
                    <div className="flex items-center justify-between mb-2">
                        <div className="flex items-center space-x-2">
                            <h2 className="text-lg font-semibold">상품정보</h2>
                            <Star
                                className={`w-6 h-6 ${rec === "Y" ? "text-purple-600 fill-current" : "text-purple-400"}`}
                                fill={rec === "Y" ? "currentColor" : "none"}
                            />
                        </div>
                        <select value={status} disabled className="border p-1 rounded bg-white text-black appearance-none">
                            <option value="ACTIVE">공개</option>
                            <option value="INACTIVE">비공개</option>
                            <option value="PACKAGE">패키지전용</option>
                        </select>
                    </div>

                    <div className="space-y-2">
                        <div className="flex items-center">
                            <label className="w-24">상품명</label>
                            <input value={name} disabled className="flex-grow border p-2 rounded bg-white text-black" />
                        </div>

                        <div className="flex items-center">
                            <label className="w-24">가격</label>
                            <input value={`${price.toLocaleString()}원`} disabled className="flex-grow border p-2 rounded bg-white text-black" />
                        </div>

                        <div className="flex items-center">
                            <label className="w-24">색상</label>
                            <select value={color} disabled className="flex-grow border p-2 rounded bg-white text-black appearance-none">
                                <option value="white">하양</option>
                                <option value="black">검정</option>
                                <option value="red">빨강</option>
                                <option value="orange">주황</option>
                                <option value="yellow">노랑</option>
                                <option value="green">초록</option>
                                <option value="blue">파랑</option>
                                <option value="navy">남</option>
                                <option value="purple">보라</option>
                                <option value="brown">갈색</option>
                                <option value="pink">분홍</option>
                            </select>
                            <div className="ml-2 w-24 h-10 rounded" style={{ backgroundColor: color, border: '1px solid #ccc' }} />
                        </div>

                        <div className="flex items-center">
                            <label className="w-24">대여시간</label>
                            <select value={task_time} disabled className="border p-2 rounded flex-grow bg-white text-black appearance-none">
                                <option value="30">30분 대여</option>
                                <option value="60">1시간 대여</option>
                                <option value="90">1시간 30분 대여</option>
                                <option value="120">2시간 대여</option>
                                <option value="150">2시간 30분 대여</option>
                                <option value="180">3시간 대여</option>
                            </select>
                        </div>
                    </div>

                    {/* 옵션 출력 */}
                    {option_list?.length > 0 && (
                        <div>
                            <h3 className="font-semibold mt-4">옵션 정보</h3>
                            {option_list.map((opt, i) => (
                                <div key={i} className="border rounded p-4 mt-2 bg-white">
                                    <div className="flex justify-between items-center mb-2">
                                        <input
                                            type="text"
                                            value={opt.name}
                                            disabled
                                            className="border p-2 rounded bg-white text-black"
                                        />
                                        <div className="flex items-center space-x-4">
                                            <label className="flex items-center space-x-1">
                                                <input
                                                    type="checkbox"
                                                    checked={opt.overlap === "Y"}
                                                    readOnly
                                                    className="w-5 h-5 rounded border-2 border-[#845EC2] appearance-none cursor-default
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
                                                    checked={opt.essential === "Y"}
                                                    readOnly
                                                    className="w-5 h-5 rounded border-2 border-[#845EC2] appearance-none cursor-default
                                                                bg-white checked:bg-[#845EC2] checked:border-[#845EC2]
                                                                checked:after:content-['✓'] checked:after:text-white checked:after:text-sm
                                                                checked:after:font-bold checked:after:block checked:after:text-center
                                                                checked:after:leading-[18px]"
                                                />
                                                <span>필수 선택</span>
                                            </label>
                                        </div>
                                    </div>

                                    {opt.option_dt_list.map((dt, j) => (
                                        <div key={j} className="flex space-x-2 mb-2 bg-white p-2 rounded">
                                            <input type="text" value={dt.op_dt_name} disabled className="border p-2 rounded bg-white text-black" />
                                            <input type="text" value={`${dt.plus_time}분`} disabled className="border p-2 rounded bg-white text-black" />
                                            <input type="text" value={`${dt.plus_cost}원`} disabled className="border p-2 rounded bg-white text-black" />
                                        </div>
                                    ))}
                                </div>
                            ))}
                        </div>
                    )}
                </div>
            </div>

            {/* 상세 설명 */}
            <div className="mt-6">
                <h2 className="text-lg font-semibold mb-2">상품 상세 설명</h2>
                <div
                    className="prose max-w-none bg-white border rounded p-4"
                    dangerouslySetInnerHTML={{ __html: description }}
                />
            </div>
        </div>
    );
}

export default ViewProduct;
