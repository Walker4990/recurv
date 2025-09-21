import { useSearchParams, Link } from "react-router-dom";

function PaymentFail() {
    const [params] = useSearchParams();
    const message = params.get("message") || "결제가 실패했습니다.";
    const code = params.get("code") || "UNKNOWN";

    return (
        <div className="flex flex-col items-center justify-center min-h-screen bg-gray-50 px-4">
            <div className="bg-white shadow-lg rounded-2xl p-8 max-w-md w-full text-center">
                <div className="text-red-500 text-5xl mb-4">❌</div>
                <h1 className="text-2xl font-bold text-gray-800 mb-2">결제 실패</h1>
                <p className="text-gray-600 mb-4">{message}</p>
                <p className="text-sm text-gray-400 mb-6">에러 코드: {code}</p>

                <div className="flex justify-center gap-4">
                    <Link
                        to="/"
                        className="bg-blue-500 hover:bg-blue-600 text-white px-4 py-2 rounded-lg"
                    >
                        메인으로 가기
                    </Link>
                    <Link
                        to="/support"
                        className="bg-gray-200 hover:bg-gray-300 text-gray-800 px-4 py-2 rounded-lg"
                    >
                        고객센터 문의
                    </Link>
                </div>
            </div>
        </div>
    );
}

export default PaymentFail;
