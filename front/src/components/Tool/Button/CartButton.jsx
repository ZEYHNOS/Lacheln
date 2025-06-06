import { FaShoppingCart, FaChevronDown, FaChevronUp } from "react-icons/fa";
import { useNavigate } from "react-router-dom";

export default function CartButton({ isActive, onClick, isLoggedIn }) {
    const navigate = useNavigate();

    const handleCartClick = () => {
        if (isLoggedIn) {
            onClick();
            navigate("/cart");
        }
    };

    return (
        <div className="relative">
            {/* 버튼 */}
            <div
                className={`flex items-center gap-1 cursor-pointer hover:bg-gray-200 p-2 rounded-lg transition ${isActive && isLoggedIn ? "bg-gray-300" : ""} ${!isLoggedIn ? "opacity-50 cursor-not-allowed" : ""}`}
                onClick={onClick}
            >
                <FaShoppingCart className="text-3xl text-[#845EC2]" />
                {isActive && isLoggedIn ? (
                    <FaChevronUp className="text-2xl text-[#845EC2]" />
                ) : (
                    <FaChevronDown className="text-2xl text-[#845EC2]" />
                )}
            </div>
            {/* 드롭다운 메뉴 */}
            {isActive && isLoggedIn && (
                <div className="absolute right-0 mt-2 w-40 bg-white shadow-lg rounded-lg py-2 z-50">
                    <p className="text-gray-500 text-center py-1 font-semibold">장바구니</p>
                    <hr />
                    <ul className="text-black text-sm">
                        <li className="px-4 py-2 hover:bg-gray-100 cursor-pointer" onClick={handleCartClick}>🛒 장바구니</li>
                        <li className="px-4 py-2 hover:bg-gray-100 cursor-pointer">🔖 찜한 상품</li>
                    </ul>
                </div>
            )}
        </div>
    );
}
