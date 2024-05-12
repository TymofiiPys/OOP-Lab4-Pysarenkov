import React, {useEffect, useState} from 'react';
import axios from "axios";
import config from "./config/Config";
import getHeaderConfig from "./config/Config";

function Menu() {

    const [order, setOrder] = useState([]);
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
        axios.get('/api/orders?which=ISSUED_FOR_PAYMENT&clid=' + 1, {headers: config.headers})
            .then(response => {
                setUnpaidOrders(response.data);
            })
            .catch(error => {
                console.error('Error fetching unpaid orders:', error);
            });
    }, []);

    const handleSubmitOrder = (orderData) => {
        axios.post('/api/orders', {
            data: orderData,
            headers: config.headers
        })
            .then(response => {
                console.log('Order placed successfully:', response.data);
            })
            .catch(error => {
                console.error('Error placing order:', error);
            });
    }
    /* TODO: form json as an array of OrderReceiveDTOs */
    /* TODO: ignore clientId and return to previous stuff due to clientId being sent as JWT */
    const handleAmountChange = (itemId, amount) => {
        setOrder(prevOrder => ([
            ...prevOrder,
            {
                clientId: 1,
                menuId: itemId,
                amount: amount
            }
        ]));
    }

    // Handle form submission
    const handleSubmit = (e) => {
        e.preventDefault();
        handleSubmitOrder(order);
    }

    {/* Called when client pays for orders */
    }
    const handlePayment = () => {
        // Change order status to paid
        axios.put('/api/orders?status=PAID', {
            data: unpaidOrders.map(order => order.id),
            headers: config.headers
        })
            .then(response => {
                console.log('Order updated successfully:', response.data);
            })
            .catch(error => {
                console.error('Error placing order:', error);
            });
        // Create payment
        axios.post('/api/payment', {clientId: 1, cost: totalUnpaidCost})
            .then(response => {
                console.log('Payment created successfully:', response.data);
            })
            .catch(error => {
                console.error('Error placing order:', error);
            });
    }

    const totalUnpaidCost = unpaidOrders.reduce((total, order) => {
        return total + order.cost;
    }, 0);

    return (<div>
        <h2>Order Form</h2>
        <form onSubmit={handleSubmit}>
            <table>
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
                                value={order.find(order => order.menuId === item.id)?.amount || 0}
                                onChange={(e) => handleAmountChange(item.id, parseInt(e.target.value))}
                                min={0}
                            />
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
            <button type="submit">Place Order</button>
            <div>
                <h2>Unpaid Orders</h2>
                <table>
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
                <button onClick={handlePayment}>Pay</button>
            </div>
        </form>
    </div>);
}

export default Menu;