import React, { useState } from "react";

const dummyOrders = [
  { id: 1, type: "BUY", quantity: 10, price: 5900, orderType: "Limit" },
  { id: 2, type: "SELL", quantity: 5, price: 5920, orderType: "Limit" },
  { id: 3, type: "BUY", quantity: 20, price: 5890, orderType: "Limit" },
  { id: 4, type: "SELL", quantity: 15, price: 5930, orderType: "Limit" },
];

const TradingPage = () => {
  const [orders, setOrders] = useState(dummyOrders);
  const [form, setForm] = useState({
    type: "BUY",
    quantity: "",
    price: "",
    orderType: "Limit",
  });

  const handleInputChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    let price = parseFloat(form.price);
    if (form.orderType === "Market") {
      const relevantOrders = orders.filter((o) => o.type !== form.type);
      const sorted = relevantOrders.sort((a, b) =>
        form.type === "BUY" ? a.price - b.price : b.price - a.price
      );
      price = sorted.length ? sorted[0].price : 5900; // Default price if no matching order found
    }

    const newOrder = {
      id: orders.length + 1,
      type: form.type,
      quantity: parseInt(form.quantity),
      price,
      orderType: form.orderType,
    };
    setOrders([newOrder, ...orders]);
    setForm({ type: "BUY", quantity: "", price: "", orderType: "Limit" });
  };

  return (
    <div className="min-h-screen bg-gray-100 p-8">
      <h1 className="text-2xl font-bold mb-8 m-auto">Gold Trading Page</h1>

      <form
        onSubmit={handleSubmit}
        className="bg-white p-4 rounded shadow mb-6"
      >
        <div className="flex gap-4 flex-wrap items-end">
          <div>
            <label className="block text-sm font-medium">Order Type</label>
            <select
              name="type"
              value={form.type}
              onChange={handleInputChange}
              className="border rounded p-2"
            >
              <option value="BUY">Buy</option>
              <option value="SELL">Sell</option>
            </select>
          </div>

          <div>
            <label className="block text-sm font-medium">Order Mode</label>
            <select
              name="orderType"
              value={form.orderType}
              onChange={handleInputChange}
              className="border rounded p-2"
            >
              <option value="Limit">Limit</option>
              <option value="Market">Market</option>
            </select>
          </div>

          <div>
            <label className="block text-sm font-medium">Quantity (gms)</label>
            <input
              type="number"
              name="quantity"
              value={form.quantity}
              onChange={handleInputChange}
              className="border rounded p-2"
              required
            />
          </div>

          {form.orderType === "Limit" && (
            <div>
              <label className="block text-sm font-medium">
                Price (INR/gm)
              </label>
              <input
                type="number"
                name="price"
                value={form.price}
                onChange={handleInputChange}
                className="border rounded p-2"
                required
              />
            </div>
          )}

          <button
            type="submit"
            className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700"
          >
            Place Order
          </button>
        </div>
      </form>

      <div className="bg-white rounded shadow p-4">
        <h2 className="text-xl font-semibold mb-4">Order Book</h2>
        <table className="w-full text-left border-t">
          <thead>
            <tr>
              <th className="py-2 border-b">Type</th>
              <th className="py-2 border-b">Order Mode</th>
              <th className="py-2 border-b">Quantity</th>
              <th className="py-2 border-b">Price</th>
            </tr>
          </thead>
          <tbody>
            {orders.map((order) => (
              <tr
                key={order.id}
                className={
                  order.type === "BUY" ? "text-green-600" : "text-red-600"
                }
              >
                <td className="py-1 border-b">{order.type}</td>
                <td className="py-1 border-b">{order.orderType}</td>
                <td className="py-1 border-b">{order.quantity}</td>
                <td className="py-1 border-b">₹{order.price.toFixed(2)}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default TradingPage;
