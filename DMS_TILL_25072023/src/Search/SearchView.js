import React, { useCallback } from "react";
import { useGlobalContext } from "../Context/DataContext";
import Button from "@mui/material/Button";
import { useEffect } from "react";
import { DMSAPI } from "../Service/DMSAPI";
import { useState } from "react";

const SearchView = () => {
  const {
      setSearchFiles,
  } = useGlobalContext();

  
  const [searchFileText, setSearchFileText] = useState("");
  const [searchFileParamValue, setSearchFileParamValue] = useState("");
  const [searchFileNameValue, setSearchFileNameValue] = useState("");
  const [searchFolderNameValue, setSearchFolderNameValue] = useState("");
  const [searchByFileDate1Value, setSearchByFileDate1Value] = useState("");
  const [searchByFileDate2Value, setSearchByFileDate2Value] = useState("");
  const [searchByCreatedDate1Value, setSearchByCreatedDate1Value] =
    useState("");
  const [searchByCreatedDate2Value, setSearchByCreatedDate2Value] =
    useState("");
  const [checkWholeWordValue, setCheckWholeWordValue] = useState(true);
  // let searchResult=[];
  let resJson;

  const handleSearchFileText = (e) => {
    setSearchFileText(e.target.value);
  };

  const handleSearchFileParamValue = (e) => {
    setSearchFileParamValue(e.target.value);
  };
  const handleSearchFileNameValue = (e) => {
    setSearchFileNameValue(e.target.value);
  };

  const handleSearchFolderNameValue = (e) => {
    setSearchFolderNameValue(e.target.value);
    console.log("folder name "+ e.target.value);
  };

  const handleSearchByFileDate1Value = (e) => {
    setSearchByFileDate1Value(e.target.value);
  };

  const handleSearchByFileDate2Value = (e) => {
    setSearchByFileDate2Value(e.target.value);
  };

  const handleSearchBycreatedDate1Value = (e) => {
    setSearchByCreatedDate1Value(e.target.value);
  };

  const handleSearchBycreatedDate2Value = (e) => {
    setSearchByCreatedDate2Value(e.target.value);
  };
  const handleCheckWholeWordValue = (e) => {
    setCheckWholeWordValue(e.target.value);
  };

  const submitSearch = async (e) => {
    e.preventDefault();
    //  let searchResult1=[];

    var searchParam = document.getElementById("searchFileText").value;
    var fileSearchParam = document.getElementById("searchFileParamValue").value;
    var fileName = document.getElementById("searchFileNameValue").value;
    var folderPathName = document.getElementById("searchFolderNameValue").value;
    var fileDate1 = document.getElementById("searchByFileDate1Value").value;
    var fileDate2 = document.getElementById("searchByFileDate2Value").value;
    var creationDate1 = document.getElementById(
      "searchByCreatedDate1Value"
    ).value;
    var creationDate2 = document.getElementById(
      "searchByCreatedDate2Value"
    ).value;
    var isWholeWord = document.getElementById("checkWholeWordValue").value;

    if (
      (fileName === null || fileName === "") &&
      (searchParam === null || searchParam === "") &&
      (fileSearchParam === null || fileSearchParam === "")
    ) {
      alert(
        "Please mention File Name (or) File Content (or) property to search"
      );
    } else if (
      (fileDate1 === "" && fileDate2 !== "") ||
      (fileDate1 !== "" && fileDate2 === "")
    ) {
      alert("Please pass both start and end file dates");
    } else if (
      (creationDate1 === "" && creationDate2 !== "") ||
      (creationDate1 !== "" && creationDate2 === "")
    ) {
      alert("Please pass both start and end creation dates");
    } else {
      // setSearchResult([])
      const response = await DMSAPI.searchFolder(
        searchParam,
        fileSearchParam,
        fileName,
        folderPathName,
        fileDate1,
        fileDate2,
        creationDate1,
        creationDate2,
        isWholeWord
      );

      setSearchFiles(response);
      console.log(response);
    }

    // console.log(searchResult1);

    // setSearchResult(searchResult1);
    // console.log("search result "+searchResult);
    // console.log("search result "+searchResult[0]);
    // console.log("rrr "+JSON.stringify(searchResult))
    // console.log("rrr "+searchResult.JSON);
  };

  const [allFolder, setAllFolder] = useState([]);

  const getAllFolders = useCallback(async () => {
    try {
      const response = await DMSAPI.getAllFolders();
      const data = await response.json();
     
      //  console.log(data);
      setAllFolder(data);
    } catch (error) {
      console.log("getAllFolders catch" + error);
    }
  }, []);
  // console.log(allFolder);

  useEffect(() => {
    getAllFolders();
  }, [getAllFolders]);

  /** Search File Function End Here **/
  return (
    <div id="fileDetailsD" style={{ width: "100%", padding: "10px" }}>
      <div style={{ padding: "0px" }}>
        <span style={{ display: "inline-block", width: "100px" }}>
          File Content :{" "}
        </span>
        <span>
          <input
            type="text"
            id="searchFileText"
            style={{ width: "200px", height: "20px" }}
            name="searchFileText"
            onChange={handleSearchFileText}
          />
        </span>
        <span style={{ display: "inline-block", width: "100px" }}>
          File Property :{" "}
        </span>
        <span>
          <input
            type="text"
            id="searchFileParamValue"
            style={{ width: "200px", height: "20px" }}
            name="searchFileParamValue"
            onChange={handleSearchFileParamValue}
          />
        </span>

        <span style={{ display: "inline-block", width: "100px" }}>
          File Name :{" "}
        </span>
        <span>
          <input
            type="text"
            id="searchFileNameValue"
            style={{ width: "200px", height: "20px" }}
            name="searchFileNameValue"
            onChange={handleSearchFileNameValue}
          />
        </span>
        <span style={{ display: "inline-block", width: "100px" }}>
          Folder Name :{" "}
        </span>
        <span>
        <select
            id="searchFolderNameValue"
            name="searchFolderNameValue"
            style={{ width: "200px", height: "20px" }}
            onChange={handleSearchFolderNameValue}
          >
                <option value="">All </option>
          
            {allFolder.map((folder) => {
              // console.log(folder.folderPath);
              return (
               
                <option value={folder.folderPath.substring(1, folder.folderPath.length - 1).replaceAll(" ", "-")}>
                  {folder.folderPath.substring(1, folder.folderPath.length - 1).replaceAll(" ", "")}
                </option>
               
              );
            })}
             </select>
         
        </span>
        <br></br>
        <span style={{ display: "inline-block", width: "100px" }}>
          {" "}
          File Date :
        </span>
        <span>
          <input
            type="Date"
            id="searchByFileDate1Value"
            name="searchByFileDate1Value"
            style={{ width: "200px" }}
            onChange={handleSearchByFileDate1Value}
          />
        </span>
        <span style={{ display: "inline-block", width: "100px" }}> </span>
        <span>
          <input
            type="Date"
            id="searchByFileDate2Value"
            name="searchByFileDate2Value"
            style={{ width: "200px" }}
            onChange={handleSearchByFileDate2Value}
          />
        </span>
        <br></br>
        <span style={{ display: "inline-block", width: "100px" }}>
          {" "}
          Created Date :{" "}
        </span>
        <span>
          <input
            type="Date"
            id="searchByCreatedDate1Value"
            name="searchByCreatedDate1Value"
            style={{ width: "200px" }}
            onChange={handleSearchBycreatedDate1Value}
          />
        </span>
        <span style={{ display: "inline-block", width: "100px" }}> </span>
        <span>
          <input
            type="Date"
            id="searchByCreatedDate2Value"
            name="searchByCreatedDate2Value"
            style={{ width: "200px" }}
            onChange={handleSearchBycreatedDate2Value}
          />
        </span>
        <span style={{ display: "inline-block", width: "72px" }}></span>
        <br></br>
        <span style={{ paddingBottom: "10px" }}>
          <input
            type="checkbox"
            id="checkWholeWordValue"
            name="checkWholeWordValue"
            onClick={handleCheckWholeWordValue}
          />{" "}
          Whole Word
        </span>
        <br></br>

        <span
          style={{
            display: "flex",
            paddingTop: "10px",
            justifyContent: "center",
          }}
        >
          <Button variant="contained" color="secondary" onClick={submitSearch}>
            Search
          </Button>
        </span>
      </div>
    </div>
  );
};

export default SearchView;
