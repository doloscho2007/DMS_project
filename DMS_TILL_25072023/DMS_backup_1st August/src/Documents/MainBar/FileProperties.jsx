import React, { useState } from 'react'
import { NavLink } from 'react-router-dom';
import FileVersions from './FileVersions';

import Properties from './Properties';
import Box from '@mui/material/Box';
import {Button,IconButton,useTheme,Typography}  from "@mui/material";
import PropTypes from 'prop-types';
import SwipeableViews from 'react-swipeable-views';
import { withStyles } from '@material-ui/core/styles';

import AppBar from '@mui/material/AppBar';
import Tabs from '@mui/material/Tabs';
import Tab from '@mui/material/Tab';

const styles = theme => ({
  swipeable: {
    width: '100%',
    height: "9rem", 
    [theme.breakpoints.down('sm')]: {
      height: "6rem",
    },
  },
});
const ResponsiveSwipeableViews = withStyles(styles)(SwipeableViews);

function TabPanel(props) {
  const { children, value, index, ...other } = props;

  return (
    <div
      role="tabpanel"
      hidden={value !== index}
      id={`full-width-tabpanel-${index}`}
      aria-labelledby={`full-width-tab-${index}`}
      {...other}
    >
      {value === index && (
        <Box sx={{ p: 3 }}>
          <Typography>{children}</Typography>
        </Box>
      )}
    </div>
  );
}

TabPanel.propTypes = {
  children: PropTypes.node,
  index: PropTypes.number.isRequired,
  value: PropTypes.number.isRequired,
};

function a11yProps(index) {
  return {
    id: `full-width-tab-${index}`,
    'aria-controls': `full-width-tabpanel-${index}`,
  };
}


const FileProperties = () => {

  
    const [tab , setTab] = useState(true)
    const theme = useTheme();
    const [value, setValue] = React.useState(0);
  
    const handleChange = (event, newValue) => {
      setValue(newValue);
    };
  
    const handleChangeIndex = (index) => {
      setValue(index);
    };
  
  
    const propTab = () => {
    // console.log("prop changed")
    setTab(true)
   }
   
   const versTab = () => {
    // console.log("vers changed")
    setTab(false)
     }

               
   return (
    //  <div style={{height: "19rem" , backgroundColor:'primary'}}>
    //   <div>
    //   <NavLink onClick={propTab} className='btn w-25 border rounded-0 text-decoration-none text-white text-uppercase' style={{backgroundColor:'#157fcc'}}>properties</NavLink>
    //   <NavLink onClick={versTab} className='btn w-25 border rounded-0 text-decoration-none text-white text-uppercase' style={{backgroundColor:'#157fcc'}}>versions</NavLink>
    //  </div>
      
    //   {
    //     tab ?
    //     <div>
    //       <Properties />
    //     </div>
    //     :
    //     <div>
    //       <FileVersions />
    //     </div>
    //   }

   
    //     </div>

    <Box component="span" sx={{width:'100%', height: "18rem" , backgroundColor:'primary'}}>
      {/* <AppBar position="static"> */}
        <Tabs
          value={value}
          onChange={handleChange}
          backgroundColor="grey"
          indicatorColor="secondary"
          textColor="inherit"
          variant="fullWidth"
          aria-label="full width tabs"
          sx={{width:'100%',height:'8rem'}}
          marginTop='0'
          class="sticky"
        >
          <Tab label="PROPERTIES" {...a11yProps(0)} />
          <Tab label="VERSIONS" {...a11yProps(1)} />
        </Tabs>
      {/* </AppBar> */}
      <ResponsiveSwipeableViews
        axis={theme.direction === 'rtl' ? 'x-reverse' : 'x'}
        index={value}
        onChangeIndex={handleChangeIndex}
        sx={{width:'100%'}}
      >
        <TabPanel value={value} index={0} dir={theme.direction} >
          <Properties />
        </TabPanel>
        <TabPanel value={value} index={1} dir={theme.direction}>
          <FileVersions/>
        </TabPanel>
      
      </ResponsiveSwipeableViews>
    </Box>
  )
}

export default FileProperties

