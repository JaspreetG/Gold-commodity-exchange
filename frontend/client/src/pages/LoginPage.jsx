import React from 'react'
import { Navigate, useNavigate } from 'react-router-dom';

const LoginPage = () => {

    const navigate=useNavigate();

    const handleSubmit = (e) => {
        e.preventDefault();
        const phoneNumber = e.target[0].value;
        console.log("Phone Number:", phoneNumber);

        if(true){
            navigate('/verifyTOTPPage');
        }
        
    }   

  return (
    <div className='bg-base-300 h-screen flex flex-col gap-15 items-center' >
        <h1 className='text-3xl pt-2'>Login Page</h1>

        <form onSubmit={handleSubmit}>
            <div className='flex flex-col gap-5 '>
                <label htmlFor="phn" className='label'>Enter your Phone Number</label>
                <input type="number" id="phn" className='input' />
                <button type="submit" className='btn bg-primary'>Submit</button>
            </div>
        </form>




    </div>
  )
}

export default LoginPage