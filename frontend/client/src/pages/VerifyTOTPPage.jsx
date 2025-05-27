import React from 'react'
import qrImage from "../assets/qr_image.svg";
import toast from 'react-hot-toast';
import { Navigate, useNavigate } from 'react-router-dom';

const VerifyTOTPPage = () => {
  const navigate=useNavigate();

  const handleSubmit = (e) => {
    e.preventDefault();
    // Handle OTP verification logic here
    console.log("OTP submitted");
    // toast.success("OTP verified successfully!");

    if(true){
      navigate('/trade');
      toast.success("OTP verified successfully!");
    }
  } 

  return (
    <div className='bg-base-300 flex flex-col items-center '>
      <h1 className='text-3xl'>Verify-TOTPPage</h1>

      <div className='flex flex-col items-center gap-1 bg-base-100 mt-5 p-5 rounded-lg shadow-lg'>
        <img src={qrImage} alt="Verify TOTP"  className=''/>
        <p className='text-2xl mt-0'>Scan using Google Authenticator</p>
      </div>
      
  
      <form className='flex flex-col gap-2 mt-5  justify-center items-center w-100' onSubmit={handleSubmit}>
        <label className='label text-2xl'> Enter the OTP received</label>
        <input type="number"  className='input w-[100%]'/>
        <button className='btn bg-primary w-[50%]'>Verify</button>
      </form>
      

      
    </div>
  )
}

export default VerifyTOTPPage