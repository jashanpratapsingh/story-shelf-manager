
import React, { useMemo } from 'react';
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Book, Customer, Purchase } from "@/lib/types";
import { 
  BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer,
  PieChart, Pie, Cell, Legend
} from 'recharts';

interface ViewStatsProps {
  books: Book[];
  customers: Customer[];
  onBack: () => void;
}

const COLORS = ['#0088FE', '#00C49F', '#FFBB28', '#FF8042', '#8884D8'];

const ViewStats: React.FC<ViewStatsProps> = ({ books, customers, onBack }) => {
  // Calculate total sales
  const totalSales = useMemo(() => {
    return customers.reduce((total, customer) => {
      return total + customer.purchaseHistory.reduce((sum, purchase) => {
        return sum + purchase.totalPrice;
      }, 0);
    }, 0);
  }, [customers]);

  // Calculate total books sold
  const totalBooksSold = useMemo(() => {
    return customers.reduce((total, customer) => {
      return total + customer.purchaseHistory.reduce((sum, purchase) => {
        return sum + purchase.quantity;
      }, 0);
    }, 0);
  }, [customers]);

  // Get all purchases across all customers
  const allPurchases = useMemo(() => {
    const purchases: Purchase[] = [];
    customers.forEach(customer => {
      purchases.push(...customer.purchaseHistory);
    });
    return purchases;
  }, [customers]);

  // Calculate top selling books
  const topSellingBooks = useMemo(() => {
    const bookSales: Record<string, { title: string, quantity: number }> = {};
    
    allPurchases.forEach(purchase => {
      if (!bookSales[purchase.bookId]) {
        bookSales[purchase.bookId] = {
          title: purchase.bookTitle,
          quantity: 0
        };
      }
      bookSales[purchase.bookId].quantity += purchase.quantity;
    });
    
    return Object.values(bookSales)
      .sort((a, b) => b.quantity - a.quantity)
      .slice(0, 5)
      .map(book => ({
        name: book.title,
        value: book.quantity
      }));
  }, [allPurchases]);

  // Calculate monthly sales data
  const monthlySalesData = useMemo(() => {
    const monthlyData: Record<string, number> = {};
    
    allPurchases.forEach(purchase => {
      const date = new Date(purchase.date);
      const monthYear = `${date.getMonth() + 1}/${date.getFullYear()}`;
      
      if (!monthlyData[monthYear]) {
        monthlyData[monthYear] = 0;
      }
      monthlyData[monthYear] += purchase.totalPrice;
    });
    
    return Object.entries(monthlyData).map(([month, sales]) => ({
      month,
      sales
    })).sort((a, b) => {
      const [aMonth, aYear] = a.month.split('/').map(Number);
      const [bMonth, bYear] = b.month.split('/').map(Number);
      return (aYear * 12 + aMonth) - (bYear * 12 + bMonth);
    });
  }, [allPurchases]);

  return (
    <div className="container mx-auto p-4">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-2xl font-bold">Bookstore Statistics</h1>
        <Button variant="outline" onClick={onBack}>
          Back to Dashboard
        </Button>
      </div>
      
      <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-6">
        <Card>
          <CardHeader className="pb-2">
            <CardTitle className="text-sm font-medium">Total Sales</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">${totalSales.toFixed(2)}</div>
          </CardContent>
        </Card>
        
        <Card>
          <CardHeader className="pb-2">
            <CardTitle className="text-sm font-medium">Total Books Sold</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{totalBooksSold}</div>
          </CardContent>
        </Card>
        
        <Card>
          <CardHeader className="pb-2">
            <CardTitle className="text-sm font-medium">Total Customers</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{customers.length}</div>
          </CardContent>
        </Card>
      </div>
      
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6 mb-6">
        <Card>
          <CardHeader>
            <CardTitle>Monthly Sales</CardTitle>
          </CardHeader>
          <CardContent className="h-[300px]">
            {monthlySalesData.length > 0 ? (
              <ResponsiveContainer width="100%" height="100%">
                <BarChart data={monthlySalesData}>
                  <CartesianGrid strokeDasharray="3 3" />
                  <XAxis dataKey="month" />
                  <YAxis />
                  <Tooltip formatter={(value) => [`$${value}`, 'Sales']} />
                  <Bar dataKey="sales" fill="#8884d8" />
                </BarChart>
              </ResponsiveContainer>
            ) : (
              <div className="h-full flex items-center justify-center text-gray-500">
                No sales data available
              </div>
            )}
          </CardContent>
        </Card>
        
        <Card>
          <CardHeader>
            <CardTitle>Top Selling Books</CardTitle>
          </CardHeader>
          <CardContent className="h-[300px]">
            {topSellingBooks.length > 0 ? (
              <ResponsiveContainer width="100%" height="100%">
                <PieChart>
                  <Pie
                    data={topSellingBooks}
                    cx="50%"
                    cy="50%"
                    outerRadius={80}
                    fill="#8884d8"
                    dataKey="value"
                    label={({ name, percent }) => 
                      `${name}: ${(percent * 100).toFixed(0)}%`
                    }
                  >
                    {topSellingBooks.map((entry, index) => (
                      <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                    ))}
                  </Pie>
                  <Tooltip />
                  <Legend />
                </PieChart>
              </ResponsiveContainer>
            ) : (
              <div className="h-full flex items-center justify-center text-gray-500">
                No book sales data available
              </div>
            )}
          </CardContent>
        </Card>
      </div>
      
      <Card>
        <CardHeader>
          <CardTitle>Inventory Status</CardTitle>
        </CardHeader>
        <CardContent>
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead>Book Title</TableHead>
                <TableHead>Author</TableHead>
                <TableHead>Current Stock</TableHead>
                <TableHead>Status</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {books.length === 0 ? (
                <TableRow>
                  <TableCell colSpan={4} className="text-center">
                    No books in inventory
                  </TableCell>
                </TableRow>
              ) : (
                books.map((book) => (
                  <TableRow key={book.id}>
                    <TableCell>{book.title}</TableCell>
                    <TableCell>{book.author}</TableCell>
                    <TableCell>{book.quantity}</TableCell>
                    <TableCell>
                      {book.quantity === 0 ? (
                        <span className="text-red-500 font-medium">Out of Stock</span>
                      ) : book.quantity < 5 ? (
                        <span className="text-amber-500 font-medium">Low Stock</span>
                      ) : (
                        <span className="text-green-500 font-medium">In Stock</span>
                      )}
                    </TableCell>
                  </TableRow>
                ))
              )}
            </TableBody>
          </Table>
        </CardContent>
      </Card>
    </div>
  );
};

export default ViewStats;
