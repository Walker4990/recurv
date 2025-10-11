import React, {useEffect, useState} from "react";
import AdminHeader from "../components/AdminHeader";
import api from "../api/api.js"

export default function BillingManage(){
    const [invoices, setInvoices] = useState([]);

    const fetchInvoices = async () => {
        try{
            const res = await api.get("/api/admin/billing");
            setInvoices(res.data);
        } catch (err){
            console.log("청구서 불러오기 실패 : ",err);
        }
    }
    const handleStatusChange = async (id, newStatus) => {
        try{
            await api.put(`/api/admin/billing/${id}`, {status : newStatus});
            setInvoices(prevInvoices =>
                prevInvoices.map(i =>
                    i.invoiceId === id ? { ...i, status: newStatus } : i
                )
            );
        } catch(err){
            console.log(err);
            alert("상태 변경 실패")
        }
    }

    useEffect(()=>{
        fetchInvoices();
    }, []);

    return   (<div className="min-h-screen bg-gray-50">
        <AdminHeader />
        <main className="p-8">
            <h1 className="text-3xl font-bold mb-6 text-gray-800">청구 내역 관리</h1>
            <div className="bg-white rounded-xl shadow-lg p-6">
                <table className="w-full border-collapse">
                    <thead>
                    <tr className="bg-gray-100 text-gray-700">
                        <th className="p-3">청구번호</th>
                        <th className="p-3">파트너 번호</th>
                        <th className="p-3">총금액</th>
                        <th className="p-3">상태</th>
                        <th className="p-3">발행일</th>
                        <th className="p-3">관리</th>
                    </tr>
                    </thead>
                    <tbody>
                    {invoices.length > 0 ? (
                        invoices.map((i) => (
                            <tr key={i.invoiceId} className="border-b">
                                <td className="p-3">{i.invoiceNo}</td>
                                <td className="p-3">{i.partnerNo}</td>
                                <td className="p-3">{i.totalAmount}</td>
                                <td className="p-3 font-semibold">{i.status}</td>
                                <td className="p-3">{i.createdAt}</td>
                                <td className="p-3 text-center">
                                    <select
                                        value={i.status}
                                        onChange={(e) =>
                                            handleStatusChange(i.invoiceId, e.target.value)
                                        }
                                        className="border rounded-lg px-2 py-1"
                                    >
                                        <option value="PENDING">PENDING</option>
                                        <option value="PAID">PAID</option>
                                        <option value="FAILED">FAILED</option>
                                    </select>
                                </td>
                            </tr>
                        ))
                    ) : (
                        <tr>
                            <td colSpan="6" className="text-center text-gray-400 p-4">
                                청구 내역이 없습니다.
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