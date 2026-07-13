import React, { useState } from 'react';

function Checkout({ cartItems, token, onOrderCreated }) {
  const [paymentMethod, setPaymentMethod] = useState('CREDIT_CARD');
  const [message, setMessage] = useState(null);

  const handleCheckout = async () => {
    const response = await fetch('http://localhost:8080/api/orders', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify({ paymentMethod }),
    });

    if (response.ok) {
      setMessage('Order created successfully');
      onOrderCreated();
    } else {
      setMessage('Failed to create order');
    }
  };

  if (cartItems.length === 0) {
    return null;
  }

  return (
    <section className="section">
      <h2>Checkout</h2>
      <label>
        Payment Method
        <select value={paymentMethod} onChange={(e) => setPaymentMethod(e.target.value)}>
          <option value="CREDIT_CARD">Credit Card</option>
          <option value="PAYPAL">PayPal</option>
        </select>
      </label>
      <button onClick={handleCheckout}>Place Order</button>
      {message && <p>{message}</p>}
    </section>
  );
}

export default Checkout;
