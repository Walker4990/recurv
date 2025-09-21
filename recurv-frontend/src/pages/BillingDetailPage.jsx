import React from "react";
import { useParams, Link } from "react-router-dom";
import SubscriptionButton from "../components/SubscriptionButton";

function BillingDetailPage() {
    const { planId } = useParams();

    const planDetails = {
        1: {
            name: "Basic",
            description: "무료 플랜 – 소규모 스타트업 추천",
            price: "₩0 / 월",
            cycle: "MONTHLY",
            target: "초기 스타트업, 테스트 목적",
            features: ["사용자 5명 제한", "기본 보고서 제공", "커뮤니티 지원"],
            benefits: ["무료 시작 가능", "필수 기능 포함"],
        },
        2: {
            name: "Pro",
            description: "월 29,000원 – 성장 기업용",
            price: "₩29,000 / 월",
            cycle: "MONTHLY",
            target: "성장하는 중소기업",
            features: [
                "무제한 사용자",
                "고급 대시보드",
                "우선 지원",
                "ERP 통합 API",
                "재무 리포트 자동화"
            ],
            benefits: ["30일 무료 체험", "연간 결제 시 10% 할인"],
        },
        3: {
            name: "Enterprise",
            description: "맞춤 견적 – 대규모 ERP 구축",
            price: "맞춤 견적",
            cycle: "YEARLY",
            target: "대기업, ERP 통합 프로젝트",
            features: [
                "전담 컨설턴트 배정",
                "맞춤형 기능 개발",
                "24/7 기술 지원",
                "무제한 API 호출",
                "보안·컴플라이언스 지원"
            ],
            benefits: ["개별 견적 제공", "맞춤형 SLA(Service Level Agreement)"],
        },
    };

    const plan = planDetails[planId];

    if (!plan) return <p>존재하지 않는 요금제입니다.</p>;

    return (
        <div className="min-h-screen bg-gray-50 py-12 px-4">
            <div className="max-w-3xl mx-auto bg-white shadow-lg rounded-2xl p-8">
                <h1 className="text-2xl font-bold mb-2">{plan.name}</h1>
                <p className="text-gray-600 mb-4">{plan.description}</p>

                <p className="text-xl font-semibold">{plan.price}</p>
                <p className="text-sm text-gray-500 mb-6">청구 주기: {plan.cycle}</p>

                <h2 className="text-lg font-semibold mb-2">대상 고객</h2>
                <p className="text-gray-700 mb-6">{plan.target}</p>

                <h2 className="text-lg font-semibold mb-2">제공 기능</h2>
                <ul className="list-disc list-inside text-gray-700 mb-6">
                    {plan.features.map((f, i) => (
                        <li key={i}>{f}</li>
                    ))}
                </ul>

                <h2 className="text-lg font-semibold mb-2">추가 혜택</h2>
                <ul className="list-disc list-inside text-gray-700 mb-6">
                    {plan.benefits.map((b, i) => (
                        <li key={i}>{b}</li>
                    ))}
                </ul>

                <div className="flex gap-4">
                    <SubscriptionButton />
                    <button className="bg-blue-500 hover:bg-blue-600 text-white px-6 py-2 rounded-lg">
                        문의하기
                    </button>
                </div>

                <Link
                    to="/billing"
                    className="mt-6 block text-blue-500 hover:underline"
                >
                    ← 요금제 목록으로
                </Link>
            </div>
        </div>
    );
}

export default BillingDetailPage;
