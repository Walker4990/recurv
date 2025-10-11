// src/components/UserHeader.jsx
import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import headerLogo from "../assets/headerLogo.png";

function UserHeader() {
    const navigate = useNavigate();
    const [nickname, setNickname] = useState("");
    const [isLoggedIn, setIsLoggedIn] = useState(false);

    const menuItems = [
        { path: "/", label: "홈" },
        { path: "/subscriptions/me", label: "내 구독" },
        { path: "/supportMain", label: "고객센터" },
    ];

    useEffect(() => {
        const token = localStorage.getItem("token");
        if (!token) {
            setIsLoggedIn(false);
            return;
        }

        axios
            .get("http://localhost:8080/api/users/me", {
                headers: { Authorization: `Bearer ${token}` },
            })
            .then((res) => {
                setNickname(res.data.nickname);
                setIsLoggedIn(true);
            })
            .catch((err) => {
                console.error("유저 정보 불러오기 실패:", err);
                setIsLoggedIn(false);
            });
    }, []);

    // 로그아웃 처리
    const handleLogout = () => {
        localStorage.removeItem("token");
        localStorage.removeItem("userId");
        setNickname("");
        setIsLoggedIn(false);
        navigate("/login");
    };

    return (
        <header
            style={{
                display: "flex",
                alignItems: "center",
                justifyContent: "space-between",
                padding: "15px 30px",
                background: "#fff",
                borderBottom: "1px solid #E5E7EB",
            }}
        >
            <div
                onClick={() => navigate("/")}
                style={{ display: "flex", alignItems: "center", cursor: "pointer" }}
            >
                <img
                    src={headerLogo}
                    alt="Recurv Logo"
                    style={{ height: "60px", objectFit: "contain" }}
                />
            </div>

            <nav style={{ display: "flex", alignItems: "center" }}>
                <ul
                    style={{
                        display: "flex",
                        listStyle: "none",
                        margin: 0,
                        padding: 0,
                    }}
                >
                    {menuItems.map((item) => (
                        <li
                            key={item.path}
                            onClick={() => navigate(item.path)}
                            style={{
                                cursor: "pointer",
                                marginLeft: "20px",
                                fontWeight: "500",
                                color: "#333",
                            }}
                            onMouseEnter={(e) => (e.currentTarget.style.color = "#00A651")}
                            onMouseLeave={(e) => (e.currentTarget.style.color = "#333")}
                        >
                            {item.label}
                        </li>
                    ))}
                </ul>

                {isLoggedIn ? (
                    <>
            <span
                style={{
                    marginLeft: "30px",
                    fontWeight: "600",
                    color: "#00A651",
                }}
            >
              {nickname} 님 환영합니다 👋
            </span>
                        <button
                            onClick={handleLogout}
                            style={{
                                marginLeft: "20px",
                                background: "#00A651",
                                color: "#fff",
                                border: "none",
                                padding: "8px 16px",
                                borderRadius: "6px",
                                cursor: "pointer",
                                fontWeight: "500",
                            }}
                        >
                            로그아웃
                        </button>
                    </>
                ) : (
                    <>
                        <button
                            onClick={() => navigate("/login")}
                            style={{
                                marginLeft: "20px",
                                background: "#00A651",
                                color: "#fff",
                                border: "none",
                                padding: "8px 16px",
                                borderRadius: "6px",
                                cursor: "pointer",
                                fontWeight: "500",
                            }}
                        >
                            로그인
                        </button>
                        <button
                            onClick={() => navigate("/register")}
                            style={{
                                marginLeft: "10px",
                                background: "#fff",
                                color: "#00A651",
                                border: "1px solid #00A651",
                                padding: "8px 16px",
                                borderRadius: "6px",
                                cursor: "pointer",
                                fontWeight: "500",
                            }}
                        >
                            회원가입
                        </button>
                    </>
                )}
            </nav>
        </header>
    );
}

export default UserHeader;
