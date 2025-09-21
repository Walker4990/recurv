import React, { useState } from "react";
import UserHeader from "../components/UserHeader";

function Inquiry() {
    const [question, setQuestion] = useState("");
    const [answer, setAnswer] = useState("");

    // FAQ 데이터
    const faqs = [
        { q: "가격", a: "요금제는 Basic, Pro, Enterprise 3가지가 있습니다." },
        { q: "환불", a: "환불은 결제 후 7일 이내 가능합니다." },
        { q: "구독 해지", a: "구독은 설정 페이지에서 언제든 해지 가능합니다." },
        { q: "지원", a: "Basic은 이메일 지원, Pro는 실시간 채팅, Enterprise는 24/7 전담 매니저가 지원합니다." },
    ];

    const handleAsk = () => {
        const match = faqs.find((f) => question.includes(f.q));
        if (match) {
            setAnswer(match.a);
        } else {
            setAnswer("담당자가 확인 후 답변드리겠습니다. 🙏");
        }
    };

    return (
        <div className="flex flex-col min-h-screen bg-gray-50">
            {/* Header */}
            <UserHeader />

            {/* Main */}
            <main className="flex-1 py-12 px-4">
                <div className="max-w-2xl mx-auto bg-white shadow-lg rounded-2xl p-8">
                    <h1 className="text-2xl font-bold mb-6 text-center">고객센터 (FAQ 챗봇)</h1>
                    <p className="text-gray-600 mb-8 text-center">
                        자주 묻는 질문을 입력해보세요. 🤖
                    </p>

                    {/* 입력 영역 */}
                    <div className="flex gap-2 mb-4">
                        <input
                            type="text"
                            value={question}
                            onChange={(e) => setQuestion(e.target.value)}
                            placeholder="예: 가격, 환불, 구독 해지..."
                            className="flex-1 border rounded-lg px-4 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
                        />
                        <button
                            onClick={handleAsk}
                            className="bg-blue-500 hover:bg-blue-600 text-white px-6 py-2 rounded-lg"
                        >
                            질문하기
                        </button>
                    </div>

                    {/* 응답 영역 */}
                    {answer && (
                        <div className="mt-6 bg-gray-100 rounded-lg p-4 text-gray-800">
                            <strong>답변:</strong> {answer}
                        </div>
                    )}

                    {/* FAQ 리스트 */}
                    <div className="mt-8">
                        <h2 className="text-lg font-semibold mb-3">📌 자주 묻는 질문</h2>
                        <ul className="list-disc list-inside text-gray-700 space-y-2">
                            {faqs.map((f, i) => (
                                <li key={i}>
                                    <span className="font-medium">{f.q}</span> → {f.a}
                                </li>
                            ))}
                        </ul>
                    </div>
                </div>
            </main>

            {/* Footer */}
            <footer className="py-4 text-center text-gray-400 text-sm">
                © 2025 Recurv. All rights reserved.
            </footer>
        </div>
    );
}

export default Inquiry;
