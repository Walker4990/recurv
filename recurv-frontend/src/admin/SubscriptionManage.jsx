// src/admin/SubscriptionManage.jsx
import React, { useEffect, useState } from "react";
import AdminHeader from "../components/AdminHeader";
import api from "../api/api.js";

export default function SubscriptionManage() {
    const [subscriptions, setSubscriptions] = useState([]);
    const [search, setSearch] = useState("");
    const [filterStatus, setFilterStatus] = useState("");

    // ✅ 구독 목록 불러오기
    const fetchSubscriptions = async () => {
        try {
            const res = await api.get("/api/admin/subscription", {
                params: { search, status: filterStatus },
            });

            setSubscriptions(res.data);
            console.log(res.data);
        } catch (err) {
            console.error("❌ 구독 목록 불러오기 실패:", err);
            alert("구독 목록을 불러오지 못했습니다.");
        }
    };

    // ✅ 상태 변경 API
    const handleStatusChange = async (id, newStatus) => {
        try {
            await api.put(`/api/admin/subscription/${id}`, { status: newStatus });
            fetchSubscriptions(); // 새로고침
        } catch (err) {
            console.error("❌ 상태 변경 실패:", err);
            alert("상태 변경 중 오류가 발생했습니다.");
        }
    };

    useEffect(() => {
        fetchSubscriptions();
    }, [filterStatus]);

    return (
        <div className="min-h-screen bg-gray-50">
            <AdminHeader />

            <main className="p-8">
                <h1 className="text-3xl font-bold text-gray-800 mb-6">구독 관리</h1>
                <div className="flex items-center mb-6 space-x-4">
                    {/* 검색 */}
                    <input
                        type="text"
                        value={search}
                        onChange={(e) => setSearch(e.target.value)}
                        placeholder="파트너 번호 검색"
                        className="border border-gray-300 rounded-lg px-4 py-2 w-1/3"
                    />
                    <button
                        onClick={fetchSubscriptions}
                        className="bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700"
                    >
                        검색
                    </button>

                    {/* 상태 필터 */}
                    <select
                        value={filterStatus}
                        onChange={(e) => setFilterStatus(e.target.value)}
                        className="border border-gray-300 rounded-lg px-3 py-2"
                    >
                        <option value="">전체 상태</option>
                        <option value="ACTIVE">ACTIVE</option>
                        <option value="CANCELED">CANCELED</option>
                        <option value="EXPIRED">EXPIRED</option>
                    </select>
                </div>

                {/* 구독 목록 테이블 */}
                <div className="bg-white rounded-xl shadow-lg p-6">
                    <table className="w-full border-collapse">
                        <thead>
                        <tr className="bg-gray-100 text-gray-700 text-left">
                            <th className="p-3">ID</th>
                            <th className="p-3">파트너 번호</th>
                            <th className="p-3">상태</th>
                            <th className="p-3">시작일</th>
                            <th className="p-3">종료일</th>
                            <th className="p-3">청구 주기</th>
                            <th className="p-3">다음 청구일</th>
                            <th className="p-3 text-center">관리</th>
                        </tr>
                        </thead>
                        <tbody>
                        {subscriptions.length > 0 ? (
                            subscriptions.map((s) => (
                                <tr key={s.partnerNo} className="border-b hover:bg-gray-50">
                                    <td className="p-3">{s.subscriptionId}</td>
                                    <td className="p-3">{s.partnerNo}</td>
                                    <td className="p-3 font-semibold">
                                            <span
                                                className={
                                                    s.status === "ACTIVE"
                                                        ? "text-green-600"
                                                        : s.status === "CANCELED"
                                                            ? "text-red-500"
                                                            : "text-gray-500"
                                                }
                                            >
                                                {s.status}
                                            </span>
                                    </td>
                                    <td className="p-3">{s.startDate || "-"}</td>
                                    <td className="p-3">{s.endDate || "-"}</td>
                                    <td className="p-3">{s.billingCycle || "-"}</td>
                                    <td className="p-3">{s.nextBillingDate || "-"}</td>
                                    <td className="p-3 text-center">
                                        <select
                                            value={s.status}
                                            onChange={(e) =>
                                                handleStatusChange(s.id, e.target.value)
                                            }
                                            className="border rounded-lg px-2 py-1"
                                        >
                                            <option value="ACTIVE">ACTIVE</option>
                                            <option value="CANCELED">CANCELED</option>
                                            <option value="EXPIRED">EXPIRED</option>
                                        </select>
                                    </td>
                                </tr>
                            ))
                        ) : (
                            <tr>
                                <td colSpan="8" className="p-6 text-center text-gray-400">
                                    구독 내역이 없습니다.
                                </td>
                            </tr>
                        )}
                        </tbody>
                    </table>
                </div>
            </main>
        </div>
    );
}
