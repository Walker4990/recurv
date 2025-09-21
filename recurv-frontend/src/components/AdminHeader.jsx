// src/components/AdminHeader.jsx
import React from "react";
import { useNavigate } from "react-router-dom";
import logo from "../assets/recurvLogo.png"

function AdminHeader() {
    const navigate = useNavigate();

    const menuItems = [
        { path: "/billing", label: "요금제 관리" },
        { path: "/partner", label: "거래처 관리" },
        { path: "/subscription", label: "구독 관리" },
        { path: "/invoice", label: "청구 관리" },
        { path: "/payment", label: "결제 관리" },
    ];

    return (
        <header
            style={{
                display: "flex",
                alignItems: "center",
                justifyContent: "space-between",
                padding: "15px 40px",
                background: "#222",
                color: "#fff",
            }}
        >
            <div
                onClick={() => navigate("/")}
                style={{ display: "flex", alignItems: "center", cursor: "pointer" }}
            >
                <img
                    src={logo}
                    alt="Recurv Logo"
                    style={{ height: "60px",
                        width: "auto",
                        objectFit: "contain",
                        display: "block",}}
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
                                marginLeft: "25px",
                                fontWeight: "500",
                                color: "#fff",
                            }}
                        >
                            {item.label}
                        </li>
                    ))}
                </ul>
            </nav>
        </header>
    );
}

export default AdminHeader;
