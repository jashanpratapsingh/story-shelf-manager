
import React from 'react';
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Customer } from "@/lib/types";
import { LogOut } from "lucide-react";

interface CustomerCostScreenProps {
  customer: Customer;
  points: number;
  onLogout: () => void;
}

const CustomerCostScreen: React.FC<CustomerCostScreenProps> = ({
  customer,
  points,
  onLogout
}) => {
  // Calculate the total cost of the most recent purchase
  const getLatestPurchaseCost = (): number => {
    if (!customer.purchaseHistory.length) return 0;
    
    // Sort purchases by date descending
    const sortedPurchases = [...customer.purchaseHistory]
      .sort((a, b) => new Date(b.date).getTime() - new Date(a.date).getTime());
    
    return sortedPurchases[0].totalPrice;
  };

  const totalCost = getLatestPurchaseCost();
  const status = points >= 1000 ? "Gold" : "Silver";
  
  return (
    <div className="container mx-auto p-4 flex items-center justify-center min-h-screen">
      <Card className="w-full max-w-md">
        <CardHeader className="text-center">
          <CardTitle className="text-2xl">Purchase Complete</CardTitle>
        </CardHeader>
        <CardContent className="space-y-6 text-center">
          <div className="text-2xl font-bold pb-4 border-b">
            Total Cost: ${totalCost.toFixed(2)}
          </div>
          
          <div className="text-lg pb-4 border-b">
            Points: {points}, Status: {status}
          </div>
          
          <Button onClick={onLogout} className="w-full">
            <LogOut className="h-4 w-4 mr-2" /> Logout
          </Button>
        </CardContent>
      </Card>
    </div>
  );
};

export default CustomerCostScreen;
