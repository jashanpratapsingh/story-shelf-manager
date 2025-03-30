
import React from 'react';
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Customer } from "@/lib/types";
import { BookOpen, History, LogOut } from "lucide-react";

interface CustomerDashboardProps {
  customer: Customer;
  onNavigate: (screen: string) => void;
  onLogout: () => void;
}

const CustomerDashboard: React.FC<CustomerDashboardProps> = ({
  customer,
  onNavigate,
  onLogout
}) => {
  const calculateTotalPoints = (): number => {
    if (!customer.purchaseHistory.length) return 0;
    
    return customer.purchaseHistory.reduce((total, purchase) => {
      return total + purchase.totalPrice * 10; // 10 points per CAD
    }, 0);
  };
  
  const customerPoints = calculateTotalPoints();
  const customerStatus = customerPoints >= 1000 ? "Gold" : "Silver";
  
  return (
    <div className="container mx-auto p-4">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-2xl font-bold">
          Welcome, {customer.name}. You have {customerPoints} points. Your status is {customerStatus}
        </h1>
        <Button variant="outline" onClick={onLogout}>
          <LogOut className="h-4 w-4 mr-2" /> Logout
        </Button>
      </div>
      
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        <Card className="hover:shadow-md transition-shadow">
          <CardHeader>
            <CardTitle>Browse Books</CardTitle>
          </CardHeader>
          <CardContent>
            <p className="mb-4">Explore our collection and purchase books.</p>
            <Button onClick={() => onNavigate("customer-browse-books")}>
              <BookOpen className="h-4 w-4 mr-2" /> Browse Books
            </Button>
          </CardContent>
        </Card>
        
        <Card className="hover:shadow-md transition-shadow">
          <CardHeader>
            <CardTitle>Purchase History</CardTitle>
          </CardHeader>
          <CardContent>
            <p className="mb-4">
              View your purchase history and receipts.
              {customer.purchaseHistory.length > 0 && ` You have made ${customer.purchaseHistory.length} purchase(s).`}
            </p>
            <Button onClick={() => onNavigate("customer-purchase-history")}>
              <History className="h-4 w-4 mr-2" /> View History
            </Button>
          </CardContent>
        </Card>
      </div>
    </div>
  );
};

export default CustomerDashboard;
