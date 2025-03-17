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

function Home() {
    return (
        <main className="p-6 flex flex-col items-center">
            <h1 className="text-3xl font-bold">메인페이지</h1>
        </main>
    );
}

function Layout({ children }) {
    const location = useLocation();
    const isAuthPage = location.pathname === "/login" || location.pathname === "/register/user" || location.pathname === "/register/company" || location.pathname === "/register/user/form" || location.pathname === "/register/company/form" || location.pathname === "/register/success"; // ✅ 로그인 & 회원가입 페이지 확인

    return (
        <div>
            {!isAuthPage && <Header />} {/* 로그인, 회원가입 페이지에서는 Header 제외 */}
            {children}
            {<Footer />} {/* 로그인, 회원가입 페이지에서는 Footer 포함 */}
        </div>
    );
}

function App() {
    useEffect(() => {
        document.title = "Lächeln";
    }, []);

    return (
        <Router>
            <Layout>
                <Routes>
                    <Route path="/" element={<Home />} />
                    <Route path="/login" element={<LoginPage />} />
                    <Route path="/register/user" element={<UserRegisterPage />} />
                    <Route path="/register/user/form" element={<UserRegisterForm />} />
                    <Route path="/register/company" element={<CompanyRegisterPage />} />
                    <Route path="/register/company/form" element={<CompanyRegisterForm />} />
                    <Route path="/register/success" element={<RegistrationSuccess />} /> 
                </Routes>
            </Layout>
        </Router>
    );
}

export default App;
