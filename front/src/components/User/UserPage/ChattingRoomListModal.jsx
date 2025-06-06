import React, { useEffect, useState } from "react";
import ChatRoomModal from "./ChatRoomModal";

export default function ChatRoomListModal({ showModal, onClose }) {
  const [chatRooms, setChatRooms] = useState([]);
  const [selectedRoom, setSelectedRoom] = useState(null);

  useEffect(() => {
    loadChatRooms();
  }, []);

  const loadChatRooms = async () => {
    try {
      const res = await fetch("http://localhost:5050/chatroom/list", {
        method: "GET",
        headers: { "Content-Type": "application/json" },
        credentials: "include"
      });

      const result = await res.json();
      setChatRooms(result.data?.chatRooms || []);
    } catch (err) {
      console.error("채팅방 불러오기 실패:", err);
    }
  };

  const handleRoomClick = (room) => {
    setSelectedRoom(room);
  };

  const closeRoomModal = () => {
    setSelectedRoom(null);
  };

  return (
    <>
      {showModal && (
        <div className="fixed inset-0 bg-black bg-opacity-40 z-40 flex items-center justify-center">
          <div className="w-[400px] h-[600px] bg-white rounded shadow-lg p-4 flex flex-col">
            <div className="flex justify-between items-center mb-4">
              <h2 className="text-lg font-semibold text-purple-700">채팅방 목록</h2>
              <button onClick={onClose} className="text-sm text-gray-600">닫기</button>
            </div>

            <div className="flex-1 overflow-y-auto space-y-2">
              {chatRooms.map(room => (
                <div key={room.roomId}
                  className="relative p-3 border rounded shadow-sm bg-white hover:bg-purple-50"
                  onClick={() => handleRoomClick(room)}
                >
                  <div className="text-sm font-bold text-purple-800">{room.userName}</div>
                  <div className="text-xs text-gray-500 mb-6">상대 회사: {room.companyName}</div>
                  {room.unreadCount > 0 && (
                    <div className="absolute top-2 right-2 bg-red-500 text-white text-xs px-2 py-1 rounded-full">
                      {room.unreadCount}
                    </div>
                  )}
                </div>
              ))}
            </div>
          </div>
        </div>
      )}

      {/* 선택된 채팅방이 있다면 채팅 모달 열기 */}
      {selectedRoom && (
        <ChatRoomModal
          room={selectedRoom}
          onClose={closeRoomModal}
        />
      )}
    </>
  );
}
