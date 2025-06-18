import React, { useEffect, useRef, useState } from "react";
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

const baseUrl = import.meta.env.VITE_API_BASE_URL;

export default function ChatRoomModal({ companyId, onClose }) {
  const [messages, setMessages] = useState([]);
  const [sender, setSender] = useState({});
  const [receiver, setReceiver] = useState({});
  const [saveRoomData, setSaveRoomData] = useState({});
  const stompClient = useRef(null);
  const messageRef = useRef(null);
  const chatBoxRef = useRef(null);

  useEffect(() => {
    joinRoom(companyId);
    return () => {
      disconnectWebSocket();
    };
  }, []);

  useEffect(() => {
    if (chatBoxRef.current) {
      chatBoxRef.current.scrollTop = chatBoxRef.current.scrollHeight;
    }
  }, [messages]);

  const disconnectWebSocket = () => {
    if (stompClient.current) {
      stompClient.current.deactivate();
      console.log("WebSocket 연결 해제");
      stompClient.current = null;
    }
  };

  const joinRoom = async (companyId) => {
    const isUserSender = true;

    const msgRes = await fetch(`${baseUrl}/chatroom/addroom/${companyId}`, {
      method: "GET",
      headers: { "Content-Type": "application/json" },
      credentials: "include"
    });

    const getRoomData = await msgRes.json();

    const roomData = getRoomData.data;
    setSaveRoomData(roomData);

    console.log(roomData);

    setSender({
      id: isUserSender ? roomData.userId : roomData.companyId,
      name: isUserSender ? roomData.userName : roomData.companyName
    });

    setReceiver({
      id: isUserSender ? roomData.companyId : roomData.userId,
      name: isUserSender ? roomData.companyName : roomData.userName
    });

    setMessages(roomData.messages || []);

    const socket = new SockJS(`${baseUrl}/ws/chat`);
    const client = new Client({
      webSocketFactory: () => socket,
      reconnectDelay: 5000,
      debug: str => console.log(str)
    });

    console.log(roomData.chatRoomId);

    client.onConnect = () => {
      client.subscribe(`/topic/chatroom.${roomData.chatRoomId}`, (msg) => {
        const received = JSON.parse(msg.body);
        console.log("메시지 수신:", received);

        setMessages(prev => {
          const exists = prev.some(m => {
            if (received.messageId && m.messageId) {
              return m.messageId === received.messageId;
            }
            return m.message === received.message &&
              m.senderId === received.senderId &&
              Math.abs(new Date(m.sendAt) - new Date(received.sendAt)) < 1000;
          });

          if (exists) {
            console.log("중복 메시지 무시:", received);
            return prev;
          }

          return [...prev, received];
        });
      });

      client.subscribe(`/topic/read.${roomData.chatRoomId}`, (msg) => {
        const data = JSON.parse(msg.body);
        console.log("읽음 처리 수신:", data);
        setMessages(prev => prev.map(m => (
          data.message_ids.includes(m.messageId)
            ? { ...m, read: 'Y' }
            : m
        )));
      });
    };

    client.activate();
    stompClient.current = client;
  };

  const sendMessage = () => {
    const content = messageRef.current.value.trim();
    if (!content || !stompClient.current) return;

    console.log(saveRoomData);

    const msg = {
      messageId: null,
      chatRoomId: saveRoomData.chatRoomId,
      message: content,
      read: "N",
      senderId: sender.id,
      receiverId: receiver.id,
      senderName: sender.name,
      receiverName: receiver.name,
      sendAt: new Date().toISOString()
    };

    console.log(msg);

    // WebSocket 전송만 하고 로컬 상태에 바로 추가하지 않음
    // 서버에서 브로드캐스트된 메시지를 구독으로 받아서 표시
    stompClient.current.publish({
      destination: "/chat/send",
      body: JSON.stringify(msg)
    });

    messageRef.current.value = "";
  };

  const formatTime = (sendAt) => {
    if (Array.isArray(sendAt) && sendAt.length >= 6) {
      const [year, month, day, hour, minute, second] = sendAt;
      const date = new Date(year, month - 1, day, hour, minute, second);
      return `${date.toLocaleDateString()} ${date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}`;
    }

    const date = new Date(sendAt);
    if (!isNaN(date)) {
      return `${date.toLocaleDateString()} ${date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}`;
    }

    return '';
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-40 z-50 flex items-center justify-center">
      <div className="w-[600px] h-[600px] bg-white rounded shadow-lg p-4 flex flex-col">
        <h3 className="text-lg font-semibold text-purple-700 mb-2">
          {receiver.name} 님과의 채팅
        </h3>

        <div ref={chatBoxRef} className="flex-1 overflow-y-auto bg-gray-50 p-3 border rounded mb-3 custom-scrollbar">
          {messages.map((msg, index) => {
            const isSent = msg.senderId === sender.id;
            return (
              <div
                key={msg.messageId || `temp-${index}`}
                className={`mb-2 p-2 rounded max-w-[40%] ${isSent ? 'ml-auto bg-pink-100 shadow-lg' : 'bg-purple-100 shadow-lg'}`}
              >
                <div className="text-sm font-semibold">{msg.senderName}</div>
                <div>{msg.message}</div>
                <div className="text-xs text-gray-600">{formatTime(msg.sendAt)}</div>
                <div className="text-xs text-right text-gray-500">
                  {isSent
                    ? (msg.read === "Y" ? "✔ 읽음" : "⌛ 전송됨")
                    : (msg.read === "N" ? "📨 안읽음" : "")}
                </div>
              </div>
            );
          })}
        </div>

        <div className="flex items-center gap-2 mb-3">
          <input
            type="text"
            ref={messageRef}
            placeholder="메시지를 입력하세요"
            className="border border-gray-300 bg-white text-blackrounded p-2 flex-1 focus:ring-2 focus:ring-purple-500 hover:border-purple-500"
            onKeyDown={(e) => e.key === "Enter" && sendMessage()}
          />
          <button
            className="bg-purple-500 text-white px-4 py-2 rounded hover:bg-purple-600"
            onClick={sendMessage}
          >
            전송
          </button>
        </div>

        <button
          className="bg-purple-300 text-white w-full py-2 rounded hover:bg-purple-400"
          onClick={onClose}
        >
          닫기
        </button>
      </div>
    </div>
  );
}