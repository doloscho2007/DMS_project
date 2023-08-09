import React from 'react';
import MainHeader from '../HeaderComponents/MainHeader';
import TabHeader from '../HeaderComponents/TabHeader';
import DashboardTab from '../DashBoard/DashboardTab';
import { Grid, useMediaQuery } from '@mui/material';
import { styled } from '@mui/material/styles';
import Stack from '@mui/material/Stack';
import Paper from '@mui/material/Paper';
import DocCheckoutAppBar1 from '../DashBoard/DocCheckoutAppBar1';
import BookmarkDashboard from '../DashBoard/BookmarkDashboard';
import {
  useWindowSize,
  useWindowWidth,
  useWindowHeight,
} from '@react-hook/window-size';

const Item = styled(Paper)(({ theme }) => ({
  backgroundColor: theme.palette.mode === 'dark' ? '#141b2d' : '#fff',
  ...theme.typography.body2,
  padding: theme.spacing(1),
  textAlign: 'center',
  color: theme.palette.text.secondary,
}));

const DashBoard = () => {
  const [width, height] = useWindowSize();
  const onlyWidth = useWindowWidth();
  const onlyHeight = useWindowHeight();
  const isSmallScreen = useMediaQuery((theme) => theme.breakpoints.down('sm'));

  return (
    <div style={{ height: '100%' }}>
      <MainHeader />
      <TabHeader />
      <DashboardTab />
      <Stack
        direction={isSmallScreen ? 'column' : 'row'}
        spacing={2}
        paddingLeft={2}
        sx={{ height: '100%', borderBottom: '0.05rem solid #666666' }}
      >
        <Grid
          container
          spacing={1}
          backgroundcolor={'primary'}
          height={isSmallScreen ? 'auto' : onlyHeight / 2}
        >
          <Grid item xs={isSmallScreen ? 12 : 6}>
            <Item
              sx={{
                padding: '0px',
                textAlign: 'left',
                height: isSmallScreen ? 'auto' : onlyHeight / 2,
                overflowY: 'scroll',
                maxHeight: isSmallScreen ? 'none' : '100vh',
              }}
            >
              <DocCheckoutAppBar1 />
            </Item>
          </Grid>
          {!isSmallScreen && (
            <Grid item xs={6}>
              {/* Additional content for larger screens */}
            </Grid>
          )}
        </Grid>
      </Stack>
      <Stack direction="row" spacing={2} paddingLeft={2} sx={{ borderBottom: '0.05rem solid #666666' }}>
        <Grid container spacing={1} backgroundcolor={'primary'} height={isSmallScreen ? 'auto' : onlyHeight / 2}>
          <Grid item xs={isSmallScreen ? 12 : 6}>
            <Item
              sx={{
                padding: '0px',
                textAlign: 'left',
                height: isSmallScreen ? 'auto' : onlyHeight / 2,
                overflowY: 'scroll',
                maxHeight: isSmallScreen ? 'none' : '100vh',
              }}
            >
              <BookmarkDashboard />
            </Item>
          </Grid>
          {!isSmallScreen && (
            <Grid item xs={6}>
              {/* Additional content for larger screens */}
            </Grid>
          )}
        </Grid>
      </Stack>
    </div>
  );
};

export default DashBoard;
