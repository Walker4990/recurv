import React, { useEffect, useState } from "react";
import SockJS from "sockjs-client";
import { Client } from "@stomp/stompjs";
import UserHeader from "../components/UserHeader";

export default function SupportChat() {
    const [client, setClient] = useState(null);
    const [messages, setMessages] = useState([]);
    const [input, setInput] = useState("");
    const partnerNo = 3; // ✅ 실제 로그인된 partnerNo로 대체
    const sender = "partner"; // or "admin"

    useEffect(() => {
        const socket = new SockJS("http://localhost:8080/ws");
        const stompClient = new Client({
            webSocketFactory: () => socket,
            reconnectDelay: 5000,
            debug: (msg) => console.log("STOMP:", msg),
            onConnect: () => {
                console.log("✅ Connected to support channel");
                stompClient.subscribe(`/topic/support/${partnerNo}`, (msg) => {
                    const payload = JSON.parse(msg.body);
                    console.log("📩 새 메시지:", payload);
                    setMessages((prev) => [...prev, payload]);
                });
            },
        });

        stompClient.activate();
        setClient(stompClient);
        return () => stompClient.deactivate();
    }, []);

    const sendMessage = () => {
        if (!input.trim()) return;
        client.publish({
            destination: "/app/chat.send",
            body: JSON.stringify({
                partnerNo,
                content: input,
                sender, // "partner" or "admin"
            }),
        });
        setMessages((prev) => [
            ...prev,
            { sender, content: input, timestamp: new Date().toISOString() },
        ]);
        setInput("");
    };

    return (
        <div className="flex flex-col min-h-screen bg-gray-50">
            <UserHeader />
            <main className="flex-1 py-12 px-4">
                <div className="max-w-2xl mx-auto bg-white shadow-lg rounded-2xl p-8">
                    <h1 className="text-2xl font-bold mb-6 text-center">💬 실시간 문의</h1>

                    {/* ✅ 메시지 영역 */}
                    <div className="h-80 border rounded-lg p-4 overflow-y-auto mb-4 bg-gray-50">
                        {messages.length === 0 && (
                            <p className="text-gray-400 text-center mt-24">아직 대화가 없습니다.</p>
                        )}
                        {messages.map((m, i) => (
                            <div
                                key={i}
                                className={`my-2 flex ${
                                    m.sender === sender ? "justify-end" : "justify-start"
                                }`}
                            >
                                <div
                                    className={`px-3 py-2 rounded-xl shadow-sm ${
                                        m.sender === sender
                                            ? "bg-blue-500 text-white"
                                            : "bg-gray-200 text-gray-800"
                                    }`}
                                >
                                    {m.content}
                                </div>
                            </div>
                        ))}
                    </div>

                    {/* ✅ 입력 영역 */}
                    <div className="flex gap-2">
                        <input
                            type="text"
                            value={input}
                            onChange={(e) => setInput(e.target.value)}
                            onKeyDown={(e) => e.key === "Enter" && sendMessage()}
                            placeholder="메시지를 입력하세요..."
                            className="flex-1 border rounded-lg px-4 py-2"
                        />
                        <button
                            onClick={sendMessage}
                            className="bg-blue-500 text-white px-5 py-2 rounded-lg hover:bg-blue-600"
                        >
                            전송
                        </button>
                    </div>
                </div>
            </main>
        </div>
    );
}
