import React from "react";
import {useAuth} from "./AuthProvider";

function LogoutButton() {
    const auth = useAuth();

    return (
        <button onClick={auth.logOut}>
            Log Out
        </button>
    );
}

export default LogoutButton;