import React from 'react'
import MainbarHeader from './DocumentHeader/MainbarHeader'
import TreeViewHeader from './DocumentHeader/TreeViewHeader'
import Grid from '@mui/material/Grid';
import Box from '@mui/material/Box';
import Paper from '@mui/material/Paper';
import RefreshOutlinedIcon from '@mui/icons-material/RefreshOutlined';
import { IconButton } from '@material-ui/core';
import { styled } from '@mui/material/styles';
import Tooltip from '@mui/material/Tooltip';

const Item = styled(Paper)(({ theme }) => ({
  backgroundColor: theme.palette.mode === 'dark' ? '#1A2027' : '#fff',
  ...theme.typography.body2,
  padding: theme.spacing(1),
  textAlign: 'center',
  color: theme.palette.text.secondary,
}));



const DocumentHeader = ({handleRefresh}) => {
  return (
   
    <Grid container spacing={1} elevation={0} border-style={'none'}>
      <Grid item xs={2} >
        <Item sx={{paddingTop:'0', margin:'0'}}><TreeViewHeader/></Item>
      </Grid>
      <Grid item xs={10}>
        <Item sx={{padding:'0',margin:'0'}} >
        <MainbarHeader handleRefresh={handleRefresh} />
        </Item>
      </Grid>
    
     
     
    </Grid>

  )
}

export default DocumentHeader