import * as React from "react";
import { styled } from "@mui/system";
import TabsUnstyled from "@mui/base/TabsUnstyled";
import TabsListUnstyled from "@mui/base/TabsListUnstyled";
import TabPanelUnstyled from "@mui/base/TabPanelUnstyled";
import { buttonUnstyledClasses } from "@mui/base/ButtonUnstyled";
import TabUnstyled, { tabUnstyledClasses } from "@mui/base/TabUnstyled";
import { IconButton, useTheme, Typography } from "@mui/material";
import { useContext } from "react";
import { ColorModeContext, tokens } from "../theme";
import Administration from "./Administration";
import Logs from "./Logs";
import Session from "./Session";
import Users from "./Users";
import Group from "./Group";
import Security from "./Security";
import General from "./General";

export default function AdminMainView({ activeTab }) {
  const theme = useTheme();
  const colors = tokens(theme.palette.mode);

  const Tab = styled(TabUnstyled)`
  font-family: IBM Plex Sans, sans-serif;
  color: white;
  cursor: pointer;
  background-color: ${colors.greenAccent[500]};
  width: 100px;
  padding: 6px;
  margin-top: 0px;
  margin-bottom: 0px;
  margin-right:5px;
  border: none;
  border-radius: 7px;
  display: flex;
  justify-content: center;
  border-collapse: separate;
  text-indent: initial;
  border-spacing: 2px;

  &:hover {
    background-color: ${colors.greenAccent[800]}};
  }

  &:focus {
    color:${colors.greenAccent[500]};
    outline: none;
  }

  &.${tabUnstyledClasses.selected} {
    background-color: ${colors.grey[700]};
    color:  ${colors.primary[500]};
  }

  &.${buttonUnstyledClasses.disabled} {
    opacity: 0.5;
    cursor: not-allowed;
  }
`;

  const TabPanel = styled(TabPanelUnstyled)(`width: 100%;`);

  const TabsList = styled(TabsListUnstyled)(
    ({ theme }) => `
  min-width: 200px;
  background-color:transparent;
  border-radius: 12px;
  margin-bottom: 16px;
  margin-left:0px;
  margin-right:0px;
  display: flex;
  align-items: left;
  align-content: flex-start;
 
  `
  );

  if (activeTab === 1) {
    return <General />;
  } else if (activeTab === 2) {
    return <Session />;
  } else if (activeTab === 3) {
    return <Administration />;
  } else if (activeTab === 4) {
    return <Users />;
  } else if (activeTab === 5) {
    return <Group />;
  } else if (activeTab === 6) {
    return <Security />;
  }
}
