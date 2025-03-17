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
    const authPages = ["/login", "/register/user", "/register/company", "/register/user/form", "/register/company/form", "/register/success"];
    const isAuthPage = authPages.includes(location.pathname);

    useEffect(() => {
        document.title = "Lächeln";
    }, []);

    return (
        <div>
            {!isAuthPage && <Header />}
            {children}
            {!isAuthPage && <Footer />}
        </div>
    );
}

function App() {
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
