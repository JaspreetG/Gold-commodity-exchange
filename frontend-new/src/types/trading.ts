export interface Order {
  id: string;
  type: "BUY" | "SELL";
  price: number;
  quantity: number;
  total: number;
  timestamp: Date;
  status: "PENDING" | "FILLED" | "CANCELLED";
}

export interface Trade {
  id: string;
  type: "BUY" | "SELL";
  price: number;
  quantity: number;
  total: number;
  timestamp: Date;
  fee: number;
}

export interface OrderBookEntry {
  price: number;
  quantity: number;
  total: number;
}

export interface OrderBook {
  bids: OrderBookEntry[];
  asks: OrderBookEntry[];
}
