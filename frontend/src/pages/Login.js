import axios from 'axios';
import React, { useState , useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Link } from 'react-router-dom';
function Login() {
    // State to store form data
    const [responseError,setResponseError] = useState()
    const navigate = useNavigate()
    const [form, setForm] = useState({
        name: '',
        emailAddress: '',
        password: ''
    });
    const [error, setError] = useState({
        username: '',
        email: '',
        password: ''
    });

    // Handle form data change
    const handleChange = (e) => {
        const { name, value } = e.target;
        setForm({
            ...form,
            [name]: value
        });
    };
    useEffect(() => {
      if (responseError)
        alert(responseError)
    }, [responseError])
    
    // Handle form submission
    const submit = async (e) => {
        e.preventDefault();
        let isValid = true;
        
        // Validate password length
        if (form.password.length < 8 || form.password.length > 25) {
            setError((prevError) => ({
                ...prevError,
                password: 'Password length must be between 8 and 25 characters.'
            }));
            isValid = false;
        } else {
            setError((prevError) => ({
                ...prevError,
                password: ''
            }));
        }
        if (form.emailAddress.length<10)
        {
            setError((prevError)=>({
                ...prevError,
                email: 'Email length must not be less than 10 characters.'
            }))
            isValid = false;
        }
        

        // If all validations pass, log form data
        if (isValid) {
            axios.post('http://localhost:8080/register/login', form)
            .then(response => {
                try {
                    let token = response.headers.authorization
                    localStorage.setItem('token',token)
                    navigate('/')
                } catch (error) {
                    
                }

            })
            .catch(error=>{
                console.log(error.status)
                alert('invalid credentials')
            })
            
        }
    };

    return (
        <div className='h-screen overflow-clip bg-[#003C43] flex-col flex items-center justify-center space-y-10 text-white'>
            <div className='font-bold text-3xl'>
                Log In
            </div>
            <div className='bg-[#135D66] rounded-3xl shadow-2xl shadow-black w-[30%] h-auto px-10 py-10 font-semibold'>
                <form onSubmit={submit} className='space-y-4'>
                    <div>
                        <label htmlFor="email">Email</label>
                        <br />
                        <input
                            type="email"
                            name="emailAddress"
                            value={form.emailAddress}
                            onChange={handleChange}
                            className='rounded-2xl bg-[#239cac] shadow-2xl shadow-black px-2 py-2 w-full'
                            required
                        />
                        {error.email && <p className="text-red-500 text-sm mt-2">*{error.email}</p>}
                    </div>
                    <div>
                        <label htmlFor="password">Password</label>
                        <br />
                        <input
                            type="password"
                            name="password"
                            value={form.password}
                            onChange={handleChange}
                            className='rounded-2xl bg-[#239cac] shadow-2xl shadow-black px-2 py-2 w-full'
                            required
                        />
                        {error.password && <p className="text-red-500 text-sm mt-2">*{error.password}</p>} {/* Display error message */}
                    </div>
                    <div className='space-y-4 flex flex-col justify-center'>
                        <input
                            type="submit"
                            value="Log In"
                            className='rounded-2xl bg-[#2335ac] px-2 py-2 active:scale-95 transition-transform duration-150'
                        />
                        <div className='text-sm flex justify-center space-x-4'>
                        <span>Dont have an account ? </span>
                        <span><Link to={'/register'} className='underline'>
                        create an account
                        </Link></span>
                        </div>
                    </div>

                    
                </form>
            </div>
        </div>
    );
}

export default Login;
