import * as React from 'react';
import { IconButton,Grid } from '@material-ui/core';
import Box from '@mui/material/Box';
import BookmarkBorderOutlinedIcon from '@mui/icons-material/BookmarkBorderOutlined';
import TaskIcon from '@mui/icons-material/Task';
import { useGlobalContext } from "../Context/DataContext";
import Table from "@mui/material/Table";
import TableBody from "@mui/material/TableBody";
import TableCell, { tableCellClasses } from "@mui/material/TableCell";
import TableContainer from "@mui/material/TableContainer";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
export default function BookmarkDashboard() {
  const {
    checkOutFiles,FilechangeEditState,bookmarkFiles
  } = useGlobalContext();

  return (
    <Box>
        <Box display="flex" sx={{width:'100%', height:25,flexGrow: 1 ,backgroundColor:'#2e7c67'}}>
          <TaskIcon color="secondary" sx={{marginRight:'5px'}}/>
        
          <span style={{marginTop:5}}>Bookmarked Documents</span>
          <Box sx={{display:'flex',justifyContent:'flex-end'}}><BookmarkBorderOutlinedIcon color="secondary" sx={{marginRight:'5px'}}/>
          
        </Box>
      
    </Box>
                <TableContainer
                  sx={{ maxHeight: 440 }}>
                <Table
                  stickyHeader
                  sx={{ minWidth: 700, height: "60%" }}
                  aria-label="sticky table"
                >
            {
            bookmarkFiles.map((row) => {
            const {
              fileId,
              fileName,
              createdOn
            } = row;
            return (
              <TableRow
                key={fileId}
                hover
                tabIndex={-1}
              >
              
                <TableCell
                  component="th"
                  id={fileId}
                  scope="row"
                  padding="none"
                  onClick={() => {
                    FilechangeEditState(row);
                  }}
                >
                  {fileName}
                </TableCell>
                <TableCell
                  align="right"
                  onClick={() => {
                    FilechangeEditState(row);
                  }}
                >
                  {createdOn}
                </TableCell>
              </TableRow>
            );
            })
            }
            </Table>
            </TableContainer>
</Box>
  );
}