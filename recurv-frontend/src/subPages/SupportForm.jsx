import React from "react";
import UserHeader from "../components/UserHeader";

function InquiryForm() {
    return (
        <div className="flex flex-col min-h-screen bg-gray-50">
            <UserHeader />
            <main className="flex-1 py-12 px-4">
                <div className="max-w-2xl mx-auto bg-white shadow-lg rounded-2xl p-8">
                    <h1 className="text-2xl font-bold mb-6 text-center">✉️ 문의하기</h1>
                    <form className="space-y-6">
                        <div>
                            <label className="block text-sm font-medium text-gray-700 mb-1">이름</label>
                            <input type="text" className="w-full border rounded-lg px-4 py-2" placeholder="이름" />
                        </div>
                        <div>
                            <label className="block text-sm font-medium text-gray-700 mb-1">이메일</label>
                            <input type="email" className="w-full border rounded-lg px-4 py-2" placeholder="example@email.com" />
                        </div>
                        <div>
                            <label className="block text-sm font-medium text-gray-700 mb-1">문의 유형</label>
                            <select className="w-full border rounded-lg px-4 py-2">
                                <option>요금제</option>
                                <option>결제/환불</option>
                                <option>계정 문제</option>
                                <option>기타</option>
                            </select>
                        </div>
                        <div>
                            <label className="block text-sm font-medium text-gray-700 mb-1">문의 내용</label>
                            <textarea rows="5" className="w-full border rounded-lg px-4 py-2" placeholder="내용을 입력하세요"></textarea>
                        </div>
                        <button type="submit" className="w-full bg-blue-500 text-white py-3 rounded-lg hover:bg-blue-600">
                            제출하기
                        </button>
                    </form>
                </div>
            </main>
        </div>
    );
}

export default InquiryForm;
