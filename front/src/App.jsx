import React, { useEffect } from "react";
import { BrowserRouter as Router, Route, Routes, useLocation } from "react-router-dom";
import Header from "./components/Tool/Header";
import Footer from "./components/Tool/Footer";
import LoginPage from "./components/Login/LoginPage";
import UserRegisterPage from "./components/Login/Register/UserRegisterPage";
import UserRegisterForm from "./components/Login/Register/UserRegisterForm";
import CompanyRegisterPage from "./components/Login/Register/CompanyRegisterPage";
import CompanyRegisterForm from "./components/Login/Register/CompanyRegisterForm";
import RegistrationSuccess from "./components/Login/Register/RegistrationSuccess";
import CompanyHeader from "./components/Company/Basic/CompanyHeader";
import CompanySidebar from "./components/Company/Basic/CompanySidebar";
import MainCompany from "./components/Company/MainCompany";
import ModifyInfo from "./components/Company/Companyinfo/Modifyinfo";
import Statistics from "./components/Company/Companyinfo/Statistics";
import ProductManagement from "./components/Company/Management/Product/Product";
import OrderManagement from "./components/Company/Management/Order";
import ReviewManagement from "./components/Company/Management/Review";
import Collaboration from "./components/Company/Work/Collaboration";
import Messenger from "./components/Company/Work/Messenger";
import Notification from "./components/Company/Work/Notification";
import Schedule from "./components/Company/Work/Schedule";
import AddProduct from "./components/Company/Management/Product/AddProduct.jsx";
import MainPage from "./components/User/Mainpage/MainPage.jsx"
import Brand from "./components/User/BrandPage/Brand.jsx"
import Product from "./components/User/ProductPage/Product.jsx"
import Package from "./components/User/PackagePage/Package.jsx"
import Event from "./components/User/EventPage/Event.jsx"
import Community from "./components/User/CommunityPage/Community.jsx"
import Community from "./components/User/CommunityPage/BoardPage.jsx"
import Support from "./components/User/SupportPage/Support.jsx"
import CompanyNav, { About, Terms, Privacy, Location } from "./components/Tool/CompanyNavgation/CompanyNav.jsx";


// `CompanyLayout`을 따로 분리하여 회사 관련 페이지를 그룹화
function CompanyLayout({ children }) {
    return (
        <div className="min-h-screen flex flex-col bg-white">
            <CompanyHeader className="h-12 min-h-[48px] max-h-[48px] flex-shrink-0" />

            <div className="flex flex-1 overflow-auto">
                <div className="w-60 flex flex-col min-h-full overflow-y-auto overflow-x-hidden flex-shrink-0 bg-gray-100 border-r border-gray-300 box-border">
                    <CompanySidebar />
                </div>

                <div className="flex flex-grow p-4 bg-white min-h-full overflow-auto">
                    {children}
                </div>
            </div>

            <Footer className="h-12 min-h-[48px] max-h-[48px] flex-shrink-0 mt-auto" />
        </div>
    );
}
// 유저 관련 페이지(로그인 화면도 포함)
function Layout({ children }) {
    const location = useLocation();
    const authPages = ["/login", "/register/user", "/register/company", "/register/user/form", "/register/company/form", "/register/success"];
    const isAuthPage = authPages.includes(location.pathname);
    const isCompanyPage = location.pathname.startsWith("/company");

    useEffect(() => {
        document.title = "Lächeln";
    }, []);

    return (
        <div>
            {/* 기본 페이지에서 Header와 Footer 관리 */}
            {isCompanyPage ? null : !isAuthPage && <Header />}
            
            <div className="flex">
                <div className="flex-grow">{children}</div>
            </div>

            {!isAuthPage && <Footer />}
        </div>
    );
}

function App() {
    return (
        <Router>
            <Routes>
                {/* ✅ /user 페이지 그룹화 */}
                {/* 메인 페이지 */}
                <Route path="/" element={<Layout><MainPage /></Layout>} />
                {/* 로그인창 */}
                <Route path="/login" element={<Layout><LoginPage /></Layout>} />
                {/* 유저 로컬 회원가입 */}
                <Route path="/register/user" element={<Layout><UserRegisterPage /></Layout>} />
                <Route path="/register/user/form" element={<Layout><UserRegisterForm /></Layout>} />
                {/* 회사 로컬 회원가입 */}
                <Route path="/register/company" element={<Layout><CompanyRegisterPage /></Layout>} />
                <Route path="/register/company/form" element={<Layout><CompanyRegisterForm /></Layout>} />
                {/* 회원가입 성공 */}
                <Route path="/register/success" element={<Layout><RegistrationSuccess /></Layout>} />
                {/* 브랜드 페이지 */}
                <Route path="/brand" element={<Layout><Brand /></Layout>} />
                {/* 상품 페이지 */}
                <Route path="/product" element={<Layout><Product /></Layout>} />
                {/* 패키지 페이지 */}
                <Route path="/package" element={<Layout><Package /></Layout>} />
                {/* 이벤트 페이지 */}
                <Route path="/event" element={<Layout><Event /></Layout>} />
                {/* 커뮤니티 페이지 */}
                <Route path="/community" element={<Layout><Community /></Layout>} />
                {/* 고객지원 */}
                <Route path="/support" element={<Layout><Support /></Layout>} />
                {/* 회사관련 네비게이션 */}
                <Route path="/about" element={<Layout><About /></Layout>} />
                <Route path="/terms" element={<Layout><Terms /></Layout>} />
                <Route path="/privacy" element={<Layout><Privacy /></Layout>} />
                <Route path="/location" element={<Layout><Location /></Layout>} />

                {/* ✅ /company 페이지 그룹화 및 Nested Routes 적용 */}
                {/* 회사 메인페이지 */}
                <Route path="/company" element={<CompanyLayout><MainCompany /></CompanyLayout>} />
                {/* 가게 정보 페이지(정보수정, 통계) */}
                <Route path="/company/modifyinfo" element={<CompanyLayout><ModifyInfo /></CompanyLayout>} />
                <Route path="/company/statistics" element={<CompanyLayout><Statistics /></CompanyLayout>} />
                {/* 관리페이지(상품, 주문, 리뷰) */}
                <Route path="/company/product" element={<CompanyLayout><ProductManagement /></CompanyLayout>} />
                <Route path="/company/product/add" element={<CompanyLayout><AddProduct/></CompanyLayout>} />
                <Route path="/company/order" element={<CompanyLayout><OrderManagement /></CompanyLayout>} />
                <Route path="/company/review" element={<CompanyLayout><ReviewManagement /></CompanyLayout>} />
                {/* 업무페이지(협업, 메신저, 알림, 일정) */}
                <Route path="/company/collaboration" element={<CompanyLayout><Collaboration /></CompanyLayout>} />
                <Route path="/company/messenger" element={<CompanyLayout><Messenger /></CompanyLayout>} />
                <Route path="/company/notification" element={<CompanyLayout><Notification /></CompanyLayout>} />
                <Route path="/company/schedule" element={<CompanyLayout><Schedule /></CompanyLayout>} />
            </Routes>
        </Router>
    );
}

export default App;
