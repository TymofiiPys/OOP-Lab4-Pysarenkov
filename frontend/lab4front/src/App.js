import './App.css';
import Menu from "./Menu";
import axios from 'axios';
import React, {useEffect, useState} from "react";


function App() {
    const [menuItems, setMenuItems] = useState([]);


    useEffect(() => {
        // Fetch menu items from backend servlet API
        axios.get('/api/menu')
            .then(response => {
                setMenuItems(response.data);
            })
            .catch(error => {
                console.error('Error fetching menu items:', error);
            });
    }, []);

    return (
    <div>
        <h1>Restaurant System</h1>
        <Menu passedMenuItems={menuItems}/>
    </div>
    );
}

export default App;
