import { FaInstagram } from "react-icons/fa";
import { SiKakaotalk } from "react-icons/si";
import CompanyNav from "./CompanyNavgation/CompanyNav.jsx";

export default function Footer() {
  return (
    <footer className="-pp text-white h-auto py-2 px-8">
      <div className="grid grid-cols-3 gap-2 pl-10 items-start"> 
        
        {/* 좌측 */}
        <div className="space-y-2 mt-2">
          <h2 className="text-2xl font-inknut font-semibold mb-2">L ä c h e l n</h2>
          <p className="text-sm">레 헬 른</p>
          <p className="text-sm">㈜영진전문대학교</p>
          <p className="text-sm">대표이사 손지훈</p>
          <p className="text-sm">COPYRIGHT ⓒ2025 LECHELN ALL RIGHT RESERVED</p>
        </div>

        {/* 중앙 */}
        <div className="space-y-2 pl-10">
          <div className="flex gap-3 mb-2 text-4xl">
            {/* 인스타그램 링크 */}
            <a 
              href="https://www.instagram.com/with_yju/" 
              target="_blank" 
              rel="noopener noreferrer"
              className="text-white hover:text-gray-300 transition"
            >
              <FaInstagram />
            </a>
            {/* 카카오톡 링크 */}
            <a 
              href="https://open.kakao.com/o/sXO0JPeh" 
              target="_blank" 
              rel="noopener noreferrer"
              className="hover:text-gray-300 transition"
            >
              <SiKakaotalk className="text-[#ffffff]" />
            </a>
          </div>
          <h3 className="text-lg font-medium mb-1">Information</h3>
          <p className="text-sm">사업자등록번호 000 00 00000</p>
          <p className="text-sm">통신판매업신고 제2025-대구동구-00000호</p>
          <p className="text-sm">개인정보관리책임자 손휘성</p>
        </div>

        {/* 우측 */}
        <div className="space-y-2 pl-10 mt-3">
          <div className="text-sm space-y-2 text-white [&_a]:text-white">
            <CompanyNav />
          </div>
          <h3 className="text-lg font-medium mb-1">Contact Us</h3>
          <p className="text-sm">오전 10:00 - 오후 06:00 (월요일-금요일)</p>
          <p className="text-sm">T 010-3755-2866 | F 00 0000 0000</p>
          <p className="text-sm">대구광역시 북구 복현로 35 도서관 2층 G03호</p>
        </div>
      </div>
    </footer>
  );
}
