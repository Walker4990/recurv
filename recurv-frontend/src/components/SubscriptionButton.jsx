import { loadTossPayments } from "@tosspayments/payment-sdk";
import { useNavigate } from "react-router-dom";
import axios from "axios";

function SubscribeButton({ plan }) {
    const navigate = useNavigate();
    // const { users } = useAuth();
    const handleSubscribe = async () => {
        try {
            const token = localStorage.getItem("token");
            const userId = localStorage.getItem("userId"); // ✅ 로그인 시 저장해둔 값

            // 1️⃣ invoice 먼저 생성
            const res = await axios.post(
                "http://localhost:8080/api/invoices",
                {
                    partnerNo: userId, // ✅ userId 사용
                    planId: plan.plan_id,
                    amount: plan.price,
                },
                {
                    headers: {
                        Authorization: `Bearer ${token}`,
                        "Content-Type": "application/json",
                    },
                }
            );

            const invoice = res.data;
            // 2️⃣ Toss 결제 SDK 호출
            const tossPayments = await loadTossPayments("test_ck_ZLKGPx4M3M12wnYqg5lo3BaWypv1");

            await tossPayments.requestPayment("카드", {
                amount: invoice.totalAmount,
                orderId: invoice.invoiceNo,
                orderName: plan.name,
                successUrl: "http://localhost:3000/payment/success?orderId=" + invoice.invoiceNo,
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
