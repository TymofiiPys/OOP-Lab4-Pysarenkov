import React, {useEffect, useState} from "react";
// import {useNavigate} from "react-router-dom";
import {useAuth} from "./AuthProvider";

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
        <div>
            <p className={errMsg ? "errmsg" : "offscreen"} aria-live="assertive">{errMsg}</p>
            <h1>Sign in</h1>
            <form onSubmit={handleSubmit}>
                <label htmlFor="email">Email:</label>
                <input type="text" id="email" name="email" autoComplete="off" onChange={handleInput}
                       value={input.email} required/>
                <label htmlFor="password">Password:</label>
                <input type="password" id="password" name="password" onChange={handleInput}
                       value={input.password} required/>
                <label>Sign Up</label>
                <input type="checkbox" name="signup" onChange={handleInput}
                       checked={input.signup}/>
                <button>Sign in</button>
            </form>
        </div>
    )
}

export default Login;