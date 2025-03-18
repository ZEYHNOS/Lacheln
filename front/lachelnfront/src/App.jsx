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

function Home() {
    return (
        <main className="p-6 flex flex-col items-center">
            <h1 className="text-3xl font-bold">메인페이지</h1>
        </main>
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
            {/* /company = companyheader+companysidebar+footer : /  = header+footer(필요사이트는 header표시 안함)*/}
            {isCompanyPage ? <CompanyHeader /> : !isAuthPage && <Header />}
            
            <div className="flex">
                {isCompanyPage && <CompanySidebar />}
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
                <Route path="/" element={<Layout><Home /></Layout>} />
                <Route path="/login" element={<Layout><LoginPage /></Layout>} />
                <Route path="/register/user" element={<Layout><UserRegisterPage /></Layout>} />
                <Route path="/register/user/form" element={<Layout><UserRegisterForm /></Layout>} />
                <Route path="/register/company" element={<Layout><CompanyRegisterPage /></Layout>} />
                <Route path="/register/company/form" element={<Layout><CompanyRegisterForm /></Layout>} />
                <Route path="/register/success" element={<Layout><RegistrationSuccess /></Layout>} />
                <Route path="/company" element={<Layout><MainCompany /></Layout>} />
            </Routes>
        </Router>
    );
}

export default App;
