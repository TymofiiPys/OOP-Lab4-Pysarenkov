import React, {useEffect, useRef, useState} from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";

function Login() {
    const navigate = useNavigate();
    // const location = useLocation();
    // const from = location.state?.from?.pathname || "/home";

    const userRef = useRef();
    const errRef = useRef();

    const [email, setEmail] = useState('');
    const [pwd, setPwd] = useState('');
    const [errMsg, setErrMsg] = useState('');
    const [success, setSuccess] = useState(false)
    const [signup, setSignup] = useState(false)

    useEffect(() => {
        userRef.current?.focus();
    }, [])

    useEffect(() => {
        setErrMsg('');
    }, [email, pwd])

    const handleSubmit = async (e) => {
        e.preventDefault();
        const url = signup === true ? "/api/auth?signup=true" : "/api/auth"
        axios.post(url, {
                email: email,
                password: pwd
            }
        ).then(response => {
            const token = response.data.jwt;
            console.log(response?.data.jwt);
            setEmail('');
            setPwd('');
            setSuccess(true);
            window.localStorage.setItem('Token', response.data.jwt);
            navigate('/home')
        })
            .catch(err => {
                console.error('Error:', err); // Handle any errors
                if (!err?.response) {
                    setErrMsg("No Response");
                }
                else if (err.response?.status === 400) {
                    setErrMsg("Missing Username or Password");
                }
                else if (err.response?.status === 401) {
                    setErrMsg("Unauthorized");
                } else {
                    setErrMsg("Login failed");
                }
                errRef.current.focus();
            });
    }

    return (
        <div>
            <p ref={errRef} className={errMsg ? "errmsg" : "offscreen"} aria-live="assertive">{errMsg}</p>
            <h1>Sign in</h1>
            <form onSubmit={handleSubmit}>
                <label htmlFor="email">Email:</label>
                <input type="text" id="email" autoComplete="off" onChange={(e) => setEmail(e.target.value)} value={email} required />
                <label htmlFor="password">Password:</label>
                <input type="password" id="password" onChange={(e) => setPwd(e.target.value)} value={pwd} required />
                <label>Sign Up</label>
                <input type="checkbox" onChange={(e) => setSignup(e.target.checked)} checked={signup}/>
                <button>Sign in</button>
            </form>
        </div>
    )
}

export default Login;