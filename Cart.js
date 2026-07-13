import React from 'react';

function Cart({ cartItems, token, onCartUpdated }) {
  const handleRemove = async (itemId) => {
    const response = await fetch(`http://localhost:8080/api/cart/${itemId}`, {
      method: 'DELETE',
      headers: { Authorization: `Bearer ${token}` },
    });

    if (response.ok) {
      onCartUpdated();
    }
  };

  const total = cartItems.reduce(
    (sum, item) => sum + item.product.price * item.quantity,
    0
  );

  return (
    <section className="section">
      <h2>Cart</h2>
      {cartItems.length === 0 && <p>Your cart is empty.</p>}
      {cartItems.map((item) => (
        <div key={item.id} className="cart-item">
          <h3>{item.product.name}</h3>
          <p>${item.product.price.toFixed(2)} x {item.quantity}</p>
          <button onClick={() => handleRemove(item.id)}>Remove</button>
        </div>
      ))}
      {cartItems.length > 0 && <p>Total: ${total.toFixed(2)}</p>}
    </section>
  );
}

export default Cart;
