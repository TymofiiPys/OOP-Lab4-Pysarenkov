import React, {useEffect, useState} from 'react';
import axios from "axios";
import getHeaderConfig from "./config/Config";
import "./Orders.css"
import AddMenu from "./menuedit/AddMenu";
import DeleteMenu from "./menuedit/DeleteMenu";
import UpdateMenu from "./menuedit/UpdateMenu";

function Orders() {
    const [orders, setOrders] = useState([]);
    const [payments, setPayments] = useState([]);
    const [unpaidOrders, setUnpaidOrders] = useState([]);
    const [activeModal, setActiveModal] = useState(null);

    const config = getHeaderConfig();

    useEffect(() => {
        // Fetch menu items from backend servlet API
        axios.get('/api/orders', {headers: config.headers})
            .then(response => {
                console.log("Parsed orders");
                setOrders(response.data);
            })
            .catch(error => {
                console.error('Error fetching orders:', error);
            });
        axios.get('/api/orders?which=ORDERED&admin', {headers: config.headers})
            .then(response => {
                console.log("Parsed UNPAID orders");
                setUnpaidOrders(response.data);
            })
            .catch(error => {
                console.error('Error fetching UNPAID orders:', error);
            });
        axios.get('/api/payment', {headers: config.headers})
            .then(
                response => {
                    console.log("Parsed payments");
                    setPayments(response.data);
                }
            )
            .catch(error => {
                console.error('Error fetching payments:', error);
            });
    }, []);

    const handleBillIssue = () => {
        axios.put('/api/orders?status=ISSUED_FOR_PAYMENT', unpaidOrders.map(order => order.id), {headers: config.headers})
            .then(response => {
                console.log('Orders updated successfully:', response.data);
            })
            .catch(error => {
                console.error('Error placing order:', error);
            });
    }

    const closeModal = () => {
        setActiveModal(null);
    };


    return (
        <div className="admin-page">
            {activeModal === 'add' && <AddMenu closeModal={closeModal}/>}
            {activeModal === 'delete' && <DeleteMenu closeModal={closeModal}/>}
            {activeModal === 'update' && <UpdateMenu closeModal={closeModal}/>}
            <h2>Menu settings</h2>
            <div className="menu-settings">
                <button onClick={() => setActiveModal('add')} className="btn btn-primary">Add Menu</button>
                <button onClick={() => setActiveModal('delete')} className="btn btn-secondary">Delete Menu</button>
                <button onClick={() => setActiveModal('update')} className="btn btn-success">Update Menu</button>
            </div>
            <div>
                <h2>Unpaid Orders</h2>
                <table className="order-table">
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
                            <td>{order.clientName}</td>
                            <td>{order.menuItemName}</td>
                            <td>{order.amount}</td>
                            <td>${order.cost.toFixed(2)}</td>
                        </tr>
                    ))}
                    </tbody>
                </table>
                <button onClick={handleBillIssue} className="btn btn-warning">Send bills</button>
            </div>
            <h2>Orders List</h2>
            <table className="order-table">
                <thead>
                <tr>
                    <th>Order ID</th>
                    <th>Client Name</th>
                    <th>Menu Item</th>
                    <th>Amount</th>
                    <th>Status</th>
                    <th>Cost</th>
                </tr>
                </thead>
                <tbody>
                {orders.map(order => (
                    <tr key={order.id}>
                        <td>{order.id}</td>
                        <td>{order.clientName}</td>
                        <td>{order.menuItemName}</td>
                        <td>{order.amount}</td>
                        <td>{order.status}</td>
                        <td>${order.cost.toFixed(2)}</td>
                    </tr>
                ))}
                </tbody>
            </table>
            <h2>Payments List</h2>
            <table className="order-table">
                <thead>
                <tr>
                    <th>Client ID</th>
                    <th>Client Name</th>
                    <th>Time of transaction</th>
                    <th>Cost</th>
                </tr>
                </thead>
                <tbody>
                {payments.map(payment => (
                    <tr>
                        <td>{payment.clientId}</td>
                        <td>{payment.clientName}</td>
                        <td>{payment.timeStr}</td>
                        <td>${payment.cost.toFixed(2)}</td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>);
}

export default Orders;