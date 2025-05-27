import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './App.css'
import Navbar from './components/Navbar'
import HomePage from './pages/HomePage'
import LoginPage from './pages/LoginPage'
import SignUpPage from './pages/SignUpPage'
import VerifyTOTPPage from './pages/VerifyTOTPPage'
import TradingPage from './pages/TradingPage'
import ProfilePage from './pages/ProfilePage'
import AboutPage from './pages/AboutPage'
import AddMoneyPage from './pages/AddMoneyPage'
import WithdrawMoneyPage from './pages/WithdrawMoneyPage'

import { Routes, Route,Navigate } from 'react-router-dom'

function App() {
  const [count, setCount] = useState(0)

  return (
    <>
      <Navbar/>

      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/signUp" element={<SignUpPage />} />
        <Route path="/verifyTOTPPage" element={<VerifyTOTPPage/>}/>
        <Route path="/trade" element={<TradingPage />} />
        <Route path="/profile" element={<ProfilePage />} />
        <Route path="/about" element={<AboutPage />} />
        <Route path="/profile-addMoney" element={<AddMoneyPage />} />
        <Route path="/profile-withdrawMoney" element={<WithdrawMoneyPage />} />
      </Routes>
        
    </>
  )
}

export default App
