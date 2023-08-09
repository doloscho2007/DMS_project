import { Box, IconButton, useTheme, Typography } from "@mui/material";
import { useContext } from "react";
import { ColorModeContext, tokens } from "../theme";
import Button from "@mui/material/Button";
import * as React from "react";
import { NavLink } from "react-router-dom";

import Stack from "@mui/material/Stack";
import DashboardCustomizeOutlinedIcon from "@mui/icons-material/DashboardCustomizeOutlined";
import DocumentScannerOutlinedIcon from "@mui/icons-material/DocumentScannerOutlined";
import SearchOutlinedIcon from "@mui/icons-material/SearchOutlined";
import AdminPanelSettingsOutlinedIcon from "@mui/icons-material/AdminPanelSettingsOutlined";
import { makeStyles } from "@mui/styles";

const useStyles = makeStyles({
  button: {
    "&.active": {
      background: "#858585",
    },
  },
});

export default function TabHeader() {
  const theme = useTheme();
  const colors = tokens(theme.palette.mode);
  const classes = useStyles();
  const user = JSON.parse(localStorage.getItem("user"));
  // console.log(user.userName);

  return (
    <div style={{ paddingBottom: 10 }}>
      <Stack
        direction="row"
        spacing={2}
        paddingLeft={2}
        sx={{ borderBottom: "0.05rem solid #666666" }}
      >
        <Button
          className={classes.button}
          component={NavLink}
          style={{
            borderBottomRightRadius: 0,
            borderBottomLeftRadius: 0,
          }}
          variant="contained"
          to="/dashboard"
          startIcon={<DashboardCustomizeOutlinedIcon />}
          color={"secondary"}
        >
          DASHBOARD
        </Button>
        <Button
          className={classes.button}
          component={NavLink}
          style={{
            borderBottomRightRadius: 0,
            borderBottomLeftRadius: 0,
          }}
          variant="contained"
          to="/documents"
          startIcon={<DocumentScannerOutlinedIcon />}
          color={"secondary"}
        >
          DOCUMENTS
        </Button>
        <Button
          className={classes.button}
          component={NavLink}
          style={{
            borderBottomRightRadius: 0,
            borderBottomLeftRadius: 0,
          }}
          variant="contained"
          to="/search"
          startIcon={<SearchOutlinedIcon />}
          color={"secondary"}
        >
          SEARCH
        </Button>
        {user.userName === "Admin" ? (
          <Button
            className={classes.button}
            component={NavLink}
            style={{
              borderBottomRightRadius: 0,
              borderBottomLeftRadius: 0,
            }}
            variant="contained"
            to="/administration"
            startIcon={<AdminPanelSettingsOutlinedIcon />}
            color={"secondary"}
          >
            ADMINISTRATION
          </Button>
        ) : ""}
      </Stack>
    </div>
  );
}
