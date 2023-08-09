import React, { useState,useEffect } from "react";
import TreeItem from "@mui/lab/TreeItem";
import {ContextMenu} from './styles';
const ContextMenuTreeItem = ({ nodeId, label, children, onContextMenu }) => {

  const [rightClicked, setRightClicked] = useState(false);
  const [points, setPoints] = useState({ x: 0, y: 0 });


  useEffect(() => {
    
    const handleClick = (event) => {
      if (event.button === 0 || event.button === 2) {
        // Left click or right click occurred
        setRightClicked(false);
      }
    };
    window.addEventListener('click', handleClick);
    return () => window.removeEventListener('click', handleClick);
  }, []);


  return (
    <>
      <TreeItem nodeId={nodeId} label={label} onContextMenu={(e) => {
         console.log(e.pageX);
         console.log(e.pageY);
         setPoints({ x: e.pageX, y: e.pageY });
         e.preventDefault();
         setRightClicked(!rightClicked);
        //  if (rightClicked) {
        //   setRightClicked(false);
        // } else {
        //   setRightClicked(true);
        // }
         onContextMenu(nodeId);
         console.log(rightClicked)
         

      }}>
        {children}
      </TreeItem>
      {rightClicked && (
        <ContextMenu top={points.y} left={points.x}>
          <ul>
            <li>Delete Message</li>
            <li>Pin Message</li>
            <li>Edit Message</li>
          </ul>
        </ContextMenu>
      )}
    </>
  );
};

export default ContextMenuTreeItem;
