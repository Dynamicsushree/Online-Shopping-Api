import React, { useState } from 'react';

function ProductList({ products, token, onCartUpdated }) {
  const [quantities, setQuantities] = useState({});

  const handleAdd = async (id) => {
    const response = await fetch('http://localhost:8080/api/cart', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify({ productId: id, quantity: quantities[id] || 1 }),
    });

    if (response.ok) {
      onCartUpdated();
    }
  };

  return (
    <section className="section">
      <h2>Products</h2>
      {products.map((product) => (
        <div key={product.id} className="product-card">
          <h3>{product.name}</h3>
          <p>{product.description}</p>
          <p>${product.price.toFixed(2)}</p>
          <label>
            Quantity
            <input
              type="number"
              min="1"
              max={product.stock}
              value={quantities[product.id] || 1}
              onChange={(e) => setQuantities({ ...quantities, [product.id]: parseInt(e.target.value, 10) })}
            />
          </label>
          <button onClick={() => handleAdd(product.id)}>Add to Cart</button>
        </div>
      ))}
    </section>
  );
}

export default ProductList;
