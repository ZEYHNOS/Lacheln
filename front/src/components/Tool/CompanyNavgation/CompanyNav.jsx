import Groupphoto from "../../../image/Company/Nav/groupphoto.png";
import Weddingcost from "../../../image/Company/Nav/weddingcost.png";
import Lacheln_bg from "../../../image/Company/Nav/lacheln_bg.png";
import Instagram from "../../../image/Company/Nav/instagram.png";
import Kakaotalk from "../../../image/Company/Nav/kakaotalk.png";
import Blog from "../../../image/Company/Nav/blog.png";
import Youtube from "../../../image/Company/Nav/youtube.png";
import Lacheln from "../../../image/Company/Nav/lacheln.png";
import Schedule from "../../../image/Company/Nav/schedule.png";
import Call2 from "../../../image/Company/Nav/call2.png";
import Home from "../../../image/Company/Nav/home.png";

// 회사소개 페이지
export function About() {
    return (
        <div className="min-h-screen bg-white text-black flex flex-col items-center justify-center">
            <h1 className="mt-[40px] block font-bold text-purple-800 text-[46px]">회사소개</h1>

            <div className="flex items-start justify-center my-8">
                {/* 텍스트: 좌측 */}
                <div className="flex flex-col items-start mr-8">
                    <div className="w-[250px] border-t-4 border-[#845EC2] ml-[1px] mb-[10px]"></div>
                    <p className="font-semibold text-[#845EC2] text-[38px] text-left mb-4">
                        레헬른은<br />
                        이렇게<br />
                        만들어졌어요
                    </p>
                    <p className="font-semibold text-[#5EBBAB] text-[24px] text-left">
                        2025년 1월 졸업을 위해 6명의 팀원이 구성되었습니다.<br />
                        서로 밀어주고 당기며 학습과 작업을 함께하고,<br />
                        믿음을 쌓아가며 여러가지를 시도하는 팀 입니다.
                    </p>
                </div>

                {/* 이미지: 우측 */}
                <img
                    src={Groupphoto}
                    alt="Groupphoto Icon"
                    className="w-[630px] h-[420px] mb-2"
                />
            </div>
            <div className="w-full flex justify-center mt-10">
                <div className="w-[230px] border-t-4 border-[#845EC2] ml-[400px] mb-[2px]"></div>
            </div>
            <div className="flex items-start justify-center mx-auto">
                {/* 이미지 영역 */}
                <img
                    src={Weddingcost}
                    alt="Weddingcost Icon"
                    className="w-[630px] h-[420px] mb-2 mr-8"
                />

                {/* 텍스트 영역 */}
                <div>
                    <p className="font-semibold text-[#845EC2] text-[38px] text-left mt-6">
                        레헬른은<br />이런 목표를<br />가지고 있어요
                    </p>
                    <p className="font-semibold text-[#5EBBAB] text-[24px] mt-4">
                        불투명한 결혼시장에서 가격투명화를 통하여,<br />
                        예비부부들의 가격부담을 낮추고,<br />
                        정확한 정보를 통한 업체를 선정하여,<br />
                        최고의 순간을 남기시길 목표로 합니다.
                    </p>
                </div>
            </div>

            <div className="flex items-start justify-center my-4">
                {/* 텍스트 영역 (왼쪽) */}
                <div className="flex flex-col items-start mr-8">
                    {/* 가로선 */}
                    <div className="w-[250px] border-t-4 border-[#845EC2] mb-[10px]"></div>

                    {/* 제목 */}
                    <p className="font-semibold text-[#845EC2] text-[38px] text-left mb-4">
                        레헬른은<br />
                        이러한 의미로<br />
                        지어졌어요
                    </p>

                    {/* 설명 */}
                    <p className="font-semibold text-[#5EBBAB] text-[24px] text-left">
                        독일어로 '미소짓다'라는 뜻의<br />
                        'Lacheln'에서 유래 되었습니다.<br />
                        웨딩 촬영중 작가님들이<br />
                        제일 많이 하는 말씀이기도 합니다.<br />
                        늘 저희 사이트를 이용하실때에는<br />
                        웃음만 지으시길 바라는 마음입니다.^_^
                    </p>
                </div>

                {/* 이미지 영역 (오른쪽) */}
                <img
                    src={Lacheln_bg}
                    alt="Lacheln_bg Icon"
                    className="w-[640px] h-[360px] mb-6"
                />
            </div>

            <div className="mb-6 font-semibold text-[#845EC2] text-[26px]">Lacheln Official Channel</div>
            <div className="flex flex-row justify-center gap-20 mb-6">
                <img src={Instagram} alt="Instagram Icon" className="w-[80px] h-[80px] mb-2" />
                <img src={Kakaotalk} alt="kakaotalk Icon" className="w-[80px] h-[80px] mb-2" />
                <img src={Blog} alt="blog Icon" className="w-[80px] h-[80px] mb-2" />
                <img src={Youtube} alt="youtube Icon" className="w-[80px] h-[80px] mb-2" />
            </div>
            <div className="flex flex-row justify-center gap-20 mb-6 text-[#845EC2] text-[24px]">
                <p>instagram</p>
                <p>kakaotalk</p>
                <p>blog</p>
                <p>youtube</p>
            </div>
        </div>
    );
}

// 이용약관 페이지
export function Terms() {
    return (
        <div className="min-h-screen bg-white text-black flex flex-col items-start justify-center">
            <div className="text-[33px] font-semibold mt-[120px] mx-auto text-center">서비스 이용약관</div>
            <div className="text-[29px] font-semibold mt-[60px] ml-20">본 이용약관은 스,드,메 서비스를 이용하는 고객님께 제공되는 서비스의 조건 및 이용에 관한 사항을 규정합니다.</div>

            <div className="text-[28px] font-semibold mt-10 ml-20">1.서비스 개요</div>
            <ul className="list-disc list-inside text-[20px] text-gray-800 space-y-1 ml-20">
                <li>스,드,메는 고객님의 원활한 웨딩 준비를 돕기 위해 스튜디오, 드레스, 메이크업 관련 정보를 제공하는 플랫폼입니다.<br />
                    본 서비스는 사용자에게 다양한 업체 정보를 제공하며, 편리한 비교 및 예약을 지원합니다.</li>
                <li>서비스 이용 과정에서 발생하는 문제(예약 변경, 취소, 품질 문제 등)에 대한 책임은
                    해당 업체에 있으며, 고객은 업체와 직접 해결해야 합니다.
                </li>
                <li>고객은 본 서비스를 이용하는 과정에서 타인의 권리를 침해하거나 부적절한 행위를 해서는 안 됩니다.</li>
            </ul>

            <div className="text-[28px] font-semibold mt-10 ml-20">2. 서비스 이용</div>
            <ul className="list-disc list-inside text-[20px] text-gray-800 space-y-1 ml-20">
                <li>고객은 본 서비스를 통해 스튜디오, 드레스, 메이크업 관련 정보를 열람하고,
                    업체와 직접 상담 또는 예약을 진행할 수 있습니다.</li>
                <li>서비스 이용은 회원가입 없이도 가능하나, 일부 기능은 회원가입 후 이용이 가능합니다.</li>
                <li>제공되는 정보는 업체에서 등록한 내용을 기반으로 하며, 본 서비스는 정보의 정확성을 보장하지 않습니다.</li>
            </ul>
            <div className="text-[28px] font-semibold mt-10 ml-20">3. 책임과 의무</div>
            <ul className="list-disc list-inside text-[20px] text-gray-800 space-y-1 ml-20">
                <li>본 서비스는 중개 역할을 수행하며, 업체와 고객 간의 계약 또는 거래에 직접 관여하지 않습니다.</li>
                <li>서비스 이용 과정에서 발생하는 문제(예약 변경, 취소, 품질 문제 등)에 대한 책임은
                    해당 업체에 있으며, 고객은 업체와 직접 해결해야 합니다.
                </li>
                <li>고객은 본 서비스를 이용하는 과정에서 타인의 권리를 침해하거나 부적절한 행위를 해서는 안 됩니다.</li>
            </ul>

            <div className="text-[28px] font-semibold mt-10 ml-20">4. 서비스 변경 및 중단</div>
            <ul className="list-disc list-inside text-[20px] text-gray-800 space-y-1 ml-20">
                <li>본 서비스는 지속적인 운영을 위해 필요에 따라 일부 기능을 변경하거나 중단할 수 있습니다.<br />
                    불가피한 사정으로 인해 서비스가 중단될 경우, 사전에 공지하도록 하겠습니다.</li>
                <li>서비스 이용 과정에서 발생하는 문제(예약 변경, 취소, 품질 문제 등)에 대한 책임은
                    해당 업체에 있으며, 고객은 업체와 직접 해결해야 합니다.
                </li>
                <li>고객은 본 서비스를 이용하는 과정에서 타인의 권리를 침해하거나 부적절한 행위를 해서는 안 됩니다.</li>
            </ul>

            <div className="text-[28px] font-semibold mt-10 ml-20">5. 개인정보 보호</div>
            <ul className="list-disc list-inside text-[20px] text-gray-800 space-y-1 ml-20">
                <li>고객의 개인정보는 관련 법령에 따라 보호되며,
                    자세한 내용은 개인정보처리방침을 참고하시기 바랍니다.</li>
                <li>서비스 이용 과정에서 제공된 개인정보는 원활한 서비스 제공을 위한 목적 외에는 사용되지 않습니다.<br />
                </li>
            </ul>
            <div className="text-[28px] font-semibold mt-10 ml-20">6. 기타</div>
            <ul className="list-disc list-inside text-[20px] text-gray-800 space-y-1 ml-20">
                <li>본 약관은 서비스 운영 정책에 따라 변경될 수 있으며, 변경 시 공지사항을 통해 안내됩니다.</li>
                <li>서비스 이용과 관련한 문의는 고객센터를 통해 가능합니다.</li>
            </ul>
            <div className="text-[24px] font-semibold mt-8 mb-8 ml-20">(최종 업데이트:  년  월  일)</div>
        </div>

    );
}

// 개인정보처리방침 페이지
export function Privacy() {
    return (
        <div className="min-h-screen bg-white text-black flex flex-col items-start justify-center">
            <div className="text-[33px] font-semibold mt-[120px] mx-auto text-center">개인정보처리방침</div>
            <div className="text-[29px] font-semibold mt-[60px] ml-20">스,드,메(이하 "회사")는 고객님의 개인정보를 중요하게 생각하며, 관련 법령을 준수하여
                개인정보를 보호하고 안전하게 관리하고 있습니다.<br></br> 본 개인정보처리방침은 고객님께
                제공하는 서비스에서 개인정보가 어떻게 수집, 이용, 보관, 보호되는지를 설명합니다.
            </div>
            <div className="text-[28px] text-left font-semibold mt-10 ml-20">1. 수집하는 개인정보 및 수집 방법</div>
            <div className="text-[25px] ml-20">회사는 다음과 같은 개인정보를 수집할 수 있습니다.</div>
            <div className="text-[25px] font-semibold mt-2 ml-20">1-1. 회원가입 및 서비스 이용 시</div>
            <ul className="list-disc list-inside text-[20px] text-gray-800 space-y-1 ml-20">
                <li>필수정보: 이름, 휴대전화번호, 이메일 주소</li>
                <li>선택정보: 프로필 사진, 관심 업체 정보</li>
            </ul>

            <div className="text-[25px] font-semibold mt-2 ml-20">1-2. 서비스 이용 과정에서 자동으로 수집되는 정보</div>
            <ul className="list-disc list-inside text-[20px] text-gray-800 space-y-1 ml-20">
                <li>접속 로그, IP 주소, 쿠키, 기기 정보</li>
            </ul>


            <div className="text-[25px] font-semibold mt-2 ml-20">1-3. 고객 문의 및 상담 시</div>
            <ul className="list-disc list-inside text-[20px] text-gray-800 space-y-1 ml-20">
                <li>문의 내용, 연락처</li>
            </ul>

            <div className="text-[28px] font-semibold mt-10 ml-20">2. 개인정보의 이용 목적</div>
            <div className="text-[25px] ml-20">회사는 수집한 개인정보를 다음과 같은 목적으로 이용합니다.</div>
            <ul className="list-disc list-inside text-[20px] text-gray-800 space-y-1 ml-20">
                <li>회원 가입 및 서비스 이용 관리</li>
                <li>업체 상담 및 예약 서비스 제공</li>
                <li>고객 문의 응대 및 서비스 개선</li>
                <li>마케팅 및 프로모션 정보 제공 (선택 동의 시)</li>
                <li>서비스 이용 통계 분석 및 시스템 보안 유지</li>
            </ul>

            <div className="text-[28px] font-semibold mt-10 ml-20">3. 개인정보의 보관 및 파기</div>
            <ul className="list-disc list-inside text-[20px] text-gray-800 space-y-1 ml-20">
                <li>수집된 개인정보는 이용 목적이 달성된 후, 관련 법령에 따라 일정 기간 보관 후 안전하게 삭제됩니다.</li>
                <li>법령에 의해 보관해야 하는 정보는 해당 기간 동안 보관 후 파기됩니다.</li>
                <li>개인정보 파기 시, 전자적 파일 형태는 영구 삭제하며, 문서 형태는 파쇄 또는 소각 처리합니다.</li>
            </ul>

            <div className="text-[28px] font-semibold mt-10 ml-20">4. 개인정보 제공 및 위탁</div>
            <ul className="list-disc list-inside text-[20px] text-gray-800 space-y-1 ml-20">
                <li>회사는 원칙적으로 고객님의 개인정보를 제3자에게 제공하지 않습니다.</li>
                <li>필요한 경우 고객님의 동의를 받은 후 특정 업체에 정보를 제공할 수 있습니다.</li>
                <li>신뢰할 수 있는 외부 업체에 개인정보 처리를 위탁할 수 있으며, 이 경우 안전한 관리를 보장합니다.</li>
            </ul>

            <div className="text-[28px] font-semibold mt-10 ml-20">5. 개인정보 보호 조치</div>
            <ul className="list-disc list-inside text-[20px] text-gray-800 space-y-1 ml-20">
                <li>회사는 개인정보 보호를 위해 보안 시스템을 갖추고 있으며, 접근 권한을 제한하여 관리하고 있습니다.</li>
                <li>개인정보 유출을 방지하기 위해 정기적인 보안 점검을 실시합니다.</li>
            </ul>

            <div className="text-[28px] font-semibold mt-10 ml-20">6. 이용자의 권리 및 행사 방법</div>
            <ul className="list-disc list-inside text-[20px] text-gray-800 space-y-1 ml-20">
                <li>고객님은 언제든지 본인의 개인정보를 조회, 수정, 삭제할 수 있으며, 회원 탈퇴를 요청할 수 있습니다.</li>
                <li>개인정보 관련 문의는 고객센터를 통해 접수할 수 있으며, 회사는 신속하게 처리하겠습니다.</li>
            </ul>

            <div className="text-[28px] font-semibold mt-10 ml-20">7. 개인정보처리방침 변경</div>
            <p className="text-[20px] text-gray-800 ml-20 mb-4">
                본 개인정보처리방침은 법령 및 정책 변경에 따라 수정될 수 있으며, 변경 사항은 홈페이지를 통해 사전 공지합니다.
            </p>
            <div className="text-[24px] font-semibold mt-8 mb-8 ml-20">(최종 업데이트:  년  월  일)</div>
        </div>
    );
}

// 오시는길 페이지
export function Location() {
    return (
        <div className="min-h-screen bg-white text-black flex flex-col items-center py-10">
            {/* 좌우 배치: 로고+타이틀 + 본점 정보 */}
            <div className="flex items-center gap-8 mb-10 px-10">
                {/* 좌측 로고 + "오시는 길" */}
                <div className="flex flex-col items-start">
                    <img src={Lacheln} alt="Lacheln Icon" className="w-[170px] h-[40px] mb-2" />
                    <div className="text-[34px] font-bold">오시는 길</div>
                </div>
                {/* 본점 타이틀 */}
                <div className="text-[46px] font-extrabold">대구 본점</div>
            </div>

            {/* 좌우 배치: 정보 + 지도 */}
            <div className="flex gap-16 px-10">
                {/* 좌측 정보 */}
                <div className="flex flex-col items-start">
                    <div className="flex items-center mb-8">
                        <img src={Schedule} alt="Schedule Icon" className="w-[45px] h-[45px] mr-3" />
                        <div>
                            <div className="text-[#e667da] text-[24px] font-semibold">BUSINESS HOUR</div>
                            <div className="text-[24px]">10:00~20:00</div>
                        </div>
                    </div>

                    <div className="flex items-center mb-8">
                        <img src={Call2} alt="Call Icon" className="w-[45px] h-[45px] mr-3" />
                        <div>
                            <div className="text-[#e667da] text-[24px] font-semibold">TEL</div>
                            <div className="text-[24px]">010-1234-5678</div>
                        </div>
                    </div>

                    <div className="flex items-center mb-8">
                        <img src={Home} alt="Home Icon" className="w-[45px] h-[45px] mr-3" />
                        <div>
                            <div className="text-[#e667da] text-[24px] font-semibold">ADDRESS</div>
                            <div className="text-[24px]">대구광역시 북구 복현로 35</div>
                        </div>
                    </div>
                </div>

                {/* 우측 지도 */}
                <div className="shadow-md border border-gray-300 rounded-lg overflow-hidden">
                    <iframe
                        title="map"
                        width="540"
                        height="460"
                        style={{ border: 0 }}
                        src="https://www.google.com/maps?q=대구광역시+북구+복현로+35&output=embed"
                        allowFullScreen
                    ></iframe>
                </div>
            </div>
        </div>
    );
}

// 회사 네비게이션
export default function CompanyNav() {
    return (
        <div className="flex space-x-4">
            <a href="/about">회사소개</a>
            <a href="/terms">이용약관</a>
            <a href="/privacy">개인정보처리방침</a>
            <a href="/location">오시는 길</a>
        </div>
    );
}