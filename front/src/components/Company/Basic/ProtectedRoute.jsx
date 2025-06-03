import { Navigate, useLocation } from 'react-router-dom';
import { useEffect, useState } from 'react';
import apiClient from '../../../lib/apiClient';

export default function CompanyProtectedRoute({ children }) {
    const [isCompany, setIsCompany] = useState(false);
    const [isLoading, setIsLoading] = useState(true);
    const location = useLocation();

    useEffect(() => {
        const checkCompanyAuth = async () => {
            try {
                const response = await apiClient.get('/auth/me');
                setIsCompany(response.data.data?.role === 'company');
            } catch (error) {
                setIsCompany(false);
            } finally {
                setIsLoading(false);
            }
        };

        checkCompanyAuth();
    }, []);

    if (isLoading) {
        return <div>로딩 중...</div>;
    }

    if (!isCompany) {
        // 로그인하지 않은 경우 로그인 페이지로 리다이렉트
        return <Navigate to="/login" state={{ from: location }} replace />;
    }

    return children;
} 