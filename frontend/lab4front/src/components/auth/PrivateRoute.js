import React from "react";
import {Navigate, Outlet} from "react-router-dom";
import {useAuth} from "./AuthProvider";
import {jwtDecode} from "jwt-decode";
import Unauthorized from "./Unauthorized";

const PrivateRoute = (adminRequired) => {
    const user = useAuth();
    console.log("User token " + user.token);
    if (!user.token) return <Navigate to="/login"/>;
    const auth = jwtDecode(user.token);
    if (adminRequired.adminRequired === true) {
        if (adminRequired.adminRequired === auth.isAdmin) {
            return <Outlet/>;
        }
        else {
            return <Unauthorized/>;
        }
    }
    return <Outlet/>;
};

export default PrivateRoute;