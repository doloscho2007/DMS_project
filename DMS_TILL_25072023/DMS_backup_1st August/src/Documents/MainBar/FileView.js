import * as React from "react";
import { styled } from "@mui/material/styles";
import Table from "@mui/material/Table";
import TableBody from "@mui/material/TableBody";
import TableCell, { tableCellClasses } from "@mui/material/TableCell";
import TableContainer from "@mui/material/TableContainer";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import Paper from "@mui/material/Paper";
import Button from "@mui/material/Button";
import EditIcon from "@mui/icons-material/Edit";
import DeleteIcon from "@mui/icons-material/Delete";
import AddIcon from "@mui/icons-material/Add";
import { useCallback, useState } from "react";
import { useGlobalContext } from "../../Context/DataContext";
import TextField from "@mui/material/TextField";
import Dialog from "@mui/material/Dialog";
import DialogTitle from "@mui/material/DialogTitle";
import DialogContent from "@mui/material/DialogContent";
import DialogActions from "@mui/material/DialogActions";
import { Box, Checkbox, Typography, useTheme } from "@mui/material";
import { IconButton, Grid } from "@material-ui/core";
import { Close as CloseIcon } from "@material-ui/icons";
import PropTypes from "prop-types";
import { DMSAPI } from "../../Service/DMSAPI";
import { useEffect } from "react";
import MuiAlert from "@mui/material/Alert";
import Snackbar from "@material-ui/core/Snackbar";
import { ContextMenu } from "../../styles/styles";

const StyledTableCell = styled(TableCell)(({ theme }) => ({
  [`&.${tableCellClasses.head}`]: {
    backgroundColor: theme.palette.secondary.main,
    color: theme.palette.common.black,
  },
  [`&.${tableCellClasses.body}`]: {
    fontSize: 14,
    padding: "Checkbox",
    columnGap: "10px",
    rowGap: "10px",
    marginTop: 0,
    marginBottom: 0,
  },
}));

const StyledTableRow = styled(TableRow)(({ theme }) => ({
  "&:nth-of-type(odd)": {
    backgroundColor: theme.palette.action.hover,
  },
  "&:last-child td, &:last-child th": {
    border: 0,
  },
  "&& td": {
    paddingTop: 4,
    paddingBottom: 4,
  },
}));

function createData(select, fileName, keyWords, date) {
  return {
    select,
    fileName,
    keyWords,
    date,
  };
}
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

function Alert(props) {
  return <MuiAlert elevation={6} variant="filled" {...props} />;
}

const GridStyleChild = {
  cursor: "pointer",
  height: "180px",
  width: "180px",
};
const overflow = { overflowX: "auto", overflowY: "auto" };
const GridStyleParent = {
  display: "flex",
  flexFlow: "row wrap",
  rowGap: "20px",
  columnGap: "30px",
  justifyContent: "flex-start",
};

export default function FileView() {
  const {
    listView,
    nodeValue,
    files,
    setFiles,
    FilechangeEditState,
    CheckboxchangeEditState,
    handlerChange,
    bookmarkOpen,
    bookmarkFiles,
    setBookmarkFiles,
    checkOutOpen,
    checkOutFiles,
    setCheckOutFiles,
    handleFileRefresh,
  } = useGlobalContext();
  const [snackbarMessage, setSnackbarMessage] = useState("");
  const [snackbarSeverity, setSnackbarSeverity] = useState("success");
  const [selected, setSelected] = React.useState([]);
  const [editOpen, setEditOpen] = React.useState(false);
  const [open1, setOpen] = useState(false);
  const [deleteOpen, setDeleteOpen] = React.useState(false);
  const [addOpen, setAddOpen] = React.useState(false);
  const [newFileId, setFileId] = useState();
  const [checkinOpen, setCheckinOpen] = React.useState(false);
  const [foldName, setFolderName] = React.useState([]);
  const [selectedFiles, setSelectedFiles] = useState([]);
  const [files1, setFiles1] = useState([]);

  const handleCheckinOpen = () => {
    console.log("dialog");
    setCheckinOpen(true);
    console.log(checkinOpen);
  };
  const handleCheckinClose = () => {
    setCheckinOpen(false);
  };

  const handleClose = (event, reason) => {
    if (reason === "clickaway") {
      return;
    }
    setOpen(false);
  };
  const isSelected = (name) => selected.indexOf(name) !== -1;
  const [clicked, setClicked] = useState(false);

  useEffect(() => {
    const handleClick = () => setClicked(false);
    window.addEventListener("click", handleClick);
    return () => {
      window.removeEventListener("click", handleClick);
    };
  }, []);

  const [points, setPoints] = useState({
    x: 0,
    y: 0,
  });
  const handleFileChange = async (e) => {
    var files2 = e.target.files;
    var filesArr = Array.prototype.slice.call(files2);
    setFiles1(filesArr);
    // setFiles1(files2);
    const files = [...e.target.files];
    console.log(files);

    setSelectedFiles(files);
  };

  // const handleFileChange = (e) => {
  //   var files2 = e.target.files;
  //   var filesArr = Array.prototype.slice.call(files2);
  //   setFiles1(filesArr);
  //   const files = [...e.target.files];

  //   setSelectedFiles(files);
  //   files.forEach((file) => {
  //     // setSelectedFiles((prevFiles) => [...prevFiles, file]);

  //     const reader = new FileReader();
  //     reader.onload = (e) => {
  //       const html = (
  //         <div key={file.name}>
  //           <p>
  //             {file.name} &nbsp;&nbsp;&nbsp;
  //             {/* <img
  //               src="assets/remove.png"
  //               data-file={file.name}
  //               className="selFile"
  //             /> */}
  //           </p>
  //         </div>
  //       );

  //       // Update the DOM or React component state to display the uploaded file
  //       // You can append the 'html' variable to your desired element or state.
  //       console.log(html);
  //       console.log(selectedFiles);
  //     };

  //     reader.readAsDataURL(file);
  //   });
  // };
  const getFolderName = (e) => {
    setFolderName = e.target.value;
  };

  // FILE GET METHOD CODE START
  const getFilesMeth = useCallback(
    async (user) => {
      try {
        const response = await DMSAPI.getFiles(nodeValue);
        const data = await response.json();
        setFiles(data);
        // console.log(files);
      } catch (error) {
        console.log(error);
        console.log("mainbar catch");
      }
    },
    [setFiles, nodeValue]
  );

  useEffect(() => {
    getFilesMeth("1234");
  }, [getFilesMeth]);
  // FILE GET METHOD CODE END

  // BOOKMARK GET METHOD CODE START
  const getBookmarkFilesMeth = useCallback(async () => {
    try {
      const response = await DMSAPI.getAllBookmarkedFiles();
      const data = await response.json();
      // console.log(data);
      setBookmarkFiles(data);
      // console.log(bookmarkFiles);
    } catch (error) {
      console.log(error);
      console.log("mainbar catch");
    }
  }, [setBookmarkFiles, bookmarkOpen]);

  useEffect(() => {
    getBookmarkFilesMeth();
  }, [getBookmarkFilesMeth]);
  // BOOKMARK GET METHOD CODE END

  // CHECKOUT GET METHOD CODE START
  const getCheckoutFilesMeth = useCallback(async () => {
    try {
      const response = await DMSAPI.getAllCheckedOutFiles();
      const data = await response.json();
      // console.log(data);
      setCheckOutFiles(data);
      // console.log(checkOutFiles);
    } catch (error) {
      console.log(error);
      console.log("mainbar catch");
    }
  }, [setCheckOutFiles, checkOutOpen]);

  useEffect(() => {
    getCheckoutFilesMeth();
  }, [getCheckoutFilesMeth]);
  // CHECKOUT GET METHOD CODE END

  // MENU BAR FUNCTIONS CODE START

  // CUT METHOD START
  function performCut() {
    localStorage.setItem("perfCutAction", "cut");
    localStorage.setItem("perfCutCopyID", newFileId);
  }
  // CUT METHOD END
  // COPY METHOD START
  function performCopy() {
    localStorage.setItem("perfCutAction", "copy");
    localStorage.setItem("perfCutCopyID", newFileId);
  }
  // COPY METHOD END

  // ADD BOOKMARK API CODE START
  const addBookmarkMeth = async () => {
    const response = await DMSAPI.addBookmark(newFileId);
    if (response.status === 200) {
      setSnackbarMessage("Bookmark added Successfully");
      setSnackbarSeverity("success");
      setOpen(true);
    } else {
      setSnackbarMessage("Bookmark can't be added");
      setSnackbarSeverity("error");
      setOpen(true);
    }
  };
  // ADD BOOKMARK API CODE END

  // DELETE BOOKMARK API CODE START
  const deleteBookmarkMeth = async () => {
    const response = await DMSAPI.deleteBookmark(newFileId);
    if (response.status === 200) {
      setSnackbarMessage("Bookmark removed Successfully");
      setSnackbarSeverity("success");
      setOpen(true);
    } else {
      setSnackbarMessage("Bookmark can't be removed");
      setSnackbarSeverity("error");
      setOpen(true);
    }
  };
  // DELETE BOOKMARK API CODE END

  // ADD CHECKOUT API CODE START
  const addCheckout = async () => {
    const response = await DMSAPI.checkout(newFileId);
    if (response.status === 200) {
      setSnackbarMessage("File Checkout Successfully");
      setSnackbarSeverity("success");
      setOpen(true);
    } else {
      setSnackbarMessage("File can't be Checkout");
      setSnackbarSeverity("error");
      setOpen(true);
    }
  };

  const handleCheckin = async (e) => {
    e.preventDefault();

    var formData = new FormData();
    if (selectedFiles.length === 0) {
      alert("Please choose a file to upload");
      return;
    } else {
      try {
        const response = await DMSAPI.checkin(files1, newFileId);
        const data = await response.json();
        console.log(data.message);
        if (response.status === 200) {
          handleCheckinClose();
          handleFileRefresh();
          setSnackbarMessage("File Checkin Successfully");
          setSnackbarSeverity("success");
          setOpen(true);
        } else if (
          response.status === 500 &&
          data.message ===
            "Error Occurred : Checkin file name not matching with real file name"
        ) {
          setSnackbarMessage(
            "Checkin file name not matching with real file name"
          );
          setSnackbarSeverity("warning");
          setOpen(true);
        }
      } catch (error) {
        console.log(error);
      }
    }
  };

  // ADD CHECKOUT API CODE END

  // MENU BAR FUNCTIONS CODE END

  return (
    <div className="" style={{ width: "100%", height: "100%" }}>
      <div>
        {listView ? (
          <Paper
            sx={{ width: "100%", mb: 2, height: "60%", overflow: "hidden" }}
          >
            <TableContainer
              sx={{ maxHeight: 440 }}
              // component={Paper}
            >
              <Table
                stickyHeader
                sx={{ minWidth: 700, height: "60%" }}
                aria-label="sticky table"
              >
                <TableHead>
                  <TableRow>
                    <StyledTableCell align="center">Select </StyledTableCell>
                    <StyledTableCell align="center">File Name </StyledTableCell>
                    <StyledTableCell align="center">Keywords </StyledTableCell>
                    <StyledTableCell align="center">Created At</StyledTableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                  {(checkOutOpen
                    ? checkOutFiles
                    : bookmarkOpen
                    ? bookmarkFiles
                    : files
                  ).map((row) => {
                    // console.log(row);
                    const {
                      fileId,
                      fileName,
                      createdAt,
                      fileSearch1,
                      fileSearch2,
                      fileSearch3,
                    } = row;
                    const isItemSelected = isSelected(fileName);
                    var createdAtdate = new Date(createdAt);
                    var date = new Date(createdAtdate);
                    var yyyy = date.getFullYear();
                    var mm = date.getMonth() + 1;
                    var dd = date.getDate();
                    if (dd < 10) dd = "0" + dd;
                    if (mm < 10) mm = "0" + mm;
                    var formattedToday = yyyy + "-" + mm + "-" + dd;
                    return (
                      <StyledTableRow
                        key={fileId}
                        hover
                        role="checkbox"
                        tabIndex={-1}
                        selected={isItemSelected}
                        onContextMenu={(e) => {
                          e.preventDefault();
                          setClicked(true);
                          setPoints({
                            x: e.pageX,
                            y: e.pageY,
                          });
                          setFileId(fileId);
                        }}
                      >
                        <StyledTableCell align="center" padding="checkbox">
                          <input
                            type="checkbox"
                            value={fileId}
                            onChange={(e) => {
                              CheckboxchangeEditState(row);
                              handlerChange(e);
                            }}
                          />
                        </StyledTableCell>
                        <StyledTableCell
                          component="th"
                          id={fileId}
                          scope="row"
                          padding="none"
                          onClick={() => {
                            FilechangeEditState(row);
                          }}
                        >
                          {fileName}
                        </StyledTableCell>
                        <StyledTableCell
                          align="center"
                          onClick={() => {
                            FilechangeEditState(row);
                          }}
                        >
                          {fileSearch1}
                          {fileSearch2 === null ? "" : ","}
                          {fileSearch2}
                          {fileSearch3 === null ? "" : ","}
                          {fileSearch3}
                        </StyledTableCell>
                        <StyledTableCell
                          align="right"
                          onClick={() => {
                            FilechangeEditState(row);
                          }}
                        >
                          {formattedToday}
                        </StyledTableCell>
                      </StyledTableRow>
                    );
                  })}
                </TableBody>
              </Table>
            </TableContainer>
          </Paper>
        ) : (
          <Box py={2} pl={3} style={GridStyleParent}>
            {(checkOutOpen
              ? checkOutFiles
              : bookmarkOpen
              ? bookmarkFiles
              : files
            ).map((file) => {
              const { fileId, fileName } = file;
              return (
                <Box
                  style={overflow}
                  onClick={() => {
                    FilechangeEditState(file);
                  }}
                >
                  <Box
                    key={fileId}
                    pt={5}
                    ml={3}
                    sx={{ bgcolor: "text.secondary", color: "red" }}
                    align="center"
                    nowrap
                    style={GridStyleChild}
                  ></Box>
                  <p style={{ wordWrap: "break-word" }}>{fileName}</p>
                </Box>
              );
            })}
          </Box>
        )}
      </div>

      {clicked && (
        <div>
          <ContextMenu top={points.y} left={points.x}>
            <ul>
              <li onClick={performCut}>Cut</li>
              <li onClick={performCopy}>Copy</li>
              <li>Edit</li>
              <li
                className={bookmarkOpen ? "disabled" : ""}
                onClick={addBookmarkMeth}
              >
                Add Bookmark
              </li>
              <li onClick={deleteBookmarkMeth}>Delete Bookmark</li>
              <li
                className={checkOutOpen ? "disabled" : ""}
                onClick={addCheckout}
              >
                Checkout
              </li>
              <li onClick={handleCheckinOpen}>Checkin</li>
            </ul>
          </ContextMenu>
        </div>
      )}

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
      <BootstrapDialog
        onClose={handleCheckinClose}
        aria-labelledby="customized-dialog-title"
        open={checkinOpen}
      >
        <BootstrapDialogTitle
          id="customized-dialog-title"
          onClose={handleCheckinClose}
        >
          UPLOAD FILE FOR CHECKIN
        </BootstrapDialogTitle>
        <DialogContent dividers>
          <Box
            component="form"
            sx={{
              "& .MuiTextField-root": { m: 2, width: "50ch" },
              width: "400px",
              height: "100px",
            }}
            noValidate
            autoComplete="off"
          >
            <span>File Name : </span>{" "}
            <input
              type="file"
              id="myFile"
              name="filename"
              onChange={handleFileChange}
            />
            <br></br>
            <div id="selectedFiles"></div>
            {/* <div id="uploadResponse"
                             style="font-size: 18px; color: red; font-weight: bold; padding-left: 20px;"></div> */}
          </Box>
        </DialogContent>
        <DialogActions>
          <Button
            variant="contained"
            color="neutral"
            onClick={handleCheckinClose}
          >
            CLOSE
          </Button>
          <Button
            variant="contained"
            id="uploadFileButton"
            color="success"
            onClick={handleCheckin}
          >
            UPLOAD
          </Button>
        </DialogActions>
      </BootstrapDialog>
    </div>
  );
}
