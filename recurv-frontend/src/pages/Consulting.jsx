import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../api/api.js";

export default function Consulting() {
    const [form, setForm] = useState({ name: "", email: "", company: "", message: "" });
    const [submitted, setSubmitted] = useState(false);
    const navigate = useNavigate();
    const handleChange = (e) => {
        setForm({ ...form, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await api.post("/consulting", form);
            setSubmitted(true);
            setForm({ name: "", email: "", company: "", message: "" }); // ✅ 초기화
        } catch (err) {
            console.error("상담 요청 실패:", err);
            alert("상담 요청 중 오류가 발생했습니다. 다시 시도해주세요.");
        }
    };

    if (submitted) {
        return (
            <div className="min-h-screen flex items-center justify-center bg-gray-50">
                <div className="bg-white p-10 rounded-xl shadow-lg text-center">
                    <h2 className="text-2xl font-bold mb-4 text-green-600">✅ 상담 요청 완료</h2>
                    <p className="text-gray-600">담당자가 곧 이메일로 연락드리겠습니다.</p>
                    <button
                        className="mt-6 px-6 py-3 bg-green-600 text-white rounded-lg hover:bg-green-700"
                        onClick={() => setSubmitted(false)}
                    >
                        다시 작성하기
                    </button>
                    <button
                        className="px-6 py-3 bg-gray-500 text-white rounded-lg hover:bg-gray-600"
                        onClick={() => navigate("/")}
                    >
                        홈으로
                    </button>
                </div>
            </div>
        );
    }

    return (
        <div className="min-h-screen flex items-center justify-center bg-gray-50">
            <div className="bg-white p-10 rounded-xl shadow-lg w-full max-w-lg">
                <h2 className="text-2xl font-bold mb-6 text-gray-800">ERP 제작 상담 신청</h2>
                <form onSubmit={handleSubmit} className="space-y-4">
                    <input
                        type="text"
                        name="name"
                        placeholder="이름"
                        value={form.name}
                        onChange={handleChange}
                        className="w-full border px-4 py-2 rounded-lg"
                        required
                    />
                    <input
                        type="email"
                        name="email"
                        placeholder="이메일"
                        value={form.email}
                        onChange={handleChange}
                        className="w-full border px-4 py-2 rounded-lg"
                        required
                    />
                    <input
                        type="text"
                        name="company"
                        placeholder="회사명"
                        value={form.company}
                        onChange={handleChange}
                        className="w-full border px-4 py-2 rounded-lg"
                    />
                    <textarea
                        name="message"
                        placeholder="문의 내용"
                        value={form.message}
                        onChange={handleChange}
                        className="w-full border px-4 py-2 rounded-lg h-32"
                        required
                    />
                    <button
                        type="submit"
                        className="w-full py-3 bg-green-600 text-white rounded-lg hover:bg-green-700"
                    >
                        상담 신청하기
                    </button>
                    <button
                        type="button"
                        className="w-full py-3 border border-green-600 text-green-600 rounded-lg hover:bg-green-600 hover:text-white transition duration-200"
                        onClick={() => window.history.back()}
                    >
                        홈으로
                    </button>

                </form>
            </div>
        </div>
    );
}
