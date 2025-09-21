import React from "react";
import { useNavigate } from "react-router-dom";

export default function Footer() {
    const navigate = useNavigate();
    return (
        <footer className="footer"
        style={{ background: "#F9FAFB", padding: "40px 80px",
            borderTop: "1px solid #E5E7EB", color:"666", fontSize: '14px'
        }}>
        <div
            style={{display:'flex', justifyContent:'space-between', flexWrap:'wrap',
                    gap:'20px'}}
        >
            {/* 회사 정보 */}
            <div>
                <h3 style={{ marginBottom: "10px", color: "#00A651" }}>Recurv</h3>
                <p>서울특별시 ○○구 ○○로 123</p>
                <p>사업자등록번호 123-45-67890</p>
                <p>문의: support@recurv.com</p>
            </div>

            {/* 링크 */}
            <div>
                <h4 style={{ marginBottom: "10px", color: "#333" }}>바로가기</h4>
                <ul style={{ listStyle: "none", padding: 0, margin: 0 }}>
                    <li
                        onClick={() => navigate("/terms")}
                        style={{ cursor: "pointer", marginBottom: "6px" }}
                    >
                        이용약관
                    </li>
                    <li
                        onClick={() => navigate("/privacy")}
                        style={{ cursor: "pointer" }}
                    >
                        개인정보처리방침
                    </li>
                </ul>
            </div>
        </div>

            {/* 저작권 */}
            <div
                style={{
                    textAlign: "center",
                    marginTop: "30px",
                    fontSize: "12px",
                    color: "#999",
                }}
            >
                © 2025 Recurv. All rights reserved.
            </div>
        </footer>

    )
}