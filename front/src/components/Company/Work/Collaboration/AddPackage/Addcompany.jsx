import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import apiClient from '../../../../../lib/apiClient';

function AddCompany({ onClose, onComplete }) {
    const [isModalVisible, setIsModalVisible] = useState(true);
    const [showPackageNameModal, setShowPackageNameModal] = useState(false);
    const [packageName, setPackageName] = useState('');
    const [searchEmail, setSearchEmail] = useState('');
    const [searchResults, setSearchResults] = useState([]);
    const [invitedUsers, setInvitedUsers] = useState([]);
    const [message, setMessage] = useState('');
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');
    const [myCompanyInfo, setMyCompanyInfo] = useState(null);
    const navigate = useNavigate();

    // 내 회사 정보 가져오기
    useEffect(() => {
        const fetchMyCompanyInfo = async () => {
            try {
                const res = await apiClient.get('/company/info', { withCredentials: true });
                if (res.data && res.data.data) {
                    setMyCompanyInfo(res.data.data);
                }
            } catch (err) {
                console.error('회사 정보 불러오기 실패:', err);
            }
        };
        
        fetchMyCompanyInfo();
    }, []);

    const handleSearch = async (value) => {
        setLoading(true);
        setError('');
        setSearchResults([]);
        try {
            const res = await apiClient.get(`/company/search/${value}`, { withCredentials: true });
            if (res.data && res.data.data) {
                // 내 회사인 경우 검색 결과에서 제외
                if (myCompanyInfo && myCompanyInfo.id === res.data.data.id) {
                    setSearchResults([]);
                    setError('자기 회사는 초대할 수 없습니다.');
                } else {
                    setSearchResults([
                        {
                            id: res.data.data.id,
                            email: res.data.data.email,
                            name: res.data.data.name
                        }
                    ]);
                }
            } else {
                setSearchResults([]);
                setError('검색 결과가 없습니다.');
            }
        } catch (err) {
            setSearchResults([]);
            setError('검색 중 오류가 발생했습니다.');
        } finally {
            setLoading(false);
        }
    };

    const handleInvite = (user) => {
        if (!invitedUsers.find(u => u.id === user.id)) {
            setInvitedUsers([...invitedUsers, user]);
            setMessage(`${user.name}님을 초대했습니다.`);
            setTimeout(() => setMessage(''), 2000);
        }
    };

    const handleRemoveInvite = (userId) => {
        setInvitedUsers(invitedUsers.filter(user => user.id !== userId));
    };

    const handleComplete = () => {
        setShowPackageNameModal(true);
    };

    const handlePackageRegister = async () => {
        try {
            const requestData = {
                package_name: packageName,
                cp_email1: invitedUsers[0].email,
                cp_email2: invitedUsers[1].email
            };
            
            const response = await apiClient.post('/package/register', requestData);
            console.log('패키지 등록 성공:', response.data);
            
            // 패키지 ID와 함께 초대된 사용자 정보를 전달
            const packageId = response.data.data.id;
            onComplete(invitedUsers, packageId);
            setIsModalVisible(false);
        } catch (err) {
            console.error('패키지 등록 실패:', err);
            setError('패키지 등록 중 오류가 발생했습니다.');
        }
    };

    const handleClose = () => {
        navigate('/company/collaboration');
    };

    return (
        <div>
            {isModalVisible && !showPackageNameModal && (
                <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-30 z-50">
                    <div className="bg-white rounded-lg shadow-lg p-8 w-full max-w-md relative">
                        <h2 className="text-xl font-bold mb-6 text-gray-800">패키지 초대</h2>
                        <button
                            className="absolute top-4 right-4 w-10 h-10 flex items-center justify-center bg-white border border-gray-200 rounded-lg shadow text-2xl text-black hover:bg-gray-100 focus:outline-none"
                            onClick={handleClose}
                            aria-label="닫기"
                        >
                            ×
                        </button>
                        <div className="flex mb-4">
                            <input
                                type="text"
                                placeholder="이메일로 검색"
                                value={searchEmail}
                                onChange={e => setSearchEmail(e.target.value)}
                                onKeyDown={e => e.key === 'Enter' && handleSearch(searchEmail)}
                                className="flex-1 px-3 py-2 border-2 border-violet-400 rounded-md focus:outline-none focus:ring-2 focus:ring-violet-400 bg-white text-gray-900 placeholder-gray-400"
                            />
                            <button
                                className="ml-2 px-4 py-2 bg-violet-500 text-white font-semibold rounded-md hover:bg-violet-600 transition-colors"
                                onClick={() => handleSearch(searchEmail)}
                                disabled={loading}
                            >
                                {loading ? '검색중...' : '검색'}
                            </button>
                        </div>
                        {error && <div className="mb-2 text-red-500 font-semibold text-sm">{error}</div>}
                        {message && <div className="mb-2 text-violet-600 font-semibold text-sm">{message}</div>}
                        {searchResults.length > 0 && (
                            <ul className="divide-y divide-gray-200 mb-4">
                                {searchResults.map(user => (
                                    <li key={user.id} className="flex items-center justify-between py-2">
                                        <span className="text-gray-800">{user.name} <span className="text-gray-400 text-sm">({user.email})</span></span>
                                        <button
                                            className={`px-3 py-1 rounded font-semibold text-white ml-2 ${invitedUsers.some(u => u.id === user.id) ? 'bg-gray-300 cursor-not-allowed' : 'bg-violet-500 hover:bg-violet-600'}`}
                                            onClick={() => handleInvite(user)}
                                            disabled={invitedUsers.some(u => u.id === user.id)}
                                        >
                                            {invitedUsers.some(u => u.id === user.id) ? '초대됨' : '초대'}
                                        </button>
                                    </li>
                                ))}
                            </ul>
                        )}
                        {invitedUsers.length > 0 && (
                            <div className="mt-6">
                                <h4 className="font-semibold mb-2 text-gray-700">초대된 사용자</h4>
                                <ul className="divide-y divide-gray-200">
                                    {invitedUsers.map(user => (
                                        <li key={user.id} className="flex items-center justify-between py-2">
                                            <span className="text-gray-800">{user.name} <span className="text-gray-400 text-sm">({user.email})</span></span>
                                            <button
                                                className="px-3 py-1 rounded font-semibold text-white bg-red-500 hover:bg-red-600 ml-2"
                                                onClick={() => handleRemoveInvite(user.id)}
                                            >
                                                제거
                                            </button>
                                        </li>
                                    ))}
                                </ul>
                            </div>
                        )}
                        <button
                            className="mt-8 w-full py-2 bg-violet-600 text-white font-bold rounded hover:bg-violet-700 transition-colors disabled:bg-gray-300 disabled:cursor-not-allowed"
                            onClick={handleComplete}
                            disabled={invitedUsers.length < 2}
                        >
                            완료
                        </button>
                    </div>
                </div>
            )}

            {/* 패키지명 입력 모달 */}
            {showPackageNameModal && (
                <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-30 z-50">
                    <div className="bg-white rounded-lg shadow-lg p-8 w-full max-w-md relative">
                        <h2 className="text-xl font-bold mb-6 text-gray-800">패키지명 설정</h2>
                        <input
                            type="text"
                            placeholder="패키지명을 입력하세요"
                            value={packageName}
                            onChange={(e) => setPackageName(e.target.value)}
                            className="w-full px-3 py-2 bg-white text-black border-2 border-violet-400 rounded-md focus:outline-none focus:ring-2 focus:ring-violet-400"
                        />
                        {error && <div className="mt-2 text-red-500 font-semibold text-sm">{error}</div>}
                        <div className="flex justify-end mt-6 space-x-4">
                            <button
                                onClick={() => setShowPackageNameModal(false)}
                                className="bg-white px-4 py-2 text-purple-600 hover:text-gray-800"
                            >
                                이전
                            </button>
                            <button
                                onClick={handlePackageRegister}
                                disabled={!packageName.trim()}
                                className="px-4 py-2 bg-violet-600 text-white rounded hover:bg-violet-700 disabled:bg-gray-300 disabled:cursor-not-allowed"
                            >
                                완료
                            </button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
}

export default AddCompany;