import React from 'react';
import { useGlobalContext } from '../Context/DataContext';
import DocumentHeader from '../Documents/DocumentHeader';
import MainBar from '../Documents/MainBar';
import Stack from '@mui/material/Stack';
import Treeview from '../Documents/Treeview';
import Split from 'react-split';
import '../index.css';
import { styled } from '@mui/material/styles';
import Box from '@mui/material/Box';
import Paper from '@mui/material/Paper';
import { Grid } from '@material-ui/core';
import MainHeader from '../HeaderComponents/MainHeader';
import TabHeader from '../HeaderComponents/TabHeader';
import PdfViewer from '../Documents/PdfViewer';

const Item = styled(Paper)(({ theme }) => ({
  backgroundColor: theme.palette.mode === 'dark' ? '#141b2d' : '#fff',
  ...theme.typography.body2,
  padding: theme.spacing(1),
  textAlign: 'center',
  color: theme.palette.text.secondary,
}));

const Documents = () => {
  const { pdfToggleActive ,pdfRefreshKey ,treeRefreshKey } = useGlobalContext();

  return (
    <div style={{overflow:"hidden"}}>
      <MainHeader />
      <TabHeader />
      <Box
        sx={{
          display: 'flex',   
          flexDirection: 'column', 
          alignItems: 'stretch',   
          height: '100%',   
          width: '100%',
          overflow:"hidden"
        }}
        paddingTop={'1px'}
        marginBottom={'0px'}
      >
        <Stack spacing={1} backgroundcolor={'primary'} border-style={'none'}>
          <Item sx={{ padding: '0px' }} backgroundcolor={'primary'} border-style={'none'}>
            <DocumentHeader />
          </Item>
          <Box marginBottom={'0px'}>
            <Grid container spacing={1} backgroundcolor={'primary'} height={'100%'} sx={{ height: { xs: 'auto', md: '40vw' } }}>
              <Grid item xs={12} md={2}>
                <Item sx={{ padding: '0px', textAlign: 'left', overflowY: "scroll" }}>
                  <Treeview key={treeRefreshKey} />
                </Item>
              </Grid>
              <Grid item xs={12} md={10}>
                {
                  pdfToggleActive ?
                    <Item sx={{ border: 0, shadow: 'none', height: '100%' }}>
                      <Split className="split" minSize={[300, 400]} gutterSize={8} sizes={[110, 65]}>
                        <Box><MainBar /></Box>
                        <Box><PdfViewer key={pdfRefreshKey} /></Box>
                      </Split>
                    </Item>
                    :
                    <Item sx={{ padding: '0px', textAlign: 'left', height: '100%', border: 0, shadow: 'none', ml: '0px', mb: '0px' }}>
                      <MainBar />
                    </Item>
                }
              </Grid>
            </Grid>
          </Box>
        </Stack>
      </Box>
    </div>
  );
};

export default Documents;
