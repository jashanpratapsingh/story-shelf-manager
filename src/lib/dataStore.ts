
import { AppState, Book, Customer, User } from "./types";

// Initial state with hardcoded admin user
export const initialState: AppState = {
  books: [],
  customers: [],
  currentUser: null,
  currentScreen: "login"
};

// Function to load data from localStorage (simulating file load)
export const loadData = (): AppState => {
  try {
    const savedBooks = localStorage.getItem('books');
    const savedCustomers = localStorage.getItem('customers');
    
    return {
      books: savedBooks ? JSON.parse(savedBooks) : [],
      customers: savedCustomers ? JSON.parse(savedCustomers) : [],
      currentUser: null,
      currentScreen: "login"
    };
  } catch (error) {
    console.error("Error loading data:", error);
    return initialState;
  }
};

// Function to save data to localStorage (simulating file save)
export const saveData = (books: Book[], customers: Customer[]): void => {
  try {
    localStorage.setItem('books', JSON.stringify(books));
    localStorage.setItem('customers', JSON.stringify(customers));
    console.log("Data saved successfully");
  } catch (error) {
    console.error("Error saving data:", error);
  }
};

// Function to authenticate a user
export const authenticateUser = (username: string, password: string, state: AppState): User | null => {
  // Check for admin credentials
  if (username === "admin" && password === "admin") {
    return { username, password, role: "owner" };
  }
  
  // Check for customer credentials
  const customer = state.customers.find(
    (c) => c.username === username && c.password === password
  );
  
  if (customer) {
    return { username, password, role: "customer" };
  }
  
  return null;
};
