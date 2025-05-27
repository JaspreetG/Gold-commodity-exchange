import React from 'react'
import { Link } from 'react-router-dom'
import { useState } from 'react'

const Navbar = () => {
    const [user, setUser] = useState(true);

    const handleLogout = () => {
        // Logic to handle user logout
        setUser(!user);
        // Optionally redirect to home or login page
    }

    const handleLogin = () => {
        // Logic to handle user login
        setUser(!user);
        // Optionally redirect to home or profile page
    }

  return (
    <div className='bg-gray-600 shadow-sm  flex   justify-between  p-2'>
        <Link to={'/'}><h1 className='text-2xl'>Gold Commodity Exchange</h1></Link>
        
        
        
        {!user && (
             <div className='flex gap-2  '>
                <Link to='/login' className='btn bg-gray-400' onClick={handleLogin}>Login</Link>
                <Link to='/signup' className='btn bg-gray-400'>Sign Up</Link>
            </div>
        )}

         {user && (
             <div className='flex justify-between  gap-1'>
               <Link to='/trade' className='btn bg-gray-400'>Trade</Link>
               <Link to='/profile' className='btn bg-gray-400'>Profile</Link>
                <Link to='/about' className='btn bg-gray-400'>About</Link>
                <button onClick={handleLogout} className='btn bg-gray-400'>Logout</button>
            </div>
        )}


       
        
    </div>
  )
}

export default Navbar