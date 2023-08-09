import { createContext , useState , useContext } from "react";

const AuthContext = createContext(null);

export const AuthProvider = ({children}) => {
    const [user , setUser] = useState(null);
    // console.log(user)

    const login = (user) => {
        setUser(localStorage.getItem("user"))
    }

    const logout = () => {
        setUser(null);
        console.log("auth logout")
        // localStorage.setItem("user", "");
        localStorage.removeItem("user");
        localStorage.removeItem("node");
        localStorage.removeItem("perfCutCopyID");
        localStorage.removeItem("perfCutAction");
        
        window.location.reload();
    
    }

 return <AuthContext.Provider value={{user , login , logout}}>{children}</AuthContext.Provider>   
}
 export const useAuth = () => {
    return useContext(AuthContext);
 }

