import React from "react";
import { useNavigate } from "react-router-dom";
import logo from "../assets/recurvLogo.png";
import Footer from "./Footer";
function Home() {
    const navigate = useNavigate();

    return (
        <div style={{ fontFamily: "sans-serif", color: "#333" }}>
            {/* Hero 섹션 */}
            <section
                style={{
                    display: "flex",
                    alignItems: "center",
                    justifyContent: "space-between",
                    padding: "100px 80px",
                    background: "#F9FAFB",
                }}
            >
                <div>
                    <h1 style={{ fontSize: "48px", marginBottom: "20px" }}>
                        정기결제를 쉽고 강력하게 <br />
                        <span style={{ color: "#00A651" }}>Recurv</span>
                    </h1>
                    <p style={{ fontSize: "18px", marginBottom: "30px" }}>
                        구독·결제·청구를 한 곳에서 관리하는 올인원 플랫폼
                    </p>
                    <button
                        onClick={() => navigate("/login")}
                        style={{
                            background: "#00A651",
                            color: "#fff",
                            border: "none",
                            padding: "15px 30px",
                            borderRadius: "8px",
                            fontSize: "16px",
                            cursor: "pointer",
                        }}
                    >
                        무료로 시작하기
                    </button>
                </div>
                <img
                    src={logo}
                    alt="Recurv Dashboard"
                    style={{
                        width: "400px",
                        objectFit: "contain",
                        borderRadius: "12px",
                        boxShadow: "0 4px 10px rgba(0,0,0,0.1)",
                    }}
                />
            </section>

            {/* 주요 기능 섹션 */}
            <section style={{ padding: "100px 80px", textAlign: "center" }}>
                <h2 style={{ fontSize: "36px", marginBottom: "50px" }}>주요 기능</h2>
                <div style={{ display: "flex", gap: "40px", justifyContent: "center" }}>
                    {[
                        { title: "구독/결제 자동화", desc: "결제 주기 및 청구서 자동 처리" },
                        { title: "청구·세금 정산", desc: "정산·세금처리까지 한 번에" },
                        { title: "구독 분석 대시보드", desc: "실시간 매출·구독 유지율 분석" },
                    ].map((f) => (
                        <div
                            key={f.title}
                            style={{
                                flex: 1,
                                background: "#fff",
                                borderRadius: "12px",
                                padding: "40px 20px",
                                boxShadow: "0 2px 8px rgba(0,0,0,0.05)",
                            }}
                        >
                            <h3 style={{ color: "#00A651", marginBottom: "15px" }}>{f.title}</h3>
                            <p>{f.desc}</p>
                        </div>
                    ))}
                </div>
            </section>

            {/* 요금제 섹션 */}
            <section
                style={{ padding: "100px 80px", background: "#F9FAFB", textAlign: "center" }}
            >
                <h2 style={{ fontSize: "36px", marginBottom: "50px" }}>요금제 안내</h2>
                <div style={{ display: "flex", gap: "40px", justifyContent: "center" }}>
                    {[
                        { name: "Basic", price: "₩0 /월", feature: "스타트업 추천" },
                        { name: "Pro", price: "₩29,000 /월", feature: "성장 중소기업" },
                        { name: "Enterprise", price: "맞춤 견적", feature: "대규모 ERP 구축" },
                    ].map((p) => (
                        <div
                            key={p.name}
                            style={{
                                flex: 1,
                                background: "#fff",
                                borderRadius: "12px",
                                padding: "40px 20px",
                                boxShadow: "0 2px 8px rgba(0,0,0,0.05)",
                            }}
                        >
                            <h3>{p.name}</h3>
                            <p style={{ fontSize: "24px", margin: "10px 0" }}>{p.price}</p>
                            <p>{p.feature}</p>
                            <button
                                onClick={() => navigate("/billing")}
                                style={{
                                    marginTop: "20px",
                                    background: "#00A651",
                                    color: "#fff",
                                    border: "none",
                                    padding: "10px 20px",
                                    borderRadius: "6px",
                                    cursor: "pointer",
                                }}
                            >
                                구독하기
                            </button>
                        </div>
                    ))}
                </div>
            </section>

            {/* ERP 제작 상담 CTA */}
            <section
                style={{
                    padding: "100px 80px",
                    background: "#00A651",
                    color: "#fff",
                    textAlign: "center",
                }}
            >
                <h2 style={{ fontSize: "36px", marginBottom: "20px" }}>
                    우리 회사에 맞는 ERP를 제작하고 싶나요?
                </h2>
                <p style={{ fontSize: "18px", marginBottom: "30px" }}>
                    맞춤 견적 및 구축 상담을 통해 전용 ERP 시스템을 제공합니다.
                </p>
                <button
                    onClick={() => navigate("/consulting")}
                    style={{
                        background: "#fff",
                        color: "#00A651",
                        border: "none",
                        padding: "15px 30px",
                        borderRadius: "8px",
                        fontSize: "16px",
                        cursor: "pointer",
                    }}
                >
                    제작 상담 신청하기
                </button>
            </section>
            <Footer />
        </div>
    );
}

export default Home;
