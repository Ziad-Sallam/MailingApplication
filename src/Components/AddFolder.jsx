// import '../Pages/add-contact.css'
import axios from "axios";
import {useEffect, useState} from "react";
import {useParams} from "react-router-dom";


function AddFolder() {
    const user = useParams().user;

    const [name, setName] = useState("");



    async function addFolder() {
        const params = {

            name : name,
        }
        console.log(params)

         await axios.post(`http://localhost:8080/api/users/addFolder/${user}`, name)
    }

    return (
        <div className="contact-container">
            <div >
                <label htmlFor={"name"}>Name:  </label>
                <input type={"text"} id={"name"} value={name} onChange={(e) => setName(e.target.value)} />
            </div>

            <button className={"btn"} onClick={addFolder}>Add Folder</button>
        </div>
    )
}

export default AddFolder;