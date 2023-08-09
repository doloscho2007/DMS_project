import React from "react";
import { useGlobalContext } from "../Context/DataContext";

const PdfViewer = () => {
  const { pdfFilePath } = useGlobalContext();
  console.log(pdfFilePath);
  var pdfPath = pdfFilePath.pdfFilePath;
  var replacedPdfPath = pdfPath.replace("Repo/", "");
  return (
    <div className="shadow doc-height border border-dark me-1">
      <iframe
        src={"http:" + replacedPdfPath}
        title="pdf"
        style={{ height: "40vw", width: "100%" }}
      ></iframe>
    </div>
  );
};

export default PdfViewer;
