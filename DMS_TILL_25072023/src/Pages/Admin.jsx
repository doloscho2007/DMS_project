import React from 'react';
import { useGlobalContext } from '../Context/DataContext';
import DocumentHeader from '../Documents/DocumentHeader';
import MainBar from '../Documents/MainBar';
import Stack from '@mui/material/Stack';
import { IconButton, useTheme, Typography } from "@mui/material";
import { useContext } from 'react';
import { ColorModeContext, tokens } from '../theme';
import { useState } from "react";

import Treeview from '../Documents/Treeview';
import Split from 'react-split';
import '../index.css';
import MainHeader from '../HeaderComponents/MainHeader';
import TabHeader from '../HeaderComponents/TabHeader';

import { styled } from '@mui/material/styles';
import Box from '@mui/material/Box';
import Paper from '@mui/material/Paper';
import Grid from '@mui/material/Grid';
import AdminSideView from '../Administration/AdminSideView';

import AdminMainView from '../Administration/AdminMainView';

const Item = styled(Paper)(({ theme }) => ({
  backgroundColor: theme.palette.mode === 'dark' ? '#141b2d' : '#fff',
  ...theme.typography.body2,
  padding: theme.spacing(1),
  textAlign: 'center',
  color: theme.palette.text.secondary,
}));

const Admin = () => {
  const theme = useTheme();
  const colors = tokens(theme.palette.mode);
  const [activeTab, setActiveTab] = useState(1);

  return (
    <>
      <MainHeader />
      <TabHeader />
      <Box sx={{ width: '100%', height: '100vh', border: 'none' }} paddingTop={'1px'} marginBottom={'0px'}>
        <Stack spacing={1} backgroundcolor={'primary'} >
          {/* <Item sx={{padding:'0px'}} backgroundColor={'primary'}><DocumentHeader/></Item> */}
          <Item>
            <Box marginBottom={'0px'}>
              <Grid container spacing={1} backgroundcolor={'primary'} sx={{ height: { xs: 'auto', md: '100%' } }}>
                <Grid item xs={12} md={2} >
                  <Item sx={{ padding: '0px', textAlign: 'left', height: '100%' }}><AdminSideView setActiveTab={setActiveTab} /></Item>
                </Grid>
                <Grid item xs={12} md={9} backgroundcolor={'secondary'}>
                  <Item sx={{ padding: '0px', height: '100%', whiteSpace: 'nowrap', overflow: 'auto' }}>
                    <AdminMainView activeTab={activeTab} />
                  </Item>
                </Grid>
              </Grid>
            </Box>
          </Item>
        </Stack>
      </Box>
    </>
  );
}

export default Admin;
