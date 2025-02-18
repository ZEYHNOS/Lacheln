import appleLogo from "../../image/apple-logo.png";
import googleLogo from "../../image/google-logo.png";
import kakaoLogo from "../../image/kakaotalk-logo.png";

export default function SocialLogin() {
    return (
        <div className="mt-6 text-center">
            <p className="text-gray-500">or</p>
            <div className="flex justify-center gap-6 mt-4">
                {/* 카카오 로그인 버튼 */}
                <button className="w-20 h-20 bg-white rounded-full flex items-center justify-center shadow-md
                                   border-2 border-transparent hover:border-[#845EC2] transition">
                    <img src={kakaoLogo} alt="카카오 로그인" className="w-14 h-14 object-contain" />
                </button>

                {/* 애플 로그인 버튼 */}
                <button className="w-20 h-20 bg-white rounded-full flex items-center justify-center shadow-md
                                   border-2 border-transparent hover:border-[#845EC2] transition">
                    <img src={appleLogo} alt="애플 로그인" className="w-14 h-14 object-contain" />
                </button>

                {/* 구글 로그인 버튼 */}
                <button className="w-20 h-20 bg-white rounded-full flex items-center justify-center shadow-md
                                   border-2 border-transparent hover:border-[#845EC2] transition">
                    <img src={googleLogo} alt="구글 로그인" className="w-14 h-14 object-contain" />
                </button>
            </div>
        </div>
    );
}
