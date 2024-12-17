// import '../Pages/add-contact.css'
import axios from "axios";
import {useEffect, useState} from "react";
import {useParams} from "react-router-dom";
import {RxCross2} from "react-icons/rx";


function AddFolder(props) {
    const user = useParams().user;

    const [name, setName] = useState('');



    async function addFolder() {
        const params = {

            name : name,
        }
        console.log(params)

         await axios.post(`http://localhost:8080/api/users/addFolder/${user}`, name)
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
        </div>
    )
}

export default AddFolder;