import React from "react";
import './App.css';
import Menu from "./components/Menu";
import Orders from "./components/Orders";
import {BrowserRouter as Router, Routes, Route, Red, Navigate} from 'react-router-dom';
import LoginButton from "./components/auth/Login";
import AccessCheck from "./components/auth/AccessCheck";
import Unauthorized from "./components/auth/Unauthorized";
import Login from "./components/auth/Login";
import Home from "./components/Home";
import AuthProvider from "./components/auth/AuthProvider";
import PrivateRoute from "./components/auth/PrivateRoute";

function App() {
    return (
        <Router>
            <AuthProvider>
            <div>
                    <h1>Restaurant System</h1>
                </div>
                <Routes>
                    <Route exact path='/' element={<PrivateRoute adminRequired={false}/>}> //doesn't do anything but redirect if not logged in
                        <Route exact path='/' element={<Navigate to={"/login"} replace/>}/>
                    </Route>
                    <Route path='/login' element={<Login/>}/>
                    <Route element={<PrivateRoute adminRequired={true}/>}>
                        <Route exact path='/admin' element={<Orders/>}/>
                    </Route>
                    <Route element={<PrivateRoute adminRequired={false}/>}>
                        <Route exact path="/user" element={<Menu/>}/>
                    </Route>
                    <Route path='unauthorized' element={<Unauthorized/>}/>
                    <Route path='home' element={<Home/>}/>
                </Routes>
            </AuthProvider>
        </Router>
    );
}

export default App;
