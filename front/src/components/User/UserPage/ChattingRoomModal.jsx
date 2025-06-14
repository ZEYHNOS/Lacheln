import React, { useEffect, useRef, useState } from "react";
import { Client } from "@stomp/stompjs";
import SockJS from "sockjs-client";

const baseUrl = import.meta.env.VITE_API_BASE_URL;

export default function ChattingRoomModal({ showModal, onClose }) {
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
    if (showModal) {
      fetchChatRooms();
    }
    return () => {
      disconnectWebSocket();
    };
  }, [showModal]);

  useEffect(() => {
    if (chatBoxRef.current) {
      chatBoxRef.current.scrollTo({
        top: chatBoxRef.current.scrollHeight,
        behavior: "smooth",
      });
    }
  }, [messagesByRoom, currentRoomId]);

  const disconnectWebSocket = () => {
    if (stompClient.current) {
      stompClient.current.deactivate();
      console.log("WebSocket 연결 해제됨");
      stompClient.current = null;
      subscribedRooms.current = new Set();
    }
  };

  const fetchChatRooms = async () => {
    try {
      const res = await fetch(`${baseUrl}/chatroom/list`, {
        method: "GET",
        headers: { "Content-Type": "application/json" },
        credentials: "include",
      });
      const result = await res.json();
      const rooms = result.data?.chatRooms || [];
      setChatRooms(rooms);
      connectWebSocket(rooms);
    } catch (err) {
      console.error("채팅방 목록 불러오기 실패:", err);
    }
  };

  const connectWebSocket = (rooms) => {
    if (stompClient.current) {
      stompClient.current.deactivate();
    }

    const socket = new SockJS(`${baseUrl}/ws/chat`);
    const client = new Client({
      webSocketFactory: () => socket,
      reconnectDelay: 5000,
      debug: (str) => console.log(str),
      onConnect: () => {
        console.log("WebSocket 연결 완료");
        rooms.forEach((room) => {
          const roomId = room.roomId;
          if (subscribedRooms.current.has(roomId)) return;

          client.subscribe(`/topic/chatroom.${roomId}`, (msg) => {
            const received = JSON.parse(msg.body);
            setMessagesByRoom((prev) => ({
              ...prev,
              [roomId]: [...(prev[roomId] || []), received],
            }));
          });

          client.subscribe(`/topic/read.${roomId}`, (msg) => {
            const data = JSON.parse(msg.body);
            setMessagesByRoom((prev) => ({
              ...prev,
              [roomId]: (prev[roomId] || []).map((m) =>
                data.message_ids.includes(m.messageId)
                  ? { ...m, read: "Y" }
                  : m
              ),
            }));
          });

          subscribedRooms.current.add(roomId);
        });
      },
    });

    client.activate();
    stompClient.current = client;
  };

  const joinRoom = async (roomId, userId, companyId, userName, companyName) => {
    const isUserSender = true;

    setSender({
      id: isUserSender ? userId : companyId,
      name: isUserSender ? userName : companyName,
    });

    setReceiver({
      id: isUserSender ? companyId : userId,
      name: isUserSender ? companyName : userName,
    });

    setCurrentRoomId(roomId);

    if (!messagesByRoom[roomId]) {
      try {
        const res = await fetch(
          `${baseUrl}/chatroom/messages/${roomId}`,
          {
            method: "GET",
            headers: { "Content-Type": "application/json" },
            credentials: "include",
          }
        );
        const data = await res.json();
        setMessagesByRoom((prev) => ({
          ...prev,
          [roomId]: data.data.messages || [],
        }));
      } catch (err) {
        console.error("채팅 메시지 불러오기 실패:", err);
      }
    }

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

    // WebSocket 전송
    stompClient.current.publish({
      destination: "/chat/send",
      body: JSON.stringify(msg),
    });

    // 로컬 상태에 바로 반영
    setMessagesByRoom(prev => ({
      ...prev,
      [currentRoomId]: [...(prev[currentRoomId] || []), msg],
    }));

    // 입력창 비우기
    messageRef.current.value = "";
  };

  const formatTime = (sendAt) => {
    if (Array.isArray(sendAt) && sendAt.length >= 6) {
      const [year, month, day, hour, minute, second] = sendAt;
      const date = new Date(year, month - 1, day, hour, minute, second || 0);
      return `${date.toLocaleDateString()} ${date.toLocaleTimeString([], {
        hour: "2-digit",
        minute: "2-digit",
      })}`;
    }

    const date = new Date(sendAt);
    return isNaN(date)
      ? "시간 정보 없음"
      : `${date.toLocaleDateString()} ${date.toLocaleTimeString([], {
          hour: "2-digit",
          minute: "2-digit",
        })}`;
  };

  if (!showModal) return null;

  return (
    <div className="fixed inset-0 bg-black bg-opacity-40 z-50 flex items-center justify-center">
      <div className="w-[900px] h-[600px] bg-white rounded shadow-lg flex">
        {/* 채팅방 목록 */}
        <div className="w-1/3 border-r p-4 flex flex-col">
          <div className="flex justify-between items-center mb-4">
            <h2 className="text-lg font-semibold text-purple-700">채팅방 목록</h2>
            <button
              onClick={fetchChatRooms}
              className="px-2 py-1 text-sm bg-purple-100 text-purple-700 rounded"
            >
              새로고침
            </button>
          </div>
          <div className="flex-1 overflow-y-auto space-y-2">
            {chatRooms.map((room) => (
              <div
                key={room.roomId}
                className={`relative p-3 border rounded shadow-sm cursor-pointer ${
                  currentRoomId === room.roomId ? "bg-purple-200" : "bg-white"
                } hover:bg-purple-50`}
                onClick={() =>
                  joinRoom(
                    room.roomId,
                    room.userId,
                    room.companyId,
                    room.userName,
                    room.companyName
                  )
                }
              >
                <div className="text-sm font-bold text-purple-800">
                  {room.userName}
                </div>
                <div className="text-xs text-gray-500">
                  상대 회사: {room.companyName}
                </div>
                {room.unreadCount > 0 && (
                  <div className="absolute top-2 right-2 bg-red-500 text-white text-xs px-2 py-1 rounded-full">
                    {room.unreadCount}
                  </div>
                )}
              </div>
            ))}
          </div>
        </div>

        {/* 채팅 영역 */}
        <div className="w-2/3 flex flex-col p-4">
          <h3 className="text-lg font-semibold text-purple-700 mb-2">메시지</h3>
          <div
            ref={chatBoxRef}
            className="flex-1 overflow-y-auto bg-gray-50 p-3 border rounded mb-3"
          >
            {(messagesByRoom[currentRoomId] || []).map((msg) => {
              const isSent = msg.senderId === sender.id;
              return (
                <div
                  key={msg.messageId}
                  className={`mb-2 p-2 rounded max-w-[35%] ${
                    isSent
                      ? "ml-auto bg-pink-100 shadow"
                      : "bg-purple-100 shadow"
                  }`}
                >
                  <div className="text-sm font-semibold">{msg.senderName}</div>
                  <div>{msg.message}</div>
                  <div className="text-xs text-gray-600">
                    {formatTime(msg.sendAt)}
                  </div>
                  <div className="text-xs text-right text-gray-500">
                    {isSent
                      ? msg.read === "Y"
                        ? "✔ 읽음"
                        : "⌛ 전송됨"
                      : msg.read === "N"
                      ? "📨 안읽음"
                      : ""}
                  </div>
                </div>
              );
            })}
          </div>

          {/* 입력창 */}
          <div className="flex items-center gap-2 mb-3">
            <input
              ref={messageRef}
              type="text"
              placeholder="메시지를 입력하세요"
              className="border border-gray-300 rounded p-2 flex-1 focus:ring-2 focus:ring-purple-500"
              onKeyDown={(e) => e.key === "Enter" && sendMessage()}
            />
            <button
              onClick={sendMessage}
              className="bg-purple-500 text-white px-4 py-2 rounded hover:bg-purple-600"
            >
              전송
            </button>
          </div>

          {/* 닫기 버튼 */}
          <button
            onClick={onClose}
            className="w-full bg-purple-300 text-white py-2 rounded hover:bg-purple-400"
          >
            닫기
          </button>
        </div>
      </div>
    </div>
  );
}
