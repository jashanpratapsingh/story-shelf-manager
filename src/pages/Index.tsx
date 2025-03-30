
import React, { useState, useEffect } from 'react';
import { v4 as uuidv4 } from 'uuid';
import LoginScreen from '@/components/LoginScreen';
import OwnerDashboard from '@/components/OwnerDashboard';
import CustomerDashboard from '@/components/CustomerDashboard';
import ManageBooks from '@/components/ManageBooks';
import ManageCustomers from '@/components/ManageCustomers';
import ViewStats from '@/components/ViewStats';
import BrowseBooks from '@/components/BrowseBooks';
import PurchaseHistory from '@/components/PurchaseHistory';
import { AppState, Book, Customer, Purchase, User } from '@/lib/types';
import { loadData, saveData, authenticateUser, initialState } from '@/lib/dataStore';
import { useToast } from '@/components/ui/use-toast';

const Index = () => {
  const [state, setState] = useState<AppState>(initialState);
  const { toast } = useToast();

  // Load data on component mount
  useEffect(() => {
    const loadedState = loadData();
    setState((prevState) => ({
      ...prevState,
      books: loadedState.books,
      customers: loadedState.customers
    }));
  }, []);

  // Save data when books or customers change
  useEffect(() => {
    // Only save when the app is already initialized (not on first render)
    if (state.books.length > 0 || state.customers.length > 0) {
      saveData(state.books, state.customers);
    }
  }, [state.books, state.customers]);

  // Save data on window close/refresh
  useEffect(() => {
    const handleBeforeUnload = () => {
      saveData(state.books, state.customers);
    };

    window.addEventListener('beforeunload', handleBeforeUnload);
    
    return () => {
      window.removeEventListener('beforeunload', handleBeforeUnload);
    };
  }, [state.books, state.customers]);

  // Login handler
  const handleLogin = (username: string, password: string) => {
    const user = authenticateUser(username, password, state);
    
    if (user) {
      setState(prev => ({
        ...prev,
        currentUser: user,
        currentScreen: user.role === 'owner' ? 'owner-dashboard' : 'customer-dashboard'
      }));
    } else {
      toast({
        title: "Login Failed",
        description: "Invalid username or password",
        variant: "destructive"
      });
    }
  };

  // Logout handler
  const handleLogout = () => {
    setState(prev => ({
      ...prev,
      currentUser: null,
      currentScreen: 'login'
    }));
  };

  // Navigation handler
  const handleNavigate = (screen: string) => {
    setState(prev => ({
      ...prev,
      currentScreen: screen as AppState['currentScreen']
    }));
  };

  // Book management handlers
  const handleAddBook = (bookData: Omit<Book, "id">) => {
    const newBook: Book = {
      id: uuidv4(),
      ...bookData
    };
    
    setState(prev => ({
      ...prev,
      books: [...prev.books, newBook]
    }));
  };

  const handleUpdateBook = (updatedBook: Book) => {
    setState(prev => ({
      ...prev,
      books: prev.books.map(book => 
        book.id === updatedBook.id ? updatedBook : book
      )
    }));
  };

  const handleDeleteBook = (bookId: string) => {
    setState(prev => ({
      ...prev,
      books: prev.books.filter(book => book.id !== bookId)
    }));
  };

  // Customer management handlers
  const handleAddCustomer = (customerData: Omit<Customer, "id" | "purchaseHistory">) => {
    const newCustomer: Customer = {
      id: uuidv4(),
      ...customerData,
      purchaseHistory: []
    };
    
    setState(prev => ({
      ...prev,
      customers: [...prev.customers, newCustomer]
    }));
  };

  const handleUpdateCustomer = (updatedCustomer: Customer) => {
    setState(prev => ({
      ...prev,
      customers: prev.customers.map(customer => 
        customer.id === updatedCustomer.id ? updatedCustomer : customer
      )
    }));
  };

  const handleDeleteCustomer = (customerId: string) => {
    setState(prev => ({
      ...prev,
      customers: prev.customers.filter(customer => customer.id !== customerId)
    }));
  };

  // Purchase handler
  const handlePurchase = (bookId: string, quantity: number) => {
    if (!state.currentUser) return;
    
    const book = state.books.find(b => b.id === bookId);
    if (!book || book.quantity < quantity) return;
    
    // Update book quantity
    const updatedBooks = state.books.map(b => 
      b.id === bookId ? { ...b, quantity: b.quantity - quantity } : b
    );
    
    // Create purchase record
    const purchase: Purchase = {
      id: uuidv4(),
      bookId,
      bookTitle: book.title,
      quantity,
      totalPrice: book.price * quantity,
      date: new Date().toISOString()
    };
    
    // Update customer purchase history
    const updatedCustomers = state.customers.map(customer => 
      customer.username === state.currentUser?.username
        ? { ...customer, purchaseHistory: [...customer.purchaseHistory, purchase] }
        : customer
    );
    
    setState(prev => ({
      ...prev,
      books: updatedBooks,
      customers: updatedCustomers
    }));
  };

  // Get the current customer object for customer screens
  const getCurrentCustomer = (): Customer | null => {
    if (!state.currentUser || state.currentUser.role !== 'customer') return null;
    return state.customers.find(c => c.username === state.currentUser?.username) || null;
  };

  // Render the appropriate screen based on currentScreen state
  const renderScreen = () => {
    const currentCustomer = getCurrentCustomer();
    
    switch (state.currentScreen) {
      case 'login':
        return <LoginScreen onLogin={handleLogin} />;
        
      case 'owner-dashboard':
        return (
          <OwnerDashboard 
            onNavigate={handleNavigate} 
            onLogout={handleLogout} 
          />
        );
        
      case 'owner-manage-books':
        return (
          <ManageBooks 
            books={state.books}
            onAddBook={handleAddBook}
            onUpdateBook={handleUpdateBook}
            onDeleteBook={handleDeleteBook}
            onBack={() => handleNavigate('owner-dashboard')}
          />
        );
        
      case 'owner-manage-customers':
        return (
          <ManageCustomers 
            customers={state.customers}
            onAddCustomer={handleAddCustomer}
            onUpdateCustomer={handleUpdateCustomer}
            onDeleteCustomer={handleDeleteCustomer}
            onBack={() => handleNavigate('owner-dashboard')}
          />
        );
        
      case 'owner-view-stats':
        return (
          <ViewStats 
            books={state.books}
            customers={state.customers}
            onBack={() => handleNavigate('owner-dashboard')}
          />
        );
        
      case 'customer-dashboard':
        return currentCustomer ? (
          <CustomerDashboard 
            customer={currentCustomer}
            onNavigate={handleNavigate}
            onLogout={handleLogout}
          />
        ) : (
          <div>Error: Customer not found</div>
        );
        
      case 'customer-browse-books':
        return currentCustomer ? (
          <BrowseBooks 
            books={state.books}
            customer={currentCustomer}
            onPurchase={handlePurchase}
            onBack={() => handleNavigate('customer-dashboard')}
          />
        ) : (
          <div>Error: Customer not found</div>
        );
        
      case 'customer-purchase-history':
        return currentCustomer ? (
          <PurchaseHistory 
            customer={currentCustomer}
            onBack={() => handleNavigate('customer-dashboard')}
          />
        ) : (
          <div>Error: Customer not found</div>
        );
        
      default:
        return <div>Screen not found</div>;
    }
  };

  return (
    <div className="min-h-screen bg-gray-50">
      {renderScreen()}
    </div>
  );
};

export default Index;
