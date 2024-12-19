
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.bundle.min';
import react from "../assets/react.svg"
import './login.css'
import {useNavigate} from 'react-router-dom'
import {useState} from "react";
import axios from "axios";
import ErrorMsg from "../Components/ErrorMsg.jsx";




function LogIn() {

    const [errorMsg, setErrorMsg] = useState(false)
    const [error,setError] = useState('')
    function handleEntryChange(e) {
        if (e.target.id === "inputEmail"){
            setEmail(e.target.value)

        }
        else if (e.target.id === "inputPassword"){
            setPassword(e.target.value)
        }
    }
    const [email, setEmail] = useState('')
    const [password, setPassword] = useState('')

    const navigate = useNavigate();

    async function handleLogin(e) {
        e.preventDefault();

        const params = {
            email: email,
            password: password,
        };
        console.log(email);
        console.log(password);
        try {
            const response = await axios.get("http://localhost:8080/api/users/signin", {
                params: { ...params }
            });
            console.log(response.data);
            const user = response.data

            // Optionally navigate after successful login
            navigate('/' + user.email + '/folder/inbox');
        } catch (error) {
            setErrorMsg(true)
            if(error.response.status === 404){

                setError("Email not found");
            }
            else if (error.response.status === 400){
                setError("Wrong Password");
            }

            console.error('There was an error logging in:', error);
        }
    }


    return (
        <>
            <form className="form-signin" onSubmit={handleLogin}>
                <img className="mb-4" src={react} alt="" width="72"
                     height="72"/>
                <h1 className="h3 mb-3 font-weight-normal">Please sign in</h1>
                {/*<label htmlFor="inputEmail" className="sr-only">Email address</label>*/}
                <input type="email" id="inputEmail" className="form-control" placeholder="Email address" required
                       autoFocus value={email} onChange={handleEntryChange}/>
                {/*<label htmlFor="inputPassword" className="sr-only">Password</label>*/}
                <input type="password" id="inputPassword" className="form-control last" placeholder="Password" required value={password}
                onChange={handleEntryChange}/>

                <button className="btn btn-lg btn-primary btn-block col-12" type="submit">Sign in</button>
                {errorMsg && <ErrorMsg message={error}/>}
                <a href={'/register'}>Don&apos;t have an account.</a>
                <p className="mt-5 mb-3 text-muted">&copy; The programmers</p>
            </form>
        </>
    )

}

export default LogIn