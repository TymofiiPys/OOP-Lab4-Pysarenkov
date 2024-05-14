import React, {useState} from 'react';
import './Modal.css';
import axios from "axios";
import getHeaderConfig from "../config/Config";

const UpdateMenu = ({closeModal}) => {
    const [menuItem, setMenuItem] = useState({id: '', name: '', mealOrDrink: false, cost: ''});
    const config = getHeaderConfig();
    const handleChange = (e) => {
        const {name, value, type, checked} = e.target;
        if (name === "id") {
            axios.get('/api/menu/' + value, {headers: config.headers})
                .then(response => {
                    setMenuItem(response.data);
                })
                .catch(error => {
                    console.error('Error fetching menu items:', error);
                });
        } else {
            setMenuItem({
                ...menuItem,
                [name]: type === 'checkbox' ? checked : value
            });
        }
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        axios.put('/api/menu', menuItem, {headers: config.headers})
            .then(response => {
                console.log('Menu updated successfully:', response.data);
            })
            .catch(error => {
                console.error('Error updating menu', error);
            });
        closeModal();
    };

    return (
        <div className="modal">
            <div className="modal-content">
                <h2>Update Menu Item</h2>
                <form onSubmit={handleSubmit}>
                    <label>
                        ID:
                        <input type="text" name="id" value={menuItem.id} onChange={handleChange} required/>
                    </label>
                    <label>
                        Food Name:
                        <input type="text" name="name" value={menuItem.name} onChange={handleChange} required/>
                    </label>
                    <label>
                        Meal or Drink:
                        <input type="checkbox" name="mealOrDrink" checked={menuItem.mealOrDrink}
                               onChange={handleChange}/>
                    </label>
                    <label>
                        Cost:
                        <input type="number" name="cost" value={menuItem.cost} onChange={handleChange} required/>
                    </label>
                    <button type="submit">Submit</button>
                    <button type="button" onClick={closeModal}>Close</button>
                </form>
            </div>
        </div>
    );
};

export default UpdateMenu;