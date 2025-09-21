// src/components/UserHeader.jsx
import React from "react";
import { useNavigate } from "react-router-dom";
import headerLogo from "../assets/headerLogo.png";


function UserHeader() {
    const navigate = useNavigate();

    const menuItems = [
        { path: "/", label: "홈" },
        { path: "/my-subscription", label: "내 구독" },
        { path: "/payment-history", label: "결제 내역" },
        { path: "/supportMain", label: "고객센터" },
    ];

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
            <nav>
                <ul style={{ display: "flex", listStyle: "none", margin: 0, padding: 0 }}>
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
            </nav>
        </header>

    );
}

export default UserHeader;
