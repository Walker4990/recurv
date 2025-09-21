// src/components/SubscribeButton.jsx
import { loadTossPayments } from "@tosspayments/payment-sdk";
import { useNavigate } from "react-router-dom";

function SubscribeButton({ plan }) {
    const navigate = useNavigate();

    const handleSubscribe = async () => {
        try {
            const tossPayments = await loadTossPayments("test_ck_ZLKGPx4M3M12wnYqg5lo3BaWypv1");

            await tossPayments.requestPayment("카드", {
                amount: plan.price,
                orderId: "order-" + new Date().getTime(),
                orderName: plan.name,
                successUrl: "http://localhost:3000/payment/success",
                failUrl: "http://localhost:3000/payment/fail",
                customerName: "홍길동",
            });
        } catch (error) {
            if (error.code === "USER_CANCEL") {
                navigate("/payment/fail?message=사용자취소&code=USER_CANCEL");
            } else {
                console.error("결제 중 오류:", error);
                navigate(`/payment/fail?message=${error.message}&code=${error.code}`);
            }
        }
    };

    return (
        <button
            onClick={handleSubscribe}
            className="bg-green-500 hover:bg-green-600 text-white px-4 py-2 rounded-lg"
        >
            {plan.name} 구독하기
        </button>
    );
}

export default SubscribeButton;
