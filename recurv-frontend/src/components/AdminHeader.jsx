import React from "react";
import { useNavigate } from "react-router-dom";
import headerLogo from "../assets/headerLogo.png";

function AdminHeader() {
    const navigate = useNavigate();

    const menuItems = [
        { path: "/admin/billing", label: "요금제 관리" },
        { path: "/admin/partner", label: "거래처 관리" },
        { path: "/admin/subscription", label: "구독 관리" },
        { path: "/admin/support/list", label: "고객 문의" },
        { path: "/admin/notification", label: "알림 이력 관리"}
    ];

    const handleLogout = () => {
        if (window.confirm("로그아웃 하시겠습니까?")) {
            localStorage.clear();
            navigate("/login");
        }
    };

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
            {/* ✅ 로고 클릭 시 관리자 홈으로 */}
            <div
                onClick={() => navigate("/adminHome")}
                style={{ display: "flex", alignItems: "center", cursor: "pointer" }}
            >
                <img
                    src={headerLogo}
                    alt="Recurv Logo"
                    style={{ height: "60px", objectFit: "contain" }}
                />
            </div>

            {/* ✅ 네비게이션 메뉴 */}
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
                                marginLeft: "25px",
                                fontWeight: "500",
                                color: "#fff",
                            }}
                        >
                            {item.label}
                        </li>
                    ))}
                </ul>

                {/* ✅ 로그아웃 버튼 */}
                <button
                    onClick={handleLogout}
                    style={{
                        marginLeft: "35px",
                        padding: "8px 16px",
                        backgroundColor: "#e53e3e",
                        border: "none",
                        borderRadius: "6px",
                        color: "#fff",
                        cursor: "pointer",
                        fontWeight: "500",
                    }}
                >
                    로그아웃
                </button>
            </nav>
        </header>
    );
}

export default AdminHeader;
