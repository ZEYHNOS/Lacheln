import { useState } from "react";
import { FaChevronDown, FaChevronUp } from "react-icons/fa";

export default function NationButton() {
    const [isOpen, setIsOpen] = useState(false);
    const [selectedFlag, setSelectedFlag] = useState("kr"); // 기본값: 한국 국기
    let timeoutId = null;

    const countries = [
        { code: "kr", name: "한국", flag: "https://upload.wikimedia.org/wikipedia/commons/thumb/0/09/Flag_of_South_Korea.svg/120px-Flag_of_South_Korea.svg.png" },
        { code: "us", name: "미국", flag: "https://upload.wikimedia.org/wikipedia/en/a/a4/Flag_of_the_United_States.svg" },
        { code: "jp", name: "일본", flag: "https://upload.wikimedia.org/wikipedia/en/9/9e/Flag_of_Japan.svg" },
        { code: "cn", name: "중국", flag: "https://upload.wikimedia.org/wikipedia/commons/thumb/f/fa/Flag_of_the_People%27s_Republic_of_China.svg/120px-Flag_of_the_People%27s_Republic_of_China.svg.png" },
    ];    

    const handleMouseEnter = () => {
        clearTimeout(timeoutId);
        setIsOpen(true);
    };

    const handleMouseLeave = () => {
        timeoutId = setTimeout(() => setIsOpen(false), 50);
    };

    return (
        <div className="relative">
            {/* 버튼 */}
            <div 
                className="flex items-center gap-1 cursor-pointer hover:bg-gray-200 p-2 rounded-lg transition"
                onMouseEnter={handleMouseEnter}
                onMouseLeave={handleMouseLeave}
            >
                
                <div className="border border-[#845EC2] rounded-lg p-1">
                    <img
                        src={countries.find(country => country.code === selectedFlag)?.flag}
                        alt="국기"
                        className="w-12 h-8"
                    />
                </div>
                {isOpen ? <FaChevronUp className="text-2xl text-[#845EC2]" /> : <FaChevronDown className="text-2xl text-[#845EC2]" />}
            </div>

            {/* 드롭다운 메뉴 */}
            {isOpen && (
                <div 
                    className="absolute left-0 mt-2 w-40 bg-white shadow-lg rounded-lg py-2 z-50"
                    onMouseEnter={handleMouseEnter}
                    onMouseLeave={handleMouseLeave}
                >
                    <p className="text-gray-500 text-center py-1 font-semibold">국가설정</p>
                    <ul className="text-black text-sm">
                        {countries.map((country) => (
                            <li 
                                key={country.code}
                                className="px-4 py-2 hover:bg-gray-100 cursor-pointer flex items-center gap-2"
                                onClick={() => setSelectedFlag(country.code)}
                            >
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
