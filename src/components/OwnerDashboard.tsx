
import React from 'react';
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";

interface OwnerDashboardProps {
  onNavigate: (screen: string) => void;
  onLogout: () => void;
}

const OwnerDashboard: React.FC<OwnerDashboardProps> = ({ onNavigate, onLogout }) => {
  return (
    <div className="container mx-auto p-4">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-2xl font-bold">BookStore Admin Dashboard</h1>
        <Button variant="outline" onClick={onLogout}>
          Logout
        </Button>
      </div>
      
      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
        <Card className="hover:shadow-md transition-shadow">
          <CardHeader>
            <CardTitle>Manage Books</CardTitle>
          </CardHeader>
          <CardContent>
            <p className="mb-4">Add, edit, or remove books from the inventory.</p>
            <Button onClick={() => onNavigate("owner-manage-books")}>
              Manage Books
            </Button>
          </CardContent>
        </Card>
        
        <Card className="hover:shadow-md transition-shadow">
          <CardHeader>
            <CardTitle>Manage Customers</CardTitle>
          </CardHeader>
          <CardContent>
            <p className="mb-4">Add, edit, or remove customer accounts.</p>
            <Button onClick={() => onNavigate("owner-manage-customers")}>
              Manage Customers
            </Button>
          </CardContent>
        </Card>
        
        <Card className="hover:shadow-md transition-shadow">
          <CardHeader>
            <CardTitle>View Statistics</CardTitle>
          </CardHeader>
          <CardContent>
            <p className="mb-4">View sales statistics and reports.</p>
            <Button onClick={() => onNavigate("owner-view-stats")}>
              View Statistics
            </Button>
          </CardContent>
        </Card>
      </div>
    </div>
  );
};

export default OwnerDashboard;
