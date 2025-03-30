
import React, { useState } from 'react';
import { Button } from "@/components/ui/button";
import { Checkbox } from "@/components/ui/checkbox";
import { useToast } from "@/components/ui/use-toast";
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/lib/table";
import { Book, Customer } from "@/lib/types";
import { ArrowLeft, ShoppingCart, Gift } from "lucide-react";

interface BrowseBooksProps {
  books: Book[];
  customer: Customer;
  onPurchase: (bookId: string, quantity: number) => void;
  onBack: () => void;
}

const BrowseBooks: React.FC<BrowseBooksProps> = ({
  books,
  customer,
  onPurchase,
  onBack
}) => {
  const [selectedBooks, setSelectedBooks] = useState<string[]>([]);
  const { toast } = useToast();

  const calculateTotalPoints = (): number => {
    if (!customer.purchaseHistory.length) return 0;
    
    return customer.purchaseHistory.reduce((total, purchase) => {
      return total + purchase.totalPrice * 10; // 10 points per CAD
    }, 0);
  };
  
  const customerPoints = calculateTotalPoints();
  const customerStatus = customerPoints >= 1000 ? "Gold" : "Silver";
  
  const handleToggleBook = (bookId: string, checked: boolean) => {
    if (checked) {
      setSelectedBooks([...selectedBooks, bookId]);
    } else {
      setSelectedBooks(selectedBooks.filter(id => id !== bookId));
    }
  };
  
  const handleBuy = (usePoints: boolean) => {
    if (selectedBooks.length === 0) {
      toast({
        title: "Selection Error",
        description: "Please select at least one book to purchase",
        variant: "destructive",
      });
      return;
    }
    
    // Purchase each selected book
    selectedBooks.forEach(bookId => {
      onPurchase(bookId, 1);
    });
    
    // Clear selections
    setSelectedBooks([]);
  };
  
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
          <h1 className="text-2xl font-bold">Browse Books</h1>
          <p className="text-sm text-gray-500">
            Points: {customerPoints}, Status: {customerStatus}
          </p>
        </div>
      </div>
      
      {/* Books Table */}
      <div className="mb-6 border rounded-md overflow-hidden">
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>Book Name</TableHead>
              <TableHead>Book Price</TableHead>
              <TableHead>Select</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {books.length === 0 ? (
              <TableRow>
                <TableCell colSpan={3} className="text-center py-4">
                  No books available
                </TableCell>
              </TableRow>
            ) : (
              books.map((book) => (
                <TableRow key={book.id}>
                  <TableCell>{book.title}</TableCell>
                  <TableCell>${book.price.toFixed(2)}</TableCell>
                  <TableCell>
                    <Checkbox 
                      checked={selectedBooks.includes(book.id)}
                      onCheckedChange={(checked) => 
                        handleToggleBook(book.id, checked === true)
                      }
                    />
                  </TableCell>
                </TableRow>
              ))
            )}
          </TableBody>
        </Table>
      </div>
      
      {/* Purchase Buttons */}
      <div className="flex flex-col sm:flex-row gap-4 justify-center">
        <Button 
          onClick={() => handleBuy(false)}
          disabled={selectedBooks.length === 0}
          className="flex-1 max-w-xs mx-auto"
        >
          <ShoppingCart className="h-4 w-4 mr-2" /> Buy
        </Button>
        
        <Button 
          onClick={() => handleBuy(true)}
          disabled={selectedBooks.length === 0 || customerPoints === 0}
          className="flex-1 max-w-xs mx-auto"
          variant="secondary"
        >
          <Gift className="h-4 w-4 mr-2" /> Redeem Points and Buy
        </Button>
      </div>
    </div>
  );
};

export default BrowseBooks;
