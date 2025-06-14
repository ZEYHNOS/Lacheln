import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import apiClient from '../../../../lib/apiClient';

const baseUrl = import.meta.env.VITE_API_BASE_URL;

const UserVerifyPage = () => {
  const [password, setPassword] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handlePasswordChange = (e) => {
    setPassword(e.target.value);
    if (error) setError(''); // 에러 메시지 초기화
  };

  const handleVerify = async () => {
    if (!password.trim()) {
      setError('비밀번호를 입력해주세요.');
      return;
    }

    setIsLoading(true);
    setError('');

    try {
      const res = await apiClient.post(`${baseUrl}/user/verify`, {
        password: password
      }, {
        headers: { "Content-Type": "application/json" },
      });

      if (res.status === 200) {
        // 검증 성공 시 내 정보 수정 페이지로 이동
        alert('인증이 완료되었습니다. 정보 수정 페이지로 이동합니다.');
        navigate('/user/update');
      }
    } catch (err) {
      console.error("비밀번호 검증 실패:", err);
      if (err.response?.status === 401) {
        setError('비밀번호가 일치하지 않습니다.');
      } else {
        setError('검증 중 오류가 발생했습니다. 다시 시도해주세요.');
      }
    } finally {
      setIsLoading(false);
    }
  };

  const handleCancel = () => {
    alert('취소되었습니다. 이전 페이지로 돌아갑니다.');
    // navigate('/user');
  };

  const handleKeyPress = (e) => {
    if (e.key === 'Enter') {
      handleVerify();
    }
  };

  return (
    <div className="bg-white min-h-screen">
      <div className="w-[880px] mx-auto bg-white">
        {/* Header Section */}
        <div className="relative p-8 rounded-b-3xl">
          <div className="text-center">
            <h2 className="text-pp text-sm mb-1">보안 인증</h2>
            <h1 className="text-xl font-bold text-pp">본인 확인</h1>
            <p className="text-sm text-gray-600 mt-2">
              개인정보 보호를 위해 비밀번호를 다시 한 번 입력해주세요.
            </p>
          </div>
        </div>

        {/* Verification Form */}
        <div className="px-8 mt-12">
          <div className="max-w-md mx-auto">
            {/* Password Input Section */}
            <div className="mb-6">
              <label className="block text-pp font-medium mb-3">
                비밀번호 입력
              </label>
              <div className="relative">
                <input
                  type="password"
                  value={password}
                  onChange={handlePasswordChange}
                  onKeyPress={handleKeyPress}
                  placeholder="현재 비밀번호를 입력하세요"
                  className={`w-full px-4 py-3 border rounded-lg focus:outline-none focus:ring-2 focus:ring-purple-500 focus:border-transparent ${
                    error ? 'border-red-500' : 'border-gray-300'
                  }`}
                  disabled={isLoading}
                />
                {error && (
                  <p className="text-red-500 text-sm mt-2">{error}</p>
                )}
              </div>
            </div>

            {/* Security Notice */}
            <div className="bg-gray-50 p-4 rounded-lg mb-6">
              <div className="flex items-start">
                <div className="flex-shrink-0">
                  <svg className="w-5 h-5 text-blue-500 mt-0.5" fill="currentColor" viewBox="0 0 20 20">
                    <path fillRule="evenodd" d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-7-4a1 1 0 11-2 0 1 1 0 012 0zM9 9a1 1 0 000 2v3a1 1 0 001 1h1a1 1 0 100-2v-3a1 1 0 00-1-1H9z" clipRule="evenodd" />
                  </svg>
                </div>
                <div className="ml-3">
                  <h3 className="text-sm font-medium text-gray-900">보안 안내</h3>
                  <div className="mt-1 text-sm text-gray-700">
                    <ul className="list-disc list-inside space-y-1">
                      <li>개인정보 보호를 위한 추가 인증 단계입니다.</li>
                      <li>현재 계정의 비밀번호를 정확히 입력해주세요.</li>
                      <li>3회 이상 실패 시 계정이 일시적으로 제한될 수 있습니다.</li>
                    </ul>
                  </div>
                </div>
              </div>
            </div>

            {/* Action Buttons */}
            <div className="flex gap-3">
              <button
                onClick={handleVerify}
                disabled={isLoading || !password.trim()}
                className={`flex-1 py-3 px-6 rounded-lg font-medium transition-colors ${
                  isLoading || !password.trim()
                    ? 'bg-gray-300 text-gray-500 cursor-not-allowed'
                    : 'bg-purple-600 text-white hover:bg-purple-700'
                }`}
              >
                {isLoading ? (
                  <div className="flex items-center justify-center">
                    <svg className="animate-spin -ml-1 mr-3 h-5 w-5 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                      <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4"></circle>
                      <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                    </svg>
                    확인 중...
                  </div>
                ) : (
                  '확인'
                )}
              </button>
              <button
                onClick={handleCancel}
                disabled={isLoading}
                className="flex-1 bg-purple-200 text-gray-700 py-3 px-6 rounded-lg font-medium hover:bg-purple-300 transition-colors disabled:opacity-50"
              >
                취소
              </button>
            </div>
          </div>
        </div>

        {/* Footer Space */}
        <div className="h-16"></div>
      </div>
    </div>
  );
};

export default UserVerifyPage;