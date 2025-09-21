import React, {useState} from "react";
import headerLogo from "../assets/headerLogo.png";
import "../style/Login.css";   // 스타일 불러오기
import axios from "axios";

function Login() {
    const [id, setId] = useState("");
    const [password, setPassword]= useState("");
    const handleSubmit = async (e) => {
        e.preventDefault(); // 새로고침 방지
        console.log(id, password)
        try {
            const res = await axios.post("http://localhost:8080/api/users/login", {
                userIdStr: id,
                password: password,
            });
            console.log("로그인 성공 : "+ res.data);
            alert("로그인 성공")
            window.location.href='/'
        } catch (err)   {
            console.log("로그인 실패 : " + err)

            alert("아이디 또는 비밀번호가 올바르지 않습니다.")
        }
    }
    return (
        <div className="login-container">
            <div className="login-box">
                <img src={headerLogo} alt="Recurv Logo" className="login-logo" />

                <form className="login-form" onSubmit={handleSubmit}>
                    <div>
                        <label>아이디</label>
                        <input type="text" placeholder="아이디를 입력하세요"
                               name='id' onChange={e => setId(e.target.value)}/>
                    </div>

                    <div className="password">
                        <label>비밀번호</label>
                        <input type="password" placeholder="••••••••" name='password'
                        onChange={e => setPassword(e.target.value)}/>
                    </div>

                    <button type="submit" className="login-button">
                        로그인
                    </button>
                </form>

                <p className="login-footer">
                    아직 계정이 없으신가요?{" "}
                    <a href="/register">회원가입</a>
                </p>
            </div>
        </div>
    );
}

export default Login;
