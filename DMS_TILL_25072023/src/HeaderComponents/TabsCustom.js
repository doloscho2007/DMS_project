import { makeStyles,withStyles } from '@material-ui/core/styles'
import Tabs from '@material-ui/core/Tabs'
import Tab from '@mui/material/Tab'
import * as React from 'react';
import {Box,IconButton,useTheme,Typography}  from "@mui/material";

const useStyles = makeStyles((theme) => ({
  customOne: {
     padding: '3rem 15rem',
     flexGrow: 1,
     backgroundColor: theme.palette.background.paper,
     fontFamily: 'Open Sans',
  },
  customTwo: {
     padding: '0rem',
     color: '#484848',
     backgroundColor: 'white',
     fontFamily: 'Open Sans',
     fontSize: '1rem',
 },
}))

const TabStyle = withStyles((theme) => ({
  root: {
     padding: '1rem 0',
     textTransform: 'none',
     fontWeight: theme.typography.fontWeightRegular,
     fontSize: '1.2rem',
     fontFamily: [
         '-apple-system',
         'BlinkMacSystemFont',
         'Roboto',
     ].join(','),
     '&:hover': {
        backgroundColor: '#004C9B',
        color: 'white',
        opacity: 1,
     },
    '&$selected': {
        backgroundColor: '#004C9B',
        color: 'white',
        fontWeight: theme.typography.fontWeightMedium,
    },
},
tab: {
    padding: '0.5rem',
    fontFamily: 'Open Sans',
    fontSize: '2rem',
    backgroundColor: 'grey',
    color: 'black',
    '&:hover': {
        backgroundColor: 'red',
        color: 'white',
        opacity: 1,
    },
},
selected: {},
}))((props) => <Tab {...props} />)

export default function TabsCustom({ activeTabIndex, onChange, values }) {
  const classes = useStyles()
  const [value, setValue] = React.useState(0)

  const handleChange = (event, newValue) => {
     setValue(newValue)
}


  return (
      <div className={classes.customOne}>
          <Tabs
              className={classes.customTwo}
              variant="fullWidth"
              value={activeTabIndex}
              onChange={onChange}
              aria-label="tabs"
          >
              <TabStyle
                  value="updateDate"
               
                 
              />

          </Tabs>
  </div>
 )
}