
import React, { useState } from 'react';
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Card, CardContent, CardHeader, CardTitle, CardFooter } from "@/components/ui/card";
import { Book, Customer } from "@/lib/types";
import { useToast } from "@/components/ui/use-toast";

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
  const [searchTerm, setSearchTerm] = useState('');
  const [purchaseQuantities, setPurchaseQuantities] = useState<Record<string, number>>({});
  const { toast } = useToast();

  const handleQuantityChange = (bookId: string, quantity: number) => {
    setPurchaseQuantities(prev => ({
      ...prev,
      [bookId]: quantity
    }));
  };

  const handlePurchase = (book: Book) => {
    const quantity = purchaseQuantities[book.id] || 1;
    
    if (quantity <= 0) {
      toast({
        title: "Error",
        description: "Quantity must be greater than zero",
        variant: "destructive",
      });
      return;
    }
    
    if (quantity > book.quantity) {
      toast({
        title: "Error",
        description: `Only ${book.quantity} copies available`,
        variant: "destructive",
      });
      return;
    }
    
    onPurchase(book.id, quantity);
    
    // Reset purchase quantity
    setPurchaseQuantities(prev => ({
      ...prev,
      [book.id]: 1
    }));
    
    toast({
      title: "Success",
      description: `Successfully purchased ${quantity} ${quantity === 1 ? 'copy' : 'copies'} of "${book.title}"`,
    });
  };

  const filteredBooks = books.filter(book => 
    book.title.toLowerCase().includes(searchTerm.toLowerCase()) || 
    book.author.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className="container mx-auto p-4">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-2xl font-bold">Browse Books</h1>
        <Button variant="outline" onClick={onBack}>
          Back to Dashboard
        </Button>
      </div>
      
      <div className="mb-6">
        <Input
          placeholder="Search by title or author..."
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          className="max-w-md"
        />
      </div>
      
      {filteredBooks.length === 0 ? (
        <div className="text-center py-8">
          <p className="text-gray-500">No books found matching your search.</p>
        </div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {filteredBooks.map(book => (
            <Card key={book.id} className="flex flex-col">
              <CardHeader>
                <CardTitle>{book.title}</CardTitle>
              </CardHeader>
              <CardContent className="flex-grow">
                <p className="text-gray-700 mb-2">Author: {book.author}</p>
                <p className="text-gray-700 mb-2">Price: ${book.price.toFixed(2)}</p>
                <p className={`font-medium ${book.quantity > 0 ? 'text-green-600' : 'text-red-600'}`}>
                  {book.quantity > 0 
                    ? `${book.quantity} in stock` 
                    : 'Out of stock'}
                </p>
              </CardContent>
              {book.quantity > 0 && (
                <CardFooter className="border-t pt-4 flex flex-col items-start space-y-3">
                  <div className="flex items-center w-full space-x-2">
                    <Label htmlFor={`quantity-${book.id}`} className="flex-none">
                      Quantity:
                    </Label>
                    <Input
                      id={`quantity-${book.id}`}
                      type="number"
                      min="1"
                      max={book.quantity}
                      value={purchaseQuantities[book.id] || 1}
                      onChange={(e) => handleQuantityChange(book.id, parseInt(e.target.value, 10))}
                      className="w-20"
                    />
                  </div>
                  <Button 
                    onClick={() => handlePurchase(book)}
                    className="w-full"
                  >
                    Purchase
                  </Button>
                </CardFooter>
              )}
            </Card>
          ))}
        </div>
      )}
    </div>
  );
};

export default BrowseBooks;
