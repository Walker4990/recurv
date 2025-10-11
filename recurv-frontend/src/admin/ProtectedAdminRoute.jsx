import React from "react";
import { Navigate } from "react-router-dom";

export default function ProtectedAdminRoute({ children }) {
    const role = localStorage.getItem("role");

    if (role !== "ROLE_ADMIN") {
        alert("관리자만 접근할 수 있습니다.");
        return <Navigate to="/" replace />;
    }

    return children;
}
