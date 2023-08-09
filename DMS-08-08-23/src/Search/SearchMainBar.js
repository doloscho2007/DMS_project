import React, { useState } from "react";
import FileProperties from "../Documents/MainBar/FileProperties";
import { ColorModeContext, tokens } from "../theme";
import { CssBaseline, ThemeProvider, useTheme } from "@mui/material";

import SearchFileView from '../Search/SearchFileView';
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



const SearchMainBar = () => {
  const { ArrowUpDown, propUp, bookmarkOpen, refreshKey } = useGlobalContext();

  const theme = useTheme();
  const colors = tokens(theme.palette.mode);

 
  return (
    <div
      className="d-flex flex-column"
      style={{
        overflow: "hidden",
        height: "40vw", // Update the height to 100%
        width: "100%",
        marginBottom: "0",
        display: "flex", // Add this line to make it a flex container
        flexDirection: "column", // Stack child elements vertically
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
        <SearchFileView key={refreshKey} />
        
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
          alignItems: "center",
        }}
      >
        <Card backgroundcolor={"primary"} sx={{ maxWidth: "100%", width:"100%",marginTop: 0 }}>
          <CardContent sx={{ maxWidth: "100%" }}>
            <Collapse
              in={propUp}
              timeout="auto"
              sx={{ marginTop: 0, width: "100%", paddingTop: 0 }} // Add paddingTop: 0
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
            width: "5%",
            height: "2%",
           
            top: "2px",
            right: "2px",
            position:"relative",
            alignItems: "center",
            justifyContent: "center",
            zIndex: 1, // Set a higher z-index value to ensure it's on top of FileView
            "&:hover": {
              backgroundColor: "transparent",
              borderRadius: "0px",
            },
          }}
        >
          <div style={{ display: "flex", alignItems: "center", justifyContent: "center"}}>
            <ExpandMoreIcon />
          </div>
        </ExpandMore>
          </CardContent>
        </Card>
      </div>
    </div>
  );
};

export default SearchMainBar;
