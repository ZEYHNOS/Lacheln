import React, { useEffect } from "react";
import { BrowserRouter as Router, Route, Routes, useLocation } from "react-router-dom";
import Header from "./components/Header";
import Footer from "./components/Footer";
import LoginPage from "./components/Login/LoginPage";

function Home() {
    return (
        <main className="p-6 flex flex-col items-center">
            <h1 className="text-3xl font-bold">메인페이지</h1>
        </main>
    );
}

// ✅ 현재 경로를 확인하는 컴포넌트
function Layout({ children }) {
    const location = useLocation(); // 현재 경로 가져오기
    const isLoginPage = location.pathname === "/login"; // 로그인 페이지 여부 확인

    return (
        <div>
            {!isLoginPage && <Header />}
            {!isLoginPage &&  <Footer />}
            {children}
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
                </Routes>
            </Layout>
        </Router>
    );
}

export default App;
