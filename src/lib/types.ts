
// Define types for our application
export interface Book {
  id: string;
  title: string;
  author: string;
  price: number;
  quantity: number;
}

export interface Customer {
  id: string;
  username: string;
  password: string;
  name: string;
  purchaseHistory: Purchase[];
}

export interface Purchase {
  id: string;
  bookId: string;
  bookTitle: string;
  quantity: number;
  totalPrice: number;
  date: string;
}

export interface User {
  username: string;
  password: string;
  role: "owner" | "customer";
}

export type AppState = {
  books: Book[];
  customers: Customer[];
  currentUser: User | null;
  currentScreen: 
    | "login" 
    | "owner-dashboard"
    | "owner-manage-books"
    | "owner-manage-customers"
    | "owner-view-stats"
    | "customer-dashboard"
    | "customer-browse-books"
    | "customer-purchase-history"
    | "customer-cost-screen";
};
