import React, { useEffect } from "react";
import { BrowserRouter as Router, Route, Routes, useLocation } from "react-router-dom";
import Header from "./components/Basic/Header";
import Footer from "./components/Basic/Footer";
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

function Home() {
    return (
        <main className="p-6 flex flex-col items-center">
            <h1 className="text-3xl font-bold">메인페이지</h1>
        </main>
    );
}

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
                {/* 기본 홈페이지 및 인증 관련 페이지 */}
                <Route path="/" element={<Layout><Home /></Layout>} />
                <Route path="/login" element={<Layout><LoginPage /></Layout>} />
                <Route path="/register/user" element={<Layout><UserRegisterPage /></Layout>} />
                <Route path="/register/user/form" element={<Layout><UserRegisterForm /></Layout>} />
                <Route path="/register/company" element={<Layout><CompanyRegisterPage /></Layout>} />
                <Route path="/register/company/form" element={<Layout><CompanyRegisterForm /></Layout>} />
                <Route path="/register/success" element={<Layout><RegistrationSuccess /></Layout>} />

                {/* ✅ /company 페이지 그룹화 및 Nested Routes 적용 */}
                <Route path="/company" element={<CompanyLayout><MainCompany /></CompanyLayout>} />
                <Route path="/company/modifyinfo" element={<CompanyLayout><ModifyInfo /></CompanyLayout>} />
                <Route path="/company/statistics" element={<CompanyLayout><Statistics /></CompanyLayout>} />
                <Route path="/company/product" element={<CompanyLayout><ProductManagement /></CompanyLayout>} />
                <Route path="/company/product/add" element={<CompanyLayout><AddProduct/></CompanyLayout>} />
                <Route path="/company/order" element={<CompanyLayout><OrderManagement /></CompanyLayout>} />
                <Route path="/company/review" element={<CompanyLayout><ReviewManagement /></CompanyLayout>} />
                <Route path="/company/collaboration" element={<CompanyLayout><Collaboration /></CompanyLayout>} />
                <Route path="/company/messenger" element={<CompanyLayout><Messenger /></CompanyLayout>} />
                <Route path="/company/notification" element={<CompanyLayout><Notification /></CompanyLayout>} />
                <Route path="/company/schedule" element={<CompanyLayout><Schedule /></CompanyLayout>} />
            </Routes>
        </Router>
    );
}

export default App;
