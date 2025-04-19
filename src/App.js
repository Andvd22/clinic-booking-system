import './App.css';
import {Link, Route, BrowserRouter as Router, Routes} from "react-router-dom";
import {HomePage} from "./components/features/home/HomePage";
import {AboutPage} from "./components/features/about/AboutPage";
import {BlogListPage} from "./components/features/BlogList/BlogListPage";
import {Blog} from "./components/features/BlogList/Blog";
import {BlogList} from "./components/features/BlogList/BlogList";
import {AppLayout} from "./components/AppLayout";

function App() {
  return (
        <Router>
            <AppLayout />
        </Router>

  );
}

export default App;
