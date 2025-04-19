import {Outlet} from "react-router-dom";

export const BlogListPage = (props) => {
    return (
        <div>
            <h1>My BlogList</h1>
            <Outlet/>
        </div>
    )
}