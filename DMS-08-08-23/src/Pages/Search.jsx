import React from 'react'
import { useGlobalContext } from '../Context/DataContext'
import MainHeader from '../HeaderComponents/MainHeader'
import TabHeader from '../HeaderComponents/TabHeader'
import DocumentHeader from '../Documents/DocumentHeader'
import MainBar from '../Documents/MainBar'
import Treeview from '../Documents/Treeview'
import SearchView from '../Search/SearchView'
// import PdfViewer  from '../Documents/PdfViewer'
import SearchPdfViewer from '../Search/SearchPdfViewer'
import Split from 'react-split'
import '../index.css'
import { styled } from '@mui/material/styles';
import Box from '@mui/material/Box';
import Paper from '@mui/material/Paper';
import {Grid, Divider} from '@material-ui/core'
import SearchMainBar from '../Search/SearchMainBar'

const Item = styled(Paper)(({ theme }) => ({
  backgroundColor: theme.palette.mode === 'dark' ? '#141b2d' : '#fff',
  ...theme.typography.body2,
  padding: theme.spacing(1),
  textAlign: 'center',
  color: theme.palette.text.secondary,
}));



const Search = () => {

  const {pdfToggleActive} = useGlobalContext()
  return (<>
      <MainHeader/>
     <TabHeader/>
      <Item sx={{padding:'0px'}} backgroundcolor={'primary'} border-style={'none'}><DocumentHeader/></Item>
     <Box  marginBottom={'0px'}>
        <Grid container spacing={1} backgroundcolor={'primary'} height={'38.4rem'}>
          <Grid item xs={2}>
            <Item  sx={{padding:'0px',textAlign:'left',height:'100%',overflowY: "scroll",maxHeight: "100vh"}}><SearchView/></Item>
            {/* border: '1px solid #fff' */}
            </Grid>
            <Grid item xs={10}>
                
               {
                pdfToggleActive ?  
                <Item sx={{border: 0,shadow:'none',height:'100%'}}>
                  <Split className="split" minSize={[300,400]}  gutterSize={8} sizes={[110, 65]} >    
                     <Box><SearchMainBar /></Box>
                     {/* <Box><Sample/></Box> */}
                     <Box><SearchPdfViewer /></Box>
                  </Split> 
                  </Item>
                  :
                
                      // <Grid item xs={12} padding='0px'>
                        <Item  sx={{padding:'0px',textAlign:'left',height:'38.4rem',border: 0,shadow:'none',ml:'0px',mb:'0px'}}><SearchMainBar/></Item>
            
                      // </Grid>
                   
              }
                
            </Grid>


    </Grid>
    </Box>
    </>
  )
}

export default Search