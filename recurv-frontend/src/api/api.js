import axios from "axios";

const api = axios.create({
    baseURL : "http://localhost:8080", // 백엔드 서버주소
});

api.interceptors.request.use((config) => {
    const token= localStorage.getItem("token");
    console.log("요청 토큰:", token);
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
})
// 응답 인터셉터 - 인증/권한 에러 처리
api.interceptors.response.use(
    (response) => response,
    (error) => {
        if (error.response) {
            const status = error.response.status;

            // 🔐 인증 안 된 사용자 (토큰 없음 or 만료)
            if (status === 401) {
                alert("로그인이 필요합니다. 다시 로그인해주세요.");
                localStorage.clear();
                window.location.href = "/login"; // 로그인 페이지로 이동
            }

            // 🚫 권한 없는 사용자 (예: ROLE_USER가 /api/admin/** 접근)
            if (status === 403) {
                alert("접근 권한이 없습니다.");
                window.location.href = "/"; // 홈이나 다른 페이지로 이동
            }
        }
        return Promise.reject(error);
    }
);
export default api;