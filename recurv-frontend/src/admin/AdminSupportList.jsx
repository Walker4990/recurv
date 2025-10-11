import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import AdminHeader from "../components/AdminHeader";
import api from "../api/api";

export default function AdminSupportList() {
    const [chats, setChats] = useState([]);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchChats = async () => {
            try {
                const res = await api.get("/api/support/recent");
                setChats(res.data);
            } catch (err) {
                console.error("❌ 목록 불러오기 실패:", err);
            }
        };
        fetchChats();
    }, []);

    const handleClick = (partnerNo) => {
        navigate(`/admin/support-chat?partnerNo=${partnerNo}`);
    };

    return (
        <div className="min-h-screen bg-gray-50">
            <AdminHeader />
            <main className="max-w-4xl mx-auto mt-8 bg-white p-6 rounded-xl shadow-md">
                <h1 className="text-2xl font-bold mb-6">💬 고객 문의함</h1>

                <ul className="divide-y divide-gray-200">
                    {chats.map((chat, idx) => (
                        <li
                            key={`${chat.partnerNo}-${idx}`}
                            onClick={() => handleClick(chat.partnerNo)}
                            className={`flex justify-between items-center p-4 cursor-pointer hover:bg-gray-50 rounded-lg transition ${
                                chat.unread ? "bg-blue-50" : ""
                            }`}
                        >
                            <div>
                                <p className="font-semibold text-gray-800">
                                    파트너 #{chat.partnerNo}
                                    {chat.unread && (
                                        <span className="ml-2 text-xs text-white bg-red-500 px-2 py-0.5 rounded">
                                            NEW
                                        </span>
                                    )}
                                </p>
                                <p className="text-sm text-gray-600 truncate w-64">
                                    {chat.content}
                                </p>
                            </div>
                            <span className="text-sm text-gray-500">
                                {new Date(chat.createdAt).toLocaleString()}
                            </span>
                        </li>
                    ))}
                </ul>

                {chats.length === 0 && (
                    <p className="text-gray-500 text-center mt-4">
                        최근 문의가 없습니다.
                    </p>
                )}
            </main>
        </div>
    );
}
