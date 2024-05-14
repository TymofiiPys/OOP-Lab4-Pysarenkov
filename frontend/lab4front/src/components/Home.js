import "./Home.css"
import {Link } from "react-router-dom";
import LogoutButton from "./auth/Logout";

function Home () {
    return (
        <section className="home-page">
            <h1 className="home-page__title">Home</h1>
            <p className="home-page__description">You are logged in!</p>
            <div className="home-page__links">
                <Link to="/user" className="home-page__link btn btn-primary">Go to the Ordering page</Link>
                <Link to="/admin" className="home-page__link btn btn-secondary">Go to the Admin page</Link>
            </div>
            <div className="home-page__button">
                <LogoutButton/>
            </div>
        </section>
    )
}

export default Home