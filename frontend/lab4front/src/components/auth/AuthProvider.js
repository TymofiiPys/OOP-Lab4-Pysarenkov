import {useContext, createContext, useState} from "react";
import {useNavigate} from "react-router-dom";
import axios from "axios";

const AuthContext = createContext();

const AuthProvider = ({children}) => {
    // const [user, setUser] = useState(null);
    const [token, setToken] = useState(localStorage.getItem("site") || "");
    const navigate = useNavigate();
    const loginAction = (data) => {
        const url = data.signup === "on" ? "/api/auth?signup=true" : "/api/auth"
        axios.post(url, {
            email: data.email,
            password: data.password
        }).then(response => {
            console.log(response?.data.jwt);
            setToken(response.data.jwt);
            localStorage.setItem("Token", response.data.jwt);
            navigate("/home");
        }).catch(err => {
            console.error('Error:', err);
            // if (!err?.response) {
            //     setErrMsg("No Response");
            // } else if (err.response?.status === 400) {
            //     setErrMsg("Missing Username or Password");
            // } else if (err.response?.status === 401) {
            //     setErrMsg("Unauthorized");
            // } else {
            //     setErrMsg("Login failed");
            // }
            // errRef.current.focus();
            return err;
        });
        return "";
    };

    const logOut = () => {
        setToken("");
        localStorage.removeItem("Token");
        navigate("/login");
    };

    return (
        <AuthContext.Provider value={{token, loginAction, logOut}}>
            {children}
        </AuthContext.Provider>
    );

};

export default AuthProvider;

export const useAuth = () => {
    return useContext(AuthContext);
};