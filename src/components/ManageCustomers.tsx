
import React, { useState } from 'react';
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { useToast } from "@/components/ui/use-toast";
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/lib/table";
import { Customer } from "@/lib/types";
import { ArrowLeft, Plus, Trash2 } from "lucide-react";

interface ManageCustomersProps {
  customers: Customer[];
  onAddCustomer: (customerData: Omit<Customer, "id" | "purchaseHistory">) => void;
  onUpdateCustomer: (updatedCustomer: Customer) => void;
  onDeleteCustomer: (customerId: string) => void;
  onBack: () => void;
}

const ManageCustomers: React.FC<ManageCustomersProps> = ({
  customers,
  onAddCustomer,
  onDeleteCustomer,
  onBack
}) => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [name, setName] = useState("");
  const [selectedCustomerId, setSelectedCustomerId] = useState<string | null>(null);
  const { toast } = useToast();

  // Calculate total points for a customer
  const calculatePoints = (customer: Customer): number => {
    if (!customer.purchaseHistory.length) return 0;
    
    return customer.purchaseHistory.reduce((total, purchase) => {
      return total + purchase.totalPrice * 10; // 10 points per CAD
    }, 0);
  };
  
  const handleAddCustomer = () => {
    if (!username || !password) {
      toast({
        title: "Input Error",
        description: "Please enter both username and password",
        variant: "destructive",
      });
      return;
    }
    
    // Check if customer with same username already exists
    if (customers.some(customer => customer.username.toLowerCase() === username.toLowerCase())) {
      toast({
        title: "Input Error",
        description: "A customer with this username already exists",
        variant: "destructive",
      });
      return;
    }
    
    onAddCustomer({
      username,
      password,
      name: name || username
    });
    
    setUsername("");
    setPassword("");
    setName("");
    
    toast({
      title: "Success",
      description: "Customer added successfully",
    });
  };
  
  const handleDeleteCustomer = () => {
    if (!selectedCustomerId) {
      toast({
        title: "Selection Error",
        description: "Please select a customer to delete",
        variant: "destructive",
      });
      return;
    }
    
    onDeleteCustomer(selectedCustomerId);
    setSelectedCustomerId(null);
    
    toast({
      title: "Success",
      description: "Customer deleted successfully",
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
        <h1 className="text-2xl font-bold">Manage Customers</h1>
      </div>
      
      {/* Customers Table */}
      <div className="mb-6 border rounded-md overflow-hidden">
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>Username</TableHead>
              <TableHead>Password</TableHead>
              <TableHead>Points</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {customers.length === 0 ? (
              <TableRow>
                <TableCell colSpan={3} className="text-center py-4">
                  No customers available
                </TableCell>
              </TableRow>
            ) : (
              customers.map((customer) => (
                <TableRow 
                  key={customer.id} 
                  className={selectedCustomerId === customer.id ? "bg-secondary/50" : ""}
                  onClick={() => setSelectedCustomerId(customer.id)}
                >
                  <TableCell>{customer.username}</TableCell>
                  <TableCell>{customer.password}</TableCell>
                  <TableCell>{calculatePoints(customer)}</TableCell>
                </TableRow>
              ))
            )}
          </TableBody>
        </Table>
      </div>
      
      {/* Add Customer Form */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mb-6">
        <div className="space-y-4 p-6 border rounded-md">
          <h2 className="text-xl font-semibold mb-4">Add New Customer</h2>
          <div className="space-y-2">
            <Label htmlFor="username">Username</Label>
            <Input
              id="username"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              placeholder="Enter username"
            />
          </div>
          <div className="space-y-2">
            <Label htmlFor="password">Password</Label>
            <Input
              id="password"
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              placeholder="Enter password"
            />
          </div>
          <div className="space-y-2">
            <Label htmlFor="name">Name (Optional)</Label>
            <Input
              id="name"
              value={name}
              onChange={(e) => setName(e.target.value)}
              placeholder="Enter name"
            />
          </div>
          <Button onClick={handleAddCustomer} className="w-full">
            <Plus className="h-4 w-4 mr-2" /> Add Customer
          </Button>
        </div>
        
        {/* Delete Customer Section */}
        <div className="space-y-4 p-6 border rounded-md">
          <h2 className="text-xl font-semibold mb-4">Delete Customer</h2>
          <p className="text-sm text-gray-500 mb-4">
            Select a customer from the table above and click the button below to delete it.
          </p>
          <Button 
            variant="destructive" 
            onClick={handleDeleteCustomer} 
            className="w-full"
            disabled={!selectedCustomerId}
          >
            <Trash2 className="h-4 w-4 mr-2" /> Delete Selected Customer
          </Button>
        </div>
      </div>
    </div>
  );
};

export default ManageCustomers;
