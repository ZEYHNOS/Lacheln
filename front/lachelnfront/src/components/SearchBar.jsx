import { useState } from "react";
import { FaSearch } from "react-icons/fa";

export default function SearchBar() {
    const [query, setQuery] = useState("");
    const [suggestions, setSuggestions] = useState([]);
    const [isDropdownVisible, setDropdownVisible] = useState(false);

    // 더미 추천 검색어 (나중에 API 연동 가능)
    const dummySuggestions = ["ssg닷컴", "ssg", "ssf몰", "ss501", "ssf", "ssd", "ssf샵"];

    // 입력 변경 핸들러
    const handleChange = (e) => {
        const value = e.target.value;
        setQuery(value);

        if (value.trim().length > 0) {
            // 검색어가 있을 때 추천어 필터링
            setSuggestions(dummySuggestions.filter((s) => s.includes(value)));
            setDropdownVisible(true);
        } else {
            setDropdownVisible(false);
        }
    };

    // 추천 검색어 클릭 핸들러
    const handleSelectSuggestion = (suggestion) => {
        setQuery(suggestion);
        setDropdownVisible(false);
    };

    return (
        <div className="relative w-full">
            <div className="relative bg-[#FBFBFB] rounded-full border-2 border-[#845EC2] flex items-center px-8 w-full h-16">
                <FaSearch className="text-[#845EC2] text-3xl cursor-pointer" />
                <input
                    type="text"
                    placeholder="검색..."
                    value={query}
                    onChange={handleChange}
                    className="w-full h-full bg-[#FBFBFB] text-black placeholder-gray-400 outline-none pl-4 text-xl"
                />
            </div>

            {/* 자동완성 드롭다운 */}
            {isDropdownVisible && (
                <div className="absolute left-0 w-full bg-white border border-gray-300 rounded-lg mt-1 shadow-md z-50">
                    <ul>
                        {suggestions.length > 0 ? (
                            suggestions.map((suggestion, index) => (
                                <li
                                    key={index}
                                    className="px-4 py-2 cursor-pointer hover:bg-gray-100 flex items-center"
                                    onClick={() => handleSelectSuggestion(suggestion)}
                                >
                                    <FaSearch className="text-gray-400 mr-2" />
                                    {suggestion}
                                </li>
                            ))
                        ) : (
                            <li className="px-4 py-2 text-gray-500">검색 결과 없음</li>
                        )}
                    </ul>
                </div>
            )}
        </div>
    );
}
ㄴ