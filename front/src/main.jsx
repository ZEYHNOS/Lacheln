import React from "react";
import ReactDOM from "react-dom/client";
import App from "./App.jsx"; // App.jsx 파일 가져오기
import "./index.css"; // Tailwind가 적용된 CSS 가져오기

ReactDOM.createRoot(document.getElementById("root")).render(
    <React.StrictMode>
        <App />
    </React.StrictMode>
);
``