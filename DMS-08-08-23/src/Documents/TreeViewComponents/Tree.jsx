import * as React from "react";
import TreeView from "@mui/lab/TreeView";
import { useEffect, useRef } from "react";
import TreeItem from "@mui/lab/TreeItem";
import {Box,IconButton,useTheme,Typography}  from "@mui/material";
import CloseIcon from '@mui/icons-material/Close';
import PropTypes from "prop-types";
import Button from '@mui/material/Button';
import {
  FaFolderOpen,
  FaFolderMinus,
  FaFolderPlus,
} from "react-icons/fa";
import { DMSAPI } from "../../Service/DMSAPI";
import { useGlobalContext } from "../../Context/DataContext";
import { ContextMenu } from "../../styles/styles";
import { useState } from "react";
import DialogContent from '@mui/material/DialogContent';
import DialogActions from '@mui/material/DialogActions';
import TextField from '@mui/material/TextField';
import { styled } from '@mui/material/styles';
import Dialog from '@mui/material/Dialog';
import DialogTitle from '@mui/material/DialogTitle';
import Snackbar from "@material-ui/core/Snackbar";
import MuiAlert from "@mui/material/Alert";

function Alert(props) {
  return <MuiAlert elevation={6} variant="filled" {...props} />;
}

const BootstrapDialog = styled(Dialog)(({ theme }) => ({
  '& .MuiDialogContent-root': {
    padding: theme.spacing(2),
  },
  '& .MuiDialogActions-root': {
    padding: theme.spacing(1),
  },
}));

function BootstrapDialogTitle(props) {
  const { children, onClose, ...other } = props;

  return (
    <DialogTitle sx={{ m: 0, p: 2 }} {...other}>
      {children}
      {onClose ? (
        <IconButton
          aria-label="close"
          onClick={onClose}
          style={{
            position: 'absolute',
            right: 8,
            top: 8,
            color: (theme) => theme.palette.grey[500],
          }}
        >
          <CloseIcon />
        </IconButton>
      ) : null}
    </DialogTitle>
  );
}

BootstrapDialogTitle.propTypes = {
  children: PropTypes.node,
  onClose: PropTypes.func.isRequired,
};

export const Tree = () => {
  const { getNodeId, nodes, setNodes, handleNodeRefresh } = useGlobalContext();
  const [clicked, setClicked] = useState(false);
  const [folderName, setFolderName] = useState(false);
  const [editOpen, setEditOpen] = React.useState(false);
  const [open1, setOpen] = useState(false);
  const [snackbarMessage, setSnackbarMessage] = useState("");
  const [snackbarSeverity, setSnackbarSeverity] = useState("success");
  const [points, setPoints] = useState({
    x: 0,
    y: 0,
  });
  useEffect(() => {
    const handleClick = () => setClicked(false);
    window.addEventListener("click", handleClick);
    return () => {
      window.removeEventListener("click", handleClick);
    };
  }, []);

  const loadTreeComp1 = async () => {
    const response = await DMSAPI.getNodes();
    const data = await response.json();
    setNodes(data);
    // console.log(data);
  };
  const dataFetchedRef = useRef(false);

  useEffect(() => {
    if (dataFetchedRef.current) return;
    dataFetchedRef.current = true;
    loadTreeComp1();
  });

  const handleContextMenu = (nodeId) => {
    // Handle the right-click event for the specific folder
    console.log("Right-clicked folder:", nodeId);
  };

  const getTreeItemsFromData = (data) => {
    return data.map((treeItemData) => {
      const { subNodes, nodeId, text } = treeItemData;
      // console.log(subNodes);
      // console.log(nodeId);
      // console.log(text);

      let children = undefined;
      if (subNodes.length === 0) {
        children = getTreeItemsFromData(subNodes);
      } else if (subNodes && subNodes.length > 0) {
        children = getTreeItemsFromData(subNodes);
      }
      return (
        <TreeItem
          key={nodeId}
          nodeId={nodeId}
          label={text}
          onContextMenu={handleContextMenu}
        >
          {children}
        </TreeItem>
      );
    });
  };

  const nodeId = localStorage.getItem("node");

  const performPaste = async () => {
    const action = localStorage.getItem("perfCutAction");
    const fileId = localStorage.getItem("perfCutCopyID");

    console.log("fileId" + fileId + "nodeId" + nodeId + "action" + action);
    const resp = await DMSAPI.fileOperation(fileId, nodeId, action);
    const data = await resp.json();
    console.log(data);
  };

  const editFolName = async () => {
      const resp = await DMSAPI.updateFolder(nodeId,folderName);
      const data = await resp.json();
      console.log(data.message)
      if(data.message === "Error Occured : Defult Node can't be edited"){
        setSnackbarMessage("Defult Node can't be edited");
        setSnackbarSeverity("warning");
        setOpen(true);
        // alert("Defult Node can't be edited"); 
      }else if(resp.status === 200){
        setSnackbarMessage("Folder Edited Successfully");
        setSnackbarSeverity("success");
        setOpen(true);
        setEditOpen(false);
        // alert("Folder Edited Successfully");
      }else{
        setSnackbarMessage("Folder can't be Edited");
        setSnackbarSeverity("error");
        setOpen(true);
        
        // alert("Folder can't be Edited ");
    }

  };

  const deleteFolder = async () => {
    const resp = await DMSAPI.deleteFolder(nodeId);
    const data = await resp.json();
    console.log(data);
    if (resp.status === 200) {
      handleNodeRefresh();
    }
  };

  const handleClickOpen = () => {
    setEditOpen(true);
  };
  const handleClose = () => {
    setEditOpen(false);
  };

  const handleSnackClose = (event, reason) => {
    if (reason === "clickaway") {
      return;
    }
    setOpen(false);
  };

  const handleFolderName = (e) => {
    setFolderName(e.target.value);

  };
  const handleKeyPressEdit = (event) => {
    if (event.keyCode === 13) {
      editFolName();
    }
  };
  const [expanded] = React.useState(localStorage.getItem("selectedNode")==undefined ? [] : JSON.parse(localStorage.getItem("selectedNode")));

  const handleToggle = (event, nodeIds) => {
    let string = JSON.stringify(nodeIds)
    localStorage.setItem("selectedNode",string);        
  }

  return (
    <div>
      <TreeView
        defaultCollapseIcon={<FaFolderOpen />}
        defaultExpandIcon={<FaFolderPlus />}
        defaultExpanded={expanded}
        defaultEndIcon={<FaFolderMinus />}
        onNodeSelect={getNodeId}
        onNodeToggle={handleToggle}
        onContextMenu={(e) => {
          e.preventDefault();
          setClicked(true);
          setPoints({
            x: e.pageX,
            y: e.pageY,
          });
          console.log("Right Click", e.pageX, e.pageY);
        }}
      >
        {getTreeItemsFromData(nodes)}
      </TreeView>
      {clicked && (
        <ContextMenu top={points.y} left={points.x}>
          <ul>
            <li onClick={performPaste}>Paste</li>
            {/* onClick={editFolName} */}
            <li  onClick={handleClickOpen}>Edit</li>
            <li onClick={deleteFolder}>Delete</li>
          </ul>
        </ContextMenu>
        
      )
      
      }
      <BootstrapDialog
        onClose={handleClose}
        aria-labelledby="customized-dialog-title"
        open={editOpen}
      >
        <BootstrapDialogTitle id="customized-dialog-title" onClose={handleClose}>
         EDIT FOLDER NAME
        </BootstrapDialogTitle>
        <DialogContent dividers>
                  
        <Box component="form" sx={{'& .MuiTextField-root': { m: 2, width: '50ch'}, width: '400px'}}
         noValidate autoComplete="off">
        <p>
      <Typography gutterBottom>Folder Name:</Typography>
        <TextField type="input"   margin="normal" id="folderName" name="folderName" onChange={handleFolderName} onKeyDown={handleKeyPressEdit}
              required fullWidth autoFocus /> 
       </p></Box>
       
      </DialogContent>
      <DialogActions>
       
        <Button variant="contained" color="neutral"   onClick={handleClose}>
          CLOSE
        </Button>
        <Button variant="contained"  color="success" onClick={editFolName}>
          UPDATE
        </Button>
    
        </DialogActions>
      </BootstrapDialog>
      <div>
      <Snackbar
        open={open1}
        autoHideDuration={3000}
        onClose={handleSnackClose}
        anchorOrigin={{ vertical: "top", horizontal: "center" }}
      >
        <Alert
          onClose={handleSnackClose}
          message={snackbarMessage}
          severity={snackbarSeverity}
        >
          {snackbarMessage}
        </Alert>
      </Snackbar>
      </div>
    </div>

  );
};

export default Tree;
