import React, { useEffect, useState } from "react";
import AdminHeader from "../components/AdminHeader";
import api from "../api/api.js";

export default function AdminNotificationLog() {
    const [logs, setLogs] = useState([]);
    const [search, setSearch] = useState("");

    useEffect(() => {
        fetchLogs();
    }, []);

    const fetchLogs = async () => {
        try {
            const res = await api.get("/api/admin/notification/log");
            setLogs(res.data);
        } catch (err) {
            console.error("❌ 알림 로그 불러오기 실패:", err);
        }
    };

    const filtered = logs.filter((log) =>
        log.target?.toLowerCase().includes(search.toLowerCase())
    );

    return (
        <div className="bg-gray-50 min-h-screen">
            {/* 헤더 - 상단 고정 */}
            <div className="sticky top-0 z-10">
                <AdminHeader title="알림 이력 관리" />
            </div>

            {/* 본문 */}
            <div className="max-w-7xl mx-auto px-6 py-6">
                {/* 🔍 검색창 */}
                <div className="flex justify-start mb-4">
                    <input
                        type="text"
                        placeholder="수신자 이메일 또는 파트너 번호 검색..."
                        className="border border-gray-300 rounded px-3 py-2 w-1/3 text-sm focus:outline-none focus:ring-1 focus:ring-[#00A651]"
                        value={search}
                        onChange={(e) => setSearch(e.target.value)}
                    />
                </div>

                {/* 📋 테이블 */}
                <div className="bg-white rounded-lg shadow p-4">
                    <table className="w-full text-sm text-gray-700 border-collapse">
                        <thead className="bg-gray-100 text-gray-700 text-xs uppercase">
                        <tr>
                            <th className="p-2 text-left">ID</th>
                            <th className="p-2 text-left">파트너 번호</th>
                            <th className="p-2 text-left">유형</th>
                            <th className="p-2 text-left">채널</th>
                            <th className="p-2 text-left">수신 대상</th>
                            <th className="p-2 text-left">상태</th>
                            <th className="p-2 text-left">발송일</th>
                        </tr>
                        </thead>

                        <tbody>
                        {filtered.length === 0 ? (
                            <tr>
                                <td
                                    colSpan="7"
                                    className="text-center py-10 text-gray-400 text-sm"
                                >
                                    알림 이력이 없습니다.
                                </td>
                            </tr>
                        ) : (
                            filtered.map((log) => (
                                <tr
                                    key={log.id}
                                    className="border-b last:border-b-0 hover:bg-gray-50"
                                >
                                    <td className="p-2">{log.id}</td>
                                    <td className="p-2">{log.partnerNo}</td>
                                    <td className="p-2">{log.type}</td>
                                    <td className="p-2">{log.channel}</td>
                                    <td className="p-2 break-all">{log.target}</td>
                                    <td
                                        className={`p-2 font-semibold ${
                                            log.status === "SUCCESS"
                                                ? "text-green-600"
                                                : "text-red-600"
                                        }`}
                                    >
                                        {log.status}
                                    </td>
                                    <td className="p-2 text-gray-500">
                                        {new Date(log.sentAt).toLocaleString("ko-KR")}
                                    </td>
                                </tr>
                            ))
                        )}
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    );
}
