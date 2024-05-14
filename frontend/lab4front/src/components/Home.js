import { useNavigate, Link } from "react-router-dom";
import { useContext } from "react";
import AuthContext from "./auth/AuthProvider";
import LogoutButton from "./auth/Logout";

function Home () {
    return (
        <section className="home-page">
            <h1>Home</h1>
            <p className="home-page__description">You are logged in!</p>
            <div className="home-page__links">
                <Link to="/user" className="home-page__link">Go to the Ordering page</Link>
                <Link to="/admin" className="home-page__link">Go to the Admin page</Link>
            </div>
            <div className="home-page__button">
                <LogoutButton/>
            </div>
        </section>
    )
}

export default Home