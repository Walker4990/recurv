import React from "react";
import { Link } from "react-router-dom";
import UserHeader from "../components/UserHeader";

function InquiryMain() {
    return (
        <div className="flex flex-col min-h-screen bg-gray-50">
            {/* Header */}
            <UserHeader />

            {/* Main */}
            <main className="flex-1 py-12 px-4">
                <div className="max-w-4xl mx-auto text-center">
                    <h1 className="text-3xl font-bold mb-6">고객센터</h1>
                    <p className="text-gray-600 mb-12">
                        자주 묻는 질문, 문의, AI 상담, 가이드 등 원하는 메뉴를 선택하세요.
                    </p>

                    {/* 메뉴 카드 */}
                    <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
                        {/* FAQ */}
                        <Link
                            to="/support/faq"
                            className="bg-white shadow-md rounded-xl p-6 hover:shadow-lg transition text-center"
                        >
                            <h2 className="text-xl font-semibold mb-2">📌 FAQ</h2>
                            <p className="text-gray-500">자주 묻는 질문 모아보기</p>
                        </Link>

                        {/* 문의하기 */}
                        <Link
                            to="/support/form"
                            className="bg-white shadow-md rounded-xl p-6 hover:shadow-lg transition text-center"
                        >
                            <h2 className="text-xl font-semibold mb-2">✉️ 문의하기</h2>
                            <p className="text-gray-500">직접 문의 접수</p>
                        </Link>

                        {/* AI 상담 */}
                        <Link
                            to="/support/ai"
                            className="bg-white shadow-md rounded-xl p-6 hover:shadow-lg transition text-center"
                        >
                            <h2 className="text-xl font-semibold mb-2">🤖 AI 상담</h2>
                            <p className="text-gray-500">AI 챗봇과 대화하기</p>
                        </Link>

                        {/* 이용 가이드 */}
                        <Link
                            to="/support/guide"
                            className="bg-white shadow-md rounded-xl p-6 hover:shadow-lg transition text-center"
                        >
                            <h2 className="text-xl font-semibold mb-2">📖 이용 가이드</h2>
                            <p className="text-gray-500">서비스 사용법 안내</p>
                        </Link>
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

export default InquiryMain;
