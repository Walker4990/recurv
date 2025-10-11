import React, {useState} from "react";
import axios from "axios";

function Register() {
    const [name, setName] = useState("");
    const [residentNumber, setResidentNumber] = useState("");
    const [id, setId] = useState("");
    const [address, setAddress] = useState("");
    const [detailAddress, setDetailAddress] = useState(""); // 상세 주소
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState('')
    const [nickname, setNickname] = useState("");
    const [email, setEmail] = useState("");
    const [phone, setPhone] = useState("");
    const [passwordVer, setPasswordVer] = useState(false);
    const openPostcode = () => {
        new window.daum.Postcode({
            oncomplete: function (data) {
                setAddress(data.address); // 도로명 주소
            },
        }).open();
    };
    const passwordVerify = (e) => {
        setConfirmPassword(e.target.value);       // 입력값을 state에 저장
        setPasswordVer(e.target.value === password); // 입력값과 password 비교
    };
    const handleSubmit = async (e) =>{
        e.preventDefault();

        if (!passwordVer){
            alert('비밀번호가 일치하지 않습니다.')
            return
        }
        const data = {
            name,                       // 이름
            residentNumber,             // 주민등록번호
            userIdStr: id,              // 로그인 아이디
            password,
            email,
            nickname,
            phone,
            address,                    // 기본 주소
            detailAddress               // 상세 주소
        };
        try{
            const res = await axios.post("http://localhost:8080/api/users/register", data);
            alert("회원가입 성공!")
            window.location.href='/';
        } catch (error) {
            console.log(error);
            alert("회원가입 실패")
        }
    }

    return (
        <div
            style={{
                minHeight: "100vh",
                display: "flex",
                justifyContent: "center",
                alignItems: "center",
                background: "#F9FAFB",
                fontFamily: "sans-serif",
            }}
        >
            <div
                style={{
                    width: "400px",
                    padding: "40px",
                    background: "#fff",
                    borderRadius: "12px",
                    boxShadow: "0 4px 12px rgba(0,0,0,0.05)",
                }}
            >
                <h2
                    style={{
                        textAlign: "center",
                        marginBottom: "30px",
                        color: "#00A651",
                    }}
                >
                    Recurv 회원가입
                </h2>

                <form onSubmit={handleSubmit}>
                    {/* 이름 */}
                    <div style={{ marginBottom: "20px" }}>
                        <label style={{ display: "block", marginBottom: "6px" }}>이름</label>
                        <input
                            type="text"
                            name="name"
                            placeholder="이름을 입력하세요."
                            style={{
                                width: "100%",
                                padding: "12px",
                                border: "1px solid #ddd",
                                borderRadius: "6px",
                            }}
                            onChange={(e)=>{setName(e.target.value)}}
                        />
                    </div>

                    {/* 주민등록번호 */}
                    <div style={{ marginBottom: "20px" }}>
                        <label style={{ display: "block", marginBottom: "6px" }}>
                            주민등록번호
                        </label>
                        <input
                            type="text"
                            name="residentNumber"
                            placeholder="000000-0000000"
                            style={{
                                width: "100%",
                                padding: "12px",
                                border: "1px solid #ddd",
                                borderRadius: "6px",
                            }}
                            onChange={(e)=>{setResidentNumber(e.target.value)}}
                        />
                    </div>
                    {/* 전화번호 */}
                    <div style={{ marginBottom: "20px" }}>
                        <label style={{ display: "block", marginBottom: "6px" }}>
                            전화번호
                        </label>
                        <input
                            type="text"
                            name="phone"
                            placeholder="010-0000-0000"
                            style={{
                                width: "100%",
                                padding: "12px",
                                border: "1px solid #ddd",
                                borderRadius: "6px",
                            }}
                            onChange={(e)=>{setPhone(e.target.value)}}
                        />
                    </div>
                    {/* 아이디 */}
                    <div style={{ marginBottom: "20px" }}>
                        <label style={{ display: "block", marginBottom: "6px" }}>아이디</label>
                        <input
                            type="text"
                            name="id"
                            placeholder="아이디를 입력하세요."
                            style={{
                                width: "100%",
                                padding: "12px",
                                border: "1px solid #ddd",
                                borderRadius: "6px",
                            }}
                            onChange={(e)=>{setId(e.target.value)}}
                        />
                    </div>
                    {/* 비밀번호 */}
                    <div style={{ marginBottom: "20px" }}>
                        <label style={{ display: "block", marginBottom: "6px" }}>비밀번호</label>
                        <input
                            type="password"
                            name="password"
                            placeholder="8자 이상"
                            style={{
                                width: "100%",
                                padding: "12px",
                                border: "1px solid #ddd",
                                borderRadius: "6px",
                            }}
                            onChange={(e)=>{setPassword(e.target.value)}}
                        />

                    </div>

                    {/* 비밀번호 확인 */}
                    <div style={{ marginBottom: "30px" }}>
                        <label style={{ display: "block", marginBottom: "6px" }}>
                            비밀번호 확인
                        </label>
                        <input
                            type="password"
                            name="confirmPassword"
                            placeholder="비밀번호 재입력"
                            style={{
                                width: "100%",
                                padding: "12px",
                                border: "1px solid #ddd",
                                borderRadius: "6px",
                            }}
                            onChange={passwordVerify}
                        />
                        {confirmPassword && !passwordVer && (
                            <p style={{ color: "red", fontSize: "12px" }}>비밀번호가 일치하지 않습니다.</p>
                        )}
                        {confirmPassword && passwordVer && (
                            <p style={{ color: "green", fontSize: "12px" }}>비밀번호가 일치합니다.</p>
                        )}
                    </div>
                    {/* 주소 */}
                    <div style={{ marginBottom: "20px" }}>
                        <label style={{ display: "block", marginBottom: "6px" }}>주소</label>

                        {/* 기본 주소 */}
                        <div style={{ display: "flex", gap: "10px", marginBottom: "10px" }}>
                            <input
                                type="text"
                                name="address"
                                value={address}
                                placeholder="주소를 검색하세요"
                                readOnly
                                style={{
                                    flex: 1,
                                    padding: "12px",
                                    border: "1px solid #ddd",
                                    borderRadius: "6px",
                                }}
                            />
                            <button
                                type="button"
                                onClick={openPostcode}
                                style={{
                                    padding: "12px 16px",
                                    background: "#00A651",
                                    color: "#fff",
                                    border: "none",
                                    borderRadius: "6px",
                                    cursor: "pointer",
                                }}
                            >
                                검색
                            </button>
                        </div>

                        {/* 상세 주소 */}
                        <input
                            type="text"
                            name="detailAddress"
                            placeholder="상세주소를 입력하세요 (건물명, 동/호수 등)"
                            style={{
                                width: "100%",
                                padding: "12px",
                                border: "1px solid #ddd",
                                borderRadius: "6px",
                            }}
                            onChange={(e) => setDetailAddress(e.target.value)}
                        />
                    </div>


                    {/* 닉네임 */}
                    <div style={{ marginBottom: "20px" }}>
                        <label style={{ display: "block", marginBottom: "6px" }}>닉네임</label>
                        <input
                            type="text"
                            name="nickname"
                            placeholder="닉네임을 입력하세요"
                            style={{
                                width: "100%",
                                padding: "12px",
                                border: "1px solid #ddd",
                                borderRadius: "6px",
                            }}
                            onChange={(e) => setNickname(e.target.value)}
                        />
                    </div>

                    {/* 이메일 */}
                    <div style={{ marginBottom: "20px" }}>
                        <label style={{ display: "block", marginBottom: "6px" }}>이메일</label>
                        <input
                            type="email"
                            name="email"
                            placeholder="example@recurv.com"
                            style={{
                                width: "100%",
                                padding: "12px",
                                border: "1px solid #ddd",
                                borderRadius: "6px",
                            }}
                            onChange={(e) => setEmail(e.target.value)}
                        />
                    </div>



                    <button
                        type="submit"
                        style={{
                            width: "100%",
                            background: "#00A651",
                            color: "#fff",
                            padding: "14px",
                            border: "none",
                            borderRadius: "6px",
                            fontSize: "16px",
                            cursor: "pointer",
                        }}
                    >
                        회원가입
                    </button>
                </form>

                <p
                    style={{
                        marginTop: "20px",
                        textAlign: "center",
                        fontSize: "14px",
                        color: "#666",
                    }}
                >
                    이미 계정이 있으신가요?{" "}
                    <a href="/login" style={{ color: "#00A651", textDecoration: "none" }}>
                        로그인
                    </a>
                </p>
            </div>
        </div>
    );
}

export default Register;
