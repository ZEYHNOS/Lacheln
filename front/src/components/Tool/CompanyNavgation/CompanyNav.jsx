// 회사소개 페이지
export function About() {
    return (
        <div className="min-h-screen bg-white text-black flex flex-col items-center justify-center">
            <h1 className="mb-4 block">회사소개 페이지</h1>
            <p className="block">현재 개발중에 있습니다.</p>
        </div>
    );
}
// 이용약관 페이지
export function Terms() {
    return (
        <div className="min-h-screen bg-white text-black flex flex-col items-center justify-center">
            <h1 className="mb-4 block">이용약관 페이지</h1>
            <p className="block">현재 개발중에 있습니다.</p>
        </div>
    );
}
// 개인정보처리방침 페이지
export function Privacy() {
    return (
        <div className="min-h-screen bg-white text-black flex flex-col items-center justify-center">
            <h1 className="mb-4 block">개인정보처리방침 페이지</h1>
            <p className="block">현재 개발중에 있습니다.</p>
        </div>
    );
}
// 오시는길 페이지
export function Location() {
    return (
        <div className="min-h-screen bg-white text-black flex flex-col items-center justify-center">
            <h1 className="mb-4 block">오시는 길 페이지</h1>
            <p className="block">현재 개발중에 있습니다.</p>
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
