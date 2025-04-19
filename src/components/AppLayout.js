import {useState} from "react";
import {Link, Route, Routes, useNavigate} from "react-router-dom";
import {HomePage} from "./features/home/HomePage";
import {AboutPage} from "./features/about/AboutPage";
import {BlogListPage} from "./features/BlogList/BlogListPage";
import {BlogList} from "./features/BlogList/BlogList";
import {Blog} from "./features/BlogList/Blog";
import {LoginPage} from "./features/login/LoginPage";
import {Stats} from "./features/Stats";

export const AppLayout = () =>{
    const [user, setUser] = useState();
    const navigate = useNavigate();
    function LogOut(){
        setUser(null);
        navigate("/");
    }
    return (
        <>
        <nav style={{margin:10}}>
            <Link to="/" style={{padding : 5}}>Home</Link>
            <Link to={"/about"} style={{padding : 5}}>About</Link>
            <Link to={"/blog"} style={{padding : 5}}>Blog</Link>
            <span>|</span>
            {user && <Link to={"/stats"} style={{padding : 5}}>Stats</Link>}
            {!user && <Link to="/login" style={{padding : 5}}>Login</Link>}
            {user && <span onClick={LogOut} style={{padding : 5}}>Logout</span>}
        </nav>
            <Routes>
                <Route path="/" element={<HomePage />} />
                <Route path="/about" element={<AboutPage />} />
                <Route path="/login" element={<LoginPage onLogin={setUser} />} />
                <Route path ="/stats" element={<Stats user={user} />} />
                <Route path="/blog" element={<BlogListPage />} >
                    <Route index element={<BlogList />} />
                    <Route path=":slug" element = {<Blog />} />
                </Route>

            </Routes>
            </>
    )
}