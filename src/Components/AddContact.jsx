// import '../Pages/add-contact.css'
import axios from "axios";
import {useEffect, useState} from "react";
import {useParams} from "react-router-dom";


function AddContact() {
    const user = useParams().user;

    const [email, setEmail] = useState("");
    const [name, setName] = useState("");



    async function addContact() {
        const params = {

                name : name,
            userEmails : [email]
        }
        console.log(params)

        await axios.post(`http://localhost:8080/api/users/addContact/${user}`, params)
    }

    return (
        <div className="contact-container">
            <div >
                <label htmlFor={"name"}>Name:  </label>
                <input type={"text"} id={"name"} value={name} onChange={(e) => setName(e.target.value)} />
            </div>
            <div>
                <label htmlFor={"email"}>Email: </label>
                <input type={"email"} id={"email"} value={email} onChange={(e) => setEmail(e.target.value)} />
            </div>
            <button className={"btn"} onClick={addContact}>Add Contact</button>
        </div>
    )
}

export default AddContact;