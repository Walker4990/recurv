import { useSearchParams, Link } from "react-router-dom";
import { useEffect, useState } from "react";
import api from "../api/api.js";

function PaymentSuccess() {
    const [params] = useSearchParams();
    const [subscription, setSubscription] = useState(null);

    useEffect(() => {
        const fetchSubscription = async () => {
            try {
                const orderId = params.get("orderId");

                // ✅ confirm은 Toss → Webhook으로만 처리됨
                // 프론트에서는 DB 저장 안 건드리고 조회만 한다
                const res = await api.get(`/subscriptions/${orderId}`);
                setSubscription(res.data);

            } catch (error) {
                console.error("구독/결제 조회 실패:", error);
            }
        };

        fetchSubscription();

    }, [params]); // ✅ useEffect 의존성 배열 추가

    return (
        <div className="flex flex-col items-center justify-center min-h-screen bg-gray-50 px-4">
            <div className="bg-white shadow-lg rounded-2xl p-8 max-w-md w-full text-center">
                <div className="text-green-500 text-5xl mb-4">✅</div>
                <h1 className="text-2xl font-bold text-gray-800 mb-2">결제 성공</h1>
                <p className="text-gray-600 mb-4">구독이 정상적으로 완료되었습니다.</p>

                {/* 구독 정보 출력 */}
                {subscription && (
                    <div className="bg-gray-100 rounded-lg p-4 text-left mb-6">
                        <p className="text-sm text-gray-700">
                            <span className="font-medium">주문 번호:</span> {subscription.id}
                        </p>
                        <p className="text-sm text-gray-700">
                            <span className="font-medium">요금제:</span> {subscription.plan}
                        </p>
                        <p className="text-sm text-gray-700">
                            <span className="font-medium">상태:</span> {subscription.status}
                        </p>
                        <p className="text-sm text-gray-700">
                            <span className="font-medium">결제 금액:</span> {subscription.amount} {subscription.currency}
                        </p>
                        <p className="text-sm text-gray-700">
                            <span className="font-medium">시작일:</span> {subscription.startedAt}
                        </p>
                        <p className="text-sm text-gray-700">
                            <span className="font-medium">만료일:</span> {subscription.expiredAt}
                        </p>
                    </div>
                )}

                <div className="flex justify-center gap-4">
                    <Link
                        to="/"
                        className="bg-blue-500 hover:bg-blue-600 text-white px-4 py-2 rounded-lg"
                    >
                        메인으로 가기
                    </Link>
                    <Link
                        to="/billing"
                        className="bg-gray-200 hover:bg-gray-300 text-gray-800 px-4 py-2 rounded-lg"
                    >
                        요금제 보기
                    </Link>
                </div>
            </div>
        </div>
    );
}

export default PaymentSuccess;
