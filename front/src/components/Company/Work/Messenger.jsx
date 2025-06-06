import React, { useEffect, useRef, useState } from "react";
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

function Messenger() {
  const [chatRooms, setChatRooms] = useState([]);
  const [messages, setMessages] = useState([]);
  const [currentRoomId, setCurrentRoomId] = useState(null);
  const [sender, setSender] = useState({});
  const [receiver, setReceiver] = useState({});
  const stompClient = useRef(null);
  const messageRef = useRef(null);
  const chatBoxRef = useRef(null);

  useEffect(() => {
    loadChatRooms();

    // 페이지 언마운트 시 연결 종료
    return () => {
      if (stompClient.current) {
        stompClient.current.deactivate();
        stompClient.current = null;
        console.log("StompClient : 컴포넌트 언마운트 감지 후 연결 해제");
      }
    };
  }, []);

  useEffect(() => {
    loadChatRooms();
    if (chatBoxRef.current) {
      chatBoxRef.current.scrollTop = chatBoxRef.current.scrollHeight;
    }
  }, [messages]);

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

  const joinRoom = async (roomId, userId, companyId, userName, companyName) => {
    const isUserSender = false; // Company가 송신자이므로 false

    setSender({
      id: isUserSender ? userId : companyId,
      name: isUserSender ? userName : companyName
    });

    setReceiver({
      id: isUserSender ? companyId : userId,
      name: isUserSender ? companyName : userName
    });

    setCurrentRoomId(roomId);

    const msgRes = await fetch(`http://localhost:5050/chatroom/messages/${roomId}`, {
      method: "GET",
      headers: { "Content-Type": "application/json" },
      credentials: "include"
    });

    const msgData = await msgRes.json();
    console.log("메시지 불러오기 완료! \n", msgData.data.messages);
    setMessages(msgData.data.messages || []);

    const socket = new SockJS("http://localhost:5050/ws/chat");
    const client = new Client({
      webSocketFactory: () => socket,
      reconnectDelay: 5000,
      debug: str => console.log(str)
    });

    client.onConnect = () => {
      client.subscribe(`/topic/chatroom.${roomId}`, (msg) => {
        const received = JSON.parse(msg.body);
        setMessages(prev => [...prev, received]);
      });

      client.subscribe(`/topic/read.${roomId}`, (msg) => {
        const data = JSON.parse(msg.body);
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

    const msg = {
      messageId: null,
      chatRoomId: currentRoomId,
      message: content,
      read: "N",
      senderId: sender.id,
      receiverId: receiver.id,
      senderName: sender.name,
      receiverName: receiver.name,
      sendAt: new Date().toISOString()
    };

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
    <div className="p-6 w-full mx-auto">
        <div className="flex h-[700px]">
        
        {/* 왼쪽: 채팅방 목록 (고정 폭) */}
        <div className="w-[300px] border-r p-4 flex flex-col">
            <div className="flex justify-between items-center mb-4">
            <h2 className="text-lg font-semibold text-purple-700">채팅방 목록</h2>
            <button onClick={loadChatRooms} className="px-2 py-1 text-sm bg-purple-100 text-purple-700 rounded">
                새로고침
            </button>
            </div>
            <div className="flex-1 overflow-y-auto space-y-2 custom-scrollbar">
            {chatRooms.map(room => (
                <div key={room.roomId} className="relative p-3 border rounded shadow-sm bg-white hover:bg-purple-200"
                        onClick={() => joinRoom(room.roomId, room.userId, room.companyId, room.userName, room.companyName)}>
                    <div className="text-sm font-bold text-purple-800">{room.companyName}</div>
                    <div className="text-xs text-gray-500 mb-6">상대 유저: {room.userName}</div>
                    {room.unreadCount > 0 && (
                        <div className="absolute top-2 right-2 bg-red-500 text-white text-xs px-2 py-1 rounded-full">
                        {room.unreadCount}
                        </div>
                    )}
                </div>
            ))}
            </div>
        </div>

        {/* 오른쪽: 메시지 영역 (넓게) */}
        <div className="w-[1000px] flex-1 flex flex-col p-4">
            <h3 className="text-lg font-semibold text-purple-700 mb-2">메시지</h3>

            {/* 메시지 박스 */}
            <div
            ref={chatBoxRef}
            className="flex-1 overflow-y-auto p-3 border rounded mb-3 custom-scrollbar"
            >
            {messages.map(msg => {
                const isSent = msg.senderId === sender.id;
                return (
                <div
                    key={msg.messageId}
                    className={`mb-2 p-2 rounded max-w-[35%] ${isSent ? 'ml-auto bg-pink-100 shadow-lg' : 'bg-purple-100 shadow-lg'}`}
                >
                    <div className="text-sm font-semibold">{msg.senderName}</div>
                    <div>{msg.message}</div>
                    <div className="text-xs text-gray-600">
                    {formatTime(msg.sendAt)}
                    </div>
                    {/* 메시지 읽음 여부 */}
                    <div className="text-xs text-right text-gray-500">
                    {isSent
                        ? (msg.read === "Y" ? "✔ 읽음" : "⌛ 전송됨")
                        : (msg.read === "N" ? "📨 안읽음" : "")
                    }
                    </div>
                </div>
                );
            })}
            </div>

            {/* 메시지 입력창 */}
            <div className="flex items-center gap-2">
            <input
                type="text"
                ref={messageRef}
                placeholder="메시지를 입력하세요"
                className="border border-gray-300 rounded p-2 flex-1 focus:ring-2 focus:ring-purple-500 hover:border-purple-500"
                onKeyDown={(e) => e.key === "Enter" && sendMessage()}
            />
            <button
                className="bg-purple-500 text-white px-4 py-2 rounded hover:bg-purple-600"
                onClick={sendMessage}
            >
                전송
            </button>
            </div>
        </div>
        </div>
    </div>
    );

}

export default Messenger;
