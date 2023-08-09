import React from 'react'
import { useEffect } from 'react'
import { useCallback } from 'react'
import { useState } from 'react'
import { useGlobalContext } from '../../Context/DataContext'
import { DMSAPI } from "../../Service/DMSAPI";
const FileVersions = () => {

    const {editForm ,versions, setVersions } =useGlobalContext()

    
  const { fileId } = editForm;
  // console.log(fileId);
  const NullCheck =(fileId === '') ? 0 : fileId; 
  // console.log(NullCheck) 
  // setVersionValue(fileId);
  // console.log(versionValue)
    
    
  const api_url = `http://localhost:8080/getAllVersions?fileId=${NullCheck}`;


  const getVersionMeth = useCallback(
    async (user) => {
      try {
        const response = await fetch(`${api_url}`);
        const data = await response.json();
        console.log("version data :",data)
        setVersions(data);
        console.log("versions",versions);
      } catch (error) {
        console.log(error);
        console.log("version catch");
      }
    },
    [api_url, setVersions]
  );
  // console.log(versions);

  const fileDownload = async (fileId,fileName,pdfFile) => {
    // console.log(e);
    // const pdfFile = e;
    const response = await DMSAPI.downloadFile(fileId,pdfFile);
    console.log(response);
    if (response.status === 200) {
      const data = await response.blob();
      const fileURL = window.URL.createObjectURL(data);
      let alink = document.createElement("a");
      alink.href = fileURL;
      if (
        pdfFile === "Yes" &&
        fileName.includes(".pdf")
      ) {
        alink.download = `${fileName}`;
      } else if (pdfFile === "No") {
        alink.download = `${fileName}`;
      } else if (pdfFile === "Yes") {
        var fname = `${fileName}`.split(".");
        alink.download = `${fname[0]}` + ".pdf";
      }

      alink.click();
     
      console.log("File Downloaded Successfully");
    } else {
      
      console.log("internal server error");
    }
  };

  useEffect(() => {
    getVersionMeth("1234");
  }, [getVersionMeth]);


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
     
                    const {userName ,versionId ,fileDetails,createdAt ,fileVersion,filePath,pdfFile, versionComment} = version;
                    const {fileName ,fileId,currentVersion} =fileDetails
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
                           {/* onClick={fileDownload(fileId,fileName,pdfFile)} */}
                           <td><a >download</a></td>
                        </tr>
                    )

                  })
                }
                
            </tbody>
        </table>
    </div>
  )
}

export default FileVersions