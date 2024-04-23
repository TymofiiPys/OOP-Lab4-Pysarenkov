import React, {useState} from 'react';

function Menu(props) {

    const [order, setOrder] = useState({"client" : 1});

    const handleAmountChange = (itemId, amount) => {
        setOrder(prevOrder => ({
            ...prevOrder,
            [itemId]: amount
        }));
    }

    // Handle form submission
    const handleSubmit = (e) => {
        e.preventDefault();
        props.onSubmit(order);
    }

    // return (<table>
    //     <thead>
    //     <tr>
    //         <th>ID</th>
    //         <th>Name</th>
    //         <th>Type</th>
    //         <th>Cost</th>
    //     </tr>
    //     </thead>
    //     <tbody>
    //     {this.props.passedMenuItems.map(item => (
    //         <tr key={item.id}>
    //             <td>{item.id}</td>
    //             <td>{item.name}</td>
    //             <td>{item.meal ? 'Meal' : 'Drink'}</td>
    //             <td>{item.cost}</td>
    //         </tr>
    //     ))}
    //     </tbody>
    // </table>);
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
                {props.passedMenuItems.map(item => (
                    <tr key={item.id}>
                        <td>{item.name}</td>
                        <td>{item.meal ? 'Meal' : 'Drink'}</td>
                        <td>${item.cost.toFixed(2)}</td>
                        <td>
                            <input
                                type="number"
                                value={order[item.id] || 0}
                                onChange={(e) => handleAmountChange(item.id, parseInt(e.target.value))}
                                min={0}
                            />
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
            <button type="submit">Place Order</button>
        </form>
    </div>);
}

export default Menu;