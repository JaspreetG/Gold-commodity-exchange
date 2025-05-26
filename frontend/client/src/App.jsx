import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './App.css'

function App() {
  const [count, setCount] = useState(0)

  return (
    <>
      <div className='bg-black h-screen flex flex-col items-center text-white'>
        <h1 className='text-5xl text-white'>Gold Commodity Exchange</h1>
      </div>
        
    </>
  )
}

export default App
