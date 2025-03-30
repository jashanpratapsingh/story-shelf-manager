
import React from 'react';
import { Button } from "@/components/ui/button";
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Customer } from "@/lib/types";

interface PurchaseHistoryProps {
  customer: Customer;
  onBack: () => void;
}

const PurchaseHistory: React.FC<PurchaseHistoryProps> = ({ customer, onBack }) => {
  // Sort purchase history with newest first
  const sortedPurchases = [...customer.purchaseHistory].sort((a, b) => {
    return new Date(b.date).getTime() - new Date(a.date).getTime();
  });

  return (
    <div className="container mx-auto p-4">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-2xl font-bold">Purchase History</h1>
        <Button variant="outline" onClick={onBack}>
          Back to Dashboard
        </Button>
      </div>
      
      <Card>
        <CardHeader>
          <CardTitle>Your Purchase History</CardTitle>
        </CardHeader>
        <CardContent>
          {sortedPurchases.length === 0 ? (
            <div className="text-center py-8">
              <p className="text-gray-500">You haven't made any purchases yet.</p>
            </div>
          ) : (
            <div className="overflow-x-auto">
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>Date</TableHead>
                    <TableHead>Book</TableHead>
                    <TableHead>Quantity</TableHead>
                    <TableHead>Total</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {sortedPurchases.map((purchase) => (
                    <TableRow key={purchase.id}>
                      <TableCell>
                        {new Date(purchase.date).toLocaleDateString()}
                      </TableCell>
                      <TableCell>{purchase.bookTitle}</TableCell>
                      <TableCell>{purchase.quantity}</TableCell>
                      <TableCell>${purchase.totalPrice.toFixed(2)}</TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </div>
          )}
        </CardContent>
      </Card>
    </div>
  );
};

export default PurchaseHistory;
