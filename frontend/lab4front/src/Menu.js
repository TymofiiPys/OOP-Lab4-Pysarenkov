import React, {Component} from 'react';

export default class Menu extends Component {
    render() {
        return (<table>
            <thead>
            <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Type</th>
                <th>Cost</th>
            </tr>
            </thead>
            <tbody>
            {this.props.passedMenuItems.map(item => (
                <tr key={item.id}>
                    <td>{item.id}</td>
                    <td>{item.name}</td>
                    <td>{item.meal ? 'Meal' : 'Drink'}</td>
                    <td>{item.cost}</td>
                </tr>
            ))}
            </tbody>
        </table>);
    }
}