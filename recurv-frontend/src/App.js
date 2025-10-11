import MainPage from "./pages/MainPage";
import {BrowserRouter as Router, Routes, Route} from "react-router-dom";
import Login from "./pages/Login";
import Register from "./pages/Register";
import BillingPage from "./pages/BillingPage";
import BillingDetailPage from "./pages/BillingDetailPage";
import SupportMain from "./pages/SupportMain";
import SupportFaq from "./subPages/SupportFaq";
import SupportForm from "./subPages/SupportForm";
import SupportAi from "./subPages/SupportAi";
import SupportGuide from "./subPages/SupportGuide";
import PaymentSuccess from "./subPages/PaymentSuccess";
import PaymentFail from "./subPages/PaymentFail";
import MySubscription from "./pages/MySubscription";
import Consulting from "./pages/Consulting";
import AdminHome from "./admin/AdminHome";
import ProtectedAdminRoute from "./admin/ProtectedAdminRoute";
import SubscriptionManage from "./admin/SubscriptionManage";
import BillingManage from "./admin/BillingManage";
import PartnerManage from "./admin/PartnerManage";
import PartnerDetail from "./admin/PartnerDetail";
import AdminSupportChat from "./admin/AdminSupportChat";
import AdminSupportList from "./admin/AdminSupportList";
import AdminNotificationLog from "./admin/AdminNotificationLog";
function App() {
  return (
      <Router>

          <Routes>
              <Route path="/" element={<MainPage />} />
              <Route path="/login" element={<Login />} />
              <Route path="/register" element={<Register />}/>
              <Route path="/billing" element={<BillingPage />}/>
              <Route path="/billing/:planId" element={<BillingDetailPage />}/>
              <Route path="/support/faq" element={<SupportFaq />}/>
              <Route path="/supportMain" element={<SupportMain />}/>
              <Route path="/support/form" element={<SupportForm />} />
              <Route path="/support/ai" element={<SupportAi />} />
              <Route path="/support/guide" element={<SupportGuide />}/>
              <Route path="/payment/success" element={<PaymentSuccess />} />
              <Route path="/payment/fail" element={<PaymentFail />} />
              <Route path="/subscriptions/me" element={<MySubscription />} />
              <Route path="/subscriptions/:id" element={<MySubscription />} />
              <Route path="/consulting" element={<Consulting/>} />

              <Route path="/adminHome" element={<ProtectedAdminRoute><AdminHome /></ProtectedAdminRoute>} />
              <Route path="/admin/subscription" element={<SubscriptionManage />} />
              <Route path="/admin/billing" element={<BillingManage />} />
              <Route path="/admin/partner" element={<PartnerManage />} />
              <Route path="/admin/partner/:userId" element={<PartnerDetail />} />
              <Route path="/admin/support-chat" element={<AdminSupportChat />} />
              <Route path="/admin/support/list" element={<AdminSupportList />} />
              <Route path="/admin/notification" element={<AdminNotificationLog />} />
          </Routes>
      </Router>

  );
}

export default App;
