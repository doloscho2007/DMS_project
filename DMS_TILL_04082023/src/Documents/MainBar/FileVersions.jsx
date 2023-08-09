import React from 'react'
import { useEffect } from 'react'
import { useCallback } from 'react'
import { useState } from 'react'
import { useGlobalContext } from '../../Context/DataContext'
import { DMSAPI } from "../../Service/DMSAPI";
import MuiAlert from "@mui/material/Alert";
import Snackbar from "@material-ui/core/Snackbar";
const FileVersions = () => {

  const {editForm ,versions, setVersions ,checkOutOpen ,setPdfFilePath } =useGlobalContext()
  const [open1, setOpen] = useState(false);
  const [snackbarMessage, setSnackbarMessage] = useState("");
  const [snackbarSeverity, setSnackbarSeverity] = useState("success");
  const { fileId } = editForm;
  // console.log(versions);
  const NullCheck =(fileId === '') ? 0 : fileId; 

  const getVersionMeth = useCallback(
    async (user) => {
      try {
        const response = await DMSAPI.getAllVersions(NullCheck) ;
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
    getVersionMeth("1234");
  }, [getVersionMeth]);

  const downloadFileOld = async (filePath , fileId , fileName) => {   
  //  console.log("filePath" ,filePath , "fileId" , fileId)
    const response = await DMSAPI.downloadFileOld(fileId , filePath , "Yes" );
    // console.log(response);
    if (response.status === 200) {
      const data = await response.blob();
      const fileURL = window.URL.createObjectURL(data);
      let alink = document.createElement("a");
      alink.href = fileURL;
      alink.download = `${fileName}`;
      alink.click();
      setSnackbarMessage("File downloaded successfully");
      setSnackbarSeverity("success");
      setOpen(true);
      // console.log("File Downloaded Successfully");
    } else {
      setSnackbarMessage("internal server error");
      setSnackbarSeverity("error");
      setOpen(true);
      console.log("internal server error");
    }

  }

  const handleClose = (event, reason) => {
    if (reason === "clickaway") {
      return;
    }
    setOpen(false);
  };
  function Alert(props) {
  return <MuiAlert elevation={6} variant="filled" {...props} />;
}


  return (
    <div id="versions" className="tab-pane container fade" style={{width:'100%',height:'10rem' }}>
        <table id="versionTable" className="table table-hover"  >
            <thead style={{backgroundColor: '#47a7e3'}}>
            <tr>
              <th>user</th>
              <th>Event</th>
              <th>File Name</th>
              <th>Version</th>
              <th>Date</th>
              <th>Permalink</th>
            </tr>
            </thead>
            <tbody id="versionTableBody">
                {
                  versions.map((version) => {
                    // console.log(version.fileDetails)
                    const {userName ,versionId ,fileDetails,createdAt ,fileVersion, versionComment ,filePath} = version;
                    const {fileId , fileName } =fileDetails
                    var createdAtdate = new Date(createdAt);

                    var date = new Date(createdAtdate);
                    var yyyy = date.getFullYear();
                    var mm = date.getMonth() + 1; // Months start at 0!
                    var dd = date.getDate();

                    if (dd < 10) dd = "0" + dd;
                    if (mm < 10) mm = "0" + mm;

                    var formattedToday = dd + "-" + mm + "-" + yyyy;
                    
                    return(
                        <tr key={versionId}>
                           <td>{userName}</td>
                           <td>{versionComment}</td>
                           <td>{fileName}</td>
                           <td>{fileVersion}</td>
                           <td>{formattedToday}</td>
                           <td  onClick={(e) => {
                             downloadFileOld(filePath , fileId , fileName)
                            }} ><button style={{cursor:"pointer"}} color='skyblue'  disabled={checkOutOpen ? true : false}>download</button></td>
                        </tr>
                    )

                  })
                }
                
            </tbody>
        </table>
         <Snackbar
        open={open1}
        autoHideDuration={6000}
        onClose={handleClose}
        anchorOrigin={{ vertical: "top", horizontal: "center" }}
      >
        <Alert
          onClose={handleClose}
          message={snackbarMessage}
          severity={snackbarSeverity}
        >
          {snackbarMessage}
        </Alert>
      </Snackbar>
    </div>
    
  )
}

export default FileVersions