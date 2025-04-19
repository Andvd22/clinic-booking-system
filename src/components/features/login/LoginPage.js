import {useState} from "react";
import {useNavigate} from "react-router-dom";

export const LoginPage = ({onLogin}) =>{
    const [credentials, setCredentials] = useState({});
    const navigate = useNavigate();
    function handleLogin() {
        if(credentials.username==='admin' && credentials.password==='1'){
            onLogin&&onLogin({username:credentials.username});
            navigate('/stats');
        }
    }
    return(
        <div style={{paddingTop:'50px'}}> <br/>
            <span>UserName</span>
        <input type="text" onChange={(e) =>{
            setCredentials({...credentials, username: e.target.value});
        }}/><br/>
            <span>Password</span>
        <input type="text" onChange={(e) =>{setCredentials({...credentials, password: e.target.value});}} /><br/>
            <button onClick={handleLogin}>Login</button><br/>
        </div>
    )
}