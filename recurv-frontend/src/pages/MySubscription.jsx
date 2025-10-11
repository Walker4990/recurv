import React, { useEffect, useState } from "react";
import { CheckCircle, XCircle } from "lucide-react";
import axios from "axios";
import UserHeader from "../components/UserHeader";
import { Link } from "react-router-dom";
import api from "../api/api.js";

export default function MySubscription() {
    const [subscription, setSubscription] = useState(null);
    const [loading, setLoading] = useState(true);
    const [showModal, setShowModal] = useState(false);

    useEffect(() => {
        const fetchSub = async () => {
            try {
                const partnerNo = localStorage.getItem("userId"); // 로그인 시 저장한 ID
                if (!partnerNo) {
                    console.error("partnerNo 없음");
                    return;
                }

                const res = await api.get("/subscriptions/me", {
                    params: { partnerNo },
                });
                setSubscription(res.data);
            } catch (err) {
                console.error("구독 조회 실패:", err);
            } finally {
                setLoading(false);
            }
        };
        fetchSub();
    }, []);

    const handleChangePaymentMethod = async() => {
        try{
            const partnerNo = localStorage.getItem("userId");
            const tossPayments = window.TossPayments(import.meta.env.VITE_TOSS_CLIENT_KEY);

            await tossPayments.requestBiilingAuth({
                customerKey: partnerNo,
                method: "CARD",
            })
            alert("결제수단 변경 요청이 완료되었습니다. 곧 반영됩니다.");
            // Webhook 처리 후 반영 → 잠시 기다렸다 다시 조회
            setTimeout(async () => {
                const res = await api.get("/subscriptions/me", { params: { partnerNo } });
                setSubscription(res.data);
            }, 2000);
        } catch (err) {
            console.error("결제수단 변경 실패:", err);
            alert("결제수단 변경에 실패했습니다.");
        }
    };


    const handleDeletePaymentMethod = async () => {
        if (!window.confirm("정말로 삭제하시겠습니까?")) return;
        try {
            const partnerNo = localStorage.getItem("userId");
            await api.delete(`/payment-methods/${partnerNo}`);
            alert("결제수단이 삭제되었습니다.");
            window.location.reload();
        } catch (err) {
            console.error("삭제 실패:", err);
            alert("결제수단 삭제에 실패했습니다.");
        }
    };


    if (loading) {
        return <p className="text-center mt-6">불러오는 중...</p>;
    }

    if (!subscription || subscription.status === "NONE") {
        return (
            <div className="min-h-screen flex flex-col bg-gray-50">
                <UserHeader />
                <div className="flex flex-1 items-center justify-center">
                    <div className="p-10 max-w-2xl w-full text-center bg-white rounded-xl shadow-lg">
                        <h2 className="text-2xl font-bold text-gray-800 mb-4">내 구독 정보</h2>
                        <XCircle className="w-12 h-12 text-gray-400 mx-auto mb-4" />
                        <p className="text-gray-600 mb-6">현재 활성화된 구독이 없습니다.</p>
                        <button className="px-6 py-3 bg-blue-600 text-white font-semibold rounded-lg hover:bg-blue-700 transition">
                            구독 시작하기
                        </button>
                    </div>
                </div>
            </div>
        );
    }

    const isActive = subscription?.status === "ACTIVE";

    const handleCancel = async () => {
        if (!subscription?.id) {
            console.error("cancel: subscription.id 없음", subscription);
            alert("구독 정보가 없습니다.");
            return;
        }

        try {
            setShowModal(false); // 모달 닫고 진행(원하면 닫기 지점 변경)
            console.log("취소 요청:", subscription.id);

            // 절대경로로 백엔드 호출 (프록시/axios baseURL 쓰면 변경)
          const res = await api.post(`/subscriptions/${subscription.id}/cancel`);
          console.log(res.status, res.data);
          alert('구독이 취소되었습니다.');
          window.location.reload();
        } catch (err) {
            console.error("취소 실패:", err);
            // 네트워크/응답 에러 메시지 상세 확인
            if (err.response) {
                console.error("서버 응답:", err.response.status, err.response.data);
                alert(`취소 실패: ${err.response.status} ${err.response.data}`);
            } else if (err.request) {
                console.error("요청 전송됐으나 응답 없음 (네트워크/CORS?):", err.request);
                alert("네트워크 오류 또는 CORS 문제. 콘솔 네트워크 탭 확인.");
            } else {
                console.error("설정 오류:", err.message);
                alert("취소 실패: " + err.message);
            }
            // 안내로 고객센터로 리다이렉트 (원하면)
            // window.location.href = "/supportMain";
        }
    };


    return (
        <div className="min-h-screen bg-gray-50">
            <UserHeader />

            <div className="max-w-5xl mx-auto py-10 px-4 space-y-8">
                {/* 제목 */}
                <div>
                    <h1 className="text-3xl font-bold text-gray-800">마이페이지</h1>
                    <p className="text-gray-600">내 구독, 결제 내역, 결제수단을 확인하세요.</p>
                </div>

                {/* 카드 레이아웃 */}
                <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                    {/* 내 구독 정보 */}
                    <div className="bg-white rounded-xl shadow-lg p-6">
                        <h2 className="text-xl font-bold mb-4">내 구독 정보</h2>
                        <p><span className="font-semibold">상태:</span> ACTIVE</p>
                        <p><span className="font-semibold">플랜:</span> Pro Plan</p>
                        <p><span className="font-semibold">만료일:</span> 2025-10-01</p>
                        <button
                            className="mt-4 w-full py-2 bg-red-500 text-white rounded-lg hover:bg-red-600"
                            onClick={() => setShowModal(true)} // 모달 열기
                        >
                            구독 취소하기
                        </button>
                    </div>

                    {/* 최근 결제 내역 */}
                    <div className="bg-white rounded-xl shadow-lg p-6">
                        <h2 className="text-xl font-bold mb-4">최근 결제 내역</h2>
                        <ul className="divide-y divide-gray-200">
                            <li className="py-2 flex justify-between">
                                <span>2025-09-01</span>
                                <span className="font-semibold">20,000 KRW</span>
                            </li>
                            <li className="py-2 flex justify-between">
                                <span>2025-08-01</span>
                                <span className="font-semibold">20,000 KRW</span>
                            </li>
                        </ul>
                        <button className="mt-4 w-full py-2 bg-gray-100 rounded-lg hover:bg-gray-200">
                            전체 결제 내역 보기
                        </button>
                    </div>

                    {/* 결제 수단 */}
                    <div className="bg-white rounded-xl shadow-lg p-6">
                        <h2 className="text-xl font-bold mb-4">결제 수단</h2>
                        {subscription?.paymentMethod ? (
                            <>
                                <p>
                                    등록된 카드 : {subscription.paymentMethod.provider}{" "}
                                    {subscription.paymentMethod.maskedNo}
                                </p>
                                <div className="flex gap-2 mt-4">
                                    <button className="flex-1 py-2 bg-blue-500 text-white rounded-lg hover:bg-blue-600"
                                            onClick={handleChangePaymentMethod} >수단 변경</button>
                                    <button
                                        className="flex-1 py-2 bg-red-500 text-white rounded-lg hover:bg-red-600"
                                        onClick={handleDeletePaymentMethod}
                                    >
                                        삭제
                                    </button>
                                </div>
                            </>
                        ) : (<p className="text-gray-500">등록된 결제수단이 없습니다.</p>)
                        }
                    </div>

                    {/* 고객센터 */}
                    <div className="bg-white rounded-xl shadow-lg p-6">
                        <h2 className="text-xl font-bold mb-4">고객센터</h2>
                        <p className="text-gray-600">궁금한 점이 있으신가요?</p>
                        <Link
                            to="/supportMain"
                            className="mt-4 w-full inline-block text-center py-2 bg-green-500 text-white rounded-lg hover:bg-green-600"
                        >
                            문의하기
                        </Link>
                    </div>
                </div>
            </div>

            {/* 모달 */}
            {showModal && (
                <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50 z-50">
                    <div className="bg-white rounded-lg shadow-lg p-6 w-full max-w-md">
                        <h2 className="text-xl font-bold text-gray-800 mb-4">구독 취소</h2>
                        <p className="text-gray-600 mb-6">
                            정말로 구독을 취소하시겠습니까? 결제가 환불 처리됩니다.
                        </p>
                        <div className="flex justify-end gap-3">
                            <button
                                onClick={() => setShowModal(false)}
                                className="px-4 py-2 bg-gray-200 rounded-lg hover:bg-gray-300"
                            >
                                취소
                            </button>
                            <button
                                onClick={handleCancel}
                                className="px-4 py-2 bg-red-500 text-white rounded-lg hover:bg-red-600"
                            >
                                확인
                            </button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
}
