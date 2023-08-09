import React from "react";
import { useGlobalContext } from "../Context/DataContext";

const SearchPdfViewer = () => {
  const { pdfFilePath } = useGlobalContext();
  var pdfPath = pdfFilePath.pdfFilePath;
  return (
    <div className="shadow doc-height border border-dark me-1">
      <iframe
        src="//127.0.0.1/F1V1s"
        title="pdf"
        style={{ height: "36.4rem", width: "100%" }}
      ></iframe>
    </div>
  );
};

export default SearchPdfViewer;
