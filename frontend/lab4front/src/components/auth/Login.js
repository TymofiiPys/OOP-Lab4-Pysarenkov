import React, {useEffect, useState} from "react";
// import {useNavigate} from "react-router-dom";
import {useAuth} from "./AuthProvider";
import "./Login.css"

function Login() {
    // const navigate = useNavigate();
    const [input, setInput] = useState({
        email: "",
        password: "",
        signup: false
    });
    const [errMsg, setErrMsg] = useState("")
    const auth = useAuth();

    useEffect(() => {
        setErrMsg('');
    }, [input.email, input.password])

    const handleSubmit = async (e) => {
        e.preventDefault();
        console.log(JSON.stringify(input));
        let err = auth.loginAction(input);
        if (!err?.response) {
            setErrMsg("No Response");
        } else if (err.response?.status === 400) {
            setErrMsg("Missing Username or Password");
        } else if (err.response?.status === 401) {
            setErrMsg("Unauthorized");
        } else {
            setErrMsg("Login failed");
        }
    }

    const handleInput = (e) => {
        const {name, value} = e.target;
        setInput((prev) => ({
            ...prev,
            [name]: value,
        }));
    };
    return (
        <div className="login-container">
            <p className={errMsg ? "errmsg" : "offscreen"} aria-live="assertive">{errMsg}</p>
            <h1 className="login-header">Sign in</h1>
            <form onSubmit={handleSubmit} className="login-form">
                <div className="form-group">
                    <label htmlFor="email">Email:</label>
                    <input type="text" id="email" name="email" autoComplete="off" onChange={handleInput}
                           value={input.email} required className="form-control"/>
                </div>
                <div>
                    <label htmlFor="password">Password:</label>
                    <input type="password" id="password" name="password" onChange={handleInput}
                           value={input.password} required className="form-control"/>
                </div>
                <div className="form-group form-check">
                    <label htmlFor="signup" className="form-check-label">Sign Up</label>
                    <input type="checkbox" name="signup" onChange={handleInput}
                           checked={input.signup} className="form-check-input"/>
                    <br/>
                </div>
                <button type="submit" className="btn btn-primary">Sign in</button>
            </form>
        </div>
    )
}

export default Login;