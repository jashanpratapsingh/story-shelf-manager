
import React, { useState } from 'react';
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Label } from "@/components/ui/label";
import { Book } from "@/lib/types";
import { useToast } from "@/components/ui/use-toast";

interface ManageBooksProps {
  books: Book[];
  onAddBook: (book: Omit<Book, "id">) => void;
  onUpdateBook: (book: Book) => void;
  onDeleteBook: (id: string) => void;
  onBack: () => void;
}

const ManageBooks: React.FC<ManageBooksProps> = ({
  books,
  onAddBook,
  onUpdateBook,
  onDeleteBook,
  onBack
}) => {
  const [title, setTitle] = useState('');
  const [author, setAuthor] = useState('');
  const [price, setPrice] = useState('');
  const [quantity, setQuantity] = useState('');
  const [editId, setEditId] = useState<string | null>(null);
  const { toast } = useToast();

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    
    if (!title || !author || !price || !quantity) {
      toast({
        title: "Error",
        description: "All fields are required",
        variant: "destructive",
      });
      return;
    }
    
    const priceNum = parseFloat(price);
    const quantityNum = parseInt(quantity, 10);
    
    if (isNaN(priceNum) || isNaN(quantityNum) || priceNum <= 0 || quantityNum < 0) {
      toast({
        title: "Error",
        description: "Price must be positive and quantity must be non-negative",
        variant: "destructive",
      });
      return;
    }
    
    if (editId) {
      onUpdateBook({
        id: editId,
        title,
        author,
        price: priceNum,
        quantity: quantityNum
      });
      setEditId(null);
      toast({
        title: "Success",
        description: "Book updated successfully",
      });
    } else {
      onAddBook({
        title,
        author,
        price: priceNum,
        quantity: quantityNum
      });
      toast({
        title: "Success",
        description: "Book added successfully",
      });
    }
    
    // Reset form
    setTitle('');
    setAuthor('');
    setPrice('');
    setQuantity('');
  };

  const handleEdit = (book: Book) => {
    setTitle(book.title);
    setAuthor(book.author);
    setPrice(book.price.toString());
    setQuantity(book.quantity.toString());
    setEditId(book.id);
  };

  const handleCancel = () => {
    setTitle('');
    setAuthor('');
    setPrice('');
    setQuantity('');
    setEditId(null);
  };

  return (
    <div className="container mx-auto p-4">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-2xl font-bold">Manage Books</h1>
        <Button variant="outline" onClick={onBack}>
          Back to Dashboard
        </Button>
      </div>
      
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        <Card className="lg:col-span-1">
          <CardHeader>
            <CardTitle>{editId ? 'Edit Book' : 'Add New Book'}</CardTitle>
          </CardHeader>
          <CardContent>
            <form onSubmit={handleSubmit} className="space-y-4">
              <div className="space-y-2">
                <Label htmlFor="title">Title</Label>
                <Input
                  id="title"
                  value={title}
                  onChange={(e) => setTitle(e.target.value)}
                  placeholder="Book title"
                />
              </div>
              
              <div className="space-y-2">
                <Label htmlFor="author">Author</Label>
                <Input
                  id="author"
                  value={author}
                  onChange={(e) => setAuthor(e.target.value)}
                  placeholder="Author name"
                />
              </div>
              
              <div className="space-y-2">
                <Label htmlFor="price">Price</Label>
                <Input
                  id="price"
                  type="number"
                  step="0.01"
                  min="0.01"
                  value={price}
                  onChange={(e) => setPrice(e.target.value)}
                  placeholder="Price"
                />
              </div>
              
              <div className="space-y-2">
                <Label htmlFor="quantity">Quantity</Label>
                <Input
                  id="quantity"
                  type="number"
                  min="0"
                  value={quantity}
                  onChange={(e) => setQuantity(e.target.value)}
                  placeholder="Quantity in stock"
                />
              </div>
              
              <div className="flex space-x-2">
                <Button type="submit" className="flex-1">
                  {editId ? 'Update Book' : 'Add Book'}
                </Button>
                {editId && (
                  <Button 
                    type="button" 
                    variant="outline" 
                    onClick={handleCancel}
                  >
                    Cancel
                  </Button>
                )}
              </div>
            </form>
          </CardContent>
        </Card>
        
        <Card className="lg:col-span-2">
          <CardHeader>
            <CardTitle>Book Inventory</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="overflow-x-auto">
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>Title</TableHead>
                    <TableHead>Author</TableHead>
                    <TableHead>Price</TableHead>
                    <TableHead>Quantity</TableHead>
                    <TableHead>Actions</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {books.length === 0 ? (
                    <TableRow>
                      <TableCell colSpan={5} className="text-center">
                        No books available
                      </TableCell>
                    </TableRow>
                  ) : (
                    books.map((book) => (
                      <TableRow key={book.id}>
                        <TableCell>{book.title}</TableCell>
                        <TableCell>{book.author}</TableCell>
                        <TableCell>${book.price.toFixed(2)}</TableCell>
                        <TableCell>{book.quantity}</TableCell>
                        <TableCell>
                          <div className="flex space-x-2">
                            <Button 
                              variant="outline" 
                              size="sm"
                              onClick={() => handleEdit(book)}
                            >
                              Edit
                            </Button>
                            <Button 
                              variant="destructive" 
                              size="sm"
                              onClick={() => onDeleteBook(book.id)}
                            >
                              Delete
                            </Button>
                          </div>
                        </TableCell>
                      </TableRow>
                    ))
                  )}
                </TableBody>
              </Table>
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  );
};

export default ManageBooks;
