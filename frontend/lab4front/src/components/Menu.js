import React, {useEffect, useState} from 'react';
import axios from "axios";
import config from "./config/Config";
import getHeaderConfig from "./config/Config";
import "./Menu.css"

function Menu() {

    const [order, setOrder] = useState({});
    const [menuItems, setMenuItems] = useState([]);
    const [unpaidOrders, setUnpaidOrders] = useState([]);
    const config = getHeaderConfig();
    useEffect(() => {
        // Fetch menu items from backend servlet API
        axios.get('/api/menu', {headers: config.headers})
            .then(response => {
                setMenuItems(response.data);
            })
            .catch(error => {
                console.error('Error fetching menu items:', error);
            });
        axios.get('/api/orders?which=ISSUED_FOR_PAYMENT', {headers: config.headers})
            .then(response => {
                setUnpaidOrders(response.data);
            })
            .catch(error => {
                console.error('Error fetching unpaid orders:', error);
            });
    }, [unpaidOrders]);

    const handleSubmitOrder = (orderData) => {
        axios.post('/api/orders', orderData, {headers: config.headers})
            .then(response => {
                console.log('Order placed successfully:', response.data);
            })
            .catch(error => {
                console.error('Error placing order:', error);
            });
    }
    const handleAmountChange = (itemId, amount) => {
        console.log(itemId, amount);
        console.log(JSON.stringify(order))
        setOrder(prevOrder => ({
            ...prevOrder,
            [itemId]: amount
        }))
    }

    // Handle form submission
    const handleSubmit = (e) => {
        e.preventDefault();
        handleSubmitOrder(order);
    }

    const handlePayment = (e) => {
        e.preventDefault();
        // Change order status to paid
        axios.put('/api/orders?status=PAID', unpaidOrders.map(order => order.id),
            {headers: config.headers})
            .then(response => {
                console.log('Order updated successfully:', response.data);
            })
            .catch(error => {
                console.error('Error placing order:', error);
            });
        // Create payment
        axios.post('/api/payment', {cost: totalUnpaidCost}, {headers: config.headers})
            .then(response => {
                console.log('Payment created successfully:', response.data);
            })
            .catch(error => {
                console.error('Error placing order:', error);
            });
        setUnpaidOrders([]);
    }

    const totalUnpaidCost = unpaidOrders?.reduce((total, order) => {
        return total + order.cost;
    }, 0);

    return (<div className="order-form-container">
            <h2>Order Form</h2>
            <form onSubmit={handleSubmit} className="order-form">
                <table className="order-table">
                    <thead>
                    <tr>
                        <th>Menu Item</th>
                        <th>Type</th>
                        <th>Cost</th>
                        <th>Amount</th>
                    </tr>
                    </thead>
                    <tbody>
                    {menuItems.map(item => (
                        <tr key={item.id}>
                            <td>{item.name}</td>
                            <td>{item.mealOrDrink ? 'Meal' : 'Drink'}</td>
                            <td>${item.cost.toFixed(2)}</td>
                            <td>
                                <input
                                    type="number"
                                    value={order[item.id] || 0}
                                    onChange={(e) => handleAmountChange(item.id, parseInt(e.target.value))}
                                    min={0}
                                    className="amount-input"
                                />
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
                <button type="submit" className="btn btn-primary">Place Order</button>
            </form>
            <form onSubmit={handlePayment} className="payment-form">
                <div>
                    <h2>Unpaid Orders</h2>
                    <table className="order-table">
                        <thead>
                        <tr>
                            <th>Menu Item</th>
                            <th>Amount</th>
                            <th>Cost Per One Item</th>
                        </tr>
                        </thead>
                        <tbody>
                        {unpaidOrders.map(order => (
                            <tr key={order.id}>
                                <td>{order.menuItemName}</td>
                                <td>{order.amount}</td>
                                <td>${order.cost.toFixed(2)}</td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                    <h1>Total: ${totalUnpaidCost.toFixed(2)}</h1>
                    <button type="submit" className="btn btn-success">Pay</button>
                </div>
            </form>
        </div>
    );
}

export default Menu;