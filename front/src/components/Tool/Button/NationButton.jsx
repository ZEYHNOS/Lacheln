import { useState } from "react";
import { FaChevronDown, FaChevronUp } from "react-icons/fa";
import FlagKorea from "../../../image/Flag/Flag_Korea.png";
import FlagUs from "../../../image/Flag/Flag_Us.png";
import FlagJapan from "../../../image/Flag/Flag_Japan.png";
import FlagChina from "../../../image/Flag/Flag_China.png";

export default function NationButton({ isActive, onClick }) {
    const [selectedFlag, setSelectedFlag] = useState("kr");

    const countries = [
        { code: "kr", name: "한국", flag: FlagKorea },
        { code: "us", name: "미국", flag: FlagUs },
        { code: "jp", name: "일본", flag: FlagJapan },
        { code: "cn", name: "중국", flag: FlagChina },
    ];

    const handleClick = (countryCode) => {
        setSelectedFlag(countryCode);
        onClick(); // 드롭다운 닫기
        // 드롭다운 닫기는 Header에서 관리
    };

    return (
        <div className="relative">
            {/* 버튼 */}
            <div
                className="flex items-center gap-1 cursor-pointer hover:bg-gray-200 p-2 rounded-lg transition"
                onClick={onClick}
            >
                <div className="border border-[#845EC2] rounded-lg p-1">
                    <img
                        src={countries.find(country => country.code === selectedFlag)?.flag}
                        alt="국기"
                        className="w-12 h-8"/>
                </div>
                {isActive ? (
                    <FaChevronUp className="text-2xl text-[#845EC2]" />
                ) : (
                    <FaChevronDown className="text-2xl text-[#845EC2]" />
                )}
            </div>
            
            {/* 드롭다운 메뉴 */}
            {isActive && (
                <div className="absolute left-0 mt-2 w-40 bg-white shadow-lg rounded-lg py-2 z-50">
                    <p className="text-gray-500 text-center py-1 font-semibold">국가설정</p>
                    <ul className="text-black text-sm" >
                        {countries.map((country) => (
                            <li
                                key={country.code}
                                className="px-4 py-2 hover:bg-gray-100 cursor-pointer flex items-center gap-2"
                                onClick={() => handleClick(country.code)}>
                                <img src={country.flag} alt={country.name} className="w-6 h-4" />
                                {country.name}
                            </li>
                        ))}
                    </ul>
                </div>
            )}
        </div>
    );
}
