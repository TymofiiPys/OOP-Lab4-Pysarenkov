import React from "react";
import './App.css';
import Menu from "./Menu";
import Orders from "./Orders";
import {BrowserRouter as Router, Routes, Route} from 'react-router-dom';


function App() {
    return (
        <Router basename={process.env.PUBLIC_URL}>
            <div>
                <h1>Restaurant System</h1>
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
