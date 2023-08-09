import React, { useEffect } from 'react';
import { DMSAPI } from './DMSAPI';

const RefreshCaller = () => {
  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await DMSAPI.refreshToken();
        // console.log(response.status)
        const data = await response.json();
        // console.log(data.token);
        var USER = JSON.parse(localStorage.getItem('user'));
        // console.log(USER.token)
        USER.token=data.token;
        localStorage.setItem('user', JSON.stringify(USER));
        console.log("refresh call")
      } catch (error) {
        console.log('Error:', error);
      }
    };
    fetchData();

  
    const intervalId = setInterval(fetchData, 300000);

  
    return () => clearInterval(intervalId);
  }, []);

  return <></>; 
};

export default RefreshCaller;