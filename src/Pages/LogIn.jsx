
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.bundle.min';
import react from "../assets/react.svg"
import './login.css'
import {useNavigate} from 'react-router-dom'


function LogIn() {

    const navigate = useNavigate();
    function handleLogin() {
        navigate('/')
    }

    return (
        <>
            <form className="form-signin" onSubmit={handleLogin}>
                <img className="mb-4" src={react} alt="" width="72"
                     height="72"/>
                <h1 className="h3 mb-3 font-weight-normal">Please sign in</h1>
                {/*<label htmlFor="inputEmail" className="sr-only">Email address</label>*/}
                <input type="email" id="inputEmail" className="form-control" placeholder="Email address" required
                       autoFocus/>
                {/*<label htmlFor="inputPassword" className="sr-only">Password</label>*/}
                <input type="password" id="inputPassword" className="form-control last" placeholder="Password" required/>

                <button className="btn btn-lg btn-primary btn-block col-12" type="submit">Sign in</button>
                <a href={'/register'}>Don&apos;t have an account.</a>
                <p className="mt-5 mb-3 text-muted">&copy; The programmers</p>
            </form>
        </>
    )

}

export default LogIn