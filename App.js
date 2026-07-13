import React, { useState, useEffect } from 'react';
import Login from './components/Login';
import ProductList from './components/ProductList';
import Cart from './components/Cart';
import Checkout from './components/Checkout';
import './App.css';

function App() {
  const [token, setToken] = useState(localStorage.getItem('token'));
  const [products, setProducts] = useState([]);
  const [cartItems, setCartItems] = useState([]);

  useEffect(() => {
    if (token) {
      fetchProducts();
      fetchCart();
    }
  }, [token]);

  const fetchProducts = async () => {
    const res = await fetch('http://localhost:8080/api/products');
    const data = await res.json();
    setProducts(data);
  };

  const fetchCart = async () => {
    const res = await fetch('http://localhost:8080/api/cart', {
      headers: { Authorization: `Bearer ${token}` },
    });
    const data = await res.json();
    setCartItems(data);
  };

  const handleLogin = (newToken) => {
    localStorage.setItem('token', newToken);
    setToken(newToken);
  };

  const handleLogout = () => {
    localStorage.removeItem('token');
    setToken(null);
    setCartItems([]);
  };

  if (!token) {
    return <Login onLogin={handleLogin} />;
  }

  return (
    <div className="app-container">
      <header>
        <h1>Online Shopping</h1>
        <button onClick={handleLogout}>Logout</button>
      </header>
      <main>
        <ProductList products={products} token={token} onCartUpdated={fetchCart} />
        <Cart cartItems={cartItems} token={token} onCartUpdated={fetchCart} />
        <Checkout cartItems={cartItems} token={token} onOrderCreated={() => fetchCart()} />
      </main>
    </div>
  );
}

export default App;
