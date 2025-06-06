
export interface Order {
  id: string;
  type: 'buy' | 'sell';
  price: number;
  quantity: number;
  total: number;
  timestamp: Date;
  status: 'pending' | 'filled' | 'cancelled';
}

export interface Trade {
  id: string;
  type: 'buy' | 'sell';
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
