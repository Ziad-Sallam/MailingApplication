
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.bundle.min';
import react from "../assets/react.svg"
import './login.css'
import {useNavigate} from 'react-router-dom'
import {useState} from "react";


function LogIn() {
    const [email, setEmail] = useState('')
    const [password, setPassword] = useState('')
    const [passwordConfirm, setPasswordConfirm] = useState('')
    const [username, setUsername] = useState('')

    const navigate = useNavigate();
    function handlePasswordChange(e) {
        const {id, value} = e.target;
        if (id === 'inputPassword') {
            setPassword(value);

        } else if (id === 'inputPasswordConfirm') {
            setPasswordConfirm(value);
        }
        else if (id === 'inputEmail') {
            setEmail(value);
        }
        else if (id === 'inputUsername') {
            setUsername(value);
        }
    }
    function handleRegister(e) {
        e.preventDefault();
        if (passwordConfirm !== password) {
            console.log("different passwords")
        }
        else {
            console.log("password change")
            navigate("/")
        }
    }


    return (
        <>
            <form className="form-signin" onSubmit={handleRegister}>
                <img className="mb-4" src={react} alt="" width="72"
                     height="72"/>
                <h1 className="h3 mb-3 font-weight-normal">Welcome !</h1>
                {/*<label htmlFor="inputEmail" className="sr-only">Email address</label>*/}
                <input type="email"
                       id="inputEmail"
                       className="form-control"
                       placeholder="Email address"
                       required
                       autoFocus/>
                <input type="name"
                       id="inputUsername"
                       className="form-control middle"
                       placeholder="Username"
                       required
                       autoFocus/>

                {/*<label htmlFor="inputPassword" className="sr-only">Password</label>*/}
                <input
                    type="password"
                    id="inputPassword"
                    className="form-control middle"
                    placeholder="Password"
                    required
                    onChange={handlePasswordChange}
                    value={password}/>
                <input
                    type="password"
                    id="inputPasswordConfirm"
                    className="form-control last"
                    placeholder="Repeat Password"
                    required
                    onChange={handlePasswordChange}
                    value={passwordConfirm}>
                </input>

                <button
                    className="btn btn-lg btn-primary btn-block col-12"
                    type="submit"> Create Account
                </button>
                <a href={'/login'}>have an account?</a>
                <p className="mt-5 mb-3 text-muted">&copy; The programmers</p>
            </form>
        </>
    )

}

export default LogIn