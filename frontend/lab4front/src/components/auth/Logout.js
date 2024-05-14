import React from "react";
import {useAuth} from "./AuthProvider";
import "../Home.css"

function LogoutButton() {
    const auth = useAuth();

    return (
        <button onClick={auth.logOut} className="logout-button">
            Log Out
        </button>
    );
}

export default LogoutButton;