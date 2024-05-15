import React, { useState } from 'react';
import './Modal.css';
import axios from "axios";
import getHeaderConfig from "../config/Config";

const DeleteMenu = ({ closeModal }) => {
    const [menuItem, setMenuItem] = useState({ id: '', name: '', mealOrDrink: false, cost: '' });
    const config = getHeaderConfig();

    const handleChange = (e) => {
        const { name, value } = e.target;
        axios.get('/api/menu/' + value, {headers: config.headers})
            .then(response => {
                setMenuItem(response.data);
            })
            .catch(error => {
                console.error('Error fetching menu item:', error);
            });
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        // const { name, value } = e.target;
        axios.delete('/api/menu/' + menuItem.id, {headers: config.headers})
            .then(() => {
                console.log('Menu deleted successfully');
            })
            .catch(error => {
                console.error('Error deleting menu item:', error);
            });
        closeModal();
    };

    return (
        <div className="modal">
            <div className="modal-content">
                <h2>Delete Menu Item</h2>
                <form onSubmit={handleSubmit}>
                    <label>
                        ID:
                        <input type="text" name="id" value={menuItem.id} onChange={handleChange} required />
                    </label>
                    <label>
                        Food Name:
                        <input type="text" name="name" value={menuItem.name} readOnly />
                    </label>
                    <label>
                        Meal or Drink:
                        <input type="checkbox" name="mealOrDrink" checked={menuItem.mealOrDrink} readOnly />
                    </label>
                    <label>
                        Cost:
                        <input type="number" name="cost" value={menuItem.cost} readOnly />
                    </label>
                    <button type="submit">Submit</button>
                    <button type="button" onClick={closeModal}>Close</button>
                </form>
            </div>
        </div>
    );
};

export default DeleteMenu;