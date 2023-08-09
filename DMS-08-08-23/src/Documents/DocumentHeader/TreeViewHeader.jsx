import React,{ useCallback,useState } from 'react'
import {Box,IconButton,useTheme,Typography}  from "@mui/material";
import { useContext } from 'react';
import { ColorModeContext,tokens } from '../../theme';
import FolderOutlinedIcon from '@mui/icons-material/FolderOutlined';
import FolderIcon from '@mui/icons-material/Folder';
import CreateNewFolderOutlinedIcon from '@mui/icons-material/CreateNewFolderOutlined';
import CreateNewFolderIcon from '@mui/icons-material/CreateNewFolder';
import FileUploadOutlinedIcon from '@mui/icons-material/FileUploadOutlined';
import FileUploadIcon from '@mui/icons-material/FileUpload';
import BookmarkBorderOutlinedIcon from '@mui/icons-material/BookmarkBorderOutlined';
import BookmarkIcon from '@mui/icons-material/Bookmark';
import ShoppingCartCheckoutOutlinedIcon from '@mui/icons-material/ShoppingCartCheckoutOutlined';
import ShoppingCartIcon from '@mui/icons-material/ShoppingCart';
import '../../index.css'
import Tooltip from '@mui/material/Tooltip';
import PropTypes from 'prop-types';
import Button from '@mui/material/Button';
import { styled } from '@mui/material/styles';
import Dialog from '@mui/material/Dialog';
import DialogTitle from '@mui/material/DialogTitle';
import DialogContent from '@mui/material/DialogContent';
import DialogActions from '@mui/material/DialogActions';
import {useDropzone} from 'react-dropzone';
import TextField from '@mui/material/TextField';
import { DMSAPI } from "../../Service/DMSAPI";
import CloseIcon from '@mui/icons-material/Close';
import Snackbar from "@material-ui/core/Snackbar";
import MuiAlert from "@mui/material/Alert";
import { useNavigate } from 'react-router-dom';
import { useGlobalContext } from "../../Context/DataContext";
import DeleteIcon from '@mui/icons-material/Delete';

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

function Alert(props) {
  return <MuiAlert elevation={6} variant="filled" {...props} />;
}


const TreeViewHeader = () => {

  const {
    bookmarkOpen,
    setBookmarkOpen,
    checkOutOpen,
    setCheckOutOpen,
    handleRefresh,
    handleTreeRefresh,
   } = useGlobalContext();

  const theme=useTheme();
  const navigate = useNavigate();
  const colors=tokens(theme.palette.mode);
  const colorMode=useContext(ColorModeContext);
  const [open1, setOpen] = useState(false);
  const [snackbarMessage, setSnackbarMessage] = useState("");
  const [snackbarSeverity, setSnackbarSeverity] = useState("success");
  const [addOpen, setAddOpen] = React.useState(false);
  const [uploadOpen, setUploadOpen] = React.useState(false);
  const [foldName,setFolderName]=React.useState([''])
  const [selectedFiles, setSelectedFiles] = useState([]);
  const [files1, setFiles1] = useState([]);
  const [uploadResponse, setUploadResponse] = useState('');
  const [showLoader, setShowLoader] = useState(false);
  const [showLoader1, setShowLoader1] = useState(false);
  const [selectedFilesCount, setSelectedFilesCount] = useState(0);

  const [showLoaderAddFolder, setShowLoaderAddFolder] = useState(false);
  const [showLoader1AddFolder, setShowLoader1AddFolder] = useState(false);

  const [list, updateList] = useState(selectedFiles);

  // console.log(showLoader)

  // console.log(files1)
  // console.log(selectedFiles.length)
  
  const handleFileChange = (e) => {
    
    // var files2 = e.target.files;
    // var filesArr = Array.prototype.slice.call(files2);
    // setFiles1(filesArr);
    // const files  = [...e.target.files]

    // setSelectedFiles(files)
    // files.forEach((file) => {
    //  // setSelectedFiles((prevFiles) => [...prevFiles, file]);
      

    //   const reader = new FileReader();
    //   reader.onload = (e) => {
    //     const html = (
    //       <div key={file.name}>
    //         <p>
    //           {file.name} &nbsp;&nbsp;&nbsp;
    //           {/* <img
    //             src="assets/remove.png"
    //             data-file={file.name}
    //             className="selFile"
    //           /> */}
    //         </p>
    //       </div>
    //     );

    //   };

    //   reader.readAsDataURL(file);
    // });
    const files = e.target.files;
    setSelectedFiles([...selectedFiles, ...files]);
    setSelectedFilesCount(selectedFilesCount + files.length);
  };

  const handleUpload =async (e) => {
     setShowLoader(true);
    e.preventDefault();
    // console.log(selectedFiles)
    // console.log(e)
    // console.log(showLoader)
    var formData = new FormData();
    if (selectedFiles.length === 0) {
      setSnackbarMessage("Please choose a file to upload");
      setSnackbarSeverity("warning");
      setOpen(true);
      
      return;
    }else{
      // const filesArray = Array.from(selectedFiles);
      // const obj={};
      // console.log(filesArray)
      for (var i = 0; i < selectedFiles.length; i++) {
        // obj.append('files', JSON.stringify(filesArray[i]));
        // console.log(obj)
       // formData.append(`file${i}`, selectedFiles[i]);
        // formData = {...formData, file:selectedFiles}
        // console.log(selectedFiles[i])
        // console.log(formData.toString())
    }
      if (localStorage.getItem("node") === '1') {
        setSnackbarMessage("Files can't be loaded into the default folder");
        setSnackbarSeverity("warning");
        setOpen(true);
        document.getElementById('uploadFileButton').disabled = true;
      } else {
      
        try {
          // setTimeout(() => {
          //   setShowLoader(false); // Hide the loader
          // }, 2000);
          
        const response = await DMSAPI.upload(files1,localStorage.getItem("node"));
        
     

        // console.log(response)
        if(response.status === 200){
          setSnackbarMessage("Files uploaded successfully");
          setSnackbarSeverity("success");
          setOpen(true);
          setShowLoader(false);
          setShowLoader1(true)
          setTimeout(() => {
            uploadHandleClose()
          }, 2000);
          handleRefresh();
         
        }else{
          setShowLoader(false);
          setShowLoader1(true)
        }
        }
        catch(error){
            if (error.response && error.response.data && error.response.data.message) {
              alert(error.response.data.message);
              if (error.response.data.message.includes('Session Expired')) {
                window.location.href = "/login";
              } else {
                document.getElementById('uploadResponse').innerHTML = "<p>Upload error. Try again.</p>";
                // $("#loader").hide();
                setSnackbarMessage("Upload error. Try again.");
                setSnackbarSeverity("error");
                setOpen(true);
                document.getElementById('uploadFileButton').disabled = false;
              }
            } else {
              setSnackbarMessage("Upload error. Try again.");
              setSnackbarSeverity("error");
              setOpen(true);
              document.getElementById('uploadResponse').innerHTML = "<p>Upload error. Try again.</p>";
              // $("#loader").hide();
              document.getElementById('uploadFileButton').disabled = false;
            }
          };
      }     
    }

    // const formData = new FormData();
    // selectedFiles.forEach((file) => {
    //   formData.append('files', file);
    // });

  
  };
  const handleRemoveItem = (name,length) => {
    const updatedList = selectedFiles.filter(file => file.name !== name);
    length-=length;
    setSelectedFiles(updatedList);
    setSelectedFilesCount(selectedFilesCount + files.length);
    
  };

 

  const handleClickOpen = () => {
    setAddOpen(true);
  };
  const handleClose = () => {
    setShowLoaderAddFolder(false)
    setShowLoader1AddFolder(false) 
    setAddOpen(false);
  };
 
  const uploadHandleClickOpen = () => {
    setUploadOpen(true);
    // console.log(uploadOpen)
  };
  const uploadHandleClose = () => {
    setSelectedFiles([])
    setShowLoader(false)
    setShowLoader1(false)

    setUploadOpen(false);
  };
  const addFile=()=>{
    console.log("file uploaded")
  }
  const getFolderName= (e) => {
    console.log(e.target.value)
    setFolderName=e.target.value;
  }

  // ADD FOLDER FUNCTION CODE START
  const handleAdd=useCallback(async () => {
    setShowLoaderAddFolder(true);
    var folderName = document.getElementById("folderName").value;
    if(folderName==="")
    {
      setSnackbarMessage("Please enter folder name and then add");
      setSnackbarSeverity("warning");
      setOpen(true);
      
    }
    else{
      try {
      const response = await DMSAPI.addFolderName(folderName);
      const data = await response.json();
      console.log(folderName+" folder added")
      if(response.status === 200){
        setShowLoaderAddFolder(false);
        setShowLoader1AddFolder(true);
        setAddOpen(false);
        setSnackbarMessage("Folder Added successfully");
        setSnackbarSeverity("success");
        setOpen(true);
        handleTreeRefresh();
       
      }
      else{
        setShowLoaderAddFolder(false);
        setShowLoader1AddFolder(true)
      }

    } catch (error) {
      console.log("add folder catch" + error);
    }
  }},[])
  // ADD FOLDER FUNCTION CODE END

  const handleKeyPressAdd = (event) => {
    if (event.keyCode === 13) {
      // Enter key is pressed
      handleAdd(); // Call your function here
    }
  };
  
  const {getRootProps, getInputProps, open, acceptedFiles} = useDropzone({
    // Disable click and keydown behavior
    noClick: true,
    noKeyboard: true
  });

  const files = acceptedFiles.map(file => (
    <li key={file.path}>
      {file.path} - {file.size} bytes
    </li>
  ));

  
   const reloadMeth = () => {
    window.location.reload(false);
    //  navigate(".", { replace: true });
    navigate("/documents");
  };

  const getFolder = () => {
    setBookmarkOpen(false)
     setCheckOutOpen(false)
  }

  // BOOKMARK CODE START
 const getBookmark = () => { 
       setBookmarkOpen(true)
       setCheckOutOpen(false)
       handleRefresh();
      //  console.log(bookmarkOpen)
 }
  // BOOKMARK CODE END

   // CHECKOUT CODE START
 const getCheckOut = () => {
      setCheckOutOpen(true)
      setBookmarkOpen(false)
      handleRefresh();
      // console.log(checkOutOpen)
 }
 // CHECKOUT CODE END

 const handleSnackClose = (event, reason) => {
  if (reason === "clickaway") {
    return;
  }
  setOpen(false);
};

const refreshMeth = () => {
  
}

  return (
     <Box display="flex" justifyContent="start" sx={{padding:'0',margin:'0'}}>
      <div>
      <Tooltip title="Folder Browser">
            <IconButton onClick={getFolder}>
            {theme.palette.mode==='dark'? (
                          <FolderOutlinedIcon color="secondary" />
                        ): (
                          <FolderIcon color="secondary" />
                        )}
                
            </IconButton>
      </Tooltip>

      {/* <Tooltip title="Folder Browser">
            <IconButton onClick={refreshMeth}>
            {theme.palette.mode==='dark'? (
                          <FolderOutlinedIcon color="secondary" />
                        ): (
                          <FolderIcon color="secondary" />
                        )}
                
            </IconButton>
      </Tooltip> */}

      <Tooltip title="Add Folder">
          <IconButton variant="outlined" onClick={handleClickOpen} >
          {theme.palette.mode==='dark'? (
                        <CreateNewFolderOutlinedIcon color="secondary" />
                      ): (
                        <CreateNewFolderIcon color="secondary" />
                      )}
              
          </IconButton>
      </Tooltip>
      <BootstrapDialog
        onClose={handleClose}
        aria-labelledby="customized-dialog-title"
        open={addOpen}
      >
        <BootstrapDialogTitle id="customized-dialog-title" onClose={handleClose}>
         ADD FOLDER
        </BootstrapDialogTitle>
        <DialogContent dividers>
                  
        <Box component="form" sx={{'& .MuiTextField-root': { m: 2, width: '50ch'}, width: '400px'}}
         noValidate autoComplete="off">
        <p>
      <Typography gutterBottom>Folder Name:</Typography>
        <TextField type="input"   margin="normal" id="folderName" name="folderName" onChange={getFolderName}  onKeyDown={handleKeyPressAdd}
              required fullWidth autoFocus /> 
       </p></Box>
       <div>
          {showLoaderAddFolder === true && showLoader1AddFolder === false ? <div ><h1>loading...</h1></div> : 
          <>{showLoaderAddFolder === false && showLoader1AddFolder === false ? <h1></h1> : <h1> Folder Added</h1>}</>}
       </div> 
      </DialogContent>
      <DialogActions>
       
        <Button variant="contained" color="neutral"   onClick={handleClose}>
          CLOSE
        </Button>
        <Button variant="contained"  color="success" onClick={handleAdd}>
          ADD
        </Button>
    
        </DialogActions>
      </BootstrapDialog>

      <Tooltip title="Upload File">
          <IconButton onClick={uploadHandleClickOpen}>
          {theme.palette.mode==='dark'? (
                        <FileUploadOutlinedIcon color="secondary" />
                      ): (
                        <FileUploadIcon color="secondary" />
                      )}
              
            
          </IconButton>
      </Tooltip>
      <BootstrapDialog
        onClose={uploadHandleClose}
        aria-labelledby="customized-dialog-title"
        open={uploadOpen}
      >
        <BootstrapDialogTitle id="customized-dialog-title" onClose={uploadHandleClose}>
         UPLOAD FILE
        </BootstrapDialogTitle>
        <DialogContent dividers>
        
        
        <Box component="form" sx={{
          '& .MuiTextField-root': { m: 2, width: '50ch'}, width: '400px',height:'100px'}}
        noValidate autoComplete="off" >
                <span>File Name : </span>
                <input type="file" id="myFile" name="filename" length={selectedFilesCount} multiple onChange={handleFileChange}/><br></br>
          
                 <div>
                  {showLoader === true && showLoader1 === false ? <div ><h1>loading...</h1></div> : 
                  <>{showLoader === false && showLoader1 === false ? <h1></h1> : <h1> upload completed</h1>}</>}
                </div> 
                <div id="selectedFiles">
                  {
                    selectedFiles.map((file) => {
                      return(
                          <div key={file.name}><p style={{display:"flex" }}>{file.name}<DeleteIcon onClick={() => handleRemoveItem(file.name)} /></p></div>
                      )})}
                </div> 
               

        </Box>
       
        </DialogContent>
        <DialogActions>
       
        <Button variant="contained" color="neutral"   onClick={uploadHandleClose}>
          CLOSE
        </Button>
        <Button variant="contained"  id="uploadFileButton" color="success" onClick={handleUpload}>
          UPLOAD
        </Button>
    
        </DialogActions>
      </BootstrapDialog>

      <Tooltip title="Bookmark">
          <IconButton  onClick={getBookmark}>
          {theme.palette.mode==='dark'? (
                        <BookmarkBorderOutlinedIcon color="secondary" />
                      ): (
                        <BookmarkIcon color="secondary" />
                      )}
              
              
          </IconButton>
      </Tooltip>

      <Tooltip title="Checkout Dashboard">
          <IconButton onClick={getCheckOut}>
          {theme.palette.mode==='dark'? (
                        <ShoppingCartCheckoutOutlinedIcon color="secondary" />
                      ): (
                        <ShoppingCartIcon color="secondary" />
                      )}
              
              
          </IconButton>
      </Tooltip>
      <div>
      <Snackbar
        open={open1}
        autoHideDuration={6000}
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
    </Box>
    
  )
}

export default TreeViewHeader