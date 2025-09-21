import React from "react";
import AdminHeader from "../components/AdminHeader";
import UserHeader from "../components/UserHeader";
import Home from "../components/Home";

export default function MainPage() {
const [isAdmin, setIsAdmin] = React.useState(false);
return (
    <div style={{diplay: 'flex', height:'100vh'}}>
            {isAdmin ? <AdminHeader /> : <UserHeader />}
        <main style={{flex:'1', padding: '40px'}}>
        <Home />
        </main>

    </div>
)
}