// PartnerManage.jsx (등록 기능 제거 버전)
import {useEffect, useState} from "react";
import api from "../api/api";
import AdminHeader from "../components/AdminHeader";
import {useNavigate} from "react-router-dom";

export default function PartnerManage() {
    const [partners, setPartners] = useState([]);
    const navigate = useNavigate();
    const fetchPartners = async () => {
        try {
            const res = await api.get("/api/admin/partner");
            setPartners(res.data);
            console.log(res.data.status);
        } catch (err) {
            console.error("파트너 목록 불러오기 실패:", err);
        }
    };

    const handleStatusChange = async (id, newStatus) => {
        try {
            await api.put(`/api/admin/partner/${id}`, { status: newStatus });
            setPartners(prev =>
                prev.map(p => (p.userId === id ? { ...p, status: newStatus } : p))
            );
        } catch (err) {
            alert("상태 변경 실패");
        }
    };

    useEffect(() => {
        fetchPartners();
    }, []);

    return (
        <div className="min-h-screen bg-gray-50">
            <AdminHeader />
            <main className="p-8">
                <h1 className="text-3xl font-bold mb-6 text-gray-800">파트너 관리</h1>

                <div className="bg-white rounded-xl shadow-lg p-6">
                    <table className="w-full border-collapse">
                        <thead>
                        <tr className="bg-gray-100 text-gray-700">
                            <th className="p-3">ID</th>
                            <th className="p-3">이름</th>
                            <th className="p-3">이메일</th>
                            <th className="p-3">상태</th>
                            <th className="p-3">최근 결제일</th>
                            <th className="p-3">관리</th>
                        </tr>
                        </thead>
                        <tbody>
                        {partners.length > 0 ? (
                            partners.map(p => (
                                <tr
                                    key={p.userId}
                                    className="border-b hover:bg-gray-50 cursor-pointer"
                                    onClick={() => navigate(`/admin/partner/${p.userId}`)}
                                >
                                    <td className="p-3">{p.userId}</td>
                                    <td className="p-3">{p.name}</td>
                                    <td className="p-3">{p.email}</td>
                                    <td className="p-3 font-semibold">{p.status}</td>
                                    <td className="p-3">{p.createdAt || "-"}</td>
                                    <td className="p-3 text-center">
                                        <select

                                            value={p.status ?? ""}
                                            onClick={(e) => e.stopPropagation()}
                                            onChange={e =>
                                                handleStatusChange(p.userId, e.target.value)
                                            }
                                            className="border rounded-lg px-2 py-1"
                                        >
                                            <option value="ACTIVE">ACTIVE</option>
                                            <option value="SUSPENDED">SUSPENDED</option>
                                            <option value="INACTIVE">INACTIVE</option>
                                        </select>
                                    </td>
                                </tr>
                            ))
                        ) : (
                            <tr>
                                <td colSpan="6" className="text-center text-gray-400 p-4">
                                    등록된 파트너가 없습니다.
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
