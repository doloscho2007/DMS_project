const api_url = "http://localhost:8080/";

function getNodes() {
  const user = JSON.parse(localStorage.getItem("user"));
  const url = api_url + "nodes";
  return fetch(`${url}`, {
    method: "GET",
    headers: {
      Authorization: user.token,
      "Content-Type": "application/json; charset=utf-8",
    },
  });
}

function getFiles(nodeValue) {
  const user = JSON.parse(localStorage.getItem("user"));
  const url = api_url + "getFiles?nodeId=" + nodeValue;
  return fetch(`${url}`, {
    method: "GET",
    headers: {
      Authorization: user.token,
      "Content-Type": "application/json; charset=utf-8",
    },
  });
}

function downloadFile(fileId, isPdfFile) {
  const user = JSON.parse(localStorage.getItem("user"));
  const url =
    api_url + "downloadFile?fileId=" + fileId + "&pdfFile=" + isPdfFile;
  return fetch(`${url}`, {
    method: "GET",
    headers: {
      Authorization: user.token,
      "Content-Type": "application/json; charset=utf-8",
    },
  });
}

function printFile(fileId) {
  const user = JSON.parse(localStorage.getItem("user"));
  const url = api_url + "printFile?fileId=" + fileId;
  return fetch(`${url}`, {
    method: "GET",
    headers: {
      Authorization: user.token,
      "Content-Type": "application/json; charset=utf-8",
    },
  });
}

function deleteFile(fileId) {
  const user = JSON.parse(localStorage.getItem("user"));
  const url = api_url + "deleteFile?fileId=" + fileId;
  return fetch(`${url}`, {
    method: "POST",
    headers: {
      Authorization: user.token,
      "Content-Type": "application/json; charset=utf-8",
    },
  });
}

function getAllVersions(fileId) {
  const user = JSON.parse(localStorage.getItem("user"));
  const url = api_url + "getAllVersions?fileId=" + fileId;
  return fetch(`${url}`, {
    method: "GET",
    headers: {
      Authorization: user.token,
      "Content-Type": "application/json; charset=utf-8",
    },
  });
}

function extractEvents() {
  const user = JSON.parse(localStorage.getItem("user"));
  const url = api_url + "getAllEvents";
  return fetch(`${url}`, {
    method: "GET",
    headers: {
      Authorization: user.token,
      "Content-Type": "application/json; charset=utf-8",
    },
  });
}

function searchEvents(username, fromDate, toDate) {
  const user = JSON.parse(localStorage.getItem("user"));
  const url =
    api_url +
    "searchAllEvents?username=" +
    username +
    "&fromDate=" +
    fromDate +
    "&toDate=" +
    toDate;
  return fetch(`${url}`, {
    method: "GET",
    headers: {
      Authorization: user.token,
      "Content-Type": "application/json; charset=utf-8",
    },
  });
}

function addFolderName(FolderName) {
  const user = JSON.parse(localStorage.getItem("user"));
  const url = api_url + "addFolderName";

  const newFolderData = {
    parentId: localStorage.getItem("node"),
    folderName: FolderName,
  };
  return fetch(`${url}`, {
    method: "POST",
    body: JSON.stringify(newFolderData),
    headers: {
      Authorization: user.token,
      "Content-Type": "application/json; charset=utf-8",
      "Access-Control-Allow-Origin": "*",
    },
  });
}

function validateLogin(pin) {
  const url = api_url + "validateLogin";
  return fetch(`${url}`, {
    method: "POST",
    body: JSON.stringify({ pinCode: pin }),
    headers: {
      "Content-Type": "application/json; charset=utf-8",
    },
  });
}

const searchFolder = async (
  searchParam,
  fileSearchParam,
  fileName,
  folderPathName,
  fileDate1,
  fileDate2,
  creationDate1,
  creationDate2,
  isWholeWord
) => {
  let fileData = [];
  const user = JSON.parse(localStorage.getItem("user"));
  const url =
    api_url +
    "searchFolder?searchParam=" +
    searchParam +
    "&fileNameSearch=" +
    fileName +
    "&fileSearch=" +
    fileSearchParam +
    "&folderSearch=" +
    folderPathName +
    "&fileDate1=" +
    fileDate1 +
    "&fileDate2=" +
    fileDate2 +
    "&creationDate1=" +
    creationDate1 +
    "&creationDate2=" +
    creationDate2 +
    "&W=" +
    isWholeWord;
  console.log(url);

  try {
    const response = await fetch(`${url}`, {
      method: "POST",
      headers: {
        Authorization: user.token,
        "Content-Type": "application/json; charset=utf-8",
      },
      body: JSON.stringify({
        searchParam,
        fileSearchParam,
        fileName,
        folderPathName,
        fileDate1,
        fileDate2,
        creationDate1,
        creationDate2,
        isWholeWord,
      }),
    });

    const data = await response.json();
    // console.log(data)
    fileData = data;
  } catch (error) {
    console.log("error in searchFolder API:" + error);
  }

  // console.log(fileData)
  return fileData;
};

const splitFile = async (
  nodeValue,
  fileId,
  splitFileName,
  splitFromRange,
  splitToRange
) => {
  let resp;
  const user = JSON.parse(localStorage.getItem("user"));
  const url =
    api_url +
    "splitFile?nodeId=" +
    nodeValue +
    "&fileId=" +
    fileId +
    "&newFileName=" +
    splitFileName +
    "&fromRange=" +
    splitFromRange +
    "&toRange=" +
    splitToRange;
  console.log(url);

  try {
    const response = await fetch(`${url}`, {
      method: "POST",
      headers: {
        Authorization: user.token,
        "Content-Type": "application/json; charset=utf-8",
      },
    });

    console.log(response.status);
    resp = response.status;

    // const data = await response.json();
    // console.log(data);
  } catch (error) {
    console.log("error in splitFile API:" + error);
  }
  return resp;
};

const mergeFile = async (multiPartFile, nodeId, fileId) => {
  const user = JSON.parse(localStorage.getItem("user"));
  console.log(user.token);
  var formData = new FormData();
  console.log(multiPartFile);
  for (var i = 0, len = multiPartFile.length; i < len; i++) {
    formData.append("files", multiPartFile[i]);
  }

  formData.append("nodeId", nodeId);
  formData.append("fileId", fileId);
  const url = api_url + "mergeFile";
  return fetch(`${url}`, {
    method: "POST",
    body: formData,
    headers: {
      Authorization: user.token,
      // "Content-Type": 'multipart/form-data',
      // "Accept": "application/json",
      // "type": "formData"
    },
  });
};

function getAllFolders() {
  const user = JSON.parse(localStorage.getItem("user"));
  const url = api_url + "getAllFolders";
  return fetch(`${url}`, {
    method: "GET",
    headers: {
      Authorization: user.token,

      "Content-Type": "application/json; charset=utf-8",
    },
  });
}

function stampFile(fileId, stampStatus, pagePreference) {
  const user = JSON.parse(localStorage.getItem("user"));
  const url =
    api_url +
    "stampFile?fileId=" +
    fileId +
    "&stampStatus=" +
    stampStatus +
    "&pagePreference=" +
    pagePreference;
  return fetch(`${url}`, {
    method: "GET",
    headers: {
      Authorization: user.token,
      "Content-Type": "application/json; charset=utf-8",
    },
  });
}

function refreshToken() {
  const user = JSON.parse(localStorage.getItem("user"));
  const url = api_url + "refreshToken";
  return fetch(`${url}`, {
    method: "GET",
    headers: {
      Authorization: user.token,
      "Content-Type": "application/json; charset=utf-8",
    },
  });
}

function addBookmark(fileId) {
  const user = JSON.parse(localStorage.getItem("user"));
  const url = api_url + "addBookmark?fileId=" + fileId;
  return fetch(`${url}`, {
    method: "GET",
    headers: {
      Authorization: user.token,
      "Content-Type": "application/json; charset=utf-8",
    },
  });
}

function getAllBookmarkedFiles() {
  const user = JSON.parse(localStorage.getItem("user"));
  const url = api_url + "getAllBookmarkedFiles";
  return fetch(`${url}`, {
    method: "GET",
    headers: {
      Authorization: user.token,
      "Content-Type": "application/json; charset=utf-8",
    },
  });
}

function deleteBookmark(fileId) {
  const user = JSON.parse(localStorage.getItem("user"));
  const url = api_url + "deleteBookmark?fileId=" + fileId;
  return fetch(`${url}`, {
    method: "GET",
    headers: {
      Authorization: user.token,
      "Content-Type": "application/json; charset=utf-8",
    },
  });
}

function getAllCheckedOutFiles() {
  const user = JSON.parse(localStorage.getItem("user"));
  const url = api_url + "getAllCheckedOutFiles";
  return fetch(`${url}`, {
    method: "GET",
    headers: {
      Authorization: user.token,
      "Content-Type": "application/json; charset=utf-8",
    },
  });
}
function checkout(fileId) {
  const user = JSON.parse(localStorage.getItem("user"));
  const url = api_url + "checkout?fileId=" + fileId;
  return fetch(`${url}`, {
    method: "GET",
    headers: {
      Authorization: user.token,
      "Content-Type": "application/json; charset=utf-8",
    },
  });
}
const upload = async (multiPartFile, nodeId) => {
  const user = JSON.parse(localStorage.getItem("user"));
  var formData = new FormData();
  console.log(multiPartFile);
  for (var i = 0, len = multiPartFile.length; i < len; i++) {
    formData.append("files", multiPartFile[i]);
  }

  formData.append("nodeId", nodeId);
  const url = api_url + "upload1";
  return fetch(`${url}`, {
    method: "POST",
    body: formData,
    headers: {
      Authorization: user.token,
      // "Content-Type": 'multipart/form-data',
      // "Accept": "application/json",
      // "type": "formData"
    },
  });
};
const checkin = async (multiPartFile, fileId) => {
  const user = JSON.parse(localStorage.getItem("user"));
  var formData = new FormData();
  console.log(multiPartFile);

  for (var i = 0, len = multiPartFile.length; i < len; i++) {
    formData.append("file", multiPartFile[i]);
  }
  formData.append("fileId", fileId);

  const url = api_url + "checkin";
  return fetch(`${url}`, {
    method: "POST",
    body: formData,
    headers: {
      Authorization: user.token,
      // "Content-Type": "multipart/form-data",
    },
  });
};

const updateFileName = async (
  fileId,
  fileName,
  fileSearch1,
  fileSearch2,
  fileSearch3,
  createdDate
) => {
  const user = JSON.parse(localStorage.getItem("user"));
  const url =
    api_url +
    "updateFileName?fileId=" +
    fileId +
    "&fileName=" +
    fileName +
    "&fileSearch1=" +
    fileSearch1 +
    "&fileSearch2=" +
    fileSearch2 +
    "&fileSearch3=" +
    fileSearch3 +
    "&createdDate=" +
    createdDate;
  return fetch(`${url}`, {
    method: "POST",
    headers: {
      Authorization: user.token,
      "Content-Type": "application/json; charset=utf-8",
    },
  });
};

const getUsers = async () => {
  const user = JSON.parse(localStorage.getItem("user"));
  const url = api_url + "getUsers";
  return fetch(`${url}`, {
    method: "GET",
    headers: {
      Authorization: user.token,
      "Content-Type": "application/json; charset=utf-8",
    },
  });
};

const deleteUser = async (userId) => {
  const user = JSON.parse(localStorage.getItem("user"));
  const url = api_url + "deleteUser?userId=" + userId;

  return fetch(`${url}`, {
    method: "POST",
    headers: {
      Authorization: user.token,
      "Content-Type": "application/json; charset=utf-8",
    },
  });
};

const updateUserDetails = async (loginId, userName, status, userGroup, pin) => {
  const user = JSON.parse(localStorage.getItem("user"));
  const url =
    api_url +
    "updateUserDetails?loginId=" +
    loginId +
    "&userName=" +
    userName +
    "&status=" +
    status +
    "&userGroup=" +
    userGroup +
    "&pin=" +
    pin;

  return fetch(`${url}`, {
    method: "POST",
    headers: {
      Authorization: user.token,
      "Content-Type": "application/json; charset=utf-8",
    },
  });
};

const addUserDetails = async (userName, status, userGroup, pin) => {
  const user = JSON.parse(localStorage.getItem("user"));
  const url =
    api_url +
    "addUserDetails?userName=" +
    userName +
    "&status=" +
    status +
    "&userGroup=" +
    userGroup +
    "&pin=" +
    pin;

  return fetch(`${url}`, {
    method: "POST",
    headers: {
      Authorization: user.token,
      "Content-Type": "application/json; charset=utf-8",
    },
  });
};

const getAllUserGroup = async () => {
  const user = JSON.parse(localStorage.getItem("user"));
  const url = api_url + "usergroup";

  return fetch(`${url}`, {
    method: "GET",
    headers: {
      Authorization: user.token,
      "Content-Type": "application/json; charset=utf-8",
    },
  });
};

const createUserGroup = async (
  userGroupName,
  userGroupStatus,
  userGroupDescription
) => {
  const user = JSON.parse(localStorage.getItem("user"));
  const url =
    api_url +
    "usergroup?userGroupName=" +
    userGroupName +
    "&userGroupStatus=" +
    userGroupStatus +
    "&userGroupDescription=" +
    userGroupDescription;

  return fetch(`${url}`, {
    method: "POST",
    headers: {
      Authorization: user.token,
      "Content-Type": "application/json; charset=utf-8",
    },
  });
};

const updateUserGroup = async (
  userGroupId,
  userGroupName,
  userGroupStatus,
  userGroupDescription
) => {
  const user = JSON.parse(localStorage.getItem("user"));
  const url =
    api_url +
    "usergroup?userGroupId=" +
    userGroupId +
    "&userGroupName=" +
    userGroupName +
    "&userGroupStatus=" +
    userGroupStatus +
    "&userGroupDescription=" +
    userGroupDescription;

  return fetch(`${url}`, {
    method: "PUT",
    headers: {
      Authorization: user.token,
      "Content-Type": "application/json; charset=utf-8",
    },
  });
};

const deleteUserGroup = async (userGroupId) => {
  const user = JSON.parse(localStorage.getItem("user"));
  const url = api_url + "usergroup?userGroupId=" + userGroupId;

  return fetch(`${url}`, {
    method: "DELETE",
    headers: {
      Authorization: user.token,
      "Content-Type": "application/json; charset=utf-8",
    },
  });
};

const fileOperation = async (fileId, nodeId, action) => {
  const user = JSON.parse(localStorage.getItem("user"));
  const url =
    api_url +
    "fileOperation?fileId=" +
    fileId +
    "&nodeId=" +
    nodeId +
    "&action=" +
    action;

  return fetch(`${url}`, {
    method: "POST",
    headers: {
      Authorization: user.token,
      "Content-Type": "application/json; charset=utf-8",
    },
  });
};

const deleteFolder = async (folderId) => {
  const user = JSON.parse(localStorage.getItem("user"));
  const url = api_url + "deleteFolder?folderId=" + folderId;

  return fetch(`${url}`, {
    method: "POST",
    headers: {
      Authorization: user.token,
      "Content-Type": "application/json; charset=utf-8",
    },
  });
};

const updateFolder = async (folderId, folderName) => {
  const user = JSON.parse(localStorage.getItem("user"));
  const url =
    api_url + "updateFolder?folderId=" + folderId + "&folderName=" + folderName;

  return fetch(`${url}`, {
    method: "POST",
    headers: {
      Authorization: user.token,
      "Content-Type": "application/json; charset=utf-8",
    },
  });
};

export const DMSAPI = {
  validateLogin,
  getNodes,
  getFiles,
  downloadFile,
  printFile,
  deleteFile,
  getAllVersions,
  extractEvents,
  searchEvents,
  addFolderName,
  upload,
  searchFolder,
  getAllFolders,
  refreshToken,
  splitFile,
  mergeFile,
  stampFile,
  addBookmark,
  getAllBookmarkedFiles,
  deleteBookmark,
  getAllCheckedOutFiles,
  checkout,
  checkin,
  updateFileName,
  getUsers,
  deleteUser,
  updateUserDetails,
  addUserDetails,
  getAllUserGroup,
  createUserGroup,
  updateUserGroup,
  deleteUserGroup,
  fileOperation,
  deleteFolder,
  updateFolder,
};
