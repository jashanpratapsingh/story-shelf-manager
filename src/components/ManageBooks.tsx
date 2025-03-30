
import React, { useState, useEffect } from 'react';
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { useToast } from "@/components/ui/use-toast";
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/lib/table";
import { Book } from "@/lib/types";
import { ArrowLeft, Plus, Trash2 } from "lucide-react";

interface ManageBooksProps {
  books: Book[];
  onAddBook: (bookData: Omit<Book, "id">) => void;
  onUpdateBook: (updatedBook: Book) => void;
  onDeleteBook: (bookId: string) => void;
  onBack: () => void;
}

const ManageBooks: React.FC<ManageBooksProps> = ({
  books,
  onAddBook,
  onDeleteBook,
  onBack
}) => {
  const [title, setTitle] = useState("");
  const [price, setPrice] = useState("");
  const [selectedBookId, setSelectedBookId] = useState<string | null>(null);
  const { toast } = useToast();

  const handleAddBook = () => {
    if (!title || !price) {
      toast({
        title: "Input Error",
        description: "Please enter both book name and price",
        variant: "destructive",
      });
      return;
    }

    const priceValue = parseFloat(price);
    if (isNaN(priceValue) || priceValue <= 0) {
      toast({
        title: "Input Error",
        description: "Please enter a valid positive price",
        variant: "destructive",
      });
      return;
    }

    // Check if book with same name already exists
    if (books.some(book => book.title.toLowerCase() === title.toLowerCase())) {
      toast({
        title: "Input Error",
        description: "A book with this name already exists",
        variant: "destructive",
      });
      return;
    }

    onAddBook({
      title,
      author: "",
      price: priceValue,
      quantity: 1
    });

    setTitle("");
    setPrice("");

    toast({
      title: "Success",
      description: "Book added successfully",
    });
  };

  const handleDeleteBook = () => {
    if (!selectedBookId) {
      toast({
        title: "Selection Error",
        description: "Please select a book to delete",
        variant: "destructive",
      });
      return;
    }

    onDeleteBook(selectedBookId);
    setSelectedBookId(null);

    toast({
      title: "Success",
      description: "Book deleted successfully",
    });
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
        <h1 className="text-2xl font-bold">Manage Books</h1>
      </div>

      {/* Books Table */}
      <div className="mb-6 border rounded-md overflow-hidden">
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>Book Name</TableHead>
              <TableHead>Book Price</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {books.length === 0 ? (
              <TableRow>
                <TableCell colSpan={2} className="text-center py-4">
                  No books available
                </TableCell>
              </TableRow>
            ) : (
              books.map((book) => (
                <TableRow 
                  key={book.id} 
                  className={selectedBookId === book.id ? "bg-secondary/50" : ""}
                  onClick={() => setSelectedBookId(book.id)}
                >
                  <TableCell>{book.title}</TableCell>
                  <TableCell>${book.price.toFixed(2)}</TableCell>
                </TableRow>
              ))
            )}
          </TableBody>
        </Table>
      </div>

      {/* Add Book Form */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mb-6">
        <div className="space-y-4 p-6 border rounded-md">
          <h2 className="text-xl font-semibold mb-4">Add New Book</h2>
          <div className="space-y-2">
            <Label htmlFor="bookName">Name</Label>
            <Input
              id="bookName"
              value={title}
              onChange={(e) => setTitle(e.target.value)}
              placeholder="Enter book name"
            />
          </div>
          <div className="space-y-2">
            <Label htmlFor="bookPrice">Price</Label>
            <Input
              id="bookPrice"
              type="number"
              min="0.01"
              step="0.01"
              value={price}
              onChange={(e) => setPrice(e.target.value)}
              placeholder="Enter book price"
            />
          </div>
          <Button onClick={handleAddBook} className="w-full">
            <Plus className="h-4 w-4 mr-2" /> Add Book
          </Button>
        </div>

        {/* Delete Book Section */}
        <div className="space-y-4 p-6 border rounded-md">
          <h2 className="text-xl font-semibold mb-4">Delete Book</h2>
          <p className="text-sm text-gray-500 mb-4">
            Select a book from the table above and click the button below to delete it.
          </p>
          <Button 
            variant="destructive" 
            onClick={handleDeleteBook} 
            className="w-full"
            disabled={!selectedBookId}
          >
            <Trash2 className="h-4 w-4 mr-2" /> Delete Selected Book
          </Button>
        </div>
      </div>
    </div>
  );
};

export default ManageBooks;
