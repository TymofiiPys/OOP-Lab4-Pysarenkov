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
    console.log(JSON.stringify(auth));
    console.log(auth.isAdmin);
    console.log(adminRequired.adminRequired);
    console.log(adminRequired.adminRequired === auth.isAdmin);
    if (adminRequired.adminRequired === true) {
        if (adminRequired.adminRequired === auth.isAdmin) {
            console.log("yay");
            return <Outlet/>;
        }
        else {
            console.log("uhoh");
            return <Unauthorized/>;
        }
    }
    return <Outlet/>;
};

export default PrivateRoute;