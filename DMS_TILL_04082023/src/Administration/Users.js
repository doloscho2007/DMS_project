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
import { useState } from "react";
import { useGlobalContext } from "../Context/DataContext";
import TextField from "@mui/material/TextField";
import Dialog from "@mui/material/Dialog";
import DialogTitle from "@mui/material/DialogTitle";
import DialogContent from "@mui/material/DialogContent";
import DialogActions from "@mui/material/DialogActions";
import { Box, Typography, useTheme } from "@mui/material";
import { IconButton, Grid } from "@material-ui/core";
import { Close as CloseIcon } from "@material-ui/icons";
import PropTypes from "prop-types";
import { DMSAPI } from "../Service/DMSAPI";
import { useEffect } from "react";
import { makeStyles } from "@material-ui/core";
import MuiAlert from "@mui/material/Alert";
import Snackbar from "@material-ui/core/Snackbar";

const StyledTableCell = styled(TableCell)(({ theme }) => ({
  [`&.${tableCellClasses.head}`]: {
    backgroundColor: theme.palette.secondary.main,
    color: theme.palette.common.black,
  },
  [`&.${tableCellClasses.body}`]: {
    fontSize: 14,
  },
}));

const StyledTableRow = styled(TableRow)(({ theme }) => ({
  "&:nth-of-type(odd)": {
    backgroundColor: theme.palette.action.hover,
  },
  "&:last-child td, &:last-child th": {
    border: 0,
  },
}));

function createData(
  userName,
  userStatus,
  loginId,
  passCode,
  systemIp,
  token,
  userGroup,
  editDelete
) {
  return {
    userName,
    userStatus,
    loginId,
    passCode,
    systemIp,
    token,
    userGroup,
    editDelete,
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
export default function Users() {
  const [snackbarMessage, setSnackbarMessage] = useState("");
  const [snackbarSeverity, setSnackbarSeverity] = useState("success");
  const [editOpen, setEditOpen] = React.useState(false);
  const [open1, setOpen] = useState(false);
  const [deleteOpen, setDeleteOpen] = React.useState(false);
  const [addOpen, setAddOpen] = React.useState(false);

  const valueGet = (row) => {
    console.log(row);
    setUserId(row.loginId);
    setUserName(row.userName);
    setUserGroup(row.userGroup);
    setUserStatus(row.empcurrentStatus);
    setUserPass(row.passCode);
    setUserConfirmPass(row.passCode);
  };

  const handleClickEditOpen = (e) => {
    console.log("edit");
    setEditOpen(true);
  };

  const handleEditClose = () => {
    setEditOpen(false);
  };

  const handleClickDeleteOpen = () => {
    console.log("edit");
    setDeleteOpen(true);
  };

  const handleDeleteClose = () => {
    setDeleteOpen(false);
  };

  const handleClickAddOpen = () => {
    console.log("edit");
    setAddOpen(true);
  };

  const handleAddClose = () => {
    setAddOpen(false);
  };
  const handleClose = (event, reason) => {
    if (reason === "clickaway") {
      return;
    }
    setOpen(false);
  };
  const [userDetails, setUserDetails] = useState([]);

  // EDIT USER DETAILS CODE START
  const [userId, setUserId] = useState("");
  const [userName, setUserName] = useState("");
  const [userGroup, setUserGroup] = useState("");
  const [userStatus, setUserStatus] = useState("");
  const [userPass, setUserPass] = useState("");
  const [userConfirmPass, setUserConfirmPass] = useState("");

  const handleUserName = (e) => {
    setUserName(e.target.value);
  };
  const handleUserGroup = (e) => {
    setUserGroup(e.target.value);
  };
  const handleUserStatus = (e) => {
    setUserStatus(e.target.value);
  };
  const handleUserPass = (e) => {
    setUserPass(e.target.value);
  };
  const handleUserConfirmPass = (e) => {
    setUserConfirmPass(e.target.value);
  };

  // EDIT USER METHOD
  const updateUserDetails = async () => {
    const resp = await DMSAPI.updateUserDetails(
      userId,
      userName,
      userStatus,
      userGroup,
      userPass
    );
    const data = await resp.json();
    console.log(resp);
    console.log(data);
    if (resp.status === 200) {
      handleEditClose();
      setSnackbarMessage("User Updated Successfully");
      setSnackbarSeverity("success");
      setOpen(true);
      console.log("User Updated Successfully");
      
    }
    else{
      handleEditClose();
      setSnackbarMessage("User can't be updated due to server error");
      setSnackbarSeverity("error");
      setOpen(true);
      console.log("User can't be updated");
    }
  };
  // EDIT USER DETAILS CODE END

  // GET USER METHOD
  const getuserDetails = async () => {
    const resp = await DMSAPI.getUsers();
    const data = await resp.json();
    console.log(data);
    setUserDetails(data);
  };

  // DELETE USER METHOD
  const deleteUser = async () => {
    const resp = await DMSAPI.deleteUser(userId);
    const data = await resp.json();
    console.log(data);
    console.log(resp);
    if (resp.status === 200) {
      handleDeleteClose();
      setSnackbarMessage("User Deleted Successfully");
      setSnackbarSeverity("success");
      setOpen(true);
      console.log("User Deleted Successfully");
    }
    else{
      handleDeleteClose();
      setSnackbarMessage("User can't be deleted due to server error");
      setSnackbarSeverity("error");
      setOpen(true);
      console.log("User can't be deleted");
    }
  };

  // ADD USER DETAILS CODE START
  const [addUserName, setAddUserName] = useState("");
  const [addUserGroup, setAddUserGroup] = useState("");
  const [addUserStatus, setAddUserStatus] = useState("IN");
  const [addUserPass, setAddUserPass] = useState("");
  const [addUserConfirmPass, setAddUserConfirmPass] = useState("");

  const handleAddUserName = (e) => {
    setAddUserName(e.target.value);
  };
  const handleAddUserGroup = (e) => {
    setAddUserGroup(e.target.value);
  };
  const handleAddUserStatus = (e) => {
    setAddUserStatus(e.target.value);
  };
  const handleAddUserPass = (e) => {
    setAddUserPass(e.target.value);
  };
  const handleAddUserConfirmPass = (e) => {
    setAddUserConfirmPass(e.target.value);
  };

  // ADD USER METHOD
  const addUser = async () => {
    console.log(addUserName, addUserStatus, addUserGroup, addUserPass);
    const resp = await DMSAPI.addUserDetails(
      addUserName,
      addUserStatus,
      addUserGroup,
      addUserPass
    );
    const data = await resp.json();
    console.log(resp);
    console.log(data);
    if (resp.status === 200) {
      handleAddClose();
      setSnackbarMessage("User Added Successfully");
      setSnackbarSeverity("success");
      setOpen(true);
      console.log("User Added Successfully");
      
    }
    else{
      handleAddClose();
      setSnackbarMessage("User can't be added due to server error");
      setSnackbarSeverity("error");
      setOpen(true);
      console.log("User can't be added");
    }

  };

  // ADD USER DETAILS CODE END

  useEffect(() => {
    getuserDetails();
  }, []);

  return (
    <>
      {/* ADD USER BUTTON AND MODAL CODE START */}
      <div style={{ paddingTop: "20px" }}>
        <div style={{ display: "flex", justifyContent: "flex-start" }}>
          <Button
            style={{
              marginBottom: 10,
            }}
            variant="contained"
            to="/"
            color={"secondary"}
            onClick={handleClickAddOpen}
          >
            <AddIcon />
            Add User
          </Button>
          <BootstrapDialog
            onClose={handleAddClose}
            aria-labelledby="customized-dialog-title"
            open={addOpen}
            sx={{
              display: "flex",
              justifyContent: "space-between",
              alignItems: "center",
            }}
          >
            <BootstrapDialogTitle
              id="customized-dialog-title"
              onClose={handleAddClose}
            >
              ADD USER DETAILS
            </BootstrapDialogTitle>
            <DialogContent dividers>
              <p>
                <span style={{ display: "inline-block", width: "100px" }}>
                  <label>
                    {" "}
                    User Name
                    <>
                      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    </>
                    :
                  </label>
                </span>
                <span>
                  <input
                    type="text"
                    id="user_name"
                    style={{ width: "165px", height: "20px" }}
                    name="userName"
                    value={addUserName}
                    onChange={handleAddUserName}
                  />
                </span>
              </p>
              <p>
                <span style={{ display: "inline-block", width: "100px" }}>
                  <label>
                    {" "}
                    User Group<>&nbsp;&nbsp;</>:
                  </label>
                </span>
                <span>
                  <input
                    type="text"
                    id="user_group"
                    style={{ width: "165px", height: "20px" }}
                    name="userGroup"
                    value={addUserGroup}
                    onChange={handleAddUserGroup}
                  />
                </span>
              </p>
              <p>
                <span style={{ display: "inline-block", width: "100px" }}>
                  <label>
                    User Status<>&nbsp;&nbsp;</>:
                  </label>
                </span>
                <span>
                  &nbsp;&nbsp;
                  <select
                    name="status"
                    id="status"
                    value={addUserStatus}
                    onChange={handleAddUserStatus}
                  >
                    <option value="IN">Active</option>
                    <option value="InActive">Inactive</option>
                  </select>
                </span>
              </p>
              <p>
                <span style={{ display: "inline-block", width: "100px" }}>
                  <label>
                    Passcode<>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</>:
                  </label>
                </span>
                <span>
                  <input
                    type="password"
                    id="pass_code"
                    style={{ width: "165px", height: "20px" }}
                    name="passCode"
                    value={addUserPass}
                    onChange={handleAddUserPass}
                  />
                </span>
              </p>
              <p>
                <span style={{ display: "inline-block", width: "100px" }}>
                  <label>
                    Confirm Passcode
                    <>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</>:
                  </label>
                </span>
                <span>
                  <input
                    type="password"
                    id="confirm_pass_code"
                    style={{ width: "165px", height: "20px" }}
                    name="confirmPassCode"
                    value={addUserConfirmPass}
                    onChange={handleAddUserConfirmPass}
                  />
                </span>
              </p>
            </DialogContent>
            <DialogActions>
              <Button
                variant="contained"
                color="neutral"
                onClick={handleAddClose}
              >
                CLOSE
              </Button>
              <Button variant="contained" color="success" onClick={addUser}>
                ADD
              </Button>
            </DialogActions>
          </BootstrapDialog>
        </div>
      </div>
      {/* ADD USER BUTTON AND MODAL CODE END */}

      <TableContainer component={Paper}>
        <Table sx={{ minWidth: 700 }} aria-label="customized table">
          <TableHead>
            <TableRow>
              <StyledTableCell align="center">LoginID</StyledTableCell>
              <StyledTableCell>Username</StyledTableCell>
              <StyledTableCell align="center">User Status</StyledTableCell>
              <StyledTableCell align="center">PassCode</StyledTableCell>
              <StyledTableCell align="center">SystemIp</StyledTableCell>
              <StyledTableCell align="center">User Group</StyledTableCell>
              <StyledTableCell align="center">Action</StyledTableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {userDetails.map((row) => (
              <StyledTableRow key={row.userName}>
                <StyledTableCell align="center">{row.loginId}</StyledTableCell>
                <StyledTableCell component="th" scope="row">
                  {row.userName}
                </StyledTableCell>
                <StyledTableCell align="center">
                  {row.empcurrentStatus}
                </StyledTableCell>
                <StyledTableCell align="center">{row.passCode}</StyledTableCell>
                <StyledTableCell align="center">{row.systemIp}</StyledTableCell>
                <StyledTableCell align="center">
                  {row.userGroup}
                </StyledTableCell>
                <StyledTableCell align="center">
                  <EditIcon
                    onClick={() => {
                      handleClickEditOpen();
                      valueGet(row);
                    }}
                    style={{ cursor: "pointer" }}
                  />
                  &nbsp;&nbsp;
                  <DeleteIcon
                    style={{ cursor: "pointer" }}
                    onClick={() => {
                      handleClickDeleteOpen();
                      valueGet(row);
                    }}
                  />
                </StyledTableCell>
              </StyledTableRow>
            ))}
          </TableBody>

          {/* EDIT USER MODAL CODE START */}
          <BootstrapDialog
            onClose={handleEditClose}
            aria-labelledby="customized-dialog-title"
            open={editOpen}
            sx={{
              display: "flex",
              justifyContent: "space-between",
              alignItems: "center",
            }}
          >
            <BootstrapDialogTitle
              id="customized-dialog-title"
              onClose={handleEditClose}
            >
              EDIT USER DETAILS
            </BootstrapDialogTitle>
            <DialogContent dividers>
              <p>
                <span style={{ display: "inline-block", width: "100px" }}>
                  <label>
                    User Name<>&nbsp;&nbsp;</>:
                  </label>
                </span>
                <span>
                  <input
                    type="text"
                    id="user_name"
                    style={{ width: "165px", height: "20px" }}
                    name="userName"
                    value={userName}
                    onChange={handleUserName}
                  />
                </span>
              </p>
              <p>
                <span style={{ display: "inline-block", width: "100px" }}>
                  <label>
                    {" "}
                    User Group<>&nbsp;&nbsp;</>:
                  </label>
                </span>
                <span>
                  <input
                    type="text"
                    id="user_group"
                    style={{ width: "165px", height: "20px" }}
                    name="userGroup"
                    value={userGroup}
                    onChange={handleUserGroup}
                  />
                </span>
              </p>
              <p>
                <span style={{ display: "inline-block", width: "100px" }}>
                  <label>
                    User Status<>&nbsp;&nbsp;</>:
                  </label>
                </span>
                <span>
                  &nbsp;&nbsp;
                  <select
                    name="status"
                    id="status"
                    value={userStatus}
                    onChange={handleUserStatus}
                  >
                    <option value="IN">Active</option>
                    <option value="InActive">Inactive</option>
                  </select>
                </span>
              </p>
              <p>
                <span style={{ display: "inline-block", width: "100px" }}>
                  <label>
                    Passcode<>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</>:
                  </label>
                </span>
                <span>
                  <input
                    type="password"
                    id="pass_code"
                    style={{ width: "165px", height: "20px" }}
                    name="passCode"
                    value={userPass}
                    onChange={handleUserPass}
                  />
                </span>
              </p>
              <p>
                <span style={{ display: "inline-block", width: "100px" }}>
                  <label>
                    Confirm Passcode
                    <>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</>:
                  </label>
                </span>
                <span>
                  <input
                    type="password"
                    id="confirm_pass_code"
                    style={{ width: "165px", height: "20px" }}
                    name="confirmPassCode"
                    value={userConfirmPass}
                    onChange={handleUserConfirmPass}
                  />
                </span>
              </p>
            </DialogContent>
            <DialogActions>
              <Button
                variant="contained"
                color="neutral"
                onClick={handleEditClose}
              >
                CLOSE
              </Button>
              <Button
                variant="contained"
                color="success"
                onClick={updateUserDetails}
              >
                UPDATE
              </Button>
            </DialogActions>
          </BootstrapDialog>
          {/* EDIT USER MODAL CODE END */}
          {/* DELETE USER MODAL CODE START */}
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
              <Button variant="contained" color="neutral" onClick={deleteUser}>
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
          {/* DELETE USER MODAL CODE END */}
        </Table>
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
      </TableContainer>
    </>
  );
}
