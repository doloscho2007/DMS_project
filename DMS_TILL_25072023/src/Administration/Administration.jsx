import * as React from "react";
import Table from "@mui/material/Table";
import TableBody from "@mui/material/TableBody";
import TableCell from "@mui/material/TableCell";
import TableContainer from "@mui/material/TableContainer";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import TablePagination from "@mui/material/TablePagination";
import Paper from "@mui/material/Paper";

import { DMSAPI } from '../Service/DMSAPI';
import {useEffect, useRef,useState } from 'react';
import MainHeader from '../HeaderComponents/MainHeader'
import TabHeaderModify from '../HeaderComponents/TabHeader'
import { makeStyles } from '@material-ui/core/styles';

const useStyles = makeStyles({
  tableRowOdd: {
    backgroundColor: '#green',
  },
  tableRowEven: {
    backgroundColor: '#2e7c67',
  },
});
const Administration = () => {
  const [events, setEvents] = useState([]);
   const [username, setUsername] = useState('');
   const [fromDate, setFromDate] = useState('');
   const [toDate, setToDate] = useState('');
  const dateInputRef = useRef(null);
const classes = useStyles();

const handleUsername = (e) => {
  setUsername(e.target.value);
    };
const handleFromDate = (e) => {
  setFromDate(e.target.value);
    };
  const handleToDate = (e) => {
    setToDate(e.target.value);
      };

    const handleSearch = async() => {
    const response = await DMSAPI.searchEvents(username,fromDate,toDate);  
    const data = await response.json();    
    setEvents(data);
    }
  const loadEventLog = async() => {
    const response = await DMSAPI.extractEvents();  
    const data = await response.json();    
    setEvents(data);
    }
    const dataFetchedRef = useRef(false);
    useEffect(() => {
      if (dataFetchedRef.current) return;
          dataFetchedRef.current = true;      
          loadEventLog();
       });

    const [pg, setpg] = useState(0);
    const [rpg, setrpg] = useState(5);
    
  
    function handleChangePage(event, newpage) {
        setpg(newpage);
    }
  
    function handleChangeRowsPerPage(event) {
        setrpg(parseInt(event.target.value, 10));
        setpg(0);
    }
  
    return (
     <>
<div style={{paddingTop:'20px'}}>
    <div style={{display:"flex",justifyContent:"flex-start"}}> 
      <label >Username : </label>
      <input className='ms-4' name="username" onChange={handleUsername}  value={username}/> 
    </div>
    <div>&nbsp;</div>
    <div style={{display:"flex",justifyContent:"flex-start"}}>
      <label>From Date :</label>
      <input name='fromDate' type="date" onChange={handleFromDate} ref={dateInputRef}/>
    </div>
    <div>&nbsp;</div>
    <div style={{display:"flex",justifyContent:"flex-start"}}>
      <label>To Date :</label>
      <input name='toDate' type="date" onChange={handleToDate} ref={dateInputRef}/>

    </div>
                   

          <button  className='btn btn-primary mt-4 me-4 ' style={{float:'right'}} onClick={handleSearch} >Search</button> <br />
</div>

       <Paper>
            <TableContainer component={Paper}>
                <Table sx={{ minWidth: 650 }} 
                    aria-label="simple table">
                    <TableHead>
                        <TableRow>
                            <TableCell>Event</TableCell>
                            <TableCell>Event Desc</TableCell>
                            <TableCell>Event Date</TableCell>
                            <TableCell>Username</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                     
                        {events.slice(pg * rpg, pg *
                            rpg + rpg).map((event,index) => (
                            <TableRow
                                className={index % 2 === 0 ? classes.tableRowEven : classes.tableRowOdd} 
                                hover
                                key={event.eventId}
                                sx={{ "&:last-child td, &:last-child th": { border: 0 } }}
                            >
                                <TableCell component="th" 
                                    scope="row">
                                    {event.eventId}
                                </TableCell>
                                <TableCell >
                                    {event.eventDesc}</TableCell>
                                    <TableCell >
                                    {event.eventDate}</TableCell>
                                    <TableCell >
                                    {event.userName}</TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>  
      

      <TablePagination
                rowsPerPageOptions={[5, 10]}
                component="div"
                count={events.length}
                rowsPerPage={rpg}
                page={pg}
                onPageChange={handleChangePage}
                onRowsPerPageChange={handleChangeRowsPerPage}
            />
            </Paper>
      </>
        
    );
}

export default Administration

  
