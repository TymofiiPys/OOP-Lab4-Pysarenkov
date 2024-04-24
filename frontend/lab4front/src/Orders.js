import React, {useEffect, useState} from 'react';
import axios from "axios";

function Orders() {
    const [orders, setOrders] = useState([]);
    const [unpaidOrders, setUnpaidOrders] = useState([]);

    useEffect(() => {
        // Fetch menu items from backend servlet API
        axios.get('/api/orders')
            .then(response => {
                setOrders(response.data);
                setUnpaidOrders(response.data.filter(order => order.status === 'ORDERED'));
            })
            .catch(error => {
                console.error('Error fetching menu items:', error);
            });
    }, []);

    const handleBillIssue = () => {
        axios.put('/api/orders', unpaidOrders)
            .then(response => {
                console.log('Order updated successfully:', response.data);
            })
            .catch(error => {
                console.error('Error placing order:', error);
            });
    }

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
        <div>
            <h2>Unpaid Orders</h2>
            <table>
                <thead>
                <tr>
                    <th>Client</th>
                    <th>Menu Item</th>
                    <th>Amount</th>
                    <th>Cost</th>
                </tr>
                </thead>
                <tbody>
                {unpaidOrders.map(order => (
                    <tr key={order.id}>
                        <td>{order.client_name}</td>
                        <td>{order.menu_item}</td>
                        <td>{order.amount}</td>
                        <td>${order.cost.toFixed(2)}</td>
                    </tr>
                ))}
                </tbody>
            </table>
            <button onClick={handleBillIssue}>Send bills</button>
        </div>
    </div>);
}

export default Orders;