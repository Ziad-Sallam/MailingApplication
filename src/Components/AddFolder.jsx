// import '../Pages/add-contact.css'
import axios from "axios";
import {useEffect, useState} from "react";
import {useNavigate, useParams} from "react-router-dom";
import {RxCross2} from "react-icons/rx";
import ErrorMsg from "./ErrorMsg.jsx";


function AddFolder(props) {
    const user = useParams().user;
    const navigate = useNavigate();

    const [name, setName] = useState('');
    const [errorMsg, setErrorMsg] = useState(false)
    const [error,setError] = useState('')



    async function addFolder() {
        const params = {

            name : name,
        }
        console.log(params)
        try{
            await axios.post(`http://localhost:8080/api/users/addFolder/${user}`, name)
            // eslint-disable-next-line react/prop-types
            props.onClick()
            navigate("/" + user +`/folder/${params.name}`)
            window.location.reload();


        }catch (error){
            console.log(error)
            setErrorMsg(true)
            setError("folder already exists")
        }
    }

    return (
        <div className="contact-container">
            {/* eslint-disable-next-line react/prop-types */}
            <button className={"btn btn-outline-danger btn-sm close"} onClick={props.onClick}>
                <RxCross2 />
            </button>

            <div >
                <label htmlFor={"name"}>Folder Name:  </label>
                <input type={"text"} id={"name"} value={name} onChange={(e) => setName(e.target.value)} />
            </div>
            <button className={"btn btn-primary"} onClick={addFolder}>Add Folder</button>
            {errorMsg && (<ErrorMsg message={error}/>)}
        </div>
    )
}

export default AddFolder;