import { Box, IconButton, useTheme, Typography } from "@mui/material";
import { useContext } from "react";
import { ColorModeContext, tokens } from "../theme";
import Button from "@mui/material/Button";
import * as React from "react";
import { NavLink } from "react-router-dom";
import { styled } from "@mui/material/styles";
import Stack from "@mui/material/Stack";
import { makeStyles } from "@mui/styles";
import Paper from "@mui/material/Paper";

const useStyles = makeStyles({
  button: {
    "&.active": {
      background: "#858585",
    },
  },
});

export default function DashboardTab() {
  const theme = useTheme();
  const colors = tokens(theme.palette.mode);
  const classes = useStyles();

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
            height: 20,
            borderBottomRightRadius: 0,
            borderBottomLeftRadius: 0,
          }}
          variant="contained"
          to=""
          color={"secondary"}
        >
          User
        </Button>
        <Button
          className={classes.button}
          component={NavLink}
          style={{
            height: 20,
            borderBottomRightRadius: 0,
            borderBottomLeftRadius: 0,
          }}
          variant="contained"
          to=""
          color={"secondary"}
        >
          Messages
        </Button>
        <Button
          className={classes.button}
          component={NavLink}
          style={{
            height: 20,
            borderBottomRightRadius: 0,
            borderBottomLeftRadius: 0,
          }}
          variant="contained"
          to=""
          color={"secondary"}
        >
          Chat
        </Button>
      </Stack>
    </div>
  );
}
