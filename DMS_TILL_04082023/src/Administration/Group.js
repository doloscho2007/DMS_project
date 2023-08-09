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
  groupId,
  groupName,
  groupDescription,
  groupStatus,
  editDelete
) {
  return { groupId, groupName, groupDescription, groupStatus, editDelete };
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
export default function Group() {
  const [snackbarMessage, setSnackbarMessage] = useState("");
  const [snackbarSeverity, setSnackbarSeverity] = useState("success");
  const [open1, setOpen] = useState(false);
  const [editOpen, setEditOpen] = React.useState(false);
  const [deleteOpen, setDeleteOpen] = React.useState(false);
  const [addOpen, setAddOpen] = React.useState(false);

  const handleClickEditOpen = () => {
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

  const valueGet = (row) => {
    setUserGroupName(row.groupName);
    setUserGroupDesc(row.groupDescription);
    setUserGroupStatus(row.groupStatus);
    setUserGroupId(row.groupId);
  };

  const [userGroupDetails, setUserGroupDetails] = useState([]);

  // EDIT USER GROUP DATA START
  const [userGroupId, setUserGroupId] = useState("");
  const [userGroupName, setUserGroupName] = useState("");
  const [userGroupDesc, setUserGroupDesc] = useState("");
  const [userGroupStatus, setUserGroupStatus] = useState("");
  const handleUserGroupName = (e) => {
    setUserGroupName(e.target.value);
  };

  const handleUserGroupDesc = (e) => {
    setUserGroupDesc(e.target.value);
  };

  const handleUserGroupStatus = (e) => {
    setUserGroupStatus(e.target.value);
  };

  // UPDATE METHOD
  const updateUserGroup = async () => {
    const resp = await DMSAPI.updateUserGroup(
      userGroupId,
      userGroupName,
      userGroupStatus,
      userGroupDesc
    );
    const data = await resp.json();
    console.log(data);
    if (resp.status === 200) {
      handleEditClose();
      setSnackbarMessage("User group Updated Successfully");
      setSnackbarSeverity("success");
      setOpen(true);
      console.log("User group Updated Successfully");
      
    }
    else{
      handleEditClose();
      setSnackbarMessage("User group can't be updated due to server error");
      setSnackbarSeverity("error");
      setOpen(true);
      console.log("User group can't be updated");
    }
  };

  // EDIT USER GROUP DATA END

  // ADD USER GROUP DATA START
  const [addUserGroupName, setAddUserGroupName] = useState("");
  const [addUserGroupDesc, setAddUserGroupDesc] = useState("");
  const [addUserGroupStatus, setAddUserGroupStatus] = useState("IN");
  const handleAddUserGroupName = (e) => {
    setAddUserGroupName(e.target.value);
  };

  const handleAddUserGroupDesc = (e) => {
    setAddUserGroupDesc(e.target.value);
  };

  const handleAddUserGroupStatus = (e) => {
    setAddUserGroupStatus(e.target.value);
  };

  // CREATE METHOD
  const createUserGroup = async () => {
    console.log(addUserGroupName, addUserGroupStatus, addUserGroupDesc);
    const resp = await DMSAPI.createUserGroup(
      addUserGroupName,
      addUserGroupStatus,
      addUserGroupDesc
    );
    const data = await resp.json();
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

  // ADD USER GROUP DATA END

  // GET USER GROUP METHOD
  const getUserGroup = async () => {
    const resp = await DMSAPI.getAllUserGroup();
    const data = await resp.json();
    console.log(data);
    setUserGroupDetails(data);
  };

  // DELETE USER GROUP METHOD
  const deleteUserGroup = async () => {
    console.log(userGroupId);
    const resp = await DMSAPI.deleteUserGroup(userGroupId);
    const data = await resp.json();
    console.log(data);
    if (resp.status === 200) {
      handleDeleteClose();
      setSnackbarMessage("User group Deleted Successfully");
      setSnackbarSeverity("success");
      setOpen(true);
      console.log("User group Deleted Successfully");
    }
    else{
      handleDeleteClose();
      setSnackbarMessage("User group can't be deleted due to server error");
      setSnackbarSeverity("error");
      setOpen(true);
      console.log("User group can't be deleted");
    }
  };

  useEffect(() => {
    getUserGroup();
  }, []);

  return (
    <>
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
            Add Group
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
              ADD USER GROUP DETAILS
            </BootstrapDialogTitle>
            <DialogContent dividers>
              <p>
                <span style={{ display: "inline-block", width: "100px" }}>
                  <label>
                    Group Name
                    <>
                      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    </>
                    :
                  </label>
                </span>
                <span>
                  <input
                    type="text"
                    id="group_name"
                    style={{ width: "165px", height: "20px" }}
                    name="groupName"
                    value={addUserGroupName}
                    onChange={handleAddUserGroupName}
                  />
                </span>
              </p>
              <p>
                <span style={{ display: "inline-block", width: "100px" }}>
                  <label>
                    Group Description<>&nbsp;&nbsp;</>:
                  </label>
                </span>
                <span>
                  <input
                    type="text"
                    id="group_desc"
                    style={{ width: "165px", height: "20px" }}
                    name="groupDesc"
                    value={addUserGroupDesc}
                    onChange={handleAddUserGroupDesc}
                  />
                </span>
              </p>
              <p>
                <span style={{ display: "inline-block", width: "100px" }}>
                  <label>
                    Group Status
                    <>
                      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    </>
                    :
                  </label>
                </span>
                <span>
                  &nbsp;&nbsp;
                  <select
                    name="status"
                    id="status"
                    value={addUserGroupStatus}
                    onChange={handleAddUserGroupStatus}
                  >
                    <option value="IN">Active</option>
                    <option value="InActive">Inactive</option>
                  </select>
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
              <Button
                variant="contained"
                color="success"
                onClick={createUserGroup}
              >
                ADD
              </Button>
            </DialogActions>
          </BootstrapDialog>
        </div>
      </div>
      <TableContainer component={Paper}>
        <Table sx={{ minWidth: 700 }} aria-label="customized table">
          <TableHead>
            <TableRow>
              <StyledTableCell align="center">Group ID</StyledTableCell>
              <StyledTableCell align="center">Group Name</StyledTableCell>
              <StyledTableCell align="center">
                Group Description
              </StyledTableCell>
              <StyledTableCell align="center">Group Status</StyledTableCell>
              <StyledTableCell align="center">Action</StyledTableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {userGroupDetails.map((row) => (
              <StyledTableRow key={row.userName}>
                <StyledTableCell align="center" component="th" scope="row">
                  {row.groupId}
                </StyledTableCell>
                <StyledTableCell align="center">
                  {row.groupName}
                </StyledTableCell>
                <StyledTableCell align="center">
                  {row.groupDescription}
                </StyledTableCell>
                <StyledTableCell align="center">
                  {row.groupStatus}
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
                    onClick={() => {
                      handleClickDeleteOpen();
                      valueGet(row);
                    }}
                    style={{ cursor: "pointer" }}
                  />
                </StyledTableCell>
              </StyledTableRow>
            ))}
          </TableBody>

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
              EDIT USER GROUP DETAILS
            </BootstrapDialogTitle>
            <DialogContent dividers>
              <p>
                <span style={{ display: "inline-block", width: "100px" }}>
                  <label>
                    {" "}
                    Group Name
                    <>
                      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    </>
                    :
                  </label>
                </span>
                <span>
                  <input
                    type="text"
                    id="group_name"
                    style={{ width: "165px", height: "20px" }}
                    name="groupName"
                    value={userGroupName}
                    onChange={handleUserGroupName}
                  />
                </span>
              </p>
              <p>
                <span style={{ display: "inline-block", width: "100px" }}>
                  <label>
                    Group Description<>&nbsp;&nbsp;</>:
                  </label>
                </span>
                <span>
                  <input
                    type="text"
                    id="group_desc"
                    style={{ width: "165px", height: "20px" }}
                    name="groupDesc"
                    value={userGroupDesc}
                    onChange={handleUserGroupDesc}
                  />
                </span>
              </p>
              <p>
                <span style={{ display: "inline-block", width: "100px" }}>
                  <label>
                    Group Status
                    <>
                      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    </>
                    :
                  </label>
                </span>
                <span>
                  &nbsp;&nbsp;
                  <select
                    name="status"
                    id="status"
                    value={userGroupStatus}
                    onChange={handleUserGroupStatus}
                  >
                    <option value="IN">Active</option>
                    <option value="InActive">Inactive</option>
                  </select>
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
                onClick={updateUserGroup}
              >
                UPDATE
              </Button>
            </DialogActions>
          </BootstrapDialog>
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
            <p style={{ width: "250px", textAlign: "center" }}>
              Are you sure you want to delete?
            </p>
            <DialogActions>
              <Button
                variant="contained"
                color="neutral"
                onClick={deleteUserGroup}
              >
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
