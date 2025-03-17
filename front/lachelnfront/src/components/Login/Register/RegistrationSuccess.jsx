import { Link } from "react-router-dom";

export default function RegistrationSuccess() {
  return (
    <div className="flex justify-center items-center min-h-screen bg-white p-4">
      <div className="w-[600px] p-10 bg-white shadow-lg rounded-2xl border border-gray-200 text-center">
        <Link to="/" className="block text-center">
          <h1 className="text-5xl font-inknut font-bold text-[#845EC2]">Lächeln</h1>
          <p className="text-[#845EC2] mb-6">스튜디오 드레스 메이크업</p>
        </Link>

        <h2 className="text-xl text-[#845EC2] font-semibold mt-6">회원가입 완료</h2>
        <p className="mt-4 text-gray-600">회원가입이 성공적으로 완료되었습니다.</p>

        <Link to="/login">
          <button className="w-full mt-6 bg-[#845EC2] text-white p-3 rounded-md">
            로그인하러 가기
          </button>
        </Link>
      </div>
    </div>
  );
}
