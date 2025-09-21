import React, { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import UserHeader from "../components/UserHeader";
import SubscribeButton from "../components/SubscriptionButton"; // ✅ 추가

function BillingPage() {
    const navigate = useNavigate();
    const [plans, setPlans] = useState([]);

    useEffect(() => {
        setPlans([
            {
                plan_id: 1,
                name: "Basic",
                description: "스타트업 추천",
                price: 0,
                currency: "KRW",
                billing_cycle: "MONTHLY",
                target: "초기 스타트업, 테스트용",
                features: ["사용자 5명 제한", "기본 보고서 제공", "커뮤니티 지원"],
                benefit: "무료로 시작 가능",
                support: "이메일 지원 (평일 9~6)",
            },
            {
                plan_id: 2,
                name: "Pro",
                description: "성장 기업용",
                price: 29000,
                currency: "KRW",
                billing_cycle: "MONTHLY",
                target: "성장 중소기업",
                features: ["무제한 사용자", "고급 대시보드", "우선 지원"],
                benefit: "30일 무료 체험 제공",
                support: "실시간 채팅 + 이메일 지원",
            },
            {
                plan_id: 3,
                name: "Enterprise",
                description: "대규모 ERP 맞춤",
                price: 0,
                currency: "KRW",
                billing_cycle: "YEARLY",
                target: "대기업, ERP 통합",
                features: ["전담 컨설턴트", "맞춤형 기능 개발", "24/7 지원"],
                benefit: "맞춤 견적 & SLA 지원",
                support: "전담 매니저 + SLA 보장",
            },
        ]);
    }, []);

    return (
        <div className="flex flex-col min-h-screen bg-gray-50">
            {/* Header */}
            <UserHeader />

            {/* Main */}
            <main className="flex-1 py-12 px-4 mt-8">
                <h1 className="text-3xl font-bold text-center mb-10">Billing Plans</h1>

                <div className="grid grid-cols-1 md:grid-cols-3 gap-8 max-w-6xl mx-auto items-stretch">
                    {plans.map((plan) => (
                        <div
                            key={plan.plan_id}
                            className="bg-white shadow-lg rounded-2xl p-6 flex flex-col justify-between text-center hover:shadow-xl transition h-full"
                        >
                            <div>
                                <h2 className="text-xl font-semibold">{plan.name}</h2>
                                <p className="text-gray-500 text-sm mb-2">{plan.description}</p>

                                <p className="text-2xl font-bold mt-2">
                                    {plan.price > 0
                                        ? `${plan.price.toLocaleString()} ${plan.currency}`
                                        : "맞춤 견적"}
                                </p>
                                <p className="text-sm text-gray-400">{plan.billing_cycle}</p>

                                <p className="mt-3 text-sm text-green-600 font-medium">
                                    {plan.target}
                                </p>

                                <ul className="mt-3 text-sm text-gray-700 space-y-1">
                                    {plan.features.map((f, i) => (
                                        <li key={i}>✅ {f}</li>
                                    ))}
                                </ul>

                                <p className="mt-3 text-xs text-blue-500">{plan.benefit}</p>
                                <p className="mt-2 text-xs text-gray-500">지원: {plan.support}</p>
                            </div>

                            <div className="mt-6 flex justify-center gap-3">
                                <Link
                                    to={`/billing/${plan.plan_id}`}
                                    className="bg-blue-500 hover:bg-blue-600 text-white px-4 py-2 rounded-lg text-sm"
                                >
                                    자세히 보기
                                </Link>
                                {/* ✅ 교체된 구독 버튼 */}
                                <SubscribeButton plan={plan} />
                            </div>
                        </div>
                    ))}
                </div>

                {/* Floating 문의하기 버튼 */}
                <button
                    onClick={() => navigate("/support")}
                    className="fixed bottom-6 right-6 bg-gray-800 hover:bg-gray-900 text-white px-6 py-3 rounded-full shadow-lg text-lg"
                >
                    문의하기
                </button>
            </main>

            {/* Footer */}
            <footer className="py-4 text-center text-gray-400 text-sm">
                © 2025 Recurv. All rights reserved.
            </footer>
        </div>
    );
}

export default BillingPage;
