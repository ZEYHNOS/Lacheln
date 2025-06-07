import React, { useEffect } from "react";
import { BrowserRouter as Router, Route, Routes, useLocation } from "react-router-dom";
import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import "./App.css";
import Header from "./components/Tool/Header";
import Footer from "./components/Tool/Footer";
import LoginPage from "./components/Login/LoginPage";
import ReportPage from "./components/Report/ReportPage";
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
import Collaboration from "./components/Company/Work/Collaboration/Collaboration.jsx";
import Setproduct from "./components/Company/Work/Collaboration/AddPackage/Setproduct.jsx";
import Setpackage from "./components/Company/Work/Collaboration/AddPackage/Setpackage.jsx";
import ViewPackage from "./components/Company/Work/Collaboration/ViewPackage.jsx";
import EditPackage from "./components/Company/Work/Collaboration/EditPackage.jsx";
import Messenger from "./components/Company/Work/Messenger";
import Notification from "./components/Company/Work/Notification";
import Schedule from "./components/Company/Work/Schedule";
import AddProduct from "./components/Company/Management/Product/AddProduct.jsx";
import ViewProduct from "./components/Company/Management/Product/ViewProduct.jsx";
import EditProduct from "./components/Company/Management/Product/EditProduct.jsx";
import MainPage from "./components/User/Mainpage/MainPage.jsx"
import Cart from "./components/User/UserPage/Cart.jsx"
import ChoicePayment from "./components/User/UserPage/Payment/ChoicePayment.jsx";
import PaymentSuccess from "./components/User/UserPage/Payment/PaymentSuccess.jsx";
import Brand from "./components/User/BrandPage/Brand.jsx"
import Product from "./components/User/ProductPage/Product.jsx"
import Productdetail from "./components/User/ProductPage/Productdetail.jsx"
import Package from "./components/User/PackagePage/Package.jsx"
import PackageDetail from "./components/User/PackagePage/Packagedetail.jsx"
import Event from "./components/User/EventPage/Event.jsx"
import Support from "./components/User/SupportPage/Support.jsx"
import CompanyNav, { About, Terms, Privacy, Location } from "./components/Tool/CompanyNavgation/CompanyNav.jsx";
import CompanyProtectedRoute from "./components/Company/Basic/ProtectedRoute.jsx";
import BoardPage from "./components/User/CommunityPage/BoardPage";
import CreatePostPage from "./components/User/CommunityPage/CreatePostPage";
import AdminLayout from "./components/Admin/AdminLayout";
import AdminDashboard from "./components/Admin/AdminDashboard";

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
            <ToastContainer />
            <Routes>
                {/* /user 페이지 그룹화 */}
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
                {/* 유저 페이지 */}
                <Route path="/cart" element={<Layout><Cart /></Layout>} />
                <Route path="/cart/payment" element={<Layout><ChoicePayment /></Layout>} />
                <Route path="/cart/payment/success" element={<Layout><PaymentSuccess /></Layout>} />
                {/* 브랜드 페이지 */}
                <Route path="/brand" element={<Layout><Brand /></Layout>} />
                {/* 상품 페이지 */}
                <Route path="/product" element={<Layout><Product /></Layout>} />
                <Route path="/product/:category/:productid" element={<Layout><Productdetail /></Layout>} />
                {/* 패키지 페이지 */}
                <Route path="/package" element={<Layout><Package /></Layout>} />
                <Route path="/package/:id" element={<Layout><PackageDetail /></Layout>} />
                {/* 이벤트 페이지 */}
                <Route path="/event" element={<Layout><Event /></Layout>} />
                {/* 고객지원 */}
                <Route path="/support" element={<Layout><Support /></Layout>} />
                {/* 회사관련 네비게이션 */}
                <Route path="/about" element={<Layout><About /></Layout>} />
                <Route path="/terms" element={<Layout><Terms /></Layout>} />
                <Route path="/privacy" element={<Layout><Privacy /></Layout>} />
                <Route path="/location" element={<Layout><Location /></Layout>} />

                {/* 커뮤니티 게시판 페이지 */}
                <Route path="/community" element={<Layout><BoardPage /></Layout>} />
                <Route path="/create" element={<Layout><CreatePostPage /></Layout>} />

                {/* /company 페이지 그룹화 및 Nested Routes 적용 */}
                {/* 회사 메인페이지 */}
                <Route path="/company" element={
                    <CompanyProtectedRoute>
                        <CompanyLayout><MainCompany /></CompanyLayout>
                    </CompanyProtectedRoute>
                } />
                {/* 가게 정보 페이지(정보수정, 통계) */}
                <Route path="/company/modifyinfo" element={
                    <CompanyProtectedRoute>
                        <CompanyLayout><ModifyInfo /></CompanyLayout>
                    </CompanyProtectedRoute>
                } />
                <Route path="/company/statistics" element={
                    <CompanyProtectedRoute>
                        <CompanyLayout><Statistics /></CompanyLayout>
                    </CompanyProtectedRoute>
                } />
                {/* 관리페이지(상품, 주문, 리뷰) */}
                <Route path="/company/product" element={
                    <CompanyProtectedRoute>
                        <CompanyLayout><ProductManagement /></CompanyLayout>
                    </CompanyProtectedRoute>
                } />
                <Route path="/company/product/add" element={
                    <CompanyProtectedRoute>
                        <CompanyLayout><AddProduct/></CompanyLayout>
                    </CompanyProtectedRoute>
                } />
                <Route path="/company/product/:id" element={
                    <CompanyProtectedRoute>
                        <CompanyLayout><ViewProduct /></CompanyLayout>
                    </CompanyProtectedRoute>
                } />
                <Route path="/company/product/edit/:id" element={
                    <CompanyProtectedRoute>
                        <CompanyLayout><EditProduct /></CompanyLayout>
                    </CompanyProtectedRoute>
                } />
                <Route path="/company/order" element={
                    <CompanyProtectedRoute>
                        <CompanyLayout><OrderManagement /></CompanyLayout>
                    </CompanyProtectedRoute>
                } />
                <Route path="/company/review" element={
                    <CompanyProtectedRoute>
                        <CompanyLayout><ReviewManagement /></CompanyLayout>
                    </CompanyProtectedRoute>
                } />
                {/* 업무페이지(협업, 메신저, 알림, 일정) */}
                <Route path="/company/collaboration" element={
                    <CompanyProtectedRoute>
                        <CompanyLayout><Collaboration /></CompanyLayout>
                    </CompanyProtectedRoute>
                } />
                <Route path="/company/collaboration/setproduct" element={
                    <CompanyProtectedRoute>
                        <CompanyLayout><Setproduct /></CompanyLayout>
                    </CompanyProtectedRoute>
                } />
                <Route path="/company/collaboration/setproduct/:id" element={
                    <CompanyProtectedRoute>
                        <CompanyLayout><Setproduct /></CompanyLayout>
                    </CompanyProtectedRoute>
                } />
                <Route path="/company/collaboration/setpackage/:id" element={
                    <CompanyProtectedRoute>
                        <CompanyLayout><Setpackage /></CompanyLayout>
                    </CompanyProtectedRoute>
                } />
                <Route path="/company/collaboration/package/:id" element={
                    <CompanyProtectedRoute>
                        <CompanyLayout><ViewPackage /></CompanyLayout>
                    </CompanyProtectedRoute>
                } />
                <Route path="/company/collaboration/package/edit/:packageId" element={
                    <CompanyProtectedRoute>
                        <CompanyLayout><EditPackage /></CompanyLayout>
                    </CompanyProtectedRoute>
                } />
                <Route path="/company/messenger" element={
                    <CompanyProtectedRoute>
                        <CompanyLayout><Messenger /></CompanyLayout>
                    </CompanyProtectedRoute>
                } />
                <Route path="/company/notification" element={
                    <CompanyProtectedRoute>
                        <CompanyLayout><Notification /></CompanyLayout>
                    </CompanyProtectedRoute>
                } />
                <Route path="/company/schedule" element={
                    <CompanyProtectedRoute>
                        <CompanyLayout><Schedule /></CompanyLayout>
                    </CompanyProtectedRoute>
                } />

                 <Route
                        path="/admin"element={<AdminLayout>
                                <AdminDashboard />
                            </AdminLayout>
                        }
                />


                <Route path="/report" element={<ReportPage />} />
                <Route path="/report/:id" element={<ReportPage />} />
            </Routes>
        </Router>
    );
}

export default App;
