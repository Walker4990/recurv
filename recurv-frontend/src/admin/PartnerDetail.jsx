import {useEffect, useState} from "react";
import {useParams, useNavigate} from "react-router-dom";
import api from "../api/api";
import AdminHeader from "../components/AdminHeader";

export default function PartnerDetail() {
    const { userId } = useParams();
    const navigate = useNavigate();
    const [partner, setPartner] = useState(null);
    const [invoices, setInvoices] = useState([]);
    const [payments, setPayments] = useState([]);

    useEffect(() => {
        fetchPartnerDetail();
    }, [userId]);

    const fetchPartnerDetail = async () => {
        try {
            const [partnerRes, invoiceRes, paymentRes] = await Promise.all([
                api.get(`/api/admin/partner/${userId}`),
                api.get(`/api/admin/partner/${userId}/invoices`),
                api.get(`/api/admin/partner/${userId}/payments`),
            ]);
            setPartner(partnerRes.data);
            setInvoices(invoiceRes.data);
            setPayments(paymentRes.data);
        } catch (err) {
            console.error("상세 조회 실패:", err);
        }
    };

    if (!partner) return <div>로딩 중...</div>;

    return (
        <div className="min-h-screen bg-gray-50">
            <AdminHeader />
            <main className="p-8">
                <button
                    onClick={() => navigate(-1)}
                    className="mb-4 bg-gray-200 px-3 py-1 rounded"
                >
                    ← 목록으로
                </button>

                <h1 className="text-2xl font-bold mb-2">{partner.name}</h1>
                <p className="text-gray-600 mb-4">{partner.email}</p>

                {/* 청구 내역 */}
                <section className="mb-8">
                    <h2 className="text-xl font-semibold mb-2">청구 내역</h2>
                    <table className="w-full border-collapse">
                        <thead>
                        <tr className="bg-gray-100">
                            <th className="p-2">청구번호</th>
                            <th className="p-2">금액</th>
                            <th className="p-2">상태</th>
                            <th className="p-2">발행일</th>
                        </tr>
                        </thead>
                        <tbody>
                        {invoices.length > 0 ? (
                            invoices.map(i => (
                                <tr key={i.invoiceId} className="border-b">
                                    <td className="p-2">{i.invoiceNo}</td>
                                    <td className="p-2">{i.totalAmount}</td>
                                    <td className="p-2">{i.status}</td>
                                    <td className="p-2">{i.createdAt}</td>
                                </tr>
                            ))
                        ) : (
                            <tr><td colSpan="4" className="text-center p-4 text-gray-400">청구 내역 없음</td></tr>
                        )}
                        </tbody>
                    </table>
                </section>

                {/* 결제 내역 */}
                <section>
                    <h2 className="text-xl font-semibold mb-2">결제 내역</h2>
                    <table className="w-full border-collapse">
                        <thead>
                        <tr className="bg-gray-100">
                            <th className="p-2">결제ID</th>
                            <th className="p-2">금액</th>
                            <th className="p-2">상태</th>
                            <th className="p-2">결제일</th>
                        </tr>
                        </thead>
                        <tbody>
                        {payments.length > 0 ? (
                            payments.map(p => (
                                <tr key={p.paymentId} className="border-b">
                                    <td className="p-2">{p.paymentKey}</td>
                                    <td className="p-2">{p.amount}</td>
                                    <td className="p-2">{p.status}</td>
                                    <td className="p-2">{p.approvedAt}</td>
                                </tr>
                            ))
                        ) : (
                            <tr><td colSpan="4" className="text-center p-4 text-gray-400">결제 내역 없음</td></tr>
                        )}
                        </tbody>
                    </table>
                </section>
            </main>
        </div>
    );
}
