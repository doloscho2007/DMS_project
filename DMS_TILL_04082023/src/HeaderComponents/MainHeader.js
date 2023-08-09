import React from "react";
import { Box, IconButton, useTheme, Typography } from "@mui/material";
import { useContext } from "react";
import { ColorModeContext, tokens } from "../theme";
import InputBase from "@mui/material/InputBase";
import LightModeOutlinedIcon from "@mui/icons-material/LightModeOutlined";
import DarkModeOutlinedIcon from "@mui/icons-material/DarkModeOutlined";
import DocumentScannerOutlinedIcon from "@mui/icons-material/DocumentScannerOutlined";
import Groups3OutlinedIcon from "@mui/icons-material/Groups3Outlined";
import LogoutOutlinedIcon from "@mui/icons-material/LogoutOutlined";

import logo from "../images/logo.png";
import logo_black from "../images/logo_black.png";
import { NavLink, useNavigate } from "react-router-dom";
import { useAuth } from "../utils/Auth";
import "../index.css";
const MainHeader = () => {
  const theme = useTheme();
  const colors = tokens(theme.palette.mode);
  const colorMode = useContext(ColorModeContext);
  const auth = useAuth();
  const navigate = useNavigate();

  const loggedout = () => {
    navigate("/");
    auth.logout();
  };

  const reloadMeth = () => {
     window.location.reload(false);
     navigate("/documents");
  };
  return (
    <Box display="flex" justifyContent="space-between" p={2}>
      <Box
        textAlign="center"
        borderRadius="3px"
        onClick={reloadMeth}
        sx={{ cursor: "pointer" }}
      >
        <Typography
          variant="h2"
          color={colors.grey[100]}
          fontWeight="bold"
          sx={{ m: "0 0 0 0" }}
        >
          {theme.palette.mode === "dark" ? (
            <img
              className="logo"
              src={logo}
              alt="ARITS"
              style={{ width: "25px" }}
            />
          ) : (
            <img
              className="logo"
              src={logo_black}
              alt="ARITS"
              style={{ width: "25px" }}
            />
          )}
          ARITS
        </Typography>
        {/* <Typography variant="h5" color={colors.greenAccent[500]}>
                  Document Management System
                </Typography> */}
      </Box>

      {/* ICONS */}
      <Box display="flex">
        <IconButton onClick={colorMode.toggleColorMode}>
          {theme.palette.mode === "dark" ? (
            <DarkModeOutlinedIcon />
          ) : (
            <LightModeOutlinedIcon />
          )}
        </IconButton>
        <IconButton>
          <DocumentScannerOutlinedIcon />
        </IconButton>
        <IconButton>
          <Groups3OutlinedIcon />
        </IconButton>
        <IconButton onClick={loggedout}>
          <LogoutOutlinedIcon />
        </IconButton>
      </Box>
    </Box>
  );
};

export default MainHeader;
