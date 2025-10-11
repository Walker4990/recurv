import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import AdminHeader from "../components/AdminHeader";
import SockJS from "sockjs-client";
import { Client } from "@stomp/stompjs";
import { ArrowUpRight, ArrowDownRight, MessageCircle } from "lucide-react";

export default function Dashboard() {
    const navigate = useNavigate();

    const [stats, setStats] = useState([
        { title: "전체 구독 수", value: 120, change: 0 },
        { title: "이번 달 결제 건수", value: 58, change: 0 },
        { title: "미결제 청구서", value: 7, change: 0 },
        { title: "거래처 수", value: 35, change: 0 },
    ]);

    const [activityLogs, setActivityLogs] = useState([]);
    const [newSupportAlert, setNewSupportAlert] = useState(null);

    useEffect(() => {
        const socket = new SockJS("http://localhost:8080/ws");
        const client = new Client({
            webSocketFactory: () => socket,
            reconnectDelay: 5000,
            debug: (msg) => console.log("STOMP:", msg),
            onConnect: () => {
                console.log("✅ WebSocket Connected (Dashboard)");

                // ✅ 결제 이벤트
                client.subscribe("/topic/payments", (message) => {
                    const data = JSON.parse(message.body);
                    setStats((prev) =>
                        prev.map((s) => {
                            if (s.title === "이번 달 결제 건수") {
                                return { ...s, value: s.value + 1 };
                            }
                            if (data.isNewSubscription && s.title === "전체 구독 수") {
                                return { ...s, value: s.value + 1 };
                            }
                            if (data.isNewPartner && s.title === "거래처 수") {
                                return { ...s, value: s.value + 1 };
                            }
                            return s;
                        })
                    );

                    setActivityLogs((prev) => [
                        {
                            message: `결제 #${data.orderId} 완료 (${data.amount}원)`,
                            date: new Date().toISOString().split("T")[0],
                        },
                        ...prev,
                    ]);
                });

                // ✅ 구독 상태 이벤트
                client.subscribe("/topic/subscriptionUpdate", (message) => {
                    const data = JSON.parse(message.body);
                    setStats((prev) =>
                        prev.map((s) =>
                            s.title === "전체 구독 수"
                                ? { ...s, change: +1, value: s.value + 1 }
                                : s
                        )
                    );

                    setActivityLogs((prev) => [
                        {
                            message: `파트너 ${data.partnerNo} - ${data.billingCycle} : ${data.status}`,
                            date: new Date().toISOString().split("T")[0],
                        },
                        ...prev,
                    ]);
                });

                // ✅ 신규 문의 이벤트
                client.subscribe("/topic/support/new", (message) => {
                    const data = JSON.parse(message.body);
                    console.log("💬 새로운 문의 도착:", data);

                    setNewSupportAlert({
                        partnerNo: data.partnerNo,
                        message: `파트너 #${data.partnerNo} ${data.message}`,
                        time: new Date().toLocaleTimeString(),
                    });

                    setActivityLogs((prev) => [
                        {
                            message: `💬 ${data.message} (파트너 #${data.partnerNo})`,
                            date: new Date().toISOString().split("T")[0],
                        },
                        ...prev,
                    ]);

                    setTimeout(() => setNewSupportAlert(null), 5000);
                });
            },
        });

        client.activate();
        return () => client.deactivate();
    }, []);

    return (
        <div className="min-h-screen bg-gray-50 relative">
            <AdminHeader />

            {/* ✅ 실시간 문의 알림 팝업 */}
            {newSupportAlert && (
                <div
                    onClick={() =>
                        navigate(`/admin/support-chat?partnerNo=${newSupportAlert.partnerNo}`)
                    }
                    className="fixed top-6 right-6 bg-white shadow-lg border-l-4 border-blue-500 rounded-lg p-4 flex items-center space-x-3 cursor-pointer hover:bg-blue-50 transition"
                >
                    <MessageCircle className="text-blue-500" size={24} />
                    <div>
                        <p className="font-semibold text-gray-800">{newSupportAlert.message}</p>
                        <p className="text-sm text-gray-500">
                            {newSupportAlert.time} • 클릭 시 채팅 열기
                        </p>
                    </div>
                </div>
            )}

            <main className="p-8">
                <h1 className="text-3xl font-bold text-gray-800 mb-6">관리자 홈</h1>
                <p className="text-gray-600 mb-8">
                    주요 지표와 최근 활동을 실시간으로 확인할 수 있습니다.
                </p>

                {/* ✅ 통계 카드 */}
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-10">
                    {stats.map((item, idx) => (
                        <div
                            key={idx}
                            className="p-6 rounded-xl shadow-md bg-white border-l-4 border-gray-200"
                        >
                            <h2 className="text-sm font-medium text-gray-500">
                                {item.title}
                            </h2>
                            <div className="flex items-center mt-2">
                                <p className="text-3xl font-bold text-gray-800">{item.value}</p>
                                {item.change > 0 && (
                                    <ArrowUpRight
                                        size={20}
                                        className="text-green-500 ml-2 animate-bounce"
                                    />
                                )}
                                {item.change < 0 && (
                                    <ArrowDownRight
                                        size={20}
                                        className="text-red-500 ml-2 animate-bounce"
                                    />
                                )}
                            </div>
                        </div>
                    ))}
                </div>

                {/* ✅ 최근 활동 로그 */}
                <div className="bg-white rounded-xl shadow-lg p-6">
                    <h2 className="text-xl font-bold mb-4">최근 활동 로그</h2>
                    <ul className="divide-y divide-gray-200">
                        {activityLogs.map((log, idx) => (
                            <li key={idx} className="py-3 flex justify-between">
                                <span>{log.message}</span>
                                <span className="text-sm text-gray-500">{log.date}</span>
                            </li>
                        ))}
                    </ul>
                </div>
            </main>
        </div>
    );
}
