import React from "react";
import UserHeader from "../components/UserHeader";

export default function SupportGuide(){
    const guides = [
        { title: "요금제 변경", desc: "마이페이지 → 구독 관리에서 요금제를 변경할 수 있습니다." },
        { title: "환불 요청", desc: "결제 후 7일 이내 고객센터 → 문의하기에서 환불 요청 가능합니다." },
        { title: "계정 보안", desc: "비밀번호는 주기적으로 변경하고 2단계 인증을 활성화하세요." },
    ];

    return (
        <div className="flex flex-col min-h-screen bg-gray-50">
            <UserHeader />
            <main className="flex-1 py-12 px-4">
                <div className="max-w-3xl mx-auto bg-white shadow-lg rounded-2xl p-8">
                    <h1 className="text-2xl font-bold mb-6 text-center">📖 이용 가이드</h1>
                    <div className="space-y-6">
                        {guides.map((g, i) => (
                            <div key={i} className="p-4 border rounded-lg hover:shadow-md transition">
                                <h2 className="font-semibold text-lg">{g.title}</h2>
                                <p className="text-gray-600 mt-2">{g.desc}</p>
                            </div>
                        ))}
                    </div>
                </div>
            </main>
        </div>)

}