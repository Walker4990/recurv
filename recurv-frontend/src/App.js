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

          </Routes>
      </Router>

  );
}

export default App;
