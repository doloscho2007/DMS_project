import { useEffect } from "react";
import { React, useState, useContext, createContext, useCallback } from "react";
import { DMSAPI } from "../Service/DMSAPI";

const DataContext = createContext();

const DataProvider = ({ children }) => {
  // states
  const [pdfToggleActive, setPdfToggleActive] = useState(true);
  // using tree component
  const [nodes, setNodes] = useState([]);
  // const [searchResult,setSearchResult]=useState([])
  // console.log("context"+searchResult)
  // using FileVersions component
  const [versions, setVersions] = useState([]);
  const [editForm, setEditForm] = useState({
    fileId: "",
    fileName: "",
    createdAt: "",
    fileSearch1: "",
    fileSearch2: "",
    fileSearch3: "",
    treePath: "",
    createdOn: "",
    fileSizeFd: "",
    userNameFd: "",
  });

  // using FileView component
  const [listView, setlistView] = useState(true);
  const [nodeValue, setNodeValue] = useState(0);
  const [files, setFiles] = useState([]);
  const [searchFiles, setSearchFiles] = useState([]);
  const [searchPath, setSearchPath] = useState("");

  const [pdfFilePath, setPdfFilePath] = useState({
    pdfFilePath: "//127.0.0.1/F1V1s",
  });

  // using inside context component
  const [isEditing, setIsEditing] = useState(false);

  // using mainbarHeader component
  const [fileInfo, setFileInfo] = useState({
    FileIds: [],
    response: [],
  });
  const [checkboxForm, setCheckboxForm] = useState({
    fileId: "",
    fileName: "",
  });
  const [bookmarkOpen, setBookmarkOpen] = useState(false);
  const [bookmarkFiles, setBookmarkFiles] = useState([]);
  const [checkOutOpen, setCheckOutOpen] = useState(false);
  const [checkOutFiles, setCheckOutFiles] = useState([]);

  // Commanded states
  // const [isSubscribed, setIsSubscribed] = useState(false);
  // const [fileProperties, setFileProperties] = useState([]);
  // const [treeToggleActive, setTreeToggleActive] = useState(true);

  // functions
  const getNodeId = (event, nodes) => {
    const NanCheck = isNaN(parseInt(nodes)) ? 0 : parseInt(nodes);
    localStorage.setItem("node", NanCheck);
    setNodeValue(NanCheck);
  };

  const pdfToggleclick = () => {
    setPdfToggleActive(!pdfToggleActive);
    console.log("pdf click");
  };

  const listToGrid = () => {
    setlistView(!listView);
    console.log("view change");
    console.log(listView);
  };

  function handleChange(e) {
    setEditForm({
      ...editForm,
      // [e.target.name]: e.target.value,
    });
  }

  // File Clicked
  function FilechangeEditState(file) {
    console.log(file);
    setPdfFilePath(file);
    if (file.fileId === editForm.fileId) {
      setIsEditing((isEditing) => !isEditing); // hides the form
    } else if (isEditing === false) {
      setIsEditing((isEditing) => !isEditing); // shows the form
    }
    FilecaptureEdit(file);
  }

  function FilecaptureEdit(clickedfile) {
    let filtered = (
      checkOutOpen
        ? checkOutFiles
        : bookmarkOpen
        ? bookmarkFiles
        : searchPath === "/search"
        ? searchFiles
        : files
    ).filter((file) => file.fileId === clickedfile.fileId);
    setEditForm(filtered[0]);
  }

  // checkbox Clicked
  function CheckboxchangeEditState(file) {
    console.log(file);
    if (file.fileId === checkboxForm.fileId) {
      setIsEditing((isEditing) => !isEditing); // hides the form
      console.log("if");
    } else if (isEditing === false) {
      setIsEditing((isEditing) => !isEditing); // shows the form
      console.log("else");
    }
    CheckboxcaptureEdit(file);
  }

  function CheckboxcaptureEdit(clickedfile) {
    let filtered = (
      checkOutOpen
        ? checkOutFiles
        : bookmarkOpen
        ? bookmarkFiles
        : searchPath === "/search"
        ? searchFiles
        : files
    ).filter((file) => file.fileId === clickedfile.fileId);
    setCheckboxForm(filtered[0]);
    console.log("capture");
  }

  const handlerChange = (e) => {
    const { value, checked } = e.target;
    const { FileIds } = fileInfo;
    //  console.log(`${value} is ${checked}`);

    // Case 1 : The user checks the box
    if (checked) {
      setFileInfo({
        FileIds: [...FileIds, value],
        response: [...FileIds, value],
      });
      // console.log(fileInfo.response)
    }

    // Case 2 : The user unchecks the box
    else {
      setFileInfo({
        FileIds: FileIds.filter((e) => e !== value),
        response: FileIds.filter((e) => e !== value),
      });
    }
  };

  const [propUp, setPropUp] = useState(true);
  const ArrowUpDown = () => {
    setPropUp(!propUp);
  };

  const [tab, setTab] = useState(true);

  const propTab = () => {
    // console.log("prop changed")
    setTab(true);
  };

  const versTab = () => {
    // console.log("vers changed")
    setTab(false);
  };

  const [refreshKey, setRefreshKey] = useState(0);

  const handleRefresh = () => {
    setRefreshKey((prevKey) => prevKey + 1);
    setFileInfo({
      FileIds: [],
      response: [],
    });
  };

  const [versionRefreshKey, setVersionRefreshKey] = useState(0);

  const handleVersionRefresh = () => {
    setVersionRefreshKey((prevKey) => prevKey + 1);
  };

  const [pdfRefreshKey, setPdfRefreshKey] = useState(0);

  const handlePdfRefresh = () => {
    setPdfRefreshKey((prevKey) => prevKey + 1);
  };

  const [treeRefreshKey, setTreePdfRefreshKey] = useState(0);

  const handleTreeRefresh = () => {
    setTreePdfRefreshKey((prevKey) => prevKey + 1);
  };

  // BOOKMARK GET METHOD CODE START
  const getBookmarkFilesMeth = useCallback(async () => {
    try {
      const response = await DMSAPI.getAllBookmarkedFiles();
      const data = await response.json();
      // console.log(data);
      setBookmarkFiles(data);
      // console.log(bookmarkFiles);
    } catch (error) {
      console.log(error);
      console.log("mainbar catch");
    }
  }, [setBookmarkFiles, bookmarkOpen]);

  useEffect(() => {
    getBookmarkFilesMeth();
  }, [getBookmarkFilesMeth]);
  // BOOKMARK GET METHOD CODE END

  // CHECKOUT GET METHOD CODE START
  const getCheckoutFilesMeth = useCallback(async () => {
    try {
      const response = await DMSAPI.getAllCheckedOutFiles();
      const data = await response.json();
      // console.log(data);
      setCheckOutFiles(data);
      // console.log(checkOutFiles);
    } catch (error) {
      console.log(error);
      console.log("mainbar catch");
    }
  }, [setCheckOutFiles, checkOutOpen]);

  useEffect(() => {
    getCheckoutFilesMeth();
  }, [getCheckoutFilesMeth]);
  // CHECKOUT GET METHOD CODE END

  const { fileId } = editForm;
  // console.log(versions);
  const NullCheck = fileId === "" ? 0 : fileId;

  const getVersionMeth = useCallback(
    async (user) => {
      try {
        const response = await DMSAPI.getAllVersions(NullCheck);
        const data = await response.json();
        // console.log("version data :",data)
        setVersions(data);
        // console.log("versions",versions);
      } catch (error) {
        console.log(error);
        console.log("version catch");
      }
    },
    [NullCheck, setVersions]
  );
  useEffect(() => {
    getVersionMeth();
  }, [getVersionMeth]);

  return (
    <DataContext.Provider
      value={{
        nodeValue,
        nodes,
        setNodes,
        files,
        setFiles,
        pdfToggleActive,
        listView,
        getNodeId,
        pdfToggleclick,
        listToGrid,
        FilechangeEditState,
        handleChange,
        editForm,
        searchFiles,
        setSearchFiles,
        isEditing,
        versions,
        setVersions,
        CheckboxchangeEditState,
        checkboxForm,
        setCheckboxForm,
        fileInfo,
        setFileInfo,
        handlerChange,
        ArrowUpDown,
        propUp,
        tab,
        propTab,
        versTab,
        pdfFilePath,
        bookmarkOpen,
        setBookmarkOpen,
        bookmarkFiles,
        setBookmarkFiles,
        checkOutOpen,
        setCheckOutOpen,
        checkOutFiles,
        setCheckOutFiles,
        refreshKey,
        handleRefresh,
        versionRefreshKey,
        handleVersionRefresh,
        setSearchPath,
        pdfRefreshKey,
        handlePdfRefresh,
        treeRefreshKey,
        handleTreeRefresh,
        getVersionMeth,
      }}
    >
      {children}
    </DataContext.Provider>
  );
};

export const useGlobalContext = () => {
  return useContext(DataContext);
};

export { DataContext, DataProvider };
