import styled, { css } from "styled-components";
export const MenuContextContainer = styled.div`
  border: 1px solid #ffffff2d;
  border-radius: 4px;
  padding: 18px;
  margin: 5px 0;
  box-sizing: border-box;
`;

export const ContextMenu = styled.div`
  position: absolute;
  width: 150px;
  background-color: #808080;
  border-radius: 0px;
  box-sizing: border-box;
  ${({ top, left }) => css`
    top: ${top}px;
    left: ${left}px;
  `}
  ul {
    box-sizing: border-box;
    padding: 5px;
    margin: 0;
    list-style: none;
  }
  ul li {
    padding: 5px 2px;
  }
  /* hover */
  ul li:hover {
    cursor: pointer;
    background-color:#4cceac;
  }
`;
