import * as React from "react";
import Accordion from "@mui/material/Accordion";
import AccordionSummary from "@mui/material/AccordionSummary";
import AccordionDetails from "@mui/material/AccordionDetails";
import Typography from "@mui/material/Typography";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import { styled } from "@mui/system";
import TabsUnstyled from "@mui/base/TabsUnstyled";
import TabsListUnstyled from "@mui/base/TabsListUnstyled";
import TabPanelUnstyled from "@mui/base/TabPanelUnstyled";
import { buttonUnstyledClasses } from "@mui/base/ButtonUnstyled";
import TabUnstyled, { tabUnstyledClasses } from "@mui/base/TabUnstyled";
import { IconButton, useTheme } from "@mui/material";
import { useContext } from "react";
import { ColorModeContext, tokens } from "../theme";
import PropTypes from "prop-types";
import Tabs from "@mui/material/Tabs";
import Tab from "@mui/material/Tab";
import Box from "@mui/material/Box";

export default function AdminSideView({ setActiveTab }) {
  const [value1, setValue1] = React.useState(0);
  const [value2, setValue2] = React.useState(0);

  const handleChange1 = (event, newValue) => {
    setValue1(newValue);
  };

  const handleChange2 = (event, newValue) => {
    setValue2(newValue);
  };
  const theme = useTheme();
  const colors = tokens(theme.palette.mode);

  const Tab = styled(TabUnstyled)`
  font-family: IBM Plex Sans, sans-serif;
  color: white;
  cursor: pointer;
  background-color: ${colors.grey[500]};
  width: 200px;
  padding: 6px;
  margin-top: 0px;
  margin-bottom: 5px;
  margin-right:5px;
  border: none;
  display: flex;
  justify-content: center;
  border-collapse: separate;
  text-indent: initial;

  &:hover {
    background-color: ${colors.grey[100]}};
  }

  &:focus {
    
    outline: none;
  }

  &.${tabUnstyledClasses.selected} {
    background-color: ${colors.grey[700]};
    
  }

  &.${buttonUnstyledClasses.disabled} {
    opacity: 0.5;
    cursor: not-allowed;
  }
`;

  const TabsList = styled(TabsListUnstyled)(
    ({ theme }) => `
  min-width: 100px;
  background-color:transparent;
  border-radius: 12px;
  margin-bottom: 16px;
  margin-left:0px;
  margin-right:0px;
  display: flex;
  align-items: left;
 
  `
  );
  return (
    <div style={{ height: "100%", boxShadow: "0 3px 10px rgb(0 0 0 / 0.2)" }}>
      <Accordion>
        <AccordionSummary
          expandIcon={<ExpandMoreIcon />}
          aria-controls="panel1a-content"
          id="panel1a-header"
        >
          <Typography>System</Typography>
        </AccordionSummary>
        <AccordionDetails>
          <TabsUnstyled defaultValue={0}>
            <Tabs
              orientation="vertical"
              variant="scrollable"
              value={value1}
              onChange={handleChange1}
              aria-label="Vertical tabs example"
            >
              <Tab onClick={() => setActiveTab(1)}>General</Tab>
              <Tab onClick={() => setActiveTab(2)}>Sessions</Tab>
              <Tab onClick={() => setActiveTab(3)}>Log</Tab>
            </Tabs>
          </TabsUnstyled>
        </AccordionDetails>
      </Accordion>
      <Accordion>
        <AccordionSummary
          expandIcon={<ExpandMoreIcon />}
          aria-controls="panel2a-content"
          id="panel2a-header"
        >
          <Typography>Security</Typography>
        </AccordionSummary>
        <AccordionDetails>
          <TabsUnstyled defaultValue={0}>
            <Tabs
              orientation="vertical"
              variant="scrollable"
              value={value2}
              onChange={handleChange2}
              aria-label="Vertical tabs example"
            >
              <Tab onClick={() => setActiveTab(4)}>Users</Tab>
              <Tab onClick={() => setActiveTab(5)}>Group</Tab>
              <Tab onClick={() => setActiveTab(6)}>Security</Tab>
            </Tabs>
          </TabsUnstyled>
        </AccordionDetails>
      </Accordion>
    </div>
  );
}
