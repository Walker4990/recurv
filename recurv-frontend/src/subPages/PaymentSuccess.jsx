import { useSearchParams, Link } from "react-router-dom";
import { useEffect, useState } from "react";

function PaymentSuccess() {
    const [params] = useSearchParams();
    const [paymentData, setPaymentData] = useState(null);

    useEffect(() => {
        const confirmPayment = async () => {
            try {
                const res = await fetch("http://localhost:8080/api/payment/confirm", {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify({
                        paymentKey: params.get("paymentKey"),
                        orderId: params.get("orderId"),
                        amount: params.get("amount"),
                    }),
                });

                const data = await res.json();
                console.log("결제 승인 결과:", data);
                setPaymentData(data);
            } catch (error) {
                console.error("결제 승인 중 오류:", error);
            }
        };
        confirmPayment();
    }, [params]);

    return (
        <div className="flex flex-col items-center justify-center min-h-screen bg-gray-50 px-4">
            <div className="bg-white shadow-lg rounded-2xl p-8 max-w-md w-full text-center">
                <div className="text-green-500 text-5xl mb-4">✅</div>
                <h1 className="text-2xl font-bold text-gray-800 mb-2">결제 성공</h1>
                <p className="text-gray-600 mb-4">구독이 정상적으로 완료되었습니다.</p>

                {/* 결제 정보 출력 */}
                <div className="bg-gray-100 rounded-lg p-4 text-left mb-6">
                    <p className="text-sm text-gray-700">
                        <span className="font-medium">주문 번호:</span> {params.get("orderId")}
                    </p>
                    <p className="text-sm text-gray-700">
                        <span className="font-medium">결제 금액:</span> {params.get("amount")} 원
                    </p>
                    {paymentData && (
                        <p className="text-sm text-gray-700">
                            <span className="font-medium">승인 시각:</span>{" "}
                            {new Date(paymentData.approvedAt).toLocaleString()}
                        </p>
                    )}
                </div>

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
