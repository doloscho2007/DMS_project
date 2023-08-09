import { Button } from '@mui/material';
import React, { useEffect } from 'react'
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
// import { Link } from 'react-router-dom';
import '../App.css'
import { useAuth } from '../utils/Auth';
import Box from '@mui/material/Box';
import { DMSAPI } from '../Service/DMSAPI';


// const api_url="http://localhost:8080/validateLogin"

const Login = () => {

  const [pin1 , setPin1] = useState("")
  const [pin2 , setPin2] = useState("")
  const [pin3 , setPin3] = useState("")
  const [pin4 , setPin4] = useState("")
  const [errorMessage , setErrorMessage] = useState(false)
  const [isLoggedIn, setIsLoggedIn] = useState(false);

  const auth = useAuth()
  const navigate = useNavigate()

  // useEffect(() => {
  //   if (isLoggedIn) {
  //     window.location.reload();
      
  //   }
  // }, [isLoggedIn]);
  const pinNum1 = (e) => {
    setPin1(e.target.value);
    if (
      e.target.value.length >= parseInt(e.target.getAttribute("maxlength"), 10)
    ) {
      document.getElementById("pin2").focus();
    }
  };

  const pinNum2 = (e) => {
    setPin2(e.target.value);
    if (
      e.target.value.length >= parseInt(e.target.getAttribute("maxlength"), 10)
    ) {
      document.getElementById("pin3").focus();
    }
  };

  const pinNum3 = (e) => {
    setPin3(e.target.value);
    if (
      e.target.value.length >= parseInt(e.target.getAttribute("maxlength"), 10)
    ) {
      document.getElementById("pin4").focus();
    }
  };

  const pinNum4 = (e) => {
    setPin4(e.target.value);
    if(e.target.value.length >= parseInt(e.target.getAttribute("maxlength"), 10)){
      document.getElementById("submit-btn").focus();
    }
  };

  const Clear = () => {
        setPin1('');
        setPin2('');
        setPin3('');
        setPin4('');
        setErrorMessage(false);
         document.getElementById("pin1").focus();
  }
  const Results = () => (
    <div id="results" className="search-results">
      Please enter a valid 4 digit PIN
    </div>
  )
   
  const validateLogin =async (e) => {
    e.preventDefault();
    // console.log("this is login validation");
      if (pin1 !== "" && pin2 !== "" && pin3 !== "" && pin4 !== "") {
      setErrorMessage(false);
       
      try {
        let res = await DMSAPI.validateLogin(pin1 + pin2 + pin3 + pin4);
       let resJson = await res.json();
        if (res.status === 200) {
          console.log("successful login")
          setIsLoggedIn(true);
          localStorage.setItem("user", JSON.stringify(resJson));
          auth.login(JSON.stringify(resJson));
          navigate("/Dashboard");
          window.location.reload();
        } else {
          console.log("Invalid Credentials");
          setErrorMessage(true)
        }
      } catch (err) {
        console.log(err);
        setErrorMessage(true)
      }
   } 
   else {
      setErrorMessage(true);
    }
    
      // localStorage.setItem("selectedFileId", pinCode);

  }

 useEffect(() => {
   document.getElementById("pin1").focus();
 } ,[]) 
   return (
    <Box className="wrapper fadeInDown">
      <Box id="formContent">
      <div className="fadeIn first" style={{borderBottom: "1px solid #dce8f1" , padding: "10px"}}>
        <div className="text-white h5" >ARITS DOCUMENT MANAGEMENT</div>
      </div>
	    <br/><br/>	
    <form>
       <input type="password" className="fadeIn second" id="pin1"  maxLength="1" onChange={pinNum1}  value={pin1} />
       <input type="password" className="fadeIn second" id="pin2"  maxLength="1" onChange={pinNum2}  value={pin2} />
       <input type="password" className="fadeIn second" id="pin3"  maxLength="1" onChange={pinNum3}  value={pin3} />
       <input type="password" className="fadeIn second" id="pin4"  maxLength="1" onChange={pinNum4}  value={pin4} />
       <br/>
     <Button  className={`${errorMessage ?'fadeIn fourth btn btn-lg btn-primary mt-4 me-5 mb-3' :'fadeIn fourth btn btn-lg btn-primary mt-4 me-2 mb-3'}`} id='submit-btn' type='button' sx={{backgroundColor:'#f7f7f7',marginRight:'10px'}} onClick={validateLogin}>LOGIN</Button>
     {
      errorMessage ?
      <Button className="btn btn btn-lg btn-danger mt-4 mb-3" sx={{backgroundColor:'#d9534f'}} id='Clear-btn' type='button' onClick={Clear}>CLEAR</Button>
      :
      ''
     }
     <br />
      </form>
      <div id="formFooter">
      { errorMessage ? <Results /> : null }
    </div>
  </Box>
</Box>
  )
}

export default Login


