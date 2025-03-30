
import React from 'react';
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/lib/table";
import { Customer } from "@/lib/types";
import { ArrowLeft } from "lucide-react";

interface PurchaseHistoryProps {
  customer: Customer;
  onBack: () => void;
}

const PurchaseHistory: React.FC<PurchaseHistoryProps> = ({
  customer,
  onBack
}) => {
  const calculateTotalPoints = (): number => {
    if (!customer.purchaseHistory.length) return 0;
    
    return customer.purchaseHistory.reduce((total, purchase) => {
      return total + purchase.totalPrice * 10; // 10 points per CAD
    }, 0);
  };
  
  const formatDate = (dateString: string): string => {
    const date = new Date(dateString);
    return date.toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  };
  
  const customerPoints = calculateTotalPoints();
  const customerStatus = customerPoints >= 1000 ? "Gold" : "Silver";
  
  return (
    <div className="container mx-auto p-4">
      <div className="flex items-center mb-6">
        <Button 
          variant="ghost" 
          className="p-2 mr-2" 
          onClick={onBack}
        >
          <ArrowLeft className="h-5 w-5" />
        </Button>
        <div>
          <h1 className="text-2xl font-bold">Purchase History</h1>
          <p className="text-sm text-gray-500">
            Points: {customerPoints}, Status: {customerStatus}
          </p>
        </div>
      </div>
      
      <Card>
        <CardHeader>
          <CardTitle>Your Purchases</CardTitle>
        </CardHeader>
        <CardContent>
          {customer.purchaseHistory.length === 0 ? (
            <p className="text-center py-4 text-gray-500">
              You haven't made any purchases yet.
            </p>
          ) : (
            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead>Book Title</TableHead>
                  <TableHead>Quantity</TableHead>
                  <TableHead>Price</TableHead>
                  <TableHead>Date</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {customer.purchaseHistory.map((purchase) => (
                  <TableRow key={purchase.id}>
                    <TableCell>{purchase.bookTitle}</TableCell>
                    <TableCell>{purchase.quantity}</TableCell>
                    <TableCell>${purchase.totalPrice.toFixed(2)}</TableCell>
                    <TableCell>{formatDate(purchase.date)}</TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          )}
        </CardContent>
      </Card>
    </div>
  );
};

export default PurchaseHistory;
