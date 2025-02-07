import React, { useEffect } from "react";
import Header from "./components/Header";
import Footer from "./components/Footer";

function App() {
    useEffect(() => {
        document.title = "Lächeln"; // 브라우저 탭 제목 변경
    }, []);
    return (
        <div>
            <Header />
            <main className="p-6">
            </main>
            <Footer />
        </div>
    );
}

export default App; // 함수 실행이 아니라 함수 자체를 export
