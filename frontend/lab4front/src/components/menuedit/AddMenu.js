import React, { useState } from 'react';
import './Modal.css';
import axios from "axios";
import getHeaderConfig from "../config/Config";

const AddMenu = ({ closeModal }) => {
    const [menuItem, setMenuItem] = useState({ name: '', mealOrDrink: false, cost: '' });
    const config = getHeaderConfig();

    const handleChange = (e) => {
        const { name, value, type, checked } = e.target;
        setMenuItem({
            ...menuItem,
            [name]: type === 'checkbox' ? checked : value
        });
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        axios.post('/api/menu', menuItem, {headers: config.headers})
            .then(response => {
                console.log('Menu added successfully:', response.data);
            })
            .catch(error => {
                console.error('Error adding menu:', error);
            });
        closeModal();
    };

    return (
        <div className="modal">
            <div className="modal-content">
                <h2>Add Menu Item</h2>
                <form onSubmit={handleSubmit}>
                    <label>
                        Food Name:
                        <input type="text" name="name" value={menuItem.name} onChange={handleChange} required />
                    </label>
                    <label>
                        Meal or Drink:
                        <input type="checkbox" name="mealOrDrink" checked={menuItem.mealOrDrink} onChange={handleChange} />
                    </label>
                    <label>
                        Cost:
                        <input type="number" name="cost" value={menuItem.cost} onChange={handleChange} required />
                    </label>
                    <button type="submit">Submit</button>
                    <button type="button" onClick={closeModal}>Close</button>
                </form>
            </div>
        </div>
    );
};

export default AddMenu;
