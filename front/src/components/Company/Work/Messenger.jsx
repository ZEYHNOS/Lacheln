import React, { useEffect, useRef, useState } from "react";
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

const baseUrl = import.meta.env.VITE_API_BASE_URL;

export default function Chatting() {
  const [chatRooms, setChatRooms] = useState([]);
  const [messagesByRoom, setMessagesByRoom] = useState({});
  const [currentRoomId, setCurrentRoomId] = useState(null);
  const [sender, setSender] = useState({});
  const [receiver, setReceiver] = useState({});

  const stompClient = useRef(null);
  const subscribedRooms = useRef(new Set());
  const messageRef = useRef(null);
  const chatBoxRef = useRef(null);

  useEffect(() => {
    fetchChatRooms();
    return () => {
      disconnectWebSocket();
    }
  }, []);

  useEffect(() => {
    if (chatBoxRef.current) {
      chatBoxRef.current.scrollTo({
        top: chatBoxRef.current.scrollHeight,
        behavior: 'smooth',
      });
    }
  }, [messagesByRoom, currentRoomId]);

  const fetchChatRooms = async () => {
    try {
      const res = await fetch(`${baseUrl}/chatroom/list`, {
        method: "GET",
        headers: { "Content-Type": "application/json" },
        credentials: "include"
      });
      const result = await res.json();
      const rooms = result.data?.chatRooms || [];
      setChatRooms(rooms);
    } catch (err) {
      console.error("채팅방 목록 불러오기 실패:", err);
    }
  };

  const disconnectWebSocket = () => {
    if (stompClient.current) {
      stompClient.current.deactivate();
      console.log("WebSocket 연결 해제");
      stompClient.current = null;
      subscribedRooms.current = new Set();
    }
  };

  const connectWebSocket = (roomId) => {
    if (stompClient.current) {
      stompClient.current.deactivate();
    }

    const socket = new SockJS(`${baseUrl}/ws/chat`);
    const client = new Client({
      webSocketFactory: () => socket,
      reconnectDelay: 5000,
      debug: (str) => console.log(str)
    });

    client.onConnect = () => {
      console.log("WebSocket 연결 완료");

      client.subscribe(`/topic/chatroom.${roomId}`, (msg) => {
        const received = JSON.parse(msg.body);
        console.log("메시지 수신:", received);
        setMessagesByRoom(prev => ({
          ...prev,
          [roomId]: [...(prev[roomId] || []), received]
        }));
      });

      client.subscribe(`/topic/read.${roomId}`, (msg) => {
        const data = JSON.parse(msg.body);
        console.log("읽음 처리 수신:", data);
        setMessagesByRoom(prev => ({
          ...prev,
          [roomId]: (prev[roomId] || []).map(m =>
            data.message_ids.includes(m.messageId)
              ? { ...m, read: 'Y' }
              : m
          )
        }));
      });
    };

    client.activate();
    stompClient.current = client;
  };

  const joinRoom = async (roomId, userId, companyId, userName, companyName) => {
    const isCompanySender = true;  // 업체 쪽이 sender임

    setSender({
      id: isCompanySender ? companyId : userId,
      name: isCompanySender ? companyName : userName,
    });

    setReceiver({
      id: isCompanySender ? userId : companyId,
      name: isCompanySender ? userName : companyName,
    });

    setCurrentRoomId(roomId);

    if (!messagesByRoom[roomId]) {
      try {
        const res = await fetch(`${baseUrl}/chatroom/messages/${roomId}`, {
          method: "GET",
          headers: { "Content-Type": "application/json" },
          credentials: "include"
        });
        const data = await res.json();
        setMessagesByRoom(prev => ({
          ...prev,
          [roomId]: data.data.messages || []
        }));
      } catch (err) {
        console.error("채팅 메시지 불러오기 실패:", err);
      }
    }

    // 채팅방 선택 시 WebSocket 연결
    connectWebSocket(roomId);
    fetchChatRooms();
  };

  const sendMessage = () => {
    if (!messageRef.current || !currentRoomId || !stompClient.current) return;

    const content = messageRef.current.value.trim();
    if (!content) return;

    const msg = {
      chatRoomId: currentRoomId,
      message: content,
      read: "N",
      senderId: sender.id,
      receiverId: receiver.id,
      senderName: sender.name,
      receiverName: receiver.name,
      sendAt: new Date().toISOString()
    };

    // WebSocket 전송만 하고 로컬 상태에 바로 추가하지 않음
    // 서버에서 브로드캐스트된 메시지를 구독으로 받아서 표시
    stompClient.current.publish({
      destination: "/chat/send",
      body: JSON.stringify(msg),
    });

    // 입력창 비우기
    messageRef.current.value = "";
  };

  const formatTime = (sendAt) => {
    // 배열 형태의 날짜 처리 (LocalDateTime을 JSON으로 받은 경우)
    if (Array.isArray(sendAt) && sendAt.length >= 6) {
      const [year, month, day, hour, minute, second] = sendAt;
      const date = new Date(year, month - 1, day, hour, minute, second || 0);
      return `${date.toLocaleDateString('ko-KR')} ${date.toLocaleTimeString('ko-KR', {hour: '2-digit', minute: '2-digit'})}`;
    }
    
    // 문자열 형태의 날짜 처리
    const date = new Date(sendAt);
    if (isNaN(date)) {
      console.log('Invalid date:', sendAt); // 디버깅용
      return "시간 정보 없음";
    }
    
    return `${date.toLocaleDateString('ko-KR')} ${date.toLocaleTimeString('ko-KR', {hour: '2-digit', minute: '2-digit'})}`;
  };

  return (
    <div className="flex w-full h-full">
      {/* 채팅방 목록 - 스크롤바 스타일 개선 */}
      <div className="w-[500px] border-r p-4 flex flex-col">
        <div className="flex justify-between items-center mb-4">
          <h2 className="text-xl font-bold text-pp">채팅방 목록</h2>
          <button onClick={fetchChatRooms} className="p-2 bg-purple-300 text-white rounded hover:bg-purple-400 flex items-center justify-center" aria-label="새로고침">
            <svg width="24" height="24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" viewBox="0 0 24 24">
              <path d="M21 2v6h-6" />
              <path d="M3 12a9 9 0 0 1 15-7.7L21 8" />
              <path d="M3 12a9 9 0 0 0 15 7.7L21 16" />
            </svg>
          </button>
        </div>
        <div className="flex-1 overflow-y-scroll space-y-2" style={{
          scrollbarWidth: 'thin',
          scrollbarColor: '#a78bfa #f3f4f6'
        }}>
          <style dangerouslySetInnerHTML={{
            __html: `
              .overflow-y-scroll::-webkit-scrollbar {
                width: 8px;
              }
              .overflow-y-scroll::-webkit-scrollbar-track {
                background: #f3f4f6;
                border-radius: 4px;
              }
              .overflow-y-scroll::-webkit-scrollbar-thumb {
                background: #a78bfa;
                border-radius: 4px;
              }
              .overflow-y-scroll::-webkit-scrollbar-thumb:hover {
                background: #8b5cf6;
              }
            `
          }} />
          {chatRooms.map(room => (
            <div
              key={room.roomId}
              className={`p-3 rounded cursor-pointer hover:bg-purple-100 ${currentRoomId === room.roomId ? "bg-purple-200" : "bg-white"}`}
              onClick={() => joinRoom(room.roomId, room.userId, room.companyId, room.userName, room.companyName)}
            >
              <div className="font-semibold text-pp">{room.companyName}</div>
              <div className="text-sm text-pp">고객: {room.userName}</div>
              {room.unreadCount > 0 && (
                <div className="inline-block bg-red-500 text-white text-xs px-2 py-0.5 rounded-full mt-1">
                  {room.unreadCount}
                </div>
              )}
            </div>
          ))}
        </div>
      </div>

      {/* 채팅 메시지 영역 - 스크롤바 스타일 개선 */}
      <div className="w-full flex flex-col p-4">
        <h3 className="text-lg font-semibold text-purple-700 mb-2">메시지</h3>
        <div 
          ref={chatBoxRef} 
          className="flex-1 overflow-y-scroll bg-gray-50 p-3 border rounded mb-3"
          style={{
            scrollbarWidth: 'thin',
            scrollbarColor: '#a78bfa #f3f4f6',
            minHeight: '400px',
            maxHeight: '500px'
          }}
        >
          <style dangerouslySetInnerHTML={{
            __html: `
              .flex-1.overflow-y-scroll::-webkit-scrollbar {
                width: 12px;
              }
              .flex-1.overflow-y-scroll::-webkit-scrollbar-track {
                background: #f3f4f6;
                border-radius: 6px;
              }
              .flex-1.overflow-y-scroll::-webkit-scrollbar-thumb {
                background: #a78bfa;
                border-radius: 6px;
                border: 2px solid #f3f4f6;
              }
              .flex-1.overflow-y-scroll::-webkit-scrollbar-thumb:hover {
                background: #8b5cf6;
              }
            `
          }} />
          {(messagesByRoom[currentRoomId] || []).map((msg, index) => {
            const isSent = msg.senderId === sender.id;
            return (
              <div
                key={msg.messageId || `temp-${index}`}
                className={`mb-2 p-2 rounded max-w-[35%] shadow-sm ${isSent ? "ml-auto bg-pink-100" : "bg-purple-100"}`}
              >
                <div className="font-semibold text-sm text-black">{msg.senderName}</div>
                <div className="my-1 text-black">{msg.message}</div>
                <div className="text-xs text-gray-600">{formatTime(msg.sendAt)}</div>
                <div className="text-xs text-right text-gray-500">
                  {isSent ? (msg.read === "Y" ? "✔ 읽음" : "⌛ 전송됨") : (msg.read === "N" ? "📨 안읽음" : "")}
                </div>
              </div>
            );
          })}
        </div>

        <div className="flex gap-2">
          <input
            ref={messageRef}
            type="text"
            placeholder="메시지를 입력하세요"
            className="flex-1 text-black border bg-white border-pp rounded p-2 focus:outline-none focus:ring-2 focus:ring-purple-400"
            onKeyDown={(e) => { if(e.key === "Enter") sendMessage(); }}
          />
          <button
            onClick={sendMessage}
            className="bg-purple-600 text-white px-4 rounded hover:bg-purple-700"
          >
            전송
          </button>
        </div>
      </div>
    </div>
  );
}