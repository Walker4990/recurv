import React, { useState } from "react";
import UserHeader from "../components/UserHeader";

function InquiryAI() {
    const [question, setQuestion] = useState("");
    const [answer, setAnswer] = useState("");

    const faqs = [
        { q: "가격", a: "요금제는 Basic 무료, Pro 29,000원/월, Enterprise 맞춤 견적입니다." },
        { q: "환불", a: "결제 후 7일 이내 환불 가능합니다." },
        { q: "구독", a: "마이페이지 > 구독 관리에서 해지할 수 있습니다." },
    ];

    const handleAsk = () => {
        const match = faqs.find((f) => question.includes(f.q));
        if (match) {
            setAnswer(match.a);
        } else {
            setAnswer("AI 상담: 입력하신 질문은 담당자가 확인 후 답변드리겠습니다.");
        }
    };

    return (
        <div className="flex flex-col min-h-screen bg-gray-50">
            <UserHeader />
            <main className="flex-1 py-12 px-4">
                <div className="max-w-2xl mx-auto bg-white shadow-lg rounded-2xl p-8">
                    <h1 className="text-2xl font-bold mb-6 text-center">🤖 AI 상담</h1>
                    <div className="flex gap-2 mb-4">
                        <input
                            type="text"
                            value={question}
                            onChange={(e) => setQuestion(e.target.value)}
                            placeholder="궁금한 점을 입력하세요"
                            className="flex-1 border px-4 py-2 rounded-lg"
                        />
                        <button
                            onClick={handleAsk}
                            className="bg-blue-500 text-white px-4 py-2 rounded-lg hover:bg-blue-600"
                        >
                            질문하기
                        </button>
                    </div>
                    {answer && <div className="bg-gray-100 rounded-lg p-4 mt-4">{answer}</div>}
                </div>
            </main>
        </div>
    );
}

export default InquiryAI;
