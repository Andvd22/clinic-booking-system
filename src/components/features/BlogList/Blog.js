import {Link, useParams} from "react-router-dom";
import BlogPosts from "../../../BlogPosts";
export const Blog = () => {
    const {slug} = useParams();
    const blog = BlogPosts[slug];
    if(!blog){
        return (<div>
            <Link to='/blog'>Return</Link>
            <p>Doesn't exist</p>
        </div>)
    }
    const {title ,description} = blog;
    return(
        <div style={{padding : 30}}>
            <Link to='/blog'>Return</Link>
            <h3>{title}</h3>
            <p>{description}</p>
        </div>
    )
}