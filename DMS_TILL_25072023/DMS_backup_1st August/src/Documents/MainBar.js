import React, { useState } from "react";
import FileProperties from "./MainBar/FileProperties";
import { ColorModeContext, tokens } from "../theme";
import { CssBaseline, ThemeProvider, useTheme } from "@mui/material";

import FileView from "./MainBar/FileView";
import { RiArrowDownSFill, RiArrowUpSFill } from "react-icons/ri";
import Collapse from "@mui/material/Collapse";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import IconButton from "@mui/material/IconButton";
import { styled } from "@mui/material/styles";
import CardContent from "@mui/material/CardContent";
import Typography from "@mui/material/Typography";
import Button from "@mui/material/Button";
import Card from "@mui/material/Card";
import Box from "@mui/material/Box";
import Accordion from "@mui/material/Accordion";
import AccordionSummary from "@mui/material/AccordionSummary";
import AccordionDetails from "@mui/material/AccordionDetails";
import "../index.css";
import { useGlobalContext } from "../Context/DataContext";
import Container from "@mui/material/Container";

const ExpandMore = styled((props) => {
  const { expand, ...other } = props;
  return <IconButton {...other} />;
})(({ theme, expand }) => ({
  transform: !expand ? "rotate(0deg)" : "rotate(180deg)",
  marginLeft: "auto",
  transition: theme.transitions.create("transform", {
    duration: theme.transitions.duration.shortest,
  }),
}));

const MainBar = () => {
  const { ArrowUpDown, propUp, bookmarkOpen, refreshKey } = useGlobalContext();
  const theme = useTheme();
  const colors = tokens(theme.palette.mode);

  return (
    <div
      className="d-flex flex-column"
      style={{
        overflow: "hidden",
        height: propUp ? "40vw" : "100vh",
        width: "100%",
        marginBottom: "0",
        display: "flex", 
        flexDirection: "column", 
      }}
    >
      <div
        className="file-container"
        style={{
          flex: 1,
          width: "100%",
          whiteSpace: "nowrap",
          height: propUp ? "40vh" : "50vh",
          overflow: "auto",
          zIndex: 2,
        }}
      >
        <FileView key={refreshKey} />
      </div>

      <div
        className="properties-container"
        style={{
          flex: 1,
          overflow: "auto",
          position: "relative",
          marginTop: 0,
          bottom: 0,
          top: 0,
          height: "50vh",
          display: "flex",
          justifyContent: "center",
          alignItems: propUp ? "center" : "flex-start",
        }}
      >
        <Card
          backgroundcolor={"primary"}
          sx={{ width: propUp ? "100%" : "30%", marginTop: 0 }} // Adjust the width here
        >
          <CardContent>
            <Collapse
              in={propUp}
              timeout="auto"
              sx={{ marginTop: 0, paddingTop: 0 }} // Add paddingTop: 0
              unmountOnExit
            >
              <FileProperties />
            </Collapse>
            <ExpandMore
              expand={propUp}
              onClick={ArrowUpDown}
              aria-expanded={propUp}
              aria-label="show more"
              sx={{
                width: "100%", // Adjust the width of the icon container
                height: "2%",

                top: "2px",
                right: "2px",
                position: "relative",
                alignItems: "center",
                justifyContent: "center",
                zIndex: 1, // Set a higher z-index value to ensure it's on top of FileView
                "&:hover": {
                  backgroundColor: "transparent",
                  borderRadius: "0px",
                },
              }}
            >
              <div
                style={{
                  display: "flex",
                  alignItems: "center",
                  justifyContent: "center",
                }}
              >
                <ExpandMoreIcon />
              </div>
            </ExpandMore>
          </CardContent>
        </Card>
      </div>
    </div>
  );
};

export default MainBar;
