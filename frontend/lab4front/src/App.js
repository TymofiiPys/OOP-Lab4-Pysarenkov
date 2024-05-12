import React from "react";
import './App.css';
import Menu from "./components/Menu";
import Orders from "./components/Orders";
import {BrowserRouter as Router, Routes, Route} from 'react-router-dom';
import LoginButton from "./components/auth/Login";
import AccessCheck from "./components/auth/AccessCheck";
import Unauthorized from "./components/auth/Unauthorized";
import Login from "./components/auth/Login";
import Home from "./components/Home";
import AuthProvider from "./components/auth/AuthProvider";

function App() {
    return (
        <Router basename={process.env.PUBLIC_URL}>
                <div>
                    <h1>Restaurant System</h1>
                </div>
                <Routes>
                    <Route path='/' element={<Login/>}/>
                    <Route element={<AccessCheck adminRequired={true}/>}>
                        <Route exact path='/admin' element={<Orders/>}/>
                    </Route>
                    <Route exact path="/user" element={<Menu/>}/>
                    <Route path='unauthorized' element={<Unauthorized/>}/>
                    <Route path='home' element={<Home/>}/>
                </Routes>
        </Router>
    );
}

export default App;
