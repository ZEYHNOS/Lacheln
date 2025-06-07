import React from "react";
import AdminSidebar from "./AdminSidebar";
import Footer from "../Tool/Footer";

export default function AdminLayout({ children }) {
    return (
        <div className="min-h-screen flex bg-gray-50">
            <AdminSidebar />
            <div className="flex-1 ml-60 flex flex-col">
                <header className="h-20 flex items-center px-10 bg-white shadow-md border-b">
                    <h1 className="text-2xl font-bold text-[#845EC2]">관리자 페이지</h1>
                </header>
                <main className="flex-1 p-10">
                    <div className="bg-white rounded-2xl shadow-lg p-10 min-h-[60vh]">
                        {children}
                    </div>
                </main>
                <Footer />
            </div>
        </div>
    );
}
