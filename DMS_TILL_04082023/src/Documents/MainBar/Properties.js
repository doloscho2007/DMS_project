import React from "react";

import { useGlobalContext } from "../../Context/DataContext";
import Button from "@mui/material/Button";
import { makeStyles } from "@mui/styles";
import { Box, IconButton, useTheme, Typography } from "@mui/material";
import { useContext } from "react";
import { ColorModeContext, tokens } from "../../theme";
import Grid from "@mui/material/Grid";
import Paper from "@mui/material/Paper";
import { styled } from "@mui/material/styles";
import "../../index.css";
import { useState } from "react";
import { useEffect } from "react";
import { DMSAPI } from "../../Service/DMSAPI";
import Snackbar from "@material-ui/core/Snackbar";
import MuiAlert from "@mui/material/Alert";

const useStyles = makeStyles({
  button: {
    "&.active": {
      background: "#858585",
    },
  },
});

const Item = styled(Paper)(({ theme }) => ({
  backgroundColor: theme.palette.mode === "dark" ? "#1A2027" : "#fff",
  ...theme.typography.body2,
  padding: theme.spacing(1),
  textAlign: "center",
  color: theme.palette.text.secondary,
}));

function Alert(props) {
  return <MuiAlert elevation={6} variant="filled" {...props} />;
}

const Properties = () => {
  const { editForm, propUp, handleRefresh, handlefileUpdate, checkOutOpen } =
    useGlobalContext();

  const {
    fileId,
    fileName,
    createdAt,
    fileSearch1,
    fileSearch2,
    fileSearch3,
    treePath,
    createdOn,
    fileSizeFd,
  } = editForm;
  const [open1, setOpen] = useState(false);
  const [snackbarMessage, setSnackbarMessage] = useState("");
  const [snackbarSeverity, setSnackbarSeverity] = useState("success");
  // console.log("created At",createdAt)
  var createdAtdate = new Date(createdAt);

  var date = new Date(createdAtdate);
  // gives you your current date
  var yyyy = date.getFullYear();
  var mm = date.getMonth() + 1; // Months start at 0!
  var dd = date.getDate();

  if (dd < 10) dd = "0" + dd;
  if (mm < 10) mm = "0" + mm;

  var formattedToday = yyyy + "-" + mm + "-" + dd;

  const valueGet = (editForm) => {
    setFileName(fileName);
    setFileDate(formattedToday);
    setKeyword1(fileSearch1 === null ? " " : fileSearch1);
    setKeyword2(fileSearch2 === null ? " " : fileSearch2);
    setKeyword3(fileSearch3 === null ? " " : fileSearch3);
  };

  const [fileName1, setFileName] = useState();
  const [fileDate, setFileDate] = useState();
  const [keyword1, setKeyword1] = useState(null);
  const [keyword2, setKeyword2] = useState(null);
  const [keyword3, setKeyword3] = useState(null);
  // console.log(keyword1, keyword2, keyword3);

  const handleFileName = (e) => {
    setFileName(e.target.value);
  };
  const handleFileDate = (e) => {
    setFileDate(e.target.value);
    console.log("file date", fileDate);
  };
  const handleKeyword1 = (e) => {
    setKeyword1(e.target.value);
  };
  const handleKeyword2 = (e) => {
    setKeyword2(e.target.value);
  };
  const handleKeyword3 = (e) => {
    setKeyword3(e.target.value);
  };

  useEffect(() => {
    valueGet(editForm);
  }, [editForm]);

  const theme = useTheme();
  const colors = tokens(theme.palette.mode);
  const classes = useStyles();
  const handleClose = (event, reason) => {
    if (reason === "clickaway") {
      return;
    }
    setOpen(false);
  };

  // API INVOKING METHOD CODE START
  const updateFileName = async () => {
    const resp = await DMSAPI.updateFileName(
      fileId,
      fileName1,
      keyword1,
      keyword2,
      keyword3,
      fileDate
    );
    const data = await resp.json();
    handleRefresh();
    setSnackbarMessage("File details updated successfully");
    setSnackbarSeverity("success");
    setOpen(true);
  };

  // API INVOKING METHOD CODE END

  return (
    <div style={{ width: "100%", height: "10rem", alignItems: "center" }}>
      <div
        style={{
          display: "flex",
          width: "100%",
          alignItems: "center",
          marginBottom: "5px",
        }}
      >
        <span style={{ flex: "1", height: "20px" }}>File Name :</span>
        <input
          type="text"
          className="edit-profile"
          id="editFileName"
          style={{ flex: "1", height: "20px", height: "20px" }}
          name="File name"
          value={fileName1}
          onChange={handleFileName}
        />
        <span
          style={{
            flex: "1",
            height: "20px",
            paddingLeft: propUp ? "100px" : "2px",
          }}
        >
          File Date :
        </span>
        <input
          type="date"
          id="createdDate2"
          style={{
            flex: "1",
            height: "20px",
            marginLeft: propUp ? "50px" : "20px",
          }}
          name="File Date"
          value={fileDate}
          onChange={handleFileDate}
        />
      </div>

      <div
        style={{ display: "flex", alignItems: "center", marginBottom: "5px" }}
      >
        <span style={{ flex: "1", height: "20px" }}>Keyword 1 :</span>
        <input
          type="text"
          id="fileSearchParam1"
          style={{ flex: "1", height: "20px" }}
          name="Keyword1"
          value={keyword1}
          onChange={handleKeyword1}
        />
        <span
          style={{
            flex: "1",
            height: "20px",
            paddingLeft: propUp ? "100px" : "2px",
          }}
        >
          Keyword 2 :
        </span>
        <input
          type="text"
          id="fileSearchParam2"
          style={{
            flex: "1",
            height: "20px",
            marginLeft: propUp ? "50px" : "20px",
          }}
          name="Keyword2"
          value={keyword2}
          onChange={handleKeyword2}
        />
      </div>

      <div
        style={{ display: "flex", alignItems: "center", marginBottom: "5px" }}
      >
        <span style={{ flex: "1", height: "20px" }}>Keyword 3 :</span>
        <input
          type="text"
          id="fileSearchParam3"
          style={{ flex: "1", height: "20px", height: "20px" }}
          name="Keyword3"
          value={keyword3}
          onChange={handleKeyword3}
        />
        <span
          style={{
            flex: "1",
            height: "20px",
            paddingLeft: propUp ? "100px" : "2px",
          }}
        >
          File Size :
        </span>
        <span
          style={{
            flex: "1",
            height: "20px",
            marginLeft: propUp ? "50px" : "20px",
          }}
        >
          {(fileSizeFd / 1e6).toFixed(3)}MB
        </span>
      </div>

      <div
        style={{
          display: "flex",
          alignItems: "center",
          marginBottom: "5px",
          whiteSpace: "nowrap",
        }}
      >
        <span style={{ flex: "0.25", height: "20px", margin: "0" }}>
          Upload Date :
        </span>
        <span style={{ flex: "0.25", height: "20px", margin: "0" }}>
          {createdOn}
        </span>
      </div>

      <div
        style={{ display: "flex", alignItems: "center", marginBottom: "5px" }}
      >
        <span style={{ flex: "0.25", height: "20px" }}>Folder :</span>
        <span style={{ flex: "0.25", height: "20px" }}>{treePath}</span>
      </div>

      <div
        style={{ fontSize: "18px", color: "red", paddingLeft: "20px" }}
      ></div>

      <div
        className={`modal-footer ${propUp ? "center-content" : ""}`}
        style={{ padding: "0px 15rem 5prem 0rem", justifyContent: "center" }}
      >
        <Button
          variant="contained"
          to="/"
          color="secondary"
          onClick={updateFileName}
          disabled={checkOutOpen ? true : false}
        >
          UPDATE
        </Button>
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
    </div>
  );
};

export default Properties;
