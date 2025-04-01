import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import UserRegisterForm from "./UserRegisterForm";

export default function UserRegisterPage() {
  const [allChecked, setAllChecked] = useState(false);
  const [termsChecked, setTermsChecked] = useState(false);
  const [privacyChecked, setPrivacyChecked] = useState(false);
  const [marketingChecked, setMarketingChecked] = useState(false);
  const adsNotification = marketingChecked ? "Y" : "N";
  const navigate = useNavigate();

  const handleAllChecked = () => {
    const newState = !allChecked;
    setAllChecked(newState);
    setTermsChecked(newState);
    setPrivacyChecked(newState);
    setMarketingChecked(newState);
  };

  const isNextEnabled = termsChecked && privacyChecked;

  const handleNext = () => {
    // 👉 다음 화면(/register/user)으로 상태와 함께 이동
    navigate("/register/user", {
      state: {
        adsNotification: marketingChecked ? "Y" : "N",
      },
    });
  };

  return (
    <div className="flex justify-center items-center min-h-screen bg-white p-4">
      <div className="w-[900px] p-10 bg-white shadow-lg rounded-2xl border border-gray-200">
        {/* 클릭 시 홈으로 이동 */}
        <Link to="/" className="block text-center">
          <h1 className="text-5xl font-inknut font-bold text-[#845EC2]">Lächeln</h1>
          <p className="text-[#845EC2] mb-6">스튜디오 드레스 메이크업</p>
        </Link>

        <h2 className="text-lg text-[#845EC2] font-semibold mt-6">이용약관 및 개인정보처리방침</h2>
        <p className="text-sm text-gray-500">이용약관, 개인정보 수집 이용에 모두 동의합니다.</p>

        <div className="mt-4 space-y-4">
          <div className="flex items-center space-x-2">
            <input
              type="checkbox"
              checked={allChecked}
              onChange={handleAllChecked}
              className="w-5 h-5 border border-[#845EC2] rounded-sm appearance-none checked:bg-[#845EC2] checked:after:content-['✔'] checked:after:text-white checked:after:text-xs checked:after:flex checked:after:justify-center checked:after:items-center cursor-pointer"
            />
            <span className="text-sm font-semibold text-[#845EC2]">모두 동의</span>
          </div>

          <div className="flex items-center space-x-2">
            <input
              type="checkbox"
              checked={termsChecked}
              onChange={() => setTermsChecked(!termsChecked)}
              className="w-5 h-5 border border-[#845EC2] rounded-sm appearance-none checked:bg-[#845EC2] checked:after:content-['✔'] checked:after:text-white checked:after:text-xs checked:after:flex checked:after:justify-center checked:after:items-center cursor-pointer"
            />
            <span className="text-sm text-[#845EC2] font-semibold">이용약관 동의 (필수)</span>
          </div>
          <div className="border border-[#845EC2] p-3 rounded-md text-sm text-gray-700 h-32 overflow-y-scroll custom-scrollbar">
            <strong>제 1조 목적</strong>
            <p className="mt-1">
              본 약관은 ‘사이트명’에서 제공하는 서비스의 이용조건과 운영에 관한
              제반사항을 규정함을 목적으로 합니다. 해당 내용은 길게 작성될 예정이며,
              이를 고려하여 스크롤 기능을 추가하였습니다.
            </p>

            <strong>제 2 조 (용어의 정의)</strong>
            <p className="mt-1">
              1. "이용자"란 회사의 서비스에 접속하여 본 약관에 따라 서비스를 이용하는 회원 및 비회원을 말합니다.<br/>
              2. "회원"이란 회사에 회원가입을 하고 서비스를 이용하는 자를 의미합니다.<br/>
              3. "비회원"이란 회원가입을 하지 않고 서비스를 이용하는 자를 의미합니다.<br/>
              4. "콘텐츠"란 서비스 내에서 이용자가 게시하거나 회사가 제공하는 텍스트, 이미지, 영상, 링크 등의 정보를 의미합니다.
            </p>
            
            <strong>제 3 조 (약관의 효력 및 변경)</strong>
            <p className="mt-1">
              1. 본 약관은 이용자가 서비스에 접속하여 동의함으로써 효력이 발생합니다.<br/>
              2. 회사는 관련 법령을 준수하는 범위 내에서 본 약관을 개정할 수 있으며, 변경된 약관은 적용일 7일 전(이용자에게 불리한 변경의 경우 30일 전)부터 서비스 내 공지 또는 이메일을 통해 통지합니다.<br/>
              3. 이용자가 변경된 약관에 동의하지 않는 경우 서비스 이용을 중단하고 회원 탈퇴를 요청할 수 있으며, 변경된 약관의 적용일 이후에도 서비스를 계속 이용하는 경우 변경된 약관에 동의한 것으로 간주합니다.
            </p>
            
            <strong>제 4 조 (회원가입 및 관리)</strong>
            <p className="mt-1">
              1. 이용자는 회사가 정한 절차에 따라 회원가입을 신청하며, 회사는 이를 승낙함으로써 회원가입이 완료됩니다.<br/>
              2. 이용자는 회원가입 시 정확한 정보를 제공해야 하며, 허위 정보를 제공하여 발생한 문제에 대한 책임은 이용자 본인에게 있습니다.<br/>
              3. 회원 계정의 관리 책임은 이용자에게 있으며, 계정 도용 및 부정 사용에 대한 책임은 회사가 부담하지 않습니다.<br/>
              4. 이용자는 계정 정보 변경 시 즉시 업데이트해야 하며, 미갱신으로 인한 불이익은 이용자가 부담합니다.
            </p>
            
            <strong>제 5 조 (서비스의 제공 및 변경)</strong>
            <p className="mt-1">
              1. 회사는 이용자에게 다음과 같은 서비스를 제공합니다.<br/>
                - 콘텐츠 제공 서비스<br/>
                - 커뮤니티 및 소셜 네트워킹 서비스<br/>
                - 기타 회사가 정하는 서비스<br/>
              2. 회사는 서비스 운영 및 기술적 필요에 따라 서비스 내용을 변경할 수 있으며, 중요한 변경 사항은 사전에 공지합니다.<br/>
              3. 회사는 이용자의 서비스 이용을 원활하게 하기 위해 유지보수, 시스템 점검 등의 사유로 서비스 제공을 일시적으로 중단할 수 있으며, 사전 통지를 원칙으로 합니다.
            </p>
            
            <strong>제 6 조 (이용자의 의무)</strong>
            <p className="mt-1">
              1. 이용자는 다음 행위를 하여서는 안 됩니다.<br/>
                - 타인의 계정을 도용하거나 부정 사용하는 행위<br/>
                - 회사의 서비스 운영을 방해하는 행위<br/>
                - 불법적인 정보 게시 및 유포 행위<br/>
                - 타인의 명예를 훼손하거나 권리를 침해하는 행위<br/>
                - 기타 관련 법령 및 본 약관을 위반하는 행위<br/>
              2. 이용자가 위 행위를 할 경우 회사는 사전 통보 없이 이용 제한, 계정 삭제 등의 조치를 취할 수 있습니다.
            </p>
            
            <strong>제 7 조 (서비스 이용 제한 및 해지)</strong>
            <p className="mt-1">
              1. 이용자가 본 약관을 위반하거나 서비스 운영을 방해하는 경우, 회사는 서비스 이용을 제한하거나 계정을 삭제할 수 있습니다.<br/>
              2. 이용자가 회원 탈퇴를 원하는 경우, 서비스 내 탈퇴 절차를 이용하여 언제든지 탈퇴할 수 있습니다.
            </p>
            
            <strong>제 8 조 (개인정보 보호)</strong>
            <p className="mt-1">
              1. 회사는 이용자의 개인정보를 보호하기 위해 관련 법령을 준수하며, 개인정보 보호정책을 따릅니다.<br/>
              2. 이용자의 개인정보 수집 및 이용 목적, 보관 기간 등은 별도의 개인정보 처리방침에서 확인할 수 있습니다.
            </p>
            
            <strong>제 9 조 (면책조항)</strong>
            <p className="mt-1">
              1. 회사는 천재지변, 전쟁, 기간통신사업자의 서비스 장애 등 불가항력적인 사유로 인해 서비스를 제공할 수 없는 경우 책임을 지지 않습니다.<br/>
              2. 이용자가 본인의 과실로 인해 발생한 서비스 이용상의 손해에 대해 회사는 책임을 지지 않습니다.
            </p>
            
            <strong>제 10 조 (준거법 및 관할)</strong>
            <p className="mt-1">
              1. 본 약관과 관련된 분쟁은 대한민국 법률을 준거법으로 합니다.<br/>
              2. 본 약관과 관련하여 회사와 이용자 간에 발생한 분쟁은 회사의 본사 소재지를 관할하는 법원을 전속 관할법원으로 합니다.
            </p>
          </div>
        </div>

        <div className="mt-6">
          <div className="flex items-center space-x-2 mb-2">
            <input
              type="checkbox"
              checked={privacyChecked}
              onChange={() => setPrivacyChecked(!privacyChecked)}
              className="w-5 h-5 border border-[#845EC2] rounded-sm appearance-none checked:bg-[#845EC2] checked:after:content-['✔'] checked:after:text-white checked:after:text-xs checked:after:flex checked:after:justify-center checked:after:items-center cursor-pointer"
            />
            <span className="text-sm text-[#845EC2] font-semibold">개인정보 수집 및 이용 동의 (필수)</span>
          </div>
          <div className="border border-[#845EC2] p-3 rounded-md text-sm text-gray-700 h-40 overflow-y-scroll custom-scrollbar">
            <strong>1. 개인정보의 수집 및 이용 목적</strong>
            <p className="mt-1">
              Lächeln(이하 ‘회사’)은 회원 가입 및 서비스 제공을 위해 다음과 같은 목적으로 개인정보를 수집 및 이용합니다.
            </p>
            <p className="mt-1">
              - 회원제 서비스 제공 및 관리 (본인 식별, 인증, 회원 유지, 계약이행, 고객관리)<br />
              - 서비스 제공에 따른 요금 정산 및 청구<br />
              - 서비스 이용 기록 관리 및 부정 이용 방지<br />
              - 법적 의무 준수를 위한 기록 보관
            </p>

            <strong className="block mt-2">2. 수집하는 개인정보 항목</strong>
            <p className="mt-1">
              <strong>필수 항목:</strong> 이름, 이메일, 휴대전화번호, 생년월일, 아이디, 비밀번호
            </p>
            <p className="mt-1">
              <strong>선택 항목:</strong> 프로필 사진, 관심 카테고리
            </p>

            <strong className="block mt-2">3. 개인정보의 보유 및 이용 기간</strong>
            <p className="mt-1">
              회사는 회원 탈퇴 시까지 개인정보를 보유 및 이용합니다. 단, 관련 법령에 따라 일정 기간 동안 보존이 필요한 경우 해당 기간 동안 보관 후 파기합니다.
            </p>
            <p className="mt-1">
              - 계약 또는 청약철회 등에 관한 기록: 5년 (전자상거래 등에서의 소비자 보호에 관한 법률)<br />
              - 대금결제 및 재화 등의 공급에 관한 기록: 5년 (전자상거래 등에서의 소비자 보호에 관한 법률)<br />
              - 소비자의 불만 또는 분쟁 처리에 관한 기록: 3년 (전자상거래 등에서의 소비자 보호에 관한 법률)
            </p>

            <strong className="block mt-2">4. 동의를 거부할 권리 및 불이익</strong>
            <p className="mt-1">
              이용자는 개인정보 수집 및 이용에 동의하지 않을 권리가 있으며, 동의를 거부할 경우 회원 가입이 제한될 수 있습니다.
            </p>
          </div>
        </div>

        {/* 약관 체크박스 */}
        <div className="mt-4 flex items-center space-x-2">
          <input
            type="checkbox"
            checked={marketingChecked}
            onChange={() => setMarketingChecked(!marketingChecked)}
            className="w-5 h-5 border border-[#845EC2] rounded-sm appearance-none checked:bg-[#845EC2] checked:after:content-['✔'] checked:after:text-white checked:after:text-xs checked:after:flex checked:after:justify-center checked:after:items-center cursor-pointer"
          />
          <span className="text-sm text-[#845EC2]">
            마케팅 활용 및 광고성 정보 수신 동의 (선택)
          </span>
        </div>

        <div className="mt-6 flex space-x-2">
          <button className="w-full bg-gray-200 text-gray-600 py-2 rounded" onClick={() => navigate("/")}>취소</button>
          <button
            className={`w-full py-2 rounded ${isNextEnabled ? "bg-[#845EC2] text-white"
                : "bg-gray-300 text-gray-500 cursor-not-allowed"
            }`}
            disabled={!isNextEnabled}
            onClick={() => {
              // ✅ 상태 로직 처리
              handleNext();

              // ✅ 상태 넘기면서 페이지 이동
              navigate("/register/user/form", {
                state: { adsNotification: marketingChecked ? "Y" : "N" },
              });
            }}
          >
            다음
          </button>
        </div>
      </div>
    </div>
  );
}
