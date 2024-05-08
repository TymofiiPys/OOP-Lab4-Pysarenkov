import React from "react";
import './App.css';
import Menu from "./components/Menu";
import Orders from "./components/Orders";
import {BrowserRouter as Router, Routes, Route} from 'react-router-dom';
import LoginButton from "./components/Login";
import {useAuth0} from "@auth0/auth0-react";


function App() {
    const { isAuthenticated, loginWithRedirect } = useAuth0();

    if(!isAuthenticated) {
        loginWithRedirect();
    }
    return (
        <Router basename={process.env.PUBLIC_URL}>
            <div>
                <h1>Restaurant System</h1>
               <LoginButton/>
            </div>
            <Routes>
                <Route exact path="/" element={<Menu/>}/>
                <Route exact path='/admin' element={<Orders/>}/>
            </Routes>
            {/*<Menu/>*/}
        </Router>
    );
}

export default App;
