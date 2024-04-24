import React, {useEffect, useState} from 'react';
import axios from "axios";

function Orders() {
    const [orders, setOrders] = useState([]);


    useEffect(() => {
        // Fetch menu items from backend servlet API
        axios.get('/api/orders')
            .then(response => {
                setOrders(response.data);
            })
            .catch(error => {
                console.error('Error fetching menu items:', error);
            });
    }, []);

    return (<div>
        <h2>Orders List</h2>
        <table>
            <thead>
            <tr>
                <th>Order ID</th>
                <th>Client Name</th>
                <th>Menu Item</th>
                <th>Amount</th>
                <th>Status</th>
            </tr>
            </thead>
            <tbody>
            {orders.map(order => (
                <tr key={order.id}>
                    <td>{order.id}</td>
                    <td>{order.client_name}</td>
                    <td>{order.menu_item}</td>
                    <td>{order.amount}</td>
                    <td>{order.status}</td>
                </tr>
            ))}
            </tbody>
        </table>
    </div>);
}

export default Orders;