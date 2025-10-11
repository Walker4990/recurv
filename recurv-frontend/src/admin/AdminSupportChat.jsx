import React, { useEffect, useRef, useState } from "react";
import { useLocation } from "react-router-dom";
import SockJS from "sockjs-client";
import { Client } from "@stomp/stompjs";
import AdminHeader from "../components/AdminHeader";

export default function AdminSupportChat() {
    const location = useLocation();
    const partnerNo = new URLSearchParams(location.search).get("partnerNo"); // ✅ URL에서 partnerNo 추출
    const [messages, setMessages] = useState([]);
    const [input, setInput] = useState("");

    // STOMP client를 useRef로 관리 (재렌더링 영향 없음)
    const clientRef = useRef(null);

    useEffect(() => {
        if (!partnerNo) return;

        // 1. DB에서 기존 대화 불러오기
        fetch(`http://localhost:8080/api/support/${partnerNo}`)
            .then((res) => res.json())
            .then((data) => {
                console.log("💬 불러온 기존 대화:", data);
                setMessages(data);
            })
            .catch((err) => console.error("대화 불러오기 실패:", err));

        // 2. WebSocket 연결
        const socket = new SockJS("http://localhost:8080/ws");
        const client = new Client({
            webSocketFactory: () => socket,
            reconnectDelay: 5000,
            debug: (msg) => console.log("STOMP:", msg),
            onConnect: () => {
                console.log("✅ Admin connected to /topic/support/" + partnerNo);
                client.subscribe(`/topic/support/${partnerNo}`, (msg) => {
                    const data = JSON.parse(msg.body);
                    setMessages((prev) => [...prev, data]);
                });
            },
        });

        client.activate();
        clientRef.current = client;

        return () => {
            client.deactivate();
            clientRef.current = null;
        };
    }, [partnerNo]);


    // WebSocket(STOMP) 메시지 전송 함수
    const sendMessage = () => {
        if (!input.trim() || !clientRef.current) return;

        const message = {
            partnerNo,
            sender: "admin",
            content: input,
        };

        console.log("📨 Sending message:", message);

        // fetch → STOMP publish 로 변경
        clientRef.current.publish({
            destination: "/app/chat.send",
            body: JSON.stringify(message),
        });

        setInput("");
    };

    return (
        <div className="min-h-screen bg-gray-50">
            <AdminHeader />
            <main className="max-w-3xl mx-auto bg-white rounded-xl shadow-md p-6 mt-8">
                <h1 className="text-2xl font-bold mb-4">💬 파트너 #{partnerNo} 문의 상담</h1>

                {/* 메시지 리스트 */}
                <div className="h-96 overflow-y-auto border p-4 rounded-lg mb-4 bg-gray-50">
                    {messages.map((msg, idx) => (
                        <div
                            key={idx}
                            className={`mb-2 flex ${
                                msg.sender === "admin"
                                    ? "justify-end"
                                    : "justify-start"
                            }`}
                        >
                            <div
                                className={`px-4 py-2 rounded-lg ${
                                    msg.sender === "admin"
                                        ? "bg-blue-500 text-white"
                                        : "bg-gray-200 text-gray-800"
                                }`}
                            >
                                {msg.content}
                            </div>
                        </div>
                    ))}
                </div>

                {/* 입력창 */}
                <div className="flex space-x-3">
                    <input
                        value={input}
                        onChange={(e) => setInput(e.target.value)}
                        placeholder="메시지를 입력하세요..."
                        className="flex-1 border rounded-lg px-4 py-2"
                    />
                    <button
                        onClick={sendMessage}
                        className="bg-blue-500 text-white px-4 py-2 rounded-lg hover:bg-blue-600"
                    >
                        보내기
                    </button>
                </div>
            </main>
        </div>
    );
}
