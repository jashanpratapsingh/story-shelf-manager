
import React, { useState } from 'react';
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Label } from "@/components/ui/label";
import { Customer } from "@/lib/types";
import { useToast } from "@/components/ui/use-toast";

interface ManageCustomersProps {
  customers: Customer[];
  onAddCustomer: (customer: Omit<Customer, "id" | "purchaseHistory">) => void;
  onUpdateCustomer: (customer: Customer) => void;
  onDeleteCustomer: (id: string) => void;
  onBack: () => void;
}

const ManageCustomers: React.FC<ManageCustomersProps> = ({
  customers,
  onAddCustomer,
  onUpdateCustomer,
  onDeleteCustomer,
  onBack
}) => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [name, setName] = useState('');
  const [editId, setEditId] = useState<string | null>(null);
  const { toast } = useToast();

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    
    if (!username || !password || !name) {
      toast({
        title: "Error",
        description: "All fields are required",
        variant: "destructive",
      });
      return;
    }
    
    // Check for username duplication
    const existingCustomer = customers.find(c => c.username === username && c.id !== editId);
    if (existingCustomer) {
      toast({
        title: "Error",
        description: "Username already exists",
        variant: "destructive",
      });
      return;
    }
    
    if (editId) {
      const customer = customers.find(c => c.id === editId);
      if (customer) {
        onUpdateCustomer({
          ...customer,
          username,
          password,
          name
        });
      }
      setEditId(null);
      toast({
        title: "Success",
        description: "Customer updated successfully",
      });
    } else {
      onAddCustomer({
        username,
        password,
        name
      });
      toast({
        title: "Success",
        description: "Customer added successfully",
      });
    }
    
    // Reset form
    setUsername('');
    setPassword('');
    setName('');
  };

  const handleEdit = (customer: Customer) => {
    setUsername(customer.username);
    setPassword(customer.password);
    setName(customer.name);
    setEditId(customer.id);
  };

  const handleCancel = () => {
    setUsername('');
    setPassword('');
    setName('');
    setEditId(null);
  };

  return (
    <div className="container mx-auto p-4">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-2xl font-bold">Manage Customers</h1>
        <Button variant="outline" onClick={onBack}>
          Back to Dashboard
        </Button>
      </div>
      
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        <Card className="lg:col-span-1">
          <CardHeader>
            <CardTitle>{editId ? 'Edit Customer' : 'Add New Customer'}</CardTitle>
          </CardHeader>
          <CardContent>
            <form onSubmit={handleSubmit} className="space-y-4">
              <div className="space-y-2">
                <Label htmlFor="username">Username</Label>
                <Input
                  id="username"
                  value={username}
                  onChange={(e) => setUsername(e.target.value)}
                  placeholder="Username"
                />
              </div>
              
              <div className="space-y-2">
                <Label htmlFor="password">Password</Label>
                <Input
                  id="password"
                  type="password"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  placeholder="Password"
                />
              </div>
              
              <div className="space-y-2">
                <Label htmlFor="name">Full Name</Label>
                <Input
                  id="name"
                  value={name}
                  onChange={(e) => setName(e.target.value)}
                  placeholder="Full Name"
                />
              </div>
              
              <div className="flex space-x-2">
                <Button type="submit" className="flex-1">
                  {editId ? 'Update Customer' : 'Add Customer'}
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
            <CardTitle>Customer List</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="overflow-x-auto">
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>Username</TableHead>
                    <TableHead>Name</TableHead>
                    <TableHead>Purchase Count</TableHead>
                    <TableHead>Actions</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {customers.length === 0 ? (
                    <TableRow>
                      <TableCell colSpan={4} className="text-center">
                        No customers available
                      </TableCell>
                    </TableRow>
                  ) : (
                    customers.map((customer) => (
                      <TableRow key={customer.id}>
                        <TableCell>{customer.username}</TableCell>
                        <TableCell>{customer.name}</TableCell>
                        <TableCell>{customer.purchaseHistory.length}</TableCell>
                        <TableCell>
                          <div className="flex space-x-2">
                            <Button 
                              variant="outline" 
                              size="sm"
                              onClick={() => handleEdit(customer)}
                            >
                              Edit
                            </Button>
                            <Button 
                              variant="destructive" 
                              size="sm"
                              onClick={() => onDeleteCustomer(customer.id)}
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

export default ManageCustomers;
