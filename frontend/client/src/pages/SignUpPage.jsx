import React from 'react'
import { Navigate, useNavigate } from 'react-router-dom';


const SignUpPage = () => {

     const navigate=useNavigate();



    const handleSubmit = (e) => {
        e.preventDefault();
        const phoneNumber = e.target[0].value;
        console.log("Phone Number:", phoneNumber);
        // Here you can add the logic to send the phone number to your backend

        if(true){
            navigate('/verifyTOTPPage');
        }
    }
  return (

    <div className='bg-base-300 h-screen flex flex-col gap-15 items-center' >
        <h1 className='text-3xl pt-2'>SignUp Page</h1>

        <form onSubmit={handleSubmit}>
            <div className='flex flex-col gap-2'>
                <div >
                    <label htmlFor="name">Enter your name</label>
                    <input type="text" id="name" className='input' />
                </div>

                <div>
                    <label htmlFor="email">Enter your Email</label>
                    <input type="text" id="email" className='input' />
                </div>

                <div>
                    <label htmlFor="phn" className='label'>Enter your Phone Number</label>
                    <input type="number" id="phn" className='input' />
                </div>
            </div>
                <button type="submit" className='btn bg-primary mt-3'>Submit</button>
        </form>
    </div>
  )
}

export default SignUpPage