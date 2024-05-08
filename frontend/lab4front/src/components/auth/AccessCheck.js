import {useLocation, Navigate, Outlet} from "react-router-dom";
import {jwtDecode} from "jwt-decode";

function AccessCheck({adminRequired}) {
    const auth = jwtDecode(window.localStorage.getItem("Token"));
    const location = useLocation();
    return (
        adminRequired === true
            ? adminRequired === auth.isAdmin ? <Outlet/>
                : <Navigate to="unauthorized" state={{from: location}} replace/>
            : <Outlet/>
    );
}

export default AccessCheck;