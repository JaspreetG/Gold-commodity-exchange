import React from 'react'
import { useState } from 'react';
import { Link } from 'react-router-dom'


const ProfilePage = () => {
    const [user, setUser] = useState("Dummy User");
    const [balance, setBalance] = useState(1000);
    const [goldAmount, setGoldAmount] = useState(0);

  return (
    <div>
        <div className='bg-base-300 h-screen flex flex-col items-center '>
            <h1 className='text-3xl font-bold mb-4'>Profile Page</h1>
            <div className='bg-white p-6 rounded-lg shadow-md w-80'>
                <h2 className='text-xl font-semibold mb-2'>Hello {user}</h2>
                <p className='mb-2'>Balance: ${balance}</p>
                <p>Gold Amount: {goldAmount} grams</p>
            </div>
             <div>
                <Link to='/profile-addMoney' className='btn bg-green-400 w-[50%]'>Add Money</Link>
                <Link to='/profile-withdrawMoney' className='btn bg-red-400 w-[50%]'>Withdraw Money</Link>
            </div>

            <div className='mt-10 bg-gray-300 p-6 rounded-lg shadow-lg w-180'>
                <h2 className='text-xl font-semibold mb-2'>Trade History</h2>
                <ul className='list-disc pl-5'>
                    <li>Bought 10 grams of gold on 2023-10-01</li>
                    <li>Sold 5 grams of gold on 2023-10-05</li>
                    <li>Bought 15 grams of gold on 2023-10-10</li>
                    <li>Bought 15 grams of gold on 2023-10-10</li>
                    <li>Bought 15 grams of gold on 2023-10-10</li>
                    <li>Bought 15 grams of gold on 2023-10-10</li>
                </ul>

            </div>
        </div>
       

    </div>
  )
}

export default ProfilePage