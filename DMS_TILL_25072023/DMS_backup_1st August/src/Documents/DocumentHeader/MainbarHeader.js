import React from "react";
import { useGlobalContext } from "../../Context/DataContext";
import { makeStyles } from "@material-ui/core";
import { RxDividerVertical } from "react-icons/rx";
import { Box, useTheme, Typography, Button, ListItemIcon } from "@mui/material";
import { useContext } from "react";
import { ColorModeContext, tokens } from "../../theme";
import { DMSAPI } from "../../Service/DMSAPI";
import RefreshOutlinedIcon from "@mui/icons-material/RefreshOutlined";
import FileDownloadOutlinedIcon from "@mui/icons-material/FileDownloadOutlined";
import PictureAsPdfOutlinedIcon from "@mui/icons-material/PictureAsPdfOutlined";
import PictureAsPdfIcon from "@mui/icons-material/PictureAsPdf";
import SplitscreenIcon from "@mui/icons-material/Splitscreen";
import DifferenceOutlinedIcon from "@mui/icons-material/DifferenceOutlined";
import DifferenceIcon from "@mui/icons-material/Difference";
import DeleteOutlineOutlinedIcon from "@mui/icons-material/DeleteOutlineOutlined";
import DeleteIcon from "@mui/icons-material/Delete";
import PrintOutlinedIcon from "@mui/icons-material/PrintOutlined";
import PrintIcon from "@mui/icons-material/Print";
import ApprovalOutlinedIcon from "@mui/icons-material/ApprovalOutlined";
import ApprovalIcon from "@mui/icons-material/Approval";
import GridViewOutlinedIcon from "@mui/icons-material/GridViewOutlined";
import ReorderIcon from "@mui/icons-material/Reorder";
import GridViewIcon from "@mui/icons-material/GridView";
import "../../index.css";
import ToggleOnIcon from "@mui/icons-material/ToggleOn";
import ToggleOffIcon from "@mui/icons-material/ToggleOff";
import { useState } from "react";
import MuiAlert from "@mui/material/Alert";
import { IconButton, Grid } from "@material-ui/core";
import { Close as CloseIcon } from "@material-ui/icons";
import Snackbar from "@material-ui/core/Snackbar";
import { styled } from "@mui/material/styles";
import PropTypes from "prop-types";
import TextField from "@mui/material/TextField";
import Dialog from "@mui/material/Dialog";
import DialogTitle from "@mui/material/DialogTitle";
import DialogContent from "@mui/material/DialogContent";
import DialogActions from "@mui/material/DialogActions";
import { withStyles } from "@material-ui/core/styles";
import { color } from "@mui/system";
import { Label } from "@material-ui/icons";
import Tooltip from "@mui/material/Tooltip";
import MenuItem from "@material-ui/core/MenuItem";
import FormControl from "@material-ui/core/FormControl";
import Select from "@material-ui/core/Select";
import Radio from "@mui/material/Radio";
import RadioGroup from "@mui/material/RadioGroup";
import FormControlLabel from "@mui/material/FormControlLabel";
import FormLabel from "@mui/material/FormLabel";
import { useLocation } from "react-router-dom";
import { useEffect } from "react";

const BootstrapDialog = styled(Dialog)(({ theme }) => ({
  "& .MuiDialogTitle-root": {
    padding: theme.spacing(1),
  },
  "& .MuiDialogContent-root": {
    padding: theme.spacing(2),
  },
  "& .MuiDialogActions-root": {
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
          sx={{
            position: "absolute",
            right: 20,
            top: 8,
            float: "right",
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

const styles = {
  input: {
    height: "30px", // Adjust the value as needed
    padding: "10px", // Adjust the value as needed
  },
  root: {
    "& .MuiOutlinedInput-root": {
      "&:hover fieldset": {
        border: "none",
      },
      "&.Mui-focused fieldset": {
        border: "none",
      },
    },
  },
};

const useStyles = makeStyles((theme) => ({
  formControl: {
    margin: theme.spacing(1),
    minWidth: 120,
  },
  selectEmpty: {
    marginTop: theme.spacing(2),
  },
  menuItem: {
    "&.Mui-selected": {
      color: "primary", // replace with your desired color
    },
  },
}));

function Alert(props) {
  return <MuiAlert elevation={6} variant="filled" {...props} />;
}

const MainbarHeader = () => {
  const location = useLocation();

  const MyTextField = withStyles(styles)(TextField);
  const theme = useTheme();
  const colors = tokens(theme.palette.mode);
  const colorMode = useContext(ColorModeContext);
  const classes = useStyles();
  const {
    pdfToggleActive,
    pdfToggleclick,
    listView,
    listToGrid,
    checkboxForm,
    fileInfo,
    files,
    nodeValue,
    handleRefresh,
    searchFiles,
  } = useGlobalContext();
  // console.log(nodeValue);
  const { fileName, fileId } = checkboxForm;
  const { response } = fileInfo;

  const stringResponse = response.toString();
  // console.log(stringResponse);

  const NullCheck = response.length;

  const [state, setState] = useState({
    open: false,
    vertical: "top",
    horizontal: "center",
  });
  const { vertical, horizontal, open } = state;
  const [open1, setOpen] = useState(false);
  const [snackbarMessage, setSnackbarMessage] = useState("");
  const [snackbarSeverity, setSnackbarSeverity] = useState("success");
  const [splitOpen, setSplitOpen] = React.useState(false);
  const [mergeOpen, setMergeOpen] = React.useState(false);
  const [stampOpen, setStampOpen] = React.useState(false);
  const [gridOpen, setGridOpen] = React.useState(false);
  const [splitFromRange, setSplitFromRange] = React.useState();
  const [splitToRange, setSplitToRange] = React.useState();
  const [splitFileName, setSplitFileName] = React.useState();
  const [selectedFiles, setSelectedFiles] = useState([]);
  const [mergefiles, setMergefiles] = useState([]);
  const [stampPageValue, setStampPageValue] = React.useState("first");
  const [showLoaderSplit, setShowLoaderSplit] = useState(false);
  const [showLoader1Split, setShowLoader1Split] = useState(false);
  const [showLoaderMerge, setShowLoaderMerge] = useState(false);
  const [showLoader1Merge, setShowLoader1Merge] = useState(false);
  const [deleteOpen, setDeleteOpen] = React.useState(false);

  const StampPageChange = (event) => {
    setStampPageValue(event.target.value);
  };
  const [stampStatus, setStampStatus] = React.useState("");
  // console.log(stampStatus);
  // console.log(stampPageValue);
  // console.log(fileId);

  const handleClickSplitOpen = () => {
    setSplitOpen(true);
  };
  const handleSplitClose = () => {
    setSplitOpen(false);
    setShowLoaderSplit(false)
    setShowLoader1Split(false)
  };
  const handleClickMergeOpen = () => {
    setMergeOpen(true);
  };
  const handleMergeClose = () => {
    setMergeOpen(false);
    setShowLoaderMerge(false)
    setShowLoader1Merge(false)
  };
  const handleClickStampOpen = () => {
    setStampOpen(true);
  };
  const handleStampClose = () => {
    setStampOpen(false);
  };
  const handleClickGridOpen = () => {
    setGridOpen(true);
  };
  const gridHandleClose = () => {
    setGridOpen(false);
  };
  const handleClose = (event, reason) => {
    if (reason === "clickaway") {
      return;
    }
    setOpen(false);
  };
  const handleStampStatus = (event) => {
    setStampStatus(event.target.value);
  };

  // File Download API Fetch Start
  const FileDownloadAPI = async (e) => {
    console.log(e);
    const pdfFile = e;
    const response = await DMSAPI.downloadFile(stringResponse, pdfFile);
    console.log(response);
    if (response.status === 200) {
      const data = await response.blob();
      const fileURL = window.URL.createObjectURL(data);
      let alink = document.createElement("a");
      alink.href = fileURL;

      if (
        pdfFile === "Yes" &&
        fileName.includes(".pdf") &&
        !stringResponse.includes(",")
      ) {
        alink.download = `${fileName}`;
      } else if (pdfFile === "No" && !stringResponse.includes(",")) {
        alink.download = `${fileName}`;
      } else if (pdfFile === "Yes" && !stringResponse.includes(",")) {
        var fname = `${fileName}`.split(".");
        alink.download = `${fname[0]}` + ".pdf";
      }

      alink.click();
      setSnackbarMessage("File downloaded successfully");
      setSnackbarSeverity("success");
      setOpen(true);
      console.log("File Downloaded Successfully");
    } else {
      setSnackbarMessage("internal server error");
      setSnackbarSeverity("error");
      setOpen(true);
      console.log("internal server error");
    }
  };
  const FileDownloadMeth = () => {
    if (NullCheck === 0) {
      console.log("Please Select Any File");
      //  alert("Please Select Any File");
      setSnackbarMessage("Please select a file to download");
      setSnackbarSeverity("warning");
      setOpen(true);
    } else {
      FileDownloadAPI("No");
    }
  };
  const PDFFileDownloadMeth = () => {
    if (NullCheck === 0) {
      console.log("Please Select Any File");
      //  alert("Please Select Any File");
      setSnackbarMessage("Please select any file");
      setSnackbarSeverity("warning");
      setOpen(true);
    } else {
      FileDownloadAPI("Yes");
    }
  };
  // File Download API Fetch End

  // File Print API Fetch Start

  const filePrintAPI = async () => {
    try {
      const response = await DMSAPI.printFile(stringResponse);
      const data = await response.json();
      // console.log(data);
      //  alert("File printed Successfully");
      setSnackbarMessage("File printed Successfully");
      setSnackbarSeverity("success");
      setOpen(true);
      console.log("File printed Successfully");
    } catch (error) {
      console.log(error);
      console.log("print api catch");
    }
  };
  const filePrintMeth = () => {
    if (NullCheck === 0) {
      console.log("Please Select Any File");
      // alert("Please Select Any File");
      setSnackbarMessage("Please Select Any File");
      setSnackbarSeverity("warning");
      setOpen(true);
     }
    //  else if (NullCheck > 1) {
    //   console.log("more than one file is not applicable of this function");
    //   //  alert("more than one file is not applicable of this function");
    //   setSnackbarMessage(
    //     "more than one file is not applicable of this function"
    //   );
    //   setSnackbarSeverity("warning");
    //   setOpen(true);
    // }
     else {
      filePrintAPI();
    }
  };
  // File Print API Fetch end

  // File Delete API Fetch start

  const fileDeleteAPI = async () => {
    try {
      const response = await DMSAPI.deleteFile(stringResponse);
      const data = await response.json();
      console.log(response);
      console.log(response.status);
      // console.log(data);
      // alert("File Deleted Successfully");
      setSnackbarMessage("File Deleted Successfully");
      setSnackbarSeverity("success");
      setOpen(true);
      setDeleteOpen(false);
      console.log("File Deleted Successfully");
      if (response.status === 200) {
        console.log(response.status);
        handleRefresh();
      }
    } catch (error) {
      console.log(error);
      console.log("Delete api catch");
    }
  };
  const fileDeleteMeth = () => {
    if (NullCheck === 0) {
      console.log("Please Select Any File");
      //  alert("Please Select Any File");
      setSnackbarMessage("Please Select Any File");
      setSnackbarSeverity("warning");
      setOpen(true);
    } else {
      setDeleteOpen(true);
      
    }
  };
  // File Delete API Fetch END

  //Split Method START
  const RangeFromChange = (e) => {
    console.log(e);
    console.log(e.target.value);
    setSplitFromRange(e.target.value);
  };
  const RangeToChange = (e) => {
    console.log(e);
    console.log(e.target.value);
    setSplitToRange(e.target.value);
  };
  const splitFileNameChange = (e) => {
    console.log(e.target.value);
    setSplitFileName(e.target.value);
  };
  const FileSplitAPI = async () => {
    setShowLoaderSplit(true);
    try {
      const response = await DMSAPI.splitFile(
        nodeValue,
        fileId,
        splitFileName,
        splitFromRange,
        splitToRange
      );
      console.log(response);
      if (response === 200) {
        setShowLoaderSplit(false);
        setShowLoader1Split(true);
        setSnackbarMessage("File Splitted successfully");
        setSnackbarSeverity("success");
        setOpen(true);
        handleSplitClose();
        handleRefresh();
      }
      else{
        setShowLoaderSplit(false);
        setShowLoader1Split(true)
      }
    } catch (error) {
      console.log("FileSplitAPI Error" + error);
    }
  };
  const FileSplitMeth = () => {
    if (NullCheck === 0) {
      console.log("Please Select Any File");
      //  alert("Please Select Any File");
      setSnackbarMessage("Please Select Any File");
      setSnackbarSeverity("warning");
      setOpen(true);
    } else {
      handleClickSplitOpen();
    }
  };
  //Split Method END
  
  const handleKeyPressSplit = (event) => {
    if (event.keyCode === 13) {
      FileSplitAPI();
    }
  };
  //Merge Method START
  const handleFileChange = (e) => {
    var files2 = e.target.files;
    var filesArr = Array.prototype.slice.call(files2);
    setMergefiles(filesArr);
    const files = [...e.target.files];

    setSelectedFiles(files);
    files.forEach((file) => {
      // setSelectedFiles((prevFiles) => [...prevFiles, file]);

      const reader = new FileReader();
      reader.onload = (e) => {
        const html = (
          <div key={file.name}>
            <p>
              {file.name} &nbsp;&nbsp;&nbsp;
              {/* <img
                src="assets/remove.png"
                data-file={file.name}
                className="selFile"
              /> */}
            </p>
          </div>
        );

        // Update the DOM or React component state to display the uploaded file
        // You can append the 'html' variable to your desired element or state.
        console.log(html);
        console.log(selectedFiles);
      };

      reader.readAsDataURL(file);
    });
  };
  const handleMerge = async (e) => {
    setShowLoaderMerge(true);
    e.preventDefault();
    console.log(selectedFiles);
    console.log(e);
    var formData = new FormData();
    if (selectedFiles.length === 0) {
      alert("Please choose a file to Merge");
      return;
    } else {
      for (var i = 0; i < selectedFiles.length; i++) {
        console.log(selectedFiles[i]);
        console.log(formData.toString());
      }
      try {
        const response = await DMSAPI.mergeFile(
          mergefiles,
          localStorage.getItem("node"),
          fileId
        );
        console.log(response);
        if (response.status === 200) {
          setShowLoaderMerge(false);
          setShowLoader1Merge(true);
          handleMergeClose();
          handleRefresh();
        }
        else{
          
            setShowLoaderMerge(false);
            setShowLoader1Merge(true)
          
        }
      } catch (error) {
        if (
          error.response &&
          error.response.data &&
          error.response.data.message
        ) {
          alert(error.response.data.message);
          if (error.response.data.message.includes("Session Expired")) {
            window.location.href = "/login";
          } else {
            document.getElementById("uploadResponse").innerHTML =
              "<p>Merge error. Try again.</p>";
            alert("Merge error. Try again.");
            document.getElementById("mergeFileButton").disabled = false;
          }
        } else {
          alert("Merge error. Try again.");
          document.getElementById("uploadResponse").innerHTML =
            "<p>Merge error. Try again.</p>";
          document.getElementById("mergeFileButton").disabled = false;
        }
      }
    }

    // const formData = new FormData();
    // selectedFiles.forEach((file) => {
    //   formData.append('files', file);
    // });
  };
  const FileMergeMeth = () => {
    if (NullCheck === 0) {
      console.log("Please Select Any File");
      //  alert("Please Select Any File");
      setSnackbarMessage("Please Select Any File");
      setSnackbarSeverity("warning");
      setOpen(true);
    } else {
      handleClickMergeOpen();
    }
  };
  //Merge Method END
  
 
  //STAMP Method START
  const handlestamp = async () => {
    try {
      const response = await DMSAPI.stampFile(
        fileId,
        stampStatus,
        stampPageValue
      );
      console.log(response);
      if (response.status === 200) {
        setSnackbarMessage("File Stamped Successfully");
        setSnackbarSeverity("success");
        setOpen(true);
        console.log("File Stamped Successfully");
        handleStampClose();
      }
    } catch (error) {
      console.log(error);
      console.log("Stamp api catch");
    }
  };
  const FileStampMeth = () => {
    if (NullCheck === 0) {
      console.log("Please Select Any File");
      //  alert("Please Select Any File");
      setSnackbarMessage("Please Select Any File");
      setSnackbarSeverity("warning");
      setOpen(true);
    } else {
      handleClickStampOpen();
    }
  };
  //STAMP Method END

  const handleClickDeleteOpen = () => {
    
    setDeleteOpen(true);
  };

  const handleDeleteClose = () => {
    setDeleteOpen(false);
  };

  return (
    <Box
      display="flex"
      justifyContent="start"
      sx={{ padding: "0", margin: "0", flexWrap: "wrap" }}
    >
      <div display="flex" style={{ padding: "0", margin: "0" }}>
        <Tooltip title="Refresh">
          <IconButton
            sx={{ minHeight: "0", flexGrow: 1 }}
            onClick={handleRefresh}
          >
            <RefreshOutlinedIcon color="secondary" mr="5px" />
          </IconButton>
        </Tooltip>

        <Tooltip title="Download File">
          <IconButton
            sx={{ minHeight: "0", flexGrow: 1 }}
            onClick={FileDownloadMeth}
          >
            <FileDownloadOutlinedIcon color="secondary" />
          </IconButton>
        </Tooltip>

        <Tooltip title="Download File as PDF">
          <IconButton
            sx={{ minHeight: "0", flexGrow: 1 }}
            onClick={PDFFileDownloadMeth}
          >
            {theme.palette.mode === "dark" ? (
              <PictureAsPdfOutlinedIcon color="secondary" />
            ) : (
              <PictureAsPdfIcon color="secondary" />
            )}
          </IconButton>
        </Tooltip>

        <Tooltip title="Split PDF">
          <IconButton
            sx={{ minHeight: "0", flexGrow: 1 }}
            onClick={FileSplitMeth}
          >
            <SplitscreenIcon color="secondary" mr="5px" />
          </IconButton>
        </Tooltip>
        <BootstrapDialog
          onClose={handleSplitClose}
          aria-labelledby="customized-dialog-title"
          open={splitOpen}
        >
          <BootstrapDialogTitle
            id="customized-dialog-title"
            onClose={handleSplitClose}
          >
            SPLIT FILE
          </BootstrapDialogTitle>
          <DialogContent dividers>
            <Box
              component="form"
              sx={{
                "& .MuiTextField-root": { m: 0, width: "20ch" },
                width: "400px",
              }}
              noValidate
              autoComplete="off"
            >
              <Grid container spacing={2} alignItems="center">
                <Grid item>
                  <Typography gutterBottom>
                    File Name &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:&nbsp;&nbsp;
                  </Typography>
                </Grid>
                <Grid item>
                  <p>{fileName}</p>
                </Grid>
              </Grid>
              <Grid container spacing={2} alignItems="center">
                <Grid item>
                  <Typography>Range From &nbsp;&nbsp;:</Typography>
                </Grid>
                <Grid item>
                  &nbsp;&nbsp;
                  <TextField
                    type="input"
                    margin="normal"
                    required
                    style={{ width: 250 }}
                    autoFocus
                    InputProps={{ style: styles.input }}
                    onChange={RangeFromChange}
                  />
                </Grid>
              </Grid>
              <Grid container spacing={2} alignItems="center">
                <Grid item>
                  <Typography>
                    Range To &nbsp; &nbsp; &nbsp;&nbsp;&nbsp;&nbsp;:{" "}
                  </Typography>
                </Grid>
                <Grid item>
                  &nbsp;&nbsp;
                  <TextField
                    type="input"
                    margin="normal"
                    required
                    style={{ width: 250 }}
                    autoFocus
                    InputProps={{ style: styles.input }}
                    onChange={RangeToChange}
                  />
                </Grid>
              </Grid>
              <Grid container spacing={1} alignItems="center">
                <Grid item>
                  <Typography>New File Name :</Typography>
                </Grid>
                <Grid item>
                  &nbsp;
                  <TextField
                    type="input"
                    margin="normal"
                    required
                    style={{ width: 250 }}
                    autoFocus
                    InputProps={{ style: styles.input }}
                    onChange={splitFileNameChange}
                    onKeyDown={handleKeyPressSplit}
                  />
                </Grid>
                <div>
                  {showLoaderSplit === true && showLoader1Split === false ? (
                    <div>
                      <h1>loading...</h1>
                    </div>
                  ) : (
                    <>
                      {showLoaderSplit === false &&
                      showLoader1Split === false ? (
                        <h1></h1>
                      ) : (
                        <h1> Split process completed</h1>
                      )}
                    </>
                  )}
                </div>
              </Grid>
            </Box>
          </DialogContent>
          <DialogActions>
            <Button
              variant="contained"
              color="neutral"
              onClick={handleSplitClose}
            >
              CLOSE
            </Button>
            <Button variant="contained" color="success" onClick={FileSplitAPI}>
              SPLIT
            </Button>
          </DialogActions>
        </BootstrapDialog>

        <Tooltip title="Merge PDF">
          <IconButton
            sx={{ minHeight: "0", flexGrow: 1 }}
            onClick={FileMergeMeth}
          >
            {theme.palette.mode === "dark" ? (
              <DifferenceOutlinedIcon color="secondary" />
            ) : (
              <DifferenceIcon color="secondary" />
            )}
          </IconButton>
        </Tooltip>
        <BootstrapDialog
          onClose={handleMergeClose}
          aria-labelledby="customized-dialog-title"
          open={mergeOpen}
        >
          <BootstrapDialogTitle
            id="customized-dialog-title"
            onClose={handleMergeClose}
          >
            Merge File
          </BootstrapDialogTitle>
          <DialogContent dividers>
            <Box
              component="form"
              sx={{
                "& .MuiTextField-root": { m: 0, width: "20ch" },
                width: "400px",
              }}
              noValidate
              autoComplete="off"
            >
              <Grid container spacing={2} alignItems="center">
                <span>File Name : </span>
                <input
                  type="file"
                  id="myFile"
                  name="filename"
                  multiple
                  onChange={handleFileChange}
                />
                <br></br>
                <Grid item>
                  <div id="selectedFiles"></div>
                </Grid>
                <div>
                  {showLoaderMerge === true && showLoader1Merge === false ? (
                    <div>
                      <h1>loading...</h1>
                    </div>
                  ) : (
                    <>
                      {showLoaderMerge === false &&
                      showLoader1Merge === false ? (
                        <h1></h1>
                      ) : (
                        <h1> Merge completed</h1>
                      )}
                    </>
                  )}
                </div>
              </Grid>
            </Box>
          </DialogContent>
          <DialogActions>
            <Button
              variant="contained"
              color="neutral"
              onClick={handleMergeClose}
            >
              CLOSE
            </Button>
            <Button
              variant="contained"
              id="mergeFileButton"
              color="success"
              onClick={handleMerge}
            >
              MERGE
            </Button>
          </DialogActions>
        </BootstrapDialog>

        <Tooltip title="Delete">
        {/* onClick={fileDeleteMeth} */}
          <IconButton
            sx={{ minHeight: "0", flexGrow: 1 }}
            onClick={() => {
              fileDeleteMeth();
              
            }}
          >
            {theme.palette.mode === "dark" ? (
              <DeleteOutlineOutlinedIcon color="secondary" />
            ) : (
              <DeleteIcon color="secondary" />
            )}
          </IconButton>
        </Tooltip>
        <BootstrapDialog
            onClose={handleDeleteClose}
            aria-labelledby="customized-dialog-title"
            open={deleteOpen}
            sx={{
              display: "flex",
              justifyContent: "space-between",
              alignItems: "center",
            }}
          >
            <p style={{ width: "250px" }}>Are you sure you want to delete?</p>
            <DialogActions>
              <Button variant="contained" color="neutral" onClick={fileDeleteAPI}>
                YES
              </Button>
              <Button
                variant="contained"
                color="success"
                onClick={handleDeleteClose}
              >
                NO
              </Button>
            </DialogActions>
          </BootstrapDialog>

        <Tooltip title="Print">
          <IconButton
            sx={{ minHeight: "0", flexGrow: 1 }}
            onClick={filePrintMeth}
          >
            {theme.palette.mode === "dark" ? (
              <PrintOutlinedIcon color="secondary" />
            ) : (
              <PrintIcon color="secondary" />
            )}
          </IconButton>
        </Tooltip>

        <Tooltip title="Stamp">
          <IconButton
            sx={{ minHeight: "0", flexGrow: 1 }}
            onClick={FileStampMeth}
          >
            {theme.palette.mode === "dark" ? (
              <ApprovalOutlinedIcon color="secondary" />
            ) : (
              <ApprovalIcon color="secondary" />
            )}
          </IconButton>
        </Tooltip>
        <BootstrapDialog
          onClose={handleStampClose}
          aria-labelledby="customized-dialog-title"
          open={stampOpen}
        >
          <BootstrapDialogTitle
            id="customized-dialog-title"
            onClose={handleStampClose}
          >
            Stamp File
          </BootstrapDialogTitle>
          <DialogContent dividers>
            <Box
              component="form"
              sx={{
                "& .MuiTextField-root": { m: 0, width: "20ch" },
                width: "400px",
              }}
              noValidate
              autoComplete="off"
            >
              <Grid container spacing={2} alignItems="center">
                <Grid item>
                  <Typography>Stamp Status:</Typography>
                </Grid>
                <Grid item>
                  <FormControl className={classes.formControl}>
                    <Select
                      value={stampStatus}
                      onChange={handleStampStatus}
                      displayEmpty
                      className={classes.selectEmpty}
                      inputProps={{ "aria-label": "Without label" }}
                      MenuProps={{ PaperProps: { style: { maxHeight: 300 } } }}
                    >
                      <MenuItem
                        value={"rejected"}
                        classes={{ selected: classes.menuItem }}
                      >
                        <Typography>Rejected</Typography>
                      </MenuItem>
                      <MenuItem
                        value={"approved"}
                        classes={{ selected: classes.menuItem }}
                      >
                        <Typography>Approved</Typography>
                      </MenuItem>
                    </Select>
                  </FormControl>
                </Grid>
                <Grid item>
                  <RadioGroup
                    aria-labelledby="radio-buttons"
                    name="radio-buttons-group"
                    value={stampPageValue}
                    onChange={StampPageChange}
                  >
                    <FormControlLabel
                      value="first"
                      control={<Radio />}
                      label="First Page"
                    />
                    <FormControlLabel
                      value="last"
                      control={<Radio />}
                      label="Last Page"
                    />
                    <FormControlLabel
                      value="all"
                      control={<Radio />}
                      label="All Pages"
                    />
                  </RadioGroup>
                </Grid>
              </Grid>
            </Box>
          </DialogContent>
          <DialogActions>
            <Button
              variant="contained"
              color="neutral"
              onClick={handleStampClose}
            >
              CLOSE
            </Button>
            <Button variant="contained" color="success" onClick={handlestamp}>
              STAMP
            </Button>
          </DialogActions>
        </BootstrapDialog>

        <Tooltip title="Grid View">
          {listView ? (
            <IconButton
              sx={{ minHeight: "0", flexGrow: 1 }}
              onClick={listToGrid}
            >
              <GridViewOutlinedIcon color="secondary" />
            </IconButton>
          ) : (
            <IconButton
              onClick={listToGrid}
              sx={{ minHeight: "0", flexGrow: 1 }}
            >
              <ReorderIcon color="secondary" />
            </IconButton>
          )}
        </Tooltip>
      </div>
      <Snackbar
        open={open1}
        autoHideDuration={6000}
        onClose={handleClose}
        anchorOrigin={{ vertical: "top", horizontal: "center" }}
      >
        <Alert
          onClose={handleClose}
          message={snackbarMessage}
          severity={snackbarSeverity}
        >
          {snackbarMessage}
        </Alert>
      </Snackbar>

      <Box display="flex" justifyContent="center" sx={{ mt: "12px" }}>
        Shown{" "}
        {location.pathname === "/search" ? searchFiles.length : files.length}
        &nbsp;Documents
      </Box>

      <Box
        display="flex"
        justifyContent="right"
        sx={{ display: "inline", mt: "12px" }}
        pl={80}
        m={0}
        alignItems="center"
      >
        {pdfToggleActive ? (
          <ToggleOnIcon onClick={pdfToggleclick} title="Close Preview" />
        ) : (
          <ToggleOffIcon onClick={pdfToggleclick} title="Open Preview" />
        )}
      </Box>
    </Box>
  );
};

export default MainbarHeader;
