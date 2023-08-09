import { ColorModeContext, useMode } from "./theme";
import { CssBaseline, ThemeProvider } from "@mui/material";
// import {BrowserRouter,Routes,Route} from "react-router-dom";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import MainHeader from "./HeaderComponents/MainHeader";
import TabHeaderModify from "./HeaderComponents/TabHeader";
import { DataProvider } from "./Context/DataContext";
import { AuthProvider } from "./utils/Auth";
import Login from "./Pages/Login";
import NotFound from "./Pages/NotFound";
import Administration from "./Administration/Administration";
import Admin from "./Pages/Admin";
import Documents from "./Pages/Documents";
import DashBoard from "./Pages/DashBoard";
import Search from "./Pages/Search";
import ProtectedRoute from "./utils/ProtectedRoute";

function App() {
  const [theme, colorMode] = useMode();

  return (
    <DataProvider>
      <AuthProvider>
        <ColorModeContext.Provider value={colorMode}>
          <ThemeProvider theme={theme}>
            <CssBaseline />

            <div className="app">
              {/* <Sidebar/> */}
              <main className="content">
                <Router>
                  <Routes>
                    <Route path="/" element={<Login />} />

                    <Route
                      path="/dashboard"
                      element={
                        localStorage.getItem("user") ? (
                          <DashBoard />
                        ) : (
                          <ProtectedRoute>
                            <DashBoard />
                          </ProtectedRoute>
                        )
                      }
                    />
                    <Route
                      path="/documents"
                      element={
                        localStorage.getItem("user") ? (
                          <Documents />
                        ) : (
                          <ProtectedRoute>
                            <Documents />
                          </ProtectedRoute>
                        )
                      }
                    />
                    <Route
                      path="/search"
                      element={
                        localStorage.getItem("user") ? (
                          <Search />
                        ) : (
                          <ProtectedRoute>
                            <Search />
                          </ProtectedRoute>
                        )
                      }
                    />
                    <Route
                      path="/Administration/*"
                      element={
                        localStorage.getItem("user") ? (
                          <Admin />
                        ) : (
                          <ProtectedRoute>
                            <Admin />
                          </ProtectedRoute>
                        )
                      }
                    />
                    <Route path="*" element={<NotFound />} />
                  </Routes>
                </Router>
              </main>
            </div>
          </ThemeProvider>
        </ColorModeContext.Provider>
      </AuthProvider>
    </DataProvider>
  );
}

export default App;
